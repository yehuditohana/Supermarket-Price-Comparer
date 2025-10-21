package com.example.mystore.enrichment.categorization;

import com.example.mystore.database.entities.Item;
import com.example.mystore.services.updateServices.ItemUpdateService;
import com.example.mystore.utils.CsvUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * ItemPythonClassifier is responsible for exporting items to a CSV file,
 * triggering an external Python script for classification, and updating item categories in the database.
 *
 * The workflow includes:
 * - Exporting items to CSV
 * - Running Python classifier
 * - Reading the classified output and updating database records
 *
 * Important:
 * This class does NOT train a machine learning model.
 * It uses a pre-trained machine learning model (in Python) to classify items.
 */
@Component
public class ItemPythonClassifier {
    private static final Logger logger = LoggerFactory.getLogger(ItemPythonClassifier.class);
    private final ItemUpdateService itemUpdateService;

    @Value("${product.classifier.model.path}")
    private String modelPath;

    @Value("${csv.input.path}")
    private String inputCsvPath;

    @Value("${csv.output.path}")
    private String outputCsvPath;

    @Value("${python.script.path}")
    private String pythonScriptPath;

    public ItemPythonClassifier(ItemUpdateService itemUpdateService) {
        this.itemUpdateService = itemUpdateService;
    }

    public String getInputCsvPath() {
        return inputCsvPath;
    }

    public String getOutputCsvPath() {
        return outputCsvPath;
    }

    /**
     * Exports all items (ID and name) to a CSV file
     * to be processed by the external Python classifier.
     */
    public void exportItemsForClassification() {
        logger.info("Exporting items for classification to CSV: {}", inputCsvPath);
        List<Item> items = itemUpdateService.findAllItems();

// Generates a CSV file at the specified inputCsvPath containing item data.
// Each row in the CSV includes the item ID and item name, extracted from the 'items' list.
        try {
            CsvUtils.writeToCsv(
                    inputCsvPath,
                    new String[]{"item_id", "item_name"},
                    items,
                    item -> {
                        if (item.getItemID() != null && item.getItemName() != null) {
                            return new String[]{item.getItemID(), item.getItemName()};
                        } else {
                            return null;
                        }
                    }
            );
            logger.info("Finished exporting items for classification.");
        } catch (IOException e) {
            logger.error("Error exporting items to CSV: {}", e.getMessage(), e);
        }
    }

    /** Executes the external Python script for item classification.
     * The script reads the exported input CSV (containing item IDs and names),
     * applies classification using a pre-trained machine learning model,
     * and generates a new output CSV at the specified outputCsvPath.
     * The output CSV contains 4 columns:
     * - item_id (String): The ID of the item (as received from the Java export).
     * - generalCategory (String): The assigned general category.
     * - subCategory (String): The assigned subcategory.
     * - specificCategory (String): The assigned specific category.
     * If the Python script fails (non-zero exit code), error messages are logged.
     */
    public void classifyItems() {
        logger.info("Starting item classification process...");

        try {
            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, inputCsvPath, outputCsvPath, modelPath);
            Process process = pb.start(); //run python script
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                logger.error("Python script failed with exit code: {}", exitCode);

                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    logger.error("Python Error: {}", line);
                }
                return;
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error running Python script: {}", e.getMessage(), e);
            return;
        }

        logger.info("Python script completed successfully.");
    }

    /**
     * Reads the classification results from the output CSV,
     * updates the corresponding items in the database, and cleans up temporary files.
     */
    @Transactional
    public void applyClassificationFromCsv() {
        List<Item> itemsToUpdate = ItemCategoryCsvUpdater.updateCategoriesFromCsv(outputCsvPath, itemUpdateService);

        if (!itemsToUpdate.isEmpty()) {
            itemUpdateService.saveAllItems(itemsToUpdate);
            logger.info("Saved {} updated items to database.", itemsToUpdate.size());
        } else {
            logger.warn("No items found to update.");
        }

    }
}

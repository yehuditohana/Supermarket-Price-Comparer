package com.example.mystore.enrichment.categorization;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**ItemCategorizer coordinates the item classification process
 * using two different classification strategies.
 * This class triggers:
 * - Classification by the external Python model
 * - Correction/fix of categories from a predefined CSV file
 * Note: Responsible also for cleaning up temporary CSV files after processing.
 */
@Component
public class ItemCategorizer {
    private static final Logger logger = LoggerFactory.getLogger(ItemCategorizer.class);
    private final ItemPythonClassifier itemPythonClassifier;
    private final FixCategories fixCategories;

    public ItemCategorizer(ItemPythonClassifier itemPythonClassifier, FixCategories fixCategories) {
        this.itemPythonClassifier = itemPythonClassifier;
        this.fixCategories = fixCategories;
    }

    /**Main method to categorize items by applying all classification strategies.*/
    @Transactional
    public void categorizeItems() {
        logger.info("Starting full item categorization process...");

        // Classify using the external Python model
        itemPythonClassifier.exportItemsForClassification();
        itemPythonClassifier.classifyItems();
        itemPythonClassifier.applyClassificationFromCsv();

        //Apply additional fixes from another CSV
        fixCategories.fixClassificationFromCsv();

        //Cleanup temporary files
        cleanupTemporaryFiles();

        logger.info("Finished item categorization process.");
    }

    private void cleanupTemporaryFiles() {
        try {
            Files.deleteIfExists(Paths.get(itemPythonClassifier.getInputCsvPath()));
            Files.deleteIfExists(Paths.get(itemPythonClassifier.getOutputCsvPath()));
            logger.info("Temporary CSV files deleted successfully.");
        } catch (IOException e) {
            logger.warn("Failed to delete temporary files: {}", e.getMessage());
        }
    }
}
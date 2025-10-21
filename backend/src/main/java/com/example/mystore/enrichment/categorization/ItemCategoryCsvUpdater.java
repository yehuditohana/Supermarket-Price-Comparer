package com.example.mystore.enrichment.categorization;

import com.example.mystore.database.entities.Item;
import com.example.mystore.services.updateServices.ItemUpdateService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
 import java.util.Optional;
/** Utility class responsible for updating item categories based on a CSV file.
 * The CSV file is expected to have the following columns:
 * 1. itemId (String)
 * 2. generalCategory (String)
 * 3. subCategory (String)
 * 4. specificCategory (String)
 * This utility reads each row, fetches the corresponding item from the database,
 * and updates its category fields accordingly, and collects the updated items into a list.
 * NOTE:
 *  This method only prepares and returns a list of categorized items.
 *  It does NOT persist any changes to the database.
 */
public class ItemCategoryCsvUpdater {

    private static final Logger logger = LoggerFactory.getLogger(ItemCategoryCsvUpdater.class);

    public static List<Item> updateCategoriesFromCsv(String csvPath, ItemUpdateService itemUpdateService) {
        logger.info("Updating item categories from CSV: {}", csvPath);
        List<Item> updatedItems = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvPath), StandardCharsets.UTF_8))) {
            String[] header = csvReader.readNext();
            if (header == null) {
                logger.error("No header found in CSV file.");
                return updatedItems;
            }
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                if (row.length < 4) { // Skip invalid or incomplete rows
                    continue;
                }
                String itemId = row[0];
                String general = row[1];
                String sub = row[2];
                String specific = row[3];

                Optional<Item> opt = itemUpdateService.findById(itemId);
                if (opt.isPresent()) {
                    Item item = opt.get();
                    item.setGeneralCategory(general);
                    item.setSubCategory(sub);
                    item.setSpecificCategory(specific);
                    updatedItems.add(item);
                }
            }
        } catch (IOException | CsvValidationException e) {
            logger.error("Error reading items from CSV: {}", e.getMessage(), e);

        }
        return updatedItems;

    }
}
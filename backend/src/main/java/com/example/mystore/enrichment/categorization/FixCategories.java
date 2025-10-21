package com.example.mystore.enrichment.categorization;

import com.example.mystore.database.entities.Item;
import com.example.mystore.services.updateServices.ItemUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * FixCategories is responsible for correcting item categorizations after initial classification.
 *
 * Responsibilities:
 * - Reads a CSV file containing updated category mappings for items.
 * - Updates the categories of existing items in the database based on the CSV.
 * - Saves the corrected item records back into the database.
 *
 * Typically used for manual adjustments and refinements following automatic classification processes.
 */

@Component
public class FixCategories {
    private static final Logger logger = LoggerFactory.getLogger(FixCategories.class);
    private final ItemUpdateService itemUpdateService;

    @Value("${csv.fix.category.path}")
    private String outputCsvPath;


    public FixCategories(ItemUpdateService itemUpdateService) {
        this.itemUpdateService = itemUpdateService;
    }

    public void fixClassificationFromCsv() {
        List<Item> itemsToUpdate = ItemCategoryCsvUpdater.updateCategoriesFromCsv(outputCsvPath, itemUpdateService);

        if (!itemsToUpdate.isEmpty()) {
            itemUpdateService.saveAllItems(itemsToUpdate);
            logger.info("Saved {} updated items to database.", itemsToUpdate.size());
        } else {
            logger.warn("No items found to update.");
        }
    }
}

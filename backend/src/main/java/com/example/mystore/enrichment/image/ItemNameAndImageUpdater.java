package com.example.mystore.enrichment.image;

import com.example.mystore.database.entities.Item;
import com.example.mystore.dto.common.ImageResultDTO;
import com.example.mystore.enrichment.categorization.ItemPythonClassifier;
import com.example.mystore.services.updateServices.ItemUpdateService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Handles the enrichment of items with names and images from external catalogs.
 * This component performs the following steps:
 * 1. Loads all items from the database that currently do not have an image.
 * 2. Searches for matching names and images in the Rami Levy catalog.
 * 3. For items not found in Rami Levy, searches in the Shufersal catalog.
 * 4. For items still missing images, searches for additional images without updating names.
 * 5. Updates the items with the new information (name and/or image) if found.
 * 6. Saves all enriched items back to the database in a single batch update.
 */
@Component
public class ItemNameAndImageUpdater {

    private static final Logger logger = LoggerFactory.getLogger(ItemPythonClassifier.class);
    private final ItemUpdateService itemUpdateService;
    private final RamiLevyCatalog ramiLevyCatalog ;
    private final ShufersalCatalog shufersalCatalog;
    private final AddingMoreImagesUrl addingMoreImagesUrl;


    public ItemNameAndImageUpdater(ItemUpdateService itemUpdateService, RamiLevyCatalog ramiLevyCatalog, ShufersalCatalog shufersalCatalog, AddingMoreImagesUrl addingMoreImagesUrl) {
        this.itemUpdateService = itemUpdateService;
        this.ramiLevyCatalog = ramiLevyCatalog;
        this.shufersalCatalog = shufersalCatalog;
        this.addingMoreImagesUrl = addingMoreImagesUrl;
    }

    // Main function to enrich items with names and images from external catalogs
    public void updateItemsWithImages() {
        logger.info("Starting enrichment process...");

        // Load all items that currently do not have an image
        List<Item> itemsWithoutImage = itemUpdateService.getItemsWithoutImage();

        if (itemsWithoutImage.isEmpty()) {
            logger.info("No items without images, exiting...");
            return;
        }

        // Extract item IDs for searching in external catalogs
        List<String> itemIds = itemsWithoutImage.stream()
                .map(Item::getItemID)
                .toList();

        // Search for matching names and images in the catalogs
        List<ImageResultDTO> ramiLevyResults = ramiLevyCatalog.searchInRamiLevyCatalogById(itemIds).orElse(Collections.emptyList());
        List<ImageResultDTO> shufersalResults = shufersalCatalog.searchInShufersalCatalogById(itemIds).orElse(Collections.emptyList());
        List<ImageResultDTO> additionalResults = addingMoreImagesUrl.searchInAdditionalImagesByIds(itemIds).orElse(Collections.emptyList());

        applyResultsAndUpdateDB(itemsWithoutImage, ramiLevyResults, shufersalResults, additionalResults);

        logger.info("Finished updating items with images and names.");
    }

    // Applies the enrichment results (names and images) to the given list of items and updates the database.
    // The process:
    // First tries to enrich from Rami Levy results.
    // If not found, tries to enrich from Shufersal results.
    // If still not found, adds an image only from additional sources (without changing the name).
    // Only items that have been successfully updated are saved back to the database
    @Transactional
    public void applyResultsAndUpdateDB(List<Item> items, List<ImageResultDTO> ramiLevyResults, List<ImageResultDTO> shufersalResults, List<ImageResultDTO> additionalResults) {
        Map<String, ImageResultDTO> ramiLevyMap = toMap(ramiLevyResults);
        Map<String, ImageResultDTO> shufersalMap = toMap(shufersalResults);
        Map<String, ImageResultDTO> additionalMap = toMap(additionalResults);

        List<Item> updatedItems = new ArrayList<>();

        for (Item item : items) {
            boolean updated = false;
            String itemId = item.getItemID();

            ImageResultDTO result = ramiLevyMap.getOrDefault(itemId, shufersalMap.get(itemId));

            if (result != null) {
                if (result.getItemName() != null && !result.getItemName().isBlank()) {
                    item.setItemName(result.getItemName());
                    updated = true;
                }
                if (result.getImageUrl() != null && !result.getImageUrl().isBlank()) {
                    item.setImageUrl(result.getImageUrl());
                    updated = true;
                }
            } else {
                ImageResultDTO imgOnly = additionalMap.get(itemId);
                if (imgOnly != null && imgOnly.getImageUrl() != null && !imgOnly.getImageUrl().isBlank()) {
                    item.setImageUrl(imgOnly.getImageUrl().trim());
                    updated = true;
                }
            }

            if (updated) {
                updatedItems.add(item);
            }
        }

        if (!updatedItems.isEmpty()) {
            itemUpdateService.saveAllItems(updatedItems);
            logger.info("{} items updated successfully.", updatedItems.size());
        } else {
            logger.warn("No items were updated.");
        }
    }

    // Converts a list of ImageResultDTO objects into a map,
    //  where each item ID is mapped to its corresponding result.
    private Map<String, ImageResultDTO> toMap(List<ImageResultDTO> list) {
        Map<String, ImageResultDTO> map = new HashMap<>();
        for (ImageResultDTO dto : list) {
            map.put(dto.getItemId(), dto);
        }
        return map;
    }

}

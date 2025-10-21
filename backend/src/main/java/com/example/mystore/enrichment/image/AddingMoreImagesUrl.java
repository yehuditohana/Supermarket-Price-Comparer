package com.example.mystore.enrichment.image;

import com.example.mystore.dto.common.ImageResultDTO;
import com.example.mystore.services.apiServices.ItemService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Service for searching additional images for items that might not exist in the main catalogs.
 * This service uses a local CSV file ("moreImagesUrl.csv") that maps item IDs to image URLs,
 * primarily for weighted products like fresh meat, fish, fruits, and vegetables.
 */
@Component
public class AddingMoreImagesUrl {
    private static final Logger logger = LoggerFactory.getLogger(AddingMoreImagesUrl.class);
    private final ItemService itemService;

    @Value("${csv.image.more}")
    private  String csvFilePath;

    public AddingMoreImagesUrl(ItemService itemService) {
        this.itemService = itemService;
    }

    // Searches for additional images for a given list of item IDs.
    // Only matches based on the item ID, without updating the item name
    // (The SCV file have only tow column "item_id" , "image_url".
    public Optional<List<ImageResultDTO>>  searchInAdditionalImagesByIds(List<String> itemIdsList) {
        Set<String> idSet = new HashSet<>(itemIdsList);

        List<ImageResultDTO> results = new ArrayList<>();

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(new FileInputStream(csvFilePath), StandardCharsets.UTF_8)
        );) {
            // Skip the header row (expected: item_id, image_url)
            reader.readNext();

            String[] line;

            while ((line = reader.readNext()) != null) {

                //Skip on invalid lines
                if (line.length < 2) {
                    logger.debug("Skipping malformed line: {}", Arrays.toString(line));
                    continue;
                }
                String id = line[0].trim();

                // Skip this line if the ID is not in the requested set
                if (!idSet.contains(id)){
                    logger.debug("Skipping line: {} -> ID not found in requested IDs set.", Arrays.toString(line));
                    continue;
                }

                // Extract the image URL and add a new result without a item name
                String imageUrl = line[1].trim();
                results.add(new ImageResultDTO(id,null,  imageUrl));
            }
        } catch (IOException | CsvValidationException e) {
            logger.warn("Failed to read catalog.: {}", csvFilePath, e);
            return Optional.empty();
        }
        logger.info("Fetched {} items from More images url catalog", results.size());
        return Optional.of(results);
    }
}

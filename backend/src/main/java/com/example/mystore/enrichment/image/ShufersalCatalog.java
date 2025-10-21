package com.example.mystore.enrichment.image;

import com.example.mystore.dto.common.ImageResultDTO;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Service for searching more items information (name and image URL)
 * in the Shufersal catalog CSV file.
 */
@Service
public class ShufersalCatalog {

    private static final Logger logger = LoggerFactory.getLogger(ShufersalCatalog.class);

    @Value("${csv.image.shufersal.catalog}")
    private  String csvFilePath;

    //Searches for items in the Shufersal catalog by their IDs.
    //For each found item, returns its ID, name, and image URL.
    public Optional<List<ImageResultDTO>> searchInShufersalCatalogById(List<String> itemIdsList)  {

        Set<String> idSet = new HashSet<>(itemIdsList);  // For faster lookup

        List<ImageResultDTO> results = new ArrayList<>();// Items from the database for which an image and name were found

        //read the file only once
        try (CSVReader reader = new CSVReader(
                new InputStreamReader(new FileInputStream(csvFilePath), StandardCharsets.UTF_8)
        );) {
            reader.readNext(); // skip on header
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 3) {
                    logger.debug("Skipping malformed line: {}", Arrays.toString(line));
                    continue; //invalid line
                }
                String id = line[0].trim();

                // If the ID is not in the set of requested IDs, skip this line
                if (!idSet.contains(id)){
                    logger.debug("Skipping line: {} -> ID not found in requested IDs set.", Arrays.toString(line));
                    continue;
                }

                String imageUrl = line[1].trim();
                String name = line[2].trim();

                // Create a new ImageResultDTO with the found ID, name, and image URL, and add it to the results list
                results.add(new ImageResultDTO(id, name, imageUrl));
            }
        } catch (IOException | CsvValidationException e) {
            logger.warn("Failed to read Shufersal catalog file: {}", csvFilePath, e);
            return Optional.empty();
        }
        logger.info("Fetched {} items from Shufersal catalog", results.size());
        return Optional.of(results);
    }
}

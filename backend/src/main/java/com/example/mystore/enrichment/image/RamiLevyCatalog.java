package com.example.mystore.enrichment.image;
import com.example.mystore.dto.common.ImageResultDTO;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RamiLevyCatalog {
    private static final Logger logger = LoggerFactory.getLogger(RamiLevyCatalog.class);


    @Value("${csv.image.rami-levi.catalog}")
    private  String csvFilePath;
    private static final String START_IMAGE_URL = "https://img.rami-levy.co.il/product/";
    private static final String END_IMAGE_URL = "/small.jpg";

    public Optional<List<ImageResultDTO>>  searchInRamiLevyCatalogById(List<String> itemIdsList)  {

        Set<String> idSet = new HashSet<>(itemIdsList);

        List<ImageResultDTO> results = new ArrayList<>();

        //read the file only once
        try (CSVReader reader = new CSVReader(
                new InputStreamReader(new FileInputStream(csvFilePath), StandardCharsets.UTF_8)
        );) {
            reader.readNext(); // skip on header
            String[] line;
            while ((line = reader.readNext()) != null) {

                //invalid line
                if (line.length < 2) {
                    logger.debug("Skipping malformed line: {}", Arrays.toString(line));
                    continue;
                }
                String id = line[0].trim();
                if (!idSet.contains(id)){
                    logger.debug("Skipping line: {} -> ID not found in requested IDs set.", Arrays.toString(line));
                    continue;
                }
                String name = line[1].trim();
                // Generate a fixed image URL based on the item ID using Rami Levy's website format
                String imageUrl = START_IMAGE_URL + id + END_IMAGE_URL;

                // Create a new ImageResultDTO with the found ID, name, and image URL, and add it to the results list
                results.add(new ImageResultDTO(id, name, imageUrl));
            }
        } catch (IOException | CsvValidationException e) {
            logger.warn("Failed to read RamiLevy catalog file: {}", csvFilePath, e);
            return Optional.empty();
        }
        logger.info("Fetched {} items from RamiLevy catalog", results.size());
        return Optional.of(results);
    }

}

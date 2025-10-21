package com.example.mystore.database.seeding;

import com.example.mystore.database.entities.Item;
import com.example.mystore.dto.xml.ItemXmlDTO;
import com.example.mystore.dto.xml.StoreXmlDTO;
import com.example.mystore.services.seederServices.ItemSeederService;
import com.example.mystore.services.seederServices.StoreSeederService;
import com.example.mystore.xml.ItemSAXHandler;
import com.example.mystore.xml.StoreIdentifierSAXHandler;
import com.example.mystore.utils.DirectoryUtils;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ItemSeeder is responsible for initializing and importing item data into the database from XML files.
 *
 * Responsibilities:
 * - Parses item and store information from XML files using SAX parsers.
 * - Validates that the store exists before importing its items.
 * - Cleans and maps raw XML data into standardized Item entities.
 * - Buffers items and flushes them in batches to improve database performance.
 * - Clears processed XML files after successful import.
 *
 * Ensures efficient and validated loading of initial item data during system setup.
 */

@Component
public class ItemSeeder {
    private static final Logger logger = LoggerFactory.getLogger(ItemSeeder.class);
    private static final int FLUSH_THRESHOLD = 1000; // Buffer size threshold to flush data to the database

    private final ItemSeederService itemSeederService;
    private final StoreSeederService storeSeederService;

    public ItemSeeder(ItemSeederService itemSeederService, StoreSeederService storeSeederService) {
        this.itemSeederService = itemSeederService;
        this.storeSeederService = storeSeederService;
    }

    public void initializeItems(String directoryPath) {
        logger.info("Starting item initialization from directory: {}", directoryPath);

        List<Item> buffer = new ArrayList<>();// Buffer to store items before saving to the database


        // Get the list of XML files in the provided directory
        File[] xmlFiles = DirectoryUtils.getXmlFilesFromDirectory(directoryPath, logger);
        if (xmlFiles == null) {
            return;
        }

        // Process each XML file
        for (File xmlFile : xmlFiles) {
            logger.info("Processing XML file: {}", xmlFile.getName());
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            try {
                SAXParser saxParser = saxParserFactory.newSAXParser();

                // Parse store identifier to check if the store exists in the database
                StoreIdentifierSAXHandler storeIdentifierSAXHandler = new StoreIdentifierSAXHandler();
                saxParser.parse(xmlFile, storeIdentifierSAXHandler);
                StoreXmlDTO storeXmlDTO = storeIdentifierSAXHandler.getStore();

                if (storeXmlDTO == null) {
                    continue;
                }
                // If the store does not exist, skip processing the file
                if (!storeSeederService.storeExists(
                        storeXmlDTO.getChainID(), storeXmlDTO.getSubChainID(),storeXmlDTO.getStoreNumber())) {
                    continue;
                }

                // Parse item data from the XML file
                ItemSAXHandler itemSAXHandler = new ItemSAXHandler();
                saxParser.parse(xmlFile, itemSAXHandler);
                List<ItemXmlDTO> itemXmlDTOList = itemSAXHandler.getDtos();
                logger.info("Found {} items in file: {}", itemXmlDTOList.size(), xmlFile.getName());


                List<Item> cleanItems = itemSeederService.createCleanItems(itemXmlDTOList);// Create clean Item entities from parsed DTOs

                buffer.addAll(cleanItems);// Add the items to the buffer


                // When the buffer size reaches the flush threshold, save items to the database
                if (buffer.size() >= FLUSH_THRESHOLD) {
                    int inserted = itemSeederService.saveNewItems(buffer);
                    logger.info(" Flushed {} items to DB", inserted);
                    buffer.clear();
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                logger.error("Error parsing file {}: {}", xmlFile.getName(), e.getMessage(), e);
            }
        }
        // Final flush if there are any remaining items in the buffer
        if (!buffer.isEmpty()) {
            // Final flush of any remaining items
            int inserted = itemSeederService.saveNewItems(buffer);
            logger.info("Final flush: {} items inserted", inserted);
            buffer.clear();
        }

        logger.info("Finished initializing items.");
    }
}
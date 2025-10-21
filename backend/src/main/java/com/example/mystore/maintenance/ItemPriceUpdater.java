package com.example.mystore.maintenance;

import com.example.mystore.database.entities.ItemPrice;
import com.example.mystore.database.entities.Store;
import com.example.mystore.dto.xml.StoreXmlDTO;
import com.example.mystore.services.seederServices.StoreSeederService;
import com.example.mystore.services.updateServices.ItemPriceUpdateService;
import com.example.mystore.xml.ItemPriceSAXHandler;
import com.example.mystore.xml.StoreIdentifierSAXHandler;
import com.example.mystore.dto.xml.ItemPriceXmlDTO;
import com.example.mystore.utils.DirectoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
/**
 * The `ItemPriceUpdater` class is responsible for updating item prices in the database from XML files.
 * This process involves:
 * 1. Parsing XML files containing item price data.
 * 2. Verifying if the store exists in the database by checking its store number, chain ID, and sub-chain ID.
 * 3. Mapping the parsed data into `ItemPrice` entities.
 * 4. Flushing the data to the database in batches once a certain threshold is reached (defined by `FLUSH_THRESHOLD`).
 * The `flushBuffer` method is responsible for updating existing prices and inserting new prices into the database.
 */
public class ItemPriceUpdater {
    private static final int FLUSH_THRESHOLD = 1000;

    private static final Logger logger = LoggerFactory.getLogger(ItemPriceUpdater.class);

    private final ItemPriceUpdateService itemPriceSeederService ;
    private final StoreSeederService storeSeederService;

    @Autowired
    public ItemPriceUpdater(ItemPriceUpdateService itemPriceSeederService, StoreSeederService storeSeederService) {
        this.itemPriceSeederService = itemPriceSeederService;
        this.storeSeederService = storeSeederService;
    }

    // Updates item prices by reading XML files from the specified directory and processing the data.
    public void updatePrices(String directoryPath) {
        logger.info("Starting to update prices from directory: {}", directoryPath);

        // Buffer to store item prices before saving to the database
        List<ItemPrice> buffer = new ArrayList<>();

        // Get all XML files from the specified directory
        File[] xmlFiles = DirectoryUtils.getXmlFilesFromDirectory(directoryPath, logger);
        if (xmlFiles == null) {
            return;
        }
        // Process each XML file
        for (File xmlFile : xmlFiles) {
            logger.info("Processing file: {}", xmlFile.getName());
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            try {
                SAXParser saxParser = saxParserFactory.newSAXParser();

                // Parse the store identifier
                StoreIdentifierSAXHandler storeIdentifierSAXHandler = new StoreIdentifierSAXHandler();
                saxParser.parse(xmlFile, storeIdentifierSAXHandler);
                StoreXmlDTO storeXmlDTO = storeIdentifierSAXHandler.getStore();

                if (storeXmlDTO == null) {
                    logger.warn("No store found in file: {}. Skipping file.", xmlFile.getName());
                    continue;
                }
                // Check if the store exists in the database
                Optional<Store> optionalStore = storeSeederService.findStoreId(storeXmlDTO.getChainID(), storeXmlDTO.getSubChainID(), storeXmlDTO.getStoreNumber());

                if (optionalStore.isEmpty()) {
                    logger.warn("Store not found in DB (ChainID: {}, SubChainID: {}, StoreNumber: {}). Skipping file.",
                            storeXmlDTO.getChainID(), storeXmlDTO.getSubChainID(), storeXmlDTO.getStoreNumber());
                    continue;
                }
                // Get the store from the database
                Store existingStore = optionalStore.get();

                logger.info("Store found: StoreID={} StoreNumber={}", existingStore.getStoreID(), existingStore.getStoreNumber());

                // Parse item price data from the XML file
                ItemPriceSAXHandler itemPriceSAXHandler = new ItemPriceSAXHandler();
                saxParser.parse(xmlFile, itemPriceSAXHandler);
                List<ItemPriceXmlDTO> dtos = itemPriceSAXHandler.getItemPriceList();
                logger.info("Found {} item prices in file: {}", dtos.size(), xmlFile.getName());

                if (dtos.isEmpty()) {
                    logger.warn("No item prices found in file: {}. Skipping save.", xmlFile.getName());
                    continue;
                }

                // Map the parsed DTOs to ItemPrice entities
                List<ItemPrice> itemPrices = itemPriceSeederService.mapDtosToItemPrice(dtos , existingStore);

                buffer.addAll(itemPrices);

                // If buffer size reaches the flush threshold, flush data to the database
                if (buffer.size() >= FLUSH_THRESHOLD) {
                    flushBuffer(buffer);
                    buffer.clear();
                }

                logger.info("Finished processing file: {}", xmlFile.getName());

            } catch (ParserConfigurationException | IOException | SAXException e) {
                logger.error("Error processing file {}: {}", xmlFile.getName(), e.getMessage(), e);
            }
        }
        // Final flush for any remaining item prices in the buffer
        if (!buffer.isEmpty()) {
            flushBuffer(buffer);
            buffer.clear();
        }

        logger.info("Completed updating item prices from all files.");

    }

    //This method updates existing prices and inserts new ones into the database.
    private void flushBuffer(List<ItemPrice> buffer) {
        logger.info("Flushing buffer of size: {}", buffer.size());
        int updated = itemPriceSeederService.updateExistingPrices(buffer);
        int inserted = itemPriceSeederService.insertNewPrices(buffer);
        logger.info("Updated {} prices, Inserted {}", updated, inserted);
    }

}

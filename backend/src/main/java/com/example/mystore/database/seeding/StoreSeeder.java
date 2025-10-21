package com.example.mystore.database.seeding;

import com.example.mystore.database.cleaning.CityNameCleaner;
import com.example.mystore.database.entities.Store;
import com.example.mystore.dto.xml.StoreXmlDTO;
import com.example.mystore.services.seederServices.StoreSeederService;
import com.example.mystore.xml.StoreSAXHandler;
import com.example.mystore.utils.DirectoryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class StoreSeeder {
    private static final Logger logger = LoggerFactory.getLogger(StoreSeeder.class);
    private final StoreSeederService storeSeederService;
    private final CityNameCleaner cityNameCleaner;

    // Path to the directory containing the XML files (configured via application.properties)
    @Value("${stores.files.directory}")
    private String directoryPath;

    public StoreSeeder(StoreSeederService storeSeederService, CityNameCleaner cityNameCleaner) {
        this.storeSeederService = storeSeederService;

        this.cityNameCleaner = cityNameCleaner;
    }

    /**
     * Initializes store data by reading XML files from a specified directory.
     * It processes each file, parses the store data, and saves the valid stores to the database.
     * For each XML file, the process is as follows:
     * 1. Parse the XML file to extract store data.
     * 2. Verify if the associated chain exists in the database.
     * 3. Save the valid stores to the database, skipping the ones that don't match any chain.
     */
    public void initializeStores() {
        logger.info("Starting initializeStores...");

        // Get all XML files from the specified directory
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
                StoreSAXHandler storeSAXHandler = new StoreSAXHandler();
                saxParser.parse(xmlFile, storeSAXHandler);

                List<StoreXmlDTO> dtos = storeSAXHandler.getDtos();
                logger.info(" Found {} stores in file: {}", dtos.size(), xmlFile.getName());

                // Save stores to the database if the associated chain exists
                int savedCounter = storeSeederService.saveAllIfChainExists(dtos);
                int skippedCounter = dtos.size() - savedCounter;

                logger.info("Finished processing file: {}. Saved: {} stores, Skipped: {} stores.", xmlFile.getName(), savedCounter, skippedCounter);

            } catch (ParserConfigurationException | SAXException | IOException e) {
                logger.error("Error processing file {}: {}", xmlFile.getName(), e.getMessage(), e);
            }
        }
        logger.info("Finished initializing stores.");
    }
    public void cleanStores(){
        List<Store> stores = storeSeederService.getAllStores();
        for(Store store: stores){
            // Clean the city name for each store
            String cleanCity = cityNameCleaner.cleanCityName(store.getStoreCity());
           if(cleanCity != null){
               store.setStoreCity(cleanCity);
           }
        }
        // Save the cleaned stores back to the database
        storeSeederService.saveAllStores(stores);
    }

}
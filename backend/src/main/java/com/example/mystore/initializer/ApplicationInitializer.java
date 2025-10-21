package com.example.mystore.initializer;

import com.example.mystore.database.entities.ProcessName;
import com.example.mystore.enrichment.categorization.FixCategories;
import com.example.mystore.enrichment.image.ItemNameAndImageUpdater;
import com.example.mystore.database.seeding.StoreSeeder;
import com.example.mystore.enrichment.image.AddingMoreImagesUrl;
import com.example.mystore.services.ProcessTrackerService;
import com.example.mystore.database.seeding.AllSeeder;
import com.example.mystore.downloader.model.FileType;
import com.example.mystore.downloader.engine.PriceFileDownloader;
import com.example.mystore.services.updateServices.ItemUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);
    private final ProcessTrackerService processTrackerService;
    private final PriceFileDownloader priceFileDownloader;
    private final AllSeeder allSeeder;


    @Value("${pricesFull.files.directory}")
    private String pricesFullFilesDirectory;

    public ApplicationInitializer(ProcessTrackerService processTrackerService, PriceFileDownloader priceFileDownloader, AllSeeder allSeeder) {
        this.processTrackerService = processTrackerService;
        this.priceFileDownloader = priceFileDownloader;
        this.allSeeder = allSeeder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {

         logger.info("!!!Application Initialization Started!!!");

         if (processTrackerService.isProcessCompleted(ProcessName.INITIAL_LOAD)) {
             logger.info("Initial Load has already been completed. Skipping initialization.");
             return;
         }

         if (!processTrackerService.isProcessCompleted(ProcessName.PRICEFULL_LOAD)) {
             logger.info("Downloading PriceFull files...");
             priceFileDownloader.downloadAndProcessFiles(pricesFullFilesDirectory, FileType.PRICEFULL, 24);
             processTrackerService.markProcessCompleted(ProcessName.PRICEFULL_LOAD);
         } else {
             logger.info("PriceFull Load has already been completed. Skipping PriceFull download.");
         }

         if (!processTrackerService.isProcessCompleted(ProcessName.TABLE_SEEDING)) {
              allSeeder.run(pricesFullFilesDirectory); // Initialize tables
              processTrackerService.markProcessCompleted(ProcessName.TABLE_SEEDING);
         } else{
              logger.info("Initializing tables has been completed. Skipping table initialization.");
         }


          processTrackerService.markProcessCompleted(ProcessName.INITIAL_LOAD);//mark the initialized completed successfully
          logger.info("!!!Application Initialization Completed Successfully.!!!");

        } catch (Exception e) {
            logger.warn("XXX Initialization failed: XXX" + e.getMessage());
        }
    }
}
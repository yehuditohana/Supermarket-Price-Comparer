package com.example.mystore.database.seeding;

import com.example.mystore.database.entities.ProcessName;
import com.example.mystore.enrichment.categorization.ItemCategorizer;
import com.example.mystore.enrichment.image.ItemNameAndImageUpdater;
import com.example.mystore.services.ProcessTrackerService;
import com.example.mystore.maintenance.ItemPriceUpdater;
import com.example.mystore.services.updateServices.ItemUpdateService;
import com.example.mystore.utils.DirectoryUtils;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AllSeeder is responsible for the initial seeding and enrichment of core database tables.
 *
 * Responsibilities:
 * - Initialize Chains, Stores, and Items tables if not already completed.
 * - Categorize items after import.
 * - Enrich items with images and clean names.
 * - Update item prices from the provided PriceFull file.
 * - Update lowest and highest item prices after price updates.
 * - Clear imported files after processing.
 *
 * Ensures the application starts with complete and properly processed data.
 */
@Component
public class AllSeeder {
    private static final Logger logger = LoggerFactory.getLogger(AllSeeder.class);
    private  final ChainSeeder chainSeeder;
    private final StoreSeeder storeSeeder;
    private final ItemSeeder itemSeeder;
    private final ItemPriceUpdater itemPriceUpdater;
    private final ProcessTrackerService processTrackerService;
    private final ItemCategorizer itemCategorizer;
    private final ItemNameAndImageUpdater itemNameAndImageUpdater;
    private final ItemUpdateService itemUpdateService;

    public AllSeeder(ChainSeeder chainSeeder, StoreSeeder storeSeeder, ItemSeeder itemSeeder, ItemPriceUpdater itemPriceUpdater, ProcessTrackerService processTrackerService, ItemCategorizer itemCategorizer, ItemNameAndImageUpdater itemNameAndImageUpdater, ItemUpdateService itemUpdateService) {
        this.chainSeeder = chainSeeder;
        this.storeSeeder = storeSeeder;
        this.itemSeeder = itemSeeder;
        this.itemPriceUpdater = itemPriceUpdater;
        this.processTrackerService = processTrackerService;
        this.itemCategorizer = itemCategorizer;
        this.itemNameAndImageUpdater = itemNameAndImageUpdater;
        this.itemUpdateService = itemUpdateService;
    }

    public void run(String pathPriceFullFile) {
        logger.info("AllSeeder started...");

        // Initialize Chains
        if (!processTrackerService.isProcessCompleted(ProcessName.CHAIN_TABLE_INIT)) {
            logger.info("Initializing Chains...");
            chainSeeder.initializeChains();
            processTrackerService.markProcessCompleted(ProcessName.CHAIN_TABLE_INIT);
            logger.info("Chains initialized successfully.");
        } else {
            logger.info("Chains already initialized. Skipping.");
        }

        // Initialize Stores
        if (!processTrackerService.isProcessCompleted(ProcessName.STORE_TABLE_INIT)) {
            logger.info("Initializing Stores...");
            storeSeeder.initializeStores();
            storeSeeder.cleanStores();
            processTrackerService.markProcessCompleted(ProcessName.STORE_TABLE_INIT);
            logger.info("Stores initialized successfully.");
        } else {
            logger.info("Stores already initialized. Skipping.");
        }

        // Initialize Items
        if (!processTrackerService.isProcessCompleted(ProcessName.ITEM_TABLE_INIT)) {
            logger.info("Initializing Items...");
            itemSeeder.initializeItems(pathPriceFullFile);
            logger.info("Items initialized successfully.");

            // Export Items for Categorization
            logger.info("Starting Item Categorization...");
            itemCategorizer.categorizeItems();
            logger.info("Item Categorization completed successfully.");

            // Enrich Items with Images and Clean Names
            logger.info("Starting Item Name and Image Enrichment...");
            itemNameAndImageUpdater.updateItemsWithImages();
            logger.info("Item Name and Image Enrichment completed successfully.");
            processTrackerService.markProcessCompleted(ProcessName.ITEM_TABLE_INIT);
        } else {
            logger.info("Items already initialized. Skipping.");
        }

        // Initialization of itemPrice table is done on PriceFull files (only once).
        // Then daily update with scheduler (updated according to Price files - only changed prices)
        if (!processTrackerService.isProcessCompleted(ProcessName.PRICE_UPDATE)) {
            logger.info("Updating Item Prices...");
            itemPriceUpdater.updatePrices(pathPriceFullFile);
            logger.info("Item prices updated successfully.");
            processTrackerService.markProcessCompleted(ProcessName.PRICE_UPDATE);

            logger.info("Updating Item Price Ranges (lowest and highest prices)...");
            itemUpdateService.updateItemPriceRange();
            logger.info("Item Price Ranges updated successfully.");

        } else {
            logger.info("ItemPrice already initialized. Skipping PRICE_UPDATE.");
        }
        DirectoryUtils.clearDirectory(pathPriceFullFile, logger); // delete priceFull XML file from directory

        logger.info("AllSeeder completed successfully.");
    }
}

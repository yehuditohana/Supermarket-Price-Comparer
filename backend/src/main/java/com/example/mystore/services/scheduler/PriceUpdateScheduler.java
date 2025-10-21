package com.example.mystore.services.scheduler;
import com.example.mystore.database.entities.ProcessName;
import com.example.mystore.downloader.engine.PriceFileDownloader;
import com.example.mystore.downloader.model.FileType;
import com.example.mystore.maintenance.ItemPriceUpdater;
import com.example.mystore.services.ProcessTrackerService;
import com.example.mystore.services.updateServices.ItemUpdateService;
import com.example.mystore.utils.DirectoryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The `PriceUpdateScheduler` class is responsible for automating the daily price update process.
 * This scheduled task performs the following steps every day at 02:00 AM:
 * 1. Downloads the latest "Price" files from external sources into a local directory
 *    (the directory path is configured via the `prices.files.directory` property).
 * 2. Processes the downloaded XML files to update item prices in the database (`item_price` table).
 * 3. Clears the download directory after all files have been processed successfully.
 * This ensures that the pricing data in the system is refreshed daily without manual intervention.
 */
@Component
public class PriceUpdateScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PriceUpdateScheduler.class);

    @Value("${prices.files.directory}")
    private String pricesFilesDirectory;
    private final ItemPriceUpdater itemPriceUpdater;
    private final ItemUpdateService itemUpdateService;
    private final ProcessTrackerService processTrackerService;
    private final PriceFileDownloader priceFileDownloader;

    public PriceUpdateScheduler(ItemPriceUpdater itemPriceUpdater, ItemUpdateService itemUpdateService, ProcessTrackerService processTrackerService, PriceFileDownloader priceFileDownloader) {
        this.itemPriceUpdater = itemPriceUpdater;
        this.itemUpdateService = itemUpdateService;
        this.processTrackerService = processTrackerService;
        this.priceFileDownloader = priceFileDownloader;
    }


    /** Scheduled task that updates item prices daily.
     * Runs every day at 02:00 AM.
     */
    @Scheduled(cron = "00 00 02 * * *")
    public void scheduledPriceUpdate() {
        logger.info("Starting daily price update...");
        try {
            // Download the latest Price files from the last 24 hours.
            logger.info("Downloading Price files...");
            priceFileDownloader.downloadAndProcessFiles(pricesFilesDirectory, FileType.PRICE, 24);

            // Update the database with new prices
            itemPriceUpdater.updatePrices(pricesFilesDirectory);
            logger.info("Daily price update completed successfully.");


            // Update the item's price range (lowest and highest prices)
            logger.info("Updating Item Price Ranges (lowest and highest prices)...");
            itemUpdateService.updateItemPriceRange();
            logger.info("Item Price Ranges updated successfully.");

            processTrackerService.markProcessCompleted(ProcessName.PRICE_UPDATE);
        } catch (Exception e) {
            logger.error("Error during scheduled price update:", e);
            // Mark the process as uncompleted if an error occurs
            processTrackerService.markProcessUnCompleted(ProcessName.PRICE_UPDATE); //To track price updates
        }
        // Clear the directory after processing all files
        DirectoryUtils.clearDirectory(pricesFilesDirectory, logger);
    }
}
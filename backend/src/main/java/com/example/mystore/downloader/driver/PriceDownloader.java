package com.example.mystore.downloader.driver;

import com.example.mystore.downloader.model.FileMetadata;
import com.example.mystore.downloader.model.FileType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

/**
 * PriceDownloader defines the contract for downloading price-related files
 * from supermarket websites using automated browser interactions (WebDriver).
 *
 * Responsibilities:
 * - Handle login into the target system.
 * - Retrieve available files of a specific type and within a specified timeframe.
 *
 * Implementations must provide mechanisms for authentication and file retrieval.
 */

public interface PriceDownloader {

    /**
     * Logs into the supermarket's price system using the provided WebDriver.
     *
     * @param driver the WebDriver instance to use
     * @param wait the WebDriverWait instance to handle waiting
     */
    void login(WebDriver driver, WebDriverWait wait);

    /**
     * Fetches the list of available price files from the supermarket's website.
     *
     * @param driver the WebDriver instance to use
     * @param wait the WebDriverWait instance to handle waiting
     * @param desiredFileType the type of file to be fetched (e.g., PRICE, PRICEFULL, PROMO, STORE)
     * @param timeFrameInHours the maximum number of hours back to look for files (e.g., 5 means files uploaded in the last 5 hours)
     * @return a list of available files (metadata for each file)
     */
    List<FileMetadata> fetchAvailableFiles(WebDriver driver, WebDriverWait wait , FileType desiredFileType , int timeFrameInHours) throws InterruptedException;
}
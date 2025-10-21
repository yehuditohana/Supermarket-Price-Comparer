package com.example.mystore.downloader.engine;

import com.example.mystore.downloader.driver.CookieProvider;
import com.example.mystore.downloader.driver.PriceDownloader;
import com.example.mystore.downloader.model.FileMetadata;
import com.example.mystore.downloader.model.FileType;
import com.example.mystore.downloader.io.GzToXmlConverter;
import com.example.mystore.downloader.io.HttpDownloadService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * PriceFileDownloader orchestrates the process of downloading and processing price files from various supermarket websites.
 *
 * Responsibilities:
 * - Initializes a WebDriver session for browser automation.
 * - Iterates over configured PriceDownloader implementations to login and fetch available files.
 * - Downloads the fetched files, optionally using session cookies if available.
 * - Converts downloaded .gz files into .xml format for further processing.
 * - Organizes downloaded files into structured directories by downloader type.
 * Supports automated retrieval, validation, and conversion of pricing data.
 */
@Service
public class PriceFileDownloader {
    @Autowired
    private final ChromeOptions chromeOptions;
    private final List<PriceDownloader> priceDownloaders;
    private final HttpDownloadService httpDownloadService;
    private final GzToXmlConverter gzToXmlConverter;
    private static final int WEB_DRIVER_WAIT_TIMEOUT_SECONDS = 15;

    private static final Logger logger = LoggerFactory.getLogger(PriceFileDownloader.class);



    @Autowired
    public PriceFileDownloader(ChromeOptions chromeOptions, List<PriceDownloader> priceDownloaders,
                               HttpDownloadService httpDownloadService,
                               GzToXmlConverter gzToXmlConverter) {
        this.chromeOptions = chromeOptions;
        this.priceDownloaders = priceDownloaders;
        this.httpDownloadService = httpDownloadService;
        this.gzToXmlConverter = gzToXmlConverter;
    }

    public void downloadAndProcessFiles(String baseDirectory ,FileType desiredFileType, int timeFrameInHours) {

        WebDriver driver = new ChromeDriver(chromeOptions);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WEB_DRIVER_WAIT_TIMEOUT_SECONDS));

        try {
            for (PriceDownloader downloader : priceDownloaders) {
                String filesDirectory = prepareDirectoryForDownloader(baseDirectory, downloader);
                downloader.login(driver, wait);
                List<FileMetadata> files = downloader.fetchAvailableFiles(driver, wait, desiredFileType, timeFrameInHours);

                for (FileMetadata file : files) {
                    downloadFile(file, filesDirectory, downloader);
                }
                convertGzFiles(filesDirectory);
            }
        } catch (Exception e) {
            logger.error("Failed to download and process files: {}", e.getMessage(), e);
        } finally {
            driver.quit();
   }
 }

    private void downloadFile(FileMetadata file, String filesDirectory, PriceDownloader downloader) {
        Path targetPath = Paths.get(filesDirectory, file.getFileName());
        Set<org.openqa.selenium.Cookie> cookies = null;

        if (downloader instanceof CookieProvider cookieProvider) {
            cookies = cookieProvider.getCookies();
        }

        try {
            httpDownloadService.downloadFile(file.getDownloadUrl(), targetPath, cookies);
            logger.info(" Downloaded file: {}", file.getFileName());
        } catch (Exception e) {
            logger.error("Failed to download file: {}", file.getFileName(), e);
            throw new RuntimeException("Failed to download file: " + file.getFileName(), e);
        }
    }

//Prepares the directory where downloaded files will be stored for a specific downloader.
//The method builds a subdirectory path based on the downloader's class name,
//ensures the directory exists (creating it if necessary), and returns the directory path.
private String prepareDirectoryForDownloader(String baseDirectory, PriceDownloader downloader) {
    String subDirName = downloader.getClass().getSimpleName().toLowerCase().replace("pricedownloader", "");
    Path filesDirectory = Paths.get(baseDirectory, subDirName);

    if (!Files.exists(filesDirectory)) {
        try {
            Files.createDirectories(filesDirectory);
            logger.info("Created directory for downloader: {}", filesDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create files directory: " + filesDirectory, e);
        }
    } else {
        logger.info(" Directory already exists for downloader: {}", filesDirectory);
    }

    return filesDirectory.toString();
}

    private Set<String> getExistingFilenames(String filesDirectory) {
        Set<String> filenames = new HashSet<>();
        File folder = new File(filesDirectory);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                filenames.add(file.getName());
            }
        }
        return filenames;
    }

    private void convertGzFiles(String filesDirectory) {
        File folder = new File(filesDirectory);
        File[] files = folder.listFiles();
        int successCount = 0;
        int failCount = 0;

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".gz")) {
                    String originalName = file.getName().replaceFirst("\\.gz$", "");
                    if (!originalName.endsWith(".xml")) {
                        originalName += ".xml";
                    }
                    File xmlFile = new File(filesDirectory, originalName);
                    //File xmlFile = new File(filesDirectory, file.getName().replace(".gz", ".xml"));
                    boolean success = gzToXmlConverter.convert(file, xmlFile);

                    if (success) {
                        successCount++;
                    } else {
                        failCount++;
                    }

                    boolean deleted = file.delete();
                    if (!deleted) {
                        logger.warn("⚠️ Failed to delete original GZ file: {}", file.getName());
                    }
                }
            }
        }
        logger.info("✅ Conversion Summary: {} succeeded, {} failed.", successCount, failCount);
    }
}

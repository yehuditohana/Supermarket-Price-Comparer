package com.example.mystore.downloader.driver;

import com.example.mystore.downloader.model.FileMetadata;
import com.example.mystore.downloader.model.FileType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VictoryPriceDownloader automates the process of retrieving price-related files from the Victory price file website.
 *
 * Responsibilities:
 * - Navigates to the Victory price page.
 * - Fetches and filters available files based on file type (Price, PriceFull) and current upload date.
 * - Ensures that only files related to the Victory chain (identified by chain ID) are processed.
 * - Extracts metadata (file name, link, upload date) for valid files.
 *
 * Used for downloading pricing and promotional data specific to Victory stores.
 */

@Service("victoryDownloader")
public class VictoryPriceDownloader implements PriceDownloader {
    private static final Logger logger = LoggerFactory.getLogger(VictoryPriceDownloader.class);


    @Value("${prices.victory.base-url}")
    private String baseUrl;

    @Override
    public void login(WebDriver driver, WebDriverWait wait) {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("a")));
        logger.info("Victory page loaded.");
    }

    @Override
    public List<FileMetadata> fetchAvailableFiles(WebDriver driver, WebDriverWait wait, FileType desiredFileType, int timeFrameInHours) {
        List<FileMetadata> allFiles  = new ArrayList<>();
        for (int dayOffset = 0; dayOffset <= 1; dayOffset++) {
            LocalDate dateToSearch = LocalDate.now().minusDays(dayOffset);
            performDateSearch(driver, wait, dateToSearch);
            List<FileMetadata> filesForDate = extractFilesFromPage(driver, desiredFileType, timeFrameInHours);
            allFiles.addAll(filesForDate);
        }

        return allFiles;
    }

    private void performDateSearch(WebDriver driver, WebDriverWait wait, LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        logger.info("Searching for files on date: {}", formattedDate);

        WebElement dateInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("MainContent_txtDate")));
        dateInput.clear();
        dateInput.sendKeys(formattedDate);

        WebElement searchButton = driver.findElement(By.id("MainContent_btnSearch"));
        searchButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("download_content")));
    }
    private List<FileMetadata> extractFilesFromPage(WebDriver driver, FileType desiredFileType, int timeFrameInHours) {
        List<FileMetadata> files = new ArrayList<>();
        Set<String> seenFileNames = new HashSet<>();
        List<WebElement> links = driver.findElements(By.cssSelector("a[href$='.gz']"));

        for (WebElement link : links) {
            try {
                String href = link.getAttribute("href");
                if (href == null) continue;

                String fileName = href.substring(href.lastIndexOf("/") + 1);
                if (!fileName.contains("7290696200003")) continue;

                FileType actualFileType = determineFileType(fileName);
                if (actualFileType != desiredFileType) continue;

                LocalDateTime uploadDate = extractDateTimeFromFileName(fileName);
                if (uploadDate == null) continue;

                long hoursAgo = Duration.between(uploadDate, LocalDateTime.now()).toHours();
                if (hoursAgo <= timeFrameInHours && !seenFileNames.contains(fileName)) {
                    files.add(new FileMetadata(fileName, href, desiredFileType, uploadDate));
                    seenFileNames.add(fileName);
                    logger.info(" Added file: {}", fileName);
                } else {
                    logger.debug(" Skipped file ({} hours old): {}", hoursAgo, fileName);
                }

            } catch (Exception e) {
                logger.warn(" Error parsing link: {}", e.getMessage());
            }
        }

        return files;
    }


    private FileType determineFileType(String fileName) {
        if (fileName == null) return FileType.UNKNOWN;
        if (fileName.startsWith("PriceFull")) return FileType.PRICEFULL;
        if (fileName.startsWith("Price")) return FileType.PRICE;
        if (fileName.startsWith("Promo")) return FileType.PROMO;
        if (fileName.startsWith("Store")) return FileType.STORE;
        return FileType.UNKNOWN;
    }

    private LocalDateTime extractDateTimeFromFileName(String fileName) {
        try {
            Pattern pattern = Pattern.compile("-(\\d{12})-");
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                String dateTimePart = matcher.group(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
                return LocalDateTime.parse(dateTimePart, formatter);
            }
        } catch (Exception e) {
            logger.warn("Failed to parse date from file: {}", fileName);
        }
        return null;
    }

}

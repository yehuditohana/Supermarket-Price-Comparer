package com.example.mystore.downloader.driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.mystore.downloader.model.FileMetadata;
import com.example.mystore.downloader.model.FileType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ShufersalPriceDownloader automates the process of fetching price-related files from Shufersal's website.
 *
 * Responsibilities:
 * - Navigates to the Shufersal download page.
 * - Fetches and parses available files based on their type (Price or PriceFull).
 * - Filters files based on upload time and type.
 * - Handles pagination to retrieve files from multiple pages.
 *
 * Used for downloading price files for further processing.
 */
@Service("shufersalDownloader")
@Component
public class ShufersalPriceDownloader implements PriceDownloader {

    @Value("${prices.shufersal.base-url}")
    private String baseUrl;
    private static final Logger logger = LoggerFactory.getLogger(ShufersalPriceDownloader.class);

    private static final String TABLE_XPATH = "//table[@class='webgrid']";
    private static final String FILE_ROW_XPATH = TABLE_XPATH + "//tr[position()>1]";
    private static final String DOWNLOAD_LINK_XPATH = ".//td[1]//a";
    private static final String FILE_TYPE_XPATH = ".//td[4]";
    private static final String NEXT_PAGE_XPATH = "//a[contains(text(),'>')]";
    private static final String TABLE_IN_CONTAINER_XPATH = "//div[@id='gridContainer']/table";

    @Override
    public void login(WebDriver driver, WebDriverWait wait) {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(TABLE_XPATH)));
        logger.info("Shufersal login page loaded.");
    }

    @Override
    public List<FileMetadata> fetchAvailableFiles(WebDriver driver, WebDriverWait wait, FileType desiredFileType, int timeFrameInHours) {
        List<FileMetadata> files = new ArrayList<>();
        Set<String> seenFileNames = new HashSet<>();
        boolean hasNext = true;
        int page = 1;

        while (hasNext) {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(TABLE_XPATH)));
                List<WebElement> rows = driver.findElements(By.xpath(FILE_ROW_XPATH));
                logger.info("Page {}: {} rows found.", page, rows.size());

                for (WebElement row : rows) {
                    try {
                        WebElement link = row.findElement(By.xpath(DOWNLOAD_LINK_XPATH));
                        String href = link.getAttribute("href");
                        String rawFileName = href.substring(href.lastIndexOf("/") + 1);
                        String fileName = rawFileName.contains("?") ? rawFileName.substring(0, rawFileName.indexOf("?")) : rawFileName;

                        String fileTypeStr = row.findElement(By.xpath(FILE_TYPE_XPATH)).getText().trim();

                        if (href == null || !fileTypeStr.equalsIgnoreCase("GZ")) continue;
                        if (!matchesFileType(fileName, desiredFileType)) continue;

                        LocalDateTime uploadDate = extractDateTimeFromFileName(fileName);
                        if (uploadDate == null) continue;

                        long hoursDiff = Duration.between(uploadDate, LocalDateTime.now()).toHours();
                        if (hoursDiff <= timeFrameInHours && !seenFileNames.contains(fileName)) {
                            files.add(new FileMetadata(fileName, href, desiredFileType, uploadDate));
                            seenFileNames.add(fileName);
                            logger.info("Added file: {}", fileName);
                        }
                    } catch (Exception e) {
                        logger.warn(" Error parsing row: {}", e.getMessage());
                    }
                }

                WebElement nextBtn = driver.findElement(By.xpath(NEXT_PAGE_XPATH));
                if (nextBtn.isDisplayed()) {
                    WebElement oldTable = driver.findElement(By.xpath(TABLE_IN_CONTAINER_XPATH));
                    nextBtn.click();
                    wait.until(ExpectedConditions.stalenessOf(oldTable));
                    page++;
                } else {
                    hasNext = false;
                }

            } catch (Exception e) {
                logger.warn("Stopping pagination on page {}: {}", page, e.getMessage());
                hasNext = false;
            }
        }
        return files;
    }

    private boolean matchesFileType(String fileName, FileType fileType) {
        if (fileName == null) return false;
        fileName = fileName.toLowerCase();

        return switch (fileType) {
            case PRICE -> fileName.startsWith("price") && !fileName.startsWith("pricefull");
            case PRICEFULL -> fileName.startsWith("pricefull") || fileName.contains("pricefull");
            case PROMO -> fileName.startsWith("promo") && !fileName.startsWith("promofull");
            case PROMOFULL -> fileName.startsWith("promofull") || fileName.contains("promofull");
            case STORE -> fileName.startsWith("store");
            default -> false;
        };
    }

    private LocalDateTime extractDateTimeFromFileName(String fileName) {
        try {
            String dateTimePart = fileName.replaceAll(".*-(\\d{12})\\.gz$", "$1");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            return LocalDateTime.parse(dateTimePart, formatter);
        } catch (Exception e) {
            logger.warn(" Failed to parse date from file name: {}", fileName);
            return null;
        }
    }
}
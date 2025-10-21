package com.example.mystore.downloader.driver;

import com.example.mystore.downloader.model.FileMetadata;
import com.example.mystore.downloader.model.FileType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("RamiLeviDownloader")

/**
 * RamiLeviPriceDownloader handles the automation of logging into the Rami Levi website
 * and retrieving available price-related files for download.
 *
 * Responsibilities:
 * - Logs into the price file website of Rami Levi, using Selenium WebDriver.
 * - Navigates to the files page and fetches available files matching desired file types.
 * - Filters files based on type (PriceFull, Price) and upload time.
 * - Extracts metadata (name, link, upload time) for valid files.
 * - Stores session cookies for potential reuse.
 *
 * Used for automating the download of pricing files.
 */
@Component
public class RamiLeviPriceDownloader implements PriceDownloader , CookieProvider {
    private static final Logger logger = LoggerFactory.getLogger(RamiLeviPriceDownloader.class);

    @Value("${prices.rami-levi.login.username}")
    private String username;

    @Value("${prices.rami-levi.login.url}")
    private String loginUrl;

    @Value("${prices.rami-levi.files.url}")
    private String filesUrl;

    private Set<Cookie> cookies;

    private static final String FILE_ROW_XPATH = "//table[@id='fileList']//tr";
    private static final String FILE_NAME_LINK_XPATH = ".//td[1]//a";
    private static final String FILE_DATE_XPATH = ".//td[4]";

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setFilesUrl(String filesUrl) {
        this.filesUrl = filesUrl;
    }

    public Set<Cookie> getCookies() {
        return this.cookies;
    }

    @Override
    public void login(WebDriver driver, WebDriverWait wait) {
        logger.info("Navigating to login page: {}", loginUrl);
        driver.get(loginUrl);

        try {
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
            usernameField.sendKeys(username);

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'כניסה')]")));
            loginButton.click();

            wait.until(ExpectedConditions.urlContains("/file"));
            logger.info("Login successful!");
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage(), e);
            throw new RuntimeException("Login failed", e);
        }
    }

    @Override
    public List<FileMetadata> fetchAvailableFiles(WebDriver driver, WebDriverWait wait, FileType desiredFileType, int timeFrameInHours) throws InterruptedException {
        logger.info("Navigating to files page: {}", filesUrl);
        driver.get(filesUrl);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody[@class='context allow-dropdown-overflow']//tr")));
        this.cookies = driver.manage().getCookies();

        List<WebElement> fileRows = driver.findElements(By.xpath(FILE_ROW_XPATH));
        logger.info("Found {} files in page.", fileRows.size());

        List<FileMetadata> files = new ArrayList<>();
        for (WebElement row : fileRows) {
            try {
                List<WebElement> nameCells = row.findElements(By.xpath(FILE_NAME_LINK_XPATH));
                if (nameCells.isEmpty()) {
                    logger.warn("Skipping a row because no <a> tag found in td[1]");
                    continue;
                }

                WebElement nameCell = nameCells.get(0);
                String fileName = nameCell.getText().trim();
                String href = nameCell.getAttribute("href");
                FileType fileType = determineFileType(fileName);

                if (fileType != desiredFileType) {
                    logger.debug("Skipping file with unmatched type: {}", fileName);
                    continue;
                }

                LocalDateTime uploadDate = extractDateTimeFromFileName(fileName);

                if (uploadDate == null) {
                    logger.warn("Skipping file due to invalid datetime in filename: {}", fileName);
                    continue;
                }

                // Calculate how many hours ago the file was uploaded
                long hoursAgo = java.time.Duration.between(uploadDate, LocalDateTime.now()).toHours();

                if (hoursAgo > timeFrameInHours) {
                    logger.debug("Skipping old file ({} hours ago): {}", hoursAgo, fileName);
                    continue;
                }

                FileMetadata fileMetadata = new FileMetadata(fileName, href, fileType, uploadDate);

                files.add(fileMetadata);

                logger.info(" File added for download: {}", fileName);

            } catch (Exception e) {
                logger.error(" Error processing a table row: {}", e.getMessage(), e);
            }
        }

        logger.info(" Finished fetching files. Total files ready to download: {}", files.size());
        return files;
    }

    private FileType determineFileType(String fileName) {
        if (fileName.startsWith("PriceFull")) {
            return FileType.PRICEFULL;
        } else if (fileName.startsWith("Price")) {
            return FileType.PRICE;
        } else if (fileName.startsWith("Promo")) {
            return FileType.PROMO;
        } else if (fileName.startsWith("Store")) {
            return FileType.STORE;
        } else {
            return FileType.UNKNOWN;
        }
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
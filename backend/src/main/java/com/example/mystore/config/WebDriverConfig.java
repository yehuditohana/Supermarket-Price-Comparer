package com.example.mystore.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * WebDriverConfig configures Selenium WebDriver settings for the application.
 *
 * Responsibilities:
 * - Sets up the ChromeDriver binary during application startup.
 * - Provides a configured ChromeOptions bean for creating WebDriver instances.
 */
@Configuration
public class WebDriverConfig {
    /**
     * Sets up the ChromeDriver once during application startup to ensure
     * the correct driver version is available and avoid redundant setup calls during runtime.
     */
    @EventListener(ContextRefreshedEvent.class)
    public void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
    }

    /**
     * Provides a pre-configured ChromeOptions bean with necessary arguments for WebDriver instances.
     */
    @Bean
    public ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        //options.addArguments("--blink-settings=imagesEnabled=false");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        return options;
    }

}
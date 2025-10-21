package com.example.mystore.downloader.driver;

import org.openqa.selenium.Cookie;
import java.util.Set;

public interface CookieProvider {
    Set<Cookie> getCookies();
}
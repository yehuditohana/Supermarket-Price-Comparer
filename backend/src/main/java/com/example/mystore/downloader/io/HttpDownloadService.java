package com.example.mystore.downloader.io;

import org.openqa.selenium.Cookie;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;

@Service
public class HttpDownloadService {

    public void downloadFile(String downloadUrl, Path targetPath, Set<Cookie> cookies) throws Exception {
        URL url = new URL(downloadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        if (cookies != null && !cookies.isEmpty()) {
            StringBuilder cookieHeader = new StringBuilder();
            for (Cookie cookie : cookies) {
                cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
            }
            connection.setRequestProperty("Cookie", cookieHeader.toString());
        }
        connection.connect();

        try (InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
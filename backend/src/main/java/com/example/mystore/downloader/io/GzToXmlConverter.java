package com.example.mystore.downloader.io;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
@Service
public class GzToXmlConverter {

    public boolean convert(File gzFile, File xmlFile) {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(gzFile));
             FileOutputStream fileOutputStream = new FileOutputStream(xmlFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
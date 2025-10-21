package com.example.mystore.downloader.model;

import java.time.LocalDateTime;

public class FileMetadata {
    private String fileName;
    private String downloadUrl;
    private FileType fileType;
    private LocalDateTime uploadDate;

    public FileMetadata(String fileName, String downloadUrl, FileType fileType, LocalDateTime uploadDate) {
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        this.fileType = fileType;
        this.uploadDate = uploadDate;
    }



    public String getFileName() {
        return fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public FileType getFileType() {
        return fileType;
    }
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
}
package com.example.mystore.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

/* Searches for XML files inside the given directory and its subdirectories.
 Returns an array of XML files if found, or null if none found.*/
public class DirectoryUtils {
    /**
     * Searches for XML files inside the given directory and its subdirectories.
     *
     * @param directoryPath the root directory to search
     * @param logger the logger for logging errors or info
     * @return an array of XML files if found, or null if none found
     */
    public static File[] getXmlFilesFromDirectory(String directoryPath, Logger logger) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            logger.error(" Directory does not exist or is not a directory: {}", directoryPath);
            return null;
        }

        List<File> xmlFiles = new ArrayList<>();
        collectXmlFiles(directory, xmlFiles);

        if (xmlFiles.isEmpty()) {
            logger.warn(" No XML files found in directory: {}", directoryPath);
            return null;
        }

        logger.info(" Found {} XML files to process.", xmlFiles.size());
        return xmlFiles.toArray(new File[0]);
    }
/* Recursive helper function that scans a directory and its subdirectories.
 Adds all XML files found to the provided list.*/
    private static void collectXmlFiles(File directory, List<File> xmlFiles) {
        File[] filesAndDirs = directory.listFiles();
        if (filesAndDirs == null) {
            return;
        }

        for (File file : filesAndDirs) {
            if (file.isDirectory()) {
                collectXmlFiles(file, xmlFiles); //Entering the next level
            } else if (file.isFile() && file.getName().toLowerCase().endsWith(".xml")) {
                xmlFiles.add(file);
            }
        }
    }



// Deletes all files inside the given directory and all its subdirectories (recursive).
// Does not delete any folders, only files.
    public static void clearDirectory(String directoryPath, Logger logger) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            logger.warn(" Directory does not exist or is not a directory: {}", directoryPath);
            return;
        }

        clearDirectoryRecursive(directory, logger);
    }

    /**
     * Recursive helper that goes through directories and deletes all files inside.
     *
     * @param dir the current directory
     * @param logger the logger for logging actions
     */    private static void clearDirectoryRecursive(File dir, Logger logger) {
        File[] filesAndDirs = dir.listFiles();
        if (filesAndDirs == null) {
            return;
        }

        for (File file : filesAndDirs) {
            if (file.isDirectory()) {
                //If it's a directory - enter it recursively
                clearDirectoryRecursive(file, logger);
            } else if (file.isFile()) {
                boolean deleted = file.delete();
                if (deleted) {
                    logger.info(" Deleted file: {}", file.getAbsolutePath());
                } else {
                    logger.warn("️ Failed to delete file: {}", file.getAbsolutePath());
                }
            }
        }
    }





}


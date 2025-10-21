package com.example.mystore.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
/**
 * Utility class that loads category hierarchy (General - Sub - Specific) from a CSV file
 * and stores it in a nested Map structure for fast access.
 *
 * The CSV is expected to have three columns:
 * GeneralCategory, SubCategory, SpecificCategory
 */
@Component
public class CategoryLoader {
    private final Map<String, Map<String, List<String>>> categoriesMap = new LinkedHashMap<>();

    public CategoryLoader() {
        loadCategoriesFromCSV("data/Categories.csv");
    }
    /**
     * Loads categories from a CSV file into a nested Map structure.
     * Each general category maps to subcategories, which in turn map to lists of specific categories.
     *
     * @param fileName the CSV file path relative to the classpath
     */
    private void loadCategoriesFromCSV(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            String[] line;
            reader.readNext();// Skip header


            while ((line = reader.readNext()) != null) {
                if (line.length < 3) continue;

                String generalCategory = line[0].trim();
                String subCategory = line[1].trim();
                String specificCategory = line[2].trim();

                categoriesMap
                        .computeIfAbsent(generalCategory, k -> new LinkedHashMap<>())
                        .computeIfAbsent(subCategory, k -> new ArrayList<>())
                        .add(specificCategory);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
    /**
     * Returns the entire nested category map.
     *
     * @return a Map from GeneralCategory to SubCategory to List of SpecificCategories
     */
    public Map<String, Map<String, List<String>>> getCategoriesMap() {
        return categoriesMap;
    }
}
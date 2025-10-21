package com.example.mystore.utils;

import com.opencsv.CSVWriter;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
/**
 * Utility class for writing data to CSV files using OpenCSV.
 */
public class CsvUtils {
    /**
     * Writes a list of data objects to a CSV file.
     *
     * @param filePath the output file path
     * @param headers the headers for the CSV file
     * @param data the list of data objects to write
     * @param rowMapper a function that maps each data object to a String array (representing a row)
     * @param <T> the type of the data objects
     * @throws IOException if an I/O error occurs
     */

    public static <T> void writeToCsv(
            String filePath,
            String[] headers,
            List<T> data,
            Function<T, String[]> rowMapper
    ) throws IOException {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.writeNext(headers); // Write the header row
            for (T item : data) {
                String[] row = rowMapper.apply(item);
                writer.writeNext(row);// Write each data row
            }
        }
    }
}
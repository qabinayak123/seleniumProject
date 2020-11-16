package com.qualitrix.common.utils.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVManager {

    public String getValue(String csvFilePath, int row, int col) throws IOException {

        if (row < 1 || col < 1) {
            System.out.println("Minimum value for Row or Column is 1");
            return "";
        } else {
            // .net SystemKeyword starts with 1 and Java's Common CSV starts from 0
            row -= 1;
            col -= 1;
        }

        // Create the CSVFormat object
        CSVFormat format = CSVFormat.RFC4180.withIgnoreHeaderCase().withDelimiter(',');
        // initialize the CSVParser object
        try (CSVParser parser = new CSVParser(new FileReader(csvFilePath), format)) {
            String value = parser.getRecords().get(row).get(col);
            parser.close();
            return value;
        }
    }

    /**
     * @param csvFilePath
     * @param row
     * @param col
     * @return boolean
     * @throws Exception
     */
    public boolean setValue(String csvFilePath, int row, int col, String newText) throws Exception {

        if (row < 1 || col < 1) {
            System.out.println("Minimum value for Row or Column is 1");
            return false;
        } else {
            // .net SystemKeyword starts with 1 and Java's Common CSV starts from 0
            row -= 1;
            col -= 1;
        }

        List<List<String>> lines = convertCsvFileToList(new File(csvFilePath));
        System.out.println("Current: " + lines.get(row).set(col, newText));
        System.out.println("Updated:" + lines.get(row).get(col));

        writeCsv(lines, new File(csvFilePath));
        return true;
    }

    private void writeCsv(List<List<String>> lines, File csvFilePath) throws IOException {
        CSVFormat format = CSVFormat.RFC4180.withIgnoreHeaderCase().withDelimiter(',');
        try (FileWriter fileWriter = new FileWriter(csvFilePath);
             CSVPrinter printer = new CSVPrinter(fileWriter, format.withDelimiter(','))) {
            for (List<String> line : lines) {
                printer.printRecord(line);
            }
        }
    }

    private List<List<String>> convertCsvFileToList(File csvFilePath) throws IOException {
        List<List<String>> lines = new ArrayList<>();
        CSVFormat format = CSVFormat.RFC4180.withIgnoreHeaderCase().withDelimiter(',');
        try (FileReader fileReader = new FileReader(csvFilePath);
             CSVParser parser = new CSVParser(fileReader, format)) {
            List<CSVRecord> allRecored = parser.getRecords();
            for (CSVRecord csvRecord : allRecored) {
                String[] wholeLine = toArray(csvRecord);
                lines.add(Arrays.asList(wholeLine));
            }
            parser.close();
            return lines;
        }
    }

    private static String[] toArray(CSVRecord rec) {
        String[] arr = new String[rec.size()];
        int i = 0;
        for (String str : rec) {
            arr[i++] = str;
        }
        return arr;
    }

}

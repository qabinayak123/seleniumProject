package com.qualitrix.common.utils.csv;

import java.io.IOException;

public class C_CSVUtils implements CSVUtils {

    CSVManager csvManager;

    public C_CSVUtils() {
        this.csvManager = new CSVManager();
    }

    @Override
    public String getValue(String csvFilePath, int row, int col) throws IOException {
        return this.csvManager.getValue(csvFilePath, row, col);
    }

    @Override
    public boolean setValue(String csvFilePath, int row, int col, String newText) throws Exception {
        return this.csvManager.setValue(csvFilePath, row, col, newText);
    }
}

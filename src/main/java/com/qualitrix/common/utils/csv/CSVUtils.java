package com.qualitrix.common.utils.csv;

import java.io.IOException;

public interface CSVUtils {
    String getValue(String csvFilePath, int row, int col) throws IOException;

    boolean setValue(String csvFilePath, int row, int col, String newText) throws Exception;
}

package com.qualitrix.common.utils.excel;

import org.apache.poi.ss.usermodel.Workbook;

public class ExcelObject {
    public String excelPath;
    public Workbook excelWorkbook;

    public ExcelObject(String excelPath, Workbook excelWorkbook) {
        this.excelPath = excelPath;
        this.excelWorkbook = excelWorkbook;
    }

    public String getExcelPath() {
        return excelPath;
    }

    public Workbook getExcelWorkbook() {
        return excelWorkbook;
    }
}

package com.qualitrix.common.utils.excel;

import com.qualitrix.common.utils.PropUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExcelUtils {

    public static Sheet sh = null;
    public static Workbook wb = null;
    public static String excelPath = null;


    private Map<String, ExcelObject> excelMap = new HashMap<String, ExcelObject>();

    public void open(String excelPath, String excelReference) throws IOException {
        Workbook wb = null;
        try (FileInputStream fis = new FileInputStream(new File(excelPath))) {
            System.out.println("File Input Stream executed");
            String extension = new PropUtils().getFileExtension(excelPath);
            if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xlsm")) {
                wb = new XSSFWorkbook(fis);
            } else if (extension.equalsIgnoreCase("xls")) {
                wb = new HSSFWorkbook(fis);
            } else {
                System.out.println("failed to open excel");
            }
            System.out.println("Work Book Executed");
        }
        ExcelObject excelObject = new ExcelObject(excelPath, wb);
        this.excelMap.put(excelReference, excelObject);
    }

    public void close(String excelReference) throws IOException {

        try {
            if (this.excelMap.remove(excelReference) == null) {
                System.out.println("Specified Excel Reference is not opened or already closed");
            } else {
                this.excelMap.remove(excelReference);
            }
        } catch (Exception e) {
            System.out.println("Specified Excel Reference is not opened or already closed");
        }
    }

    public void closeAll() throws IOException {
        try {
            Set<String> wbKeys = this.excelMap.keySet();
            for (String key : wbKeys) {
                System.out.println("Closing " + key);
                ExcelObject excelObject = this.excelMap.get(key);
                excelObject.excelWorkbook.close();
                excelObject.excelWorkbook = null;
            }
            this.excelMap.clear();
            System.gc();
            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCellValue(String excelReference, String sheetName, int row, int col) {
        this.setExcelWorkbookAndSheet(excelReference, sheetName);
        String cellData = this.getExcelCellValue(row, col);
        return cellData;
    }

    public void setCellValue(String excelReference, String sheetName,
                             String text, int row, int col) throws IOException {

        this.setExcelWorkbookAndSheet(excelReference, sheetName);
        row--;
        col--;
        Row row1 = this.sh.getRow(row);
        if (row1 == null) {
            row1 = this.sh.createRow(row);
        }

        Cell cell = row1.getCell(col);

        if (cell == null) {
            cell = row1.createCell(col);
        }

        // **************************************************************************

        boolean isDate = false;
        SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd/yyyy");
        sdFormat.setLenient(false);
        try {
            sdFormat.parse(text);
            isDate = true;
            System.out.println("IS DATE");
        } catch (ParseException e) {
            // Not Date
        }
        CellStyle cellStyle = cell.getCellStyle();

        if (isDate) {

            Date formattedDate = new Date(text);
            String dateToEnter = sdFormat.format(formattedDate);

            System.out.println("DATE ENTER IS AS ::: " + dateToEnter);
            cell.setCellValue(dateToEnter);
            cell.setCellStyle(cellStyle);

        } else {
            cell.setCellStyle(cellStyle);

            int Ivalue = -1;
            double Dvalue = -1;
            boolean Bvalue = false;
            boolean conversionDone = false;

            if (!conversionDone) {
                try {
                    cell.setCellValue(Integer.parseInt(text));
                    System.out.println("Integer Type Written");
                    conversionDone = true;
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            if (!conversionDone) {
                try {
                    cell.setCellValue(Double.parseDouble(text));
                    System.out.println("Double Type Written");
                    conversionDone = true;
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            if (!conversionDone) {
                try {
                    if (String.valueOf(text).toLowerCase().equals("true")
                        || String.valueOf(text).toLowerCase().equals("false")) {
                        cell.setCellValue(Boolean.parseBoolean(text));
                        System.out.println("Boolean Type Written");
                        conversionDone = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!conversionDone) {
                cell.setCellValue(text);
                System.out.println("String Type Written");
                conversionDone = true;
            }
        }
        // **************************************************************************
        this.writeToExcel(excelReference);
    }


    private void writeToExcel(String excelReference) throws IOException {
        try (FileOutputStream fos =
                 new FileOutputStream(this.excelMap.get(excelReference).getExcelPath())) {
            this.wb.write(fos);
        } catch (Exception e) {
            System.out.println("Close the Excel File <" + excelReference
                + "> to modify the data of file");
        }
    }

    private void setExcelWorkbookAndSheet(String excelPath, String sheetName) {
        sh = null;
        wb = null;
        try {
            this.wb = this.excelMap.get(excelPath).excelWorkbook;
            this.excelPath = this.excelMap.get(excelPath).excelPath;
        } catch (Exception e) {
            System.out.println("Excel Reference <" + excelPath + "> is not opened");
        }

        try {
            int sheetIndex = wb.getSheetIndex(sheetName);
            // System.out.println("sheetIndex executed");
            sh = (Sheet) wb.getSheetAt(sheetIndex);
            // System.out.println("sh executed");
        } catch (Exception e) {
            System.out.println("Sheet <" + sheetName
                + "> is not present in Excel File <" + excelPath + ">");
        }
    }

    private String getExcelCellValue(int row, int col) {
        Cell cell = getExcelCell(row, col);
        return getExcelCellValue(cell);
    }


    private String getExcelCellValue(Cell cell) {
        int cellType = cell.getCellType().getCode();
        return getExcelCellValue(cell, cellType, "");
    }

    private Cell getExcelCell(int row, int col) {
        row--;
        col--;

        Row row1 = sh.getRow(row);
        if (row1 == null) {
            row1 = sh.createRow(row);
        }

        Cell cell = row1.getCell(col);
        if (cell == null) {
            cell = row1.createCell(col);
        }

        return cell;
    }

    private String getExcelCellValue(Cell cell, int cellType, String oldValue) {

        String cellData = "";
        if (cellType == CellType.FORMULA.getCode()) {
            System.out.println("Formula cell found.........");
            return handleFormulaCell(cell, cellType);
        }

        if (cellType == 0) {
            if (oldValue != null && !oldValue.isEmpty()) {
                return oldValue;
            }

            DataFormatter df = new DataFormatter();
            cellData = df.formatCellValue(cell);

            System.out.println("numeric doubValue is : " + cellData);
            return String.valueOf(cellData);
        }

        if (cellType == 5) {
            System.out.println("Found Error");
            try {
                double doubValue = cell.getNumericCellValue();
                return String.valueOf(doubValue);
            } catch (Exception e) {
                System.out.println(">.Exception :" + e.getMessage());
                return oldValue;
            }
        }

        if (cellType == 4) {
            System.out.println("Found Boolean");
            try {
                boolean boolValue = cell.getBooleanCellValue();
                return String.valueOf(boolValue);
            } catch (Exception e) {
                System.out.println(">.Exception :" + e.getMessage());
                return oldValue;
            }
        }

        try {
            DataFormatter df = new DataFormatter();
            cellData = df.formatCellValue(cell);
            cellData = cell.getStringCellValue();
        } catch (Exception e) {
            System.out.println("XYZ");
            e.printStackTrace();

            try {
                System.out.println("XYZ");
                cellData = String.valueOf((cell.getNumericCellValue()));
                if (cellData.contains("E") || cellData.contains("e")) {
                    BigDecimal bd = new BigDecimal(cellData);
                    long lonVal = bd.longValue();
                    cellData = String.valueOf(lonVal);
                    return cellData;
                }
                if (cellData.indexOf(".") == cellData.lastIndexOf(".")) {
                    try {
                        if (Integer.parseInt(cellData.split("\\.")[1]) == 0) {
                            cellData = cellData.split("\\.")[0];
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                cellData = "";
            }
        }
        return cellData;
    }

    private String handleFormulaCell(Cell cell, int type) {
        FormulaEvaluator evaluator = wb.getCreationHelper()
            .createFormulaEvaluator();
        System.out.println(">>Inside Formula");
        System.out.println(">.Prev Cell Type " + cell.getCellType() + "  "
            + cell.getCellType().getCode());
        String oldNumericValue = "";
        try {
            //double oldValue = cell.getNumericCellValue();

            DataFormatter df = new DataFormatter();

            String tempoldNumericValue = df.formatCellValue(cell);
            //oldNumericValue = cell.getStringCellValue();


            //to check if provided value is integer type
            Integer.valueOf(tempoldNumericValue);
            System.out.println(">>Cell Value " + oldNumericValue);
            oldNumericValue = String.valueOf(tempoldNumericValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type == CellType.FORMULA.getCode()) {
            System.out.println(">>Cell is of Formula Type");
            evaluator.evaluateInCell(cell);
        }
        System.out.println(">>Current Cell Type " + cell.getCellType()
            + "  " + cell.getCellType().getCode());
        return getExcelCellValue(cell, cell.getCellType().getCode(), oldNumericValue);
    }

}

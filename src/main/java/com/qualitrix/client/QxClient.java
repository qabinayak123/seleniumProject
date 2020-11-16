package com.qualitrix.client;

import com.aventstack.extentreports.ExtentTest;
import com.qualitrix.common.utils.AssertUtils;
import com.qualitrix.common.utils.PropUtils;
import com.qualitrix.common.utils.ScreenshotUtils;
import com.qualitrix.common.utils.csv.CSVUtils;
import com.qualitrix.common.utils.csv.C_CSVUtils;
import com.qualitrix.common.utils.database.CDBUtils;
import com.qualitrix.common.utils.database.DBUtils;
import com.qualitrix.common.utils.database.DriverType;
import com.qualitrix.common.utils.excel.ExcelUtils;
import com.qualitrix.common.utils.gsheet.GSheet;
import com.qualitrix.common.utils.gsheet.GSheetObject;
import com.qualitrix.report.factory.QxReport;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class QxClient {

    private final WebDriver webDriver;
    private ScreenshotUtils screenshotUtils;
    private GSheetObject googleSheetObject;
    private AssertUtils assertUtils;
    private CSVUtils csvUtils;
    private PropUtils propUtils;
    private ExcelUtils excelUtils;

    public QxClient(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebDriver driver() {
        return this.webDriver;
    }

    /**
     * Return ScreenshotUtils Object.
     *
     * @return screenshotUtils
     */
    public ScreenshotUtils screenshotUtils() {
        if (this.screenshotUtils == null) {
            this.screenshotUtils = new ScreenshotUtils(this.webDriver);
        }
        return screenshotUtils;
    }

    /**
     * Return the googlesheet object.
     *
     * @param sheetId - ID of the sheet
     * @return googleSheetObject
     * @throws GeneralSecurityException - throws security exception
     * @throws IOException              - throws IO Exception
     */
    public GSheet gsheet(String sheetId) throws GeneralSecurityException, IOException {
        if (this.googleSheetObject == null) {
            this.googleSheetObject = new GSheetObject();
        }
        return this.googleSheetObject.getGSheet(sheetId);
    }

    public ExtentTest report() {
        return QxReport.getTest();
    }

    public AssertUtils getAssertUtils() {

        if (this.assertUtils == null) {
            this.assertUtils = new AssertUtils();
        }

        return this.assertUtils;
    }

    public DBUtils dbUtils(DriverType driverType, String server,
                           String uid, String pwd, String dbName) throws SQLException {
        return new CDBUtils(driverType, server, uid, pwd, dbName);
    }

    public  CSVUtils csvUtils() {
        if (csvUtils == null) {
            this.csvUtils = new C_CSVUtils();
        }

        return this.csvUtils;
    }

    public PropUtils propUtils() {
        if (this.propUtils == null) {
            this.propUtils = new PropUtils();
        }

        return this.propUtils;
    }

    public ExcelUtils excelUtils() {
        if (this.excelUtils == null) {
            this.excelUtils = new ExcelUtils();
        }
        return this.excelUtils;
    }

    private static final ThreadLocal<QxClient> qualitrix = new ThreadLocal<>();

    public static void setQX(QxClient qx) {
        qualitrix.set(qx);
    }

    public static QxClient get() {
        return qualitrix.get();
    }
}
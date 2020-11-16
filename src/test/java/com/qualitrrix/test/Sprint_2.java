package com.qualitrrix.test;

import com.qualitrix.annotation.values.Author;
import com.qualitrix.annotation.values.Skip;
import com.qualitrix.client.QxClient;
import com.qualitrix.common.utils.database.DBUtils;
import com.qualitrix.common.utils.database.DriverType;
import org.testng.annotations.Test;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Sprint_2 {

    @Author(name = "Binayak")
    @Test()
    public void Database() throws Exception {
        System.out.println("Outer Test Database");

        try (DBUtils database = QxClient.get().dbUtils(DriverType.MYSQL, "localhost",
            "root", "root", "mydatabase")) {
            int output = database.executeQuery("SELECT * FROM books;", 5);
            System.out.println("executeQuery: " + output);

            String insertQuery = " INSERT INTO books VALUES('Math', 60.00, 'English', 'Shibu');";
            System.out.println("Insert: " + database.executeQuery(insertQuery, 5));


            List<Map<String, String>> rows = database.fetchRecords("SELECT * FROM books;", 5);
            for (Map<String, String> row : rows) {
                System.out.println();
                for (String col : row.keySet()) {
                    System.out.print(col + ":" + row.get(col) + " ");
                }
                System.out.println();
            }

            int colCount = database.getColumnCount("SELECT * FROM books;", 5);
            System.out.println("colCount: " + colCount);

            int rowCount = database.getRowCount("SELECT * FROM books;", 5);
            System.out.println("RowCount: " + rowCount);

            boolean isExist = database.verifyValue("SELECT * FROM books;", "title", "Math");
            System.out.println("isExist: " + isExist);

            boolean isExist1 = database.verifyValue("SELECT * FROM books;",
                "title", "I don't exists");
            System.out.println("isExist: " + isExist1);
        } finally {
            System.out.println("Test End......");
        }
    }

    @Author(name = "Shibu")
    @Skip
    @Test()
    public void CSV() throws Exception {
        String value = QxClient.get().csvUtils().getValue(System.getProperty("user.dir")
            + "/testdata/Sample100.csv", 1, 2);
        System.out.println("Value: " + value);

        QxClient.get().csvUtils().setValue(System.getProperty("user.dir")
            + "/testdata/Sample100.csv", 2, 2, "I am New Value");

        String newValue = QxClient.get().csvUtils().getValue(System.getProperty("user.dir")
            + "/testdata/Sample100.csv", 2, 2);
        System.out.println("New Value: " + newValue);
    }

    @Author(name = "Shibu")
    @Skip
    @Test()
    public void Properties() throws Exception {
        Properties properties = QxClient.get().propUtils().getProperties(
            System.getProperty("user.dir")
                + "/testdata/config.properties"
        );
        System.out.println("@name:" + properties.getProperty("name"));

        properties = QxClient.get().propUtils().getProperties(
            new File(System.getProperty("user.dir")
                + "/testdata/config.properties")
        );
        System.out.println("@email:" + properties.getProperty("email"));

        Properties prop = new Properties();
        prop.setProperty("name", "Shibu");
        prop.setProperty("email", "panda");
        prop.setProperty("address", "Bang");
        QxClient.get().propUtils().addProperties(prop, System.getProperty("user.dir")
            + "/testdata/config.properties");

        Properties prop1 = new Properties();
        prop1.setProperty("newEmail", "Shibu@email.com");
        QxClient.get().propUtils().addProperties(prop1, new File(System.getProperty("user.dir")
            + "/testdata/config.properties"));
    }

    @Author(name = "Shibu")
    @Skip
    @Test()
    public void Excel() throws Exception {
        String excelPath = System.getProperty("user.dir") + "/testdata/FinancialSample1.xlsx";
        QxClient.get().excelUtils().open(excelPath, "Excel1");
        String cellValue1 = QxClient.get().excelUtils().getCellValue("Excel1", "Sheet1",
            1, 2);
        System.out.println("@cellValue1: " + cellValue1);

        QxClient.get().excelUtils().setCellValue("Excel1", "Sheet1",
            "New Change Log", 1, 2);

        QxClient.get().excelUtils().open(System.getProperty("user.dir")
            + "/testdata/FinancialSample2.xlsx", "Excel2");
        String cellValue2 = QxClient.get().excelUtils().getCellValue("Excel2",
            "Sheet1", 2, 2);
        System.out.println("@cellValue2: " + cellValue2);
        QxClient.get().excelUtils().close("Excel1");
        QxClient.get().excelUtils().close("Excel2");
        QxClient.get().excelUtils().closeAll();
    }

    @Author(name = "Shibu")
    @Skip
    @Test()
    public void Screenshot() throws Exception {
        String base64 = QxClient.get().screenshotUtils().captureBase64();
        System.out.println("Base64: " + base64);

        File file = QxClient.get().screenshotUtils().captureFile();
        System.out.println("ImageFile: " + file.getPath());

        String imagePath = System.getProperty("user.dir") + "/reports/" + "image1.jpg";
        File file1 = QxClient.get().screenshotUtils().captureFile(imagePath);
        System.out.println("@imagePath1: " + file1.getPath());
    }
}
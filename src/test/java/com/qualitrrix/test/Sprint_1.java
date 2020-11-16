package com.qualitrrix.test;

import com.qualitrix.annotation.values.Author;
import com.qualitrix.annotation.values.Skip;
import com.qualitrix.client.QxClient;
import com.qualitrix.common.utils.gsheet.GSheet;
import com.qualitrix.testng.listener.RetryCount;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Sprint_1 {

    @Author(name = "Binayak")
    @Test()
    @RetryCount(maxRetryCount = 1)
    public void RetryTest() {
        System.out.println("Outer Test1");
        WebDriver driver = QxClient.get().driver();
        driver.get("http://www.javacodegeeks.com/");
        String url = driver.getCurrentUrl();
        QxClient.get().report().info("Page Url: " + url);
        QxClient.get().report().info("Expecting: " + "google.com");

        QxClient.get().report().info("Test End");
        System.out.println("Test End......");
        Assert.assertEquals("hello", "helloq");
    }

    @Author(name = "Shibu")
    @Test()
    public void HappyPathTest() {
        System.out.println("Outer Test1");
        WebDriver driver = QxClient.get().driver();
        driver.get("http://www.javacodegeeks.com/");
        String url = driver.getCurrentUrl();
        QxClient.get().report().info("Page Url: " + url);
        QxClient.get().report().info("Test End");
        System.out.println("Test End......");
    }

    @Author(name = "Shibu")
    @Test()
    @Skip
    public void GoogleSheetTest() throws GeneralSecurityException, IOException {
        QxClient.get().report().info("Hi I am info log");
        GSheet gsheet = QxClient.get().gsheet("PUT_SHEET_ID_HERE");
        gsheet.getCellValue("Sheet1", 3, "C");
        gsheet.setCellValue("Sheet1", 3, "C", "Hello Welcome");
    }

    @Author(name = "Shibu")
    @Test()
    @Skip
    public void SkipTest() {

    }
}
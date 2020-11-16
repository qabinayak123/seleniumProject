package com.qualitrix.common.utils;

import com.qualitrix.common.manager.GlobalSession;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

    WebDriver driver;

    public ScreenshotUtils(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Method is to capture screenshot in base64 format.
     *
     * @return base64 Url
     */
    public String captureBase64() {
        TakesScreenshot newScreen = (TakesScreenshot) driver;
        String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
        return "data:image/jpg;base64, " + scnShot;
    }

    /**
     * Method is to capture the screenshot.
     *
     * @return FilePath
     * @throws IOException - Throws IOException
     */
    public File captureFile() throws IOException {
        System.out.println("GlobalSession.get(): " + GlobalSession.get());
        System.out.println("Selenium Manager: " + GlobalSession.get().getSeleniumManager());
        String path = GlobalSession.get().getSeleniumManager()
            .getReportPath()
            + File.separator + "screenshots" + File.separator
            + UUID.randomUUID().toString() + ".jpg";
        System.out.println("@Path: " + path);
        return this.captureFile(path);
    }

    public File captureFile(String filePath) throws IOException {
        return this.captureFile(new File(filePath));
    }

    /**
     * Method is to take the screenshot in specific file.
     *
     * @param file - Pass file name where want to store the screenshot
     * @return File Instance
     * @throws IOException - Throws IOException
     */
    public File captureFile(File file) throws IOException {
        File screenshotFile = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, file);
        return file;
    }
}
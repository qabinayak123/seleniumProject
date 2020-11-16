package com.qualitrix.report.factory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.qualitrix.Global;

import java.io.File;

public class ExtentManager {

    private static ExtentReports extent;

    /**
     * Method is to get extent report instance.
     *
     * @return extentReport instance
     */
    public static ExtentReports getExtent() {
        if (extent == null) {
            try {
                extent = new ExtentReports();
                extent.attachReporter(getHtmlReporter());
                return extent;
            } catch (Exception e) {
                System.out.println("Exception while creating report html file.");
            }
        }
        return extent;
    }

    private static ExtentHtmlReporter getHtmlReporter() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(Global.getReportPath()
            + File.separator + "report.html");
        htmlReporter
            .loadXMLConfig(System.getProperty("user.dir")
                + "/src/main/resources/extent.xml");
        htmlReporter.config().setDocumentTitle(Global.getConfig().getProperties().getReportName());
        htmlReporter.config().setReportName(Global.getConfig().getProperties().getReportName());
        htmlReporter.config().setTheme(Theme.STANDARD);
        return htmlReporter;
    }
}
package com.qualitrix.report.factory;

import com.aventstack.extentreports.ExtentTest;


public class QxReport {

    private static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return testReport.get();
    }

    public static void setTestReport(ExtentTest extent) {
        testReport.set(extent);
    }
}
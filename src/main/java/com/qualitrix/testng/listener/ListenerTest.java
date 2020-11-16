package com.qualitrix.testng.listener;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qualitrix.Global;
import com.qualitrix.annotation.values.Author;
import com.qualitrix.annotation.values.Skip;
import com.qualitrix.client.QxClient;
import com.qualitrix.common.manager.GlobalSession;
import com.qualitrix.common.manager.SeleniumManger;
import com.qualitrix.report.factory.ExtentManager;
import com.qualitrix.report.factory.ExtentTestManager;
import com.qualitrix.report.factory.QxReport;

import java.io.IOException;

import org.testng.IInvokedMethodListener;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;

public class ListenerTest implements ITestListener, ISuiteListener, IInvokedMethodListener {

    @Override
    public void onTestStart(ITestResult testResult) {
        String testName = testResult.getMethod().getMethodName() + " " + this;
        System.out.println("@onTestStart: " + testResult.getMethod().getMethodName() + this);
        Author author = testResult.getMethod().getConstructorOrMethod()
            .getMethod().getAnnotation(Author.class);

        ExtentTest testReport = ExtentTestManager
            .createTest(testResult.getMethod().getMethodName(),
                GlobalSession.get().getSeleniumManager().getCapability().getBrowserName()
                    + "_" + GlobalSession.get().getSeleniumManager()
                    .getCapability().getBrowserVersion(), author);

        QxReport.setTestReport(testReport);
        //System.out.println("Retry Instance: " + testResult.getInstance().toString());
        //Global.retryingTests.put(testResult.getInstance().toString(), testReport);
        Skip skip = testResult.getMethod().getConstructorOrMethod()
            .getMethod().getAnnotation(Skip.class);
        System.out.println("@Skip: " + skip);
        if (skip == null) {
            try {
                GlobalSession.get().getSeleniumManager().createDriver();
                QxReport.getTest().log(Status.PASS, "Application Launched Successfully");
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                e.printStackTrace();
                //Cant't throw because don't know if client using driver in his testcase.
                //throw new RuntimeException("Driver not created: " + e.getMessage());
            }
            QxClient.setQX(new QxClient(SeleniumManger.getWebDriver()));
        } else {
            System.out.println("skipping test");
            throw new SkipException("Skipped because annotation is set to the testcase");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("@onTestFailure");

        if (SeleniumManger.getWebDriver() != null) {
            QxReport.getTest().log(Status.FAIL, result.getThrowable().getMessage());
            String base64 = QxClient.get().screenshotUtils().captureBase64();
            /*QxReport.getTest().addScreenCaptureFromBase64String(QxClient.get()
          .screenshotUtils().captureBase64(), result.getMethod().getMethodName());*/
            try {
                QxReport.getTest().fail(result.getMethod().getMethodName() + " Failed",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            QxReport.getTest().log(Status.FAIL, "Driver not created");
            QxReport.getTest().log(Status.FAIL, result.getMethod().getMethodName() + " Failed");
        }

        GlobalSession.get().getSeleniumManager().quitDriver();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("@onTestSuccess");
        GlobalSession.get().getSeleniumManager().quitDriver();
        QxReport.getTest().log(Status.PASS, result.getMethod().getMethodName() + " Passed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("@onTestSkipped");
        QxReport.getTest().log(Status.SKIP, result.getMethod().getMethodName() + " Skipped");
        if (GlobalSession.get().getSeleniumManager() != null) {
            GlobalSession.get().getSeleniumManager().quitDriver();
        }
    }

    @Override
    public void onStart(ITestContext testContext) {
        System.out.println("@Start: ITestContext");
        GlobalSession.set(new GlobalSession());
        SeleniumManger appiumManager = Global.getAvailableBrowser();
        if (appiumManager != null) {
            GlobalSession.get().setSeleniumManager(appiumManager);
            try {
                appiumManager.startSeleniumServer();
            } catch (Exception e) {
                System.out.println("Exception onStart: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
        System.out.println("@OnFinish: ITestContext");
        GlobalSession.get().getSeleniumManager().stopSeleniumServer();
        ExtentManager.getExtent().flush();
    }
}
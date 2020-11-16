package com.qualitrix.common.manager;

public class GlobalSession {

    private String reportPath;
    private String screenshotPath;
    private String logPath;

    private static ThreadLocal<GlobalSession> globalSession = new ThreadLocal<>();

    public static void set(com.qualitrix.common.manager.GlobalSession globalSession) {
        com.qualitrix.common.manager.GlobalSession.globalSession.set(globalSession);
    }

    public static com.qualitrix.common.manager.GlobalSession get() {
        return globalSession.get();
    }

    private SeleniumManger seleniumManager;

    public void setSeleniumManager(SeleniumManger seleniumManager) {
        this.seleniumManager = seleniumManager;
    }

    public SeleniumManger getSeleniumManager() {
        return this.seleniumManager;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getLogPath() {
        return logPath;
    }
}
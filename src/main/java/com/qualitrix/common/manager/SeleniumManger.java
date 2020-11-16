package com.qualitrix.common.manager;

import com.qualitrix.config.Capability;
import com.qualitrix.selenium.SeleniumHub;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumManger {

    private WebDriver driver;
    private DesiredCapabilities desiredCapabilities;
    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    private Capability capability;
    private String reportPath;
    private boolean isAvailable;
    private SeleniumHub selenumHub;

    public SeleniumManger(Capability capability) {
        this.capability = capability;
        this.isAvailable = true;
    }

    public Capability getCapability() {
        return capability;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }

    /**
     * This method is to start selenium server.
     *
     * @throws IOException          - Throws IOException
     * @throws InterruptedException - Throws Interrupted Exception
     */
    public void startSeleniumServer() throws IOException, InterruptedException {
        System.out.println("Start Selenium Server...");
        this.selenumHub = new SeleniumHub();
        this.selenumHub.start(this.reportPath, this.capability.getBrowserName(), getDriverPath());
    }

    /**
     * This method is to create the driver.
     *
     * @throws Exception - Throws Exception
     */
    public void createDriver() throws Exception {
        System.out.println("Create Driver...");
        this.setDesiredCapabilities();
        this.setDriver();
    }

    private DesiredCapabilities setDesiredCapabilities() throws Exception {
        desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName(capability.getBrowserName());
        desiredCapabilities.setVersion(capability.getBrowserVersion());
        return desiredCapabilities;
    }

    private void setDriver() throws MalformedURLException {
        this.driver = new RemoteWebDriver(this.selenumHub.getUrl(), this.desiredCapabilities);
        SeleniumManger.setDriverToThreadLocal(this.driver);
    }

    public static void setDriverToThreadLocal(WebDriver driver) {
        webDriver.set(driver);
    }

    public static WebDriver getWebDriver() {
        return webDriver.get();
    }

    /**
     * This method id to quit the driver.
     */
    public void quitDriver() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getReportPath() {
        return reportPath;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void stopSeleniumServer() {
        System.out.println("Stop Selenium Server...");
        this.selenumHub.stop();
    }

    private String getDriverPath() {
        String osName = System.getProperty("os.name");
        System.out.println("osName: " + osName);
        if (osName.contains("Win")) {
            osName = "windows";
        } else if (osName.contains("Lin")) {
            osName = "linux";
        } else {
            osName = "mac";
        }

        StringBuffer driverPath = new StringBuffer(System.getProperty("user.dir"));
        driverPath.append(File.separator).append("driver").append(File.separator)
            .append(capability.getBrowserName()).append(File.separator)
            .append(osName).append(File.separator).append(capability.getBrowserVersion());
        System.out.println("driverPath: " + driverPath);
        return driverPath.toString();
    }
}
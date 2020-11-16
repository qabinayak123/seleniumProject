package com.qualitrix.testng.listener;

import com.qualitrix.Global;
import com.qualitrix.common.manager.SeleniumManger;
import com.qualitrix.config.Capability;
import com.qualitrix.report.factory.ExtentTestManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * Created by Qualitrix on DD/MM/YYY.
 *
 * @author
 */
public class AlterSuiteListener implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> suites) {
        System.out.println("AlterSuite");
        System.out.println("prop.capabilities: " + System.getProperty("prop.capabilities"));

        String capabilitiesJsonPath = System.getProperty("prop.capabilities");
        Global.setConfig(Global.getConfigJson(capabilitiesJsonPath).toString());

        this.generateTestngXml(suites);

        this.createHtmlReportFile();

        for (Capability capability : Global.getConfig().getCapabilities()) {
            this.setSeleniumManager(capability);
        }

        ExtentTestManager.initReport();
    }

    private void setSeleniumManager(Capability capability) {
        SeleniumManger seleniumManager = new SeleniumManger(capability);
        this.setBrowserReportPath(seleniumManager);
        Global.addAppiumManagerToList(seleniumManager);
    }

    private void createHtmlReportFile() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String reportPath = System.getProperty("user.dir") + File.separator + "reports"
            + File.separator + "Report_" + dateFormat.format(date);
        File file = new File(reportPath);
        file.mkdir();
        Global.setReportPath(reportPath);
    }

    private void setBrowserReportPath(SeleniumManger seleniumManager) {
        String browserName = seleniumManager.getCapability().getBrowserName()
            + "_" + seleniumManager.getCapability().getBrowserVersion();
        String folderPath = Global.getReportPath() + File.separator + browserName;
        File file = new File(folderPath);
        file.mkdir();
        seleniumManager.setReportPath(folderPath);

        folderPath = Global.getReportPath() + File.separator
            + browserName + File.separator + "screenshots";
        file = new File(folderPath);
        file.mkdir();

        folderPath = Global.getReportPath()
            + File.separator + browserName + File.separator + "logs";
        file = new File(folderPath);
        file.mkdir();
    }

    /*Clone testCases if needed*/
    private void generateTestngXml(List<XmlSuite> suites) {
        XmlSuite suite = suites.get(0);
        suite.setThreadCount(Global.getConfig().getCapabilities().size());
        suite.setPreserveOrder(true);
        List<XmlTest> dynamictests = suite.getTests().stream()
            .filter(xmlTest -> xmlTest.getName().startsWith("Test")).collect(Collectors.toList());

        List<XmlTest> clonedTests = new ArrayList<>();
        for (XmlTest each : dynamictests) {
            for (int i = 1; i < Global.getConfig().getCapabilities().size(); i++) {
                XmlTest cloned = new XmlTest(suite);
                cloned.setName(Global.getConfig().getCapabilities().get(i).getBrowserName()
                    + "_" + Global.getConfig().getCapabilities().get(i).getBrowserVersion());
                cloned.getLocalParameters().put("browserName", Global.getConfig()
                    .getCapabilities().get(i).getBrowserName());
                cloned.getLocalParameters().put("browserVersion", Global.getConfig()
                    .getCapabilities().get(i).getBrowserVersion());
                cloned.getXmlClasses().addAll(each.getClasses());
                clonedTests.add(cloned);
            }
        }
        dynamictests.addAll(clonedTests);
    }
}
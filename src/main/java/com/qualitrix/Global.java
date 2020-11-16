package com.qualitrix;

import com.aventstack.extentreports.ExtentTest;
import com.google.gson.Gson;
import com.qualitrix.common.manager.SeleniumManger;
import com.qualitrix.common.utils.JsonParser;
import com.qualitrix.config.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Global {

    private static Config config;
    private static String reportPath;
    private static List<SeleniumManger> appiumManagerList = new ArrayList<SeleniumManger>();

    public static void setReportPath(String reportPath) {
        Global.reportPath = reportPath;
    }

    public static String getReportPath() {
        return reportPath;
    }

    public static Map<String, ExtentTest> retryingTests = new HashMap<>();

    public static void setConfig(String configJson) {
        config = new Gson().fromJson(configJson, Config.class);
    }

    public static Config getConfig() {
        return config;
    }

    /**
     * This method is return json object of capabilities.
     *
     * @param jsonPath - capabilities json path
     * @return JSONObject
     */
    public static JSONObject getConfigJson(String jsonPath) {
        if (jsonPath == null) {
            jsonPath = System.getProperty("user.dir")
                + File.separator + "caps" + File.separator + "capabilities.json";
        }
        JsonParser jsonParser = new JsonParser(jsonPath);
        return jsonParser.getObjectFromJson();
    }

    public static synchronized void addAppiumManagerToList(SeleniumManger seleniumManger) {
        appiumManagerList.add(seleniumManger);
    }

    public static List<SeleniumManger> getSeleniumManagerList() {
        return appiumManagerList;
    }

    /**
     * Method is to hold the seleniumManager instance.
     *
     * @return seleniumManager
     */
    public static synchronized SeleniumManger getAvailableBrowser() {
        for (SeleniumManger seleniumManager : Global.getSeleniumManagerList()) {
            if (seleniumManager.isAvailable()) {
                seleniumManager.setAvailable(false);
                return seleniumManager;
            }
        }
        return null;
    }
}
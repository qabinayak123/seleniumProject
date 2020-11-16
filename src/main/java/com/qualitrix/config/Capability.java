package com.qualitrix.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Capability {

    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("osVersion")
    @Expose
    private String osVersion;
    @SerializedName("browserVersion")
    @Expose
    private String browserVersion;
    @SerializedName("browserName")
    @Expose
    private String browserName;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }
}
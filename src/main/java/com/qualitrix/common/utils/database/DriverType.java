package com.qualitrix.common.utils.database;

import java.sql.Driver;

public enum DriverType {
    MYSQL("MYSQL");

    private String value;

    DriverType(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}

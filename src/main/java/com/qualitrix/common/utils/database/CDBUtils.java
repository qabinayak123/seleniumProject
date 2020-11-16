package com.qualitrix.common.utils.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CDBUtils implements DBUtils, AutoCloseable {

    final private DBManager dbManager;

    public CDBUtils(DriverType driverType, String server, String uid,
                    String pwd, String dbName) throws SQLException {
        this.dbManager = new DBManager(driverType, server, uid, pwd, dbName);
        this.dbManager.connectDB();
    }

    @Override
    public void disconnectDB() throws SQLException {
        this.dbManager.disconnectDB();
    }

    @Override
    public int executeQuery(String query, int commandTimeOut) throws SQLException {
        return this.dbManager.executeQuery(query, commandTimeOut);
    }

    @Override
    public int updateDataBase(String query, int commandTimeOut) throws SQLException {
        return this.dbManager.updateDataBase(query, commandTimeOut);
    }

    @Override
    public boolean verifyValue(String query, String columnName, String value) throws SQLException {
        return this.dbManager.verifyValue(query, columnName, value);
    }


    @Override
    public int getRowCount(String query, int commandTimeOut) throws SQLException {
        return this.dbManager.getRowCount(query, commandTimeOut);
    }

    @Override
    public int getColumnCount(String query, int commandTimeOut) throws SQLException {
        return this.dbManager.getColumnCount(query, commandTimeOut);
    }

    @Override
    public List<Map<String, String>> fetchRecords(String query,
                                                  int commandTimeOut) throws SQLException {
        return this.dbManager.fetchRecords(query, commandTimeOut);
    }

    @Override
    public void close() throws Exception {
        this.dbManager.disconnectDB();
    }
}

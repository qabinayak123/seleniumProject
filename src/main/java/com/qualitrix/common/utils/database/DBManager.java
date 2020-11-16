package com.qualitrix.common.utils.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

/**
 *
 */
class DBManager {


    private Connection connection;
    private DriverType driver;
    private String server;
    private String uid;
    private String pwd;
    private String dbName;

    public DBManager(DriverType driverType, String server, String uid, String pwd, String dbName) {
        this.driver = driverType;
        this.server = server;
        this.uid = uid;
        this.pwd = pwd;
        this.dbName = dbName;
    }

    public Connection connectDB() throws SQLException {

        switch (driver.toString()) {
            case "MYSQL":
                connection = connectMysql();
                return connection;
            default:
                break;
        }

        throw new SQLException("Currently the connection with DataBase is not present");
    }

    public void disconnectDB() throws SQLException {

        if (connection == null) {
            throw new SQLException("Currently the connection with DataBase is not present");
        }

        System.out.println("Connection Closing TYPE IS:"
            + connection.getMetaData().getDatabaseProductName());
        connection.close();
        System.out.println("Connection disconnected successfully");
    }

    private Connection connectMysql() throws SQLException {
        String conString = "jdbc:mysql://" + this.server + "/" + this.dbName;
        // String conString = "jdbc:mysql://" + server + "/" + database + "?useSSL=false";

        Connection conn = DriverManager
            .getConnection(conString, this.uid, this.pwd);
        System.out.println("Connection Established Successfully and the DATABASE TYPE IS:"
            + conn.getMetaData().getDatabaseProductName());
        return conn;
    }


    /**
     * @param query
     * @param commandTimeOut
     * @throws SQLException
     * @description Run update query on database
     */
    public int updateDataBase(String query, int commandTimeOut) throws SQLException {

        if (connection == null) {
            throw new SQLException("Currently the connection with DataBase is not present");
        }

        Statement stmt;
        stmt = connection.createStatement();
        return stmt.executeUpdate(query);
    }

    public int getColumnCount(String query, int Command_time_out) throws SQLException {
        if (connection == null) {
            throw new SQLException("Currently the connection with DataBase is not present");
        }

        int columnCount;
        columnCount = connection.createStatement()
            .executeQuery(query).getMetaData()
            .getColumnCount();
        return columnCount;
    }


    public int getRowCount(String query, int commandTimeOut) throws SQLException {
        if (connection == null) {
            throw new SQLException("Currently the connection with DataBase is not present");
        }

        ResultSet resultSet = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY).executeQuery(query);

        resultSet.last();
        return resultSet.getRow();
    }

    public List<Map<String, String>> fetchRecords(String query,
                                                  int Command_time_out) throws SQLException {
        if (connection == null) {
            throw new SQLException("Currently the connection with DataBase is not present");
        }

        List<Map<String, String>> rows = new ArrayList<>();
        ResultSet resultSet;

        resultSet = connection.createStatement().executeQuery(query);
        String data = "";
        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            Map<String, String> column = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                column.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
            }
            rows.add(column);
        }
        return rows;
    }


    public boolean verifyValue(String query, String columnName, String value) throws SQLException {
        if (connection == null) {
            throw new SQLException("Currently the connection with DataBase is not present");
        }

        ResultSet resultSet;
        resultSet = connection.createStatement().executeQuery(query);
        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (resultSet.getMetaData().getColumnName(i).equals(columnName)
                    && value.equals(resultSet.getString(i))) {
                    return true;
                }

            }
        }
        System.out.println("Unable to verify value-no" + value + "record found");
        return false;
    }

    public int executeQuery(String query, int commandTimeOut) throws SQLException {
        Statement stmt = connection.createStatement();

        boolean status = stmt.execute(query);
        return stmt.getUpdateCount();
    }
}

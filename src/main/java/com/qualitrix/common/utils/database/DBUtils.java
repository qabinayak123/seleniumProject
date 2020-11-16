package com.qualitrix.common.utils.database;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DBUtils extends AutoCloseable {
    void disconnectDB() throws SQLException;

    int executeQuery(String query, int commandTimeOut) throws SQLException,
        TransformerException, ParserConfigurationException;

    int updateDataBase(String query, int commandTimeOut) throws SQLException;

    boolean verifyValue(String query, String columnName, String value) throws SQLException;

    int getRowCount(String query, int commandTimeOut) throws SQLException;

    int getColumnCount(String query, int commandTimeOut) throws SQLException;

    List<Map<String, String>> fetchRecords(String query, int Command_time_out) throws SQLException;
}

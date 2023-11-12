package edu.systemanalysis.finalproject.util;

import java.sql.*;
import java.util.Properties;

public class DBMgr {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/final_project";

    static final String USER = "root";
    static final String PASS = "";

    static {
        try {
            Class.forName(DBMgr.JDBC_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public DBMgr() {

    }
    public static Connection getConnection() {
        Properties props = new Properties();
        props.setProperty("useSSL", "false");
        props.setProperty("serverTimezone", "UTC");
        props.setProperty("useUnicode", "true");
        props.setProperty("characterEncoding", "utf8");
        props.setProperty("user", DBMgr.USER);
        props.setProperty("password", DBMgr.PASS);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DBMgr.DB_URL, props);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Statement stm, Connection conn) {
        try {
            if (stm != null) stm.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rs, Statement stm, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] stringToArray(String data, String delimiter) {
        String[] result;
        result = data.split(delimiter);
        return result;
    }
}

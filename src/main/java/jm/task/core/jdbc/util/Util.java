package jm.task.core.jdbc.util;

import java.sql.*;

public class Util {
        private static final String url = "jdbc:mysql://localhost:3306/dbtest";
        private static final String user = "root";
        private static final String password = "SQLtest123";

    public static Connection getConnection() throws SQLException {
        return getConnection(url, user, password);
    }

    public static Connection getConnection(String url, String login, String password) throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }

}

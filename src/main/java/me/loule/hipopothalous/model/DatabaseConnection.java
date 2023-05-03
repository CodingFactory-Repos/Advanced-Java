package me.loule.hipopothalous.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://192.168.5.105:9000/clh7g0dqz0006agrr8lcre6aq";
    private static final String DB_USER = "clh7g0dqy0004agrr94nre8lv";
    private static final String DB_PASSWORD = "HYvRAuN9tMrRhz94RtLyaywm";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}

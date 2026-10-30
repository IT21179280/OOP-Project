package com.eventplanner.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Creates JDBC connections to the MySQL database.
 *
 * Connection details are read from db.properties which is located in
 * src/main/resources. They can also be supplied through the environment
 * variables DB_URL, DB_USERNAME and DB_PASSWORD.
 */
public class DBConnection {

    private static final String CONFIG_FILE = "db.properties";

    private static Properties config;
    private static boolean driverLoaded = false;

    private DBConnection() {
    }

    private static synchronized Properties getConfig() throws SQLException {
        if (config == null) {
            Properties properties = new Properties();
            try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                if (input == null) {
                    throw new SQLException("Database configuration file " + CONFIG_FILE + " was not found.");
                }
                properties.load(input);
            } catch (IOException e) {
                throw new SQLException("Could not read the database configuration file.", e);
            }
            config = properties;
        }
        return config;
    }

    private static void loadDriver() throws SQLException {
        if (!driverLoaded) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                driverLoaded = true;
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC driver was not found. Check the MySQL Connector/J dependency.", e);
            }
        }
    }

    /**
     * Opens and returns a new connection. The caller is responsible for
     * closing it (try-with-resources is used in all DAO classes).
     */
    public static Connection getConnection() throws SQLException {
        loadDriver();
        Properties properties = getConfig();

        String url = System.getenv("DB_URL");
        if (isBlank(url)) {
            url = properties.getProperty("db.url");
        }

        String username = System.getenv("DB_USERNAME");
        if (isBlank(username)) {
            username = properties.getProperty("db.username");
        }

        String password = System.getenv("DB_PASSWORD");
        if (isBlank(password)) {
            password = properties.getProperty("db.password");
        }

        if (isBlank(url) || isBlank(username)) {
            throw new SQLException("Database URL or username is not configured. Please edit db.properties.");
        }

        return DriverManager.getConnection(url, username, password);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

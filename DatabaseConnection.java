package com.iesce.maintenance.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String CONFIG_FILE = "db.properties";
    private static String url;
    private static String username;
    private static String password;

    static {
        loadConfig();
        loadDriver();
    }

    private static void loadConfig() {
        Properties props = new Properties();
        boolean loaded = false;

        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
                loaded = true;
                System.out.println("Loaded db.properties from: " + configFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Warning: Could not read db.properties: " + e.getMessage());
            }
        }

        if (!loaded) {
            InputStream classpathStream = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream(CONFIG_FILE);
            if (classpathStream != null) {
                try {
                    props.load(classpathStream);
                    loaded = true;
                    System.out.println("Loaded db.properties from classpath resources.");
                } catch (IOException e) {
                    System.err.println("Warning: Could not read db.properties from classpath: " + e.getMessage());
                }
            }
        }

        url = props.getProperty("db.url", "jdbc:mysql://localhost:3307/anex");
        username = props.getProperty("db.username", "root");
        password = props.getProperty("db.password", "");

        if (!loaded) {
            System.err.println("WARNING: db.properties not found. Using defaults.");
            System.err.println("  Create a db.properties file with: db.url, db.username, db.password");
        }

        System.out.println("Database URL: " + url);
        System.out.println("Database Username: " + username);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void close(java.sql.Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
        }
    }

    public static void close(java.sql.ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing result set: " + e.getMessage());
            }
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: MySQL JDBC driver not found: " + e.getMessage());
        }
    }
}

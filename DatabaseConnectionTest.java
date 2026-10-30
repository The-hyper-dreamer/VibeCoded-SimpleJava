package com.iesce.maintenance.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseConnectionTest {

    public static void main(String[] args) {
        System.out.println("=== Classroom Maintenance - Database Connection Test ===");
        System.out.println();

        boolean success = testConnection();

        System.out.println();
        if (success) {
            System.out.println("RESULT: Connection test PASSED. The application can connect to MySQL.");
            System.exit(0);
        } else {
            System.out.println("RESULT: Connection test FAILED. Please check your database configuration.");
            System.out.println();
            System.out.println("Troubleshooting tips:");
            System.out.println("  1. Ensure MySQL service is running.");
            System.out.println("  2. Check db.properties for correct url, username, and password.");
            System.out.println("  3. Ensure the database 'anex' exists.");
            System.out.println("  4. Verify MySQL is listening on the configured port (default: 3307).");
            System.exit(1);
        }
    }

    public static boolean testConnection() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            System.out.println("Attempting to connect to: " + DatabaseConnection.getUrl());
            System.out.println("Using username: " + DatabaseConnection.getUsername());

            conn = DatabaseConnection.getConnection();

            if (conn == null || !conn.isValid(5)) {
                System.out.println("ERROR: Could not establish a valid connection.");
                return false;
            }

            System.out.println("Connection established successfully.");

            String sql = "SELECT 1 AS connection_test";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                int result = rs.getInt("connection_test");
                System.out.println("Query 'SELECT 1' returned: " + result);
                if (result == 1) {
                    System.out.println("Database query executed successfully.");
                } else {
                    System.out.println("ERROR: Unexpected query result: " + result);
                    return false;
                }
            } else {
                System.out.println("ERROR: Query returned no results.");
                return false;
            }

            String catalog = conn.getCatalog();
            System.out.println("Connected to database catalog: " + catalog);
            if (catalog == null || catalog.isEmpty()) {
                System.out.println("WARNING: No database catalog selected.");
            }

            return true;

        } catch (SQLException e) {
            System.out.println("ERROR: Database connection failed.");
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println();
            System.out.println("Common causes:");
            System.out.println("  - MySQL service not running");
            System.out.println("  - Incorrect port (check my.ini - this server uses 3307)");
            System.out.println("  - Wrong username or password in db.properties");
            System.out.println("  - Database 'anex' does not exist yet");
            return false;
        } finally {
            DatabaseConnection.close(rs);
            DatabaseConnection.close(stmt);
            DatabaseConnection.close(conn);
        }
    }
}

package com.sqlsecurity;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class LoginTest {
    // Load environment variables from .env file
    private static final Dotenv dotenv = Dotenv.load();

    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    public static void main(String[] args) {
        // Test values for SQL Injection
        String usernameInjection = "admin' OR '1'='1"; // SQL Injection attack vector
        String password = "anything";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Testing with Statement:");
            loginWithStatement(conn, usernameInjection, password);

            System.out.println("\nTesting with PreparedStatement:");
            loginWithPreparedStatement(conn, usernameInjection, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to test login using Statement (Vulnerable to SQL Injection)
    public static void loginWithStatement(Connection conn, String username, String password) throws SQLException {
        // Vulnerable SQL query
        String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        System.out.println("Query: " + query);  // Debugging: print the SQL query

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                System.out.println("✅ Login successful (Statement)");
            } else {
                System.out.println("❌ Login failed (Statement)");
            }
        }
    }

    // Method to test login using PreparedStatement (Safe from SQL Injection)
    public static void loginWithPreparedStatement(Connection conn, String username, String password) throws SQLException {
        // Safe SQL query using PreparedStatement
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        System.out.println("Query (Prepared): " + query);  // Debugging: print the prepared query

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("✅ Login successful (PreparedStatement)");
                } else {
                    System.out.println("❌ Login failed (PreparedStatement)");
                }
            }
        }
    }
}

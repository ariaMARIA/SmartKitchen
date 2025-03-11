package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnection {

    // SQLite database URL
    private static final String DB_URL = "jdbc:sqlite:your_database.db"; // Ensure this path is correct

    // Method to create a connection to the SQLite database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Create a table for users
    public static void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, email TEXT)";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // Insert user into the users table
    public static void insertUser(String name, String email) {
        String insertSQL = "INSERT INTO users(name, email) VALUES(?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }

    // Query to get users from the users table
    public static void getUsers() {
        String querySQL = "SELECT id, name, email FROM users";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(querySQL)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }

    // Create a table for foods
    public static void createFoodTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS foods (id INTEGER PRIMARY KEY, name TEXT, calories INTEGER, health_benefits TEXT)";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Foods table created.");
        } catch (SQLException e) {
            System.out.println("Error creating foods table: " + e.getMessage());
        }
    }

    // Insert food into the foods table
    public static void insertFood(String name, int calories, String health_benefits) {
        String insertSQL = "INSERT INTO foods(name, calories, health_benefits) VALUES(?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, calories);
            pstmt.setString(3, health_benefits);
            pstmt.executeUpdate();
            System.out.println("Food inserted: " + name);
        } catch (SQLException e) {
            System.out.println("Error inserting food: " + e.getMessage());
        }
    }

    // Example method to add food data
    public static void addSampleFoodData() {
        // Insert sample food records
        insertFood("Apple", 52, "Rich in fiber and vitamin C");
        insertFood("Banana", 89, "Good source of potassium and vitamin B6");
    }
}

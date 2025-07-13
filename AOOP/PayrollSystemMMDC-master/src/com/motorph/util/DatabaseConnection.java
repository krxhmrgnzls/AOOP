package com.motorph.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    

    private static final String URL = "jdbc:mysql://localhost:3307/aoop_db"; 
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = "0914kmg@gnzls";
    
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully to aoop_db!");
        } catch (ClassNotFoundException ex) {
            System.err.println("MySQL Driver not found: " + ex.getMessage());
            throw new SQLException("Failed to load MySQL driver", ex);
        } catch (SQLException ex) {
            System.err.println("Database connection failed: " + ex.getMessage());
            throw ex;
        }
    }
    
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    // Test connection method
    public static boolean testConnection() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            System.out.println("Database connection test successful!");
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
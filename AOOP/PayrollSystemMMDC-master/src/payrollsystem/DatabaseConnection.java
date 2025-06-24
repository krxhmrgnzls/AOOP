package payrollsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Database configuration
    private static final String URL = "jdbc:mysql://localhost:3307/aoop";
    private static final String USERNAME = "root"; // Change as needed
    private static final String PASSWORD = "0914kmg@gnzls"; // Change as needed
    
    // Private constructor for Singleton pattern
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully!");
        } catch (ClassNotFoundException ex) {
            System.err.println("Database Connection Creation Failed : " + ex.getMessage());
            throw new SQLException("Failed to load MySQL driver", ex);
        }
    }
    
    /**
     * Get singleton instance of DatabaseConnection
     * @return DatabaseConnection instance
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Get the database connection
     * @return Connection object
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Close the database connection
     */
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
}
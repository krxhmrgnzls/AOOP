package payrollsystem;

import java.sql.*;
import java.util.ArrayList;

public abstract class Credentials extends AccountDetails {
    protected String userID, userPassword; // Changed from private to protected
    protected DatabaseConnection dbConnection;
    protected Connection connection;
    
    // Constructor
    public Credentials() {
        super();
        initializeDatabaseConnection();
    }
    
    public Credentials(String id, String password) {
        super();
        this.userID = id;
        this.userPassword = password;
        initializeDatabaseConnection();
    }
    
    private void initializeDatabaseConnection() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.connection = dbConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database connection: " + e.getMessage());
        }
    }
    
    // Getters and Setters
    public String getUserID() {
        return userID;
    }
    
    public String getUserPassword() {
        return userPassword;
    }
    
    // Check credentials against database
    public ArrayList<ArrayList<String>> checkCredentials() {
        ArrayList<ArrayList<String>> tempData = new ArrayList<>();
        
        String sql = "SELECT lc.employee_id, lc.employee_name, lc.password, lc.role, " +
                    "e.first_name, e.last_name FROM login_credentials lc " +
                    "LEFT JOIN employees e ON lc.employee_id = e.employee_id " +
                    "WHERE lc.employee_id = ? AND lc.password = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userID);
            pstmt.setString(2, userPassword);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                ArrayList<String> userData = new ArrayList<>();
                userData.add(rs.getString("employee_id"));
                userData.add(rs.getString("employee_name"));
                userData.add(rs.getString("password"));
                userData.add(rs.getString("role"));
                tempData.add(userData);
                
                System.out.println("Login successful for user: " + rs.getString("employee_name"));
            } else {
                System.out.println("Invalid credentials for user ID: " + userID);
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking credentials: " + e.getMessage());
        }
        
        return tempData;
    }
    
    // ===== LEGACY METHODS FOR BACKWARD COMPATIBILITY =====
    
    // Legacy method - now uses database instead of CSV
    public void setFilePath(String filePath) {
        // This method is kept for compatibility but no longer uses file paths
        System.out.println("Note: setFilePath() is deprecated. Using database instead of CSV files.");
    }
    
    // Legacy method - now retrieves from database instead of CSV
    public void retrivedDetails() {
        // This method is kept for compatibility but now loads from database
        System.out.println("Note: retrivedDetails() is deprecated. Use checkCredentials() instead.");
    }
    
    // Legacy method - returns empty list as data is now handled differently
    public ArrayList<ArrayList<String>> getDataList() {
        // Return credentials data from database
        ArrayList<ArrayList<String>> allCredentials = new ArrayList<>();
        
        String sql = "SELECT employee_id, employee_name, role FROM login_credentials ORDER BY employee_id";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(rs.getString("employee_id"));
                row.add(rs.getString("employee_name"));
                row.add(""); // Password placeholder (don't return actual passwords)
                row.add(rs.getString("role"));
                allCredentials.add(row);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting credentials list: " + e.getMessage());
        }
        
        return allCredentials;
    }
    
    // ===== NEW DATABASE METHODS =====
    
    // Add new credentials to database
    public boolean addCredentials(String employeeId, String employeeName, String password, String role) {
        String sql = "INSERT INTO login_credentials (employee_id, employee_name, password, role) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            pstmt.setString(2, employeeName);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding credentials: " + e.getMessage());
            return false;
        }
    }
    
    // Update existing credentials
    public boolean updateCredentials(String employeeId, String newPassword, String newRole) {
        String sql = "UPDATE login_credentials SET password = ?, role = ? WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, newRole);
            pstmt.setString(3, employeeId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating credentials: " + e.getMessage());
            return false;
        }
    }
    
    // Delete credentials
    public boolean deleteCredentials(String employeeId) {
        String sql = "DELETE FROM login_credentials WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting credentials: " + e.getMessage());
            return false;
        }
    }
    
    // Check if user exists
    public boolean userExists(String employeeId) {
        String sql = "SELECT COUNT(*) FROM login_credentials WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
        }
        
        return false;
    }
    
    // Get user role
    public String getUserRole(String employeeId) {
        String sql = "SELECT role FROM login_credentials WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("role");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user role: " + e.getMessage());
        }
        
        return "";
    }
    
    // Validate credentials (returns user data if valid)
    public ArrayList<String> validateLogin(String employeeId, String password) {
        ArrayList<ArrayList<String>> result = checkCredentials();
        return result.isEmpty() ? new ArrayList<>() : result.get(0);
    }
}
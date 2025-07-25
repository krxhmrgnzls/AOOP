package com.motorph.gui;

import com.motorph.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class Login {
    private int employeeID;
    private String userEmail;
    private String userPassword;
    private Connection connection;
    
    public Login() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Login(int employeeID, String userEmail, String userPassword) {
        this();
        this.employeeID = employeeID;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
    
    public boolean authenticateDB(int employeeId, String password) {
        String sql = "SELECT lc.*, e.first_name, e.last_name FROM login_credentials lc " +
                    "JOIN employee_profile_view e ON lc.employee_id = e.employee_id " +
                    "WHERE lc.employee_id = ? AND lc.password = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<ArrayList<String>> getUserDetails(int employeeId, String password) {
        ArrayList<ArrayList<String>> userDetails = new ArrayList<>();
        
        String sql = "SELECT employee_id, full_name, password, role FROM employee_credentials_view WHERE employee_id = ? AND password = ?";
        
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                ArrayList<String> details = new ArrayList<>();
                details.add(String.valueOf(rs.getInt("employee_id")));  
                details.add(rs.getString("full_name"));    
                details.add("");
                details.add(rs.getString("role"));
                details.add(rs.getString("full_name"));    
                
                userDetails.add(details);
                
                System.out.println("Login successful for: " + rs.getString("full_name") + " (" + rs.getString("role") + ")");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return userDetails;
    }
    
    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }
}
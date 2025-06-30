package payrollsystem;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class LeaveDAO {
    private Connection connection;
    
    public LeaveDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // CREATE - Submit new leave request
    public boolean submitLeaveRequest(int employeeId, String leaveType, LocalDate fromDate, 
                                    LocalDate toDate, double numberOfDays, String reason) {
        String sql = "INSERT INTO leave_requests (employee_id, date_filed, leave_type, from_date, " +
                    "to_date, number_of_days, reason, status) VALUES (?, CURDATE(), ?, ?, ?, ?, ?, 'Pending')";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, leaveType);
            pstmt.setDate(3, Date.valueOf(fromDate));
            pstmt.setDate(4, Date.valueOf(toDate));
            pstmt.setDouble(5, numberOfDays);
            pstmt.setString(6, reason);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // READ - Get leave requests by employee
    public List<ArrayList<String>> getLeaveRequestsByEmployee(int employeeId) {
    List<ArrayList<String>> leaveRequests = new ArrayList<>();
    
    // Check connection first
    if (connection == null) {
        System.err.println("ERROR: Database connection is null in LeaveDAO!");
        return leaveRequests;
    }
    
    String query = "SELECT l.*, e.first_name, e.last_name " +
                   "FROM leaves l " +
                   "JOIN employees e ON l.employee_id = e.employee_id " +
                   "WHERE l.employee_id = ? " +
                   "ORDER BY l.date_filed DESC";
    
    System.out.println("DEBUG LeaveDAO: Executing query for employee " + employeeId);
    
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, employeeId);
        ResultSet rs = stmt.executeQuery();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        while (rs.next()) {
            ArrayList<String> row = new ArrayList<>();
            
            Date dateFiled = rs.getDate("date_filed");
            Date fromDate = rs.getDate("from_date");
            Date toDate = rs.getDate("to_date");
            
            row.add(displayFormat.format(dateFiled)); // DATE FILED
            row.add(rs.getString("leave_type")); // TYPE OF REQUEST  
            row.add(displayFormat.format(fromDate)); // PERIOD FROM
            row.add(displayFormat.format(toDate)); // PERIOD TO
            row.add(String.valueOf(rs.getDouble("number_of_days"))); // NUMBER OF DAYS
            row.add(rs.getString("reason")); // REASON
            row.add(rs.getString("status")); // STATUS
            
            leaveRequests.add(row);
            System.out.println("DEBUG: Added leave request: " + row);
        }
        
        rs.close();
        
    } catch (SQLException e) {
        System.err.println("ERROR in LeaveDAO.getLeaveRequestsByEmployee: " + e.getMessage());
        e.printStackTrace();
    }
    
    System.out.println("DEBUG LeaveDAO: Returning " + leaveRequests.size() + " leave requests");
    return leaveRequests;
}
    
    // READ - Get all pending leave requests (for supervisors)
    public List<ArrayList<String>> getPendingLeaveRequests() {
        List<ArrayList<String>> requests = new ArrayList<>();
        String sql = "SELECT lr.*, e.first_name, e.last_name FROM leave_requests lr " +
                    "JOIN employees e ON lr.employee_id = e.employee_id " +
                    "WHERE lr.status = 'Pending' ORDER BY lr.date_filed ASC";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(rs.getInt("leave_id")));
                row.add(String.valueOf(rs.getInt("employee_id")));
                row.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                row.add(rs.getDate("date_filed").toString());
                row.add(rs.getString("leave_type"));
                row.add(rs.getDate("from_date").toString());
                row.add(rs.getDate("to_date").toString());
                row.add(String.valueOf(rs.getDouble("number_of_days")));
                row.add(rs.getString("reason"));
                row.add(rs.getString("status"));
                requests.add(row);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return requests;
    }
    
    // UPDATE - Approve or deny leave request
    public boolean updateLeaveRequestStatus(int leaveId, String status) {
        String sql = "UPDATE leave_requests SET status = ? WHERE leave_id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, leaveId);
            
            int result = pstmt.executeUpdate();
            
            // If approved, update leave balance
            if (result > 0 && status.equals("Approved")) {
                updateLeaveBalance(leaveId);
            }
            
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // UPDATE - Deduct from leave balance when request is approved
    private void updateLeaveBalance(int leaveId) {
        String sql = "UPDATE leave_balances lb " +
                    "JOIN leave_requests lr ON lb.employee_id = lr.employee_id " +
                    "SET lb.vacation_leave = CASE " +
                    "    WHEN lr.leave_type = 'Vacation Leave' THEN lb.vacation_leave - lr.number_of_days " +
                    "    ELSE lb.vacation_leave END, " +
                    "lb.sick_leave = CASE " +
                    "    WHEN lr.leave_type = 'Sick Leave' THEN lb.sick_leave - lr.number_of_days " +
                    "    ELSE lb.sick_leave END " +
                    "WHERE lr.leave_id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, leaveId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // READ - Get leave balance by employee
    public ArrayList<String> getLeaveBalance(int employeeId) {
        ArrayList<String> balance = new ArrayList<>();
        String sql = "SELECT lb.*, e.first_name, e.last_name FROM leave_balances lb " +
                    "JOIN employees e ON lb.employee_id = e.employee_id " +
                    "WHERE lb.employee_id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                balance.add(String.valueOf(rs.getInt("employee_id")));
                balance.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                balance.add(String.valueOf(rs.getDouble("vacation_leave")));
                balance.add(String.valueOf(rs.getDouble("sick_leave")));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return balance;
    }
    
    // READ - Get all leave balances (for reports)
    public List<ArrayList<String>> getAllLeaveBalances() {
        List<ArrayList<String>> balances = new ArrayList<>();
        String sql = "SELECT lb.*, e.first_name, e.last_name FROM leave_balances lb " +
                    "JOIN employees e ON lb.employee_id = e.employee_id " +
                    "ORDER BY e.last_name, e.first_name";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(rs.getInt("employee_id")));
                row.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                row.add(String.valueOf(rs.getDouble("vacation_leave")));
                row.add(String.valueOf(rs.getDouble("sick_leave")));
                balances.add(row);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return balances;
    }
    
    // UTILITY - Check if employee has sufficient leave balance
    public boolean hasSufficientBalance(int employeeId, String leaveType, double requestedDays) {
        String sql = "SELECT vacation_leave, sick_leave FROM leave_balances WHERE employee_id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                if (leaveType.equals("Vacation Leave")) {
                    return rs.getDouble("vacation_leave") >= requestedDays;
                } else if (leaveType.equals("Sick Leave")) {
                    return rs.getDouble("sick_leave") >= requestedDays;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
package payrollsystem;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class OvertimeDAO {
    private Connection connection;
    
    public OvertimeDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
            
            if (connection == null) {
                System.err.println("ERROR: OvertimeDAO connection is NULL!");
            } else if (connection.isClosed()) {
                System.err.println("ERROR: OvertimeDAO connection is CLOSED!");
            } else {
                System.out.println("OvertimeDAO initialized successfully with valid connection");
                
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM overtime_requests");
                if (rs.next()) {
                    System.out.println("Total overtime_requests in database: " + rs.getInt(1));
                }
                rs.close();
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Error initializing OvertimeDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
 
    public List<ArrayList<String>> getOvertimeRequestsByEmployee(int employeeId) {
        List<ArrayList<String>> overtimeRequests = new ArrayList<>();
        
        String query = "SELECT date_filed, from_time, to_time, number_of_days, reason, status " +
                       "FROM overtime_requests " +
                       "WHERE employee_id = ? " +
                       "ORDER BY date_filed DESC";
        
        System.out.println("DEBUG OvertimeDAO: Executing query for employee " + employeeId);
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            
            SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                
                Date dateFiled = rs.getDate("date_filed");
                Timestamp fromTime = rs.getTimestamp("from_time");
                Timestamp toTime = rs.getTimestamp("to_time");
                
                row.add(displayFormat.format(dateFiled)); // DATE FILED
                row.add("Overtime"); // TYPE OF REQUEST  
                row.add(datetimeFormat.format(fromTime)); // PERIOD FROM
                row.add(datetimeFormat.format(toTime)); // PERIOD TO
                row.add(String.valueOf(rs.getDouble("number_of_days"))); // NUMBER OF DAYS
                row.add(rs.getString("reason")); // REASON
                row.add(rs.getString("status")); // STATUS
                
                overtimeRequests.add(row);
                System.out.println("DEBUG: Added overtime request: " + row);
            }
            
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("ERROR in OvertimeDAO.getOvertimeRequestsByEmployee: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("DEBUG OvertimeDAO: Returning " + overtimeRequests.size() + " overtime requests");
        return overtimeRequests;
    }
    
    public boolean submitOvertimeRequest(int employeeId, LocalDateTime fromTime, 
                                       LocalDateTime toTime, double numberOfDays, String reason) {
        String sql = "INSERT INTO overtime_requests (employee_id, date_filed, type_request, from_time, " +
                    "to_time, number_of_days, reason, status) VALUES (?, CURDATE(), 'Overtime', ?, ?, ?, ?, 'Pending')";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setTimestamp(2, Timestamp.valueOf(fromTime));
            pstmt.setTimestamp(3, Timestamp.valueOf(toTime));
            pstmt.setDouble(4, numberOfDays);
            pstmt.setString(5, reason);
            
            int result = pstmt.executeUpdate();
            boolean success = result > 0;
            
            if (success) {
                System.out.println("Overtime request saved to database for employee " + employeeId);
            } else {
                System.err.println("Failed to save overtime request for employee " + employeeId);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Database error submitting overtime request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<ArrayList<String>> getPendingOvertimeRequests() {
        List<ArrayList<String>> requests = new ArrayList<>();
        String sql = "SELECT o.*, e.first_name, e.last_name FROM overtime_requests o " +
                    "JOIN employees e ON o.employee_id = e.employee_id " +
                    "WHERE o.status = 'Pending' ORDER BY o.date_filed ASC";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(rs.getInt("overtime_id")));
                row.add(String.valueOf(rs.getInt("employee_id")));
                row.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                row.add(displayFormat.format(rs.getDate("date_filed")));
                row.add(datetimeFormat.format(rs.getTimestamp("from_time")));
                row.add(datetimeFormat.format(rs.getTimestamp("to_time")));
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
    
    public boolean updateOvertimeRequestStatus(int overtimeId, String status) {
        String sql = "UPDATE overtime_requests SET status = ? WHERE overtime_id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, overtimeId);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean cancelOvertimeRequest(int overtimeId) {
        String sql = "DELETE FROM overtime_requests WHERE overtime_id = ? AND status = 'Pending'";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, overtimeId);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<ArrayList<String>> getApprovedOvertimeForPayroll(String payrollPeriod) {
        List<ArrayList<String>> approvedOvertime = new ArrayList<>();
        return approvedOvertime;
    }

    public double getOvertimeHours(int employeeId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(number_of_days * 8) as total_hours FROM overtime_requests " +
                    "WHERE employee_id = ? AND status = 'Approved' " +
                    "AND from_time >= ? AND to_time <= ?";
        
        double totalHours = 0.0;
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalHours = rs.getDouble("total_hours");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return totalHours;
    }
}
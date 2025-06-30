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
            System.out.println("OvertimeDAO initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing OvertimeDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Submit a new overtime request to the database
     */
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
    
    /**
     * Get all overtime requests for a specific employee - FIXED SQL QUERY
     */
    public List<ArrayList<String>> getOvertimeRequestsByEmployee(int employeeId) {
    List<ArrayList<String>> overtimeRequests = new ArrayList<>();
        String query = "SELECT o.*, e.first_name, e.last_name " +
                       "FROM overtime o " +
                       "JOIN employees e ON o.employee_id = e.employee_id " +
                       "WHERE o.employee_id = ? " +
                       "ORDER BY o.date_filed DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");

            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();

                // Format dates without time
                Date dateFiled = rs.getDate("date_filed");
                Timestamp fromTime = rs.getTimestamp("from_time");
                Timestamp toTime = rs.getTimestamp("to_time");

                row.add(displayFormat.format(dateFiled)); // DATE FILED
                row.add("Overtime Request"); // TYPE OF REQUEST

                // Format overtime dates without the time portion
                if (fromTime != null) {
                    row.add(displayFormat.format(new Date(fromTime.getTime()))); // PERIOD FROM (date only)
                } else {
                    row.add("");
                }

                if (toTime != null) {
                    row.add(displayFormat.format(new Date(toTime.getTime()))); // PERIOD TO (date only)
                } else {
                    row.add("");
                }

                row.add(String.valueOf(rs.getDouble("number_of_hours"))); // NUMBER OF DAYS/HOURS
                row.add(rs.getString("reason")); // REASON
                row.add(rs.getString("status")); // STATUS

                overtimeRequests.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving overtime requests: " + e.getMessage());
            e.printStackTrace();
        }

        return overtimeRequests;
}
    
    /**
     * Get all pending overtime requests (for supervisors) - FIXED SQL QUERY
     */
    public List<ArrayList<String>> getPendingOvertimeRequests() {
        List<ArrayList<String>> requests = new ArrayList<>();
        // FIXED: Corrected the table alias
        String sql = "SELECT ot.*, e.first_name, e.last_name FROM overtime_requests ot " +
                    "JOIN employees e ON ot.employee_id = e.employee_id " +
                    "WHERE ot.status = 'Pending' ORDER BY ot.date_filed ASC";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(rs.getInt("overtime_id")));
                row.add(String.valueOf(rs.getInt("employee_id")));
                row.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                row.add(rs.getDate("date_filed").toString());
                row.add(rs.getString("type_request"));
                row.add(rs.getTimestamp("from_time").toString());
                row.add(rs.getTimestamp("to_time").toString());
                row.add(String.valueOf(rs.getDouble("number_of_days")));
                row.add(rs.getString("reason"));
                row.add(rs.getString("status"));
                requests.add(row);
            }
            
            System.out.println("Retrieved " + requests.size() + " pending overtime requests");
            
        } catch (SQLException e) {
            System.err.println("Database error getting pending overtime requests: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Update overtime request status (approve/deny)
     */
    public boolean updateOvertimeRequestStatus(int overtimeId, String status) {
        String sql = "UPDATE overtime_requests SET status = ? WHERE overtime_id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, overtimeId);
            
            int result = pstmt.executeUpdate();
            boolean success = result > 0;
            
            if (success) {
                System.out.println("Overtime request " + overtimeId + " status updated to: " + status);
            } else {
                System.err.println("Failed to update overtime request " + overtimeId);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Database error updating overtime status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
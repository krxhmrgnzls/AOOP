package payrollsystem;

import java.sql.*;

public class LeaveDAO {
    private Connection connection;
    
    public LeaveDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Create leave request
    public boolean createLeaveRequest(LeaveRequest leave) {
        String sql = "INSERT INTO leave_requests (employee_id, date_filed, leave_type, " +
                    "from_date, to_date, number_of_days, reason, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, leave.getEmployeeId());
            pstmt.setDate(2, Date.valueOf(leave.getDateFiled()));
            pstmt.setString(3, leave.getLeaveType());
            pstmt.setDate(4, Date.valueOf(leave.getFromDate()));
            pstmt.setDate(5, Date.valueOf(leave.getToDate()));
            pstmt.setDouble(6, leave.getNumberOfDays());
            pstmt.setString(7, leave.getReason());
            pstmt.setString(8, "Pending");
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get leave balance
    public LeaveBalance getLeaveBalance(int employeeId) {
        String sql = "SELECT * FROM leave_balances WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                LeaveBalance balance = new LeaveBalance();
                balance.setEmployeeId(rs.getInt("employee_id"));
                balance.setVacationLeave(rs.getDouble("vacation_leave"));
                balance.setSickLeave(rs.getDouble("sick_leave"));
                return balance;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Update leave balance
    public boolean updateLeaveBalance(int employeeId, double vacationLeave, double sickLeave) {
        String sql = "UPDATE leave_balances SET vacation_leave = ?, sick_leave = ? WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, vacationLeave);
            pstmt.setDouble(2, sickLeave);
            pstmt.setInt(3, employeeId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update leave request status
    public boolean updateLeaveStatus(int leaveId, String status) {
        String sql = "UPDATE leave_requests SET status = ? WHERE leave_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, leaveId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

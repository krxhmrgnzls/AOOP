package payrollsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    private Connection connection;
    
    public AttendanceDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // CREATE - Add new attendance record
    public boolean addAttendance(AttendanceRecord attendance) {
        String sql = "INSERT INTO attendance (employee_id, log_date, login_time, logout_time, " +
                    "submitted_to_supervisor, submitted_to_payroll, remarks) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, attendance.getEmployeeId());
            pstmt.setDate(2, new java.sql.Date(attendance.getLogDate().getTime()));
            pstmt.setTime(3, attendance.getLoginTime());
            pstmt.setTime(4, attendance.getLogoutTime());
            pstmt.setBoolean(5, attendance.isSubmittedToSupervisor());
            pstmt.setBoolean(6, attendance.isSubmittedToPayroll());
            pstmt.setString(7, attendance.getRemarks());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // READ - Get attendance by employee ID and date range
    public List<AttendanceRecord> getAttendanceByEmployee(int employeeId, Date startDate, Date endDate) {
        List<AttendanceRecord> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, e.first_name, e.last_name FROM attendance a " +
                    "JOIN employees e ON a.employee_id = e.employee_id " +
                    "WHERE a.employee_id = ? AND a.log_date BETWEEN ? AND ? " +
                    "ORDER BY a.log_date DESC";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                AttendanceRecord attendance = new AttendanceRecord();
                attendance.setAttendanceId(rs.getInt("attendance_id"));
                attendance.setEmployeeId(rs.getInt("employee_id"));
                attendance.setEmployeeName(rs.getString("first_name") + " " + rs.getString("last_name"));
                attendance.setLogDate(rs.getDate("log_date"));
                attendance.setLoginTime(rs.getTime("login_time"));
                attendance.setLogoutTime(rs.getTime("logout_time"));
                attendance.setSubmittedToSupervisor(rs.getBoolean("submitted_to_supervisor"));
                attendance.setSubmittedToPayroll(rs.getBoolean("submitted_to_payroll"));
                attendance.setRemarks(rs.getString("remarks"));
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }
    
    // READ - Get all attendance records for payroll processing
    public List<AttendanceRecord> getAttendanceForPayroll(String payrollPeriod) {
        List<AttendanceRecord> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, e.first_name, e.last_name FROM attendance a " +
                    "JOIN employees e ON a.employee_id = e.employee_id " +
                    "WHERE a.submitted_to_payroll = true ORDER BY a.employee_id, a.log_date";
        
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                AttendanceRecord attendance = new AttendanceRecord();
                attendance.setAttendanceId(rs.getInt("attendance_id"));
                attendance.setEmployeeId(rs.getInt("employee_id"));
                attendance.setEmployeeName(rs.getString("first_name") + " " + rs.getString("last_name"));
                attendance.setLogDate(rs.getDate("log_date"));
                attendance.setLoginTime(rs.getTime("login_time"));
                attendance.setLogoutTime(rs.getTime("logout_time"));
                attendance.setSubmittedToSupervisor(rs.getBoolean("submitted_to_supervisor"));
                attendance.setSubmittedToPayroll(rs.getBoolean("submitted_to_payroll"));
                attendance.setRemarks(rs.getString("remarks"));
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }
    
    // UPDATE - Update attendance record
    public boolean updateAttendance(AttendanceRecord attendance) {
        String sql = "UPDATE attendance SET log_date=?, login_time=?, logout_time=?, " +
                    "submitted_to_supervisor=?, submitted_to_payroll=?, remarks=? WHERE attendance_id=?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(attendance.getLogDate().getTime()));
            pstmt.setTime(2, attendance.getLoginTime());
            pstmt.setTime(3, attendance.getLogoutTime());
            pstmt.setBoolean(4, attendance.isSubmittedToSupervisor());
            pstmt.setBoolean(5, attendance.isSubmittedToPayroll());
            pstmt.setString(6, attendance.getRemarks());
            pstmt.setInt(7, attendance.getAttendanceId());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Delete attendance record
    public boolean deleteAttendance(int attendanceId) {
        String sql = "DELETE FROM attendance WHERE attendance_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, attendanceId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // APPROVE - Submit attendance to supervisor
    public boolean submitToSupervisor(int attendanceId) {
        String sql = "UPDATE attendance SET submitted_to_supervisor = true WHERE attendance_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, attendanceId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // APPROVE - Submit attendance to payroll
    public boolean submitToPayroll(int attendanceId) {
        String sql = "UPDATE attendance SET submitted_to_payroll = true WHERE attendance_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, attendanceId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // BULK APPROVAL - Submit multiple attendance records to payroll
    public boolean bulkSubmitToPayroll(List<Integer> attendanceIds) {
        String sql = "UPDATE attendance SET submitted_to_payroll = true WHERE attendance_id IN (";
        
        // Build IN clause
        for (int i = 0; i < attendanceIds.size(); i++) {
            sql += "?";
            if (i < attendanceIds.size() - 1) sql += ",";
        }
        sql += ")";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < attendanceIds.size(); i++) {
                pstmt.setInt(i + 1, attendanceIds.get(i));
            }
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ANALYTICS - Get total working hours for employee in period
    public double getTotalWorkingHours(int employeeId, Date startDate, Date endDate) {
        String sql = "SELECT SUM(TIMESTAMPDIFF(HOUR, login_time, logout_time)) as total_hours " +
                    "FROM attendance WHERE employee_id = ? AND log_date BETWEEN ? AND ? " +
                    "AND logout_time IS NOT NULL";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_hours");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // ANALYTICS - Get attendance summary for employee
    public AttendanceSummary getAttendanceSummary(int employeeId, Date startDate, Date endDate) {
        AttendanceSummary summary = new AttendanceSummary();
        
        String sql = "SELECT COUNT(*) as total_days, " +
                    "SUM(CASE WHEN login_time <= '08:00:00' THEN 1 ELSE 0 END) as on_time, " +
                    "SUM(CASE WHEN login_time > '08:00:00' THEN 1 ELSE 0 END) as late, " +
                    "SUM(TIMESTAMPDIFF(HOUR, login_time, logout_time)) as total_hours " +
                    "FROM attendance WHERE employee_id = ? AND log_date BETWEEN ? AND ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                summary.setTotalDays(rs.getInt("total_days"));
                summary.setOnTimeDays(rs.getInt("on_time"));
                summary.setLateDays(rs.getInt("late"));
                summary.setTotalHours(rs.getDouble("total_hours"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summary;
    }
}
package com.motorph.service;

import com.motorph.model.AttendanceRecord;
import com.motorph.util.DatabaseConnection;
import com.motorph.dao.AttendanceDAO;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class AttendanceReportGenerator {
    private Connection connection;
    private AttendanceDAO attendanceDAO;
    
    public AttendanceReportGenerator() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
            this.attendanceDAO = new AttendanceDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public File generateDailyReport(java.util.Date selectedDate) {
        try {
            File reportDir = new File("reports/attendance/daily");
            reportDir.mkdirs();
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dateStr = sdf.format(selectedDate);
            
            String filename = "Daily_Attendance_" + dateStr + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            writer.println("MOTORPH DAILY ATTENDANCE REPORT");
            writer.println("Date: " + dateStr);
            writer.println("");
            writer.println("Employee ID,Employee Name,Login Time,Logout Time,Hours Worked,Status,Remarks");
            
            String sql = "SELECT a.*, e.first_name, e.last_name FROM attendance a " +
                        "JOIN employees e ON a.employee_id = e.employee_id " +
                        "WHERE a.log_date = ? ORDER BY a.employee_id";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(selectedDate.getTime()));
            ResultSet rs = pstmt.executeQuery();
            
            int totalPresent = 0;
            int totalLate = 0;
            
            while (rs.next()) {
                String loginTime = rs.getTime("login_time") != null ? rs.getTime("login_time").toString() : "ABSENT";
                String logoutTime = rs.getTime("logout_time") != null ? rs.getTime("logout_time").toString() : "";
                
                double hoursWorked = 0;
                String status = "ABSENT";
                
                if (rs.getTime("login_time") != null && rs.getTime("logout_time") != null) {
                    long diff = rs.getTime("logout_time").getTime() - rs.getTime("login_time").getTime();
                    hoursWorked = diff / (1000.0 * 60 * 60);
                    totalPresent++;
                    
                    if (rs.getTime("login_time").after(java.sql.Time.valueOf("08:00:00"))) {
                        status = "LATE";
                        totalLate++;
                    } else {
                        status = "ON TIME";
                    }
                }
                
                writer.println(String.format("%d,\"%s %s\",%s,%s,%.2f,%s,\"%s\"",
                    rs.getInt("employee_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    loginTime,
                    logoutTime,
                    hoursWorked,
                    status,
                    rs.getString("remarks") != null ? rs.getString("remarks") : ""
                ));
            }
            
            writer.println("");
            writer.println("SUMMARY:");
            writer.println("Total Present:," + totalPresent);
            writer.println("Total Late:," + totalLate);
            writer.println("On Time:," + (totalPresent - totalLate));
            
            writer.close();
            System.out.println("Daily attendance report generated");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public File generateWeeklyReport(java.util.Date weekStart) {
        try {
            File reportDir = new File("reports/attendance/weekly");
            reportDir.mkdirs();

            Calendar cal = Calendar.getInstance();
            cal.setTime(weekStart);
            cal.add(Calendar.DAY_OF_MONTH, 6);
            java.util.Date weekEnd = cal.getTime();
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String filename = "Weekly_Attendance_" + sdf.format(weekStart) + "_to_" + sdf.format(weekEnd) + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            writer.println("MOTORPH WEEKLY ATTENDANCE REPORT");
            writer.println("Week: " + sdf.format(weekStart) + " to " + sdf.format(weekEnd));
            writer.println("");
            writer.println("Employee ID,Employee Name,Days Present,Days Late,Days Absent,Total Hours,Average Hours/Day");
            
            String sql = "SELECT e.employee_id, e.first_name, e.last_name, " +
                        "COUNT(CASE WHEN a.login_time IS NOT NULL THEN 1 END) as days_present, " +
                        "COUNT(CASE WHEN a.login_time > '08:00:00' THEN 1 END) as days_late, " +
                        "SUM(CASE WHEN a.login_time IS NOT NULL AND a.logout_time IS NOT NULL " +
                        "THEN TIMESTAMPDIFF(HOUR, a.login_time, a.logout_time) ELSE 0 END) as total_hours " +
                        "FROM employees e " +
                        "LEFT JOIN attendance a ON e.employee_id = a.employee_id " +
                        "AND a.log_date BETWEEN ? AND ? " +
                        "GROUP BY e.employee_id, e.first_name, e.last_name " +
                        "ORDER BY e.employee_id";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(weekStart.getTime()));
            pstmt.setDate(2, new java.sql.Date(weekEnd.getTime()));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int daysPresent = rs.getInt("days_present");
                int daysLate = rs.getInt("days_late");
                int daysAbsent = 7 - daysPresent; // Assuming 7-day work week
                double totalHours = rs.getDouble("total_hours");
                double avgHours = daysPresent > 0 ? totalHours / daysPresent : 0;
                
                writer.println(String.format("%d,\"%s %s\",%d,%d,%d,%.2f,%.2f",
                    rs.getInt("employee_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    daysPresent,
                    daysLate,
                    daysAbsent,
                    totalHours,
                    avgHours
                ));
            }
            
            writer.close();
            System.out.println("Weekly attendance report generated");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public File generateMonthlyAttendanceReport(String month, int year) {
        try {
            File reportDir = new File("reports/attendance/monthly");
            reportDir.mkdirs();
            
            String filename = "Monthly_Attendance_" + month + "_" + year + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            writer.println("MOTORPH MONTHLY ATTENDANCE REPORT");
            writer.println("Month: " + month + "/" + year);
            writer.println("");
            writer.println("Employee ID,Employee Name,Days Present,Days Late,Days Absent,Total Hours,Attendance Rate %");
            
            String sql = "SELECT e.employee_id, e.first_name, e.last_name, " +
                        "COUNT(CASE WHEN a.login_time IS NOT NULL THEN 1 END) as days_present, " +
                        "COUNT(CASE WHEN a.login_time > '08:00:00' THEN 1 END) as days_late, " +
                        "SUM(CASE WHEN a.login_time IS NOT NULL AND a.logout_time IS NOT NULL " +
                        "THEN TIMESTAMPDIFF(HOUR, a.login_time, a.logout_time) ELSE 0 END) as total_hours " +
                        "FROM employees e " +
                        "LEFT JOIN attendance a ON e.employee_id = a.employee_id " +
                        "AND MONTH(a.log_date) = ? AND YEAR(a.log_date) = ? " +
                        "GROUP BY e.employee_id, e.first_name, e.last_name " +
                        "ORDER BY e.employee_id";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(month));
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            
            Calendar cal = Calendar.getInstance();
            cal.set(year, Integer.parseInt(month) - 1, 1);
            int workingDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            while (rs.next()) {
                int daysPresent = rs.getInt("days_present");
                int daysLate = rs.getInt("days_late");
                int daysAbsent = workingDays - daysPresent;
                double totalHours = rs.getDouble("total_hours");
                double attendanceRate = (double) daysPresent / workingDays * 100;
                
                writer.println(String.format("%d,\"%s %s\",%d,%d,%d,%.2f,%.1f",
                    rs.getInt("employee_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    daysPresent,
                    daysLate,
                    daysAbsent,
                    totalHours,
                    attendanceRate
                ));
            }
            
            writer.close();
            System.out.println("Monthly attendance report generated");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File generateEmployeeAttendanceHistory(int employeeId, java.util.Date startDate, java.util.Date endDate) {
        try {
            File reportDir = new File("reports/attendance/employee");
            reportDir.mkdirs();
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String filename = "Employee_" + employeeId + "_Attendance_" + sdf.format(startDate) + "_to_" + sdf.format(endDate) + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            String empSql = "SELECT first_name, last_name FROM employees WHERE employee_id = ?";
            PreparedStatement empStmt = connection.prepareStatement(empSql);
            empStmt.setInt(1, employeeId);
            ResultSet empRs = empStmt.executeQuery();
            
            String employeeName = "Unknown Employee";
            if (empRs.next()) {
                employeeName = empRs.getString("first_name") + " " + empRs.getString("last_name");
            }

            writer.println("MOTORPH EMPLOYEE ATTENDANCE HISTORY");
            writer.println("Employee: " + employeeName + " (ID: " + employeeId + ")");
            writer.println("Period: " + sdf.format(startDate) + " to " + sdf.format(endDate));
            writer.println("");
            writer.println("Date,Login Time,Logout Time,Hours Worked,Status,Remarks");
            
            List<AttendanceRecord> attendance = attendanceDAO.getAttendanceByEmployee(
                employeeId, new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
            
            double totalHours = 0;
            int daysPresent = 0;
            int daysLate = 0;
            
            for (AttendanceRecord record : attendance) {
                String loginTime = record.getLoginTime() != null ? record.getLoginTime().toString() : "";
                String logoutTime = record.getLogoutTime() != null ? record.getLogoutTime().toString() : "";
                double hoursWorked = record.getWorkingHours();
                
                String status = "ABSENT";
                if (record.getLoginTime() != null) {
                    daysPresent++;
                    if (record.isLate()) {
                        status = "LATE";
                        daysLate++;
                    } else {
                        status = "ON TIME";
                    }
                }
                
                writer.println(String.format("%s,%s,%s,%.2f,%s,\"%s\"",
                    record.getLogDate().toString(),
                    loginTime,
                    logoutTime,
                    hoursWorked,
                    status,
                    record.getRemarks() != null ? record.getRemarks() : ""
                ));
                
                totalHours += hoursWorked;
            }
            
            writer.println("");
            writer.println("SUMMARY:");
            writer.println("Total Days Present:," + daysPresent);
            writer.println("Total Days Late:," + daysLate);
            writer.println("Total Hours Worked:," + String.format("%.2f", totalHours));
            writer.println("Average Hours/Day:," + String.format("%.2f", daysPresent > 0 ? totalHours / daysPresent : 0));
            
            writer.close();
            System.out.println("✅ Employee attendance history generated");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public File generateLateAbsentReport(java.util.Date reportDate) {
        try {
            File reportDir = new File("reports/attendance/late-absent");
            reportDir.mkdirs();
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String filename = "Late_Absent_Report_" + sdf.format(reportDate) + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);

            writer.println("MOTORPH LATE/ABSENT EMPLOYEES REPORT");
            writer.println("Date: " + sdf.format(reportDate));
            writer.println("");
            writer.println("LATE EMPLOYEES:");
            writer.println("Employee ID,Employee Name,Login Time,Minutes Late");
            
            String lateSql = "SELECT a.*, e.first_name, e.last_name FROM attendance a " +
                           "JOIN employees e ON a.employee_id = e.employee_id " +
                           "WHERE a.log_date = ? AND a.login_time > '08:00:00' " +
                           "ORDER BY a.login_time DESC";
            
            PreparedStatement lateStmt = connection.prepareStatement(lateSql);
            lateStmt.setDate(1, new java.sql.Date(reportDate.getTime()));
            ResultSet lateRs = lateStmt.executeQuery();
            
            int lateCount = 0;
            while (lateRs.next()) {
                long minutesLate = (lateRs.getTime("login_time").getTime() - 
                                  java.sql.Time.valueOf("08:00:00").getTime()) / (1000 * 60);
                
                writer.println(String.format("%d,\"%s %s\",%s,%d",
                    lateRs.getInt("employee_id"),
                    lateRs.getString("first_name"),
                    lateRs.getString("last_name"),
                    lateRs.getTime("login_time").toString(),
                    minutesLate
                ));
                lateCount++;
            }
            
            writer.println("");
            writer.println("ABSENT EMPLOYEES:");
            writer.println("Employee ID,Employee Name,Position");
            
            String absentSql = "SELECT e.employee_id, e.first_name, e.last_name, e.position " +
                              "FROM employees e " +
                              "WHERE e.employee_id NOT IN " +
                              "(SELECT DISTINCT a.employee_id FROM attendance a WHERE a.log_date = ?) " +
                              "ORDER BY e.employee_id";
            
            PreparedStatement absentStmt = connection.prepareStatement(absentSql);
            absentStmt.setDate(1, new java.sql.Date(reportDate.getTime()));
            ResultSet absentRs = absentStmt.executeQuery();
            
            int absentCount = 0;
            while (absentRs.next()) {
                writer.println(String.format("%d,\"%s %s\",\"%s\"",
                    absentRs.getInt("employee_id"),
                    absentRs.getString("first_name"),
                    absentRs.getString("last_name"),
                    absentRs.getString("position")
                ));
                absentCount++;
            }
            
            writer.println("");
            writer.println("SUMMARY:");
            writer.println("Total Late Employees:," + lateCount);
            writer.println("Total Absent Employees:," + absentCount);
            
            writer.close();
            System.out.println("Late/Absent report generated");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

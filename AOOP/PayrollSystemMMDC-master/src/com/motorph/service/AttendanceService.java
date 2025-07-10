package com.motorph.service;

import com.motorph.dao.AttendanceResult;
import com.motorph.model.AttendanceRecord;
import com.motorph.dao.AttendanceDAO;
import com.motorph.dao.AttendanceDAO;
import com.motorph.model.AttendanceRecord;
import com.motorph.dao.AttendanceResult;
import java.util.List;
import java.util.Date;
import com.motorph.dao.AttendanceStatus;
import com.motorph.dao.AttendanceStatus;

public class AttendanceService {
    private AttendanceDAO attendanceDAO;
    
    public AttendanceService() {
        this.attendanceDAO = new AttendanceDAO();
    }

    public AttendanceResult processTimeIn(int employeeId) {
        try {
            if (hasAlreadyLoggedInToday(employeeId)) {
                return new AttendanceResult(false, "You have already logged in today!", null);
            }
            
            AttendanceRecord attendance = new AttendanceRecord();
            attendance.setEmployeeId(employeeId);
            attendance.setLogDate(new Date());
            attendance.setLoginTime(new java.sql.Time(System.currentTimeMillis()));
            attendance.setLogoutTime(null);
            attendance.setSubmittedToSupervisor(false);
            attendance.setSubmittedToPayroll(false);
            attendance.setRemarks("Regular login");
            
            boolean success = attendanceDAO.addAttendance(attendance);
            
            if (success) {
                String timeFormatted = java.time.LocalTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
                return new AttendanceResult(true, 
                    "Time In recorded successfully at " + timeFormatted, 
                    attendance);
            } else {
                return new AttendanceResult(false, "Failed to record Time In", null);
            }
            
        } catch (Exception e) {
            return new AttendanceResult(false, "Error: " + e.getMessage(), null);
        }
    }
    
    public AttendanceResult processTimeOut(int employeeId) {
        try {
            AttendanceRecord todayRecord = getTodayAttendanceRecord(employeeId);
            
            if (todayRecord == null) {
                return new AttendanceResult(false, 
                    "No Time In record found for today! Please Time In first.", null);
            }
            
            if (todayRecord.getLogoutTime() != null) {
                return new AttendanceResult(false, "You have already logged out today!", null);
            }
            
            todayRecord.setLogoutTime(new java.sql.Time(System.currentTimeMillis()));

            double hoursWorked = todayRecord.getWorkingHours();

            boolean success = attendanceDAO.updateAttendance(todayRecord);
            
            if (success) {
                String timeFormatted = java.time.LocalTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
                String message = String.format(
                    "Time Out recorded successfully at %s\nHours Worked: %.2f", 
                    timeFormatted, hoursWorked);
                return new AttendanceResult(true, message, todayRecord);
            } else {
                return new AttendanceResult(false, "Failed to record Time Out", null);
            }
            
        } catch (Exception e) {
            return new AttendanceResult(false, "Error: " + e.getMessage(), null);
        }
    }

    public AttendanceStatus getAttendanceStatus(int employeeId) {
        try {
            AttendanceRecord todayRecord = getTodayAttendanceRecord(employeeId);
            
            if (todayRecord == null) {
                return new AttendanceStatus(true, false, "Can Time In");
            } else if (todayRecord.getLogoutTime() == null) {
                return new AttendanceStatus(false, true, "Can Time Out");
            } else {
                return new AttendanceStatus(false, false, "Already completed for today");
            }
            
        } catch (Exception e) {
            return new AttendanceStatus(true, false, "Error checking status");
        }
    }
    
    private boolean hasAlreadyLoggedInToday(int employeeId) {
        try {
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            List<AttendanceRecord> todayRecords = attendanceDAO.getAttendanceByEmployee(
                employeeId, today, today);
            return !todayRecords.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private AttendanceRecord getTodayAttendanceRecord(int employeeId) {
        try {
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            List<AttendanceRecord> todayRecords = attendanceDAO.getAttendanceByEmployee(
                employeeId, today, today);
            
            if (!todayRecords.isEmpty()) {
                return todayRecords.get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

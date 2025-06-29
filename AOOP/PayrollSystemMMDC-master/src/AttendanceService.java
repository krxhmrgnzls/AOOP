package payrollsystem;

import java.util.List;
import java.util.Date;

/**
 * Service class to handle attendance business logic
 * Separates business logic from GUI presentation
 */
public class AttendanceService {
    private AttendanceDAO attendanceDAO;
    
    public AttendanceService() {
        this.attendanceDAO = new AttendanceDAO();
    }
    
    /**
     * Handle time in process
     * @param employeeId Employee ID
     * @return AttendanceResult with success/failure info
     */
    public AttendanceResult processTimeIn(int employeeId) {
        try {
            // Business rule: Check if already logged in today
            if (hasAlreadyLoggedInToday(employeeId)) {
                return new AttendanceResult(false, "You have already logged in today!", null);
            }
            
            // Create attendance record
            AttendanceRecord attendance = new AttendanceRecord();
            attendance.setEmployeeId(employeeId);
            attendance.setLogDate(new Date());
            attendance.setLoginTime(new java.sql.Time(System.currentTimeMillis()));
            attendance.setLogoutTime(null);
            attendance.setSubmittedToSupervisor(false);
            attendance.setSubmittedToPayroll(false);
            attendance.setRemarks("Regular login");
            
            // Save to database
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
    
    /**
     * Handle time out process
     * @param employeeId Employee ID
     * @return AttendanceResult with success/failure info
     */
    public AttendanceResult processTimeOut(int employeeId) {
        try {
            // Business rule: Find today's attendance record
            AttendanceRecord todayRecord = getTodayAttendanceRecord(employeeId);
            
            if (todayRecord == null) {
                return new AttendanceResult(false, 
                    "No Time In record found for today! Please Time In first.", null);
            }
            
            if (todayRecord.getLogoutTime() != null) {
                return new AttendanceResult(false, "You have already logged out today!", null);
            }
            
            // Update with logout time
            todayRecord.setLogoutTime(new java.sql.Time(System.currentTimeMillis()));
            
            // Business rule: Calculate hours worked
            double hoursWorked = todayRecord.getWorkingHours();
            
            // Update in database
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
    
    /**
     * Get current attendance status for an employee
     * @param employeeId Employee ID
     * @return AttendanceStatus indicating what actions are available
     */
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
    
    // Private helper methods (business logic)
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

/**
 * Result object for attendance operations
 */
class AttendanceResult {
    private boolean success;
    private String message;
    private AttendanceRecord attendanceRecord;
    
    public AttendanceResult(boolean success, String message, AttendanceRecord record) {
        this.success = success;
        this.message = message;
        this.attendanceRecord = record;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public AttendanceRecord getAttendanceRecord() { return attendanceRecord; }
}

/**
 * Status object for attendance state
 */
class AttendanceStatus {
    private boolean canTimeIn;
    private boolean canTimeOut;
    private String statusMessage;
    
    public AttendanceStatus(boolean canTimeIn, boolean canTimeOut, String message) {
        this.canTimeIn = canTimeIn;
        this.canTimeOut = canTimeOut;
        this.statusMessage = message;
    }
    
    public boolean canTimeIn() { return canTimeIn; }
    public boolean canTimeOut() { return canTimeOut; }
    public String getStatusMessage() { return statusMessage; }
}
package payrollsystem;

import java.sql.Time;
import java.util.Date;

public class AttendanceRecord {
    private int attendanceId;
    private int employeeId;
    private String employeeName;
    private Date logDate;
    private Time loginTime;
    private Time logoutTime;
    private boolean submittedToSupervisor;
    private boolean submittedToPayroll;
    private String remarks;
    

    public AttendanceRecord() {}
    

    public AttendanceRecord(int employeeId, Date logDate, Time loginTime, Time logoutTime) {
        this.employeeId = employeeId;
        this.logDate = logDate;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.submittedToSupervisor = false;
        this.submittedToPayroll = false;
        this.remarks = "";
    }
    
    // Getters and Setters
    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    
    public Date getLogDate() { return logDate; }
    public void setLogDate(Date logDate) { this.logDate = logDate; }
    
    public Time getLoginTime() { return loginTime; }
    public void setLoginTime(Time loginTime) { this.loginTime = loginTime; }
    
    public Time getLogoutTime() { return logoutTime; }
    public void setLogoutTime(Time logoutTime) { this.logoutTime = logoutTime; }
    
    public boolean isSubmittedToSupervisor() { return submittedToSupervisor; }
    public void setSubmittedToSupervisor(boolean submittedToSupervisor) { 
        this.submittedToSupervisor = submittedToSupervisor; 
    }
    
    public boolean isSubmittedToPayroll() { return submittedToPayroll; }
    public void setSubmittedToPayroll(boolean submittedToPayroll) { 
        this.submittedToPayroll = submittedToPayroll; 
    }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public double getWorkingHours() {
        if (loginTime != null && logoutTime != null) {
            long diff = logoutTime.getTime() - loginTime.getTime();
            return diff / (1000.0 * 60 * 60); // Convert to hours
        }
        return 0.0;
    }
    
    public boolean isLate() {
        if (loginTime != null) {
            Time standardTime = Time.valueOf("08:00:00");
            return loginTime.after(standardTime);
        }
        return false;
    }
    
    public boolean isEarlyOut() {
        if (logoutTime != null) {
            Time standardTime = Time.valueOf("17:00:00");
            return logoutTime.before(standardTime);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("AttendanceRecord{ID=%d, Employee=%s, Date=%s, Hours=%.2f}",
                attendanceId, employeeName, logDate, getWorkingHours());
    }
}

class AttendanceSummary {
    private int totalDays;
    private int onTimeDays;
    private int lateDays;
    private double totalHours;
    private double averageHours;
    
    public AttendanceSummary() {}
    
    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { 
        this.totalDays = totalDays;
        calculateAverageHours();
    }
    
    public int getOnTimeDays() { return onTimeDays; }
    public void setOnTimeDays(int onTimeDays) { this.onTimeDays = onTimeDays; }
    
    public int getLateDays() { return lateDays; }
    public void setLateDays(int lateDays) { this.lateDays = lateDays; }
    
    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) { 
        this.totalHours = totalHours;
        calculateAverageHours();
    }
    
    public double getAverageHours() { return averageHours; }
    
    private void calculateAverageHours() {
        if (totalDays > 0) {
            this.averageHours = totalHours / totalDays;
        } else {
            this.averageHours = 0.0;
        }
    }
    
    public double getAttendanceRate() {
        if (totalDays > 0) {
            return (double) onTimeDays / totalDays * 100;
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return String.format("AttendanceSummary{Days=%d, OnTime=%d, Late=%d, Hours=%.2f, Rate=%.1f%%}",
                totalDays, onTimeDays, lateDays, totalHours, getAttendanceRate());
    }
}
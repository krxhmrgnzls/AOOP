package com.motorph.dao;

import java.util.Date;

public class LeaveRequest {
    private int leaveId;
    private int employeeId;
    private String employeeName;
    private Date dateFiled;
    private String leaveType;
    private Date fromDate;
    private Date toDate;
    private double numberOfDays;
    private String reason;
    private String status;
    
    public LeaveRequest() {
        this.dateFiled = new Date();
        this.status = "Pending";
    }
 
    public LeaveRequest(int employeeId, String leaveType, Date fromDate, Date toDate, 
                       double numberOfDays, String reason) {
        this();
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
    }
    
    // Getters and Setters
    public int getLeaveId() { return leaveId; }
    public void setLeaveId(int leaveId) { this.leaveId = leaveId; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public void setEmployeeID(int employeeID) { 
        this.employeeId = employeeID; 
    }
    
    public int getEmployeeID() { 
        return this.employeeId; 
    }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    
    public Date getDateFiled() { return dateFiled; }
    public void setDateFiled(Date dateFiled) { this.dateFiled = dateFiled; }
    
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    
    public Date getFromDate() { return fromDate; }
    public void setFromDate(Date fromDate) { this.fromDate = fromDate; }
    
    public Date getToDate() { return toDate; }
    public void setToDate(Date toDate) { this.toDate = toDate; }
    
    public double getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(double numberOfDays) { this.numberOfDays = numberOfDays; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("LeaveRequest{ID=%d, Employee=%s, Type=%s, Days=%.1f, Status=%s}",
                leaveId, employeeName, leaveType, numberOfDays, status);
    }
}
package payrollsystem;

import java.time.LocalDate;

public class LeaveRequest {
    private int leaveId;
    private int employeeId;
    private LocalDate dateFiled;
    private String leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private double numberOfDays;
    private String reason;
    private String status;
    
    // Getters and setters
    public int getLeaveId() { return leaveId; }
    public void setLeaveId(int leaveId) { this.leaveId = leaveId; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public LocalDate getDateFiled() { return dateFiled; }
    public void setDateFiled(LocalDate dateFiled) { this.dateFiled = dateFiled; }
    
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    
    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }
    
    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }
    
    public double getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(double numberOfDays) { this.numberOfDays = numberOfDays; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

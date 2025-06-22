package payrollsystem;

public class LeaveBalance {
    private int balanceId;
    private int employeeId;
    private double vacationLeave;
    private double sickLeave;
    
    // Getters and setters
    public int getBalanceId() { return balanceId; }
    public void setBalanceId(int balanceId) { this.balanceId = balanceId; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public double getVacationLeave() { return vacationLeave; }
    public void setVacationLeave(double vacationLeave) { this.vacationLeave = vacationLeave; }
    
    public double getSickLeave() { return sickLeave; }
    public void setSickLeave(double sickLeave) { this.sickLeave = sickLeave; }
}

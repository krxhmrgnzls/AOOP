package payrollsystem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PayrollRecord {
    private int payrollId;
    private int employeeId;
    private String payrollPeriod;
    private String position;
    private double grossIncome;
    private double benefits;
    private double overtime;
    private double undertime;
    private double sss;
    private double philhealth;
    private double pagibig;
    private double tax;
    private double netPay;
    private String status;
    
    // Constructor
    public PayrollRecord() {
    }
    
    // Getters and setters
    public int getPayrollId() { return payrollId; }
    public void setPayrollId(int payrollId) { this.payrollId = payrollId; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public String getPayrollPeriod() { return payrollPeriod; }
    public void setPayrollPeriod(String payrollPeriod) { this.payrollPeriod = payrollPeriod; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public double getGrossIncome() { return grossIncome; }
    public void setGrossIncome(double grossIncome) { this.grossIncome = grossIncome; }
    
    public double getBenefits() { return benefits; }
    public void setBenefits(double benefits) { this.benefits = benefits; }
    
    public double getOvertime() { return overtime; }
    public void setOvertime(double overtime) { this.overtime = overtime; }
    
    public double getUndertime() { return undertime; }
    public void setUndertime(double undertime) { this.undertime = undertime; }
    
    public double getSss() { return sss; }
    public void setSss(double sss) { this.sss = sss; }
    
    public double getPhilhealth() { return philhealth; }
    public void setPhilhealth(double philhealth) { this.philhealth = philhealth; }
    
    public double getPagibig() { return pagibig; }
    public void setPagibig(double pagibig) { this.pagibig = pagibig; }
    
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    
    public double getNetPay() { return netPay; }
    public void setNetPay(double netPay) { this.netPay = netPay; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    // Helper method to populate from ResultSet
    public void setPayrollDetails(ResultSet rs) throws SQLException {
        this.payrollId = rs.getInt("payroll_id");
        this.employeeId = rs.getInt("employee_id");
        this.payrollPeriod = rs.getString("payroll_period");
        this.position = rs.getString("position");
        this.grossIncome = rs.getDouble("gross_income");
        this.benefits = rs.getDouble("benefits");
        this.overtime = rs.getDouble("overtime");
        this.undertime = rs.getDouble("undertime");
        this.sss = rs.getDouble("sss");
        this.philhealth = rs.getDouble("philhealth");
        this.pagibig = rs.getDouble("pagibig");
        this.tax = rs.getDouble("tax");
        this.netPay = rs.getDouble("net_pay");
        this.status = rs.getString("status");
    }
}
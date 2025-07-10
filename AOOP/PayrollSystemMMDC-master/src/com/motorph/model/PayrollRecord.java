package com.motorph.model;

public class PayrollRecord {
    private int payrollId;
    private int employeeId;
    private String employeeName;
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
    
    public PayrollRecord() {}
    
    public PayrollRecord(int employeeId, String payrollPeriod, String position, 
                        double grossIncome, double benefits, double overtime, double undertime,
                        double sss, double philhealth, double pagibig, double tax, 
                        double netPay, String status) {
        this.employeeId = employeeId;
        this.payrollPeriod = payrollPeriod;
        this.position = position;
        this.grossIncome = grossIncome;
        this.benefits = benefits;
        this.overtime = overtime;
        this.undertime = undertime;
        this.sss = sss;
        this.philhealth = philhealth;
        this.pagibig = pagibig;
        this.tax = tax;
        this.netPay = netPay;
        this.status = status;
    }
    
    public int getPayrollId() { return payrollId; }
    public void setPayrollId(int payrollId) { this.payrollId = payrollId; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    
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
    
    public double getTotalDeductions() {
        return sss + philhealth + pagibig + tax + undertime;
    }
    
    @Override
    public String toString() {
        return String.format("PayrollRecord{ID=%d, Employee=%s, Period=%s, NetPay=%.2f, Status=%s}",
                payrollId, employeeName, payrollPeriod, netPay, status);
    }
}
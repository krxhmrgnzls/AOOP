package payrollsystem;

import java.util.Date;
import java.text.SimpleDateFormat;

public class AccountDetails {
    // Employee personal information
    private int databaseID;
    private int employeeID;  
    private String firstName;
    private String lastName;
    private Date birthday;
    private String address;
    private String phoneNumber;
    
    // Government IDs
    private String sssNumber;
    private String philHealthNumber;
    private String tinNumber;
    private String pagibigNumber;
    
    // Employment details
    private String status;
    private String position;
    private String supervisor;
    
    // Salary and allowances
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double semiBasicSalary;  // Semi-monthly rate
    private double hourlyRate;
    
    // Default constructor
    public AccountDetails() {
        // Initialize with default values
        this.employeeID = 0;
        this.firstName = "";
        this.lastName = "";
        this.birthday = null;
        this.address = "";
        this.phoneNumber = "";
        this.sssNumber = "";
        this.philHealthNumber = "";
        this.tinNumber = "";
        this.pagibigNumber = "";
        this.status = "";
        this.position = "";
        this.supervisor = "";
        this.basicSalary = 0.0;
        this.riceSubsidy = 0.0;
        this.phoneAllowance = 0.0;
        this.clothingAllowance = 0.0;
        this.semiBasicSalary = 0.0;
        this.hourlyRate = 0.0;
    }
    
    // Constructor with employee ID
    public AccountDetails(int employeeID) {
        this();
        this.employeeID = employeeID;
    }
    
    // ===== BASIC GETTERS AND SETTERS =====
    public int getDatabaseID() { 
        return databaseID; 
    }
    
    public void setDatabaseID(int databaseID) { 
        this.databaseID = databaseID; 
    }
    
    public int getEmployeeID() { 
        return employeeID; 
    }
    
    public void setEmployeeID(int employeeID) { 
        this.employeeID = employeeID; 
    }
    
    public String getFirstName() { 
        return firstName; 
    }
    
    public void setFirstName(String firstName) { 
        this.firstName = firstName != null ? firstName : ""; 
    }
    
    public String getLastName() { 
        return lastName; 
    }
    
    public void setLastName(String lastName) { 
        this.lastName = lastName != null ? lastName : ""; 
    }
    
    public Date getBirthday() { 
        return birthday; 
    }
    
    public void setBirthday(Date birthday) { 
        this.birthday = birthday; 
    }
    
    // Get birthday as formatted string
    public String getBirthdayString() {
        if (birthday != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            return sdf.format(birthday);
        }
        return "";
    }
    
    public String getAddress() { 
        return address; 
    }
    
    public void setAddress(String address) { 
        this.address = address != null ? address : ""; 
    }
    
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber != null ? phoneNumber : ""; 
    }
    
    // ===== GOVERNMENT ID GETTERS AND SETTERS =====
    public String getSSSNumber() { 
        return sssNumber; 
    }
    
    public void setSSSNumber(String sssNumber) { 
        this.sssNumber = sssNumber != null ? sssNumber : ""; 
    }
    
    public String getPhilHealthNumber() { 
        return philHealthNumber; 
    }
    
    public void setPhilHealthNumber(String philHealthNumber) { 
        this.philHealthNumber = philHealthNumber != null ? philHealthNumber : ""; 
    }
    
    public String getTinNumber() { 
        return tinNumber; 
    }
    
    public void setTinNumber(String tinNumber) { 
        this.tinNumber = tinNumber != null ? tinNumber : ""; 
    }
    
    public String getPagibigNumber() { 
        return pagibigNumber; 
    }
    
    public void setPagibigNumber(String pagibigNumber) { 
        this.pagibigNumber = pagibigNumber != null ? pagibigNumber : ""; 
    }
    
    // ===== EMPLOYMENT DETAILS GETTERS AND SETTERS =====
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status != null ? status : ""; 
    }
    
    public String getPosition() { 
        return position; 
    }
    
    public void setPosition(String position) { 
        this.position = position != null ? position : ""; 
    }
    
    public String getSupervisor() { 
        return supervisor; 
    }
    
    public void setSupervisor(String supervisor) { 
        this.supervisor = supervisor != null ? supervisor : ""; 
    }
    
    // ===== SALARY AND ALLOWANCES GETTERS AND SETTERS =====
    public double getBasicSalary() { 
        return basicSalary; 
    }
    
    public void setBasicSalary(double basicSalary) { 
        this.basicSalary = basicSalary >= 0 ? basicSalary : 0.0; 
    }
    
    public double getRiceSubsidy() { 
        return riceSubsidy; 
    }
    
    public void setRiceSubsidy(double riceSubsidy) { 
        this.riceSubsidy = riceSubsidy >= 0 ? riceSubsidy : 0.0; 
    }
    
    public double getPhoneAllowance() { 
        return phoneAllowance; 
    }
    
    public void setPhoneAllowance(double phoneAllowance) { 
        this.phoneAllowance = phoneAllowance >= 0 ? phoneAllowance : 0.0; 
    }
    
    public double getClothingAllowance() { 
        return clothingAllowance; 
    }
    
    public void setClothingAllowance(double clothingAllowance) { 
        this.clothingAllowance = clothingAllowance >= 0 ? clothingAllowance : 0.0; 
    }
    
    public double getSemiBasicSalary() { 
        return semiBasicSalary; 
    }
    
    public void setSemiBasicSalary(double semiBasicSalary) { 
        this.semiBasicSalary = semiBasicSalary >= 0 ? semiBasicSalary : 0.0; 
    }
    
    public double getHourlyRate() { 
        return hourlyRate; 
    }
    
    public void setHourlyRate(double hourlyRate) { 
        this.hourlyRate = hourlyRate >= 0 ? hourlyRate : 0.0; 
    }
    
    // ===== CONVENIENCE METHODS =====
    
    // Get complete name
    public String getEmployeeCompleteName() {
        return (firstName + " " + lastName).trim();
    }
    
    // Get formatted employee ID
    public String getFormattedEmployeeID() {
        return String.format("%05d", employeeID);
    }
    
    // Calculate total allowances
    public double getTotalAllowances() {
        return riceSubsidy + phoneAllowance + clothingAllowance;
    }
    
    // Calculate gross income (basic salary + allowances)
    public double getGrossIncome() {
        return basicSalary + getTotalAllowances();
    }
    
    // Validate employee data
    public boolean isValidEmployee() {
        return employeeID > 0 && 
               !firstName.trim().isEmpty() && 
               !lastName.trim().isEmpty() && 
               basicSalary > 0;
    }
    
    // ===== DISPLAY METHODS =====
    
    // Get formatted salary
    public String getFormattedBasicSalary() {
        return String.format("₱%.2f", basicSalary);
    }
    
    public String getFormattedSemiBasicSalary() {
        return String.format("₱%.2f", semiBasicSalary);
    }
    
    public String getFormattedHourlyRate() {
        return String.format("₱%.2f", hourlyRate);
    }
    
    public String getFormattedGrossIncome() {
        return String.format("₱%.2f", getGrossIncome());
    }
    
    // ===== toString METHOD =====
    @Override
    public String toString() {
        return String.format("Employee[ID=%d, Name=%s %s, Position=%s, Salary=%.2f]", 
                           employeeID, firstName, lastName, position, basicSalary);
    }
    
    // ===== EQUALS AND HASHCODE =====
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        AccountDetails that = (AccountDetails) obj;
        return employeeID == that.employeeID;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(employeeID);
    }
    
    // ===== CLONE METHOD =====
    public AccountDetails clone() {
        AccountDetails cloned = new AccountDetails();
        cloned.setDatabaseID(this.databaseID);
        cloned.setEmployeeID(this.employeeID);
        cloned.setFirstName(this.firstName);
        cloned.setLastName(this.lastName);
        cloned.setBirthday(this.birthday != null ? new Date(this.birthday.getTime()) : null);
        cloned.setAddress(this.address);
        cloned.setPhoneNumber(this.phoneNumber);
        cloned.setSSSNumber(this.sssNumber);
        cloned.setPhilHealthNumber(this.philHealthNumber);
        cloned.setTinNumber(this.tinNumber);
        cloned.setPagibigNumber(this.pagibigNumber);
        cloned.setStatus(this.status);
        cloned.setPosition(this.position);
        cloned.setSupervisor(this.supervisor);
        cloned.setBasicSalary(this.basicSalary);
        cloned.setRiceSubsidy(this.riceSubsidy);
        cloned.setPhoneAllowance(this.phoneAllowance);
        cloned.setClothingAllowance(this.clothingAllowance);
        cloned.setSemiBasicSalary(this.semiBasicSalary);
        cloned.setHourlyRate(this.hourlyRate);
        return cloned;
    }
}
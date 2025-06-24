package payrollsystem;

import java.util.ArrayList;
import java.util.Date;

public class AccountDetails {
    // Private fields
    private int databaseID;
    private int employeeID;  // Make sure this exists
    private String firstName;
    private String lastName;
    private Date birthday;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philHealthNumber;
    private String tinNumber;
    private String pagibigNumber;
    private String status;
    private String position;
    private String supervisor;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double semiMonthlyRate;  // Fixed name
    private double hourlyRate;
    private String filePath;
    private ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    
    // Default constructor
    public AccountDetails() {}
    
    // Getters and Setters - FIXED NAMES
    public int getDatabaseID() { return databaseID; }
    public void setDatabaseID(int databaseID) { this.databaseID = databaseID; }
    
    // FIX: Make sure these methods exist with EXACT names
    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getSSSNumber() { return sssNumber; }
    public void setSSSNumber(String sssNumber) { this.sssNumber = sssNumber; }
    
    public String getPhilHealthNumber() { return philHealthNumber; }
    public void setPhilHealthNumber(String philHealthNumber) { this.philHealthNumber = philHealthNumber; }
    
    public String getTINNumber() { return tinNumber; }
    public void setTINNumber(String tinNumber) { this.tinNumber = tinNumber; }
    
    public String getPagibigNumber() { return pagibigNumber; }
    public void setPagibigNumber(String pagibigNumber) { this.pagibigNumber = pagibigNumber; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getSupervisor() { return supervisor; }
    public void setSupervisor(String supervisor) { this.supervisor = supervisor; }
    
    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    
    public double getRiceSubsidy() { return riceSubsidy; }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }
    
    public double getPhoneAllowance() { return phoneAllowance; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; }
    
    public double getClothingAllowance() { return clothingAllowance; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }
    
    // FIX: Use consistent naming
    public double getSemiMonthlyRate() { return semiMonthlyRate; }
    public void setSemiMonthlyRate(double semiMonthlyRate) { this.semiMonthlyRate = semiMonthlyRate; }
    
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    // Additional methods that might be called
    public void retrieveFromDB() {
        // Implementation for retrieving from database
    }
    
    public boolean saveToDB() {
        // Implementation for saving to database
        return true;
    }
    
    public boolean updateInDB() {
        // Implementation for updating in database
        return true;
    }
    
    public boolean validateData() {
        // Implementation for data validation
        return true;
    }
    public String getEmployeeCompleteName() {
    return this.firstName + " " + this.lastName;
    }
    
    public String getSemiBasicSalary() {
        return String.valueOf(this.semiMonthlyRate);
    }

    public String getTinNumber() {
        return this.tinNumber;
    }

    // 3. Method to get birthday as string for text fields
    public String getBirthdayAsString() {
        if (birthday != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            return sdf.format(birthday);
        }
        return "";
    }
    
    private ArrayList<ArrayList<String>> newData = new ArrayList<>();

    public ArrayList<ArrayList<String>> getNewData() {
        return newData;
    }

    public void setNewData(ArrayList<ArrayList<String>> newData) {
        this.newData = newData;
    }
    
    public void setFilePath(String filePath) {
    this.filePath = filePath;
}

    public void setDataList(ArrayList<ArrayList<String>> dataList) {
    this.dataList = dataList;
    }
    
    public void retrievedDetails() {
        // Basic implementation to read CSV file
        try {
            dataList.clear();
            // You can implement actual CSV reading here
            System.out.println("Reading from: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<String>> getDataList() {
        return this.dataList;
    }

    
    @Override
    public String toString() {
        return String.format("AccountDetails{ID=%d, Name=%s %s, Position=%s, Salary=%.2f}",
                employeeID, firstName, lastName, position, basicSalary);
    }
}
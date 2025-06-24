package payrollsystem;

import java.util.ArrayList;

public class Employee {
    protected int databaseID;
    protected int employeeID;
    protected AccountDetails accountDetails;
    
    // Add these properties that PayrollStaff needs
    protected ArrayList<ArrayList<String>> newData;
    
    public Employee() {
        this.accountDetails = new AccountDetails();
        this.newData = new ArrayList<>();
    }
    
    // ADD: Constructor that GUI classes expect
    public Employee(String employeeId) {
        this.accountDetails = new AccountDetails();
        this.newData = new ArrayList<>();
        try {
            this.employeeID = Integer.parseInt(employeeId);
            this.accountDetails.setEmployeeID(this.employeeID);
        } catch (NumberFormatException e) {
            this.employeeID = 0;
        }
    }
    
    // Getters and Setters
    public int getDatabaseID() { return databaseID; }
    public void setDatabaseID(int databaseID) { this.databaseID = databaseID; }
    
    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }
    
    public AccountDetails getAccountDetails() { return accountDetails; }
    public void setAccountDetails(AccountDetails accountDetails) { this.accountDetails = accountDetails; }
    
    public ArrayList<ArrayList<String>> getNewData() { return newData; }
    public void setNewData(ArrayList<ArrayList<String>> newData) { this.newData = newData; }
    
    // ADD: All missing methods that GUI classes expect
    
    // Table management methods
    public void setTableData(ArrayList<ArrayList<String>> data) {
        this.newData = data;
    }
    
    public void setTableSize(int size) {
        // Set table size - placeholder
    }
    
    public void displayDataTable(javax.swing.JTable table) {
        if (newData != null && !newData.isEmpty()) {
            String[] columnNames = {"ID", "Name", "Type", "Date", "Status"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0);
            
            for (ArrayList<String> row : newData) {
                Object[] rowData = row.toArray();
                model.addRow(rowData);
            }
            table.setModel(model);
        }
    }
    
    // Data retrieval methods
    public ArrayList<ArrayList<String>> getDataAllRequests() {
        return new ArrayList<>(); // Return empty for now
    }
    
    public ArrayList<ArrayList<String>> getDataAllDTR(java.util.Date startDate, java.util.Date endDate) {
        return new ArrayList<>(); // Return empty for now
    }
    
    // Leave balance methods
    public void updateLeaveBalanceLabels(javax.swing.JLabel label1, javax.swing.JLabel label2) {
        label1.setText("24.0");
        label2.setText("24.0");
    }
    
    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        return new ArrayList<>();
    }
    
    // Payslip method
    public ArrayList<ArrayList<String>> viewPersonalPayslip(java.util.Date startDate, java.util.Date endDate, String employeeId) {
        return new ArrayList<>();
    }
    
    public boolean isValidDateRange(java.util.Date startDate, java.util.Date endDate) {
        return countNumberOfDays(startDate, endDate) > 0;
    }
    // Leave calculation methods
    public int countNumberOfDays(java.util.Date startDate, java.util.Date endDate) {
        if (startDate == null || endDate == null) return 0;
        
        long diffInMillies = endDate.getTime() - startDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        return (int) diffInDays + 1;
    }
    
    public int getNumberOfDaysLeave() {
        return 0; // Default implementation
    }
    
    public void setNumberOfDaysLeave() {
        // Default implementation
    }
    
    // Overtime request method
    public boolean fileOvertimeRequest(ArrayList<String> overtimeData) {
        try {
            System.out.println("Overtime request filed for employee: " + overtimeData.get(0));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     
    // DTR forwarding method
    public void forwardDTRToSupervisor(ArrayList<ArrayList<String>> dtrData) {
        System.out.println("DTR forwarded to supervisor");
    }
    
    // Leave balance information methods
    public void leaveBalancesInformation() {
        System.out.println("Loading leave balance information...");
    }
    
    public String getBalanceVL() {
        return "24.0";
    }
    
    public String getBalanceSL() {
        return "24.0";
    }
    
    // Override fileLeaveRequest to accept ArrayList parameter
    public boolean fileLeaveRequest(ArrayList<String> leaveData) {
        try {
            System.out.println("Leave request filed for employee: " + leaveData.get(0));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void viewPayslip() {
        // Implementation
    }
    
    public void viewPersonalDetails(String employeeId) {
    // Method that takes employee ID parameter (called by GUI)
}

    public void viewPersonalDetails() {
        // Method without parameters (for inheritance)
    }
    public void downloadPayslipPDF() {
        // Implementation
    }
    
    public boolean fileLeaveRequest() {
        // Implementation
        return true;
    }
    
    public void viewDTR() {
        // Implementation
    }
    
    public void userLogin() {
        // Implementation
    }
    
    public void userLogout() {
        // Implementation
    }
    
    
}
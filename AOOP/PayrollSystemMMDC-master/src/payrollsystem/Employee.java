package payrollsystem;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Employee {
    // Core employee data
    protected int databaseID;
    protected int employeeID;
    protected AccountDetails accountDetails;
    protected ArrayList<ArrayList<String>> newData;
    
    // For date calculations
    private int calculatedDays = 0;
    
    // DAOs for database operations
    protected LeaveDAO leaveDAO;
    protected OvertimeDAO overtimeDAO;
    
    // ===== CONSTRUCTORS =====
    public Employee() {
        this.accountDetails = new AccountDetails();
        this.newData = new ArrayList<>();
        initializeDAOs();
    }
    
    public Employee(String employeeId) {
        this.accountDetails = new AccountDetails();
        this.newData = new ArrayList<>();
        try {
            this.employeeID = Integer.parseInt(employeeId);
            this.accountDetails.setEmployeeID(this.employeeID);
        } catch (NumberFormatException e) {
            this.employeeID = 0;
        }
        initializeDAOs();
    }
    
    private void initializeDAOs() {
        this.leaveDAO = new LeaveDAO();
        this.overtimeDAO = new OvertimeDAO();
    }
    
    // ===== BASIC GETTERS AND SETTERS =====
    public int getDatabaseID() { return databaseID; }
    public void setDatabaseID(int databaseID) { this.databaseID = databaseID; }
    
    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }
    
    public AccountDetails getAccountDetails() { return accountDetails; }
    public void setAccountDetails(AccountDetails accountDetails) { this.accountDetails = accountDetails; }
    
    public ArrayList<ArrayList<String>> getNewData() { return newData; }
    public void setNewData(ArrayList<ArrayList<String>> newData) { this.newData = newData; }
    
    // ===== TABLE DATA MANAGEMENT =====
    public void setTableData(ArrayList<ArrayList<String>> data) {
        this.newData = data;
    }
    
    public void setTableSize(int size) {
        // Implementation for setting table size if needed
    }
    
    public void displayDataTable(javax.swing.JTable table) {
    // ALWAYS use the correct column headers for requests
    String[] columnNames = {"DATE FILED", "TYPE OF REQUEST", "PERIOD FROM", "PERIOD TO", "NUMBER OF DAYS", "REASON", "STATUS"};
    
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Make table read-only
        }
    };
    
    if (newData != null && !newData.isEmpty()) {
        System.out.println("Displaying " + newData.size() + " requests in table");
        
        for (ArrayList<String> row : newData) {
            // Ensure each row has exactly 7 columns in the correct order
            Object[] tableRow = new Object[7];
            
            if (row.size() >= 7) {
                // Data is already in correct format
                tableRow[0] = row.get(0); // DATE FILED
                tableRow[1] = row.get(1); // TYPE OF REQUEST
                tableRow[2] = row.get(2); // PERIOD FROM
                tableRow[3] = row.get(3); // PERIOD TO
                tableRow[4] = row.get(4); // NUMBER OF DAYS
                tableRow[5] = row.get(5); // REASON
                tableRow[6] = row.get(6); // STATUS
            } else {
                // Fill with available data and empty strings for missing columns
                for (int i = 0; i < 7; i++) {
                    tableRow[i] = (i < row.size()) ? row.get(i) : "";
                }
            }
            
            model.addRow(tableRow);
            System.out.println("Added row: " + java.util.Arrays.toString(tableRow));
        }
    } else {
        System.out.println("No data to display in requests table");
    }
    
    table.setModel(model);
    
    // Optional: Set column widths for better display
    if (table.getColumnModel().getColumnCount() == 7) {
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // DATE FILED
        table.getColumnModel().getColumn(1).setPreferredWidth(120); // TYPE OF REQUEST
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // PERIOD FROM
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // PERIOD TO
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // NUMBER OF DAYS
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // REASON
        table.getColumnModel().getColumn(6).setPreferredWidth(80);  // STATUS
    }
    
    System.out.println("Table model updated with correct column headers");
}
   
    
    public int calculateDaysFromDates(java.util.Date fromDate, java.util.Date toDate) {
    if (fromDate == null || toDate == null) {
        this.calculatedDays = 0;
        return 0;
    }
    
    if (!isValidDateRange(fromDate, toDate)) {
        this.calculatedDays = 0;
        return 0;
    }
    
    // Calculate difference in milliseconds
    long diffInMillies = toDate.getTime() - fromDate.getTime();
    // Convert to days (add 1 to include both start and end date)
    long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
    int days = (int) diffInDays + 1;
    
    // Store the result
    this.calculatedDays = days;
    
    return days;
}
   public int countNumberOfDays(java.util.Date startDate, java.util.Date endDate) {
    if (startDate == null || endDate == null) {
        this.calculatedDays = 0;
        return 0;
    }
    
    // Calculate difference in milliseconds
    long diffInMillies = endDate.getTime() - startDate.getTime();
    // Convert to days (add 1 to include both start and end date)
    long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
    int days = (int) diffInDays + 1;
    
    // Store the result in the employee object
    this.calculatedDays = days;
    
    return days;
}
    public boolean isValidDateRange(java.util.Date startDate, java.util.Date endDate) {
    if (startDate == null || endDate == null) {
        return false;
    }
    return !endDate.before(startDate);
}

    public int getNumberOfDaysLeave() {
        return this.calculatedDays; // Return the stored calculated days
    }

    public void setNumberOfDaysLeave() {
}
    
    public void setCalculatedDays(int days) {
        this.calculatedDays = days;
    }
    
    public int calculateDaysFromGUI(java.util.Date fromDate, java.util.Date toDate) {
    if (isValidDateRange(fromDate, toDate)) {
        return countNumberOfDays(fromDate, toDate);
    } else {
        this.calculatedDays = 0;
        return 0;
    }
}
    
    // ===== LEAVE REQUEST METHODS =====
    public boolean fileLeaveRequest(ArrayList<String> leaveData) {
        try {
            if (leaveData == null || leaveData.size() < 7) {
                System.err.println("Invalid leave data: insufficient parameters");
                return false;
            }
            
            int employeeId = Integer.parseInt(leaveData.get(0));
            String leaveType = leaveData.get(2);
            String fromDateStr = leaveData.get(3);
            String toDateStr = leaveData.get(4);
            double numberOfDays = Double.parseDouble(leaveData.get(5));
            String reason = leaveData.get(6);
            
            // Parse dates (assuming MM/dd/yyyy format)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            java.util.Date fromDate = sdf.parse(fromDateStr);
            java.util.Date toDate = sdf.parse(toDateStr);
            
            // Convert to LocalDate for DAO
            LocalDate fromLocalDate = LocalDate.ofEpochDay(fromDate.getTime() / (24 * 60 * 60 * 1000));
            LocalDate toLocalDate = LocalDate.ofEpochDay(toDate.getTime() / (24 * 60 * 60 * 1000));
            
            boolean success = leaveDAO.submitLeaveRequest(employeeId, leaveType, fromLocalDate, toLocalDate, numberOfDays, reason);
            
            if (success) {
                System.out.println("Leave request submitted successfully for employee " + employeeId);
            } else {
                System.err.println("Failed to submit leave request for employee " + employeeId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("Error filing leave request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ===== OVERTIME REQUEST METHODS =====
    public boolean fileOvertimeRequest(ArrayList<String> overtimeData) {
        try {
            if (overtimeData == null || overtimeData.size() < 6) {
                System.err.println("Invalid overtime data: insufficient parameters");
                return false;
            }
            
            int employeeId = Integer.parseInt(overtimeData.get(0));
            String fromDateTimeStr = overtimeData.get(2);
            String toDateTimeStr = overtimeData.get(3);
            double numberOfDays = Double.parseDouble(overtimeData.get(4));
            String reason = overtimeData.get(5);
            
            // For now, using current time as placeholder (you can modify this based on your date format)
            LocalDateTime fromTime = LocalDateTime.now();
            LocalDateTime toTime = fromTime.plusHours((long)(numberOfDays * 8)); // Assuming 8 hours per day
            
            boolean success = overtimeDAO.submitOvertimeRequest(employeeId, fromTime, toTime, numberOfDays, reason);
            
            if (success) {
                System.out.println("Overtime request submitted successfully for employee " + employeeId);
            } else {
                System.err.println("Failed to submit overtime request for employee " + employeeId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("Error filing overtime request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<ArrayList<String>> getDataAllRequests() {
        try {
            // Get employee ID properly
            int empId = this.employeeID; // Use the instance variable
            if (empId == 0 && this.accountDetails != null) {
                empId = this.accountDetails.getEmployeeID();
            }

            System.out.println("DEBUG: Getting requests for employee ID: " + empId);

            if (empId == 0) {
                System.err.println("ERROR: Invalid employee ID (0) - cannot load requests");
                return new ArrayList<>();
            }

            ArrayList<ArrayList<String>> allRequests = new ArrayList<>();

            // Initialize DAOs if needed
            if (leaveDAO == null) {
                System.out.println("DEBUG: Initializing LeaveDAO");
                leaveDAO = new LeaveDAO();
            }

            if (overtimeDAO == null) {
                System.out.println("DEBUG: Initializing OvertimeDAO");
                overtimeDAO = new OvertimeDAO();
            }

            // Get leave requests
            System.out.println("DEBUG: Fetching leave requests...");
            List<ArrayList<String>> leaveRequests = leaveDAO.getLeaveRequestsByEmployee(empId);
            System.out.println("DEBUG: Found " + leaveRequests.size() + " leave requests");

            // Get overtime requests  
            System.out.println("DEBUG: Fetching overtime requests...");
            List<ArrayList<String>> overtimeRequests = overtimeDAO.getOvertimeRequestsByEmployee(empId);
            System.out.println("DEBUG: Found " + overtimeRequests.size() + " overtime requests");

            // Add all requests
            allRequests.addAll(leaveRequests);
            allRequests.addAll(overtimeRequests);

            System.out.println("DEBUG: Total requests loaded: " + allRequests.size());

            // Store in newData for display
            this.newData = allRequests;

            return allRequests;

        } catch (Exception e) {
            System.err.println("ERROR in getDataAllRequests: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
    }
}
    
    public boolean refreshAllRequests() {
        try {
            ArrayList<ArrayList<String>> freshData = getDataAllRequests();
            this.newData = freshData;
            return true;
        } catch (Exception e) {
            System.err.println("Error refreshing requests: " + e.getMessage());
            return false;
        }
    }
    
    // You can REMOVE this method - it already exists but with a different name
    public boolean refreshRequestsTable() {
        try {
            ArrayList<ArrayList<String>> requests = loadAllRequestsForEmployee();
            return !requests.isEmpty();
        } catch (Exception e) {
            System.err.println("Error refreshing requests table: " + e.getMessage());
            return false;
        }
    }

    
    public ArrayList<ArrayList<String>> getDataAllDTR(java.util.Date startDate, java.util.Date endDate) {
        // Placeholder for DTR data retrieval
        System.out.println("DTR data requested from " + startDate + " to " + endDate);
        return new ArrayList<>();
    }
    
    public void updateLeaveBalanceLabels(javax.swing.JLabel vlLabel, javax.swing.JLabel slLabel) {
        // In a real implementation, you'd get this from database
        vlLabel.setText("24.0");
        slLabel.setText("24.0");
        System.out.println("Leave balance labels updated");
    }
    
    public String getBalanceVL() {
        return "24.0"; // Placeholder - should get from database
    }
    
    public String getBalanceSL() {
        return "24.0"; // Placeholder - should get from database
    }
    
    public void leaveBalancesInformation() {
        System.out.println("Loading leave balance information...");
    }
    
    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        // Placeholder for approved leave ledger
        return new ArrayList<>();
    }
    
    public ArrayList<ArrayList<String>> loadAllRequestsForEmployee() {
    try {
        int empId = this.accountDetails.getEmployeeID();
        if (empId == 0) {
            System.err.println("Invalid employee ID for loading requests");
            return new ArrayList<>();
        }
        
        ArrayList<ArrayList<String>> allRequests = new ArrayList<>();
        
        // Get leave requests if LeaveDAO is available
        if (leaveDAO != null) {
            try {
                List<ArrayList<String>> leaveRequests = leaveDAO.getLeaveRequestsByEmployee(empId);
                allRequests.addAll(leaveRequests);
                System.out.println("Loaded " + leaveRequests.size() + " leave requests");
            } catch (Exception e) {
                System.err.println("Error loading leave requests: " + e.getMessage());
            }
        }
        
        // Get overtime requests if OvertimeDAO is available
        if (overtimeDAO != null) {
            try {
                List<ArrayList<String>> overtimeRequests = overtimeDAO.getOvertimeRequestsByEmployee(empId);
                allRequests.addAll(overtimeRequests);
                System.out.println("Loaded " + overtimeRequests.size() + " overtime requests");
            } catch (Exception e) {
                System.err.println("Error loading overtime requests: " + e.getMessage());
            }
        }
        
        System.out.println("Total requests loaded: " + allRequests.size());
        
        // Store the data in the employee object for GUI access
        this.setTableData(allRequests);
        
        return allRequests;
        
    } catch (Exception e) {
        System.err.println("Error in loadAllRequestsForEmployee: " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    }
}

    
    public ArrayList<ArrayList<String>> viewPersonalPayslip(java.util.Date startDate, java.util.Date endDate, String employeeId) {
        System.out.println("Payslip requested for employee " + employeeId + " from " + startDate + " to " + endDate);
        return new ArrayList<>();
    }
    
    public void viewPayslip() {
        System.out.println("Viewing payslip for employee " + this.employeeID);
    }
    
    public void downloadPayslipPDF() {
        System.out.println("Downloading payslip PDF for employee " + this.employeeID);
    }
    
    // ===== DTR METHODS =====
    public void forwardDTRToSupervisor(ArrayList<ArrayList<String>> dtrData) {
        System.out.println("DTR forwarded to supervisor for employee " + this.employeeID);
    }
    
    public void viewDTR() {
        System.out.println("Viewing DTR for employee " + this.employeeID);
    }
    
    // ===== PERSONAL DETAILS METHODS =====
    public void viewPersonalDetails(String employeeId) {
        System.out.println("Viewing personal details for employee " + employeeId);
    }
    
    public void viewPersonalDetails() {
        System.out.println("Viewing personal details for employee " + this.employeeID);
    }
    
    // ===== LOGIN/LOGOUT METHODS =====
    public void userLogin() {
        System.out.println("User login for employee " + this.employeeID);
    }
    
    public void userLogout() {
        System.out.println("User logout for employee " + this.employeeID);
    }
}
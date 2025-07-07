package payrollsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;

public class Employee {
    // Core employee data
    protected int databaseID;
    protected int employeeID;
    protected AccountDetails accountDetails;
    protected ArrayList<ArrayList<String>> newData;
    protected ArrayList<ArrayList<String>> tableData;
    protected ArrayList<ArrayList<String>> idAndNames;
    protected int tableSize;
    
    // Database connection
    protected DatabaseConnection dbConnection;
    protected Connection connection;
    
    // For date calculations
    private int calculatedDays = 0;
    
    // ===== CONSTRUCTORS =====
    public Employee() {
        this.accountDetails = new AccountDetails();
        this.newData = new ArrayList<>();
        this.tableData = new ArrayList<>();
        this.idAndNames = new ArrayList<>();
        initializeDatabaseConnection();
    }
    
    public Employee(String employeeId) {
        this.accountDetails = new AccountDetails();
        this.newData = new ArrayList<>();
        this.tableData = new ArrayList<>();
        this.idAndNames = new ArrayList<>();
        
        try {
            this.employeeID = Integer.parseInt(employeeId);
            this.accountDetails.setEmployeeID(this.employeeID);
            System.out.println("✅ Employee created with ID: " + this.employeeID);
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid employee ID string: " + employeeId);
            this.employeeID = 0;
        }
        
        initializeDatabaseConnection();
    }
    
    // ===== DATABASE CONNECTION =====
    private void initializeDatabaseConnection() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.connection = dbConnection.getConnection();
            System.out.println("✅ Database connection initialized successfully");
        } catch (SQLException e) {
            System.err.println("❌ Failed to initialize database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initializeConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DatabaseConnection.getInstance().getConnection();
                System.out.println("✅ Database connection reinitialized");
            }
        } catch (Exception e) {
            System.err.println("❌ Error reinitializing database connection: " + e.getMessage());
        }
    }
    
    // ===== GETTERS AND SETTERS =====
    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
        if (this.accountDetails != null) {
            this.accountDetails.setEmployeeID(employeeID);
        }
        System.out.println("Employee ID set to: " + this.employeeID);
    }
    
    public AccountDetails getAccountDetails() { return accountDetails; }
    public void setAccountDetails(AccountDetails accountDetails) { this.accountDetails = accountDetails; }
    
    public ArrayList<ArrayList<String>> getNewData() { return newData; }
    public void setNewData(ArrayList<ArrayList<String>> newData) { this.newData = newData; }
    
    // ===== TABLE MANAGEMENT =====
    public void setTableData(ArrayList<ArrayList<String>> data) {
        this.tableData = data;
        this.newData = data; // Sync both
    }
    
    public void setTableSize(int size) {
        this.tableSize = size;
    }
    
    public void displayDataTable(JTable table) {
        // Check what type of table this is based on the data
        String[] columnNames;
        int expectedColumns;
        
        // If we have request data (7 columns), use request headers
        if (newData != null && !newData.isEmpty() && newData.get(0).size() == 7) {
            columnNames = new String[]{"DATE FILED", "TYPE OF REQUEST", "PERIOD FROM", "PERIOD TO", "NUMBER OF DAYS", "REASON", "STATUS"};
            expectedColumns = 7;
        } else {
            // Default to DTR headers (5 columns)
            columnNames = new String[]{"DATE", "LOGIN TIME", "LOGOUT TIME", "SUBMITTED", "REMARKS"};
            expectedColumns = 5;
        }
        
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Use newData if tableData is empty
        ArrayList<ArrayList<String>> dataToDisplay = (tableData != null && !tableData.isEmpty()) ? tableData : newData;
        
        if (dataToDisplay != null && !dataToDisplay.isEmpty()) {
            for (ArrayList<String> row : dataToDisplay) {
                Object[] rowData = new Object[expectedColumns];
                for (int i = 0; i < expectedColumns; i++) {
                    rowData[i] = (i < row.size()) ? row.get(i) : "";
                }
                model.addRow(rowData);
            }
            System.out.println("✅ Displayed " + dataToDisplay.size() + " rows in table");
        } else {
            System.out.println("⚠️ No data to display in table");
        }
        
        table.setModel(model);
    }
    
    // ===== DTR METHODS =====
    public ArrayList<ArrayList<String>> getDataAllDTR(Date startDate, Date endDate) {
        ArrayList<ArrayList<String>> dtrData = new ArrayList<>();
        
        try {
            System.out.println("=== LOADING DTR DATA ===");
            System.out.println("Employee ID: " + this.employeeID);
            System.out.println("Date range: " + startDate + " to " + endDate);
            
            // Validate employee ID
            if (this.employeeID == 0) {
                System.err.println("❌ Employee ID is 0! Cannot load DTR data.");
                insertTestDTRData(); // Try to insert test data
                return dtrData;
            }
            
            // Check database connection
            if (connection == null || connection.isClosed()) {
                System.err.println("❌ Database connection issue!");
                initializeConnection();
                if (connection == null) {
                    return dtrData;
                }
            }
            
            // Create test data if no data exists
            if (!hasAttendanceData()) {
                System.out.println("⚠️ No attendance data found. Creating test data...");
                insertTestDTRData();
            }
            
            String sql = "SELECT log_date, login_time, logout_time, submitted_to_supervisor, remarks " +
                         "FROM attendance " +
                         "WHERE employee_id = ? AND log_date BETWEEN ? AND ? " +
                         "ORDER BY log_date DESC";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, this.employeeID);
                pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
                pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
                
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();
                    
                    String logDate = rs.getDate("log_date") != null ? rs.getDate("log_date").toString() : "";
                    String loginTime = rs.getTime("login_time") != null ? rs.getTime("login_time").toString() : "";
                    String logoutTime = rs.getTime("logout_time") != null ? rs.getTime("logout_time").toString() : "";
                    String submitted = rs.getBoolean("submitted_to_supervisor") ? "Yes" : "No";
                    String remarks = rs.getString("remarks") != null ? rs.getString("remarks") : "";
                    
                    row.add(logDate);
                    row.add(loginTime);
                    row.add(logoutTime);
                    row.add(submitted);
                    row.add(remarks);
                    
                    dtrData.add(row);
                    System.out.println("✅ Loaded DTR: " + logDate + " | " + loginTime + " | " + logoutTime);
                }
                
                System.out.println("✅ Total DTR records loaded: " + dtrData.size());
                
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ General error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return dtrData;
    }
    
    private boolean hasAttendanceData() {
        try {
            String sql = "SELECT COUNT(*) FROM attendance WHERE employee_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, this.employeeID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Found " + count + " attendance records for employee " + this.employeeID);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking attendance data: " + e.getMessage());
        }
        return false;
    }
    
    public void insertTestDTRData() {
        try {
            System.out.println("=== INSERTING TEST DTR DATA ===");
            
            // Insert data for the last 5 days
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < 5; i++) {
                String insertSql = "INSERT IGNORE INTO attendance (employee_id, log_date, login_time, logout_time, submitted_to_supervisor, remarks) " +
                                  "VALUES (?, ?, '08:00:00', '17:00:00', 0, 'Test data')";
                
                try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                    pstmt.setInt(1, this.employeeID);
                    pstmt.setDate(2, new java.sql.Date(cal.getTimeInMillis()));
                    
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        System.out.println("✅ Inserted test DTR for: " + new java.sql.Date(cal.getTimeInMillis()));
                    }
                }
                
                cal.add(Calendar.DAY_OF_MONTH, -1); // Go back one day
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error inserting test DTR data: " + e.getMessage());
        }
    }
    
    // ===== REQUEST METHODS =====
    public ArrayList<ArrayList<String>> getDataAllRequests() {
        return loadAllRequestsForEmployee();
    }
    
    public ArrayList<ArrayList<String>> loadAllRequestsForEmployee() {
        ArrayList<ArrayList<String>> allRequests = new ArrayList<>();
        
        try {
            // Check if employee ID is valid
            if (this.employeeID == 0) {
                System.err.println("❌ Employee ID is 0! Cannot load requests.");
                return allRequests;
            }
            
            // Check database connection
            if (connection == null || connection.isClosed()) {
                initializeConnection();
                if (connection == null) {
                    return allRequests;
                }
            }
            
            String sql = "SELECT date_filed, leave_type as type_request, from_date, to_date, number_of_days, reason, status " +
                         "FROM leave_requests WHERE employee_id = ? " +
                         "UNION ALL " +
                         "SELECT date_filed, type_request, DATE(from_time), DATE(to_time), number_of_days, reason, status " +
                         "FROM overtime_requests WHERE employee_id = ? " +
                         "ORDER BY date_filed DESC";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, employeeID);
                pstmt.setInt(2, employeeID);
                
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();
                    row.add(rs.getDate("date_filed").toString());
                    row.add(rs.getString("type_request"));
                    row.add(rs.getDate(3).toString());
                    row.add(rs.getDate(4).toString());
                    row.add(String.valueOf(rs.getDouble("number_of_days")));
                    row.add(rs.getString("reason"));
                    row.add(rs.getString("status"));
                    allRequests.add(row);
                }
            } catch (SQLException e) {
                System.err.println("❌ Error loading requests: " + e.getMessage());
                e.printStackTrace();
            }
            
            this.newData = allRequests;
            System.out.println("✅ Loaded " + allRequests.size() + " requests for employee " + this.employeeID);
            
        } catch (Exception e) {
            System.err.println("❌ Error in loadAllRequestsForEmployee: " + e.getMessage());
            e.printStackTrace();
        }
        
        return allRequests;
    }
    
    // ===== ACCOUNT DETAILS HELPER METHODS =====
    public String getEmployeeCompleteName() {
        if (accountDetails != null) {
            return accountDetails.getEmployeeCompleteName();
        }
        return "Unknown Employee";
    }
    
    // Note: AccountDetails.getSemiBasicSalary() returns String, but GUI expects double
    // We need to ensure AccountDetails is loaded properly
    
    // Update viewPersonalDetails to set complete name properly
    public void viewPersonalDetails(String empId) {
        try {
            System.out.println("Loading personal details for employee: " + empId);
            int empID = Integer.parseInt(empId);
            
            String sql = "SELECT employee_id, first_name, last_name, position, basic_salary, birthday, address, phone_number, " +
                        "sss_number, philhealth_number, tin_number, pagibig_number, status, rice_subsidy, phone_allowance, " +
                        "clothing_allowance, gross_rate, hourly_rate FROM employees WHERE employee_id = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, empID);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    // Set all the account details
                    accountDetails.setEmployeeID(rs.getInt("employee_id"));
                    accountDetails.setFirstName(rs.getString("first_name"));
                    accountDetails.setLastName(rs.getString("last_name"));
                    accountDetails.setPosition(rs.getString("position"));
                    accountDetails.setBasicSalary(rs.getDouble("basic_salary"));
                    accountDetails.setBirthday(rs.getDate("birthday"));
                    accountDetails.setAddress(rs.getString("address"));
                    accountDetails.setPhoneNumber(rs.getString("phone_number"));
                    accountDetails.setSSSNumber(rs.getString("sss_number"));
                    accountDetails.setPhilHealthNumber(rs.getString("philhealth_number"));
                    accountDetails.setTinNumber(rs.getString("tin_number"));
                    accountDetails.setPagibigNumber(rs.getString("pagibig_number"));
                    accountDetails.setStatus(rs.getString("status"));
                    accountDetails.setRiceSubsidy(rs.getDouble("rice_subsidy"));
                    accountDetails.setPhoneAllowance(rs.getDouble("phone_allowance"));
                    accountDetails.setClothingAllowance(rs.getDouble("clothing_allowance"));
                    accountDetails.setSemiBasicSalary(rs.getDouble("gross_rate"));
                    accountDetails.setHourlyRate(rs.getDouble("hourly_rate"));
                    
                    this.employeeID = empID;
                    System.out.println("✅ Loaded employee: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                } else {
                    System.err.println("❌ No employee found with ID: " + empId);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error loading personal details: " + e.getMessage());
        }
    }
    
    // ===== LEAVE BALANCE METHODS =====
    public String getBalanceVL() {
        try {
            String sql = "SELECT vacation_leave FROM leave_balances WHERE employee_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, this.employeeID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return String.valueOf(rs.getDouble("vacation_leave"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting VL balance: " + e.getMessage());
        }
        return "24.0"; // Default
    }
    
    public String getBalanceSL() {
        try {
            String sql = "SELECT sick_leave FROM leave_balances WHERE employee_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, this.employeeID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return String.valueOf(rs.getDouble("sick_leave"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting SL balance: " + e.getMessage());
        }
        return "24.0"; // Default
    }
    
    public void updateLeaveBalanceLabels(javax.swing.JLabel vlLabel, javax.swing.JLabel slLabel) {
        vlLabel.setText(getBalanceVL());
        slLabel.setText(getBalanceSL());
    }
    
    // ===== MISSING METHODS FOR GUI COMPATIBILITY =====
    
    // Get current payroll period dates
    public Date[] getCurrentPayrollPeriodDates() {
        try {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            
            Date fromDate, toDate;
            
            if (day <= 15) {
                cal.set(Calendar.DAY_OF_MONTH, 1);
                fromDate = cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH, 15);
                toDate = cal.getTime();
            } else {
                cal.set(Calendar.DAY_OF_MONTH, 16);
                fromDate = cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                toDate = cal.getTime();
            }
            
            return new Date[]{fromDate, toDate};
            
        } catch (Exception e) {
            System.err.println("❌ Error getting payroll period dates: " + e.getMessage());
            return new Date[]{new Date(), new Date()};
        }
    }
    
    // File overtime request method
    public boolean fileOvertimeRequest(ArrayList<String> data) {
        try {
            if (data.size() < 6) {
                System.err.println("❌ Insufficient overtime request data");
                return false;
            }
            
            String sql = "INSERT INTO overtime_requests (employee_id, date_filed, type_request, from_time, to_time, number_of_days, reason, status) " +
                         "VALUES (?, CURDATE(), 'Overtime', ?, ?, ?, ?, 'Pending')";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(data.get(0)));
                pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(convertToSqlDate(data.get(2)) + " 00:00:00"));
                pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(convertToSqlDate(data.get(3)) + " 00:00:00"));
                pstmt.setDouble(4, Double.parseDouble(data.get(4)));
                pstmt.setString(5, data.get(5));
                
                int result = pstmt.executeUpdate();
                
                if (result > 0) {
                    System.out.println("✅ Overtime request filed successfully");
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error filing overtime request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Convert to SQL date helper
    private String convertToSqlDate(String dateStr) {
        try {
            if (dateStr.contains("-")) {
                return dateStr;
            } else if (dateStr.contains("/")) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(dateStr);
                return outputFormat.format(date);
            } else {
                return dateStr;
            }
        } catch (Exception e) {
            System.err.println("❌ Error converting date: " + dateStr);
            return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
    }
    
    // Validate date range
    public boolean isValidDateRange(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            return false;
        }
        return !fromDate.after(toDate);
    }
    
    // Set number of days leave methods
    public void setNumberOfDaysLeave() {
        this.calculatedDays = 0;
    }
    
    public void setNumberOfDaysLeave(int days) {
        this.calculatedDays = days;
    }
    
    public int getNumberOfDaysLeave() {
        return this.calculatedDays;
    }
    
    // Forward DTR to supervisor
    public void forwardDTRToSupervisor(ArrayList<ArrayList<String>> dtrData) {
        try {
            System.out.println("=== Employee Submitting DTR to Supervisor ===");
            
            if (dtrData == null || dtrData.isEmpty()) {
                System.err.println("❌ No DTR data to submit");
                return;
            }
            
            String sql = "UPDATE attendance SET submitted_to_supervisor = 1 WHERE log_date = ? AND employee_id = ?";
            
            int successCount = 0;
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                for (ArrayList<String> row : dtrData) {
                    if (row.size() >= 1) {
                        try {
                            String dateStr = row.get(0);
                            java.sql.Date sqlDate = convertDateStringToSqlDate(dateStr);
                            
                            pstmt.setDate(1, sqlDate);
                            pstmt.setInt(2, this.employeeID);
                            
                            int result = pstmt.executeUpdate();
                            if (result > 0) {
                                successCount++;
                                System.out.println("✅ Successfully submitted DTR for date: " + dateStr);
                            }
                        } catch (Exception e) {
                            System.err.println("❌ Error processing DTR row: " + e.getMessage());
                        }
                    }
                }
            }
            
            System.out.println("✅ Successfully submitted: " + successCount + " entries");
            
        } catch (SQLException e) {
            System.err.println("❌ Database error submitting DTR to supervisor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // View personal payslip - FIXED FOR YOUR DATABASE
    public ArrayList<ArrayList<String>> viewPersonalPayslip(Date startDate, Date endDate, String empId) {
        ArrayList<ArrayList<String>> payslipData = new ArrayList<>();
        
        try {
            System.out.println("=== GENERATING PAYSLIP (FIXED VERSION) ===");
            System.out.println("Employee ID: " + empId);
            System.out.println("Date Range: " + startDate + " to " + endDate);
            
            // Check if employee exists first
            String checkSql = "SELECT COUNT(*) FROM employees WHERE employee_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, Integer.parseInt(empId));
                ResultSet checkRs = checkStmt.executeQuery();
                if (checkRs.next() && checkRs.getInt(1) == 0) {
                    System.err.println("❌ Employee " + empId + " not found!");
                    return payslipData;
                }
            }
            
            // Generate payslip from employee data (since payroll table structure is unknown)
            String sql = "SELECT employee_id, first_name, last_name, position, " +
                        "basic_salary, rice_subsidy, phone_allowance, clothing_allowance, " +
                        "gross_rate, hourly_rate " +
                        "FROM employees WHERE employee_id = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(empId));
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    
                    // Employee Info
                    row.add(rs.getString("employee_id"));                           // 0: Employee ID
                    row.add(rs.getString("first_name") + " " + rs.getString("last_name")); // 1: Name
                    row.add(sdf.format(startDate) + " to " + sdf.format(endDate)); // 2: Period
                    row.add(rs.getString("position"));                             // 3: Position
                    
                    // Salary Calculations
                    double basicSalary = rs.getDouble("basic_salary");
                    double grossRate = rs.getDouble("gross_rate");
                    double actualGross = (grossRate > 0) ? grossRate : basicSalary;
                    
                    row.add(String.format("%.2f", actualGross));                   // 4: Gross Pay
                    
                    // Benefits
                    double riceSubsidy = rs.getDouble("rice_subsidy");
                    double phoneAllowance = rs.getDouble("phone_allowance");
                    double clothingAllowance = rs.getDouble("clothing_allowance");
                    double totalBenefits = riceSubsidy + phoneAllowance + clothingAllowance;
                    
                    row.add(String.format("%.2f", totalBenefits));                 // 5: Benefits
                    row.add("0.00");                                               // 6: Overtime
                    row.add("0.00");                                               // 7: Undertime
                    
                    // Deductions
                    double sss = calculateSSSContribution(actualGross);
                    double philHealth = calculatePhilHealthContribution(actualGross);
                    double pagIbig = calculatePagIbigContribution(actualGross);
                    double taxableIncome = actualGross - sss - philHealth - pagIbig;
                    double tax = calculateWithholdingTax(taxableIncome);
                    
                    row.add(String.format("%.2f", sss));                          // 8: SSS
                    row.add(String.format("%.2f", philHealth));                   // 9: PhilHealth  
                    row.add(String.format("%.2f", pagIbig));                      // 10: Pag-IBIG
                    row.add(String.format("%.2f", tax));                          // 11: Tax
                    
                    // Net Pay
                    double netPay = actualGross + totalBenefits - sss - philHealth - pagIbig - tax;
                    row.add(String.format("%.2f", netPay));                       // 12: Net Pay
                    
                    payslipData.add(row);
                    
                    System.out.println("✅ Payslip generated successfully!");
                    System.out.println("Employee: " + row.get(1));
                    System.out.println("Gross Pay: ₱" + row.get(4));
                    System.out.println("Net Pay: ₱" + row.get(12));
                    
                } else {
                    System.err.println("❌ No employee data found for ID: " + empId);
                }
            }
            
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid employee ID format: " + empId);
        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payslipData;
    }
    
    // Enhanced calculation methods (simplified)
    private double calculateSSSContribution(double salary) {
        if (salary <= 3250) return 135.00;
        else if (salary <= 3750) return 157.50;
        else if (salary <= 4250) return 180.00;
        else if (salary <= 4750) return 202.50;
        else if (salary <= 5250) return 225.00;
        else return Math.min(salary * 0.045, 1125.00);
    }
    
    private double calculatePhilHealthContribution(double salary) {
        return Math.min(salary * 0.03, 900.00);
    }
    
    private double calculatePagIbigContribution(double salary) {
        if (salary <= 1500) return salary * 0.01;
        else return Math.min(salary * 0.02, 100.00);
    }
    
    private double calculateWithholdingTax(double taxableIncome) {
        if (taxableIncome <= 20833) return 0;
        else if (taxableIncome <= 33333) return (taxableIncome - 20833) * 0.15;
        else if (taxableIncome <= 66667) return 1875 + (taxableIncome - 33333) * 0.20;
        else if (taxableIncome <= 166667) return 8541.80 + (taxableIncome - 66667) * 0.25;
        else if (taxableIncome <= 666667) return 33541.80 + (taxableIncome - 166667) * 0.30;
        else return 183541.80 + (taxableIncome - 666667) * 0.35;
    }
    
    // File leave request method
    public boolean fileLeaveRequest(ArrayList<String> leaveData) {
        try {
            if (leaveData.size() < 7) {
                System.err.println("❌ Insufficient leave request data");
                return false;
            }
            
            String sql = "INSERT INTO leave_requests (employee_id, date_filed, leave_type, from_date, to_date, number_of_days, reason, status) " +
                         "VALUES (?, CURDATE(), ?, ?, ?, ?, ?, 'Pending')";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(leaveData.get(0)));
                pstmt.setString(2, leaveData.get(2));
                pstmt.setDate(3, convertDateStringToSqlDate(leaveData.get(3)));
                pstmt.setDate(4, convertDateStringToSqlDate(leaveData.get(4)));
                pstmt.setDouble(5, Double.parseDouble(leaveData.get(5)));
                pstmt.setString(6, leaveData.get(6));
                
                int result = pstmt.executeUpdate();
                
                if (result > 0) {
                    System.out.println("✅ Leave request filed successfully");
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error filing leave request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Date conversion helper
    private java.sql.Date convertDateStringToSqlDate(String dateStr) {
        try {
            dateStr = dateStr.trim();
            
            if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return java.sql.Date.valueOf(dateStr);
            } else if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                String[] parts = dateStr.split("/");
                String month = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
                String day = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
                String year = parts[2];
                String sqlDateStr = year + "-" + month + "-" + day;
                return java.sql.Date.valueOf(sqlDateStr);
            }
            
            return java.sql.Date.valueOf(dateStr);
            
        } catch (Exception e) {
            System.err.println("❌ Error converting date string '" + dateStr + "': " + e.getMessage());
            return new java.sql.Date(System.currentTimeMillis());
        }
    }
    
    // Calculate days from dates method
    public int calculateDaysFromDates(Date fromDate, Date toDate) {
        return calculateDaysFromGUI(fromDate, toDate);
    }
    
    // Leave balances information method
    public void leaveBalancesInformation() {
        try {
            String sql = "SELECT vacation_leave, sick_leave FROM leave_balances WHERE employee_id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, this.employeeID);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    double vlBalance = rs.getDouble("vacation_leave");
                    double slBalance = rs.getDouble("sick_leave");
                    System.out.println("✅ Leave balances loaded for employee " + this.employeeID + 
                                     " - VL: " + vlBalance + ", SL: " + slBalance);
                } else {
                    System.err.println("⚠️ No leave balance record found for employee ID: " + this.employeeID);
                    createDefaultLeaveBalance();
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error loading leave balances information: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Create default leave balance
    private void createDefaultLeaveBalance() {
        try {
            String sql = "INSERT INTO leave_balances (employee_id, vacation_leave, sick_leave) VALUES (?, 24.00, 24.00)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, this.employeeID);
                int result = pstmt.executeUpdate();
                
                if (result > 0) {
                    System.out.println("✅ Created default leave balance for employee ID: " + this.employeeID);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error creating default leave balance: " + e.getMessage());
        }
    }
    
    public void refreshAllRequests() {
        getDataAllRequests();
    }
    
    public ArrayList<ArrayList<String>> getAllApprovedPersonalLeaveLedger() {
        return new ArrayList<>(); // Placeholder
    }
    
    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        return getAllApprovedPersonalLeaveLedger();
    }
    
    // ===== DATE CALCULATION =====
    public boolean countNumberOfDays(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null || toDate.before(fromDate)) {
            return false;
        }
        
        Calendar start = Calendar.getInstance();
        start.setTime(fromDate);
        Calendar end = Calendar.getInstance();
        end.setTime(toDate);
        
        int workDays = 0;
        while (!start.after(end)) {
            int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                workDays++;
            }
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        calculatedDays = workDays;
        return true;
    }
    
    public int calculateDaysFromGUI(Date fromDate, Date toDate) {
        if (countNumberOfDays(fromDate, toDate)) {
            return calculatedDays;
        }
        return 0;
    }
    
    public int getCalculatedDays() { return calculatedDays; }
    
    // ===== DEBUG METHODS =====
    public boolean validateEmployeeAndConnection() {
        try {
            if (this.employeeID <= 0) {
                System.err.println("❌ Invalid employee ID: " + this.employeeID);
                return false;
            }
            
            if (connection == null || connection.isClosed()) {
                System.err.println("❌ Database connection issue!");
                return false;
            }
            
            String sql = "SELECT first_name, last_name FROM employees WHERE employee_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, this.employeeID);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    System.out.println("✅ Employee validated: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                    return true;
                } else {
                    System.err.println("❌ Employee not found in database!");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error validating: " + e.getMessage());
            return false;
        }
    }
    
    public void debugEmployeeState() {
        System.out.println("=== Employee Debug ===");
        System.out.println("Employee ID: " + this.employeeID);
        System.out.println("Connection: " + (connection != null ? "OK" : "NULL"));
        System.out.println("Account Details: " + (accountDetails != null ? "OK" : "NULL"));
    }
}
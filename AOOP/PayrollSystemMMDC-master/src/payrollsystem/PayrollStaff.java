package payrollsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;

public class PayrollStaff extends Employee implements Payroll {
    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;
    private AttendanceDAO attendanceDAO;
    private ArrayList<ArrayList<String>> tableData;
    private ArrayList<ArrayList<String>> newData = new ArrayList<>();
    private int tableSize;
    private DatabaseConnection dbConnection;
    private Connection connection;
    private int numberOfDaysLeave = 0;
    private double perHour;
    private double perMonth; 
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    
    public PayrollStaff() {
        super();
        initializeDAOs();
        initializeConnection();
    }
    
    public PayrollStaff(String employeeId) {
        super(employeeId);
        initializeDAOs();
        initializeConnection();
    }
    
    private void initializeDAOs() {
        this.payrollDAO = new PayrollDAO();
        this.employeeDAO = new EmployeeDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.tableData = new ArrayList<>();
        this.newData = new ArrayList<>();
    }
    
    private void initializeConnection() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.connection = dbConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
    
    // *** VIEW PERSONAL DETAILS METHOD ***
    public void viewPersonalDetails(String employeeId) {
        String sql = "SELECT e.employee_id, e.last_name, e.first_name, e.birthday, e.address, e.phone_number, " +
                    "e.sss_number, e.philhealth_number, e.tin_number, e.pagibig_number, e.status, e.position, " +
                    "e.supervisor_id, " +
                    "CONCAT(s.first_name, ' ', s.last_name) as supervisor_name, " +
                    "e.basic_salary, e.rice_subsidy, e.phone_allowance, e.clothing_allowance, " +
                    "e.gross_rate, e.hourly_rate " +
                    "FROM employees e " +
                    "LEFT JOIN employees s ON e.supervisor_id = s.employee_id " +
                    "WHERE e.employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(employeeId));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                if (accountDetails == null) {
                    accountDetails = new AccountDetails();
                }
                
                accountDetails.setEmployeeID(rs.getInt("employee_id"));
                accountDetails.setLastName(rs.getString("last_name"));
                accountDetails.setFirstName(rs.getString("first_name"));
                accountDetails.setBirthday(rs.getDate("birthday"));
                accountDetails.setAddress(rs.getString("address"));
                accountDetails.setPhoneNumber(rs.getString("phone_number"));
                
                accountDetails.setSSSNumber(formatGovernmentNumber(rs.getString("sss_number")));
                accountDetails.setPhilHealthNumber(formatGovernmentNumber(rs.getString("philhealth_number")));
                accountDetails.setTinNumber(formatGovernmentNumber(rs.getString("tin_number")));
                accountDetails.setPagibigNumber(formatGovernmentNumber(rs.getString("pagibig_number")));
                
                accountDetails.setStatus(rs.getString("status"));
                accountDetails.setPosition(rs.getString("position"));
                
                String supervisorName = rs.getString("supervisor_name");
                accountDetails.setSupervisor(supervisorName != null ? supervisorName : "");
                
                accountDetails.setBasicSalary(rs.getDouble("basic_salary"));
                accountDetails.setRiceSubsidy(rs.getDouble("rice_subsidy"));
                accountDetails.setPhoneAllowance(rs.getDouble("phone_allowance"));
                accountDetails.setClothingAllowance(rs.getDouble("clothing_allowance"));
                accountDetails.setSemiBasicSalary(rs.getDouble("gross_rate"));
                accountDetails.setHourlyRate(rs.getDouble("hourly_rate"));
                
                System.out.println("DEBUG: Successfully loaded personal details for payroll staff " + employeeId);
            } else {
                System.err.println("ERROR: No employee found with ID " + employeeId);
            }
        } catch (SQLException e) {
            System.err.println("Error loading personal details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // *** HELPER METHODS ***
    private String formatGovernmentNumber(String numberStr) {
        if (numberStr == null || numberStr.trim().isEmpty()) {
            return "";
        }
        
        try {
            if (numberStr.toUpperCase().contains("E")) {
                double scientificNumber = Double.parseDouble(numberStr);
                return String.format("%.0f", scientificNumber);
            } else {
                return numberStr;
            }
        } catch (NumberFormatException e) {
            return numberStr;
        }
    }

    private String convertToSqlDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (Exception e) {
            System.err.println("Error converting date: " + e.getMessage());
            return dateStr;
        }
    }

    // *** DATE CALCULATION METHOD ***
    public int calculateDaysFromDates(java.util.Date fromDate, java.util.Date toDate) {
        System.out.println("DEBUG: calculateDaysFromDates called with fromDate=" + fromDate + ", toDate=" + toDate);
        if (fromDate == null || toDate == null) {
            System.out.println("DEBUG: One or both dates are null");
            return 0;
        }
        
        if (toDate.before(fromDate)) {
            System.out.println("DEBUG: Invalid date range - toDate is before fromDate");
            return 0;
        }
        
        long diffInMillies = toDate.getTime() - fromDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        int days = (int) diffInDays + 1;
        
        System.out.println("DEBUG: Calculated " + days + " days between " + fromDate + " and " + toDate);
        return days;
    }

    // *** DATA RETRIEVAL METHODS ***
    public ArrayList<ArrayList<String>> getDataAllRequests() {
        ArrayList<ArrayList<String>> requests = new ArrayList<>();
        
        String sql = "SELECT date_filed, 'Leave' as request_type, from_date, to_date, number_of_days, reason, status " +
                    "FROM leave_requests WHERE employee_id = ? " +
                    "UNION ALL " +
                    "SELECT date_filed, 'Overtime' as request_type, DATE(from_time), DATE(to_time), number_of_days, reason, status " +
                    "FROM overtime_requests WHERE employee_id = ? " +
                    "ORDER BY date_filed DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int empId = accountDetails.getEmployeeID();
            pstmt.setInt(1, empId);
            pstmt.setInt(2, empId);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(rs.getDate("date_filed").toString());
                row.add(rs.getString("request_type"));
                row.add(rs.getDate(3).toString());
                row.add(rs.getDate(4).toString());
                row.add(String.valueOf(rs.getDouble("number_of_days")));
                row.add(rs.getString("reason"));
                row.add(rs.getString("status"));
                requests.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error loading requests: " + e.getMessage());
        }
        
        return requests;
    }

    public ArrayList<ArrayList<String>> getDataAllDTR(Date fromDate, Date toDate) {
        ArrayList<ArrayList<String>> dtrRecords = new ArrayList<>();
        String sql = "SELECT log_date, login_time, logout_time, " +
                    "CASE WHEN submitted_to_supervisor = 1 THEN 'Yes' ELSE 'No' END as submitted, " +
                    "COALESCE(remarks, '') as remarks FROM attendance " +
                    "WHERE employee_id = ? AND log_date BETWEEN ? AND ? ORDER BY log_date";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountDetails.getEmployeeID());
            pstmt.setDate(2, new java.sql.Date(fromDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(toDate.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(rs.getDate("log_date").toString());
                row.add(rs.getTime("login_time") != null ? rs.getTime("login_time").toString() : "");
                row.add(rs.getTime("logout_time") != null ? rs.getTime("logout_time").toString() : "");
                row.add(rs.getString("submitted"));
                row.add(rs.getString("remarks"));
                dtrRecords.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting DTR data: " + e.getMessage());
        }
        
        return dtrRecords;
    }

    public ArrayList<ArrayList<String>> getAllApprovedPersonalLeaveLedger() {
        ArrayList<ArrayList<String>> leaveData = new ArrayList<>();
        String sql = "SELECT date_filed, leave_type, from_date, to_date, number_of_days, reason, status " +
                    "FROM leave_requests WHERE employee_id = ? AND status = 'Approved' ORDER BY date_filed DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountDetails.getEmployeeID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(rs.getDate("date_filed").toString());
                row.add(rs.getString("leave_type"));
                row.add(rs.getDate("from_date").toString());
                row.add(rs.getDate("to_date").toString());
                row.add(String.valueOf(rs.getDouble("number_of_days")));
                row.add(rs.getString("reason"));
                row.add(rs.getString("status"));
                leaveData.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting leave ledger: " + e.getMessage());
        }
        
        return leaveData;
    }

    // *** LEAVE BALANCE METHODS ***
    public void leaveBalancesInformation() {
        String sql = "SELECT vacation_leave, sick_leave FROM leave_balances WHERE employee_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountDetails.getEmployeeID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("VL Balance: " + rs.getDouble("vacation_leave"));
                System.out.println("SL Balance: " + rs.getDouble("sick_leave"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting leave balances: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public String getBalanceVL() {
        try {
            String sql = "SELECT vacation_leave FROM leave_balances WHERE employee_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountDetails.getEmployeeID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String balance = String.valueOf(rs.getDouble("vacation_leave"));
                rs.close();
                pstmt.close();
                return balance;
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.err.println("Error getting VL balance: " + e.getMessage());
            e.printStackTrace();
        }
        return "24.0";
    }

    public String getBalanceSL() {
        try {
            String sql = "SELECT sick_leave FROM leave_balances WHERE employee_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountDetails.getEmployeeID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String balance = String.valueOf(rs.getDouble("sick_leave"));
                rs.close();
                pstmt.close();
                return balance;
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.err.println("Error getting SL balance: " + e.getMessage());
            e.printStackTrace();
        }
        return "24.0";
    }
    
    public void updateLeaveBalanceLabels(JLabel vlLabel, JLabel slLabel) {
        vlLabel.setText(getBalanceVL());
        slLabel.setText(getBalanceSL());
    }

    // *** REQUEST FILING METHODS ***
    public boolean fileLeaveRequest(ArrayList<String> data) {
        String sql = "INSERT INTO leave_requests (employee_id, date_filed, leave_type, from_date, to_date, number_of_days, reason, status) " +
                    "VALUES (?, CURDATE(), ?, ?, ?, ?, ?, 'Pending')";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(data.get(0)));
            pstmt.setString(2, data.get(2));
            pstmt.setDate(3, java.sql.Date.valueOf(convertToSqlDate(data.get(3))));
            pstmt.setDate(4, java.sql.Date.valueOf(convertToSqlDate(data.get(4))));
            pstmt.setDouble(5, Double.parseDouble(data.get(5)));
            pstmt.setString(6, data.get(6));
            
            int rowsAffected = pstmt.executeUpdate();
            data.clear();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error filing leave request: " + e.getMessage());
            data.clear();
            return false;
        }
    }

    public boolean fileOvertimeRequest(ArrayList<String> data) {
        String sql = "INSERT INTO overtime_requests (employee_id, date_filed, type_request, from_time, to_time, number_of_days, reason, status) " +
                    "VALUES (?, CURDATE(), 'Overtime', ?, ?, ?, ?, 'Pending')";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(data.get(0)));
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(convertToSqlDate(data.get(2)) + " 00:00:00"));
            pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(convertToSqlDate(data.get(3)) + " 00:00:00"));
            pstmt.setDouble(4, Double.parseDouble(data.get(4)));
            pstmt.setString(5, data.get(5));
            
            int rowsAffected = pstmt.executeUpdate();
            data.clear();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error filing overtime request: " + e.getMessage());
            data.clear();
            return false;
        }
    }

    // *** ATTENDANCE METHODS ***
    public void userLogin() {
        try {
            AttendanceService attendanceService = new AttendanceService();
            int employeeId = accountDetails.getEmployeeID();
            AttendanceResult result = attendanceService.processTimeIn(employeeId);
            
            if (result.isSuccess()) {
                System.out.println("Time In successful: " + result.getMessage());
            } else {
                System.out.println("Time In failed: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error during time in: " + e.getMessage());
        }
    }

    public void userLogout() {
        try {
            AttendanceService attendanceService = new AttendanceService();
            int employeeId = accountDetails.getEmployeeID();
            AttendanceResult result = attendanceService.processTimeOut(employeeId);
            
            if (result.isSuccess()) {
                System.out.println("Time Out successful: " + result.getMessage());
            } else {
                System.out.println("Time Out failed: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error during time out: " + e.getMessage());
        }
    }

    public ArrayList<ArrayList<String>> getEmployeeDTR(String employeeName, Date fromDate, Date toDate) {
    ArrayList<ArrayList<String>> dtrData = new ArrayList<>();
    
    // First, get the employee ID from the name
    String sql = "SELECT e.employee_id FROM employees e " +
                "WHERE CONCAT(e.first_name, ' ', e.last_name) = ? " +
                "OR CONCAT(e.last_name, ', ', e.first_name) = ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employeeName);
            pstmt.setString(2, employeeName);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int employeeId = rs.getInt("employee_id");

                // Now get the DTR data for this employee
                dtrData = getDTRDataByEmployeeId(employeeId, fromDate, toDate);

                System.out.println("DEBUG: Loaded DTR for employee: " + employeeName + " (ID: " + employeeId + ")");
                System.out.println("DEBUG: Found " + dtrData.size() + " DTR records");

            } else {
                System.err.println("ERROR: No employee found with name: " + employeeName);
            }

        } catch (SQLException e) {
            System.err.println("Error getting employee DTR: " + e.getMessage());
            e.printStackTrace();
        }

        return dtrData;
    }
       
    public ArrayList<ArrayList<String>> getDTRDataByEmployeeId(int employeeId, Date fromDate, Date toDate) {
    ArrayList<ArrayList<String>> dtrData = new ArrayList<>();
    
    String sql = "SELECT a.log_date, a.login_time, a.logout_time, " +
                "CASE WHEN a.submitted_to_supervisor = 1 THEN 'Yes' ELSE 'No' END as submitted, " +
                "COALESCE(a.remarks, '') as remarks, " +
                "e.first_name, e.last_name " +
                "FROM attendance a " +
                "JOIN employees e ON a.employee_id = e.employee_id " +
                "WHERE a.employee_id = ? AND a.log_date BETWEEN ? AND ? " +
                "ORDER BY a.log_date DESC";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, employeeId);
        pstmt.setDate(2, new java.sql.Date(fromDate.getTime()));
        pstmt.setDate(3, new java.sql.Date(toDate.getTime()));
        
        ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(employeeId)); // Employee ID
                row.add(rs.getString("first_name") + " " + rs.getString("last_name")); // Employee Name
                row.add(rs.getDate("log_date").toString()); // Date
                row.add(rs.getTime("login_time") != null ? rs.getTime("login_time").toString() : ""); // Login Time
                row.add(rs.getTime("logout_time") != null ? rs.getTime("logout_time").toString() : ""); // Logout Time
                row.add(rs.getString("submitted")); // Submitted to Supervisor
                row.add(rs.getString("remarks")); // Remarks
                dtrData.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Error getting DTR data by employee ID: " + e.getMessage());
            e.printStackTrace();
        }

        return dtrData;
}

    public ArrayList<String> getAllEmployeeNames() {
        ArrayList<String> employeeNames = new ArrayList<>();

        String sql = "SELECT CONCAT(first_name, ' ', last_name) as full_name " +
                    "FROM employees " +
                    "ORDER BY last_name, first_name";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                employeeNames.add(rs.getString("full_name"));
            }

            System.out.println("DEBUG: Loaded " + employeeNames.size() + " employee names for dropdown");

        } catch (SQLException e) {
            System.err.println("Error loading employee names: " + e.getMessage());
            e.printStackTrace();
        }
    
    return employeeNames;
}
    
    public Date[] getCurrentPayrollPeriodDates() {
    Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date startDate, endDate;

        if (day <= 15) {
            // First half of month (1-15)
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, 15);
            endDate = cal.getTime();
        } else {
            // Second half of month (16-end)
            cal.set(Calendar.DAY_OF_MONTH, 16);
            startDate = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = cal.getTime();
    }
    
    return new Date[]{startDate, endDate};
}
    
    public void forwardDTRToSupervisor(ArrayList<ArrayList<String>> dtrData) {
        String sql = "UPDATE attendance SET submitted_to_supervisor = 1 WHERE employee_id = ? AND log_date = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (ArrayList<String> row : dtrData) {
                pstmt.setInt(1, accountDetails.getEmployeeID());
                pstmt.setDate(2, java.sql.Date.valueOf(row.get(0)));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("DTR forwarded to supervisor");
        } catch (SQLException e) {
            System.err.println("Error forwarding DTR to supervisor: " + e.getMessage());
        }
    }

    public String getCurrentPayrollPeriod() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_MONTH);
        String startDate, endDate;

        if (day <= 15) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = sdf.format(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, 15);
            endDate = sdf.format(cal.getTime());
        } else {
            cal.set(Calendar.DAY_OF_MONTH, 16);
            startDate = sdf.format(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = sdf.format(cal.getTime());
        }

        return startDate + " to " + endDate;
    }
    
    // *** VALIDATION METHODS ***
    public boolean isValidDateRange(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            return false;
        }
        numberOfDaysLeave = calculateDaysFromDates(fromDate, toDate);
        return !toDate.before(fromDate);
    }

    public int getNumberOfDaysLeave() {
        return numberOfDaysLeave;
    }

    public void setNumberOfDaysLeave() {
        this.numberOfDaysLeave = 0;
    }

    // *** PAYROLL METHODS ***
    public String processPayrollDB() {
        try {
            String payrollPeriod = getCurrentPayrollPeriod();
            System.out.println("Processing payroll for period: " + payrollPeriod);
            
            boolean success = payrollDAO.processPayrollForAllEmployees(payrollPeriod);
            
            if (success) {
                System.out.println("Payroll processing completed successfully!");
                return payrollPeriod;
            } else {
                System.out.println("Payroll processing failed!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<ArrayList<String>> getDataForPayrollTable() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        
        try {
            String currentPeriod = getCurrentPayrollPeriod();
            List<PayrollRecord> payrolls = payrollDAO.getPayrollByMonth(
                currentPeriod.substring(0, 2),
                Integer.parseInt(currentPeriod.substring(6, 10))
            );
            
            for (PayrollRecord payroll : payrolls) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(payroll.getEmployeeId()));
                row.add(payroll.getEmployeeName());
                row.add(payroll.getPayrollPeriod());
                row.add(payroll.getPosition());
                row.add(String.format("%.2f", payroll.getGrossIncome()));
                row.add(String.format("%.2f", payroll.getBenefits()));
                row.add(String.format("%.2f", payroll.getOvertime()));
                row.add(String.format("%.2f", payroll.getUndertime()));
                row.add(String.format("%.2f", payroll.getSss()));
                row.add(String.format("%.2f", payroll.getPhilhealth()));
                row.add(String.format("%.2f", payroll.getPagibig()));
                row.add(String.format("%.2f", payroll.getTax()));
                row.add(String.format("%.2f", payroll.getNetPay()));
                row.add(payroll.getStatus());
                data.add(row);
            }
            
            System.out.println("Retrieved " + data.size() + " payroll records from database");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }
    
    public void releasedPayroll(ArrayList<ArrayList<String>> selectedPayrolls) {
        try {
            for (ArrayList<String> payrollData : selectedPayrolls) {
                int employeeId = Integer.parseInt(payrollData.get(0));
                String payrollPeriod = payrollData.get(2);
                
                List<PayrollRecord> payrolls = payrollDAO.getPayrollByEmployee(employeeId);
                for (PayrollRecord payroll : payrolls) {
                    if (payroll.getPayrollPeriod().equals(payrollPeriod)) {
                        payroll.setStatus("Released");
                        payrollDAO.updatePayroll(payroll);
                        System.out.println("✅ Released payroll for Employee ID: " + employeeId);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // *** TABLE MANAGEMENT METHODS ***
    public void setTableData(ArrayList<ArrayList<String>> data) {
        this.tableData = data;
        this.newData = data;
    }
    
    public void setTableSize(int size) {
        this.tableSize = size;
    }
    
    public void displayDataTable(javax.swing.JTable table) {
        try {
            System.out.println("=== DEBUG displayDataTable ===");
            System.out.println("Table: " + (table != null ? table.getClass().getSimpleName() : "NULL"));
            System.out.println("TableData: " + (tableData != null ? tableData.size() + " rows" : "NULL"));
            
            if (tableData != null && !tableData.isEmpty()) {
                System.out.println("First row data: " + tableData.get(0));
            }
            
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            
            if (tableData != null && !tableData.isEmpty()) {
                for (ArrayList<String> row : tableData) {
                    Object[] rowData = new Object[Math.min(row.size(), model.getColumnCount())];
                    for (int j = 0; j < rowData.length; j++) {
                        rowData[j] = (j < row.size()) ? row.get(j) : "";
                    }
                    model.addRow(rowData);
                }
                System.out.println("Successfully added " + tableData.size() + " rows to table");
            } else {
                System.out.println("No data to display - tableData is empty or null");
            }
            
        } catch (Exception e) {
            System.err.println("Error displaying data in table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // *** UTILITY METHODS ***
    public void getEmployeeNames() {
        if (newData == null) {
            newData = new ArrayList<>();
        }
        newData.clear();
        try {
            List<AccountDetails> employees = employeeDAO.findAll();
            for (AccountDetails emp : employees) {
                ArrayList<String> empData = new ArrayList<>();
                empData.add(emp.getLastName() + ", " + emp.getFirstName());
                newData.add(empData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<String>> getNewData() {
        if (newData == null) {
            newData = new ArrayList<>();
        }
        return newData;
    }

    public boolean computePayrollForDateRange(Date fromDate, Date toDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String payrollPeriod = sdf.format(fromDate) + " to " + sdf.format(toDate);
            
            System.out.println("Computing payroll for period: " + payrollPeriod);
            
            List<AccountDetails> employees = employeeDAO.findAll();
            int processedCount = 0;
            
            for (AccountDetails employee : employees) {
                if (employee != null && employee.getEmployeeID() > 0) {
                    List<AttendanceRecord> attendanceRecords = attendanceDAO.getAttendanceByEmployee(
                        employee.getEmployeeID(), fromDate, toDate);
                    
                    if (!attendanceRecords.isEmpty()) {
                        PayrollRecord payroll = calculatePayrollForEmployee(employee, attendanceRecords, payrollPeriod);
                        
                        if (payroll != null) {
                            if (payrollExistsForPeriod(employee.getEmployeeID(), payrollPeriod)) {
                                int existingPayrollId = getExistingPayrollId(employee.getEmployeeID(), payrollPeriod);
                                payroll.setPayrollId(existingPayrollId);
                                payrollDAO.updatePayroll(payroll);
                                System.out.println("📝 Updated payroll for Employee ID: " + employee.getEmployeeID());
                            } else {
                                payrollDAO.savePayroll(payroll);
                                System.out.println("✅ Created payroll for Employee ID: " + employee.getEmployeeID());
                            }
                            processedCount++;
                        }
                    }
                }
            }
            
            System.out.println("Payroll computation completed. Processed " + processedCount + " employees.");
            return processedCount > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private PayrollRecord calculatePayrollForEmployee(AccountDetails employee, List<AttendanceRecord> attendanceRecords, String payrollPeriod) {
        try {
            PayrollRecord payroll = new PayrollRecord();
            payroll.setEmployeeId(employee.getEmployeeID());
            payroll.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
            payroll.setPayrollPeriod(payrollPeriod);
            payroll.setPosition(employee.getPosition());

            ArrayList<ArrayList<String>> perEmployeeAttendance = convertAttendanceRecords(attendanceRecords);

            this.perHour = employee.getHourlyRate();
            this.perMonth = employee.getBasicSalary();
            this.riceSubsidy = employee.getRiceSubsidy();
            this.phoneAllowance = employee.getPhoneAllowance();
            this.clothingAllowance = employee.getClothingAllowance();

            double gross = grossCalculation(perEmployeeAttendance);
            double benefits = benefitsCalculation();
            double overtime = overtimeCalculations(perEmployeeAttendance);
            double undertime = undertimeCalculations(perEmployeeAttendance);
            double sss = sssCalculation();
            double philhealth = philhealthCalculation();
            double pagibig = pagibigCalculation();
            double tax = taxCalculation(sss + philhealth + pagibig);
            double netPay = netPayrollCalculations(gross, benefits, overtime, undertime, sss + philhealth + pagibig + tax);

            payroll.setGrossIncome(gross);
            payroll.setBenefits(benefits);
            payroll.setOvertime(overtime);
            payroll.setUndertime(undertime);
            payroll.setSss(sss);
            payroll.setPhilhealth(philhealth);
            payroll.setPagibig(pagibig);
            payroll.setTax(tax);
            payroll.setNetPay(netPay);
            payroll.setStatus("Pending");

            return payroll;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private ArrayList<ArrayList<String>> convertAttendanceRecords(List<AttendanceRecord> attendanceRecords) {
        ArrayList<ArrayList<String>> converted = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        
        for (AttendanceRecord record : attendanceRecords) {
            ArrayList<String> row = new ArrayList<>();
            row.add(String.valueOf(record.getEmployeeId())); // 0 - Employee ID
            row.add(record.getEmployeeName()); // 1 - Employee Name
            row.add(dateFormat.format(record.getLogDate())); // 2 - Date
            row.add(timeFormat.format(record.getLoginTime())); // 3 - Login Time
            row.add(timeFormat.format(record.getLogoutTime())); // 4 - Logout Time
            row.add(record.isSubmittedToSupervisor() ? "Yes" : "No"); // 5 - Submitted to Supervisor
            row.add(record.isSubmittedToPayroll() ? "Yes" : "No"); // 6 - Submitted to Payroll
            row.add(hasApprovedOvertime(record) ? "With Approved Overtime" : "No Overtime"); // 7 - Overtime status
            
            converted.add(row);
        }
        
        return converted;
    }
    
    private boolean hasApprovedOvertime(AttendanceRecord record) {
        try {
            String sql = "SELECT COUNT(*) FROM overtime_requests WHERE employee_id = ? AND DATE(from_time) = ? AND status = 'Approved'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, record.getEmployeeId());
            pstmt.setDate(2, new java.sql.Date(record.getLogDate().getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public ArrayList<ArrayList<String>> getDataForPayrollTableDB() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        try {
            String currentPeriod = getCurrentPayrollPeriod();
            String month = currentPeriod.substring(0, 2);
            int year = Integer.parseInt(currentPeriod.substring(6, 10));
            List<PayrollRecord> payrolls = payrollDAO.getPayrollByMonth(month, year);

            for (PayrollRecord payroll : payrolls) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(payroll.getEmployeeId()));
                row.add(payroll.getEmployeeName());
                row.add(payroll.getPayrollPeriod());
                row.add(payroll.getPosition());
                row.add(String.format("%.2f", payroll.getGrossIncome()));
                row.add(String.format("%.2f", payroll.getBenefits()));
                row.add(String.format("%.2f", payroll.getOvertime()));
                row.add(String.format("%.2f", payroll.getUndertime()));
                row.add(String.format("%.2f", payroll.getSss()));
                row.add(String.format("%.2f", payroll.getPhilhealth()));
                row.add(String.format("%.2f", payroll.getPagibig()));
                row.add(String.format("%.2f", payroll.getTax()));
                row.add(String.format("%.2f", payroll.getNetPay()));
                row.add(payroll.getStatus());
                data.add(row);
            }

            System.out.println("Retrieved " + data.size() + " payroll records from database");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    
    public ArrayList<ArrayList<String>> getPayrollDataForDateRange(Date fromDate, Date toDate) {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String payrollPeriod = sdf.format(fromDate) + " to " + sdf.format(toDate);
        
        try {
            List<PayrollRecord> payrolls = payrollDAO.getPayrollByDateRange(fromDate, toDate);
            
            for (PayrollRecord payroll : payrolls) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(payroll.getEmployeeId()));
                row.add(payroll.getEmployeeName());
                row.add(payroll.getPayrollPeriod());
                row.add(payroll.getPosition());
                row.add(String.format("%.2f", payroll.getGrossIncome()));
                row.add(String.format("%.2f", payroll.getBenefits()));
                row.add(String.format("%.2f", payroll.getOvertime()));
                row.add(String.format("%.2f", payroll.getUndertime()));
                row.add(String.format("%.2f", payroll.getSss()));
                row.add(String.format("%.2f", payroll.getPhilhealth()));
                row.add(String.format("%.2f", payroll.getPagibig()));
                row.add(String.format("%.2f", payroll.getTax()));
                row.add(String.format("%.2f", payroll.getNetPay()));
                row.add(payroll.getStatus());
                data.add(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }

    private boolean payrollExistsForPeriod(int employeeId, String payrollPeriod) {
        try {
            String sql = "SELECT COUNT(*) FROM payroll WHERE employee_id = ? AND payroll_period = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, payrollPeriod);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getExistingPayrollId(int employeeId, String payrollPeriod) {
        try {
            String sql = "SELECT payroll_id FROM payroll WHERE employee_id = ? AND payroll_period = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, payrollPeriod);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("payroll_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void releasedPayrollDB(ArrayList<ArrayList<String>> selectedPayrolls) {
        try {
            for (ArrayList<String> payrollData : selectedPayrolls) {
                int employeeId = Integer.parseInt(payrollData.get(0));
                String payrollPeriod = payrollData.get(2);

                List<PayrollRecord> payrolls = payrollDAO.getPayrollByEmployee(employeeId);
                for (PayrollRecord payroll : payrolls) {
                    if (payroll.getPayrollPeriod().equals(payrollPeriod)) {
                        payroll.setStatus("Approved");
                        payrollDAO.updatePayroll(payroll);
                        System.out.println("✅ Released payroll for Employee ID: " + employeeId);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getEmployeeNamesDB() {
        if (newData == null) {
            newData = new ArrayList<>();
        }
        newData.clear();

        try {
            List<AccountDetails> employees = employeeDAO.findAll();
            ArrayList<String> names = new ArrayList<>();
            for (AccountDetails emp : employees) {
                names.add(emp.getLastName() + ", " + emp.getFirstName());
            }
            newData.add(names);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTableData() {
        try {
            ArrayList<ArrayList<String>> data = getDataForPayrollTable();
            setTableData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setSelectedName(String selectedName) {
        System.out.println("Selected employee: " + selectedName);
    }
    
    public ArrayList<ArrayList<String>> getDataForDTRTable() {
        return getDataAllDTR(new java.util.Date(), new java.util.Date());
    }
    
    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        return getAllApprovedPersonalLeaveLedger();
    }

    public ArrayList<ArrayList<String>> viewPersonalPayslip(Date fromDate, Date toDate, String empId) {
        ArrayList<ArrayList<String>> payslip = new ArrayList<>();
        // Implementation for personal payslip view
        return payslip;
    }

    // *** PAYROLL INTERFACE IMPLEMENTATIONS ***
    
    @Override
    public double deductionCalculation(ArrayList<ArrayList<String>> perEmployeeAttendance) {
        return sssCalculation() + philhealthCalculation() + pagibigCalculation();
    }
    
    @Override
    public double grossCalculation(ArrayList<ArrayList<String>> perEmployeeAttendance) {
        double grossPay = (this.perHour * 8) * perEmployeeAttendance.size();
        return grossPay;
    }
    
    @Override
    public double benefitsCalculation() {
        return (riceSubsidy + phoneAllowance + clothingAllowance);
    }
    
    @Override
    public double sssCalculation() {
        return this.perMonth * 0.045; // 4.5%
    }

    @Override
    public double philhealthCalculation() {
        return this.perMonth * 0.03; // 3%
    }

    @Override
    public double pagibigCalculation() {
        return Math.min(this.perMonth * 0.02, 100); // 2% max 100
    }

    @Override
    public double taxCalculation(double totalDeductions) {
        double taxableIncome = this.perMonth - totalDeductions;

        if (taxableIncome <= 20000) return 0;
        else if (taxableIncome <= 33000) return (taxableIncome - 20000) * 0.15;
        else if (taxableIncome <= 66000) return 1950 + (taxableIncome - 33000) * 0.20;
        else return 8550 + (taxableIncome - 66000) * 0.25;
    }
    
    @Override
    public double netPayrollCalculations(double gross, double benefits, double overtime, 
                                       double undertime, double totalDeductions) {
        return gross + benefits + overtime - undertime - totalDeductions;
    }
    
    @Override
    public double overtimeCalculations(ArrayList<ArrayList<String>> perEmployeeAttendance) {
        double overtimePay = 0.0;
        
        for (ArrayList<String> attendance : perEmployeeAttendance) {
            if (attendance.size() > 7 && "With Approved Overtime".equals(attendance.get(7))) {
                // Calculate overtime pay - 1.25x regular rate for first 2 hours, 1.5x for excess
                overtimePay += this.perHour * 1.25 * 2; // Simplified calculation
            }
        }
        
        return overtimePay;
    }
    
    @Override
    public double undertimeCalculations(ArrayList<ArrayList<String>> perEmployeeAttendance) {
        double undertimeDeduction = 0.0;
        
        for (ArrayList<String> attendance : perEmployeeAttendance) {
            if (attendance.size() > 4) {
                String loginTime = attendance.get(3);
                String logoutTime = attendance.get(4);
                
                // Calculate if employee worked less than 8 hours
                // This is a simplified calculation - you may need to enhance based on your business rules
                if (!loginTime.isEmpty() && !logoutTime.isEmpty()) {
                    try {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        Date login = timeFormat.parse(loginTime);
                        Date logout = timeFormat.parse(logoutTime);
                        
                        long diffInMillies = logout.getTime() - login.getTime();
                        double hoursWorked = diffInMillies / (1000.0 * 60 * 60);
                        
                        if (hoursWorked < 8) {
                            undertimeDeduction += (8 - hoursWorked) * this.perHour;
                        }
                    } catch (Exception e) {
                        System.err.println("Error calculating undertime: " + e.getMessage());
                    }
                }
            }
        }
        
        return undertimeDeduction;
    }
}
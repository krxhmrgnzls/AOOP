package com.motorph.gui;

import com.motorph.gui.Employee;
import com.motorph.dao.AttendanceDAO;
import com.motorph.dao.EmployeeDAO;
import com.motorph.dao.PayrollDAO;
import com.motorph.model.PayrollRecord;
import com.motorph.model.AccountDetails;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import com.motorph.model.AttendanceRecord;
import com.motorph.dao.AttendanceResult;
import com.motorph.service.AttendanceService;
import com.motorph.util.DatabaseConnection;
import com.motorph.util.Payroll;


public class PayrollStaff extends Employee implements Payroll {
    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;
    private AttendanceDAO attendanceDAO;
    public AccountDetails accountDetails;
    private ArrayList<ArrayList<String>> tableData;
    private ArrayList<ArrayList<String>> newData;
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
        this.accountDetails = new AccountDetails();
        this.tableData = new ArrayList<>();
        this.newData = new ArrayList<>();
        
    }
    
    private void initializeConnection() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.connection = dbConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public AccountDetails getAccountDetails() {
        return this.accountDetails;
    }
    
   public void viewPersonalDetails(String employeeId) {
    String sql = "SELECT employee_id, last_name, first_name, birthday, address, phone_number, " +
                "sss_number, philhealth_number, tin_number, pagibig_number, status, position, " +
                "supervisor_id, basic_salary, rice_subsidy, phone_allowance, clothing_allowance, " +
                "gross_rate, hourly_rate " +
                "FROM employee_profile_view " +
                "WHERE employee_id = ?";
    
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

            int supervisorId = rs.getInt("supervisor_id");
            if (supervisorId > 0) {
                String supervisorSql = "SELECT CONCAT(first_name, ' ', last_name) as supervisor_name " +
                                     "FROM employee_profile_view WHERE employee_id = ?";
                try (PreparedStatement supervisorStmt = connection.prepareStatement(supervisorSql)) {
                    supervisorStmt.setInt(1, supervisorId);
                    ResultSet supervisorRs = supervisorStmt.executeQuery();
                    if (supervisorRs.next()) {
                        accountDetails.setSupervisor(supervisorRs.getString("supervisor_name"));
                    }
                }
            }
            
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
 
    String sql = "SELECT e.employee_id FROM employee_profile_view e " +
                "WHERE CONCAT(e.first_name, ' ', e.last_name) = ? " +
                "OR CONCAT(e.last_name, ', ', e.first_name) = ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employeeName);
            pstmt.setString(2, employeeName);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int employeeId = rs.getInt("employee_id");

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
                    "JOIN employee_profile_view e ON a.employee_id = e.employee_id " +
                    "WHERE a.employee_id = ? AND a.log_date BETWEEN ? AND ? " +
                    "ORDER BY a.log_date DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setDate(2, new java.sql.Date(fromDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(toDate.getTime()));

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(employeeId)); 
                row.add(rs.getString("first_name") + " " + rs.getString("last_name")); 
                row.add(rs.getDate("log_date").toString()); 
                row.add(rs.getTime("login_time") != null ? rs.getTime("login_time").toString() : ""); 
                row.add(rs.getTime("logout_time") != null ? rs.getTime("logout_time").toString() : ""); 
                row.add(rs.getString("submitted")); 
                row.add(rs.getString("remarks")); 
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
                    "FROM employee_profile_view " +
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
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, 15);
            endDate = cal.getTime();
        } else {
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
                        System.out.println("Released payroll for Employee ID: " + employeeId);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        System.out.println("Starting payroll computation...");
        
        try {
            if (fromDate == null || toDate == null) {
                System.err.println("Invalid date range: fromDate or toDate is null");
                return false;
            }
            
            if (fromDate.after(toDate)) {
                System.err.println("Invalid date range: fromDate is after toDate");
                return false;
            }
            
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not available");
                initializeConnection();
                
                if (connection == null || connection.isClosed()) {
                    throw new SQLException("Could not establish database connection");
                }
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String payrollPeriod = sdf.format(fromDate) + " to " + sdf.format(toDate);
            
            System.out.println("Computing payroll for period: " + payrollPeriod);

            List<AccountDetails> employees = employeeDAO.findAll();
            
            if (employees == null || employees.isEmpty()) {
                System.err.println("No employees found in database");
                javax.swing.JOptionPane.showMessageDialog(null,
                    "No employees found in database.\nPlease ensure employee records exist before computing payroll.",
                    "No Employees Found",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            System.out.println("Found " + employees.size() + " employees to process");
            
            int processedCount = 0;
            int skippedCount = 0;
            int errorCount = 0;

            // Set the date range
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            fromDate = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            toDate = cal.getTime();

            payrollPeriod = sdf.format(fromDate) + " to " + sdf.format(toDate);
            
            for (AccountDetails employee : employees) {
                try {
                    if (employee == null || employee.getEmployeeID() <= 0) {
                        skippedCount++;
                        System.out.println("Skipping invalid employee record");
                        continue;
                    }
                    
                    System.out.println("Processing employee: " + employee.getEmployeeID() + " - " + 
                                     employee.getFirstName() + " " + employee.getLastName());
                    
                    AttendanceDAO attendanceDAO = new AttendanceDAO();
                    PayrollDAO payrollDAO = new PayrollDAO();
                    List<AttendanceRecord> attendanceRecords = new ArrayList<>();

                    try {
                        java.sql.Date sqlFromDate = new java.sql.Date(fromDate.getTime());
                        java.sql.Date sqlToDate = new java.sql.Date(toDate.getTime());

                        attendanceRecords = attendanceDAO.getAttendanceByEmployee(employee.getEmployeeID(), sqlFromDate, sqlToDate);
                        
                        if (attendanceRecords == null) {
                            attendanceRecords = new ArrayList<>();
                        }
                        
                        System.out.println("Found " + attendanceRecords.size() + " attendance records");
                        
                    } catch (Exception e) {
                        System.err.println("Could not get attendance for employee " + employee.getEmployeeID() + ": " + e.getMessage());
                        attendanceRecords = new ArrayList<>();
                    }
 
                    PayrollRecord payroll = calculatePayrollForEmployee(employee, attendanceRecords, payrollPeriod);
                    
                    if (payroll != null) {
                        if (payrollExistsForPeriod(employee.getEmployeeID(), payrollPeriod)) {
                            int existingPayrollId = getExistingPayrollId(employee.getEmployeeID(), payrollPeriod);
                            payroll.setPayrollId(existingPayrollId);
                            payrollDAO.updatePayroll(payroll);
                            System.out.println("Updated existing payroll");
                        } else {
                            payrollDAO.savePayroll(payroll);
                            System.out.println("Created new payroll record");
                        }
                        processedCount++;
                    } else {
                        errorCount++;
                        System.err.println("Failed to calculate payroll for Employee ID: " + employee.getEmployeeID());
                    }
                    
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("Error processing employee " + 
                                     (employee != null ? employee.getEmployeeID() : "null") + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("Payroll computation completed:");
            System.out.println("   Processed: " + processedCount);
            System.out.println("   Skipped: " + skippedCount);
            System.out.println("   Errors: " + errorCount);
            
            boolean success = processedCount > 0;
            
            if (success) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Payroll computation completed successfully!\n\n" +
                    "Processed: " + processedCount + " employees\n" +
                    "Skipped: " + skippedCount + " employees\n" +
                    "Errors: " + errorCount + " employees",
                    "Payroll Computation Complete",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Payroll computation failed!\n\n" +
                    "No employees were successfully processed.\n" +
                    "Please check the database connection and employee records.",
                    "Payroll Computation Failed",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Database error during payroll computation: " + e.getMessage());
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(null,
                "Database connection error during payroll computation:\n\n" + 
                e.getMessage() + 
                "\n\nPlease check your database connection and try again.",
                "Database Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error during payroll computation: " + e.getMessage());
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(null,
                "Unexpected error during payroll computation:\n\n" + 
                e.getMessage() + 
                "\n\nPlease try again or contact system administrator.",
                "System Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            
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
        
        try {
            if (payrollDAO == null) {
                System.err.println("WARNING: PayrollDAO is not initialized");
                return data; 
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String fromDateStr = sdf.format(fromDate);
            String toDateStr = sdf.format(toDate);
            
            System.out.println("Searching for payroll data from " + fromDateStr + " to " + toDateStr);
            
            List<PayrollRecord> payrolls = payrollDAO.getPayrollByDateRange(fromDate, toDate);
            
            if (payrolls == null) {
                System.out.println("PayrollDAO returned null - using empty list");
                payrolls = new ArrayList<>();
            }
            
            if (payrolls.isEmpty()) {
                System.out.println("ℹ️ No payroll records found for the selected date range");
                System.out.println("   This is normal if payroll hasn't been computed for this period yet");
                return data; 
            }
            
            // Convert PayrollRecord objects to ArrayList format
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
            
            System.out.println("Successfully retrieved " + data.size() + " payroll records");
            
        } catch (Exception e) {
            System.err.println("Error getting payroll data for date range: " + e.getMessage());
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
                        System.out.println("Released payroll for Employee ID: " + employeeId);
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
        return payslip;
    }

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
        return this.perMonth * 0.045;
    }

    @Override
    public double philhealthCalculation() {
        return this.perMonth * 0.03; 
    }

    @Override
    public double pagibigCalculation() {
        return Math.min(this.perMonth * 0.02, 100); 
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
                overtimePay += this.perHour * 1.25 * 2; 
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
    
    public void clearPayrollTable(javax.swing.JTable table) {
        try {
            if (table != null) {
                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
                model.setRowCount(0);
            }
        } catch (Exception e) {
            System.err.println("Error clearing payroll table: " + e.getMessage());
        }
    }

    public void updatePayrollPeriodLabel(javax.swing.JLabel label, String customMessage) {
        try {
            if (label != null) {
                label.setText(customMessage);
            }
        } catch (Exception e) {
            System.err.println("Error updating payroll period label: " + e.getMessage());
        }
    }
    
    public void updatePayrollPeriodLabel(javax.swing.JLabel label, Date fromDate, Date toDate, javax.swing.JTable table) {
        try {
            if (fromDate != null && toDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
                String dateRange = sdf.format(fromDate) + "-" + 
                                 sdf.format(toDate) + ", 2025";

                int recordCount = 0;
                if (table != null) {
                    recordCount = table.getRowCount();
                }
                
                String message = "Payroll from " + dateRange;
                if (recordCount > 0) {
                    message += " (" + recordCount + " records)";
                }
                
                updatePayrollPeriodLabel(label, message);
            }
        } catch (Exception e) {
            System.err.println("Error updating payroll period label: " + e.getMessage());
        }
    }
    
    public void showExistingPayrollData(Date fromDate, Date toDate, javax.swing.JTable table, 
                                       javax.swing.JLabel periodLabel, javax.swing.JButton releasedButton,
                                       javax.swing.JFrame parentFrame) {
        try {
            System.out.println("Loading existing payroll data...");
            
            releasedButton.setEnabled(false);
            
            ArrayList<ArrayList<String>> existingData = getPayrollDataForDateRange(fromDate, toDate);
            
            setTableData(existingData);
            setTableSize(14);
            displayDataTable(table);
            
            if (existingData.isEmpty()) {
                updatePayrollPeriodLabel(periodLabel, "No existing payroll records found");
                
                javax.swing.JOptionPane.showMessageDialog(parentFrame,
                    "No payroll records found for the selected date range.\n\n" +
                    "This is normal if payroll hasn't been computed for this period yet.\n" +
                    "Click 'Generate Report' to compute payroll for this period.",
                    "No Payroll Records",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                updatePayrollPeriodLabel(periodLabel, fromDate, toDate, table);
                releasedButton.setEnabled(true);
            }
            
        } catch (Exception e) {
            System.err.println("Error showing existing payroll data: " + e.getMessage());
            e.printStackTrace();
            
            updatePayrollPeriodLabel(periodLabel, "Error loading data");
            
            javax.swing.JOptionPane.showMessageDialog(parentFrame,
                "Error loading existing payroll data:\n" + e.getMessage(),
                "Loading Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean validatePayrollDates(Date fromDate, Date toDate, javax.swing.JFrame parentFrame) {
        if (fromDate == null || toDate == null) {
            javax.swing.JOptionPane.showMessageDialog(parentFrame, 
                "Please select both From and To dates to compute payroll.", 
                "Date Selection Required", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (fromDate.after(toDate)) {
            javax.swing.JOptionPane.showMessageDialog(parentFrame, 
                "The 'From' date cannot be later than the 'To' date.\nPlease correct the date range.", 
                "Invalid Date Range", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }

        long daysDiff = (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24);
        if (daysDiff > 365) {
            int choice = javax.swing.JOptionPane.showConfirmDialog(parentFrame,
                "You've selected a date range of " + daysDiff + " days.\n" +
                "This might take a long time to compute.\n\n" +
                "Do you want to continue?",
                "Large Date Range Warning",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);
            
            return choice == javax.swing.JOptionPane.YES_OPTION;
        }

        return true;
    }

    public void handleDateRangeChange(Date fromDate, Date toDate, javax.swing.JTable table, 
                                     javax.swing.JLabel periodLabel, javax.swing.JButton releasedButton) {
        try {
            if (fromDate == null || toDate == null) {
                System.out.println("ℹ️ Date range incomplete - clearing table");
                clearPayrollTable(table);
                updatePayrollPeriodLabel(periodLabel, "Please select both dates");
                releasedButton.setEnabled(false);
                return;
            }
            
            if (fromDate.after(toDate)) {
                System.out.println("Invalid date range - From date is after To date");
                clearPayrollTable(table);
                updatePayrollPeriodLabel(periodLabel, "Invalid date range");
                releasedButton.setEnabled(false);
                return;
            }

            ArrayList<ArrayList<String>> existingData = getPayrollDataForDateRange(fromDate, toDate);

            setTableData(existingData);
            setTableSize(14);
            displayDataTable(table);

            if (existingData.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
                String message = "No payroll records found for " + 
                                sdf.format(fromDate) + " to " + 
                                sdf.format(toDate);
                updatePayrollPeriodLabel(periodLabel, message);
                releasedButton.setEnabled(false);
                
                System.out.println("No existing payroll data - user can compute new payroll");
            } else {
                updatePayrollPeriodLabel(periodLabel, fromDate, toDate, table);
                releasedButton.setEnabled(true);
                
                System.out.println("Loaded " + existingData.size() + " existing payroll records");
            }
            
        } catch (Exception e) {
            System.err.println("Error in handleDateRangeChange: " + e.getMessage());
            e.printStackTrace();

            clearPayrollTable(table);
            updatePayrollPeriodLabel(periodLabel, "Error loading data");
            releasedButton.setEnabled(false);
        }
    }
}
package payrollsystem;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;

public class Supervisor extends Employee {
    private List<AccountDetails> teamMembers;
    private EmployeeDAO employeeDAO;
    private AttendanceDAO attendanceDAO;
    private LeaveDAO leaveDAO;
    private String selectedEmployeeName = "";
    
    public Supervisor() {
        super();
        this.teamMembers = new ArrayList<>();
        this.employeeDAO = new EmployeeDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.leaveDAO = new LeaveDAO();
    }
    
    // Constructor that GUI expects
    public Supervisor(String employeeId) {
        super(employeeId);  // Call Employee constructor with string
        this.teamMembers = new ArrayList<>();
        this.employeeDAO = new EmployeeDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.leaveDAO = new LeaveDAO();
    }
    
    public Supervisor(ArrayList<ArrayList<String>> userDetails) {
        super();
        this.teamMembers = new ArrayList<>();
        this.employeeDAO = new EmployeeDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.leaveDAO = new LeaveDAO();
        
        if (userDetails != null && !userDetails.isEmpty()) {
            ArrayList<String> details = userDetails.get(0);
            if (details.size() >= 3) {
                this.accountDetails.setEmployeeID(Integer.parseInt(details.get(0)));
                this.accountDetails.setFirstName(details.get(1));
                this.accountDetails.setLastName(details.get(2));
            }
        }
    }

    public List<AccountDetails> getTeamMembers() {
        return teamMembers;
    }
    
    public void setTeamMembers(List<AccountDetails> teamMembers) {
        this.teamMembers = teamMembers;
    }
    
    // ===== SUPERVISOR-SPECIFIC METHODS =====
    
    // Approve work schedule/attendance
    public void approveWorkSchedule() {
        try {
            List<AttendanceRecord> pendingAttendance = attendanceDAO.getAttendanceForPayroll("current");
            
            for (AttendanceRecord attendance : pendingAttendance) {
                if (!attendance.isSubmittedToSupervisor()) {
                    attendanceDAO.submitToSupervisor(attendance.getAttendanceId());
                }
            }
            
            System.out.println("Work schedules approved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<ArrayList<String>> getPendingLeaveRequests() {
        try {
            return leaveDAO.getPendingLeaveRequests();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // Generate team report
    public void generateTeamReport() {
        try {
            List<AccountDetails> team = getTeamData();
            System.out.println("Team Report:");
            System.out.println("Team Size: " + team.size());
            
            for (AccountDetails member : team) {
                System.out.println("- " + member.getFirstName() + " " + member.getLastName() + 
                                 " (" + member.getPosition() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Get team data
    public List<AccountDetails> getTeamData() {
        try {
            return employeeDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // Get team attendance summary
    public void getTeamAttendanceSummary() {
        try {
            List<AccountDetails> team = getTeamData();
            for (AccountDetails member : team) {
                java.util.Date startDate = new java.util.Date();
                java.util.Date endDate = new java.util.Date();
                
                java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
                java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
                
                AttendanceSummary summary = attendanceDAO.getAttendanceSummary(
                    member.getEmployeeID(), sqlStartDate, sqlEndDate);
                
                System.out.println(member.getFirstName() + " " + member.getLastName() + ": " + summary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Bulk approve attendance
    public boolean bulkApproveAttendance(List<Integer> attendanceIds) {
        try {
            return attendanceDAO.bulkSubmitToPayroll(attendanceIds);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ===== CLEAN OOP DTR METHODS =====
    
    // All-in-one method to load employee dropdown
    public void loadEmployeeDropdown(javax.swing.JComboBox<String> comboBox) {
    try {
        System.out.println("=== Loading Employee Dropdown ===");
        
        // Get employee names using existing method
        getEmployeeNames(); 
        
        // Get the loaded employee names
        ArrayList<ArrayList<String>> employeeData = getNewData();
        
        if (employeeData.isEmpty()) {
            System.out.println("⚠️ No employee data found");
            return;
        }
        
        // Add employees to combo box
        for (ArrayList<String> row : employeeData) {
            for (String employeeName : row) {
                if (employeeName != null && !employeeName.trim().isEmpty()) {
                    comboBox.addItem(employeeName.trim());
                }
            }
        }
        
        System.out.println("✅ Loaded " + (comboBox.getItemCount() - 1) + " employees into dropdown");
        
    } catch (Exception e) {
        System.err.println("❌ Error loading employee dropdown: " + e.getMessage());
        e.printStackTrace();
    }
}
    // All-in-one method to handle employee selection and load DTR
    public void handleEmployeeSelection(String employeeName, javax.swing.JTable table) {
    if (employeeName == null || employeeName.trim().isEmpty()) {
        clearTable(table);
        return;
    }
    
    setSelectedEmployeeName(employeeName);
    
    // Get current pay period dates
    Calendar[] period = getCurrentPayPeriod();
    
    // Load and display DTR
    ArrayList<ArrayList<String>> dtrData = getEmployeeDTR(employeeName, period[0].getTime(), period[1].getTime());
    setTableData(dtrData);
    setTableSize(8);
    displayDataTable(table);
    
    System.out.println("✅ Handled employee selection for " + employeeName + " - loaded " + dtrData.size() + " records");
}
    // All-in-one method to handle DTR forwarding
    public boolean handleDTRForwarding(javax.swing.JTable table, int[] selectedRows) {
        if (selectedRows.length == 0) {
            return false; // Let GUI show error message
        }
        
        // Validate selection
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        ArrayList<ArrayList<String>> selectedData = new ArrayList<>();
        
        for (int row : selectedRows) {
            String toPayrollStatus = model.getValueAt(row, 6).toString();
            if (toPayrollStatus.equals("Yes")) {
                return false; // Already forwarded
            }
            
            ArrayList<String> rowData = new ArrayList<>();
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object value = model.getValueAt(row, col);
                rowData.add(value != null ? value.toString() : "");
            }
            selectedData.add(rowData);
        }
        
        // Forward to payroll
        return forwardDTRToPayroll(selectedData);
    }
    
    // Utility method to get current pay period
    private Calendar[] getCurrentPayPeriod() {
    Calendar today = Calendar.getInstance();
    int currentDay = today.get(Calendar.DAY_OF_MONTH);
    
    Calendar start = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    
    if (currentDay <= 15) {
        start.set(Calendar.DAY_OF_MONTH, 1);
        end.set(Calendar.DAY_OF_MONTH, 15);
    } else {
        start.set(Calendar.DAY_OF_MONTH, 16);
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
    }
    
    return new Calendar[]{start, end};
}
    
    // Utility method to clear table
    private void clearTable(javax.swing.JTable table) {
      setTableData(new ArrayList<>());
      displayDataTable(table);
      System.out.println("🧹 Table cleared");
  }
    // ===== SUPERVISOR REQUEST APPROVAL METHODS =====
    
    public ArrayList<ArrayList<String>> getAllRequestData(String requestType) {
        ArrayList<ArrayList<String>> requests = new ArrayList<>();
        
        String sql = "";
        if (requestType.equals("All Request")) {
            sql = "SELECT lr.leave_id as id, lr.date_filed, lr.leave_type as type_request, lr.from_date, lr.to_date, " +
                  "lr.number_of_days, lr.reason, lr.status, e.employee_id, CONCAT(e.first_name, ' ', e.last_name) as employee_name " +
                  "FROM leave_requests lr " +
                  "JOIN employees e ON lr.employee_id = e.employee_id " +
                  "WHERE lr.status = 'Pending' " +
                  "UNION ALL " +
                  "SELECT or_req.overtime_id as id, or_req.date_filed, or_req.type_request, DATE(or_req.from_time), DATE(or_req.to_time), " +
                  "or_req.number_of_days, or_req.reason, or_req.status, e.employee_id, CONCAT(e.first_name, ' ', e.last_name) as employee_name " +
                  "FROM overtime_requests or_req " +
                  "JOIN employees e ON or_req.employee_id = e.employee_id " +
                  "WHERE or_req.status = 'Pending' " +
                  "ORDER BY date_filed DESC";
        } else if (requestType.equals("Leave Request")) {
            sql = "SELECT lr.leave_id as id, lr.date_filed, lr.leave_type as type_request, lr.from_date, lr.to_date, " +
                  "lr.number_of_days, lr.reason, lr.status, e.employee_id, CONCAT(e.first_name, ' ', e.last_name) as employee_name " +
                  "FROM leave_requests lr " +
                  "JOIN employees e ON lr.employee_id = e.employee_id " +
                  "WHERE lr.status = 'Pending' " +
                  "ORDER BY lr.date_filed DESC";
        } else if (requestType.equals("Overtime Request")) {
            sql = "SELECT or_req.overtime_id as id, or_req.date_filed, or_req.type_request, DATE(or_req.from_time), DATE(or_req.to_time), " +
                  "or_req.number_of_days, or_req.reason, or_req.status, e.employee_id, CONCAT(e.first_name, ' ', e.last_name) as employee_name " +
                  "FROM overtime_requests or_req " +
                  "JOIN employees e ON or_req.employee_id = e.employee_id " +
                  "WHERE or_req.status = 'Pending' " +
                  "ORDER BY or_req.date_filed DESC";
        }
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            int displayId = 1; // Clean sequential counter starting from 1
            
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                
                // Store the real database ID
                int realId = rs.getInt("id");
                
                // Build the row data in the correct order
                row.add(String.valueOf(displayId));                       // 0: DISPLAY ID (1, 2, 3, 4...)
                row.add(rs.getString("employee_name"));                   // 1: NAME
                row.add(rs.getDate("date_filed").toString());             // 2: DATE FILED
                row.add(rs.getString("type_request"));                    // 3: TYPE OF REQUEST
                row.add(rs.getDate(4).toString());                        // 4: PERIOD FROM
                row.add(rs.getDate(5).toString());                        // 5: PERIOD TO
                row.add(String.valueOf(rs.getDouble("number_of_days")));  // 6: NUMBER OF DAYS
                row.add(rs.getString("reason"));                          // 7: REASON
                row.add(rs.getString("status"));                          // 8: STATUS
                row.add(String.valueOf(realId));                          // 9: HIDDEN REAL ID (for operations)
                
                requests.add(row);
                displayId++; // Increment for next row
            }
            
            System.out.println("Found " + requests.size() + " requests with clean display IDs");
            
        } catch (SQLException e) {
            System.err.println("Error getting supervisor request data: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Get pending requests only (for display in supervisor table)
     */
    public ArrayList<ArrayList<String>> getPendingRequestsOnly() {
        ArrayList<ArrayList<String>> requests = new ArrayList<>();

        try {
            String sql = "SELECT lr.leave_id as id, CONCAT(e.first_name, ' ', e.last_name) as name, " +
                         "lr.date_filed, lr.leave_type as type_request, lr.from_date, lr.to_date, " +
                         "lr.number_of_days, lr.reason, lr.status " +
                         "FROM leave_requests lr " +
                         "JOIN employees e ON lr.employee_id = e.employee_id " +
                         "WHERE lr.status = 'Pending' " +
                         "UNION ALL " +
                         "SELECT or_req.overtime_id as id, CONCAT(e.first_name, ' ', e.last_name) as name, " +
                         "or_req.date_filed, or_req.type_request, DATE(or_req.from_time), DATE(or_req.to_time), " +
                         "or_req.number_of_days, or_req.reason, or_req.status " +
                         "FROM overtime_requests or_req " +
                         "JOIN employees e ON or_req.employee_id = e.employee_id " +
                         "WHERE or_req.status = 'Pending' " +
                         "ORDER BY date_filed DESC";

            try (PreparedStatement pstmt = connection.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                int displayId = 1; // Clean sequential counter starting from 1

                while (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();

                    // Store the real database ID for operations
                    int realId = rs.getInt("id");

                    row.add(String.valueOf(displayId));                       // DISPLAY ID (1, 2, 3, 4...)
                    row.add(rs.getString("name"));                            // Employee Name
                    row.add(rs.getDate("date_filed").toString());             // Date Filed
                    row.add(rs.getString("type_request"));                    // Type of Request
                    row.add(rs.getDate(5).toString());                        // Period From
                    row.add(rs.getDate(6).toString());                        // Period To
                    row.add(String.valueOf(rs.getDouble("number_of_days")));  // Number of Days
                    row.add(rs.getString("reason"));                          // Reason
                    row.add(rs.getString("status"));                          // Status
                    row.add(String.valueOf(realId));                          // HIDDEN REAL ID (for operations)

                    requests.add(row);
                    displayId++; // Increment for next row
                }
            }

            System.out.println("Found " + requests.size() + " pending requests with clean display IDs");

        } catch (SQLException e) {
            System.err.println("Error getting pending requests: " + e.getMessage());
            e.printStackTrace();
        }

        return requests;
    }
    
    public int getRealIdFromDisplayRow(javax.swing.JTable table, int selectedRow) {
        try {
            // Check if the table has enough columns
            if (table.getColumnCount() < 10) {
                System.err.println("❌ Table doesn't have enough columns. Expected 10, got " + table.getColumnCount());
                return -1;
            }
            
            // Check if the selected row is valid
            if (selectedRow < 0 || selectedRow >= table.getRowCount()) {
                System.err.println("❌ Invalid row selection: " + selectedRow);
                return -1;
            }
            
            // The real ID is stored in column 9 (last column)
            Object realIdValue = table.getValueAt(selectedRow, 9);
            
            if (realIdValue == null) {
                System.err.println("❌ Real ID value is null for row " + selectedRow);
                return -1;
            }
            
            String realIdStr = realIdValue.toString().trim();
            if (realIdStr.isEmpty()) {
                System.err.println("❌ Real ID value is empty for row " + selectedRow);
                return -1;
            }
            
            int realId = Integer.parseInt(realIdStr);
            System.out.println("✅ Retrieved Real ID: " + realId + " for display row " + selectedRow);
            return realId;
            
        } catch (NumberFormatException e) {
            System.err.println("❌ Error parsing Real ID from row " + selectedRow + ": " + e.getMessage());
            return -1;
        } catch (Exception e) {
            System.err.println("❌ Unexpected error getting real ID from row " + selectedRow + ": " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    
    // ===== APPROVAL METHODS =====
    
    /**
     * Approve or disapprove a leave request
     */
    public boolean approveLeaveRequest(int leaveId, boolean approve) {
        try {
            System.out.println("=== Processing Leave Request ===");
            System.out.println("Leave ID: " + leaveId);
            System.out.println("Action: " + (approve ? "APPROVE" : "DISAPPROVE"));
            
            String status = approve ? "Approved" : "Rejected";
            String sql = "UPDATE leave_requests SET status = ? WHERE leave_id = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setInt(2, leaveId);
                
                int rowsUpdated = pstmt.executeUpdate();
                
                if (rowsUpdated > 0) {
                    System.out.println("✅ Leave request " + leaveId + " " + status.toLowerCase());
                    return true;
                } else {
                    System.out.println("❌ No leave request found with ID: " + leaveId);
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating leave request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getBalanceVL() {
    try {
        // CORRECT: Use leave_balances table with vacation_leave column
        String sql = "SELECT vacation_leave FROM leave_balances WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, this.employeeID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double vlBalance = rs.getDouble("vacation_leave");
                System.out.println("✅ VL Balance for employee " + this.employeeID + ": " + vlBalance);
                return String.valueOf(vlBalance);
            } else {
                System.err.println("⚠️ No leave balance record found for employee ID: " + this.employeeID);
                // Try to create default balance
                createDefaultLeaveBalance();
                return "24.0"; // Default balance
            }
        }
        
    } catch (SQLException e) {
        System.err.println("❌ Error getting VL balance: " + e.getMessage());
        e.printStackTrace();
        return "0.0";
    }
}

public String getBalanceSL() {
    try {
        // CORRECT: Use leave_balances table with sick_leave column
        String sql = "SELECT sick_leave FROM leave_balances WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, this.employeeID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double slBalance = rs.getDouble("sick_leave");
                System.out.println("✅ SL Balance for employee " + this.employeeID + ": " + slBalance);
                return String.valueOf(slBalance);
            } else {
                System.err.println("⚠️ No leave balance record found for employee ID: " + this.employeeID);
                // Try to create default balance
                createDefaultLeaveBalance();
                return "24.0"; // Default balance
            }
        }
        
    } catch (SQLException e) {
        System.err.println("❌ Error getting SL balance: " + e.getMessage());
        e.printStackTrace();
        return "0.0";
    }
}

// 2. REPLACE in Employee.java
public void leaveBalancesInformation() {
    try {
        System.out.println("=== Loading leave balance information for employee: " + this.employeeID + " ===");
        
        String sql = "SELECT vacation_leave, sick_leave FROM leave_balances WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, this.employeeID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double vlBalance = rs.getDouble("vacation_leave");
                double slBalance = rs.getDouble("sick_leave");
                
                System.out.println("✅ Leave balances loaded:");
                System.out.println("   - Vacation Leave: " + vlBalance);
                System.out.println("   - Sick Leave: " + slBalance);
            } else {
                System.err.println("⚠️ No leave balance record found. Creating default balance...");
                createDefaultLeaveBalance();
            }
        }
        
    } catch (SQLException e) {
        System.err.println("❌ Error loading leave balances information: " + e.getMessage());
        e.printStackTrace();
    }
}
    /**
     * Approve or disapprove an overtime request
     */
    public boolean approveOvertimeRequest(int overtimeId, boolean approve) {
        try {
            System.out.println("=== Processing Overtime Request ===");
            System.out.println("Overtime ID: " + overtimeId);
            System.out.println("Action: " + (approve ? "APPROVE" : "DISAPPROVE"));
            
            String status = approve ? "Approved" : "Rejected";
            String sql = "UPDATE overtime_requests SET status = ? WHERE overtime_id = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setInt(2, overtimeId);
                
                int rowsUpdated = pstmt.executeUpdate();
                
                if (rowsUpdated > 0) {
                    System.out.println("✅ Overtime request " + overtimeId + " " + status.toLowerCase());
                    return true;
                } else {
                    System.out.println("❌ No overtime request found with ID: " + overtimeId);
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating overtime request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generic approval method (tries both leave and overtime)
     */
    public boolean approveRequest(int requestId, boolean approve) {
        // Try leave request first
        if (approveLeaveRequest(requestId, approve)) {
            return true;
        }

        // Try overtime request
        if (approveOvertimeRequest(requestId, approve)) {
            return true;
        }

        System.out.println("❌ Request ID " + requestId + " not found in any table");
        return false;
    }
    
    // ===== INHERITED METHOD OVERRIDES =====
    
    @Override
    public ArrayList<ArrayList<String>> getDataAllRequests() {
        try {
            // For supervisor, show their own requests
            
            return super.loadAllRequestsForEmployee();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public ArrayList<ArrayList<String>> getDataAllDTR(java.util.Date startDate, java.util.Date endDate) {
        // Return supervisor's own DTR data
        return super.getDataAllDTR(startDate, endDate);
    }

    @Override
    public void updateLeaveBalanceLabels(javax.swing.JLabel label1, javax.swing.JLabel label2) {
        label1.setText(getBalanceVL());
        label2.setText(getBalanceSL());
    }
    
    @Override
    public ArrayList<ArrayList<String>> viewPersonalPayslip(java.util.Date startDate, java.util.Date endDate, String employeeId) {
        return super.viewPersonalPayslip(startDate, endDate, employeeId);
    }
    
    @Override
    public boolean countNumberOfDays(java.util.Date startDate, java.util.Date endDate) {
        return super.countNumberOfDays(startDate, endDate);
    }
    
    public boolean isValidDateRange(java.util.Date startDate, java.util.Date endDate) {
        return super.isValidDateRange(startDate, endDate);
    }
    
    // ===== EMPLOYEE NAME MANAGEMENT =====
    
    public void getEmployeeNames() {
        try {
            newData.clear();
            idAndNames.clear();
            ArrayList<String> employeeNames = new ArrayList<>();

            String sql = "SELECT employee_id, CONCAT(first_name, ' ', last_name) as full_name " +
                         "FROM employees " +
                         "WHERE employee_id != ? " + // Exclude current supervisor
                         "ORDER BY first_name, last_name";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, this.employeeID); // Exclude current supervisor

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    ArrayList<String> idName = new ArrayList<>();
                    idName.add(String.valueOf(rs.getInt("employee_id")));
                    idName.add(rs.getString("full_name"));
                    idAndNames.add(idName);
                    employeeNames.add(rs.getString("full_name"));
                }

                newData.add(employeeNames);
                System.out.println("✅ Loaded " + employeeNames.size() + " employee names");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error getting employee names: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<String>> getNewData() {
        if (newData == null) {
            newData = new ArrayList<>();
        }
        return newData;
    }

    public void setData() {
        // Initialize or reset data
        if (newData == null) {
            newData = new ArrayList<>();
        }
        System.out.println("Data set/reset for supervisor");
    }
    
    private java.sql.Date convertStringToSqlDate(String dateStr) {
    try {
        // Handle different date formats
        if (dateStr.contains("-") && dateStr.length() == 10) {
            // Already in SQL format (yyyy-MM-dd)
            return java.sql.Date.valueOf(dateStr);
        } else if (dateStr.contains("/")) {
            // Convert from MM/dd/yyyy to yyyy-MM-dd
            String[] parts = dateStr.split("/");
            if (parts.length == 3) {
                String month, day, year;
                
                // Assume MM/dd/yyyy format
                month = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
                day = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
                year = parts[2];
                
                String sqlDateStr = year + "-" + month + "-" + day;
                return java.sql.Date.valueOf(sqlDateStr);
            }
        } else if (dateStr.contains("-") && dateStr.length() != 10) {
            // Handle other date formats like yyyy-M-d
            String[] parts = dateStr.split("-");
            if (parts.length == 3) {
                String year = parts[0];
                String month = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
                String day = parts[2].length() == 1 ? "0" + parts[2] : parts[2];
                
                String sqlDateStr = year + "-" + month + "-" + day;
                return java.sql.Date.valueOf(sqlDateStr);
            }
        }
        
        // Try to parse as-is if all else fails
        return java.sql.Date.valueOf(dateStr);
        
    } catch (Exception e) {
        System.err.println("❌ Error converting date string '" + dateStr + "': " + e.getMessage());
        // Return current date as fallback
        return new java.sql.Date(System.currentTimeMillis());
    }
}

    @Override
    public ArrayList<ArrayList<String>> getAllApprovedPersonalLeaveLedger() {
        // Return supervisor's own approved leave ledger
        return super.getAllApprovedPersonalLeaveLedger();
    }
    
    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        return getAllApprovedPersonalLeaveLedger();
    }

    // ===== DTR FORWARDING METHODS =====
    
    public void forwardDTR(ArrayList<ArrayList<String>> dtrData) {
        System.out.println("DTR forwarded by supervisor to payroll");
        // In a real implementation, update DTR status to forwarded to payroll
    }

    public void forwardDTRToSupervisor(ArrayList<ArrayList<String>> dtrData) {
    try {
        System.out.println("=== Supervisor submitting DTR to higher authority ===");
        
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
                        
                        // Use the helper method to convert date
                        java.sql.Date sqlDate = convertStringToSqlDate(dateStr);
                        
                        pstmt.setDate(1, sqlDate);
                        pstmt.setInt(2, this.employeeID); // Supervisor's own employee ID
                        
                        int result = pstmt.executeUpdate();
                        if (result > 0) {
                            successCount++;
                            System.out.println("✅ Submitted DTR for date: " + dateStr);
                        }
                        
                    } catch (Exception e) {
                        System.err.println("❌ Error processing DTR row: " + e.getMessage());
                    }
                }
            }
        }
        
        System.out.println("✅ Successfully submitted " + successCount + " DTR entries");
        
    } catch (SQLException e) {
        System.err.println("❌ Error submitting DTR: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    // ===== EMPLOYEE DTR MANAGEMENT =====
    
    public void setSelectedName(String selectedName) {
        System.out.println("Selected employee: " + selectedName);
        // Store selected employee for DTR viewing
    }
    
    public ArrayList<ArrayList<String>> getDataForDTRTable() {
    if (selectedEmployeeName == null || selectedEmployeeName.trim().isEmpty()) {
        System.err.println("⚠️ No employee selected for DTR");
        return new ArrayList<>();
    }

    // Get current pay period
    Calendar today = Calendar.getInstance();
    int currentDay = today.get(Calendar.DAY_OF_MONTH);
    
    Calendar startCal = Calendar.getInstance();
    Calendar endCal = Calendar.getInstance();
    
    if (currentDay <= 15) {
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        endCal.set(Calendar.DAY_OF_MONTH, 15);
    } else {
        startCal.set(Calendar.DAY_OF_MONTH, 16);
        endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    return getEmployeeDTR(selectedEmployeeName, startCal.getTime(), endCal.getTime());
}

    
    /**
     * Get DTR data for a specific employee
     */
    public ArrayList<ArrayList<String>> getEmployeeDTR(String employeeName, java.util.Date startDate, java.util.Date endDate) {
    ArrayList<ArrayList<String>> dtrData = new ArrayList<>();

    if (employeeName == null || employeeName.trim().isEmpty()) {
        System.err.println("❌ Employee name is null or empty");
        return dtrData;
    }

    // Find employee ID from the name
    int employeeId = getEmployeeIdByName(employeeName.trim());
    if (employeeId == -1) {
        System.err.println("❌ Employee not found: " + employeeName);
        return dtrData;
    }

    String sql = "SELECT a.attendance_id, a.log_date, a.login_time, a.logout_time, " +
                 "a.submitted_to_supervisor, a.submitted_to_payroll, a.remarks, " +
                 "CONCAT(e.first_name, ' ', e.last_name) as employee_name " +
                 "FROM attendance a " +
                 "JOIN employees e ON a.employee_id = e.employee_id " +
                 "WHERE a.employee_id = ? AND a.log_date BETWEEN ? AND ? " +
                 "ORDER BY a.log_date DESC";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, employeeId);
        pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
        pstmt.setDate(3, new java.sql.Date(endDate.getTime()));

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            ArrayList<String> row = new ArrayList<>();
            row.add(String.valueOf(rs.getInt("attendance_id")));         // 0: Attendance ID (hidden)
            row.add(rs.getString("employee_name"));                      // 1: Employee Name
            row.add(rs.getDate("log_date").toString());                  // 2: Date
            row.add(rs.getTime("login_time") != null ? rs.getTime("login_time").toString() : ""); // 3: Login
            row.add(rs.getTime("logout_time") != null ? rs.getTime("logout_time").toString() : ""); // 4: Logout
            row.add(rs.getBoolean("submitted_to_supervisor") ? "Yes" : "No"); // 5: To Supervisor
            row.add(rs.getBoolean("submitted_to_payroll") ? "Yes" : "No");     // 6: To Payroll
            row.add(rs.getString("remarks") != null ? rs.getString("remarks") : ""); // 7: Remarks
            dtrData.add(row);
        }

        System.out.println("✅ Loaded " + dtrData.size() + " DTR records for " + employeeName + " (ID: " + employeeId + ")");

    } catch (SQLException e) {
        System.err.println("❌ Error getting employee DTR: " + e.getMessage());
        e.printStackTrace();
    }

    return dtrData;
}

    /**
     * Get employee ID by name
     */
   private int getEmployeeIdByName(String employeeName) {
    if (employeeName == null || employeeName.trim().isEmpty()) {
        return -1;
    }

    // Search in the idAndNames list first (more efficient)
    for (ArrayList<String> idName : idAndNames) {
        if (idName.size() >= 2 && idName.get(1).equals(employeeName)) {
            try {
                return Integer.parseInt(idName.get(0));
            } catch (NumberFormatException e) {
                System.err.println("❌ Invalid employee ID format: " + idName.get(0));
            }
        }
    }

    // If not found in cache, search database directly
    String sql = "SELECT employee_id FROM employees WHERE CONCAT(first_name, ' ', last_name) = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, employeeName);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int empId = rs.getInt("employee_id");
            System.out.println("✅ Found employee ID " + empId + " for: " + employeeName);
            return empId;
        }
    } catch (SQLException e) {
        System.err.println("❌ Error finding employee ID: " + e.getMessage());
        e.printStackTrace();
    }

    System.err.println("❌ Employee not found in database: " + employeeName);
    return -1;
}
    /**
     * Forward DTR records to payroll staff
     */
   public boolean forwardDTRToPayroll(ArrayList<ArrayList<String>> selectedDTRData) {
    if (selectedDTRData == null || selectedDTRData.isEmpty()) {
        System.err.println("❌ No DTR data to forward");
        return false;
    }

    String sql = "UPDATE attendance SET submitted_to_payroll = 1 WHERE attendance_id = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        int successCount = 0;

        for (ArrayList<String> row : selectedDTRData) {
            if (row.size() >= 1) {
                try {
                    int attendanceId = Integer.parseInt(row.get(0)); // Attendance ID is at index 0
                    pstmt.setInt(1, attendanceId);

                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        successCount++;
                        System.out.println("✅ Forwarded DTR ID: " + attendanceId + " to payroll");
                    } else {
                        System.err.println("❌ Failed to forward DTR ID: " + attendanceId);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("❌ Invalid attendance ID: " + row.get(0));
                }
            }
        }

        System.out.println("✅ Successfully forwarded " + successCount + " DTR records to payroll");
        return successCount > 0;

    } catch (SQLException e) {
        System.err.println("❌ Error forwarding DTR to payroll: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

  public void setSelectedEmployeeName(String employeeName) {
    this.selectedEmployeeName = employeeName != null ? employeeName.trim() : "";
    System.out.println("📝 Selected employee for DTR: " + this.selectedEmployeeName);
}
    public String getSelectedEmployeeName() {
        return selectedEmployeeName;
    }

    /**
     * Get DTR data for currently selected employee
     */
    public ArrayList<ArrayList<String>> getSelectedEmployeeDTRData(java.util.Date startDate, java.util.Date endDate) {
        if (selectedEmployeeName == null || selectedEmployeeName.trim().isEmpty()) {
            System.err.println("No employee selected for DTR");
            return new ArrayList<>();
        }

        return getEmployeeDTR(selectedEmployeeName, startDate, endDate);
    }
    
    // ===== TABLE DISPLAY METHODS =====
    
    @Override
    public void setTableData(ArrayList<ArrayList<String>> data) {
        this.newData = data;
    }
    
    public void setTableData() {
        // Load default data
        setTableData(getDataAllRequests());
    }
    
    @Override
    public void setTableSize(int size) {
        this.tableSize = size;
    }
    
    @Override
    public void displayDataTable(javax.swing.JTable table) {
        if (newData != null && !newData.isEmpty()) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing data
            
            // Check how many columns the table model has
            int tableColumns = model.getColumnCount();
            System.out.println("DEBUG: Table model has " + tableColumns + " columns");
            
            for (ArrayList<String> row : newData) {
                // Create row data array matching the table's column count
                Object[] rowData = new Object[tableColumns];
                
                // Fill the row data with available data
                for (int i = 0; i < tableColumns; i++) {
                    if (i < row.size()) {
                        rowData[i] = row.get(i);
                    } else {
                        rowData[i] = ""; // Fill missing columns with empty string
                    }
                }
                model.addRow(rowData);
            }
            
            // Only hide the Real ID column if the table has 10 columns
            // This check prevents the error you're seeing
            if (tableColumns >= 10) {
                try {
                    // Hide the last column (Real ID column)
                    int lastColumnIndex = tableColumns - 1;
                    table.getColumnModel().getColumn(lastColumnIndex).setMinWidth(0);
                    table.getColumnModel().getColumn(lastColumnIndex).setMaxWidth(0);
                    table.getColumnModel().getColumn(lastColumnIndex).setWidth(0);
                    System.out.println("✅ Hidden Real ID column at index " + lastColumnIndex);
                } catch (Exception e) {
                    System.err.println("❌ Error hiding Real ID column: " + e.getMessage());
                }
            }
            
            System.out.println("✅ Table populated with " + newData.size() + " rows and " + tableColumns + " columns");
        } else {
            System.out.println("⚠️ No data to display in table");
        }
    }
    
    // ===== LEAVE REQUEST FILING =====
    
    @Override
    public boolean fileLeaveRequest(ArrayList<String> leaveData) {
        // Supervisor can also file leave requests
        return super.fileLeaveRequest(leaveData);
    }
    
    public boolean fileOvertimeRequest(ArrayList<String> overtimeData) {
        // Supervisor can also file overtime requests
        return super.fileOvertimeRequest(overtimeData);
    }
    
    // ===== DATA RETRIEVAL METHODS =====
    
    public ArrayList<ArrayList<String>> getDataList() {
        return newData;
    }
    
    // ===== UTILITY METHODS =====
    
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
    
    // ===== AUTHENTICATION METHODS =====
    
    public void userLogin() {
        System.out.println("Supervisor login: " + employeeID);
    }
    
    public void userLogout() {
        System.out.println("Supervisor logout: " + employeeID);
    }

  private void createDefaultLeaveBalance() {
    try {
        System.out.println("=== Creating default leave balance for employee: " + this.employeeID + " ===");
        
        // First check if balance already exists
        String checkSql = "SELECT employee_id FROM leave_balances WHERE employee_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, this.employeeID);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Leave balance already exists for employee: " + this.employeeID);
                return;
            }
        }
        
        // Create new leave balance record with default values (24 days each)
        String insertSql = "INSERT INTO leave_balances (employee_id, vacation_leave, sick_leave) VALUES (?, 24.00, 24.00)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            insertStmt.setInt(1, this.employeeID);
            int result = insertStmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ Created default leave balance (24 VL, 24 SL) for employee: " + this.employeeID);
            } else {
                System.err.println("❌ Failed to create leave balance for employee: " + this.employeeID);
            }
        }
        
    } catch (SQLException e) {
        System.err.println("❌ Error creating default leave balance for employee " + this.employeeID + ": " + e.getMessage());
        e.printStackTrace();
    }
}
  
    public boolean submitMultipleDTRToSupervisor(int employeeId, List<String> dates) {
        try {
            String sql = "UPDATE attendance SET submitted_to_supervisor = 1 WHERE employee_id = ? AND log_date = ?";

            int successCount = 0;

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                for (String dateStr : dates) {
                    pstmt.setInt(1, employeeId);

                    // Use the helper method to convert date
                    java.sql.Date sqlDate = convertStringToSqlDate(dateStr);
                    pstmt.setDate(2, sqlDate);

                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        successCount++;
                    }
                }
            }

            System.out.println("✅ Successfully submitted " + successCount + " DTR entries to supervisor");
            return successCount > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error in bulk DTR submission: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 5. FIX: Add isDTRSubmittedToSupervisor to Supervisor.java
    // Add this method to your Supervisor.java class:
    public boolean isDTRSubmittedToSupervisor(String employeeId, String date) {
        try {
            String sql = "SELECT submitted_to_supervisor FROM attendance WHERE employee_id = ? AND log_date = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(employeeId));
                pstmt.setDate(2, convertStringToSqlDate(date));

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    return rs.getBoolean("submitted_to_supervisor");
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error checking DTR submission status: " + e.getMessage());
        }

        return false;
    }   
}
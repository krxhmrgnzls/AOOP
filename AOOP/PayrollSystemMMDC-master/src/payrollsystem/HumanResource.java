package payrollsystem;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.text.SimpleDateFormat;
import java.util.List;
import payrollsystem.LeaveDAO;

public class HumanResource extends Employee {
    private DatabaseConnection dbConnection;
    Connection connection;
    private String employeeID, selectedName;
    private ArrayList<String> fullName = new ArrayList<>();
    private ArrayList<ArrayList<String>> idAndNames = new ArrayList<>();
    
    // *** FIX: Use separate data variables for each table type ***
    private ArrayList<ArrayList<String>> employeeData = new ArrayList<>();
    private ArrayList<ArrayList<String>> requestData = new ArrayList<>();
    private ArrayList<ArrayList<String>> dtrData = new ArrayList<>();
    private ArrayList<ArrayList<String>> ledgerData = new ArrayList<>();
    private ArrayList<ArrayList<String>> credentialData = new ArrayList<>();
    
    // Keep the old newData for backward compatibility
    private ArrayList<ArrayList<String>> newData = new ArrayList<>();
    private int tableSize;
    private int numberOfDaysLeave = 0;
    private String balanceVL = "0.00";
    private String balanceSL = "0.00";
    private EmployeeDAO employeeDAO;
    private LeaveDAO leaveDAO;
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    
    public HumanResource(String employeeID) {
        super();
        this.employeeID = employeeID;
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.connection = dbConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
        }
        this.employeeDAO = new EmployeeDAO();
        this.leaveDAO = new LeaveDAO();
    }
    
    public void setEmployeeTableData(ArrayList<ArrayList<String>> data) {
        this.employeeData = data;
        this.newData = data;
    }
    
    public void setRequestTableData(ArrayList<ArrayList<String>> data) {
        this.requestData = data;
        this.newData = data; 
    }
    
    public void setDTRTableData(ArrayList<ArrayList<String>> data) {
        this.dtrData = data;
        this.newData = data;
    }
    
    public void setLedgerTableData(ArrayList<ArrayList<String>> data) {
        this.ledgerData = data;
        this.newData = data; 
    }
    
    public void setCredentialTableData(ArrayList<ArrayList<String>> data) {
        this.credentialData = data;
        this.newData = data; 
    }

    public void setTableData(ArrayList<ArrayList<String>> tableData) { 
        this.newData = tableData; 
    }

    public void displayEmployeeTable(JTable table) {
        try {
            System.out.println("DEBUG: Displaying EMPLOYEE table");

            ArrayList<ArrayList<String>> employees = displayAllDetails();
            this.employeeData = employees;
            this.newData = employees; 

            displayDataTable(table);
            
            System.out.println("DEBUG: Employee table updated with " + employees.size() + " employees");
        } catch (Exception e) {
            System.err.println("Error displaying employee table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void displayRequestTable(JTable table) {
        try {
            System.out.println("DEBUG: Displaying REQUEST table");

            ArrayList<ArrayList<String>> requests = loadAllRequestsForEmployee();
            this.requestData = requests;
            this.newData = requests; 

            displayDataTable(table);
            
            System.out.println("DEBUG: Request table updated with " + requests.size() + " requests");
        } catch (Exception e) {
            System.err.println("Error displaying request table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void displayDTRTable(JTable table, Date fromDate, Date toDate) {
        try {
            System.out.println("DEBUG: Displaying DTR table");

            ArrayList<ArrayList<String>> dtr = getDTRData(fromDate, toDate);
            this.dtrData = dtr;
            this.newData = dtr; 

            displayDataTable(table);
            
            System.out.println("DEBUG: DTR table updated with " + dtr.size() + " records");
            } catch (Exception e) {
                System.err.println("Error displaying DTR table: " + e.getMessage());
                e.printStackTrace();
        }
    }
    
    public void displayLedgerTable(JTable table) {
        try {
            System.out.println("DEBUG: Displaying LEDGER table");
            
            ArrayList<ArrayList<String>> ledger = getLeaveBalancesLedger();
            this.ledgerData = ledger;
            this.newData = ledger; 

            displayDataTable(table);
            
            System.out.println("DEBUG: Ledger table updated with " + ledger.size() + " records");
        } catch (Exception e) {
            System.err.println("Error displaying ledger table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void displayCredentialTable(JTable table) {
        try {
            System.out.println("DEBUG: Displaying CREDENTIAL table");
            
            ArrayList<ArrayList<String>> credentials = allCredentials();
            this.credentialData = credentials;
            this.newData = credentials; 

            displayDataTable(table);
            
            System.out.println("DEBUG: Credential table updated with " + credentials.size() + " credentials");
        } catch (Exception e) {
            System.err.println("Error displaying credential table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<String>> loadAllRequestsForEmployee() {
        ArrayList<ArrayList<String>> requests = new ArrayList<>();
        
        String sql = "SELECT date_filed, 'Leave' as request_type, from_date, to_date, number_of_days, reason, status " +
                    "FROM leave_requests WHERE employee_id = ? " +
                    "UNION ALL " +
                    "SELECT date_filed, 'Overtime' as request_type, DATE(from_time), DATE(to_time), number_of_days, reason, status " +
                    "FROM overtime_requests WHERE employee_id = ? " +
                    "ORDER BY date_filed DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int empId = Integer.parseInt(employeeID);
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
            System.err.println("Error loading requests for employee: " + e.getMessage());
        }
        
        return requests;
    }
    
    public ArrayList<ArrayList<String>> getDTRData(Date fromDate, Date toDate) {
        ArrayList<ArrayList<String>> dtrRecords = new ArrayList<>();
        String sql = "SELECT log_date, login_time, logout_time, " +
                    "CASE WHEN submitted_to_supervisor = 1 THEN 'Yes' ELSE 'No' END as submitted, " +
                    "COALESCE(remarks, '') as remarks FROM attendance " +
                    "WHERE employee_id = ? AND log_date BETWEEN ? AND ? ORDER BY log_date";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(employeeID));
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
    
    public ArrayList<ArrayList<String>> getLeaveBalancesLedger() {
        ArrayList<ArrayList<String>> ledger = new ArrayList<>();
        String sql = "SELECT date_filed, leave_type, from_date, to_date, number_of_days, reason, status " +
                    "FROM leave_requests WHERE employee_id = ? AND status = 'Approved' ORDER BY date_filed DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(employeeID));
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
                ledger.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting leave ledger: " + e.getMessage());
        }
        
        return ledger;
    }

    public ArrayList<ArrayList<String>> getIdAndNames() { return idAndNames; }
    public ArrayList<ArrayList<String>> getNewData() { return newData; }
    public void setTableSize(int tableSize) { this.tableSize = tableSize; }
    public int getNumberOfDaysLeave() { return numberOfDaysLeave; }
    public void setNumberOfDaysLeave() { this.numberOfDaysLeave = 0; }
   
    public String getBalanceVL() {
        try {
            int employeeId = this.accountDetails.getEmployeeID();
            String sql = "SELECT vacation_leave FROM leave_balances WHERE employee_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
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
        }
        return "24.0";
    }
    
    public String getBalanceSL() {
        try {
            int employeeId = this.accountDetails.getEmployeeID();
            String sql = "SELECT sick_leave FROM leave_balances WHERE employee_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
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
        }
        return "24.0";
    }
    
    public void setSelectedName(String selectedName) { this.selectedName = selectedName; }
    public String getSelectedName() { return selectedName; }

    public String nextID() {
        String sql = "SELECT MAX(employee_id) FROM employees";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.valueOf(maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting next ID: " + e.getMessage());
        }
        return "10001";
    }
    
    public boolean addDetails(ArrayList<String> tempData) {
        for (String info : tempData) {
            if (info.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Complete All The Details!");
                return false;
            }
        }
        
        String checkSql = "SELECT COUNT(*) FROM employees WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, tempData.get(1));
            checkStmt.setString(2, tempData.get(2));
            
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Cannot Add New Employee - Employee Already Exists!");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error checking existing employee: " + e.getMessage());
            return false;
        }
        
        String insertSql = "INSERT INTO employees (employee_id, last_name, first_name, birthday, address, " +
                            "phone_number, sss_number, philhealth_number, tin_number, pagibig_number, " +
                            "status, position, supervisor_id, basic_salary, rice_subsidy, " +
                            "phone_allowance, clothing_allowance, gross_rate, hourly_rate) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
            pstmt.setInt(1, Integer.parseInt(tempData.get(0)));
            pstmt.setString(2, tempData.get(2));
            pstmt.setString(3, tempData.get(1));
            pstmt.setDate(4, java.sql.Date.valueOf(convertToSqlDate(tempData.get(3))));
            pstmt.setString(5, tempData.get(4));
            pstmt.setString(6, tempData.get(5));
            pstmt.setString(7, tempData.get(6));
            pstmt.setString(8, tempData.get(7));
            pstmt.setString(9, tempData.get(8));
            pstmt.setString(10, tempData.get(9));
            pstmt.setString(11, tempData.get(10));
            pstmt.setString(12, tempData.get(11));
            pstmt.setString(13, tempData.get(12));
            pstmt.setDouble(14, Double.parseDouble(tempData.get(13)));
            pstmt.setDouble(15, Double.parseDouble(tempData.get(14)));
            pstmt.setDouble(16, Double.parseDouble(tempData.get(15)));
            pstmt.setDouble(17, Double.parseDouble(tempData.get(16)));
            pstmt.setDouble(18, Double.parseDouble(tempData.get(17)));
            pstmt.setDouble(19, Double.parseDouble(tempData.get(18)));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                initializeLeaveBalances(Integer.parseInt(tempData.get(0)));
                JOptionPane.showMessageDialog(null, "Successfully Added New Employee!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error adding employee: " + e.getMessage());
        }
        return false;
    }
    
    private void initializeLeaveBalances(int employeeId) {
        String sql = "INSERT INTO leave_balances (employee_id, vacation_leave_balance, sick_leave_balance) VALUES (?, 24.00, 24.00)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error initializing leave balances: " + e.getMessage());
        }
    }
    
    public ArrayList<ArrayList<String>> allCredentials() {
        ArrayList<ArrayList<String>> credentials = new ArrayList<>();
        String sql = "SELECT lc.employee_name, lc.employee_id, lc.role " +
                    "FROM login_credentials lc ORDER BY lc.employee_id";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(rs.getString("employee_id"));
                row.add(rs.getString("employee_name"));
                row.add(rs.getString("role"));
                credentials.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting credentials: " + e.getMessage());
        }
        return credentials;
    }
    
    public ArrayList<ArrayList<String>> displayAllDetails() {
    ArrayList<ArrayList<String>> employees = new ArrayList<>();

    String sql = "SELECT e.employee_id, e.last_name, e.first_name, e.birthday, e.address, e.phone_number, " +
                "e.sss_number, e.philhealth_number, e.tin_number, e.pagibig_number, e.status, e.position, " +
                "e.supervisor_id, " +
                "CONCAT(s.first_name, ' ', s.last_name) as supervisor_name, " +
                "e.basic_salary, e.rice_subsidy, e.phone_allowance, " +
                "e.clothing_allowance, e.gross_rate, e.hourly_rate " +
                "FROM employees e " +
                "LEFT JOIN employees s ON e.supervisor_id = s.employee_id " +
                "ORDER BY e.employee_id";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        
        while (rs.next()) {
            ArrayList<String> row = new ArrayList<>();
            row.add(String.valueOf(rs.getInt("employee_id")));
            row.add(rs.getString("last_name") != null ? rs.getString("last_name") : "");
            row.add(rs.getString("first_name") != null ? rs.getString("first_name") : "");
            row.add(rs.getDate("birthday") != null ? rs.getDate("birthday").toString() : "");
            row.add(rs.getString("address") != null ? rs.getString("address") : "");
            row.add(rs.getString("phone_number") != null ? rs.getString("phone_number") : "");

            row.add(formatGovernmentNumber(rs.getString("sss_number")));
            row.add(formatGovernmentNumber(rs.getString("philhealth_number")));
            row.add(formatGovernmentNumber(rs.getString("tin_number")));
            row.add(formatGovernmentNumber(rs.getString("pagibig_number")));
            
            row.add(rs.getString("status") != null ? rs.getString("status") : "");
            row.add(rs.getString("position") != null ? rs.getString("position") : "");

            String supervisorName = rs.getString("supervisor_name");
            row.add(supervisorName != null ? supervisorName : "");
            
            row.add(String.format("%.2f", rs.getDouble("basic_salary")));
            row.add(String.format("%.2f", rs.getDouble("rice_subsidy")));
            row.add(String.format("%.2f", rs.getDouble("phone_allowance")));
            row.add(String.format("%.2f", rs.getDouble("clothing_allowance")));
            row.add(String.format("%.2f", rs.getDouble("gross_rate")));
            row.add(String.format("%.2f", rs.getDouble("hourly_rate")));
            employees.add(row);
        }
    } catch (SQLException e) {
        System.err.println("Error getting employee details: " + e.getMessage());
        e.printStackTrace();
    }
    return employees;
}
    
    public void getEmployeeNames() {
        idAndNames.clear();
        newData.clear();
        fullName.clear();
        
        String sql = "SELECT employee_id, CONCAT(first_name, ' ', last_name) as full_name " +
                    "FROM employees ORDER BY first_name, last_name";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ArrayList<String> names = new ArrayList<>();
                names.add(String.valueOf(rs.getInt("employee_id")));
                names.add(rs.getString("full_name"));
                idAndNames.add(names);
                fullName.add(rs.getString("full_name"));
            }
            
            Collections.sort(fullName);
            newData.add(fullName);
        } catch (SQLException e) {
            System.err.println("Error getting employee names: " + e.getMessage());
        }
    }
    
    public String getID() {
        for (ArrayList<String> idName : idAndNames) {
            if (idName.get(1).equals(selectedName)) {
                return idName.get(0);
            }
        }
        return "";
    }
    
    public boolean addNewCredentials(ArrayList<String> tempData) {
        for (String info : tempData) {
            if (info.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Provide All The Necessary Information!");
                return false;
            }
        }
        
        String checkSql = "SELECT COUNT(*) FROM login_credentials WHERE employee_id = ? AND role = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, tempData.get(0));
            checkStmt.setString(2, tempData.get(3));
            
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Cannot Add New Credentials - Employee Already Exists With The Same Role!");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error checking existing credentials: " + e.getMessage());
            return false;
        }
        
        String insertSql = "INSERT INTO login_credentials (employee_name, employee_id, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
            pstmt.setString(1, tempData.get(1));
            pstmt.setString(2, tempData.get(0));
            pstmt.setString(3, tempData.get(2));
            pstmt.setString(4, tempData.get(3));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "New Credentials Added!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding credentials: " + e.getMessage());
        }
        return false;
    }
    
    public boolean validateDateBirthday(Date dateBirthday) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateBirthday);
        
        LocalDate birthDate = LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        LocalDate currentDate = LocalDate.now();
        LocalDate eighteenYearsAgo = currentDate.minusYears(18);
        
        return !birthDate.isAfter(eighteenYearsAgo);
    }
    
    public void displayDataTable(JTable table) {
        try {
            System.out.println("=== DEBUG displayDataTable ===");
            System.out.println("Table: " + (table != null ? table.getClass().getSimpleName() : "NULL"));
            System.out.println("NewData: " + (newData != null ? newData.size() + " rows" : "NULL"));
            
            if (newData != null && !newData.isEmpty()) {
                System.out.println("First row data: " + newData.get(0));
            }
            
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            System.out.println("Table model columns: " + model.getColumnCount());
            System.out.println("Table model rows before clear: " + model.getRowCount());
            
            model.setRowCount(0);
            System.out.println("Table cleared");
            
            if (newData != null && !newData.isEmpty()) {
                for (int i = 0; i < newData.size(); i++) {
                    ArrayList<String> row = newData.get(i);
                    
                    Object[] rowData = new Object[Math.min(row.size(), model.getColumnCount())];
                    
                    for (int j = 0; j < rowData.length; j++) {
                        rowData[j] = (j < row.size()) ? row.get(j) : "";
                    }
                    
                    model.addRow(rowData);
                    
                    if (i < 3) {
                        System.out.println("Added row " + i + ": " + java.util.Arrays.toString(rowData));
                    }
                }
                
                System.out.println("Successfully added " + newData.size() + " rows to table");
                System.out.println("Table model rows after adding: " + model.getRowCount());
            } else {
                System.out.println("No data to display - newData is empty or null");
            }
            
            System.out.println("=== END DEBUG displayDataTable ===");
            
        } catch (Exception e) {
            System.err.println("Error displaying data in table: " + e.getMessage());
            e.printStackTrace();
        }
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
    
    public boolean isValidDateRange(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            return false;
        }
        
        if (toDate.before(fromDate)) {
            return false;
        }
        
        numberOfDaysLeave = calculateDaysFromDates(fromDate, toDate);
        return true;
    }
    
    public void leaveBalancesInformation() {
        String sql = "SELECT vacation_leave_balance, sick_leave_balance FROM leave_balances WHERE employee_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(employeeID));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                balanceVL = String.format("%.2f", rs.getDouble("vacation_leave_balance"));
                balanceSL = String.format("%.2f", rs.getDouble("sick_leave_balance"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting leave balances: " + e.getMessage());
        }
    }
    
    public void updateLeaveBalanceLabels(JLabel vlLabel, JLabel slLabel) {
        String vlBalance = "24.0";
        String slBalance = "24.0";
        try {
            int employeeId = this.accountDetails.getEmployeeID();
            String sql = "SELECT vacation_leave, sick_leave FROM leave_balances WHERE employee_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                vlBalance = String.valueOf(rs.getDouble("vacation_leave"));
                slBalance = String.valueOf(rs.getDouble("sick_leave"));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.err.println("Error getting leave balance: " + e.getMessage());
        }
        vlLabel.setText(vlBalance);
        slLabel.setText(slBalance);
        System.out.println("Leave balances: VL=" + vlBalance + ", SL=" + slBalance);
    }
    
    public ArrayList<ArrayList<String>> getAttendanceRecords(Date fromDate, Date toDate) {
        ArrayList<ArrayList<String>> attendanceData = new ArrayList<>();
        try {
            AttendanceDAO attendanceDAO = new AttendanceDAO();
            java.sql.Date sqlFromDate = new java.sql.Date(fromDate.getTime());
            java.sql.Date sqlToDate = new java.sql.Date(toDate.getTime());
            
            List<AttendanceRecord> records = attendanceDAO.getAttendanceByEmployee(Integer.parseInt(employeeID), sqlFromDate, sqlToDate);
            for (AttendanceRecord record : records) {
                ArrayList<String> row = new ArrayList<>();
                row.add(record.getLogDate().toString());
                row.add(record.getLoginTime() != null ? record.getLoginTime().toString() : "");
                row.add(record.getLogoutTime() != null ? record.getLogoutTime().toString() : "");
                row.add(record.isSubmittedToSupervisor() ? "Yes" : "No");
                row.add(record.getRemarks() != null ? record.getRemarks() : "");
                attendanceData.add(row);
            }
        } catch (Exception e) {
            System.err.println("Error getting attendance records: " + e.getMessage());
        }
        return attendanceData;
    }
    
    public boolean submitDTRToSupervisor(ArrayList<String> selectedDates) {
        try {
            AttendanceDAO attendanceDAO = new AttendanceDAO();
            for (String dateStr : selectedDates) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(dateStr);
                List<AttendanceRecord> records = attendanceDAO.getAttendanceByEmployee(Integer.parseInt(employeeID), sqlDate, sqlDate);
                if (!records.isEmpty()) {
                    AttendanceRecord record = records.get(0);
                    record.setSubmittedToSupervisor(true);
                    boolean success = attendanceDAO.updateAttendance(record);
                    if (!success) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error submitting DTR to supervisor: " + e.getMessage());
            return false;
        }
    }
    
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

                System.out.println("DEBUG: Successfully loaded personal details for employee " + employeeId);
            } else {
                System.err.println("ERROR: No employee found with ID " + employeeId);
            }
        } catch (SQLException e) {
            System.err.println("Error loading personal details: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        return getLeaveBalancesLedger();
    }
    
    public ArrayList<ArrayList<String>> getDataAllRequests() {
        return loadAllRequestsForEmployee();
    }
    
    public ArrayList<ArrayList<String>> getDataAllDTR(Date fromDate, Date toDate) {
        return getDTRData(fromDate, toDate);
    }
    
    public void userLogin() {
    }
    
    public void userLogout() {
    }
}
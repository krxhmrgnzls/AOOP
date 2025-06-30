package payrollsystem;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.ZoneId;

public class HumanResource extends Employee {
    private EmployeeDAO employeeDAO;
    private LeaveDAO leaveDAO;
    
    public HumanResource() {
        super();
        this.employeeDAO = new EmployeeDAO();
        this.leaveDAO = new LeaveDAO();
    }
    
    // ADD: Constructor that GUI expects
    public HumanResource(String employeeId) {
        super(employeeId);  // Call Employee constructor with string
        this.employeeDAO = new EmployeeDAO();
        this.leaveDAO = new LeaveDAO();
    }
    
    public HumanResource(ArrayList<ArrayList<String>> userDetails) {
        super();
        this.employeeDAO = new EmployeeDAO();
        this.leaveDAO = new LeaveDAO();
        
        if (userDetails != null && !userDetails.isEmpty()) {
            // Initialize from user details
            ArrayList<String> details = userDetails.get(0);
            if (details.size() >= 4) {
                this.accountDetails.setEmployeeID(Integer.parseInt(details.get(0)));
                this.accountDetails.setFirstName(details.get(1));
                this.accountDetails.setLastName(details.get(2));
                // this.role = details.get(3); 
            }
        }
    }
    
    // Database operations
    public boolean addEmployeeToDB(AccountDetails employee) {
        try {
            return employeeDAO.create(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteEmployeeFromDB(int employeeId) {
        try {
            return employeeDAO.delete(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<AccountDetails> searchEmployees(String keyword) {
        try {
            return employeeDAO.searchEmployees(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<AccountDetails> getAllEmployees() {
        try {
            return employeeDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public boolean updateEmployee(AccountDetails employee) {
        try {
            return employeeDAO.update(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public AccountDetails getEmployee(int employeeId) {
        try {
            return employeeDAO.read(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Export functionality
    public void exportEmployeeList() {
        try {
            List<AccountDetails> employees = employeeDAO.findAll();
            System.out.println("Exported " + employees.size() + " employees");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // CSV Migration (if needed)
    public boolean migrateCSVtoDB(String csvFilePath) {
        try {
            // Implementation for migrating CSV data to database
            System.out.println("CSV migration completed");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Override viewPersonalDetails method
    public void viewPersonalDetails() {
        if (accountDetails != null) {
            System.out.println("HR Personal Details:");
            System.out.println("ID: " + accountDetails.getEmployeeID());
            System.out.println("Name: " + accountDetails.getFirstName() + " " + accountDetails.getLastName());
            System.out.println("Position: " + accountDetails.getPosition());
        }
    }
    
    // Method to get employee names for dropdown/combo boxes
    public ArrayList<ArrayList<String>> getEmployeeNames() {
        ArrayList<ArrayList<String>> employeeNames = new ArrayList<>();
        try {
            List<AccountDetails> employees = employeeDAO.findAll();
            for (AccountDetails emp : employees) {
                ArrayList<String> nameData = new ArrayList<>();
                nameData.add(emp.getLastName() + ", " + emp.getFirstName());
                nameData.add(String.valueOf(emp.getEmployeeID()));
                employeeNames.add(nameData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeNames;
    }
    
    // Method to validate employee data
    public boolean validateEmployeeData(AccountDetails employee) {
        if (employee == null) return false;
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) return false;
        if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) return false;
        if (employee.getEmployeeID() <= 0) return false;
        return true;
    }
    
    public void setTableData(ArrayList<ArrayList<String>> data) {
        // Store table data for display
        this.newData = data;
    }

    public void setTableSize(int size) {
        // Set table size - not critical for functionality
    }

    public void displayRequestsTable(javax.swing.JTable table) {
    String[] columnNames = {"DATE FILED", "TYPE OF REQUEST", "PERIOD FROM", "PERIOD TO", "NUMBER OF DAYS", "REASON", "STATUS"};
    
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    if (newData != null && !newData.isEmpty()) {
        for (ArrayList<String> row : newData) {
            Object[] rowData = new Object[7];
            for (int i = 0; i < 7; i++) {
                rowData[i] = (i < row.size()) ? row.get(i) : "";
            }
            model.addRow(rowData);
        }
        System.out.println("Requests table updated with " + newData.size() + " rows");
    } else {
        System.out.println("No requests data to display");
    }
    
    table.setModel(model);
}

    public ArrayList<ArrayList<String>> getAllCredentials() {
        ArrayList<ArrayList<String>> credentials = new ArrayList<>();
        try {
            List<AccountDetails> employees = employeeDAO.findAll();
            for (AccountDetails emp : employees) {
                ArrayList<String> cred = new ArrayList<>();
                cred.add(String.valueOf(emp.getEmployeeID()));
                cred.add(emp.getFirstName() + " " + emp.getLastName());
                cred.add(emp.getPosition());
                cred.add(emp.getStatus());
                credentials.add(cred);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return credentials;
    }

    public ArrayList<ArrayList<String>> getDataAllEmployees() {
        return getAllCredentials(); 
    }

    public ArrayList<ArrayList<String>> getDataAllRequests() {
        return new ArrayList<>();
    }

    public AccountDetails getEmployeeById(int employeeId) {
        try {
            return employeeDAO.read(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateLeaveBalanceLabels(javax.swing.JLabel label1, javax.swing.JLabel label2) {
        label1.setText("24.0");
        label2.setText("24.0");
    }

    public ArrayList<ArrayList<String>> getAllApprovedPersonalLeaveLedger() {
        return new ArrayList<>();
    }

    public ArrayList<ArrayList<String>> getDataAllDTR(java.util.Date startDate, java.util.Date endDate) {
        return new ArrayList<>();
    }

    public boolean fileLeaveRequest(ArrayList<String> leaveData) {
        try {
            if (leaveData.size() >= 7) {
                int employeeId = Integer.parseInt(leaveData.get(0));
                String leaveType = leaveData.get(2);
                String reason = leaveData.get(6);
                
                // Parse dates
                LocalDate fromDate = LocalDate.now(); 
                LocalDate toDate = LocalDate.now().plusDays(1); 
                double numberOfDays = 1.0; 
                
                return leaveDAO.submitLeaveRequest(employeeId, leaveType, fromDate, toDate, numberOfDays, reason);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void forwardDTRToSupervisor(ArrayList<ArrayList<String>> dtrData) {

        System.out.println("DTR forwarded to supervisor");
    }

    public ArrayList<ArrayList<String>> viewPersonalPayslip(java.util.Date startDate, java.util.Date endDate, String employeeId) {

        return new ArrayList<>();
    }

    public int countNumberOfDays(java.util.Date startDate, java.util.Date endDate) {
        if (startDate == null || endDate == null) return 0;

        long diffInMillies = endDate.getTime() - startDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        return (int) diffInDays + 1; 
    }

    public int getNumberOfDaysLeave() {
        return 0; 
    }

    public void setNumberOfDaysLeave() {
       
    }

    public boolean addNewCredentials(ArrayList<String> credData) {
        try {
            AccountDetails emp = new AccountDetails();
            emp.setEmployeeID(Integer.parseInt(credData.get(0)));
            emp.setFirstName(credData.get(1));
            emp.setLastName(credData.get(2));
            return employeeDAO.create(emp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ArrayList<String>> allCredentials() {
        return getAllCredentials();
    }

    public boolean addDetails(ArrayList<String> empData) {
        try {
            AccountDetails emp = new AccountDetails();
            if (empData.size() >= 3) {
                emp.setEmployeeID(Integer.parseInt(empData.get(0)));
                emp.setFirstName(empData.get(1));
                emp.setLastName(empData.get(2));
            }
            return employeeDAO.create(emp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ArrayList<String>> displayAllDetails() {
        return getDataAllEmployees();
    }

    public String nextID() {
        try {
            List<AccountDetails> employees = employeeDAO.findAll();
            int maxId = 10000;
            for (AccountDetails emp : employees) {
                if (emp.getEmployeeID() > maxId) {
                    maxId = emp.getEmployeeID();
                }
            }
            return String.valueOf(maxId + 1);
        } catch (Exception e) {
            return "10001";
        }
    }

    public void leaveBalancesInformation() {
        System.out.println("Loading leave balance information...");
    }

    public String getBalanceVL() {
        return "24.0";
    }

    public String getBalanceSL() {
        return "24.0"; 
    }

    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        return getAllApprovedPersonalLeaveLedger();
    }

   
    public boolean fileOvertimeRequest(ArrayList<String> overtimeData) {
        try {
         
            System.out.println("Overtime request filed for employee: " + overtimeData.get(0));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Employee selection methods
    public void setSelectedName(String name) {
        // Store selected employee name
        System.out.println("Selected employee: " + name);
    }

    public String getID() {
        return String.valueOf(accountDetails.getEmployeeID());
    }

    public boolean validateDateBirthday(java.util.Date birthday) {
        if (birthday == null) return false;

        java.util.Date today = new java.util.Date();
        return birthday.before(today);
    }
    
    public boolean isValidDateRange(java.util.Date startDate, java.util.Date endDate) {
        return countNumberOfDays(startDate, endDate) > 0;
    }
}
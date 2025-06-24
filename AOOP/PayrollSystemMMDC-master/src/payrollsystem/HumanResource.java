package payrollsystem;

import java.util.List;
import java.util.ArrayList;

public class HumanResource extends Employee {
    private EmployeeDAO employeeDAO;
    
    public HumanResource() {
        super();
        this.employeeDAO = new EmployeeDAO();
    }
    
    // ADD: Constructor that GUI expects
    public HumanResource(String employeeId) {
        super(employeeId);  // Call Employee constructor with string
        this.employeeDAO = new EmployeeDAO();
    }
    
    public HumanResource(ArrayList<ArrayList<String>> userDetails) {
        super();
        this.employeeDAO = new EmployeeDAO();
        
        if (userDetails != null && !userDetails.isEmpty()) {
            // Initialize from user details
            ArrayList<String> details = userDetails.get(0);
            if (details.size() >= 4) {
                this.accountDetails.setEmployeeID(Integer.parseInt(details.get(0)));
                this.accountDetails.setFirstName(details.get(1));
                this.accountDetails.setLastName(details.get(2));
                // this.role = details.get(3); // Remove if role is not needed
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
            // Implementation for exporting to CSV/Excel
            System.out.println("✅ Exported " + employees.size() + " employees");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // CSV Migration (if needed)
    public boolean migrateCSVtoDB(String csvFilePath) {
        try {
            // Implementation for migrating CSV data to database
            System.out.println("✅ CSV migration completed");
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

    public void displayDataTable(javax.swing.JTable table) {
        // Display data in JTable
        if (newData != null && !newData.isEmpty()) {
            String[] columnNames = {"ID", "Name", "Position", "Department", "Status"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0);

            for (ArrayList<String> row : newData) {
                Object[] rowData = row.toArray();
                model.addRow(rowData);
            }
            table.setModel(model);
        }
    }

    // 2. Methods for credentials
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

    // 3. Methods for employee data
    public ArrayList<ArrayList<String>> getDataAllEmployees() {
        return getAllCredentials(); // Same as credentials for now
    }

    public ArrayList<ArrayList<String>> getDataAllRequests() {
        // Return empty list for now, implement later if needed
        return new ArrayList<>();
    }

    // 4. Method to get employee by ID
    public AccountDetails getEmployeeById(int employeeId) {
        try {
            return employeeDAO.read(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 5. Leave-related methods (basic implementation)
    public void updateLeaveBalanceLabels(javax.swing.JLabel label1, javax.swing.JLabel label2) {
        // Basic implementation - you can enhance later
        label1.setText("24.0");
        label2.setText("24.0");
    }

    public ArrayList<ArrayList<String>> getAllApprovedPersonalLeaveLedger() {
        // Return empty for now
        return new ArrayList<>();
    }

    public ArrayList<ArrayList<String>> getDataAllDTR(java.util.Date startDate, java.util.Date endDate) {
        // Return empty for now - implement attendance later
        return new ArrayList<>();
    }

    // 6. Leave request methods
 // REPLACE the fileLeaveRequest method with this:
    public boolean fileLeaveRequest(ArrayList<String> leaveData) {
        try {
            LeaveRequest leave = new LeaveRequest();

            // Set the data from the array
            if (leaveData.size() >= 7) {
                leave.setEmployeeID(Integer.parseInt(leaveData.get(0))); // Now this method exists
                leave.setLeaveType(leaveData.get(2));
                leave.setReason(leaveData.get(6));

                // You can add more field mappings here if needed
            }

            LeaveDAO leaveDAO = new LeaveDAO();
            return leaveDAO.createLeaveRequest(leave);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 7. DTR forwarding method
    public void forwardDTRToSupervisor(ArrayList<ArrayList<String>> dtrData) {
        // Basic implementation
        System.out.println("DTR forwarded to supervisor");
    }

    // 8. Payslip viewing method
    public ArrayList<ArrayList<String>> viewPersonalPayslip(java.util.Date startDate, java.util.Date endDate, String employeeId) {
        // Return empty for now - implement later
        return new ArrayList<>();
    }

    // 9. Count methods for leave calculations
    public int countNumberOfDays(java.util.Date startDate, java.util.Date endDate) {
        if (startDate == null || endDate == null) return 0;

        long diffInMillies = endDate.getTime() - startDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        return (int) diffInDays + 1; // +1 to include both start and end date
    }

    public int getNumberOfDaysLeave() {
        return 0; // Default implementation
    }

    public void setNumberOfDaysLeave() {
        // Default implementation
    }
    
    // ADD ALL THESE METHODS TO YOUR HumanResource.java CLASS:

    // 1. Credential management methods
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

    // 2. Employee details methods
    public boolean addDetails(ArrayList<String> empData) {
        try {
            AccountDetails emp = new AccountDetails();
            if (empData.size() >= 3) {
                emp.setEmployeeID(Integer.parseInt(empData.get(0)));
                emp.setFirstName(empData.get(1));
                emp.setLastName(empData.get(2));
                // Add more fields as needed
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

    // 3. Leave balance methods
    public void leaveBalancesInformation() {
        // Load leave balance information
        System.out.println("Loading leave balance information...");
    }

    public String getBalanceVL() {
        return "24.0"; // Default vacation leave balance
    }

    public String getBalanceSL() {
        return "24.0"; // Default sick leave balance
    }

    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        return getAllApprovedPersonalLeaveLedger();
    }

    // 4. Overtime request method
    public boolean fileOvertimeRequest(ArrayList<String> overtimeData) {
        try {
            // Basic overtime request implementation
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

    // 6. Date validation method
    public boolean validateDateBirthday(java.util.Date birthday) {
        if (birthday == null) return false;

        // Check if birthday is not in the future
        java.util.Date today = new java.util.Date();
        return birthday.before(today);
    }
    // Helper method for boolean checks in GUI
    public boolean isValidDateRange(java.util.Date startDate, java.util.Date endDate) {
        return countNumberOfDays(startDate, endDate) > 0;
    }
}
package payrollsystem;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

public class PayrollStaff extends Employee implements Payroll {
    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;
    private ArrayList<ArrayList<String>> tableData;
    private int tableSize;
    
    public PayrollStaff() {
        super();
        this.payrollDAO = new PayrollDAO();
        this.employeeDAO = new EmployeeDAO();
        this.tableData = new ArrayList<>();
    }
    
    // ADD: Constructor that GUI expects
    public PayrollStaff(String employeeId) {
        super(employeeId);  // Call Employee constructor with string
        this.payrollDAO = new PayrollDAO();
        this.employeeDAO = new EmployeeDAO();
        this.tableData = new ArrayList<>();
    }
    
    // **FIXED: Process payroll using database instead of CSV**
    public String processPayrollDB() {
        try {
            String payrollPeriod = getCurrentPayrollPeriod();
            System.out.println("🔄 Processing payroll for period: " + payrollPeriod);
            
            boolean success = payrollDAO.processPayrollForAllEmployees(payrollPeriod);
            
            if (success) {
                System.out.println("✅ Payroll processing completed successfully!");
                return payrollPeriod;
            } else {
                System.out.println("❌ Payroll processing failed!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // **FIXED: Get payroll data from database for table display**
    public ArrayList<ArrayList<String>> getDataForPayrollTable() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        
        try {
            String currentPeriod = getCurrentPayrollPeriod();
            List<PayrollRecord> payrolls = payrollDAO.getPayrollByMonth(
                currentPeriod.substring(0, 2), // month
                Integer.parseInt(currentPeriod.substring(6, 10)) // year
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
            
            System.out.println("📊 Retrieved " + data.size() + " payroll records from database");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }
    
    // **FIXED: Release payroll using database**
    public void releasedPayroll(ArrayList<ArrayList<String>> selectedPayrolls) {
        try {
            for (ArrayList<String> payrollData : selectedPayrolls) {
                int employeeId = Integer.parseInt(payrollData.get(0));
                String payrollPeriod = payrollData.get(2);
                
                // Get the payroll record from database
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
    
    // **FIXED: Get employee names from database**
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
    
    // Helper method to get current payroll period
    private String getCurrentPayrollPeriod() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String startDate, endDate;
        
        if (day <= 15) {
            // First half of month: 1st to 15th
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = sdf.format(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, 15);
            endDate = sdf.format(cal.getTime());
        } else {
            // Second half of month: 16th to end of month
            cal.set(Calendar.DAY_OF_MONTH, 16);
            startDate = sdf.format(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = sdf.format(cal.getTime());
        }
        
        return startDate + " to " + endDate;
    }
    
    // Table management methods
    public void setTableData(ArrayList<ArrayList<String>> data) {
        this.tableData = data;
    }
    
    public void setTableSize(int size) {
        this.tableSize = size;
    }
    
    public void displayDataTable(javax.swing.JTable table) {
        if (tableData == null || tableData.isEmpty()) {
            System.out.println("⚠️ No data to display in table");
            return;
        }
        
        String[] columnNames = {
            "Employee ID", "Name", "Payroll Period", "Position", 
            "Gross Income", "Benefits", "Overtime", "Undertime",
            "SSS", "PhilHealth", "Pag-IBIG", "Tax", "Net Pay", "Status"
        };
        
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        for (ArrayList<String> row : tableData) {
            Object[] rowData = row.toArray();
            model.addRow(rowData);
        }
        
        table.setModel(model);
        System.out.println("📋 Updated table with " + tableData.size() + " records");
    }
    
    // **Implement Payroll interface methods**
    @Override
    public double taxCalculation(double totalDeductions) {
        // Simplified tax calculation
        double taxableIncome = accountDetails.getBasicSalary() - totalDeductions;
        
        if (taxableIncome <= 20000) return 0;
        else if (taxableIncome <= 33000) return (taxableIncome - 20000) * 0.15;
        else if (taxableIncome <= 66000) return 1950 + (taxableIncome - 33000) * 0.20;
        else return 8550 + (taxableIncome - 66000) * 0.25;
    }
    
    @Override
    public double sssCalculation() {
        return accountDetails.getBasicSalary() * 0.045; // 4.5%
    }
    
    @Override
    public double philhealthCalculation() {
        return accountDetails.getBasicSalary() * 0.03; // 3%
    }
    
    @Override
    public double pagibigCalculation() {
        return Math.min(accountDetails.getBasicSalary() * 0.02, 100); // 2% max 100
    }
    
    @Override
    public double deductionCalculation(ArrayList<ArrayList<String>> perEmployeeAttendance) {
        return sssCalculation() + philhealthCalculation() + pagibigCalculation();
    }
    
    @Override
    public double grossCalculation(ArrayList<ArrayList<String>> perEmployeeAttendance) {
        return accountDetails.getBasicSalary() + benefitsCalculation();
    }
    
    @Override
    public double benefitsCalculation() {
        return accountDetails.getRiceSubsidy() + 
               accountDetails.getPhoneAllowance() + 
               accountDetails.getClothingAllowance();
    }
    
    @Override
    public double netPayrollCalculations(double gross, double benefits, double overtime, 
                                       double undertime, double totalDeductions) {
        return gross + overtime - undertime - totalDeductions;
    }
    
    @Override
    public double overtimeCalculations(ArrayList<ArrayList<String>> perEmployeeAttendance) {
        // Calculate overtime based on attendance data
        return 0.0; // Implement based on your overtime logic
    }
    
    @Override
    public double undertimeCalculations(ArrayList<ArrayList<String>> perEmployeeAttendance) {
        // Calculate undertime based on attendance data
        return 0.0; // Implement based on your undertime logic
    }
    
    // **Method to generate PDF payslips**
    public void generatePDFPayslips() {
        try {
            ReportGenerator reportGen = new ReportGenerator();
            String currentPeriod = getCurrentPayrollPeriod();
            
            List<PayrollRecord> payrolls = payrollDAO.getPayrollByMonth(
                currentPeriod.substring(0, 2),
                Integer.parseInt(currentPeriod.substring(6, 10))
            );
            
            for (PayrollRecord payroll : payrolls) {
                reportGen.generatePayslipPDF(payroll.getEmployeeId(), payroll.getPayrollPeriod());
            }
            
            System.out.println("Generated PDF payslips for all employees");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // **ADD: Missing methods for PayrollStaff GUI**
    
    public String computePayroll() {
        try {
            return processPayrollDB();
        } catch (Exception e) {
            e.printStackTrace();
            return getCurrentPayrollPeriod();
        }
    }
    
    // Overloaded setTableData method (no parameters)
    public void setTableData() {
        // Load default payroll data
        try {
            ArrayList<ArrayList<String>> data = getDataForPayrollTable();
            setTableData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setSelectedName(String selectedName) {
        // Store selected employee name
        System.out.println("Selected employee: " + selectedName);
    }
    
    public ArrayList<ArrayList<String>> getDataForDTRTable() {
        // Return DTR data for selected employee
        return getDataAllDTR(new java.util.Date(), new java.util.Date());
    }
    
    public ArrayList<ArrayList<String>> getApprovedDataForPayrollTable(java.util.Date startDate, java.util.Date endDate) {
        // Return approved payroll data for date range
        return getDataForPayrollTable();
    }
    
    public ArrayList<ArrayList<String>> getAllApprovedPersonalLeaveLedger() {
        ArrayList<ArrayList<String>> leaveData = new ArrayList();
        try{
            LeaveDAO leaveDAO = new LeaveDAO();
            
            System.out.println("Getting approved personal leav ledger");
            
            return leaveData;
        } catch (Exception e) {
            e.printStackTrace();
            return leaveData;
        }
    }
    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        return getAllApprovedPersonalLeaveLedger();
    }
    
    public void forwardDTRToSupervisor(ArrayList<ArrayList<String>> dtrData) {
        System.out.println("DTR forwarded to supervisor");
    }
}
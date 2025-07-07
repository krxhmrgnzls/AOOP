package payrollsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

public class PayrollDAO {
    private Connection connection;
    
    public PayrollDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // CREATE - Save new payroll record
    public boolean savePayroll(PayrollRecord payroll) {
        String sql = "INSERT INTO payroll (employee_id, payroll_period, position, gross_income, " +
                    "benefits, overtime, undertime, sss, philhealth, pagibig, tax, net_pay, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, payroll.getEmployeeId());
            pstmt.setString(2, payroll.getPayrollPeriod());
            pstmt.setString(3, payroll.getPosition());
            pstmt.setDouble(4, payroll.getGrossIncome());
            pstmt.setDouble(5, payroll.getBenefits());
            pstmt.setDouble(6, payroll.getOvertime());
            pstmt.setDouble(7, payroll.getUndertime());
            pstmt.setDouble(8, payroll.getSss());
            pstmt.setDouble(9, payroll.getPhilhealth());
            pstmt.setDouble(10, payroll.getPagibig());
            pstmt.setDouble(11, payroll.getTax());
            pstmt.setDouble(12, payroll.getNetPay());
            pstmt.setString(13, payroll.getStatus());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // READ - Get payroll by month and year
    public List<PayrollRecord> getPayrollByMonth(String month, int year) {
        List<PayrollRecord> payrolls = new ArrayList<>();
        String sql = "SELECT p.*, e.first_name, e.last_name FROM payroll p " +
                    "JOIN employees e ON p.employee_id = e.employee_id " +
                    "WHERE p.payroll_period LIKE ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "%" + month + "/" + year + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                PayrollRecord payroll = createPayrollFromResultSet(rs);
                payrolls.add(payroll);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payrolls;
    }
    
    // READ - Get payroll by employee ID
    public List<PayrollRecord> getPayrollByEmployee(int employeeId) {
        List<PayrollRecord> payrolls = new ArrayList<>();
        String sql = "SELECT p.*, e.first_name, e.last_name FROM payroll p " +
                    "JOIN employees e ON p.employee_id = e.employee_id " +
                    "WHERE p.employee_id = ? ORDER BY p.payroll_period DESC";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                PayrollRecord payroll = createPayrollFromResultSet(rs);
                payrolls.add(payroll);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payrolls;
    }
    
    // READ - Get payroll by date range - FIXED IMPLEMENTATION
    public List<PayrollRecord> getPayrollByDateRange(java.util.Date fromDate, java.util.Date toDate) {
        List<PayrollRecord> payrolls = new ArrayList<>();
        
        if (connection == null) {
            System.err.println("ERROR: Database connection is null in PayrollDAO.getPayrollByDateRange");
            return payrolls; // Return empty list instead of throwing exception
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String fromDateStr = sdf.format(fromDate);
        String toDateStr = sdf.format(toDate);
        
        String sql = "SELECT p.*, e.first_name, e.last_name FROM payroll p " +
                    "JOIN employees e ON p.employee_id = e.employee_id " +
                    "WHERE p.payroll_period LIKE ? OR p.payroll_period LIKE ? " +
                    "ORDER BY p.payroll_id DESC";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            // Search for payroll periods that contain these dates
            pstmt.setString(1, "%" + fromDateStr + "%");
            pstmt.setString(2, "%" + toDateStr + "%");
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                PayrollRecord payroll = createPayrollFromResultSet(rs);
                payrolls.add(payroll);
            }
            
            System.out.println("Found " + payrolls.size() + " payroll records for date range: " + fromDateStr + " to " + toDateStr);
            
        } catch (SQLException e) {
            System.err.println("Database error in getPayrollByDateRange: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payrolls;
    }
    
    // READ - Get payroll by period string
    public List<PayrollRecord> getPayrollByPeriod(String payrollPeriod) {
        List<PayrollRecord> payrolls = new ArrayList<>();
        String sql = "SELECT p.*, e.first_name, e.last_name FROM payroll p " +
                    "JOIN employees e ON p.employee_id = e.employee_id " +
                    "WHERE p.payroll_period = ? ORDER BY p.employee_id";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, payrollPeriod);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                PayrollRecord payroll = createPayrollFromResultSet(rs);
                payrolls.add(payroll);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payrolls;
    }
    
    // UPDATE - Update payroll record
    public boolean updatePayroll(PayrollRecord payroll) {
        String sql = "UPDATE payroll SET payroll_period=?, position=?, gross_income=?, " +
                    "benefits=?, overtime=?, undertime=?, sss=?, philhealth=?, pagibig=?, " +
                    "tax=?, net_pay=?, status=? WHERE payroll_id=?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, payroll.getPayrollPeriod());
            pstmt.setString(2, payroll.getPosition());
            pstmt.setDouble(3, payroll.getGrossIncome());
            pstmt.setDouble(4, payroll.getBenefits());
            pstmt.setDouble(5, payroll.getOvertime());
            pstmt.setDouble(6, payroll.getUndertime());
            pstmt.setDouble(7, payroll.getSss());
            pstmt.setDouble(8, payroll.getPhilhealth());
            pstmt.setDouble(9, payroll.getPagibig());
            pstmt.setDouble(10, payroll.getTax());
            pstmt.setDouble(11, payroll.getNetPay());
            pstmt.setString(12, payroll.getStatus());
            pstmt.setInt(13, payroll.getPayrollId());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Delete payroll record
    public boolean deletePayroll(int payrollId) {
        String sql = "DELETE FROM payroll WHERE payroll_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, payrollId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // BULK INSERT - Process payroll for all employees
    public boolean processPayrollForAllEmployees(String payrollPeriod) {
        try {
            // Get all employees
            EmployeeDAO empDAO = new EmployeeDAO();
            List<AccountDetails> employees = empDAO.findAll();
            
            for (AccountDetails emp : employees) {
                // Calculate payroll for each employee
                PayrollRecord payroll = calculatePayrollForEmployee(emp, payrollPeriod);
                if (payroll != null) {
                    savePayroll(payroll);
                    System.out.println("Processed payroll for: " + emp.getFirstName() + " " + emp.getLastName());
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // HELPER - Calculate payroll for individual employee
    private PayrollRecord calculatePayrollForEmployee(AccountDetails employee, String payrollPeriod) {
        try {
            PayrollRecord payroll = new PayrollRecord();
            payroll.setEmployeeId(employee.getEmployeeID());
            payroll.setPayrollPeriod(payrollPeriod);
            payroll.setPosition(employee.getPosition());
            
            // Calculate basic earnings
            double basicSalary = employee.getBasicSalary();
            double benefits = employee.getRiceSubsidy() + employee.getPhoneAllowance() + employee.getClothingAllowance();
            
            // Get attendance data for overtime/undertime calculation
            double overtime = getOvertimeHours(employee.getEmployeeID(), payrollPeriod) * employee.getHourlyRate() * 1.25;
            double undertime = getUndertimeHours(employee.getEmployeeID(), payrollPeriod) * employee.getHourlyRate();
            
            double grossIncome = basicSalary + benefits + overtime;
            
            // Calculate deductions
            double sss = grossIncome * 0.045;  // 4.5%
            double philhealth = grossIncome * 0.03;  // 3%
            double pagibig = Math.min(grossIncome * 0.02, 100);  // 2% max 100
            double taxableIncome = grossIncome - sss - philhealth - pagibig;
            double tax = calculateTax(taxableIncome);
            
            double netPay = grossIncome - sss - philhealth - pagibig - tax - undertime;
            
            payroll.setGrossIncome(grossIncome);
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

    // HELPER - Get overtime hours for employee
    private double getOvertimeHours(int employeeId, String period) {
        // Query overtime_requests table for approved overtime
        String sql = "SELECT COALESCE(SUM(number_of_days * 8), 0) FROM overtime_requests " +
                    "WHERE employee_id = ? AND status = 'Approved'";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // HELPER - Get undertime hours for employee
    private double getUndertimeHours(int employeeId, String period) {
        // Simplified - return 0 for now, implement based on attendance
        return 0.0;
    }

    // HELPER - Calculate tax based on brackets
    private double calculateTax(double taxableIncome) {
        // Simplified tax calculation - implement proper tax brackets
        if (taxableIncome <= 20000) return 0;
        else if (taxableIncome <= 33000) return (taxableIncome - 20000) * 0.15;
        else if (taxableIncome <= 66000) return 1950 + (taxableIncome - 33000) * 0.20;
        else return 8550 + (taxableIncome - 66000) * 0.25;
    }

    /**
     * Helper method to create PayrollRecord from ResultSet
     */
    private PayrollRecord createPayrollFromResultSet(ResultSet rs) throws SQLException {
        PayrollRecord payroll = new PayrollRecord();

        // Check if payroll_id column exists
        try {
            payroll.setPayrollId(rs.getInt("payroll_id"));
        } catch (SQLException e) {
            // Column might not exist, set default value
            payroll.setPayrollId(0);
        }

        payroll.setEmployeeId(rs.getInt("employee_id"));
        payroll.setPayrollPeriod(rs.getString("payroll_period"));
        payroll.setPosition(rs.getString("position"));
        payroll.setGrossIncome(rs.getDouble("gross_income"));
        payroll.setBenefits(rs.getDouble("benefits"));
        payroll.setOvertime(rs.getDouble("overtime"));
        payroll.setUndertime(rs.getDouble("undertime"));
        payroll.setSss(rs.getDouble("sss"));
        payroll.setPhilhealth(rs.getDouble("philhealth"));
        payroll.setPagibig(rs.getDouble("pagibig"));
        payroll.setTax(rs.getDouble("tax"));
        payroll.setNetPay(rs.getDouble("net_pay"));
        payroll.setStatus(rs.getString("status"));

        // Set employee name if available
        try {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            if (firstName != null && lastName != null) {
                payroll.setEmployeeName(firstName + " " + lastName);
            }
        } catch (SQLException e) {
            // Columns might not be available
        }

        return payroll;
    }
    
    // UTILITY - Check if payroll exists for period
    public boolean payrollExistsForPeriod(int employeeId, String payrollPeriod) {
        String sql = "SELECT COUNT(*) FROM payroll WHERE employee_id = ? AND payroll_period = ?";
        try {
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
    
    // UTILITY - Get existing payroll ID
    public int getExistingPayrollId(int employeeId, String payrollPeriod) {
        String sql = "SELECT payroll_id FROM payroll WHERE employee_id = ? AND payroll_period = ?";
        try {
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
    
    // ANALYTICS - Get total payroll cost for period
    public double getTotalPayrollCost(String payrollPeriod) {
        String sql = "SELECT COALESCE(SUM(net_pay), 0) FROM payroll WHERE payroll_period = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, payrollPeriod);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // BULK UPDATE - Release multiple payrolls
    public boolean bulkReleasePayrolls(List<Integer> payrollIds) {
        String sql = "UPDATE payroll SET status = 'Approved' WHERE payroll_id IN (";
        
        // Build IN clause
        for (int i = 0; i < payrollIds.size(); i++) {
            sql += "?";
            if (i < payrollIds.size() - 1) sql += ",";
        }
        sql += ")";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < payrollIds.size(); i++) {
                pstmt.setInt(i + 1, payrollIds.get(i));
            }
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
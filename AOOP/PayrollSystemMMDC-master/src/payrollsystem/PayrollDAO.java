package payrollsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {
    private Connection connection;
    
    public PayrollDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Save payroll record
    public boolean savePayroll(PayrollRecord payroll) {
        String sql = "INSERT INTO payroll (employee_id, payroll_period, position, " +
                    "gross_income, benefits, overtime, undertime, sss, philhealth, " +
                    "pagibig, tax, net_pay, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get payroll by employee ID
    public List<PayrollRecord> getPayrollByEmployee(int employeeId) {
        List<PayrollRecord> payrollList = new ArrayList<>();
        String sql = "SELECT * FROM payroll WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                PayrollRecord payroll = new PayrollRecord();
                payroll.setPayrollDetails(rs);
                payrollList.add(payroll);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payrollList;
    }
    
    // Update payroll status
    public boolean updatePayrollStatus(int payrollId, String status) {
        String sql = "UPDATE payroll SET status = ? WHERE payroll_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, payrollId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
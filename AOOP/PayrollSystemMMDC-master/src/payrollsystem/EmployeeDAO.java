package payrollsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private Connection connection;
    
    public EmployeeDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // CREATE - Add new employee
    public boolean create(Employee employee) {
        String sql = "INSERT INTO employees (last_name, first_name, birthday, address, " +
                    "phone_number, sss_number, philhealth_number, tin_number, pagibig_number, " +
                    "status, position, immediate_supervisor, basic_salary, rice_subsidy, " +
                    "phone_allowance, clothing_allowance, gross_semi_monthly_rate, hourly_rate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employee.getLastName());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setString(3, employee.getBirthday());
            pstmt.setString(4, employee.getAddress());
            pstmt.setString(5, employee.getPhoneNumber());
            pstmt.setString(6, employee.getSssNumber());
            pstmt.setString(7, employee.getPhilHealthNumber());
            pstmt.setString(8, employee.getTinNumber());
            pstmt.setString(9, employee.getPagibigNumber());
            pstmt.setString(10, employee.getStatus());
            pstmt.setString(11, employee.getPosition());
            pstmt.setString(12, employee.getSupervisor());
            pstmt.setDouble(13, employee.getBasicSalary());
            pstmt.setDouble(14, employee.getRiceSubsidy());
            pstmt.setDouble(15, employee.getPhoneAllowance());
            pstmt.setDouble(16, employee.getClothingAllowance());
            pstmt.setDouble(17, employee.getSemiBasicSalary());
            pstmt.setDouble(18, employee.getHourlyRate());
            pstmt.setInt(19, Integer.parseInt(employee.getEmployeeID()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Remove employee
    public boolean delete(int employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Find all employees
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(String.valueOf(rs.getInt("employee_id")));
                employee.accountDetails.setEmployeeDetails(rs);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    
}

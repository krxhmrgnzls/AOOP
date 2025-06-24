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
    public boolean create(AccountDetails employee) {
        String sql = "INSERT INTO employees (employee_id, last_name, first_name, birthday, address, " +
                    "phone_number, sss_number, philhealth_number, tin_number, pagibig_number, " +
                    "status, position, supervisor_id, basic_salary, rice_subsidy, phone_allowance, " +
                    "clothing_allowance, gross_rate, hourly_rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employee.getEmployeeID());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getFirstName());
            pstmt.setDate(4, new java.sql.Date(employee.getBirthday().getTime()));
            pstmt.setString(5, employee.getAddress());
            pstmt.setString(6, employee.getPhoneNumber());
            pstmt.setString(7, employee.getSSSNumber());
            pstmt.setString(8, employee.getPhilHealthNumber());
            pstmt.setString(9, employee.getTINNumber());
            pstmt.setString(10, employee.getPagibigNumber());
            pstmt.setString(11, employee.getStatus());
            pstmt.setString(12, employee.getPosition());
            pstmt.setInt(13, 0); // supervisor_id - set default or get from employee
            pstmt.setDouble(14, employee.getBasicSalary());
            pstmt.setDouble(15, employee.getRiceSubsidy());
            pstmt.setDouble(16, employee.getPhoneAllowance());
            pstmt.setDouble(17, employee.getClothingAllowance());
            pstmt.setDouble(18, employee.getSemiMonthlyRate());
            pstmt.setDouble(19, employee.getHourlyRate());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // READ - Get employee by ID
    public AccountDetails read(int employeeId) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                AccountDetails employee = new AccountDetails();
                employee.setEmployeeID(rs.getInt("employee_id"));
                employee.setLastName(rs.getString("last_name"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setBirthday(rs.getDate("birthday"));
                employee.setAddress(rs.getString("address"));
                employee.setPhoneNumber(rs.getString("phone_number"));
                employee.setSSSNumber(rs.getString("sss_number"));
                employee.setPhilHealthNumber(rs.getString("philhealth_number"));
                employee.setTINNumber(rs.getString("tin_number"));
                employee.setPagibigNumber(rs.getString("pagibig_number"));
                employee.setStatus(rs.getString("status"));
                employee.setPosition(rs.getString("position"));
                employee.setBasicSalary(rs.getDouble("basic_salary"));
                employee.setRiceSubsidy(rs.getDouble("rice_subsidy"));
                employee.setPhoneAllowance(rs.getDouble("phone_allowance"));
                employee.setClothingAllowance(rs.getDouble("clothing_allowance"));
                employee.setSemiMonthlyRate(rs.getDouble("gross_rate"));
                employee.setHourlyRate(rs.getDouble("hourly_rate"));
                return employee;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // UPDATE - Update employee
    public boolean update(AccountDetails employee) {
        String sql = "UPDATE employees SET last_name=?, first_name=?, birthday=?, address=?, " +
                    "phone_number=?, sss_number=?, philhealth_number=?, tin_number=?, pagibig_number=?, " +
                    "status=?, position=?, basic_salary=?, rice_subsidy=?, phone_allowance=?, " +
                    "clothing_allowance=?, gross_rate=?, hourly_rate=? WHERE employee_id=?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, employee.getLastName());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setDate(3, new java.sql.Date(employee.getBirthday().getTime()));
            pstmt.setString(4, employee.getAddress());
            pstmt.setString(5, employee.getPhoneNumber());
            pstmt.setString(6, employee.getSSSNumber());
            pstmt.setString(7, employee.getPhilHealthNumber());
            pstmt.setString(8, employee.getTINNumber());
            pstmt.setString(9, employee.getPagibigNumber());
            pstmt.setString(10, employee.getStatus());
            pstmt.setString(11, employee.getPosition());
            pstmt.setDouble(12, employee.getBasicSalary());
            pstmt.setDouble(13, employee.getRiceSubsidy());
            pstmt.setDouble(14, employee.getPhoneAllowance());
            pstmt.setDouble(15, employee.getClothingAllowance());
            pstmt.setDouble(16, employee.getSemiMonthlyRate());
            pstmt.setDouble(17, employee.getHourlyRate());
            pstmt.setInt(18, employee.getEmployeeID());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Delete employee
    public boolean delete(int employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // FIND ALL - Get all employees
    public List<AccountDetails> findAll() {
        List<AccountDetails> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY employee_id";
        
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                AccountDetails employee = new AccountDetails();
                employee.setEmployeeID(rs.getInt("employee_id"));
                employee.setLastName(rs.getString("last_name"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setBirthday(rs.getDate("birthday"));
                employee.setAddress(rs.getString("address"));
                employee.setPhoneNumber(rs.getString("phone_number"));
                employee.setSSSNumber(rs.getString("sss_number"));
                employee.setPhilHealthNumber(rs.getString("philhealth_number"));
                employee.setTINNumber(rs.getString("tin_number"));
                employee.setPagibigNumber(rs.getString("pagibig_number"));
                employee.setStatus(rs.getString("status"));
                employee.setPosition(rs.getString("position"));
                employee.setBasicSalary(rs.getDouble("basic_salary"));
                employee.setRiceSubsidy(rs.getDouble("rice_subsidy"));
                employee.setPhoneAllowance(rs.getDouble("phone_allowance"));
                employee.setClothingAllowance(rs.getDouble("clothing_allowance"));
                employee.setSemiMonthlyRate(rs.getDouble("gross_rate"));
                employee.setHourlyRate(rs.getDouble("hourly_rate"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    
    // SEARCH - Search employees by name or position
    public List<AccountDetails> searchEmployees(String keyword) {
        List<AccountDetails> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE first_name LIKE ? OR last_name LIKE ? OR position LIKE ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                AccountDetails employee = new AccountDetails();
                employee.setEmployeeID(rs.getInt("employee_id"));
                employee.setLastName(rs.getString("last_name"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setPosition(rs.getString("position"));
                employee.setBasicSalary(rs.getDouble("basic_salary"));
                // Add other fields as needed
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
}
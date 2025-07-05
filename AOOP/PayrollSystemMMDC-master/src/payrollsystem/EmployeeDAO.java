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
            pstmt.setDate(4, employee.getBirthday() != null ? new java.sql.Date(employee.getBirthday().getTime()) : null);
            pstmt.setString(5, employee.getAddress());
            pstmt.setString(6, employee.getPhoneNumber());
            pstmt.setString(7, employee.getSSSNumber());
            pstmt.setString(8, employee.getPhilHealthNumber());
            pstmt.setString(9, employee.getTinNumber());
            pstmt.setString(10, employee.getPagibigNumber());
            pstmt.setString(11, employee.getStatus());
            pstmt.setString(12, employee.getPosition());
            pstmt.setInt(13, 0); // supervisor_id - set default
            pstmt.setDouble(14, employee.getBasicSalary());
            pstmt.setDouble(15, employee.getRiceSubsidy());
            pstmt.setDouble(16, employee.getPhoneAllowance());
            pstmt.setDouble(17, employee.getClothingAllowance());
            pstmt.setDouble(18, employee.getSemiBasicSalary());
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
                AccountDetails emp = new AccountDetails();
                emp.setEmployeeID(rs.getInt("employee_id"));
                emp.setLastName(rs.getString("last_name"));
                emp.setFirstName(rs.getString("first_name"));
                emp.setBirthday(rs.getDate("birthday"));
                emp.setAddress(rs.getString("address"));
                emp.setPhoneNumber(rs.getString("phone_number"));
                emp.setSSSNumber(rs.getString("sss_number"));
                emp.setPhilHealthNumber(rs.getString("philhealth_number"));
                emp.setTinNumber(rs.getString("tin_number"));
                emp.setPagibigNumber(rs.getString("pagibig_number"));
                emp.setStatus(rs.getString("status"));
                emp.setPosition(rs.getString("position"));
                emp.setBasicSalary(rs.getDouble("basic_salary"));
                emp.setRiceSubsidy(rs.getDouble("rice_subsidy"));
                emp.setPhoneAllowance(rs.getDouble("phone_allowance"));
                emp.setClothingAllowance(rs.getDouble("clothing_allowance"));
                emp.setSemiBasicSalary(rs.getDouble("gross_rate"));
                emp.setHourlyRate(rs.getDouble("hourly_rate"));
                
                return emp;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // UPDATE - Update employee information
    public boolean update(AccountDetails employee) {
        String sql = "UPDATE employees SET last_name=?, first_name=?, birthday=?, address=?, " +
                    "phone_number=?, sss_number=?, philhealth_number=?, tin_number=?, " +
                    "pagibig_number=?, status=?, position=?, basic_salary=?, rice_subsidy=?, " +
                    "phone_allowance=?, clothing_allowance=?, gross_rate=?, hourly_rate=? " +
                    "WHERE employee_id=?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, employee.getLastName());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setDate(3, employee.getBirthday() != null ? new java.sql.Date(employee.getBirthday().getTime()) : null);
            pstmt.setString(4, employee.getAddress());
            pstmt.setString(5, employee.getPhoneNumber());
            pstmt.setString(6, employee.getSSSNumber());
            pstmt.setString(7, employee.getPhilHealthNumber());
            pstmt.setString(8, employee.getTinNumber());
            pstmt.setString(9, employee.getPagibigNumber());
            pstmt.setString(10, employee.getStatus());
            pstmt.setString(11, employee.getPosition());
            pstmt.setDouble(12, employee.getBasicSalary());
            pstmt.setDouble(13, employee.getRiceSubsidy());
            pstmt.setDouble(14, employee.getPhoneAllowance());
            pstmt.setDouble(15, employee.getClothingAllowance());
            pstmt.setDouble(16, employee.getBasicSalary());
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
    
    // Get all employees - matches your existing code expectations
    public List<AccountDetails> findAll() {
        List<AccountDetails> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY employee_id";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                AccountDetails emp = new AccountDetails();
                emp.setEmployeeID(rs.getInt("employee_id"));
                emp.setLastName(rs.getString("last_name"));
                emp.setFirstName(rs.getString("first_name"));
                emp.setBirthday(rs.getDate("birthday"));
                emp.setAddress(rs.getString("address"));
                emp.setPhoneNumber(rs.getString("phone_number"));
                emp.setSSSNumber(rs.getString("sss_number"));
                emp.setPhilHealthNumber(rs.getString("philhealth_number"));
                emp.setTinNumber(rs.getString("tin_number"));
                emp.setPagibigNumber(rs.getString("pagibig_number"));
                emp.setStatus(rs.getString("status"));
                emp.setPosition(rs.getString("position"));
                emp.setBasicSalary(rs.getDouble("basic_salary"));
                emp.setRiceSubsidy(rs.getDouble("rice_subsidy"));
                emp.setPhoneAllowance(rs.getDouble("phone_allowance"));
                emp.setClothingAllowance(rs.getDouble("clothing_allowance"));
                emp.setBasicSalary(rs.getDouble("gross_rate"));
                emp.setHourlyRate(rs.getDouble("hourly_rate"));
                
                employees.add(emp);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return employees;
    }
    
    // Find employee by ID
    public AccountDetails findById(int employeeId) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                AccountDetails emp = new AccountDetails();
                emp.setEmployeeID(rs.getInt("employee_id"));
                emp.setLastName(rs.getString("last_name"));
                emp.setFirstName(rs.getString("first_name"));
                emp.setBirthday(rs.getDate("birthday"));
                emp.setAddress(rs.getString("address"));
                emp.setPhoneNumber(rs.getString("phone_number"));
                emp.setSSSNumber(rs.getString("sss_number"));
                emp.setPhilHealthNumber(rs.getString("philhealth_number"));
                emp.setTinNumber(rs.getString("tin_number"));
                emp.setPagibigNumber(rs.getString("pagibig_number"));
                emp.setStatus(rs.getString("status"));
                emp.setPosition(rs.getString("position"));
                emp.setBasicSalary(rs.getDouble("basic_salary"));
                emp.setRiceSubsidy(rs.getDouble("rice_subsidy"));
                emp.setPhoneAllowance(rs.getDouble("phone_allowance"));
                emp.setClothingAllowance(rs.getDouble("clothing_allowance"));
                emp.setSemiBasicSalary(rs.getDouble("gross_rate"));
                emp.setHourlyRate(rs.getDouble("hourly_rate"));
                
                return emp;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Update employee information
    public boolean updateEmployee(AccountDetails employee) {
        String sql = "UPDATE employees SET last_name=?, first_name=?, birthday=?, address=?, " +
                    "phone_number=?, sss_number=?, philhealth_number=?, tin_number=?, " +
                    "pagibig_number=?, status=?, position=?, basic_salary=?, rice_subsidy=?, " +
                    "phone_allowance=?, clothing_allowance=?, gross_rate=?, hourly_rate=? " +
                    "WHERE employee_id=?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, employee.getLastName());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setDate(3, new java.sql.Date(employee.getBirthday().getTime()));
            pstmt.setString(4, employee.getAddress());
            pstmt.setString(5, employee.getPhoneNumber());
            pstmt.setString(6, employee.getSSSNumber());
            pstmt.setString(7, employee.getPhilHealthNumber());
            pstmt.setString(8, employee.getTinNumber());
            pstmt.setString(9, employee.getPagibigNumber());
            pstmt.setString(10, employee.getStatus());
            pstmt.setString(11, employee.getPosition());
            pstmt.setDouble(12, employee.getBasicSalary());
            pstmt.setDouble(13, employee.getRiceSubsidy());
            pstmt.setDouble(14, employee.getPhoneAllowance());
            pstmt.setDouble(15, employee.getClothingAllowance());
            pstmt.setDouble(16, employee.getSemiBasicSalary());
            pstmt.setDouble(17, employee.getHourlyRate());
            pstmt.setInt(18, employee.getEmployeeID());
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get employees for dropdown lists (ID and Name)
    public ArrayList<String[]> getEmployeeList() {
        ArrayList<String[]> empList = new ArrayList<>();
        String sql = "SELECT employee_id, first_name, last_name FROM employees ORDER BY last_name, first_name";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String[] emp = new String[2];
                emp[0] = String.valueOf(rs.getInt("employee_id"));
                emp[1] = rs.getString("first_name") + " " + rs.getString("last_name");
                empList.add(emp);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return empList;
    }

    List<AccountDetails> searchEmployees(String keyword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
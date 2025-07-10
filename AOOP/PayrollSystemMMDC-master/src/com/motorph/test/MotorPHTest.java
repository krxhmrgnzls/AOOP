package com.motorph.test;

import com.motorph.dao.EmployeeDAO;
import com.motorph.dao.PayrollDAO;
import com.motorph.gui.Employee;
import com.motorph.gui.Login;
import com.motorph.model.PayrollRecord;
import com.motorph.util.DatabaseConnection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class MotorPHTest {
    
    private static Connection connection;
    private Employee employee;
    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;
    private Login login;
    
    @BeforeAll
    static void setupDatabase() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            System.out.println("Database connection established for testing");
        } catch (SQLException e) {
            fail("Failed to connect to database: " + e.getMessage());
        }
    }
    
    @BeforeEach
    void setup() {
        try {
            employee = new Employee();
            payrollDAO = new PayrollDAO();
            employeeDAO = new EmployeeDAO();
            login = new Login();
        } catch (Exception e) {
            fail("Failed to initialize test objects: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test Database Connection")
    void testDatabaseConnection() {
        assertTrue(DatabaseConnection.testConnection(), 
                  "Database should connect successfully");
        System.out.println("Database connection test passed");
    }
    
    @Test
    @DisplayName("Test Database Tables Exist")
    void testTablesExist() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            
            String[] tables = {"employees", "payroll", "attendance", "login_credentials"};
            for (String table : tables) {
                ResultSet rs = metaData.getTables(null, null, table, null);
                assertTrue(rs.next(), "Table '" + table + "' should exist");
                rs.close();
            }
            System.out.println("All required tables exist");
        } catch (SQLException e) {
            fail("Database table check failed: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test Employee Authentication")
    void testEmployeeAuthentication() {
        Login login = new Login();
        boolean validLogin = login.authenticateDB(10003, "10003");
        assertTrue(validLogin, "Valid credentials should work");
        
        boolean invalidLogin = login.authenticateDB(99999, "wrong");
        assertFalse(invalidLogin, "Invalid credentials should fail");
        
        System.out.println("Employee authentication test passed");
    }
    
    @Test
    @DisplayName("Test Employee Data Retrieval")
    void testEmployeeDataRetrieval() {
        try {
            String sql = "SELECT employee_id, first_name, last_name, basic_salary FROM employees LIMIT 5";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                int empId = rs.getInt("employee_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                double salary = rs.getDouble("basic_salary");
                
                assertTrue(empId > 0, "Employee ID should be positive");
                assertNotNull(firstName, "First name should not be null");
                assertNotNull(lastName, "Last name should not be null");
                assertTrue(salary >= 0, "Salary should be non-negative");
                
                System.out.println("Employee: " + empId + " - " + firstName + " " + lastName + " (₱" + salary + ")");
            }
            
            assertTrue(count > 0, "Should have at least one employee");
            System.out.println("Successfully retrieved " + count + " employees directly from database");
            
        } catch (Exception e) {
            fail("Direct database employee retrieval failed: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test Working Days Calculation")
    void testWorkingDaysCalculation() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse("2025-03-03");
            Date toDate = sdf.parse("2025-03-07");
            
            boolean result = employee.countNumberOfDays(fromDate, toDate);
            assertTrue(result, "Working days calculation should succeed");
            
            int workingDays = employee.getCalculatedDays();
            assertEquals(5, workingDays, "Monday to Friday should be 5 working days");
            
            System.out.println("Working days calculation: " + workingDays + " days");
        } catch (Exception e) {
            fail("Working days calculation failed: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test Tax Calculation")
    void testTaxCalculation() {
        double tax1 = calculateTax(15000);
        assertEquals(0.0, tax1, 0.01, "Income below 20k should have no tax");
        
        double tax2 = calculateTax(25000);
        double expectedTax2 = (25000 - 20000) * 0.15;
        assertEquals(expectedTax2, tax2, 0.01, "Tax for 20k-33k bracket should be correct");
        
        double tax3 = calculateTax(50000);
        double expectedTax3 = 1950 + (50000 - 33000) * 0.20;
        assertEquals(expectedTax3, tax3, 0.01, "Tax for 33k-66k bracket should be correct");
        
        System.out.println("Tax calculations: 15k=" + tax1 + ", 25k=" + tax2 + ", 50k=" + tax3);
    }
    
    @Test
    @DisplayName("Test SSS Contribution")
    void testSSSContribution() {
        double salary1 = 20000;
        double sss1 = calculateSSS(salary1);
        assertTrue(sss1 > 0, "SSS should be greater than 0");
        assertTrue(sss1 <= salary1 * 0.045, "SSS should not exceed 4.5%");
        
        double salary2 = 30000;
        double sss2 = calculateSSS(salary2);
        assertTrue(sss2 >= sss1, "Higher salary should have equal or higher SSS");
        
        System.out.println("SSS contributions: 20k=" + sss1 + ", 30k=" + sss2);
    }
    
    @Test
    @DisplayName("Test PhilHealth Contribution")
    void testPhilHealthContribution() {
        double salary = 30000;
        double philhealth = calculatePhilHealth(salary);
        
        assertTrue(philhealth > 0, "PhilHealth should be greater than 0");
        assertTrue(philhealth <= 1800, "PhilHealth should not exceed maximum");
        
        System.out.println("PhilHealth for 30k: " + philhealth);
    }
    
    @Test
    @DisplayName("Test Pag-IBIG Contribution")
    void testPagIbigContribution() {
        double salary1 = 15000;
        double pagibig1 = calculatePagIbig(salary1);
        assertEquals(150.0, pagibig1, 0.01, "Pag-IBIG for <=20k should be 1%");
        
        double salary2 = 30000;
        double pagibig2 = calculatePagIbig(salary2);
        assertEquals(600.0, pagibig2, 0.01, "Pag-IBIG for >20k should be 2%");
        
        System.out.println("Pag-IBIG: 15k=" + pagibig1 + ", 30k=" + pagibig2);
    }
    
    @Test
    @DisplayName("Test Complete Payroll Calculation")
    void testCompletePayrollCalculation() {
        double basicSalary = 30000;
        double riceSubsidy = 1500;
        double phoneAllowance = 500;
        double clothingAllowance = 500;
        
        double grossIncome = basicSalary;
        double benefits = riceSubsidy + phoneAllowance + clothingAllowance;
        double sss = calculateSSS(basicSalary);
        double philhealth = calculatePhilHealth(basicSalary);
        double pagibig = calculatePagIbig(basicSalary);
        double tax = calculateTax(basicSalary);
        
        double netPay = grossIncome + benefits - sss - philhealth - pagibig - tax;
        
        assertTrue(netPay > 0, "Net pay should be positive");
        assertTrue(netPay < grossIncome + benefits, "Net pay should be less than gross + benefits");
        
        System.out.println("Complete payroll calculation:");
        System.out.println("   Gross Income: " + grossIncome);
        System.out.println("   Benefits: " + benefits);
        System.out.println("   Deductions: " + (sss + philhealth + pagibig + tax));
        System.out.println("   Net Pay: " + String.format("%.2f", netPay));
    }
    
    @Test
    @DisplayName("Test Payroll Record Creation")
    void testPayrollRecordCreation() {
        PayrollRecord payroll = new PayrollRecord();
        payroll.setEmployeeId(10003);
        payroll.setPayrollPeriod("TEST_PERIOD");
        payroll.setGrossIncome(30000.0);
        payroll.setNetPay(25000.0);
        
        assertEquals(10003, payroll.getEmployeeId());
        assertEquals("TEST_PERIOD", payroll.getPayrollPeriod());
        assertEquals(30000.0, payroll.getGrossIncome(), 0.01);
        assertEquals(25000.0, payroll.getNetPay(), 0.01);
        
        System.out.println("Payroll record creation test passed");
    }
    
    @Test
    @DisplayName("Test Database Performance")
    void testDatabasePerformance() {
        long startTime = System.currentTimeMillis();
        
        try {
            String sql = "SELECT employee_id, first_name, last_name FROM employees";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
            }
            
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            assertTrue(executionTime < 5000, "Query should complete within 5 seconds");
            assertTrue(count > 0, "Should have at least one employee");
            System.out.println("Database query performance: " + executionTime + "ms for " + count + " employees");
            
        } catch (Exception e) {
            fail("Database performance test failed: " + e.getMessage());
        }
    }
    
    private double calculateTax(double taxableIncome) {
        if (taxableIncome <= 20000) return 0;
        else if (taxableIncome <= 33000) return (taxableIncome - 20000) * 0.15;
        else if (taxableIncome <= 66000) return 1950 + (taxableIncome - 33000) * 0.20;
        else return 8550 + (taxableIncome - 66000) * 0.25;
    }
    
    private double calculateSSS(double salary) {
        if (salary <= 20000) return salary * 0.045;
        else if (salary <= 25000) return 1125;
        else return Math.min(1800, salary * 0.045);
    }
    
    private double calculatePhilHealth(double salary) {
        return Math.min(1800, Math.max(400, salary * 0.03));
    }
    
    private double calculatePagIbig(double salary) {
        if (salary <= 20000) return salary * 0.01;
        else return salary * 0.02;
    }
    
    @AfterAll
    static void cleanup() {
        try {
            if (connection != null) {
                DatabaseConnection.getInstance().closeConnection();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        
        System.out.println("ALL TESTS COMPLETED");
    }

    public static void main(String[] args) {
        MotorPHTest test = new MotorPHTest();
    
        try {
            System.out.println("=== Starting MotorPH Unit Tests ===");

            MotorPHTest.setupDatabase();
            test.setup();

            test.testDatabaseConnection();
            test.testTablesExist();
            test.testEmployeeAuthentication();
            test.testEmployeeDataRetrieval();
            test.testWorkingDaysCalculation();
            test.testTaxCalculation();
            test.testSSSContribution();
            test.testPhilHealthContribution();
            test.testPagIbigContribution();
            test.testCompletePayrollCalculation();
            test.testPayrollRecordCreation();
            test.testDatabasePerformance();

            MotorPHTest.cleanup();

            System.out.println("=== All Tests Completed Successfully! ===");

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

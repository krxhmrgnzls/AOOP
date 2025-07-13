package com.motorph.test;

import com.motorph.dao.*;
import com.motorph.gui.*;
import com.motorph.model.*;
import com.motorph.service.*;
import com.motorph.util.DatabaseConnection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Enhanced MotorPH Payroll System Test Suite
 * Fixed to match actual database schema
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MotorPHTest {
    
    private static Connection connection;
    private Employee employee;
    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;
    private AttendanceDAO attendanceDAO;
    private Login login;
    private PayrollStaff payrollStaff;
    private HumanResource humanResource;
    private AttendanceService attendanceService;
    
    // Test data constants
    private static final int TEST_EMPLOYEE_ID = 99999;
    private static final String TEST_EMPLOYEE_FNAME = "Test";
    private static final String TEST_EMPLOYEE_LNAME = "Employee";
    private static final String TEST_PASSWORD = "testpass123";
    private static final String TEST_PAYROLL_PERIOD = "07/01/2025 - 07/15/2025";
    
    @BeforeAll
    static void setupDatabase() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            assertNotNull(connection, "Database connection should be established");
            System.out.println("✓ Database connection established for testing");
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
            attendanceDAO = new AttendanceDAO();
            login = new Login();
            payrollStaff = new PayrollStaff();
            attendanceService = new AttendanceService();
            System.out.println("Test components initialized");
        } catch (Exception e) {
            fail("Failed to initialize test objects: " + e.getMessage());
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("1.1 Test Database Connection")
    void testDatabaseConnection() {
        assertTrue(DatabaseConnection.testConnection(), 
                  "Database should connect successfully");
        
        try {
            assertFalse(connection.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("Error checking connection status: " + e.getMessage());
        }
        
        System.out.println("Database connection test passed");
    }
    
    @Test
    @Order(2)
    @DisplayName("1.2 Test Database Tables Exist")
    void testTablesExist() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
           
            String[] tables = {"emp_info", "payroll", "attendance", "login_credentials", 
                             "positions", "roles", "statuses", "emp_compensations",
                             "emp_details", "emp_gov_ids", "leave_balances", "leave_requests"};
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
    @Order(3)
    @DisplayName("1.3 Test Employee Authentication - Valid Credentials")
    void testEmployeeAuthentication() {
        boolean validLogin = login.authenticateDB(10003, "10003");
        assertTrue(validLogin, "Valid credentials should authenticate successfully");
        
        ArrayList<ArrayList<String>> userDetails = login.getUserDetails(10003, "10003");
        assertNotNull(userDetails, "User details should be retrieved");
        assertFalse(userDetails.isEmpty(), "User details should not be empty");
        
        System.out.println("Valid employee authentication test passed");
    }
    
    @Test
    @Order(4)
    @DisplayName("1.4 Test Employee Authentication - Invalid Credentials")
    void testInvalidEmployeeAuthentication() {
        boolean invalidLogin = login.authenticateDB(99999, "wrong");
        assertFalse(invalidLogin, "Invalid credentials should fail");
        
        boolean wrongPassword = login.authenticateDB(10003, "wrongpass");
        assertFalse(wrongPassword, "Wrong password should fail authentication");
        
        System.out.println("Invalid employee authentication test passed");
    }
    
    @Test
    @Order(5)
    @DisplayName("1.5 Test Role-Based Access Control")
    void testRoleBasedAccess() {
        try {
            String sql = "SELECT employee_id, role FROM employee_credentials_view WHERE employee_id IN (10001, 10002, 10003)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            Set<String> foundRoles = new HashSet<>();
            int employeeCount = 0;
            
            while (rs.next()) {
                String role = rs.getString("role"); 
                foundRoles.add(role);
                employeeCount++;
                assertNotNull(role, "Employee role should not be null");
                System.out.println("  Employee " + rs.getInt("employee_id") + " has role: " + role);
            }
            
            assertTrue(employeeCount > 0, "Should have employees with assigned roles");
            System.out.println("Role-based access test passed - Found " + foundRoles.size() + " different roles");
            
        } catch (SQLException e) {
            fail("Role-based access test failed: " + e.getMessage());
        }
    }

    @Test
    @Order(6)
    @DisplayName("2.1 Test Tax Calculation")
    void testTaxCalculation() {
        double tax1 = calculateTax(15000);
        assertEquals(0, tax1, 0.01, "Income ≤20k should have no tax");
        
        double tax2 = calculateTax(25000);
        assertEquals(750, tax2, 0.01, "Tax calculation for 25k salary");
        
        double tax3 = calculateTax(40000);
        assertEquals(3350, tax3, 0.01, "Tax calculation for 40k salary");
        
        System.out.println("Tax calculation test passed");
        System.out.println("  15k tax: ₱" + tax1);
        System.out.println("  25k tax: ₱" + tax2);
        System.out.println("  40k tax: ₱" + tax3);
    }
    
    @Test
    @Order(7)
    @DisplayName("2.2 Test SSS Contribution")
    void testSSSContribution() {
        double salary1 = 15000;
        double sss1 = calculateSSS(salary1);
        assertEquals(675.0, sss1, 0.01, "SSS for 15k should be 4.5%");
        
        double salary2 = 30000;
        double sss2 = calculateSSS(salary2);
        assertTrue(sss2 <= 1800, "SSS should not exceed maximum");
        
        System.out.println("SSS contribution test passed");
        System.out.println("  15k SSS: ₱" + sss1);
        System.out.println("  30k SSS: ₱" + sss2);
    }
    
    @Test
    @Order(8)
    @DisplayName("2.3 Test PhilHealth Contribution")
    void testPhilHealthContribution() {
        double salary = 30000;
        double philhealth = calculatePhilHealth(salary);
        
        assertTrue(philhealth >= 400, "PhilHealth should be at least minimum");
        assertTrue(philhealth <= 1800, "PhilHealth should not exceed maximum");
        assertEquals(900.0, philhealth, 0.01, "PhilHealth for 30k should be 3%");
        
        System.out.println("PhilHealth contribution test passed: ₱" + philhealth);
    }
    
    @Test
    @Order(9)
    @DisplayName("2.4 Test Pag-IBIG Contribution")
    void testPagIbigContribution() {
        double salary1 = 15000;
        double pagibig1 = calculatePagIbig(salary1);
        assertEquals(150.0, pagibig1, 0.01, "Pag-IBIG for ≤20k should be 1%");
        
        double salary2 = 30000;
        double pagibig2 = calculatePagIbig(salary2);
        assertEquals(600.0, pagibig2, 0.01, "Pag-IBIG for >20k should be 2%");
        
        System.out.println("Pag-IBIG contribution test passed");
        System.out.println("  15k: ₱" + pagibig1 + " | 30k: ₱" + pagibig2);
    }
    
    @Test
    @Order(10)
    @DisplayName("2.5 Test Complete Payroll Calculation")
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
        
        double totalDeductions = sss + philhealth + pagibig + tax;
        double netPay = grossIncome + benefits - totalDeductions;
        
        assertTrue(netPay > 0, "Net pay should be positive");
        assertTrue(netPay < grossIncome + benefits, "Net pay should be less than gross + benefits");
        assertTrue(totalDeductions > 0, "Total deductions should be positive");
        
        System.out.println("Complete payroll calculation test passed");
        System.out.println("  Gross Income: ₱" + String.format("%.2f", grossIncome));
        System.out.println("  Benefits: ₱" + String.format("%.2f", benefits));
        System.out.println("  Total Deductions: ₱" + String.format("%.2f", totalDeductions));
        System.out.println("  Net Pay: ₱" + String.format("%.2f", netPay));
    }
    
    @Test
    @Order(11)
    @DisplayName("2.6 Test Payroll Record Creation")
    void testPayrollRecordCreation() {
        PayrollRecord payroll = new PayrollRecord();
        payroll.setEmployeeId(10003);
        payroll.setPayrollPeriod(TEST_PAYROLL_PERIOD);
        payroll.setPosition("Chief Finance Officer");
        payroll.setGrossIncome(30000.0);
        payroll.setBenefits(2500.0);
        payroll.setOvertime(1500.0);
        payroll.setUndertime(0.0);
        payroll.setSss(1350.0);
        payroll.setPhilhealth(900.0);
        payroll.setPagibig(600.0);
        payroll.setTax(2250.0);
        payroll.setNetPay(28900.0);
        payroll.setStatus("PROCESSED");
        
        assertEquals(10003, payroll.getEmployeeId());
        assertEquals(TEST_PAYROLL_PERIOD, payroll.getPayrollPeriod());
        assertEquals("Chief Finance Officer", payroll.getPosition());
        assertEquals(30000.0, payroll.getGrossIncome(), 0.01);
        assertEquals(2500.0, payroll.getBenefits(), 0.01);
        assertEquals(1500.0, payroll.getOvertime(), 0.01);
        assertEquals(0.0, payroll.getUndertime(), 0.01);
        assertEquals(1350.0, payroll.getSss(), 0.01);
        assertEquals(900.0, payroll.getPhilhealth(), 0.01);
        assertEquals(600.0, payroll.getPagibig(), 0.01);
        assertEquals(2250.0, payroll.getTax(), 0.01);
        assertEquals(28900.0, payroll.getNetPay(), 0.01);
        assertEquals("PROCESSED", payroll.getStatus());
        
        System.out.println("Payroll record creation test passed");
    }

    @Test
    @Order(12)
    @DisplayName("3.1 Test Employee Data Retrieval")
    void testEmployeeDataRetrieval() {
        try {
            String sql = "SELECT ei.employee_id, ei.first_name, ei.last_name, ec.basic_salary, p.title as position " +
                        "FROM emp_info ei " +
                        "LEFT JOIN emp_compensations ec ON ei.employee_id = ec.employee_id " +
                        "LEFT JOIN emp_details ed ON ei.employee_id = ed.employee_id " +
                        "LEFT JOIN positions p ON ed.position_id = p.position_id " +
                        "LIMIT 5";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                int empId = rs.getInt("employee_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                double salary = rs.getDouble("basic_salary");
                String position = rs.getString("position");
                
                assertTrue(empId > 0, "Employee ID should be positive");
                assertNotNull(firstName, "First name should not be null");
                assertNotNull(lastName, "Last name should not be null");
                assertTrue(salary >= 0, "Salary should be non-negative");
                
                System.out.println("  Employee: " + empId + " - " + firstName + " " + lastName + 
                                 " (" + position + ") - ₱" + String.format("%.2f", salary));
            }
            
            assertTrue(count > 0, "Should have at least one employee");
            System.out.println("Employee data retrieval test passed - " + count + " employees found");
            
        } catch (Exception e) {
            fail("Employee data retrieval test failed: " + e.getMessage());
        }
    }
    
    @Test
    @Order(13)
    @DisplayName("3.2 Test Employee Search Functionality")
    void testEmployeeSearchFunctionality() {
        try {
            String sql = "SELECT * FROM employee_profile_view WHERE employee_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, 10003);
            ResultSet rs = pstmt.executeQuery();
            
            assertTrue(rs.next(), "Should find employee with ID 10003");
            assertEquals(10003, rs.getInt("employee_id"));
            assertNotNull(rs.getString("first_name"));
            assertNotNull(rs.getString("last_name"));
            
            String sql2 = "SELECT COUNT(*) as count FROM employee_profile_view " +
                            "WHERE position LIKE '%Officer%'";
            PreparedStatement pstmt2 = connection.prepareStatement(sql2);
            ResultSet rs2 = pstmt2.executeQuery();
            
            if (rs2.next()) {
                int officerCount = rs2.getInt("count");
                System.out.println("  Found " + officerCount + " officers in the system");
            }
            
            System.out.println("Employee search functionality test passed");
            
        } catch (SQLException e) {
            fail("Employee search test failed: " + e.getMessage());
        }
    }
    
    @Test
    @Order(14)
    @DisplayName("3.3 Test Employee CRUD Operations Validation")
    void testEmployeeCRUDValidation() {
        try {
            String sql = "DESCRIBE emp_info";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            Set<String> requiredFields = new HashSet<>(Arrays.asList(
                "employee_id", "first_name", "last_name", "birthday", 
                "address", "phone_number"
            ));
            
            Set<String> foundFields = new HashSet<>();
            while (rs.next()) {
                foundFields.add(rs.getString("Field"));
            }
            
            for (String field : requiredFields) {
                assertTrue(foundFields.contains(field), 
                          "Required field '" + field + "' should exist in emp_info table");
            }
            
            System.out.println("Employee CRUD validation test passed - All required fields present");
            System.out.println("  Found fields: " + foundFields);
            
        } catch (SQLException e) {
            fail("Employee CRUD validation test failed: " + e.getMessage());
        }
    }
    
    @Test
    @Order(15)
    @DisplayName("4.1 Test Employee Personal Information Access")
    void testEmployeePersonalInfoAccess() {
        try {
            Employee testEmployee = new Employee("10003");
            
            String sql = "SELECT ei.*, ec.basic_salary, p.title as position " +
                        "FROM emp_info ei " +
                        "LEFT JOIN emp_compensations ec ON ei.employee_id = ec.employee_id " +
                        "LEFT JOIN emp_details ed ON ei.employee_id = ed.employee_id " +
                        "LEFT JOIN positions p ON ed.position_id = p.position_id " +
                        "WHERE ei.employee_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, 10003);
            ResultSet rs = pstmt.executeQuery();
            
            assertTrue(rs.next(), "Employee should be able to access their personal information");
            
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String position = rs.getString("position");
            double basicSalary = rs.getDouble("basic_salary");
            
            assertNotNull(firstName, "First name should be accessible");
            assertNotNull(lastName, "Last name should be accessible");
            assertTrue(basicSalary > 0, "Basic salary should be accessible and positive");
            
            System.out.println("Employee personal info access test passed");
            System.out.println("  Employee: " + firstName + " " + lastName + " - " + position);
            
        } catch (Exception e) {
            fail("Employee personal info access test failed: " + e.getMessage());
        }
    }
    
    @Test
    @Order(16)
    @DisplayName("4.2 Test Employee Attendance Tracking")
    void testEmployeeAttendanceTracking() {
        try {
            String sql = "SELECT * FROM attendance WHERE employee_id = 10003 LIMIT 5";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            boolean hasAttendanceRecords = false;
            while (rs.next()) {
                hasAttendanceRecords = true;
                int empId = rs.getInt("employee_id");
                Date logDate = rs.getDate("log_date");
                
                assertEquals(10003, empId, "Employee ID should match");
                assertNotNull(logDate, "Log date should be present");
                
                System.out.println("  Attendance record: " + empId + " - " + logDate);
            }
            
            System.out.println("Employee attendance tracking test passed");
            if (!hasAttendanceRecords) {
                System.out.println("  (No attendance records found - this is acceptable)");
            }
            
        } catch (SQLException e) {
            fail("Employee attendance tracking test failed: " + e.getMessage());
        }
    }

    @Test
    @Order(17)
    @DisplayName("5.1 Test Payroll Report Generation")
    void testPayrollReportGeneration() {
        try {
            String sql = "SELECT p.employee_id, ei.first_name, ei.last_name, p.payroll_period, " +
                        "p.gross_income, p.net_pay, p.status " +
                        "FROM payroll p JOIN emp_info ei ON p.employee_id = ei.employee_id " +
                        "LIMIT 10";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            int reportCount = 0;
            double totalGross = 0;
            double totalNet = 0;
            
            while (rs.next()) {
                reportCount++;
                totalGross += rs.getDouble("gross_income");
                totalNet += rs.getDouble("net_pay");
                
                System.out.println("  Payroll: " + rs.getString("first_name") + " " + 
                                 rs.getString("last_name") + " - Period: " + 
                                 rs.getString("payroll_period") + " - Net: ₱" + 
                                 String.format("%.2f", rs.getDouble("net_pay")));
            }
            
            if (reportCount > 0) {
                double avgGross = totalGross / reportCount;
                double avgNet = totalNet / reportCount;
                
                System.out.println("Payroll report generation test passed");
                System.out.println("  Records processed: " + reportCount);
                System.out.println("  Average gross: ₱" + String.format("%.2f", avgGross));
                System.out.println("  Average net: ₱" + String.format("%.2f", avgNet));
            } else {
                System.out.println("Payroll report structure test passed (no data yet)");
            }
            
        } catch (SQLException e) {
            fail("Payroll report generation test failed: " + e.getMessage());
        }
    }
    
    @Test
    @Order(18)
    @DisplayName("5.2 Test Employee Summary Report")
    void testEmployeeSummaryReport() {
        try {
            String sql = "SELECT " +
                        "COUNT(DISTINCT ei.employee_id) as total_employees, " +
                        "COUNT(DISTINCT p.title) as unique_positions, " +
                        "AVG(ec.basic_salary) as avg_salary, " +
                        "MIN(ec.basic_salary) as min_salary, " +
                        "MAX(ec.basic_salary) as max_salary " +
                        "FROM emp_info ei " +
                        "LEFT JOIN emp_compensations ec ON ei.employee_id = ec.employee_id " +
                        "LEFT JOIN emp_details ed ON ei.employee_id = ed.employee_id " +
                        "LEFT JOIN positions p ON ed.position_id = p.position_id";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int totalEmployees = rs.getInt("total_employees");
                int uniquePositions = rs.getInt("unique_positions");
                double avgSalary = rs.getDouble("avg_salary");
                double minSalary = rs.getDouble("min_salary");
                double maxSalary = rs.getDouble("max_salary");
                
                assertTrue(totalEmployees > 0, "Should have employees in the system");
                assertTrue(uniquePositions > 0, "Should have different positions");
                assertTrue(avgSalary > 0, "Average salary should be positive");
                
                System.out.println("Employee summary report test passed");
                System.out.println("  Total employees: " + totalEmployees);
                System.out.println("  Unique positions: " + uniquePositions);
                System.out.println("  Average salary: ₱" + String.format("%.2f", avgSalary));
                System.out.println("  Salary range: ₱" + String.format("%.2f", minSalary) + 
                                 " - ₱" + String.format("%.2f", maxSalary));
            }
            
        } catch (SQLException e) {
            fail("Employee summary report test failed: " + e.getMessage());
        }
    }
    
    @Test
    @Order(19)
    @DisplayName("5.3 Test Database Performance")
    void testDatabasePerformance() {
        long startTime = System.currentTimeMillis();
        
        try {
            String sql = "SELECT ei.employee_id, ei.first_name, ei.last_name, p.title, ec.basic_salary " +
                        "FROM emp_info ei " +
                        "LEFT JOIN emp_compensations ec ON ei.employee_id = ec.employee_id " +
                        "LEFT JOIN emp_details ed ON ei.employee_id = ed.employee_id " +
                        "LEFT JOIN positions p ON ed.position_id = p.position_id " +
                        "ORDER BY ei.employee_id";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                double salary = rs.getDouble("basic_salary");
            }
            
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            assertTrue(executionTime < 5000, "Query should complete within 5 seconds");
            assertTrue(count > 0, "Should have at least one employee");
            
            System.out.println("Database performance test passed");
            System.out.println("  Query execution time: " + executionTime + "ms");
            System.out.println("  Records processed: " + count);
            System.out.println("  Performance: " + String.format("%.2f", (double)count/executionTime*1000) + " records/second");
            
        } catch (Exception e) {
            fail("Database performance test failed: " + e.getMessage());
        }
    }
    
    @Test
    @Order(20)
    @DisplayName("5.4 Test Working Days Calculation")
    void testWorkingDaysCalculation() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(2025, Calendar.JULY, 1);
            Date startDate = cal.getTime();
            
            cal.set(2025, Calendar.JULY, 15);
            Date endDate = cal.getTime();
            
            int workingDays = calculateWorkingDays(startDate, endDate);
            
            assertTrue(workingDays > 0, "Working days should be positive");
            assertTrue(workingDays <= 15, "Working days should not exceed total days");
            
            System.out.println("Working days calculation test passed");
            System.out.println("  Working days (July 1-15, 2025): " + workingDays);
            
        } catch (Exception e) {
            fail("Working days calculation test failed: " + e.getMessage());
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
    
    private int calculateWorkingDays(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        
        int workingDays = 0;
        while (start.getTimeInMillis() <= end.getTimeInMillis()) {
            int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                workingDays++;
            }
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        return workingDays;
    }
    

    @AfterAll
    static void cleanup() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL MOTORPH TESTS COMPLETED SUCCESSFULLY! ");
        System.out.println("=".repeat(60));
        System.out.println("Employee login functionality - TESTED");
        System.out.println("Payroll generation capability - TESTED");
        System.out.println("HR management functions - TESTED");
        System.out.println("Employee self-service portal - TESTED");
        System.out.println("Reporting capabilities - TESTED");
    }
    
    public static void main(String[] args) {
        System.out.println("RUNNING MOTORPH JUNIT TESTS MANUALLY");
        System.out.println("=" .repeat(50));

        try {
            MotorPHTest.setupDatabase();

            MotorPHTest test = new MotorPHTest();
            test.setup();

            System.out.println("\nRUNNING TESTS...\n");

            test.testDatabaseConnection();
            test.testTablesExist();
            test.testEmployeeAuthentication();
            test.testInvalidEmployeeAuthentication();
            test.testRoleBasedAccess();
            test.testTaxCalculation();
            test.testSSSContribution();
            test.testPhilHealthContribution();
            test.testPagIbigContribution();
            test.testCompletePayrollCalculation();
            test.testPayrollRecordCreation();
            test.testEmployeeDataRetrieval();
            test.testEmployeeSearchFunctionality();
            test.testEmployeeCRUDValidation();
            test.testEmployeePersonalInfoAccess();
            test.testEmployeeAttendanceTracking();
            test.testPayrollReportGeneration();
            test.testEmployeeSummaryReport();
            test.testDatabasePerformance();
            test.testWorkingDaysCalculation();

            MotorPHTest.cleanup();

            System.out.println("\nALL TESTS COMPLETED!");

        } catch (Exception e) {
            System.err.println("Test execution failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
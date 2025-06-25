package payrollsystem;

import java.io.*;
import java.sql.*;
import java.util.*;

public class EmployeeReportGenerator {
    private Connection connection;
    private EmployeeDAO employeeDAO;
    private LeaveDAO leaveDAO;
    
    public EmployeeReportGenerator() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
            this.employeeDAO = new EmployeeDAO();
            this.leaveDAO = new LeaveDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Generate Employee Directory
    public File generateEmployeeDirectory() {
        try {
            File reportDir = new File("reports/employee/directory");
            reportDir.mkdirs();
            
            String filename = "Employee_Directory_" + java.time.LocalDate.now() + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            writer.println("MOTORPH EMPLOYEE DIRECTORY");
            writer.println("Generated on: " + java.time.LocalDate.now());
            writer.println("");
            writer.println("Employee ID,Last Name,First Name,Position,Phone Number,Email,Address,Status,Hire Date");
            
            String sql = "SELECT employee_id, last_name, first_name, position, phone_number, " +
                        "address, status FROM employees ORDER BY last_name, first_name";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            int totalEmployees = 0;
            while (rs.next()) {
                writer.println(String.format("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                    rs.getInt("employee_id"),
                    rs.getString("last_name"),
                    rs.getString("first_name"),
                    rs.getString("position"),
                    rs.getString("phone_number") != null ? rs.getString("phone_number") : "",
                    rs.getString("address") != null ? rs.getString("address") : "",
                    rs.getString("status")
                ));
                totalEmployees++;
            }
            
            writer.println("");
            writer.println("Total Employees:," + totalEmployees);
            
            writer.close();
            System.out.println("✅ Employee directory generated for " + totalEmployees + " employees");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Generate Employee Benefits Summary
    public File generateBenefitsSummary() {
        try {
            File reportDir = new File("reports/employee/benefits");
            reportDir.mkdirs();
            
            String filename = "Employee_Benefits_Summary_" + java.time.LocalDate.now() + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            // Header
            writer.println("MOTORPH EMPLOYEE BENEFITS SUMMARY");
            writer.println("Generated on: " + java.time.LocalDate.now());
            writer.println("");
            writer.println("Employee ID,Employee Name,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Total Benefits,SSS Number,PhilHealth Number,TIN Number,Pag-IBIG Number");
            
            String sql = "SELECT employee_id, first_name, last_name, basic_salary, rice_subsidy, " +
                        "phone_allowance, clothing_allowance, sss_number, philhealth_number, " +
                        "tin_number, pagibig_number FROM employees ORDER BY employee_id";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            double totalBenefitsCompany = 0;
            int employeeCount = 0;
            
            while (rs.next()) {
                double totalBenefits = rs.getDouble("rice_subsidy") + 
                                     rs.getDouble("phone_allowance") + 
                                     rs.getDouble("clothing_allowance");
                
                writer.println(String.format("%d,\"%s %s\",%.2f,%.2f,%.2f,%.2f,%.2f,\"%s\",\"%s\",\"%s\",\"%s\"",
                    rs.getInt("employee_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDouble("basic_salary"),
                    rs.getDouble("rice_subsidy"),
                    rs.getDouble("phone_allowance"),
                    rs.getDouble("clothing_allowance"),
                    totalBenefits,
                    rs.getString("sss_number") != null ? rs.getString("sss_number") : "",
                    rs.getString("philhealth_number") != null ? rs.getString("philhealth_number") : "",
                    rs.getString("tin_number") != null ? rs.getString("tin_number") : "",
                    rs.getString("pagibig_number") != null ? rs.getString("pagibig_number") : ""
                ));
                
                totalBenefitsCompany += totalBenefits;
                employeeCount++;
            }
            
            writer.println("");
            writer.println("SUMMARY:");
            writer.println("Total Employees:," + employeeCount);
            writer.println("Total Company Benefits Cost:," + String.format("%.2f", totalBenefitsCompany));
            writer.println("Average Benefits per Employee:," + String.format("%.2f", totalBenefitsCompany / employeeCount));
            
            writer.close();
            System.out.println("✅ Employee benefits summary generated");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Generate Leave Balance Report
    public File generateLeaveBalanceReport() {
        try {
            File reportDir = new File("reports/employee/leave-balance");
            reportDir.mkdirs();
            
            String filename = "Leave_Balance_Report_" + java.time.LocalDate.now() + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            // Header
            writer.println("MOTORPH EMPLOYEE LEAVE BALANCE REPORT");
            writer.println("Generated on: " + java.time.LocalDate.now());
            writer.println("");
            writer.println("Employee ID,Employee Name,Position,Vacation Leave Balance,Sick Leave Balance,Total Leave Days,Leave Utilization %");
            
            String sql = "SELECT e.employee_id, e.first_name, e.last_name, e.position, " +
                        "lb.vacation_leave, lb.sick_leave " +
                        "FROM employees e " +
                        "LEFT JOIN leave_balances lb ON e.employee_id = lb.employee_id " +
                        "ORDER BY e.employee_id";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            double totalVLBalance = 0, totalSLBalance = 0;
            int employeeCount = 0;
            
            while (rs.next()) {
                double vlBalance = rs.getDouble("vacation_leave");
                double slBalance = rs.getDouble("sick_leave");
                double totalBalance = vlBalance + slBalance;
                
                // Calculate utilization (assuming 24 days annual allocation)
                double utilizationRate = ((48 - totalBalance) / 48.0) * 100;
                
                writer.println(String.format("%d,\"%s %s\",\"%s\",%.1f,%.1f,%.1f,%.1f",
                    rs.getInt("employee_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("position"),
                    vlBalance,
                    slBalance,
                    totalBalance,
                    utilizationRate
                ));
                
                totalVLBalance += vlBalance;
                totalSLBalance += slBalance;
                employeeCount++;
            }
            
            writer.println("");
            writer.println("SUMMARY:");
            writer.println("Total Employees:," + employeeCount);
            writer.println("Average VL Balance:," + String.format("%.1f", totalVLBalance / employeeCount));
            writer.println("Average SL Balance:," + String.format("%.1f", totalSLBalance / employeeCount));
            writer.println("Company Leave Liability (Days):," + String.format("%.1f", (totalVLBalance + totalSLBalance)));
            
            writer.close();
            System.out.println("✅ Leave balance report generated");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Generate Salary Analysis Report
    public File generateSalaryAnalysis() {
        try {
            File reportDir = new File("reports/employee/salary-analysis");
            reportDir.mkdirs();
            
            String filename = "Salary_Analysis_Report_" + java.time.LocalDate.now() + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            // Header
            writer.println("MOTORPH SALARY ANALYSIS REPORT");
            writer.println("Generated on: " + java.time.LocalDate.now());
            writer.println("");
            
            // Salary by Position
            writer.println("SALARY ANALYSIS BY POSITION:");
            writer.println("Position,Employee Count,Min Salary,Max Salary,Average Salary,Total Salary Cost");
            
            String posSql = "SELECT position, COUNT(*) as emp_count, MIN(basic_salary) as min_sal, " +
                           "MAX(basic_salary) as max_sal, AVG(basic_salary) as avg_sal, " +
                           "SUM(basic_salary) as total_sal " +
                           "FROM employees GROUP BY position ORDER BY avg_sal DESC";
            
            PreparedStatement posStmt = connection.prepareStatement(posSql);
            ResultSet posRs = posStmt.executeQuery();
            
            double grandTotal = 0;
            int totalEmployees = 0;
            
            while (posRs.next()) {
                writer.println(String.format("\"%s\",%d,%.2f,%.2f,%.2f,%.2f",
                    posRs.getString("position"),
                    posRs.getInt("emp_count"),
                    posRs.getDouble("min_sal"),
                    posRs.getDouble("max_sal"),
                    posRs.getDouble("avg_sal"),
                    posRs.getDouble("total_sal")
                ));
                
                grandTotal += posRs.getDouble("total_sal");
                totalEmployees += posRs.getInt("emp_count");
            }
            
            writer.println("");
            writer.println("SALARY DISTRIBUTION:");
            writer.println("Salary Range,Employee Count,Percentage");
            
            String[] ranges = {"0-25000", "25001-50000", "50001-75000", "75001-100000", "100000+"};
            String[] conditions = {
                "basic_salary <= 25000",
                "basic_salary BETWEEN 25001 AND 50000", 
                "basic_salary BETWEEN 50001 AND 75000",
                "basic_salary BETWEEN 75001 AND 100000",
                "basic_salary > 100000"
            };
            
            for (int i = 0; i < ranges.length; i++) {
                String rangeSql = "SELECT COUNT(*) as count FROM employees WHERE " + conditions[i];
                PreparedStatement rangeStmt = connection.prepareStatement(rangeSql);
                ResultSet rangeRs = rangeStmt.executeQuery();
                
                if (rangeRs.next()) {
                    int count = rangeRs.getInt("count");
                    double percentage = (double) count / totalEmployees * 100;
                    writer.println(String.format("%s,%d,%.1f%%", ranges[i], count, percentage));
                }
            }
            
            writer.println("");
            writer.println("OVERALL SUMMARY:");
            writer.println("Total Employees:," + totalEmployees);
            writer.println("Total Monthly Salary Cost:," + String.format("%.2f", grandTotal));
            writer.println("Average Salary:," + String.format("%.2f", grandTotal / totalEmployees));
            writer.println("Annual Salary Cost:," + String.format("%.2f", grandTotal * 12));
            
            writer.close();
            System.out.println("✅ Salary analysis report generated");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Generate Department Statistics
    public File generateDepartmentStats() {
        try {
            File reportDir = new File("reports/employee/department-stats");
            reportDir.mkdirs();
            
            String filename = "Department_Statistics_" + java.time.LocalDate.now() + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            // Header
            writer.println("MOTORPH DEPARTMENT STATISTICS REPORT");
            writer.println("Generated on: " + java.time.LocalDate.now());
            writer.println("");
            
            // Group by position/department
            writer.println("DEPARTMENT BREAKDOWN:");
            writer.println("Department/Position,Employee Count,Average Salary,Total Cost,Avg Benefits");
            
            String deptSql = "SELECT position, COUNT(*) as emp_count, AVG(basic_salary) as avg_sal, " +
                            "SUM(basic_salary) as total_cost, AVG(rice_subsidy + phone_allowance + clothing_allowance) as avg_benefits " +
                            "FROM employees GROUP BY position ORDER BY emp_count DESC";
            
            PreparedStatement deptStmt = connection.prepareStatement(deptSql);
            ResultSet deptRs = deptStmt.executeQuery();
            
            int totalEmp = 0;
            double totalCost = 0;
            
            while (deptRs.next()) {
                int empCount = deptRs.getInt("emp_count");
                double avgSal = deptRs.getDouble("avg_sal");
                double totalCostDept = deptRs.getDouble("total_cost");
                double avgBenefits = deptRs.getDouble("avg_benefits");
                
                writer.println(String.format("\"%s\",%d,%.2f,%.2f,%.2f",
                    deptRs.getString("position"),
                    empCount,
                    avgSal,
                    totalCostDept,
                    avgBenefits
                ));
                
                totalEmp += empCount;
                totalCost += totalCostDept;
            }
            
            writer.println("");
            writer.println("STATUS BREAKDOWN:");
            writer.println("Status,Employee Count,Percentage");
            
            String statusSql = "SELECT status, COUNT(*) as count FROM employees GROUP BY status";
            PreparedStatement statusStmt = connection.prepareStatement(statusSql);
            ResultSet statusRs = statusStmt.executeQuery();
            
            while (statusRs.next()) {
                int count = statusRs.getInt("count");
                double percentage = (double) count / totalEmp * 100;
                writer.println(String.format("%s,%d,%.1f%%", 
                    statusRs.getString("status"), count, percentage));
            }
            
            writer.println("");
            writer.println("COMPANY OVERVIEW:");
            writer.println("Total Employees:," + totalEmp);
            writer.println("Total Monthly Cost:," + String.format("%.2f", totalCost));
            writer.println("Average Cost per Employee:," + String.format("%.2f", totalCost / totalEmp));
            writer.println("Estimated Annual Cost:," + String.format("%.2f", totalCost * 12));
            
            writer.close();
            System.out.println("✅ Department statistics report generated");
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

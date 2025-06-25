package payrollsystem;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ReportGenerator {
    private Connection connection;
    private String templatePath;
    
    public ReportGenerator() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Generate Individual Payslip PDF (or text file for now)
    public File generatePayslipPDF(int employeeId, String payrollPeriod) {
        try {
            String sql = "SELECT p.*, e.* FROM payroll p " +
                        "JOIN employees e ON p.employee_id = e.employee_id " +
                        "WHERE p.employee_id = ? AND p.payroll_period = ?";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, payrollPeriod);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                File reportDir = new File("reports/payslips");
                reportDir.mkdirs();
                
                String filename = "Payslip_" + employeeId + "_" + payrollPeriod.replace("/", "-").replace(" ", "_") + ".txt";
                File file = new File(reportDir, filename);
                
                PrintWriter writer = new PrintWriter(file);
                
                writer.println("=========================================");
                writer.println("           MOTORPH PAYSLIP               ");
                writer.println("=========================================");
                writer.println("Employee ID    : " + rs.getInt("employee_id"));
                writer.println("Name          : " + rs.getString("first_name") + " " + rs.getString("last_name"));
                writer.println("Position      : " + rs.getString("position"));
                writer.println("Department    : " + rs.getString("immediate_supervisor"));
                writer.println("Payroll Period: " + payrollPeriod);
                writer.println("-----------------------------------------");
                writer.println("EARNINGS:");
                writer.println("  Basic Salary    : ₱ " + String.format("%,10.2f", rs.getDouble("basic_salary")));
                writer.println("  Rice Subsidy    : ₱ " + String.format("%,10.2f", rs.getDouble("rice_subsidy")));
                writer.println("  Phone Allowance : ₱ " + String.format("%,10.2f", rs.getDouble("phone_allowance")));
                writer.println("  Clothing Allow. : ₱ " + String.format("%,10.2f", rs.getDouble("clothing_allowance")));
                writer.println("  Overtime        : ₱ " + String.format("%,10.2f", rs.getDouble("overtime")));
                writer.println("                      ---------------");
                writer.println("  Gross Income    : ₱ " + String.format("%,10.2f", rs.getDouble("gross_income")));
                writer.println("-----------------------------------------");
                writer.println("DEDUCTIONS:");
                writer.println("  SSS             : ₱ " + String.format("%,10.2f", rs.getDouble("sss")));
                writer.println("  PhilHealth      : ₱ " + String.format("%,10.2f", rs.getDouble("philhealth")));
                writer.println("  Pag-IBIG        : ₱ " + String.format("%,10.2f", rs.getDouble("pagibig")));
                writer.println("  Withholding Tax : ₱ " + String.format("%,10.2f", rs.getDouble("tax")));
                writer.println("  Undertime       : ₱ " + String.format("%,10.2f", rs.getDouble("undertime")));
                
                double totalDeductions = rs.getDouble("sss") + rs.getDouble("philhealth") + 
                                       rs.getDouble("pagibig") + rs.getDouble("tax") + rs.getDouble("undertime");
                
                writer.println("                      ---------------");
                writer.println("  Total Deductions: ₱ " + String.format("%,10.2f", totalDeductions));
                writer.println("=========================================");
                writer.println("NET PAY           : ₱ " + String.format("%,10.2f", rs.getDouble("net_pay")));
                writer.println("=========================================");
                writer.println("\nGenerated on: " + LocalDate.now());
                writer.println("This is a system-generated payslip.");
                
                writer.close();
                
                JOptionPane.showMessageDialog(null, "Payslip saved to: " + file.getAbsolutePath());
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Generate Monthly Report
    public File generateMonthlyReport(String month, int year) {
        try {
            File reportDir = new File("reports/monthly");
            reportDir.mkdirs();
            
            String filename = "Monthly_Report_" + month + "_" + year + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            // Header
            writer.println("MOTORPH MONTHLY PAYROLL REPORT");
            writer.println("Month: " + month + " " + year);
            writer.println("");
            writer.println("Employee ID,Name,Position,Days Worked,Gross Income,Total Deductions,Net Pay,Status");
            
            String sql = "SELECT p.*, e.first_name, e.last_name FROM payroll p " +
                        "JOIN employees e ON p.employee_id = e.employee_id " +
                        "WHERE p.payroll_period LIKE ?";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "%" + month + "%" + year + "%");
            ResultSet rs = pstmt.executeQuery();
            
            double totalGross = 0, totalDeductions = 0, totalNet = 0;
            int employeeCount = 0;
            
            while (rs.next()) {
                double deductions = rs.getDouble("sss") + rs.getDouble("philhealth") + 
                                   rs.getDouble("pagibig") + rs.getDouble("tax") + rs.getDouble("undertime");
                
                writer.println(String.format("%d,\"%s %s\",%s,%d,%.2f,%.2f,%.2f,%s",
                    rs.getInt("employee_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("position"),
                    15, // Assuming 15 days per period
                    rs.getDouble("gross_income"),
                    deductions,
                    rs.getDouble("net_pay"),
                    rs.getString("status")
                ));
                
                totalGross += rs.getDouble("gross_income");
                totalDeductions += deductions;
                totalNet += rs.getDouble("net_pay");
                employeeCount++;
            }
            
            writer.println("");
            writer.println("SUMMARY");
            writer.println("Total Employees:," + employeeCount);
            writer.println("Total Gross Income:," + String.format("%.2f", totalGross));
            writer.println("Total Deductions:," + String.format("%.2f", totalDeductions));
            writer.println("Total Net Pay:," + String.format("%.2f", totalNet));
            
            writer.close();
            
            JOptionPane.showMessageDialog(null, "Monthly report saved to: " + file.getAbsolutePath());
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Export to Excel (CSV format)
    public File exportToExcel(List<Map<String, Object>> data, String filename) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(filename + ".csv"));
            
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                PrintWriter writer = new PrintWriter(file);
                
                if (!data.isEmpty()) {
                    // Write headers
                    Map<String, Object> firstRow = data.get(0);
                    writer.println(String.join(",", firstRow.keySet()));
                    
                    // Write data
                    for (Map<String, Object> row : data) {
                        List<String> values = new ArrayList<>();
                        for (Object value : row.values()) {
                            values.add(value != null ? value.toString() : "");
                        }
                        writer.println(String.join(",", values));
                    }
                }
                
                writer.close();
                JOptionPane.showMessageDialog(null, "Data exported to: " + file.getAbsolutePath());
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Generate Government Reports (SSS, PhilHealth, Pag-IBIG)
    public File generateGovReports(String type, String period) {
        try {
            File reportDir = new File("reports/government");
            reportDir.mkdirs();
            
            String filename = type + "_Report_" + period.replace("/", "-").replace(" ", "_") + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);
            
            switch (type.toUpperCase()) {
                case "SSS":
                    generateSSSReport(writer, period);
                    break;
                case "PHILHEALTH":
                    generatePhilHealthReport(writer, period);
                    break;
                case "PAGIBIG":
                    generatePagIbigReport(writer, period);
                    break;
            }
            
            writer.close();
            JOptionPane.showMessageDialog(null, type + " report saved to: " + file.getAbsolutePath());
            return file;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void generateSSSReport(PrintWriter writer, String period) throws SQLException {
        writer.println("SSS CONTRIBUTION REPORT");
        writer.println("Period: " + period);
        writer.println("");
        writer.println("SSS No.,Employee Name,Monthly Salary,Employee Share,Employer Share,Total Contribution");
        
        String sql = "SELECT e.sss_number, e.first_name, e.last_name, e.basic_salary, p.sss " +
                    "FROM payroll p JOIN employees e ON p.employee_id = e.employee_id " +
                    "WHERE p.payroll_period = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, period);
        ResultSet rs = pstmt.executeQuery();
        
        double totalEmployee = 0, totalEmployer = 0;
        
        while (rs.next()) {
            double employeeShare = rs.getDouble("sss");
            double employerShare = employeeShare * 2; // Assuming employer pays double
            
            writer.println(String.format("%s,\"%s %s\",%.2f,%.2f,%.2f,%.2f",
                rs.getString("sss_number"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDouble("basic_salary"),
                employeeShare,
                employerShare,
                employeeShare + employerShare
            ));
            
            totalEmployee += employeeShare;
            totalEmployer += employerShare;
        }
        
        writer.println("");
        writer.println(String.format("TOTALS:,,,%.2f,%.2f,%.2f", 
            totalEmployee, totalEmployer, totalEmployee + totalEmployer));
    }
    
    private void generatePhilHealthReport(PrintWriter writer, String period) throws SQLException {
        // Similar implementation for PhilHealth
    }
    
    private void generatePagIbigReport(PrintWriter writer, String period) throws SQLException {
        // Similar implementation for Pag-IBIG
    }

    public File generateAllPayslips() {
        try {
            PayrollDAO payrollDAO = new PayrollDAO();
            java.time.LocalDate now = java.time.LocalDate.now();
            String month = String.format("%02d", now.getMonthValue());
            int year = now.getYear();

            List<PayrollRecord> payrolls = payrollDAO.getPayrollByMonth(month, year);

            File reportDir = new File("reports/payslips/batch");
            reportDir.mkdirs();

            String batchFilename = "All_Payslips_" + month + "_" + year + ".txt";
            File batchFile = new File(reportDir, batchFilename);
            PrintWriter batchWriter = new PrintWriter(batchFile);

            batchWriter.println("===========================================");
            batchWriter.println("    MOTORPH - ALL EMPLOYEE PAYSLIPS       ");
            batchWriter.println("    Period: " + month + "/" + year);
            batchWriter.println("===========================================");
            batchWriter.println("");

            for (PayrollRecord payroll : payrolls) {
                // Generate individual payslip content
                generatePayslipContent(batchWriter, payroll);
                batchWriter.println("\n" + "=".repeat(50) + "\n");
            }

            batchWriter.println("END OF BATCH PAYSLIPS");
            batchWriter.println("Total Employees: " + payrolls.size());
            batchWriter.println("Generated on: " + java.time.LocalDate.now());
            batchWriter.close();

            System.out.println("Generated payslips for " + payrolls.size() + " employees");
            return batchFile;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

// Generate Tax Report
    public File generateTaxReport() {
        try {
            File reportDir = new File("reports/tax");
            reportDir.mkdirs();

            java.time.LocalDate now = java.time.LocalDate.now();
            String filename = "Tax_Report_" + now.getMonthValue() + "_" + now.getYear() + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);

            // Header
            writer.println("MOTORPH TAX WITHHOLDING REPORT");
            writer.println("Period: " + now.getMonthValue() + "/" + now.getYear());
            writer.println("");
            writer.println("Employee ID,Employee Name,TIN,Gross Income,Tax Withheld,Net Pay");

            String sql = "SELECT p.employee_id, e.first_name, e.last_name, e.tin_number, " +
                        "p.gross_income, p.tax, p.net_pay " +
                        "FROM payroll p JOIN employees e ON p.employee_id = e.employee_id " +
                        "ORDER BY p.employee_id";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            double totalGross = 0, totalTax = 0, totalNet = 0;
            int employeeCount = 0;

            while (rs.next()) {
                writer.println(String.format("%d,\"%s %s\",%s,%.2f,%.2f,%.2f",
                    rs.getInt("employee_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("tin_number"),
                    rs.getDouble("gross_income"),
                    rs.getDouble("tax"),
                    rs.getDouble("net_pay")
                ));

                totalGross += rs.getDouble("gross_income");
                totalTax += rs.getDouble("tax");
                totalNet += rs.getDouble("net_pay");
                employeeCount++;
            }

            writer.println("");
            writer.println("SUMMARY:");
            writer.println(String.format("Total Employees:,%d", employeeCount));
            writer.println(String.format("Total Gross Income:,%.2f", totalGross));
            writer.println(String.format("Total Tax Withheld:,%.2f", totalTax));
            writer.println(String.format("Total Net Pay:,%.2f", totalNet));

            writer.close();
            System.out.println("Tax report generated for " + employeeCount + " employees");
            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public File generateBenefitsReport() {
        try {
            File reportDir = new File("reports/benefits");
            reportDir.mkdirs();

            java.time.LocalDate now = java.time.LocalDate.now();
            String filename = "Benefits_Report_" + now.getMonthValue() + "_" + now.getYear() + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);

            // Header
            writer.println("MOTORPH EMPLOYEE BENEFITS REPORT");
            writer.println("Period: " + now.getMonthValue() + "/" + now.getYear());
            writer.println("");
            writer.println("Employee ID,Employee Name,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Total Benefits,SSS,PhilHealth,Pag-IBIG,Total Contributions");

            String sql = "SELECT p.employee_id, e.first_name, e.last_name, e.basic_salary, " +
                        "e.rice_subsidy, e.phone_allowance, e.clothing_allowance, " +
                        "p.benefits, p.sss, p.philhealth, p.pagibig " +
                        "FROM payroll p JOIN employees e ON p.employee_id = e.employee_id " +
                        "ORDER BY e.last_name, e.first_name";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            double totalBenefits = 0, totalContributions = 0;
            int employeeCount = 0;

            while (rs.next()) {
                double benefits = rs.getDouble("rice_subsidy") + rs.getDouble("phone_allowance") + rs.getDouble("clothing_allowance");
                double contributions = rs.getDouble("sss") + rs.getDouble("philhealth") + rs.getDouble("pagibig");

                writer.println(String.format("%d,\"%s %s\",%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f",
                    rs.getInt("employee_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDouble("basic_salary"),
                    rs.getDouble("rice_subsidy"),
                    rs.getDouble("phone_allowance"),
                    rs.getDouble("clothing_allowance"),
                    benefits,
                    rs.getDouble("sss"),
                    rs.getDouble("philhealth"),
                    rs.getDouble("pagibig"),
                    contributions
                ));

                totalBenefits += benefits;
                totalContributions += contributions;
                employeeCount++;
            }

            writer.println("");
            writer.println("SUMMARY:");
            writer.println(String.format("Total Employees:,%d", employeeCount));
            writer.println(String.format("Total Benefits Paid:,%.2f", totalBenefits));
            writer.println(String.format("Total Contributions:,%.2f", totalContributions));
            writer.println(String.format("Average Benefits per Employee:,%.2f", totalBenefits / employeeCount));

            writer.close();
            System.out.println("Benefits report generated for " + employeeCount + " employees");
            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to generate payslip content
    private void generatePayslipContent(PrintWriter writer, PayrollRecord payroll) throws SQLException {
        String empSql = "SELECT * FROM employees WHERE employee_id = ?";
        PreparedStatement empStmt = connection.prepareStatement(empSql);
        empStmt.setInt(1, payroll.getEmployeeId());
        ResultSet empRs = empStmt.executeQuery();

        if (empRs.next()) {
            writer.println("=========================================");
            writer.println("           MOTORPH PAYSLIP               ");
            writer.println("=========================================");
            writer.println("Employee ID    : " + payroll.getEmployeeId());
            writer.println("Name          : " + empRs.getString("first_name") + " " + empRs.getString("last_name"));
            writer.println("Position      : " + payroll.getPosition());
            writer.println("Payroll Period: " + payroll.getPayrollPeriod());
            writer.println("-----------------------------------------");
            writer.println("EARNINGS:");
            writer.println("  Basic Salary    : ₱ " + String.format("%,10.2f", empRs.getDouble("basic_salary")));
            writer.println("  Rice Subsidy    : ₱ " + String.format("%,10.2f", empRs.getDouble("rice_subsidy")));
            writer.println("  Phone Allowance : ₱ " + String.format("%,10.2f", empRs.getDouble("phone_allowance")));
            writer.println("  Clothing Allow. : ₱ " + String.format("%,10.2f", empRs.getDouble("clothing_allowance")));
            writer.println("  Overtime        : ₱ " + String.format("%,10.2f", payroll.getOvertime()));
            writer.println("                      ---------------");
            writer.println("  Gross Income    : ₱ " + String.format("%,10.2f", payroll.getGrossIncome()));
            writer.println("-----------------------------------------");
            writer.println("DEDUCTIONS:");
            writer.println("  SSS             : ₱ " + String.format("%,10.2f", payroll.getSss()));
            writer.println("  PhilHealth      : ₱ " + String.format("%,10.2f", payroll.getPhilhealth()));
            writer.println("  Pag-IBIG        : ₱ " + String.format("%,10.2f", payroll.getPagibig()));
            writer.println("  Withholding Tax : ₱ " + String.format("%,10.2f", payroll.getTax()));
            writer.println("  Undertime       : ₱ " + String.format("%,10.2f", payroll.getUndertime()));

            double totalDeductions = payroll.getSss() + payroll.getPhilhealth() + 
                                   payroll.getPagibig() + payroll.getTax() + payroll.getUndertime();

            writer.println("                      ---------------");
            writer.println("  Total Deductions: ₱ " + String.format("%,10.2f", totalDeductions));
            writer.println("=========================================");
            writer.println("NET PAY           : ₱ " + String.format("%,10.2f", payroll.getNetPay()));
            writer.println("=========================================");
        }
    }
}
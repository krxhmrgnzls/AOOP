package com.motorph.service;

import com.motorph.util.DatabaseConnection;
import com.motorph.dao.PayrollDAO;
import com.motorph.model.PayrollRecord;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;

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
    
    public File generatePayslipPDF(int employeeId, String payrollPeriod) {
        try {
            String sql = "SELECT e.*, " +
                        "COALESCE(sup.first_name, 'N/A') as supervisor_first_name, " +
                        "COALESCE(sup.last_name, '') as supervisor_last_name " +
                        "FROM employees e " +
                        "LEFT JOIN employees sup ON e.supervisor_id = sup.employee_id " +
                        "WHERE e.employee_id = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
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

                String supervisorName = rs.getString("supervisor_first_name");
                if (!"N/A".equals(supervisorName)) {
                    supervisorName += " " + rs.getString("supervisor_last_name");
                }
                writer.println("Supervisor    : " + supervisorName);
                writer.println("Payroll Period: " + payrollPeriod);
                writer.println("-----------------------------------------");
                writer.println("EARNINGS:");
                writer.println("  Basic Salary    : ₱ " + String.format("%,10.2f", rs.getDouble("basic_salary")));
                writer.println("  Rice Subsidy    : ₱ " + String.format("%,10.2f", rs.getDouble("rice_subsidy")));
                writer.println("  Phone Allowance : ₱ " + String.format("%,10.2f", rs.getDouble("phone_allowance")));
                writer.println("  Clothing Allow. : ₱ " + String.format("%,10.2f", rs.getDouble("clothing_allowance")));
                writer.println("-----------------------------------------");
                writer.println("DEDUCTIONS:");
                writer.println("  SSS            : ₱ " + String.format("%,10.2f", 0.0)); // Calculate based on salary
                writer.println("  PhilHealth     : ₱ " + String.format("%,10.2f", 0.0)); // Calculate based on salary
                writer.println("  Pag-IBIG       : ₱ " + String.format("%,10.2f", 0.0)); // Calculate based on salary
                writer.println("  Income Tax     : ₱ " + String.format("%,10.2f", 0.0)); // Calculate based on salary
                writer.println("-----------------------------------------");

                double grossPay = rs.getDouble("basic_salary") + rs.getDouble("rice_subsidy") + 
                                 rs.getDouble("phone_allowance") + rs.getDouble("clothing_allowance");
                double netPay = grossPay; 

                writer.println("GROSS PAY      : ₱ " + String.format("%,10.2f", grossPay));
                writer.println("NET PAY        : ₱ " + String.format("%,10.2f", netPay));
                writer.println("=========================================");

                writer.close();

                System.out.println("Payslip PDF generated successfully: " + file.getAbsolutePath());

                // Show success message and ask if user wants to open
                int choice = JOptionPane.showConfirmDialog(null,
                    "Payslip saved successfully!\nLocation: " + file.getAbsolutePath() + 
                    "\n\nWould you like to open the file?",
                    "Payslip Generated", 
                    JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        java.awt.Desktop.getDesktop().open(file);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, 
                            "File saved but could not open automatically.\nPlease open: " + file.getAbsolutePath());
                    }
                }

                return file;

            } else {
                System.err.println("Employee not found: " + employeeId);
                JOptionPane.showMessageDialog(null, "Employee data not found!");
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error generating payslip PDF: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating PDF: " + e.getMessage());
            return null;
        }
    }

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
                    15,
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
    
    public File exportToExcel(List<Map<String, Object>> data, String filename) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(filename + ".csv"));
            
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                PrintWriter writer = new PrintWriter(file);
                
                if (!data.isEmpty()) {

                    Map<String, Object> firstRow = data.get(0);
                    writer.println(String.join(",", firstRow.keySet()));

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
            double employerShare = employeeShare * 2; 
            
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
    }
    
    private void generatePagIbigReport(PrintWriter writer, String period) throws SQLException {
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

    public File generateTaxReport() {
        try {
            File reportDir = new File("reports/tax");
            reportDir.mkdirs();

            java.time.LocalDate now = java.time.LocalDate.now();
            String filename = "Tax_Report_" + now.getMonthValue() + "_" + now.getYear() + ".csv";
            File file = new File(reportDir, filename);
            PrintWriter writer = new PrintWriter(file);

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
    
    public File generateJasperPayslipPDF(int employeeId, String payrollPeriod) {
    try {
        // Get employee and payroll data
        String sql = "SELECT e.employee_id, e.first_name, e.last_name, e.position, " +
                    "e.basic_salary, e.rice_subsidy, e.phone_allowance, e.clothing_allowance, " +
                    "p.payroll_period, p.gross_income, p.benefits, p.overtime, p.undertime, " +
                    "p.sss, p.philhealth, p.pagibig, p.tax, p.net_pay " +
                    "FROM employees e " +
                    "LEFT JOIN payroll p ON e.employee_id = p.employee_id " +
                    "WHERE e.employee_id = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, employeeId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            Map<String, Object> parameters = new HashMap<>();

        parameters.put("EMPLOYEE_ID", rs.getInt("employee_id"));
        parameters.put("EMPLOYEE_NAME", rs.getString("first_name") + " " + rs.getString("last_name"));
        parameters.put("POSITION", rs.getString("position"));

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String[] periods = payrollPeriod.split(" to ");
        try {
            parameters.put("PERIOD_FROM", new java.sql.Date(sdf.parse(periods[0]).getTime()));
            parameters.put("PERIOD_TO", new java.sql.Date(sdf.parse(periods[1]).getTime()));
        } catch (Exception e) {
            parameters.put("PERIOD_FROM", new java.sql.Date(System.currentTimeMillis()));
            parameters.put("PERIOD_TO", new java.sql.Date(System.currentTimeMillis()));
        }

        parameters.put("MONTHLY_RATE", rs.getDouble("basic_salary"));
        parameters.put("DAILY_RATE", rs.getDouble("basic_salary") / 22);
        parameters.put("DAYS_WORKED", 22); // You can calculate this from attendance
        parameters.put("OVERTIME", rs.getDouble("overtime"));
        parameters.put("GROSS_INCOME", rs.getDouble("gross_income"));

        parameters.put("RICE_SUBSIDY", rs.getDouble("rice_subsidy"));
        parameters.put("PHONE_ALLOWANCE", rs.getDouble("phone_allowance"));
        parameters.put("CLOTHING_ALLOWANCE", rs.getDouble("clothing_allowance"));
        parameters.put("TOTAL_BENEFITS", rs.getDouble("benefits"));

        parameters.put("SSS", rs.getDouble("sss"));
        parameters.put("PHILHEALTH", rs.getDouble("philhealth"));
        parameters.put("PAG_IBIG", rs.getDouble("pagibig"));
        parameters.put("TAX", rs.getDouble("tax"));
        parameters.put("TOTAL_DEDUCTIONS", rs.getDouble("sss") + rs.getDouble("philhealth") + rs.getDouble("pagibig") + rs.getDouble("tax"));

        parameters.put("TOTAL_NET_INCOME", rs.getDouble("net_pay"));

            
            FileInputStream fileInputStream = new FileInputStream("jarfiles/jar files/PayrollSlip.jrxml");
            JasperDesign jasperDesign = JRXmlLoader.load(fileInputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JREmptyDataSource dataSource = new JREmptyDataSource();
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            File reportDir = new File("reports/payslips");
            reportDir.mkdirs();
            String filename = "MotorPH_Payslip_" + employeeId + "_" + payrollPeriod.replace("/", "-").replace(" ", "_") + ".pdf";
            File pdfFile = new File(reportDir, filename);
            
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile.getAbsolutePath());
            
            System.out.println("JasperReports PDF generated: " + pdfFile.getAbsolutePath());

            JOptionPane.showMessageDialog(null, 
                "MotorPH Payslip PDF generated successfully!\nSaved to: " + pdfFile.getAbsolutePath(),
                "PDF Generated", JOptionPane.INFORMATION_MESSAGE);
            
            return pdfFile;
        }
        
    } catch (Exception e) {
        System.err.println("Error generating JasperReports PDF: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error generating PDF: " + e.getMessage());
    }
    return null;
}
    private String getSupervisorName(int supervisorId) {
        if (supervisorId == 0) {
            return "N/A";
        }

        try {
            String sql = "SELECT CONCAT(first_name, ' ', last_name) as supervisor_name FROM employees WHERE employee_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, supervisorId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("supervisor_name");
            }
        } catch (SQLException e) {
            System.err.println("Error getting supervisor name: " + e.getMessage());
        }

        return "N/A";
    }
}
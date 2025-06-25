package payrollsystem;

import java.util.ArrayList;

interface Payroll {
    static double PHILHEALTH_PERCENT = 0.03;
    double taxCalculation(double totalDeductions);
    double sssCalculation();
    double philhealthCalculation();
    double pagibigCalculation();
    double deductionCalculation(ArrayList<ArrayList<String>> perEmployeeAttendance);
    double grossCalculation(ArrayList<ArrayList<String>> perEmployeeAttendance);
    double benefitsCalculation();
    double netPayrollCalculations(double gross, double benefits, double overtime, double undertime, double totalDeductions);
    double overtimeCalculations(ArrayList<ArrayList<String>> perEmployeeAttendance);
    double undertimeCalculations(ArrayList<ArrayList<String>> perEmployeeAttendance);
}

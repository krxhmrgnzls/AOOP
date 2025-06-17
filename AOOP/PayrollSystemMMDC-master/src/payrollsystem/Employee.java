/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class Employee extends AccountDetails {
    
    AccountDetails accountDetails = new AccountDetails();
    AccountDetails attendance = new AccountDetails();
    AccountDetails leave = new AccountDetails();
    AccountDetails overtime = new AccountDetails();
    AccountDetails balance = new AccountDetails();
    AccountDetails payroll = new AccountDetails();
    
    private String employeeID;
    protected int indexAttendance;
    private String filePath;
    private String dateToday, timeNow;
    private String leaveDays;
    private int numberOfDaysLeave = 0;
    private String balanceVL, balanceSL;
    
    Employee(){
        super();
    }
    
    
    void viewPersonalDetails(String employeeID){
        accountDetails.getDataList().clear();
        accountDetails.setFilePath("CSVFiles//EmployeeDatabase.csv");
        accountDetails.retrivedDetails();
        accountDetails.userDetails(employeeID);
    }
    
    
    //To view all Personal Leave Ledger
    void viewPersonalLeaveLedger(){
        leave.setFilePath("CSVFiles//LeaveRequests.csv");
        leave.retrivedDetails();
    }
    
    //To view
     void viewPersonalOvertime(){
        overtime.setFilePath("CSVFiles//OvertimeRequest.csv");
        overtime.retrivedDetails();
    }

     
     void leaveBalancesInformation(){
         accountDetails.getDataList().clear();
         accountDetails.setFilePath("CSVFiles//LeaveBalances.csv");
         accountDetails.retrivedDetails();
         for(int i=1; i<accountDetails.getDataList().size(); i++){
             if(accountDetails.getDataList().get(i).get(0).equals(accountDetails.getEmployeeID())){
                 this.balanceVL = accountDetails.getDataList().get(i).get(1);
                 this.balanceSL = accountDetails.getDataList().get(i).get(2);
             }
         }
     }
     
    // To format the Local Time and Date Now
    void localDateTimeNow(){
        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        this.dateToday = dateFormat.format(dateNow); 
        
        LocalTime time = LocalTime.now();
        this.timeNow = time.getHour()+":"+time.getMinute();
    }
  
    
    //To Validate Attendance first before adding it in CSV
    boolean validateAttendance(String date){
        for (int i=1; i<accountDetails.getDataList().size(); i++){
            if(accountDetails.getDataList().get(i).get(0).equals(accountDetails.getEmployeeID()) && 
                    accountDetails.getDataList().get(i).get(1).equals(accountDetails.getEmployeeCompleteName()) &&
                    accountDetails.getDataList().get(i).get(2).equals(date)) {
                this.indexAttendance = i;
                return true;
            }
        }
        return false;
    }
    
    //To record the login or time in of the employeee
    void userLogin(){
        accountDetails.getDataList().clear();
        accountDetails.setFilePath("CSVFiles//AttendanceDatabase.csv");
        accountDetails.retrivedDetails();
        
        localDateTimeNow(); // To format the date

        if(validateAttendance(getDateToday())){
            JOptionPane.showMessageDialog(null, "You already made your time-in!!");
        }else{
            String [] newAttendance = {String.valueOf(accountDetails.getEmployeeID()), accountDetails.getEmployeeCompleteName(), getDateToday() , getTimeNow(),"","No", "No"," "};
            ArrayList<String> data = new ArrayList<>();
            data.addAll(Arrays.asList(newAttendance));
            accountDetails.getDataList().add(data);
            accountDetails.addDetailsCSV();
            JOptionPane.showMessageDialog(null, "Successfuly Time-in!!");
        }
    }
    
    //To record the logout or time out of the employee
    void userLogout(){
        accountDetails.getDataList().clear();
        accountDetails.setFilePath("CSVFiles//AttendanceDatabase.csv");
        accountDetails.retrivedDetails();
        localDateTimeNow();
        if(validateAttendance(getDateToday())){
            if(accountDetails.getDataList().get(indexAttendance).size() == 6){
        
                accountDetails.getDataList().get(indexAttendance).add(5, getTimeNow());
                accountDetails.addDetailsCSV();
                JOptionPane.showMessageDialog(null, "Successfuly Time-out!!");
            }else if (accountDetails.getDataList().get(indexAttendance).size() > 5 || attendance.getDataList().get(indexAttendance).get(4).equals("")){
                accountDetails.getDataList().get(indexAttendance).set(4, getTimeNow());
                accountDetails.addDetailsCSV();
                JOptionPane.showMessageDialog(null, "Successfuly Time-out!");
            }
        }else{
            String [] newAttendance = {String.valueOf(accountDetails.getEmployeeID()), accountDetails.getEmployeeCompleteName(), getDateToday(), "",getTimeNow(),"No", "No",""};
            ArrayList<String> data = new ArrayList<>();
            data.addAll(Arrays.asList(newAttendance));
            accountDetails.getDataList().add(data);
            accountDetails.addDetailsCSV(); 
            JOptionPane.showMessageDialog(null, "Successfuly Time-out!");
        }
    }
    
    boolean countNumberOfDays(Date dateFrom, Date dateTo){ // New method to count the days for leave
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTime(dateFrom);
        endDate.setTime(dateTo);
        while(!startDate.after(endDate)){
            if (startDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                this.numberOfDaysLeave++; //To count the days of leave
            }
            // Move to the next day
            startDate.add(Calendar.DATE, 1);
        }
        if(getNumberOfDaysLeave() == 0){
            JOptionPane.showMessageDialog(null, "Invalid date applied!");
            return false;
        }
        return true;
    }
    //To file new leave request
    boolean fileLeaveRequest(ArrayList<String> data){
        localDateTimeNow();
        viewPersonalLeaveLedger();
        
        data.add(2, getDateToday()); //To insert date filed in index 1 of the arraylist data 
        data.add("Pending");
        leave.getDataList().add(data);
        leave.addDetailsCSV(); //To add all data in the LeaveRequest CSV  
        
        data.clear(); //To empty or clear data in array list
        leave.getDataList().clear(); //To empty or clear all data list
        return true;
    }

    
    boolean fileOvertimeRequest(ArrayList<String> data) {
        localDateTimeNow();
        viewPersonalOvertime();
        
        data.add(2, getDateToday());
        data.add(3, "Overtime");
        data.add("Pending");
        overtime.getDataList().add(data);
        overtime.addDetailsCSV();
       
        data.clear();
        overtime.getDataList().clear();
        return true;
    }
    
    ArrayList<ArrayList<String>> getDataAllRequests() {
    accountDetails.getDataList().clear();
    ArrayList<ArrayList<String>> tempData = new ArrayList<>();
    accountDetails.setFilePath("CSVFiles//LeaveRequests.csv");
    accountDetails.retrivedDetails();
    for(int i=0; i<accountDetails.getDataList().size(); i++){
        if(accountDetails.getDataList().get(i).get(0).equals(accountDetails.getEmployeeID()) && accountDetails.getDataList().get(i).get(8).equals("Pending")){
            ArrayList<String> data = new ArrayList<>();
            data.add(accountDetails.getDataList().get(i).get(2)); // Date Filed
            data.add(accountDetails.getDataList().get(i).get(3)); // Type of Request (Leave Type)
            data.add(accountDetails.getDataList().get(i).get(4)); // Period From
            data.add(accountDetails.getDataList().get(i).get(5)); // Period To
            data.add(accountDetails.getDataList().get(i).get(6)); // Number of days
            data.add(accountDetails.getDataList().get(i).get(7)); // Reason
            data.add(accountDetails.getDataList().get(i).get(8)); // Status
            tempData.add(data);
        }
    }
    
    accountDetails.getDataList().clear();
    accountDetails.setFilePath("CSVFiles//OvertimeRequest.csv");
    accountDetails.retrivedDetails();
    for(int i=0; i<accountDetails.getDataList().size(); i++){
        if(accountDetails.getDataList().get(i).get(0).equals(accountDetails.getEmployeeID()) && accountDetails.getDataList().get(i).get(8).equals("Pending")){
            ArrayList<String> data = new ArrayList<>();
            data.add(accountDetails.getDataList().get(i).get(2)); // Date Filed
            data.add(accountDetails.getDataList().get(i).get(3)); // Type of Request (Leave Type)
            data.add(accountDetails.getDataList().get(i).get(4)); // Period From
            data.add(accountDetails.getDataList().get(i).get(5)); // Period To
            data.add(accountDetails.getDataList().get(i).get(6)); // Number of days
            data.add(accountDetails.getDataList().get(i).get(7)); // Reason
            data.add(accountDetails.getDataList().get(i).get(8)); // Status
            tempData.add(data);
        }
    }
    return tempData;
    }
    
    void viewPersonalDTR(Date dateFrom, Date dateTo){
         SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
 
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTime(dateFrom);
        endDate.setTime(dateTo);
        while(!startDate.after(endDate)){
            Date currentDate = startDate.getTime();
            if (startDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                String formattedDate = formatter.format(currentDate);
            }
            // Move to the next day
            startDate.add(Calendar.DATE, 1);
        }
    }
    
    ArrayList<ArrayList<String>> getDataAllDTR(Date fromDate, Date toDate) {
        accountDetails.getDataList().clear();
        accountDetails.setFilePath("CSVFiles//AttendanceDatabase.csv");
        accountDetails.retrivedDetails();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar start = Calendar.getInstance();
        start.setTime(fromDate);
        
        Calendar end = Calendar.getInstance();
        end.setTime(toDate);
        ArrayList<ArrayList<String>> tempData = new ArrayList<>();
        // Loop through each day from fromDate to toDate
        while (!start.after(end)) {
            // Print the current date in your desired format (e.g., "MMM d, yyyy")
            for(int i=1; i<accountDetails.getDataList().size(); i++){
                if(accountDetails.getDataList().get(i).get(0).equals(accountDetails.getEmployeeID()) 
                        && accountDetails.getDataList().get(i).get(2).equals(dateFormat.format(start.getTime()))){
                    ArrayList<String> data = new ArrayList<>();
                    data.add(accountDetails.getDataList().get(i).get(2));
                    data.add(accountDetails.getDataList().get(i).get(3));
                    data.add(accountDetails.getDataList().get(i).get(4));
                    data.add(accountDetails.getDataList().get(i).get(5));
                    data.add(accountDetails.getDataList().get(i).get(7));
                    tempData.add(data);
                    break;
                }
            }
            // Increment the day by one
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        return tempData;
}
    ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
    //load personal details to ensure we have the employee ID
    accountDetails.getDataList().clear();
    accountDetails.setFilePath("CSVFiles//LeaveRequests.csv");
    accountDetails.retrivedDetails();
    ArrayList<ArrayList<String>> tempData = new ArrayList<>();
    for(int i=1; i<accountDetails.getDataList().size(); i++){
        
        if(accountDetails.getDataList().get(i).get(0).equals(accountDetails.getEmployeeID()) && accountDetails.getDataList().get(i).get(8).equals("Approved")){
            String [] list = {accountDetails.getDataList().get(i).get(2), accountDetails.getDataList().get(i).get(3),accountDetails.getDataList().get(i).get(4),
                    accountDetails.getDataList().get(i).get(5),accountDetails.getDataList().get(i).get(6), accountDetails.getDataList().get(i).get(7), 
                    accountDetails.getDataList().get(i).get(8)
            };
            ArrayList<String> row = new ArrayList<>();
            row.addAll(Arrays.asList(list));
            tempData.add(row);
        }
    }
    return tempData;
}

    public void updateLeaveBalanceLabels(javax.swing.JLabel lblVL, javax.swing.JLabel lblSL) {
        // Load personal details to ensure we have the employee ID
    
        // Load leave balances
        leaveBalancesInformation();

        // Update the labels with current balances
        lblVL.setText(getBalanceVL());
        lblSL.setText(getBalanceSL());
}
    ArrayList<ArrayList<String>> viewPersonalPayslip(Date dateFrom, Date dateTo, String id){
        accountDetails.getDataList().clear();
        ArrayList<ArrayList<String>> tempData = new ArrayList<>();
        if(dateFrom == null || dateTo == null){
            JOptionPane.showMessageDialog(null, "Please Provide Payroll Period!");
        }else{
            String fromFormatted = new SimpleDateFormat("MM/dd/yyyy").format(dateFrom);
            String toFormatted = new SimpleDateFormat("MM/dd/yyyy").format(dateTo);
            String datePeriod = fromFormatted + " to " + toFormatted;
            int date = dateFrom.compareTo(dateTo);
            if(date > 0){
                JOptionPane.showMessageDialog(null, "Invalid Payroll Period");
            }else{
                accountDetails.setFilePath("CSVFiles//Payroll.csv");
                accountDetails.retrivedDetails();
                for(int i=0; i<accountDetails.getDataList().size(); i++){
                    if(accountDetails.getDataList().get(i).get(0).equals(id) && accountDetails.getDataList().get(i).get(2).equals(datePeriod) && accountDetails.getDataList().get(i).get(13).equals("Approved")){
                        tempData.add(accountDetails.getDataList().get(i));
                        break;
                    }
                }
            }
        }
        return tempData;
    }    
    
        void forwardDTRToSupervisor(ArrayList<ArrayList <String>> tempData){
        accountDetails.getDataList().clear();
        accountDetails.setFilePath("CSVFiles//AttendanceDatabase.csv");
        accountDetails.retrivedDetails();
        for(int i=0; i<tempData.size(); i++){
            for(int j=0; j<accountDetails.getDataList().size(); j++){
                if(accountDetails.getDataList().get(j).get(0).equals(accountDetails.getEmployeeID()) && tempData.get(i).get(0).equals(accountDetails.getDataList().get(j).get(2)) &&
                       tempData.get(i).get(1).equals(accountDetails.getDataList().get(j).get(5)) ){
                    accountDetails.getDataList().get(j).set(5, "Yes");
                    break;
                }
            }
        }
        accountDetails.addDetailsCSV();
    }
        
    public void updateLeaveRequest() {
    
    }
    
    public void updateOvertimeRequest() {
    
    }
    
    String getDateToday(){
        return dateToday;
    }
    String getTimeNow(){
        return timeNow;
    }

    int getNumberOfDaysLeave(){
        return numberOfDaysLeave;
    }

    String getBalanceVL() {
        return balanceVL;
    }

    String getBalanceSL() {
        return balanceSL;
    }
    
    void setNumberOfDaysLeave(){
        this.numberOfDaysLeave = 0;
    }
    
}
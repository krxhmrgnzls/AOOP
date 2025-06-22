package payrollsystem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List; 
import javax.swing.JOptionPane;

public class HumanResource extends Employee{
    Employee employee = new Employee();
    private String employeeID, selectedName;
    private ArrayList <String> fullName = new ArrayList<>();
    
    HumanResource(String employeeID){
        super();
        this.employeeID = employeeID;
    }
    
    // Keep the original CSV-based nextID method for now
    String nextID(){
        int tempID = 10000; // Start from 10000 as base ID
        employee.getDataList().clear();
        employee.setFilePath("CSVFiles//EmployeeDatabase.csv");
        employee.retrivedDetails();
        
        for(int i=1; i<employee.getDataList().size(); i++){
            if(tempID < Integer.parseInt(employee.getDataList().get(i).get(0))){
                tempID = Integer.parseInt(employee.getDataList().get(i).get(0));
            }
        }
        return String.valueOf(tempID+1);
    }
    
    // the original CSV-based addDetails method 
    boolean addDetails(ArrayList<String> tempData){
        boolean isComplete = true;
        for(String info : tempData){
            if(info.equals("")){
                JOptionPane.showMessageDialog(null, "Please Complete All The Details!");
                isComplete = false;
                break;
            }
        }
        
        if(isComplete){
            boolean isValid = true;
            employee.getDataList().clear();
            employee.setFilePath("CSVFiles//EmployeeDatabase.csv");
            employee.retrivedDetails();
            
            for(int i=1; i<employee.getDataList().size(); i++){
                if(employee.getDataList().get(i).get(1).equals(tempData.get(2)) && 
                   employee.getDataList().get(i).get(2).equals(tempData.get(1))){
                    isValid = false;
                    isComplete = false;
                    JOptionPane.showMessageDialog(null, "Cannot Be Add New Employee Due To Employee Already Exist!");
                    break;
                }
            }
            
            if(isValid){
                employee.getDataList().add(tempData);
                employee.addDetailsCSV();
                JOptionPane.showMessageDialog(null, "Successfully Added New Employee!");
            }
        }
        return isComplete;
    }
    
    // Database version of nextID (commented out until EmployeeDAO is implemented)
    /*
    String nextIDFromDatabase() {
        EmployeeDAO dao = new EmployeeDAO();
        List<Employee> employees = dao.findAll();
        int maxId = 10000; // Starting ID

        for (Employee emp : employees) {
            int currentId = Integer.parseInt(emp.getEmployeeID());
            if (currentId > maxId) {
                maxId = currentId;
            }
        }

        return String.valueOf(maxId + 1);
    }
    */
    
    // Database version of addDetails (commented out until EmployeeDAO is implemented)
    /*
    boolean addDetailsToDatabase(ArrayList<String> tempData) {
        boolean isComplete = true;
        for (String info : tempData) {
            if (info.equals("")) {
                JOptionPane.showMessageDialog(null, "Please Complete All The Details!");
                isComplete = false;
                break;
            }
        }

        if (isComplete) {
            Employee newEmployee = new Employee();
            // Set all employee properties from tempData
            newEmployee.accountDetails.setLastName(tempData.get(2));
            newEmployee.accountDetails.setFirstName(tempData.get(1));
            // ... set other properties

            EmployeeDAO dao = new EmployeeDAO();
            if (dao.create(newEmployee)) {
                JOptionPane.showMessageDialog(null, "Successfully Added New Employee!");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add employee!");
                return false;
            }
        }
        return false;
    }
    */
    
    void updateDetails(){
        
    }
    void deleteDetails(){
        
    }
    
    ArrayList<ArrayList<String>> allCredentials(){
        employee.getDataList().clear();
        employee.setFilePath("CSVFiles//CredentialsDatabase.csv");
        employee.retrivedDetails();
        ArrayList<ArrayList<String>> tempData = new ArrayList<>();
        for(int i=1; i<employee.getDataList().size(); i++){
            employee.getDataList().get(i).remove(2);
            tempData.add(employee.getDataList().get(i));
        }
        
        return tempData;
    }
    
    ArrayList<ArrayList<String>> displayAllDetails(){
        employee.getDataList().clear();
        employee.setFilePath("CSVFiles//EmployeeDatabase.csv");
        employee.retrivedDetails();
        employee.getDataList().remove(0);
        return employee.getDataList();
    }
    
    boolean validateDateBirthday(Date dateBirthday){
        boolean isValid = true;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateBirthday);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int days = calendar.get(Calendar.DATE);

        
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        int currentDay = currentDate.getDayOfMonth();
        System.out.println(currentYear);
        System.out.println(currentMonth);
        System.out.println(currentDay);
        
        int legalAge = 18;
        int yearDifference = currentYear - year;
        int monthDifference = currentMonth - month;
        int daysDifference = currentDay -days;
        
        if(yearDifference >= legalAge){   // if  5 >= 5;
            System.out.println("Age gap : "+yearDifference);
            if(yearDifference == legalAge){ // 5==5
                if(currentMonth >= month){    // 3>=3
                    if(currentMonth == month){ //3==3
                        if(currentDay >= days) //21 >= 20
                            isValid = true;
                        else
                            isValid = false;
                    }
                }else
                  isValid = true;  
                
            }
        }else
            isValid = false;
        
        return isValid;
    }

    ArrayList<ArrayList<String>> displayAllCredentials(){
        employee.getDataList().clear();
        employee.setFilePath("CSVFiles//CredentialsDatabase.csv");
        employee.retrivedDetails();
        employee.getDataList().remove(0);
        return employee.getDataList();
    }
    

    void getEmployeeNames(){  
        employee.getDataList().clear();
        employee.getNewData().clear();
        fullName.clear();
        employee.setFilePath("CSVFiles//EmployeeDatabase.csv");
        employee.retrivedDetails();
        for(int i=1; i<employee.getDataList().size(); i++){
            ArrayList <String> names = new ArrayList<>();
            names.add(employee.getDataList().get(i).get(0));
            names.add(employee.getDataList().get(i).get(1) + " "+employee.getDataList().get(i).get(2));
            employee.getIdAndNames().add(names); // Fixed: use employee.getIdAndNames()
            fullName.add(employee.getDataList().get(i).get(1) + " "+employee.getDataList().get(i).get(2));
        }
           
        Collections.sort(fullName);
        employee.getNewData().add(fullName); // Fixed: use employee.getNewData()
    }
    
    String getID(){
        employee.getDataList().clear();
        String id = "";
        for(ArrayList<String> idName : employee.getIdAndNames()){ // Fixed: use employee.getIdAndNames()
           if(idName.get(1).equals(getSelectedName())){
               id = idName.get(0);
           }
        }
        return id;
    }
    
    boolean addNewCredentials(ArrayList<String> tempData){
        boolean isValid = true;
        for(String info : tempData){
            if(info.equals("")){
                isValid = false;
                break;
            }
        }
        if(!isValid){
            JOptionPane.showMessageDialog(null, "Please Provide All The Necessary Information!");
            isValid = false;
        }else{
            employee.getDataList().clear();
            employee.setFilePath("CSVFiles//CredentialsDatabase.csv");
            employee.retrivedDetails();
            for(int i=1; i<employee.getDataList().size(); i++){
                if(tempData.get(0).equals(employee.getDataList().get(i).get(0)) && tempData.get(3).equals(employee.getDataList().get(i).get(3))){
                    JOptionPane.showMessageDialog(null, "Cannot Be Add New Credentials Due To Employee Already Exist With The Same Role!");
                    isValid = false;
                    break;
                }
            }
            if(isValid){
                employee.getDataList().add(tempData);
                employee.addDetailsCSV();
                JOptionPane.showMessageDialog(null, "New Credentials Added!");
            }
        }
        return isValid;
    }
    
   void setSelectedName(String selectedName){
       this.selectedName = selectedName;
   }
      
   String getSelectedName(){
       return this.selectedName;
   } 
}
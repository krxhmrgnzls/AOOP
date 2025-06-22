package payrollsystem;

import java.util.ArrayList;

abstract class Credentials extends AccountDetails {
    protected String userID, userPassword; // Changed from private to protected
    
    AccountDetails accountDetails = new AccountDetails();
    
    Credentials(String id, String password){
        this.userID = id;
        this.userPassword = password;
    }
    
    // Add getter methods
    public String getUserID() {
        return userID;
    }
    
    public String getUserPassword() {
        return userPassword;
    }
    
    ArrayList<ArrayList<String>> checkCredentials(){
        ArrayList<ArrayList<String>> tempData = new ArrayList<>();
        accountDetails.setFilePath("CSVFiles//CredentialsDatabase.csv");
        accountDetails.retrivedDetails();
        for(ArrayList<String> data : accountDetails.getDataList()){
            if(data.get(0).equals(userID) && data.get(2).equals(userPassword)){
                tempData.add(data);
            }
        }
        return tempData;
    }
}
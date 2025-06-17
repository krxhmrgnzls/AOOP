/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem;

import java.util.ArrayList;

/**
 *
 * @author Paul
 */
abstract class Credentials extends AccountDetails {
    private String userID, userPassword;
    
    AccountDetails accountDetails = new AccountDetails();
    
    Credentials(String id, String password){
        this.userID = id;
        this.userPassword = password;
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

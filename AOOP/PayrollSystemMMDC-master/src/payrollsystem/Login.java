/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem;

import java.util.ArrayList;


public class Login extends Credentials {

    Login(String id, String password){
        super(id, password);
    }
    
    @Override
    ArrayList<ArrayList<String>> checkCredentials(){
        return super.checkCredentials();
    }
   
}

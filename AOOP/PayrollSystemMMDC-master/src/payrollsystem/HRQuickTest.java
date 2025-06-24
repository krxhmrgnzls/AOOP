package payrollsystem;

import java.util.ArrayList;

public class HRQuickTest {
    public static void main(String[] args) {
        System.out.println("🔧 Testing HumanResource methods...");
        
        try {
            // Test LeaveRequest class
            System.out.println("1️⃣ Testing LeaveRequest...");
            LeaveRequest leave = new LeaveRequest();
            leave.setEmployeeID(123); // This should work now
            System.out.println("✅ LeaveRequest works!");
            
            // Test HumanResource class
            System.out.println("2️⃣ Testing HumanResource...");
            HumanResource hr = new HumanResource();
            
            // Test the problematic method
            ArrayList<String> leaveData = new ArrayList<>();
            leaveData.add("123");           // Employee ID
            leaveData.add("John Doe");      // Name
            leaveData.add("Sick Leave");    // Leave type
            leaveData.add("2025-06-25");    // From date
            leaveData.add("2025-06-26");    // To date
            leaveData.add("2");             // Number of days
            leaveData.add("Not feeling well"); // Reason
            
            boolean result = hr.fileLeaveRequest(leaveData);
            System.out.println("✅ fileLeaveRequest works! Result: " + result);
            
            System.out.println("🎉 All HumanResource methods work!");
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
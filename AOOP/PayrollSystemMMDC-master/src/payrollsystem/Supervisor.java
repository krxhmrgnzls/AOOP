package payrollsystem;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class Supervisor extends Employee {
    private List<AccountDetails> teamMembers;
    private EmployeeDAO employeeDAO;
    private AttendanceDAO attendanceDAO;
    private LeaveDAO leaveDAO;
    
    public Supervisor() {
        super();
        this.teamMembers = new ArrayList<>();
        this.employeeDAO = new EmployeeDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.leaveDAO = new LeaveDAO();
    }
    
    // ADD: Constructor that GUI expects
    public Supervisor(String employeeId) {
        super(employeeId);  // Call Employee constructor with string
        this.teamMembers = new ArrayList<>();
        this.employeeDAO = new EmployeeDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.leaveDAO = new LeaveDAO();
    }
    
    public Supervisor(ArrayList<ArrayList<String>> userDetails) {
        super();
        this.teamMembers = new ArrayList<>();
        this.employeeDAO = new EmployeeDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.leaveDAO = new LeaveDAO();
        
        if (userDetails != null && !userDetails.isEmpty()) {
            ArrayList<String> details = userDetails.get(0);
            if (details.size() >= 3) {
                this.accountDetails.setEmployeeID(Integer.parseInt(details.get(0)));
                this.accountDetails.setFirstName(details.get(1));
                this.accountDetails.setLastName(details.get(2));
            }
        }
    }
    
    // Team management
    public List<AccountDetails> getTeamMembers() {
        return teamMembers;
    }
    
    public void setTeamMembers(List<AccountDetails> teamMembers) {
        this.teamMembers = teamMembers;
    }
    
    // Approve work schedule/attendance
    public void approveWorkSchedule() {
        try {
            // Get pending attendance for approval
            List<AttendanceRecord> pendingAttendance = attendanceDAO.getAttendanceForPayroll("current");
            
            for (AttendanceRecord attendance : pendingAttendance) {
                if (!attendance.isSubmittedToSupervisor()) {
                    attendanceDAO.submitToSupervisor(attendance.getAttendanceId());
                }
            }
            
            System.out.println("✅ Work schedules approved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Approve leave requests
    public boolean approveLeaveRequest(int leaveId, boolean approve) {
        try {
            String status = approve ? "Approved" : "Rejected";
            return leaveDAO.updateLeaveStatus(leaveId, status);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get pending leave requests for this supervisor's team
    public List<LeaveRequest> getPendingLeaveRequests() {
        try {
            // Get all pending leaves - in a real system, filter by supervisor's team
            return leaveDAO.getPendingLeaveRequests();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // Generate team report
    public void generateTeamReport() {
        try {
            List<AccountDetails> team = getTeamData();
            System.out.println("📊 Team Report:");
            System.out.println("Team Size: " + team.size());
            
            for (AccountDetails member : team) {
                System.out.println("- " + member.getFirstName() + " " + member.getLastName() + 
                                 " (" + member.getPosition() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Get team data
    public List<AccountDetails> getTeamData() {
        try {
            // In a real system, filter employees by supervisor ID
            return employeeDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // Override viewPersonalDetails
    public void viewPersonalDetails() {
        if (accountDetails != null) {
            System.out.println("Supervisor Personal Details:");
            System.out.println("ID: " + accountDetails.getEmployeeID());
            System.out.println("Name: " + accountDetails.getFirstName() + " " + accountDetails.getLastName());
            System.out.println("Position: " + accountDetails.getPosition());
            System.out.println("Team Size: " + teamMembers.size());
        }
    }
    
    // Get team attendance summary
    public void getTeamAttendanceSummary() {
        try {
            List<AccountDetails> team = getTeamData();
            for (AccountDetails member : team) {
                java.util.Date startDate = new java.util.Date(); // Set appropriate date range
                java.util.Date endDate = new java.util.Date();
                
                java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
                java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
                
                AttendanceSummary summary = attendanceDAO.getAttendanceSummary(
                    member.getEmployeeID(), sqlStartDate, sqlEndDate);
                
                System.out.println(member.getFirstName() + " " + member.getLastName() + ": " + summary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Bulk approve attendance
    public boolean bulkApproveAttendance(List<Integer> attendanceIds) {
        try {
            return attendanceDAO.bulkSubmitToPayroll(attendanceIds);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // **ADD: Missing methods for Supervisor GUI**
    
    public ArrayList<ArrayList<String>> getAllRequestData(String requestType) {
        // Return request data based on type
        if (requestType.equals("Leave Request")) {
            return getDataAllRequests();
        } else {
            return new ArrayList<>();
        }
    }
    
    public void setTableSize(int size) {
        // Placeholder method
    }
    
    public void displayDataTable(javax.swing.JTable table) {
        if (newData != null && !newData.isEmpty()) {
            String[] columnNames = {"ID", "Name", "Type", "Date", "Status", "Days", "Reason"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0);
            
            for (ArrayList<String> row : newData) {
                Object[] rowData = row.toArray();
                model.addRow(rowData);
            }
            table.setModel(model);
        }
    }
    
    public void setTableData(ArrayList<ArrayList<String>> data) {
        this.newData = data;
    }
    
    // Overloaded setTableData method (no parameters)
    public void setTableData() {
        // Load default data
        setTableData(getDataAllRequests());
    }
    
    public ArrayList<String> list = new ArrayList<>(); // Add this property
    
    public void approvedEmployeeRequest(String action) {
        try {
            if (action.equals("Approve")) {
                System.out.println("Request approved");
            } else {
                System.out.println("Request rejected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getEmployeeNames() {
    if (newData == null) {
        newData = new ArrayList<>();
    }
    newData.clear();
    try {
        List<AccountDetails> employees = employeeDAO.findAll();
        for (AccountDetails emp : employees) {
            ArrayList<String> empData = new ArrayList<>();
            empData.add(emp.getLastName() + ", " + emp.getFirstName());
            newData.add(empData);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public ArrayList<ArrayList<String>> getNewData() {
        if (newData == null) {
            newData = new ArrayList<>();
        }
        return newData;
    }

    public void setData() {
        // Initialize or reset data
        if (newData == null) {
            newData = new ArrayList<>();
        }
        System.out.println("Data set/reset for supervisor");
    }

    public ArrayList<ArrayList<String>> getAllApprovedPersonalLeaveLedger() {
        ArrayList<ArrayList<String>> leaveData = new ArrayList<>();
        try {
            List<LeaveRequest> approvedLeaves = leaveDAO.getPendingLeaveRequests();
            for (LeaveRequest leave : approvedLeaves) {
                if (leave.getStatus().equals("Approved")) {
                    ArrayList<String> row = new ArrayList<>();
                    row.add(String.valueOf(leave.getEmployeeId()));
                    row.add(leave.getEmployeeName());
                    row.add(leave.getLeaveType());
                    row.add(leave.getFromDate().toString());
                    row.add(leave.getToDate().toString());
                    row.add(String.valueOf(leave.getNumberOfDays()));
                    row.add(leave.getReason());
                    leaveData.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leaveData;
    }

    // Override the fileLeaveRequest method to accept ArrayList parameter
    @Override
    public boolean fileLeaveRequest(ArrayList<String> leaveData) {
        try {
            LeaveRequest leave = new LeaveRequest();
            leave.setEmployeeID(Integer.parseInt(leaveData.get(0)));
            leave.setLeaveType(leaveData.get(2));
            leave.setReason(leaveData.get(6));

            return leaveDAO.createLeaveRequest(leave);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public ArrayList<ArrayList<String>> getDataList() {
        return newData;
    }
    
    public void setSelectedName(String selectedName) {
        System.out.println("Selected employee: " + selectedName);
    }
    
    public ArrayList<ArrayList<String>> getDataForDTRTable() {
        return getDataAllDTR(new java.util.Date(), new java.util.Date());
    }
    
    public void forwardDTR(ArrayList<ArrayList<String>> dtrData) {
        System.out.println("DTR forwarded by supervisor");
    }
    
    public ArrayList<ArrayList<String>> allApprovedPersonalLeaveLedger() {
        return getAllApprovedPersonalLeaveLedger();
    }
    
    public void forwardDTRToSupervisor(ArrayList<ArrayList<String>> dtrData) {
        System.out.println("DTR forwarded to supervisor");
    }
}
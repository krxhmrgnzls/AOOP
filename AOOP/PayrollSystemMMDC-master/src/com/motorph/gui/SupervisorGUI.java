package com.motorph.gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.motorph.dao.AttendanceResult;
import com.motorph.service.AttendanceService;
import com.motorph.dao.AttendanceStatus;
import com.motorph.service.ReportGenerator;

public class SupervisorGUI extends javax.swing.JFrame {
    String id, name, role;
    Supervisor supervisor;
    ArrayList<String> data = new ArrayList<>(); 
    SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    private AttendanceService attendanceService;
    
    public SupervisorGUI(ArrayList<ArrayList<String>> userDetails) {
        initComponents();
        this.id = userDetails.get(0).get(0);
        this.name = userDetails.get(0).get(1);
        this.role = userDetails.get(0).get(3);
        
        lblNameSidebar.setText(name);
        lblIDSidebar.setText(id);
        
        supervisor = new Supervisor(id);
        supervisor.viewPersonalDetails(id);
        setupEmployeeRequestTable();
        setupEmployeeDTRTable(); 
        setupTableSelection();
        
        
    }
    
    private void setupEmployeeRequestTable() {
    try {
        System.out.println("=== Setting up Employee Request Table ===");
        
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "ID", "NAME", "DATE FILED", "TYPE OF REQUEST", 
                "PERIOD FROM", "PERIOD TO", "NUMBER OF DAYS", 
                "REASON", "STATUS", "REAL_ID"
            }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        jTableAllRequest1.setModel(model);

        jTableAllRequest1.setRowHeight(25);
        jTableAllRequest1.getTableHeader().setReorderingAllowed(false);

        if (jTableAllRequest1.getColumnCount() >= 10) {
            jTableAllRequest1.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
            jTableAllRequest1.getColumnModel().getColumn(1).setPreferredWidth(150); // NAME
            jTableAllRequest1.getColumnModel().getColumn(2).setPreferredWidth(100); // DATE FILED
            jTableAllRequest1.getColumnModel().getColumn(3).setPreferredWidth(120); // TYPE
            jTableAllRequest1.getColumnModel().getColumn(4).setPreferredWidth(100); // FROM
            jTableAllRequest1.getColumnModel().getColumn(5).setPreferredWidth(100); // TO
            jTableAllRequest1.getColumnModel().getColumn(6).setPreferredWidth(80);  // DAYS
            jTableAllRequest1.getColumnModel().getColumn(7).setPreferredWidth(150); // REASON
            jTableAllRequest1.getColumnModel().getColumn(8).setPreferredWidth(80);  // STATUS

            jTableAllRequest1.getColumnModel().getColumn(9).setMinWidth(0);
            jTableAllRequest1.getColumnModel().getColumn(9).setMaxWidth(0);
            jTableAllRequest1.getColumnModel().getColumn(9).setWidth(0);
            jTableAllRequest1.getColumnModel().getColumn(9).setPreferredWidth(0);
        }
        
        System.out.println("Employee request table setup completed with " + 
                          jTableAllRequest1.getColumnCount() + " columns");
        
    } catch (Exception e) {
        System.err.println("Error setting up employee request table: " + e.getMessage());
        e.printStackTrace();
    }
}
    

    private SupervisorGUI() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
     private String fixNumber(String number) {
        if (number == null || number.isEmpty()) {
            return "";
        }
        
        try {
            if (number.toUpperCase().contains("E")) {
                double scientificNumber = Double.parseDouble(number);
                return String.format("%.0f", scientificNumber);
            } else {
                return number;
            }
        } catch (NumberFormatException e) {
            return number;
        }
    }

    private void setDateTextField(javax.swing.JTextField textField, java.util.Date date) {
        if (date != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            textField.setText(sdf.format(date));
        } else {
            textField.setText("");
        }
    }
    private java.util.Date showDatePicker(String message) {
        String dateStr = JOptionPane.showInputDialog(message + " (MM/DD/YYYY)");
        if (dateStr != null) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
                return sdf.parse(dateStr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use MM/DD/YYYY");
                return null;
            }
        }
        return null;
    }
    
      private void clearLeaveForm() {
        comboLeaveType.setSelectedIndex(0);
        dateFrom.setDate(null);
        dateTo.setDate(null);
        txtDaysNumber.setText("0");
        txtReason.setText("");
    }
    
    private void clearOvertimeForm() {
        dateFromOvertime.setDate(null);
        dateToOvertime.setDate(null);
        txtDaysNumber1.setText("0");
        txtReasonOvertime.setText("");
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        mainTabbed = new javax.swing.JTabbedPane();
        tabbedPersonalDetails = new javax.swing.JPanel();
        panelPersonalDetails1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblEmpID = new javax.swing.JLabel();
        lblFName = new javax.swing.JLabel();
        lblLName = new javax.swing.JLabel();
        lblBDay = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblPhoneNum = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        txtFName = new javax.swing.JTextField();
        txtLName = new javax.swing.JTextField();
        txtBDay = new javax.swing.JTextField();
        txtPhoneNum = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaAddress = new javax.swing.JTextArea();
        panelPersonalDetails = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lblBasicSalary = new javax.swing.JLabel();
        lblRiceSubsidy = new javax.swing.JLabel();
        lblPhoneAllowances = new javax.swing.JLabel();
        lblClothingAllowanes = new javax.swing.JLabel();
        txtBasicSalary = new javax.swing.JTextField();
        txtRiceSubsidy = new javax.swing.JTextField();
        txtPhoneAllowance = new javax.swing.JTextField();
        txtClothingAllowance = new javax.swing.JTextField();
        lblBiMonthlyRate = new javax.swing.JLabel();
        txtBiMonthlyRate = new javax.swing.JTextField();
        lblHourlyRate = new javax.swing.JLabel();
        txtHourlyRate = new javax.swing.JTextField();
        panelPersonalDetails2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblPhilNum = new javax.swing.JLabel();
        lblSSSNum = new javax.swing.JLabel();
        lblTINNum = new javax.swing.JLabel();
        lblPagIbigNum = new javax.swing.JLabel();
        txtPhilNum = new javax.swing.JTextField();
        txtSSSNum = new javax.swing.JTextField();
        txtTINNum = new javax.swing.JTextField();
        txtPagIbigNum = new javax.swing.JTextField();
        panelPersonalDetails3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblPosition = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblSupervisor = new javax.swing.JLabel();
        txtPosition = new javax.swing.JTextField();
        txtStatus = new javax.swing.JTextField();
        txtSupervisor = new javax.swing.JTextField();
        tabbedRequest = new javax.swing.JPanel();
        panelTypeRequest = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblRequestType = new javax.swing.JLabel();
        comboTypeRequest = new javax.swing.JComboBox<>();
        tabbedInsideRequest = new javax.swing.JTabbedPane();
        panelAllRequest = new javax.swing.JPanel();
        lblAllRequest = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableAllRequest = new javax.swing.JTable();
        jSeparator6 = new javax.swing.JSeparator();
        panelLeaveRequestDetails = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtReason = new javax.swing.JTextArea();
        dateTo = new com.toedter.calendar.JDateChooser();
        dateFrom = new com.toedter.calendar.JDateChooser();
        lblMyName1 = new javax.swing.JLabel();
        txtDaysNumber = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        lblSLBalance = new javax.swing.JLabel();
        lblVLBalance = new javax.swing.JLabel();
        lblSL = new javax.swing.JLabel();
        lblVL = new javax.swing.JLabel();
        lblLeaveBalances = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        lblID = new javax.swing.JLabel();
        lblMyName = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblEmpID1 = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        comboLeaveType = new javax.swing.JComboBox<>();
        panelOvertimeRequest = new javax.swing.JPanel();
        lblEmpID2 = new javax.swing.JLabel();
        lblID1 = new javax.swing.JLabel();
        lblName1 = new javax.swing.JLabel();
        lblMyName2 = new javax.swing.JLabel();
        dateFromOvertime = new com.toedter.calendar.JDateChooser();
        dateToOvertime = new com.toedter.calendar.JDateChooser();
        lblMyName3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtReasonOvertime = new javax.swing.JTextArea();
        btnSubmit1 = new javax.swing.JButton();
        lblLeaveBalances1 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        txtDaysNumber1 = new javax.swing.JTextField();
        tabbedDTR = new javax.swing.JPanel();
        panelDTR = new javax.swing.JPanel();
        tableDTR = new javax.swing.JScrollPane();
        jTableAllDTR = new javax.swing.JTable();
        dateFrom2 = new com.toedter.calendar.JDateChooser();
        dateTo2 = new com.toedter.calendar.JDateChooser();
        lblPeriod = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btnSubmitToSepervisor = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        lblName2 = new javax.swing.JLabel();
        lblMyName4 = new javax.swing.JLabel();
        lblEmpID3 = new javax.swing.JLabel();
        lblID2 = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        tabbedLeaveLedger = new javax.swing.JPanel();
        panelLeaveLedger = new javax.swing.JPanel();
        tableDTR1 = new javax.swing.JScrollPane();
        jTableAllRequest3 = new javax.swing.JTable();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        lblSL1 = new javax.swing.JLabel();
        lblSLBalance1 = new javax.swing.JLabel();
        lblVL1 = new javax.swing.JLabel();
        lblVLBalance1 = new javax.swing.JLabel();
        lblMyName5 = new javax.swing.JLabel();
        lblName3 = new javax.swing.JLabel();
        lblEmpID4 = new javax.swing.JLabel();
        lblID3 = new javax.swing.JLabel();
        lblLeaveBalances2 = new javax.swing.JLabel();
        tabbedPayslip = new javax.swing.JPanel();
        panelPayslip = new javax.swing.JPanel();
        jSeparator12 = new javax.swing.JSeparator();
        lblMyName6 = new javax.swing.JLabel();
        lblName4 = new javax.swing.JLabel();
        lblEmpID5 = new javax.swing.JLabel();
        lblID4 = new javax.swing.JLabel();
        dateFrom3 = new com.toedter.calendar.JDateChooser();
        dateTo3 = new com.toedter.calendar.JDateChooser();
        btnReport = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        lblName18 = new javax.swing.JLabel();
        lblName7 = new javax.swing.JLabel();
        lblName8 = new javax.swing.JLabel();
        lblName9 = new javax.swing.JLabel();
        lblName10 = new javax.swing.JLabel();
        lblName11 = new javax.swing.JLabel();
        lblName12 = new javax.swing.JLabel();
        lblName13 = new javax.swing.JLabel();
        lblName14 = new javax.swing.JLabel();
        lblName15 = new javax.swing.JLabel();
        lblName16 = new javax.swing.JLabel();
        lblName19 = new javax.swing.JLabel();
        lblName21 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        lblGross = new javax.swing.JLabel();
        lblBenefits = new javax.swing.JLabel();
        lblOvertime = new javax.swing.JLabel();
        lblUndertime = new javax.swing.JLabel();
        lblSSS = new javax.swing.JLabel();
        lblPhilHealth = new javax.swing.JLabel();
        lblPagIbig = new javax.swing.JLabel();
        lblTax = new javax.swing.JLabel();
        lblNetPay = new javax.swing.JLabel();
        lblPayrollPeriod = new javax.swing.JLabel();
        lblPositon = new javax.swing.JLabel();
        tabbedEmployeeRequest = new javax.swing.JPanel();
        panelTypeRequest1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        lblRequestType1 = new javax.swing.JLabel();
        comboTypeRequest1 = new javax.swing.JComboBox<>();
        tabbedInsideRequest1 = new javax.swing.JTabbedPane();
        panelAllRequest1 = new javax.swing.JPanel();
        lblAllRequest1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableAllRequest1 = new javax.swing.JTable();
        jSeparator16 = new javax.swing.JSeparator();
        btnUpdate1 = new javax.swing.JButton();
        btnCancel3 = new javax.swing.JButton();
        tabbedEmployeeDTR = new javax.swing.JPanel();
        panelTypeRequest2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        lblRequestType2 = new javax.swing.JLabel();
        comboEmployeeName = new javax.swing.JComboBox<>();
        tabbedInsideRequest2 = new javax.swing.JTabbedPane();
        panelAllRequest2 = new javax.swing.JPanel();
        lblAllRequest2 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableDTR = new javax.swing.JTable();
        jSeparator17 = new javax.swing.JSeparator();
        btnForwardToPayroll = new javax.swing.JButton();
        sideBarPanel = new javax.swing.JPanel();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        lblNameSidebar = new javax.swing.JLabel();
        lblName5 = new javax.swing.JLabel();
        lblIDSidebar = new javax.swing.JLabel();
        btnPersonalDetails = new javax.swing.JButton();
        btnRequestPort = new javax.swing.JButton();
        btnDTR = new javax.swing.JButton();
        btnLeaveLedger = new javax.swing.JButton();
        btnpayslip = new javax.swing.JButton();
        btnemployeeRequest = new javax.swing.JButton();
        btnemployeeDTR = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SUPERVISOR PORTAL");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setForeground(new java.awt.Color(0, 34, 195));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 1)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/SP.png"))); // NOI18N
        jPanel8.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 460, 70));

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1750, 100));

        tabbedPersonalDetails.setBackground(new java.awt.Color(255, 255, 255));
        tabbedPersonalDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 0)));

        panelPersonalDetails1.setBackground(new java.awt.Color(255, 255, 255));
        panelPersonalDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Personal Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        lblEmpID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEmpID.setText("Employee ID:");

        lblFName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFName.setText("First Name:");

        lblLName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblLName.setText("Last Name:");

        lblBDay.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBDay.setText("Birthday:");

        lblAddress.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblAddress.setText("Address: ");

        lblPhoneNum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPhoneNum.setText("Phone No.");

        txtID.setEditable(false);
        txtID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtFName.setEditable(false);
        txtFName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtLName.setEditable(false);
        txtLName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtBDay.setEditable(false);
        txtBDay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtPhoneNum.setEditable(false);
        txtPhoneNum.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        textAreaAddress.setEditable(false);
        textAreaAddress.setColumns(20);
        textAreaAddress.setRows(5);
        jScrollPane1.setViewportView(textAreaAddress);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblPhoneNum, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblBDay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblLName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lblAddress))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(txtLName)
                            .addComponent(txtBDay)
                            .addComponent(txtPhoneNum)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblFName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEmpID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txtID, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFName)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmpID)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFName)
                    .addComponent(txtFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLName)
                    .addComponent(txtLName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBDay)
                    .addComponent(txtBDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPhoneNum)
                    .addComponent(txtPhoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAddress)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelPersonalDetails1Layout = new javax.swing.GroupLayout(panelPersonalDetails1);
        panelPersonalDetails1.setLayout(panelPersonalDetails1Layout);
        panelPersonalDetails1Layout.setHorizontalGroup(
            panelPersonalDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalDetails1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelPersonalDetails1Layout.setVerticalGroup(
            panelPersonalDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalDetails1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPersonalDetails.setBackground(new java.awt.Color(255, 255, 255));
        panelPersonalDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Salaries and Allowances", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        lblBasicSalary.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBasicSalary.setText("Basic Salary :");

        lblRiceSubsidy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblRiceSubsidy.setText("Rice Subsidy: ");

        lblPhoneAllowances.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPhoneAllowances.setText("Phone Allowances: ");

        lblClothingAllowanes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblClothingAllowanes.setText("Clothing Allowances: ");

        txtBasicSalary.setEditable(false);
        txtBasicSalary.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtBasicSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBasicSalaryActionPerformed(evt);
            }
        });

        txtRiceSubsidy.setEditable(false);
        txtRiceSubsidy.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtRiceSubsidy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRiceSubsidyActionPerformed(evt);
            }
        });

        txtPhoneAllowance.setEditable(false);
        txtPhoneAllowance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPhoneAllowance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneAllowanceActionPerformed(evt);
            }
        });

        txtClothingAllowance.setEditable(false);
        txtClothingAllowance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtClothingAllowance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClothingAllowanceActionPerformed(evt);
            }
        });

        lblBiMonthlyRate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBiMonthlyRate.setText("BiMonthly Rate:");

        txtBiMonthlyRate.setEditable(false);
        txtBiMonthlyRate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblHourlyRate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblHourlyRate.setText("Hourly Rate:");

        txtHourlyRate.setEditable(false);
        txtHourlyRate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(lblBiMonthlyRate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(36, 36, 36)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblHourlyRate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtHourlyRate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(txtBiMonthlyRate, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtBasicSalary, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblClothingAllowanes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPhoneAllowances, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblRiceSubsidy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPhoneAllowance, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addComponent(txtClothingAllowance, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtRiceSubsidy, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(6, 6, 6))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBasicSalary)
                    .addComponent(txtBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBiMonthlyRate)
                    .addComponent(txtBiMonthlyRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHourlyRate)
                    .addComponent(txtHourlyRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRiceSubsidy)
                    .addComponent(txtRiceSubsidy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhoneAllowances)
                    .addComponent(txtPhoneAllowance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblClothingAllowanes)
                    .addComponent(txtClothingAllowance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelPersonalDetailsLayout = new javax.swing.GroupLayout(panelPersonalDetails);
        panelPersonalDetails.setLayout(panelPersonalDetailsLayout);
        panelPersonalDetailsLayout.setHorizontalGroup(
            panelPersonalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        panelPersonalDetailsLayout.setVerticalGroup(
            panelPersonalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalDetailsLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPersonalDetails2.setBackground(new java.awt.Color(255, 255, 255));
        panelPersonalDetails2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Government IDs", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        lblPhilNum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPhilNum.setText("PhilHealth No.");

        lblSSSNum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSSSNum.setText("SSS No.");

        lblTINNum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTINNum.setText("TIN No.");

        lblPagIbigNum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPagIbigNum.setText("PagIbig No.");

        txtPhilNum.setEditable(false);
        txtPhilNum.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtSSSNum.setEditable(false);
        txtSSSNum.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtTINNum.setEditable(false);
        txtTINNum.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtPagIbigNum.setEditable(false);
        txtPagIbigNum.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblPagIbigNum, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTINNum, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTINNum, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(txtPagIbigNum)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSSSNum)
                            .addComponent(lblPhilNum))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPhilNum)
                            .addComponent(txtSSSNum))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPhilNum)
                    .addComponent(txtPhilNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSSSNum)
                    .addComponent(txtSSSNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTINNum)
                    .addComponent(txtTINNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPagIbigNum)
                    .addComponent(txtPagIbigNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelPersonalDetails2Layout = new javax.swing.GroupLayout(panelPersonalDetails2);
        panelPersonalDetails2.setLayout(panelPersonalDetails2Layout);
        panelPersonalDetails2Layout.setHorizontalGroup(
            panelPersonalDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalDetails2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPersonalDetails2Layout.setVerticalGroup(
            panelPersonalDetails2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalDetails2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPersonalDetails3.setBackground(new java.awt.Color(255, 255, 255));
        panelPersonalDetails3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employment Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        lblPosition.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPosition.setText("Position: ");

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStatus.setText("Status:");

        lblSupervisor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSupervisor.setText("Supervisor:");

        txtPosition.setEditable(false);
        txtPosition.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPositionActionPerformed(evt);
            }
        });

        txtStatus.setEditable(false);
        txtStatus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtSupervisor.setEditable(false);
        txtSupervisor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSupervisor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupervisorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSupervisor, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(lblPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                    .addComponent(txtSupervisor)
                    .addComponent(txtPosition))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPosition)
                    .addComponent(txtPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSupervisor)
                    .addComponent(txtSupervisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelPersonalDetails3Layout = new javax.swing.GroupLayout(panelPersonalDetails3);
        panelPersonalDetails3.setLayout(panelPersonalDetails3Layout);
        panelPersonalDetails3Layout.setHorizontalGroup(
            panelPersonalDetails3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalDetails3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelPersonalDetails3Layout.setVerticalGroup(
            panelPersonalDetails3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPersonalDetails3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(171, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabbedPersonalDetailsLayout = new javax.swing.GroupLayout(tabbedPersonalDetails);
        tabbedPersonalDetails.setLayout(tabbedPersonalDetailsLayout);
        tabbedPersonalDetailsLayout.setHorizontalGroup(
            tabbedPersonalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabbedPersonalDetailsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelPersonalDetails1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(panelPersonalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(panelPersonalDetails2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(panelPersonalDetails3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );
        tabbedPersonalDetailsLayout.setVerticalGroup(
            tabbedPersonalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedPersonalDetailsLayout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addGroup(tabbedPersonalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelPersonalDetails1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelPersonalDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelPersonalDetails2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelPersonalDetails3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(258, Short.MAX_VALUE))
        );

        mainTabbed.addTab("tab7", tabbedPersonalDetails);

        tabbedRequest.setBackground(new java.awt.Color(255, 255, 255));

        panelTypeRequest.setBackground(new java.awt.Color(255, 255, 255));
        panelTypeRequest.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "NEW REQUEST", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(255, 102, 0))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRequestType.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblRequestType.setText("Request Type :");
        jPanel2.add(lblRequestType, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 130, -1));

        comboTypeRequest.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        comboTypeRequest.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Request", "Leave Application", "Overtime Application" }));
        comboTypeRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTypeRequestActionPerformed(evt);
            }
        });
        jPanel2.add(comboTypeRequest, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 198, 31));

        tabbedInsideRequest.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        panelAllRequest.setBackground(new java.awt.Color(255, 255, 255));

        lblAllRequest.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblAllRequest.setText("ALL REQUEST");

        jTableAllRequest.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableAllRequest.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTableAllRequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DATE FILED", "TYPE OF REQUEST", "PERIOD FROM", "PERIOD TO", "NUMBER OF DAYS", "REASON", "STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAllRequest.setColumnSelectionAllowed(true);
        jTableAllRequest.setRowHeight(25);
        jTableAllRequest.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jTableAllRequest);

        jSeparator6.setBackground(new java.awt.Color(255, 204, 153));

        javax.swing.GroupLayout panelAllRequestLayout = new javax.swing.GroupLayout(panelAllRequest);
        panelAllRequest.setLayout(panelAllRequestLayout);
        panelAllRequestLayout.setHorizontalGroup(
            panelAllRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAllRequestLayout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(panelAllRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAllRequestLayout.createSequentialGroup()
                        .addComponent(lblAllRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(400, 400, 400))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAllRequestLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 933, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
        );
        panelAllRequestLayout.setVerticalGroup(
            panelAllRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllRequestLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblAllRequest)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );

        tabbedInsideRequest.addTab("", panelAllRequest);

        panelLeaveRequestDetails.setBackground(new java.awt.Color(255, 255, 255));

        txtReason.setColumns(20);
        txtReason.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtReason.setRows(5);
        txtReason.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reason", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N
        jScrollPane2.setViewportView(txtReason);

        dateTo.setBackground(new java.awt.Color(255, 255, 255));
        dateTo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "To", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N
        dateTo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateToPropertyChange(evt);
            }
        });

        dateFrom.setBackground(new java.awt.Color(255, 255, 255));
        dateFrom.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "From", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51)))); // NOI18N
        dateFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFromPropertyChange(evt);
            }
        });

        lblMyName1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMyName1.setText("Inclusive Dates");

        txtDaysNumber.setEditable(false);
        txtDaysNumber.setBackground(new java.awt.Color(255, 255, 255));
        txtDaysNumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDaysNumber.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Number of Working Days Applied For", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 102))); // NOI18N
        txtDaysNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDaysNumberActionPerformed(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(255, 204, 153));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        lblSLBalance.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSLBalance.setText("30.30");

        lblVLBalance.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblVLBalance.setText("20.20");

        lblSL.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSL.setText("Sick Leave Leave (SL) :");

        lblVL.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblVL.setText("Vacation Leave (VL) :");

        lblLeaveBalances.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblLeaveBalances.setText("Leave Balances as of this Date");

        jSeparator3.setBackground(new java.awt.Color(255, 204, 153));
        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(255, 204, 153));

        lblID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblID.setText("123");

        lblMyName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMyName.setText("John Paul Arquita");

        lblName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName.setText("Name :");

        lblEmpID1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblEmpID1.setText("Employee ID:");

        btnSubmit.setBackground(new java.awt.Color(27, 49, 74));
        btnSubmit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setText("SUBMIT");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        comboLeaveType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboLeaveType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Vacation Leave", "Sick Leave" }));
        comboLeaveType.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Type of Leave", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N

        javax.swing.GroupLayout panelLeaveRequestDetailsLayout = new javax.swing.GroupLayout(panelLeaveRequestDetails);
        panelLeaveRequestDetails.setLayout(panelLeaveRequestDetailsLayout);
        panelLeaveRequestDetailsLayout.setHorizontalGroup(
            panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLeaveRequestDetailsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLeaveRequestDetailsLayout.createSequentialGroup()
                        .addComponent(lblLeaveBalances, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(353, 353, 353))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLeaveRequestDetailsLayout.createSequentialGroup()
                        .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                                .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dateFrom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMyName1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(110, 110, 110)
                                .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane2)
                                .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                                    .addComponent(txtDaysNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(110, 110, 110)
                                    .addComponent(comboLeaveType, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(220, 220, 220))))
            .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2)
                    .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                        .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                                .addGap(195, 195, 195)
                                .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblEmpID1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMyName, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(104, 104, 104)
                                .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                                        .addComponent(lblSL, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblSLBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                                        .addComponent(lblVL, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblVLBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 1004, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelLeaveRequestDetailsLayout.setVerticalGroup(
            panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLeaveRequestDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLeaveBalances)
                .addGap(12, 12, 12)
                .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                        .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEmpID1)
                            .addComponent(lblID))
                        .addGap(13, 13, 13)
                        .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMyName)
                            .addComponent(lblName)))
                    .addGroup(panelLeaveRequestDetailsLayout.createSequentialGroup()
                        .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblVLBalance)
                            .addComponent(lblVL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSLBalance)
                            .addComponent(lblSL))))
                .addGap(15, 15, 15)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboLeaveType, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDaysNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMyName1)
                .addGap(9, 9, 9)
                .addGroup(panelLeaveRequestDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateTo, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                    .addComponent(dateFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(42, 42, 42)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        tabbedInsideRequest.addTab("", panelLeaveRequestDetails);

        panelOvertimeRequest.setBackground(new java.awt.Color(255, 255, 255));

        lblEmpID2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblEmpID2.setText("Employee ID:");

        lblID1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblID1.setText("10001");

        lblName1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName1.setText("Name :");

        lblMyName2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMyName2.setText("John Paul Arquita");

        dateFromOvertime.setBackground(new java.awt.Color(255, 255, 255));
        dateFromOvertime.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "From", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51)))); // NOI18N
        dateFromOvertime.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFromOvertimePropertyChange(evt);
            }
        });

        dateToOvertime.setBackground(new java.awt.Color(255, 255, 255));
        dateToOvertime.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "To", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N
        dateToOvertime.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateToOvertimePropertyChange(evt);
            }
        });

        lblMyName3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMyName3.setText("Inclusive Dates*");

        txtReasonOvertime.setColumns(20);
        txtReasonOvertime.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtReasonOvertime.setRows(5);
        txtReasonOvertime.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reason", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N
        jScrollPane3.setViewportView(txtReasonOvertime);

        btnSubmit1.setBackground(new java.awt.Color(27, 49, 74));
        btnSubmit1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSubmit1.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit1.setText("SUBMIT");
        btnSubmit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmit1ActionPerformed(evt);
            }
        });

        lblLeaveBalances1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblLeaveBalances1.setText("Overtime Request");

        jSeparator4.setBackground(new java.awt.Color(255, 204, 153));
        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator5.setBackground(new java.awt.Color(255, 204, 153));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel1.setText("*Please be informed that the overtime period is from 5:01pm to 9:00pm only.");

        txtDaysNumber1.setEditable(false);
        txtDaysNumber1.setBackground(new java.awt.Color(255, 255, 255));
        txtDaysNumber1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDaysNumber1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Number of Working Days Applied For", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 102))); // NOI18N
        txtDaysNumber1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDaysNumber1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOvertimeRequestLayout = new javax.swing.GroupLayout(panelOvertimeRequest);
        panelOvertimeRequest.setLayout(panelOvertimeRequestLayout);
        panelOvertimeRequestLayout.setHorizontalGroup(
            panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOvertimeRequestLayout.createSequentialGroup()
                .addContainerGap(196, Short.MAX_VALUE)
                .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOvertimeRequestLayout.createSequentialGroup()
                        .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateToOvertime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtDaysNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblMyName3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dateFromOvertime, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSubmit1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(185, 185, 185))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOvertimeRequestLayout.createSequentialGroup()
                        .addComponent(lblLeaveBalances1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(350, 350, 350))
                    .addGroup(panelOvertimeRequestLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEmpID2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblName1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblID1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMyName2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
            .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelOvertimeRequestLayout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addComponent(jSeparator4)
                    .addGap(2, 2, 2)))
            .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE))
        );
        panelOvertimeRequestLayout.setVerticalGroup(
            panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOvertimeRequestLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblLeaveBalances1)
                .addGap(24, 24, 24)
                .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmpID2)
                    .addComponent(lblID1))
                .addGap(13, 13, 13)
                .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMyName2)
                    .addComponent(lblName1))
                .addGap(35, 35, 35)
                .addComponent(lblMyName3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateToOvertime, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addComponent(dateFromOvertime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(txtDaysNumber1, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addGap(17, 17, 17)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(53, 53, 53)
                .addComponent(btnSubmit1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelOvertimeRequestLayout.createSequentialGroup()
                    .addGap(137, 137, 137)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(427, Short.MAX_VALUE)))
            .addGroup(panelOvertimeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelOvertimeRequestLayout.createSequentialGroup()
                    .addGap(461, 461, 461)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(103, Short.MAX_VALUE)))
        );

        tabbedInsideRequest.addTab("", panelOvertimeRequest);

        javax.swing.GroupLayout panelTypeRequestLayout = new javax.swing.GroupLayout(panelTypeRequest);
        panelTypeRequest.setLayout(panelTypeRequestLayout);
        panelTypeRequestLayout.setHorizontalGroup(
            panelTypeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTypeRequestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTypeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedInsideRequest, javax.swing.GroupLayout.DEFAULT_SIZE, 997, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelTypeRequestLayout.setVerticalGroup(
            panelTypeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTypeRequestLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tabbedInsideRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabbedRequestLayout = new javax.swing.GroupLayout(tabbedRequest);
        tabbedRequest.setLayout(tabbedRequestLayout);
        tabbedRequestLayout.setHorizontalGroup(
            tabbedRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedRequestLayout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(panelTypeRequest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(238, Short.MAX_VALUE))
        );
        tabbedRequestLayout.setVerticalGroup(
            tabbedRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedRequestLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(panelTypeRequest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        mainTabbed.addTab("Request Port", tabbedRequest);

        tabbedDTR.setBackground(new java.awt.Color(255, 255, 255));

        panelDTR.setBackground(new java.awt.Color(255, 255, 255));
        panelDTR.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "YOUR DAILY TIME RECORD", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(255, 102, 0))); // NOI18N

        jTableAllDTR.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTableAllDTR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DATE", "LOGIN", "LOGOUT", "SUBMITTED TO SUPERVISOR", "REMARKS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAllDTR.setColumnSelectionAllowed(true);
        jTableAllDTR.setRowHeight(25);
        jTableAllDTR.getTableHeader().setReorderingAllowed(false);
        tableDTR.setViewportView(jTableAllDTR);

        dateFrom2.setBackground(new java.awt.Color(255, 255, 255));
        dateFrom2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "From", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51)))); // NOI18N

        dateTo2.setBackground(new java.awt.Color(255, 255, 255));
        dateTo2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "To", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N

        lblPeriod.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPeriod.setText("Period :");

        jSeparator7.setBackground(new java.awt.Color(255, 204, 153));

        btnSubmitToSepervisor.setBackground(new java.awt.Color(27, 49, 74));
        btnSubmitToSepervisor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSubmitToSepervisor.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmitToSepervisor.setText("SUBMIT");
        btnSubmitToSepervisor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitToSepervisorActionPerformed(evt);
            }
        });

        jSeparator8.setBackground(new java.awt.Color(255, 204, 153));
        jSeparator8.setForeground(new java.awt.Color(255, 255, 255));

        lblName2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName2.setText("Name :");

        lblMyName4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMyName4.setText("John Paul Arquita");

        lblEmpID3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblEmpID3.setText("Employee ID:");

        lblID2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblID2.setText("10001");

        btnClear.setBackground(new java.awt.Color(27, 49, 74));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Search");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDTRLayout = new javax.swing.GroupLayout(panelDTR);
        panelDTR.setLayout(panelDTRLayout);
        panelDTRLayout.setHorizontalGroup(
            panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator7)
            .addGroup(panelDTRLayout.createSequentialGroup()
                .addGroup(panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDTRLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(btnSubmitToSepervisor, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDTRLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEmpID3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblName2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblID2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMyName4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelDTRLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDTRLayout.createSequentialGroup()
                        .addGap(0, 39, Short.MAX_VALUE)
                        .addGroup(panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelDTRLayout.createSequentialGroup()
                                .addComponent(lblPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(dateFrom2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(dateTo2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDTRLayout.createSequentialGroup()
                                .addComponent(tableDTR, javax.swing.GroupLayout.PREFERRED_SIZE, 906, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31))))
                    .addComponent(jSeparator8)))
        );
        panelDTRLayout.setVerticalGroup(
            panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDTRLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmpID3)
                    .addComponent(lblID2))
                .addGap(13, 13, 13)
                .addGroup(panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMyName4)
                    .addComponent(lblName2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDTRLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateFrom2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDTRLayout.createSequentialGroup()
                                .addComponent(lblPeriod)
                                .addGap(9, 9, 9))
                            .addComponent(dateTo2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelDTRLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(tableDTR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSubmitToSepervisor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout tabbedDTRLayout = new javax.swing.GroupLayout(tabbedDTR);
        tabbedDTR.setLayout(tabbedDTRLayout);
        tabbedDTRLayout.setHorizontalGroup(
            tabbedDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedDTRLayout.createSequentialGroup()
                .addGap(245, 245, 245)
                .addComponent(panelDTR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(273, Short.MAX_VALUE))
        );
        tabbedDTRLayout.setVerticalGroup(
            tabbedDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabbedDTRLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(panelDTR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        mainTabbed.addTab("tab3", tabbedDTR);

        tabbedLeaveLedger.setBackground(new java.awt.Color(255, 255, 255));

        panelLeaveLedger.setBackground(new java.awt.Color(255, 255, 255));
        panelLeaveLedger.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "YOUR LEAVE LEDGER HISTORY", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(255, 102, 0))); // NOI18N

        jTableAllRequest3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTableAllRequest3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DATE FILED", "TYPE OF LEAVE", "FROM", "TO", "NUMBER OF DAYS", "REASON", "STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAllRequest3.setColumnSelectionAllowed(true);
        jTableAllRequest3.setRowHeight(25);
        jTableAllRequest3.getTableHeader().setReorderingAllowed(false);
        tableDTR1.setViewportView(jTableAllRequest3);

        jSeparator9.setBackground(new java.awt.Color(255, 204, 153));

        jSeparator11.setBackground(new java.awt.Color(255, 204, 153));
        jSeparator11.setForeground(new java.awt.Color(255, 255, 255));

        lblSL1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSL1.setText("Sick Leave Leave (SL) :");

        lblSLBalance1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSLBalance1.setText("30.30");

        lblVL1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblVL1.setText("Vacation Leave (VL) :");

        lblVLBalance1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblVLBalance1.setText("20.20");

        lblMyName5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMyName5.setText("John Paul Arquita");

        lblName3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName3.setText("Name :");

        lblEmpID4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblEmpID4.setText("Employee ID:");

        lblID3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblID3.setText("10001");

        lblLeaveBalances2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblLeaveBalances2.setText("LEAVE LEDGER");

        javax.swing.GroupLayout panelLeaveLedgerLayout = new javax.swing.GroupLayout(panelLeaveLedger);
        panelLeaveLedger.setLayout(panelLeaveLedgerLayout);
        panelLeaveLedgerLayout.setHorizontalGroup(
            panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator9)
            .addGroup(panelLeaveLedgerLayout.createSequentialGroup()
                .addComponent(jSeparator11)
                .addGap(6, 6, 6))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLeaveLedgerLayout.createSequentialGroup()
                .addGap(0, 47, Short.MAX_VALUE)
                .addComponent(tableDTR1, javax.swing.GroupLayout.PREFERRED_SIZE, 969, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLeaveLedgerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEmpID4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblName3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblID3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMyName5, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(180, 180, 180)
                .addGroup(panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLeaveLedgerLayout.createSequentialGroup()
                        .addComponent(lblSL1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSLBalance1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLeaveLedgerLayout.createSequentialGroup()
                        .addComponent(lblVL1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblVLBalance1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(215, 215, 215))
            .addGroup(panelLeaveLedgerLayout.createSequentialGroup()
                .addGap(450, 450, 450)
                .addComponent(lblLeaveBalances2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLeaveLedgerLayout.setVerticalGroup(
            panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLeaveLedgerLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblLeaveBalances2)
                .addGap(22, 22, 22)
                .addGroup(panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelLeaveLedgerLayout.createSequentialGroup()
                        .addGroup(panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEmpID4)
                            .addComponent(lblID3))
                        .addGap(13, 13, 13)
                        .addGroup(panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMyName5)
                            .addComponent(lblName3)))
                    .addGroup(panelLeaveLedgerLayout.createSequentialGroup()
                        .addGroup(panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblVLBalance1)
                            .addComponent(lblVL1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSLBalance1)
                            .addComponent(lblSL1))))
                .addGap(21, 21, 21)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tableDTR1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );

        javax.swing.GroupLayout tabbedLeaveLedgerLayout = new javax.swing.GroupLayout(tabbedLeaveLedger);
        tabbedLeaveLedger.setLayout(tabbedLeaveLedgerLayout);
        tabbedLeaveLedgerLayout.setHorizontalGroup(
            tabbedLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedLeaveLedgerLayout.createSequentialGroup()
                .addGap(222, 222, 222)
                .addComponent(panelLeaveLedger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(231, Short.MAX_VALUE))
        );
        tabbedLeaveLedgerLayout.setVerticalGroup(
            tabbedLeaveLedgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabbedLeaveLedgerLayout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(panelLeaveLedger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        mainTabbed.addTab("tab4", tabbedLeaveLedger);

        tabbedPayslip.setBackground(new java.awt.Color(255, 255, 255));

        panelPayslip.setBackground(new java.awt.Color(255, 255, 255));
        panelPayslip.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "YOUR PERSONAL PAYSLIP", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(255, 102, 0))); // NOI18N

        jSeparator12.setBackground(new java.awt.Color(255, 204, 153));
        jSeparator12.setForeground(new java.awt.Color(255, 255, 255));

        lblMyName6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMyName6.setText("N/A");

        lblName4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName4.setText("Name :");

        lblEmpID5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblEmpID5.setText("Employee ID:");

        lblID4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblID4.setText("N/A");

        dateFrom3.setBackground(new java.awt.Color(255, 255, 255));
        dateFrom3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "From", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51)))); // NOI18N

        dateTo3.setBackground(new java.awt.Color(255, 255, 255));
        dateTo3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "To", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 153, 51))); // NOI18N

        btnReport.setBackground(new java.awt.Color(27, 49, 74));
        btnReport.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReport.setForeground(new java.awt.Color(255, 255, 255));
        btnReport.setText("Generate");
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });

        lblName18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName18.setText("Payroll Period : ");

        lblName7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName7.setText("Position :");

        lblName8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName8.setText("Gross Income : ");

        lblName9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName9.setText("Add :");

        lblName10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName10.setText("Overtime");

        lblName11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName11.setText("Undertime");

        lblName12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName12.setText("SSS");

        lblName13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName13.setText("PhilHealth");

        lblName14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName14.setText("PagIbig");

        lblName15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName15.setText("Tax");

        lblName16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName16.setText("Benefits/Allowances");

        lblName19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName19.setText("Less :");

        lblName21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblName21.setText("Net Pay");

        lblGross.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGross.setText("0.00");

        lblBenefits.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblBenefits.setText("0.00");

        lblOvertime.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblOvertime.setText("0.00");

        lblUndertime.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblUndertime.setText("0.00");

        lblSSS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSSS.setText("0.00");

        lblPhilHealth.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblPhilHealth.setText("0.00");

        lblPagIbig.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblPagIbig.setText("0.00");

        lblTax.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTax.setText("0.00");

        lblNetPay.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNetPay.setText("0.00");

        lblPayrollPeriod.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblPayrollPeriod.setText("N/A");

        lblPositon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblPositon.setText("N/A");

        javax.swing.GroupLayout panelPayslipLayout = new javax.swing.GroupLayout(panelPayslip);
        panelPayslip.setLayout(panelPayslipLayout);
        panelPayslipLayout.setHorizontalGroup(
            panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator12)
            .addGroup(panelPayslipLayout.createSequentialGroup()
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPayslipLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(jLabel7))
                    .addGroup(panelPayslipLayout.createSequentialGroup()
                        .addGap(290, 290, 290)
                        .addComponent(dateFrom3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dateTo3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReport))
                    .addGroup(panelPayslipLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelPayslipLayout.createSequentialGroup()
                                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblName8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblName18))
                                    .addComponent(lblName7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPositon, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblPayrollPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelPayslipLayout.createSequentialGroup()
                                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblEmpID5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblName4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelPayslipLayout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(lblMyName6, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelPayslipLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(lblID4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelPayslipLayout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelPayslipLayout.createSequentialGroup()
                                        .addComponent(lblName9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblName10, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblName16, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panelPayslipLayout.createSequentialGroup()
                                        .addComponent(lblName19, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(22, 22, 22)
                                        .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblName11, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblName12, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblName13, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblName14, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblName15, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblName21, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelPayslipLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(panelPayslipLayout.createSequentialGroup()
                                                        .addGap(90, 90, 90)
                                                        .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(lblUndertime, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lblSSS, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lblPhilHealth, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lblPagIbig, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lblTax, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lblOvertime, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lblBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lblGross, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(panelPayslipLayout.createSequentialGroup()
                                                        .addGap(91, 91, 91)
                                                        .addComponent(lblNetPay, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(24, 24, 24))
                                            .addGroup(panelPayslipLayout.createSequentialGroup()
                                                .addGap(62, 62, 62)
                                                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 33, Short.MAX_VALUE)))))))))
                .addContainerGap(168, Short.MAX_VALUE))
        );
        panelPayslipLayout.setVerticalGroup(
            panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPayslipLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(dateTo3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateFrom3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnReport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(28, 28, 28)
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmpID5)
                    .addComponent(lblID4))
                .addGap(13, 13, 13)
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMyName6)
                    .addComponent(lblName4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName18)
                    .addComponent(lblPayrollPeriod))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName7)
                    .addComponent(lblPositon))
                .addGap(18, 18, 18)
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName8)
                    .addComponent(lblGross))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName9)
                    .addComponent(lblName16)
                    .addComponent(lblBenefits))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName10)
                    .addComponent(lblOvertime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPayslipLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(panelPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblName11)
                            .addComponent(lblName19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblName12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblName13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblName14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblName15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblName21))
                    .addGroup(panelPayslipLayout.createSequentialGroup()
                        .addComponent(lblUndertime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSSS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPhilHealth)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPagIbig)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTax)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNetPay)))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout tabbedPayslipLayout = new javax.swing.GroupLayout(tabbedPayslip);
        tabbedPayslip.setLayout(tabbedPayslipLayout);
        tabbedPayslipLayout.setHorizontalGroup(
            tabbedPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedPayslipLayout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addComponent(panelPayslip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(303, Short.MAX_VALUE))
        );
        tabbedPayslipLayout.setVerticalGroup(
            tabbedPayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedPayslipLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(panelPayslip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );

        mainTabbed.addTab("tab5", tabbedPayslip);

        tabbedEmployeeRequest.setBackground(new java.awt.Color(255, 255, 255));

        panelTypeRequest1.setBackground(new java.awt.Color(255, 255, 255));
        panelTypeRequest1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "EMPLOYEE REQUEST", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(255, 102, 0))); // NOI18N

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRequestType1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblRequestType1.setText("Request Type :");
        jPanel10.add(lblRequestType1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 108, -1));

        comboTypeRequest1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        comboTypeRequest1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Request", "Leave Request", "Overtime Request" }));
        comboTypeRequest1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTypeRequest1ActionPerformed(evt);
            }
        });
        jPanel10.add(comboTypeRequest1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 198, 40));

        tabbedInsideRequest1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        panelAllRequest1.setBackground(new java.awt.Color(255, 255, 255));

        lblAllRequest1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblAllRequest1.setText("ALL REQUEST");

        jTableAllRequest1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableAllRequest1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTableAllRequest1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "DATE FILED", "TYPE OF REQUEST", "PERIOD FROM", "PERIOD TO", "NUMBER OF DAYS", "REASON", "STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAllRequest1.setColumnSelectionAllowed(true);
        jTableAllRequest1.setRowHeight(25);
        jTableAllRequest1.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(jTableAllRequest1);
        jTableAllRequest1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (jTableAllRequest1.getColumnModel().getColumnCount() > 0) {
            jTableAllRequest1.getColumnModel().getColumn(0).setResizable(false);
            jTableAllRequest1.getColumnModel().getColumn(1).setResizable(false);
            jTableAllRequest1.getColumnModel().getColumn(2).setResizable(false);
            jTableAllRequest1.getColumnModel().getColumn(3).setResizable(false);
            jTableAllRequest1.getColumnModel().getColumn(4).setResizable(false);
            jTableAllRequest1.getColumnModel().getColumn(5).setResizable(false);
            jTableAllRequest1.getColumnModel().getColumn(6).setResizable(false);
            jTableAllRequest1.getColumnModel().getColumn(7).setResizable(false);
            jTableAllRequest1.getColumnModel().getColumn(8).setResizable(false);
        }

        jSeparator16.setBackground(new java.awt.Color(255, 204, 153));

        btnUpdate1.setBackground(new java.awt.Color(27, 49, 74));
        btnUpdate1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate1.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate1.setText("APPROVED");
        btnUpdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate1ActionPerformed(evt);
            }
        });

        btnCancel3.setBackground(new java.awt.Color(27, 49, 74));
        btnCancel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancel3.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel3.setText("DISAPPROVED");
        btnCancel3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAllRequest1Layout = new javax.swing.GroupLayout(panelAllRequest1);
        panelAllRequest1.setLayout(panelAllRequest1Layout);
        panelAllRequest1Layout.setHorizontalGroup(
            panelAllRequest1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAllRequest1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator16)
                .addContainerGap())
            .addGroup(panelAllRequest1Layout.createSequentialGroup()
                .addGroup(panelAllRequest1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAllRequest1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnUpdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(btnCancel3))
                    .addGroup(panelAllRequest1Layout.createSequentialGroup()
                        .addGap(486, 486, 486)
                        .addComponent(lblAllRequest1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAllRequest1Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1097, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        panelAllRequest1Layout.setVerticalGroup(
            panelAllRequest1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllRequest1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblAllRequest1)
                .addGap(49, 49, 49)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAllRequest1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        tabbedInsideRequest1.addTab("", panelAllRequest1);

        javax.swing.GroupLayout panelTypeRequest1Layout = new javax.swing.GroupLayout(panelTypeRequest1);
        panelTypeRequest1.setLayout(panelTypeRequest1Layout);
        panelTypeRequest1Layout.setHorizontalGroup(
            panelTypeRequest1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTypeRequest1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(tabbedInsideRequest1)
        );
        panelTypeRequest1Layout.setVerticalGroup(
            panelTypeRequest1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTypeRequest1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tabbedInsideRequest1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout tabbedEmployeeRequestLayout = new javax.swing.GroupLayout(tabbedEmployeeRequest);
        tabbedEmployeeRequest.setLayout(tabbedEmployeeRequestLayout);
        tabbedEmployeeRequestLayout.setHorizontalGroup(
            tabbedEmployeeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedEmployeeRequestLayout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(panelTypeRequest1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(175, Short.MAX_VALUE))
        );
        tabbedEmployeeRequestLayout.setVerticalGroup(
            tabbedEmployeeRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedEmployeeRequestLayout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(panelTypeRequest1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        mainTabbed.addTab("tab7", tabbedEmployeeRequest);

        tabbedEmployeeDTR.setBackground(new java.awt.Color(255, 255, 255));

        panelTypeRequest2.setBackground(new java.awt.Color(255, 255, 255));
        panelTypeRequest2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "EMPLOYEE DTR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(255, 102, 0))); // NOI18N

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRequestType2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblRequestType2.setText("Employee Name:");
        jPanel11.add(lblRequestType2, new org.netbeans.lib.awtextra.AbsoluteConstraints(348, 20, 120, -1));

        comboEmployeeName.setEditable(true);
        comboEmployeeName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        comboEmployeeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEmployeeNameActionPerformed(evt);
            }
        });
        jPanel11.add(comboEmployeeName, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 198, 40));

        tabbedInsideRequest2.setBackground(new java.awt.Color(255, 255, 255));
        tabbedInsideRequest2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        panelAllRequest2.setBackground(new java.awt.Color(255, 255, 255));

        lblAllRequest2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblAllRequest2.setText("DAILY TIME RECORDS");

        jTableDTR.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableDTR.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTableDTR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "DATE", "LOGIN", "LOGOUT", "REMARKS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDTR.setRowHeight(25);
        jTableDTR.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(jTableDTR);
        jTableDTR.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (jTableDTR.getColumnModel().getColumnCount() > 0) {
            jTableDTR.getColumnModel().getColumn(0).setResizable(false);
            jTableDTR.getColumnModel().getColumn(1).setResizable(false);
            jTableDTR.getColumnModel().getColumn(2).setResizable(false);
            jTableDTR.getColumnModel().getColumn(3).setResizable(false);
            jTableDTR.getColumnModel().getColumn(4).setResizable(false);
            jTableDTR.getColumnModel().getColumn(5).setResizable(false);
        }

        jSeparator17.setBackground(new java.awt.Color(255, 204, 153));

        btnForwardToPayroll.setBackground(new java.awt.Color(27, 49, 74));
        btnForwardToPayroll.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnForwardToPayroll.setForeground(new java.awt.Color(255, 255, 255));
        btnForwardToPayroll.setText("FORWARD TO PAYROLL STAFF");
        btnForwardToPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForwardToPayrollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAllRequest2Layout = new javax.swing.GroupLayout(panelAllRequest2);
        panelAllRequest2.setLayout(panelAllRequest2Layout);
        panelAllRequest2Layout.setHorizontalGroup(
            panelAllRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAllRequest2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator17)
                .addContainerGap())
            .addGroup(panelAllRequest2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnForwardToPayroll)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAllRequest2Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(panelAllRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1097, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelAllRequest2Layout.createSequentialGroup()
                        .addGap(447, 447, 447)
                        .addComponent(lblAllRequest2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        panelAllRequest2Layout.setVerticalGroup(
            panelAllRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllRequest2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblAllRequest2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnForwardToPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        tabbedInsideRequest2.addTab("", panelAllRequest2);

        javax.swing.GroupLayout panelTypeRequest2Layout = new javax.swing.GroupLayout(panelTypeRequest2);
        panelTypeRequest2.setLayout(panelTypeRequest2Layout);
        panelTypeRequest2Layout.setHorizontalGroup(
            panelTypeRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTypeRequest2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(tabbedInsideRequest2)
        );
        panelTypeRequest2Layout.setVerticalGroup(
            panelTypeRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTypeRequest2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedInsideRequest2, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout tabbedEmployeeDTRLayout = new javax.swing.GroupLayout(tabbedEmployeeDTR);
        tabbedEmployeeDTR.setLayout(tabbedEmployeeDTRLayout);
        tabbedEmployeeDTRLayout.setHorizontalGroup(
            tabbedEmployeeDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedEmployeeDTRLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(panelTypeRequest2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(325, Short.MAX_VALUE))
        );
        tabbedEmployeeDTRLayout.setVerticalGroup(
            tabbedEmployeeDTRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabbedEmployeeDTRLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(panelTypeRequest2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        mainTabbed.addTab("tab8", tabbedEmployeeDTR);

        jPanel1.add(mainTabbed, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 1510, 800));

        sideBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        sideBarPanel.setForeground(new java.awt.Color(102, 102, 102));

        jSeparator13.setBackground(new java.awt.Color(255, 204, 153));
        jSeparator13.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator14.setBackground(new java.awt.Color(255, 204, 153));
        jSeparator14.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator15.setBackground(new java.awt.Color(255, 204, 153));
        jSeparator15.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/employee.png"))); // NOI18N

        lblNameSidebar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNameSidebar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameSidebar.setText("Aquino Bianca Sofia");
        lblNameSidebar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblName5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName5.setText("No.");

        lblIDSidebar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblIDSidebar.setText("10003");

        btnPersonalDetails.setBackground(new java.awt.Color(27, 49, 74));
        btnPersonalDetails.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPersonalDetails.setForeground(new java.awt.Color(255, 255, 255));
        btnPersonalDetails.setText("PERSONAL DETAILS");
        btnPersonalDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPersonalDetailsActionPerformed(evt);
            }
        });

        btnRequestPort.setBackground(new java.awt.Color(27, 49, 74));
        btnRequestPort.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRequestPort.setForeground(new java.awt.Color(255, 255, 255));
        btnRequestPort.setText("EMPLOYEE REQUEST");
        btnRequestPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRequestPortActionPerformed(evt);
            }
        });

        btnDTR.setBackground(new java.awt.Color(27, 49, 74));
        btnDTR.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDTR.setForeground(new java.awt.Color(255, 255, 255));
        btnDTR.setText("DTR");
        btnDTR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDTRActionPerformed(evt);
            }
        });

        btnLeaveLedger.setBackground(new java.awt.Color(27, 49, 74));
        btnLeaveLedger.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLeaveLedger.setForeground(new java.awt.Color(255, 255, 255));
        btnLeaveLedger.setText("LEAVE LEDGER");
        btnLeaveLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveLedgerActionPerformed(evt);
            }
        });

        btnpayslip.setBackground(new java.awt.Color(27, 49, 74));
        btnpayslip.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnpayslip.setForeground(new java.awt.Color(255, 255, 255));
        btnpayslip.setText("PAYSLIP");
        btnpayslip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpayslipActionPerformed(evt);
            }
        });

        btnemployeeRequest.setBackground(new java.awt.Color(27, 49, 74));
        btnemployeeRequest.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnemployeeRequest.setForeground(new java.awt.Color(255, 255, 255));
        btnemployeeRequest.setText("EMPLOYEE REQUEST");
        btnemployeeRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnemployeeRequestActionPerformed(evt);
            }
        });

        btnemployeeDTR.setBackground(new java.awt.Color(27, 49, 74));
        btnemployeeDTR.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnemployeeDTR.setForeground(new java.awt.Color(255, 255, 255));
        btnemployeeDTR.setText("EMPLOYEE DTR");
        btnemployeeDTR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnemployeeDTRActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel5.setText("8:00 AM");

        btnLogin.setBackground(new java.awt.Color(27, 49, 74));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Time In");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnLogout.setBackground(new java.awt.Color(27, 49, 74));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Time Out");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout sideBarPanelLayout = new javax.swing.GroupLayout(sideBarPanel);
        sideBarPanel.setLayout(sideBarPanelLayout);
        sideBarPanelLayout.setHorizontalGroup(
            sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideBarPanelLayout.createSequentialGroup()
                .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sideBarPanelLayout.createSequentialGroup()
                        .addGap(204, 204, 204)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.DEFAULT_SIZE, 1530, Short.MAX_VALUE))
                    .addGroup(sideBarPanelLayout.createSequentialGroup()
                        .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(sideBarPanelLayout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(lblName5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblIDSidebar))
                            .addGroup(sideBarPanelLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel5))
                            .addGroup(sideBarPanelLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnemployeeDTR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnemployeeRequest, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                        .addComponent(btnpayslip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnLeaveLedger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnDTR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnRequestPort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPersonalDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(sideBarPanelLayout.createSequentialGroup()
                                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(sideBarPanelLayout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel8))
                            .addGroup(sideBarPanelLayout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(jLabel6)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(sideBarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNameSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sideBarPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator13, javax.swing.GroupLayout.DEFAULT_SIZE, 1728, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sideBarPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator15, javax.swing.GroupLayout.DEFAULT_SIZE, 1728, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        sideBarPanelLayout.setVerticalGroup(
            sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sideBarPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNameSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName5)
                    .addComponent(lblIDSidebar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPersonalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRequestPort, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDTR, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLeaveLedger, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnpayslip, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnemployeeRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnemployeeDTR, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sideBarPanelLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(sideBarPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(140, Short.MAX_VALUE))
            .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sideBarPanelLayout.createSequentialGroup()
                    .addGap(158, 158, 158)
                    .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(614, Short.MAX_VALUE)))
            .addGroup(sideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sideBarPanelLayout.createSequentialGroup()
                    .addContainerGap(435, Short.MAX_VALUE)
                    .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(337, 337, 337)))
        );

        jPanel1.add(sideBarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1740, -1));

        jScrollPane8.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1671, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
         try {
            if (attendanceService == null) {
                try {
                    this.attendanceService = new AttendanceService();
                } catch (Exception serviceError) {
                    JOptionPane.showMessageDialog(this, "Cannot initialize attendance service. Please contact IT support.", 
                        "Service Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            int employeeId = Integer.parseInt(lblIDSidebar.getText());

            AttendanceResult result = attendanceService.processTimeOut(employeeId);

            if (result.isSuccess()) {
                JOptionPane.showMessageDialog(this, result.getMessage(), 
                    "Time Out Success", JOptionPane.INFORMATION_MESSAGE);
                updateAttendanceButtonStates();
            } else {
                JOptionPane.showMessageDialog(this, result.getMessage(), 
                    "Time Out Failed", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "System Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
         try {
            if (attendanceService == null) {
                try {
                    this.attendanceService = new AttendanceService();
                } catch (Exception serviceError) {
                    JOptionPane.showMessageDialog(this, "Cannot initialize attendance service. Please contact IT support.", 
                        "Service Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
           int employeeId = Integer.parseInt(lblIDSidebar.getText());

           AttendanceResult result = attendanceService.processTimeIn(employeeId);

           if (result.isSuccess()) {
               JOptionPane.showMessageDialog(this, result.getMessage(), 
                   "Time In Success", JOptionPane.INFORMATION_MESSAGE);
               updateAttendanceButtonStates();
           } else {
               JOptionPane.showMessageDialog(this, result.getMessage(), 
                   "Time In Failed", JOptionPane.WARNING_MESSAGE);
           }

       } catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Invalid Employee ID!", 
               "Error", JOptionPane.ERROR_MESSAGE);
       } catch (Exception e) {
           JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
               "System Error", JOptionPane.ERROR_MESSAGE);
           e.printStackTrace();
       }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void updateAttendanceButtonStates() {
        try {
            if (attendanceService == null) {
                btnLogin.setEnabled(true);
                btnLogout.setEnabled(false);
                return;
            }
            
            int employeeId = Integer.parseInt(lblIDSidebar.getText());
            AttendanceStatus status = attendanceService.getAttendanceStatus(employeeId);
            
            btnLogin.setEnabled(status.canTimeIn());
            btnLogout.setEnabled(status.canTimeOut());
            
        } catch (Exception e) {
            btnLogin.setEnabled(true);
            btnLogout.setEnabled(false);
            System.err.println("Error updating attendance button states: " + e.getMessage());
        }
    }
    
    private void btnemployeeRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnemployeeRequestActionPerformed
        // TODO add your handling code here:
        mainTabbed.setSelectedIndex(5);
        supervisor.setTableData(supervisor.getAllRequestData(comboTypeRequest1.getSelectedItem().toString()));
        supervisor.setTableSize(9);
        supervisor.displayDataTable(jTableAllRequest1);
        //supervisor.TableData(jTableAllRequest1, comboTypeRequest1.getSelectedItem().toString());
    }//GEN-LAST:event_btnemployeeRequestActionPerformed

    private void btnemployeeDTRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnemployeeDTRActionPerformed
        // TODO add your handling code here:
        try {
            System.out.println("=== Loading Employee DTR Tab ===");

            mainTabbed.setSelectedIndex(6); 

            comboEmployeeName.removeAllItems();
            comboEmployeeName.addItem("-- Select Employee --"); 

            supervisor.loadEmployeeDropdown(comboEmployeeName);

            DefaultTableModel model = (DefaultTableModel) jTableDTR.getModel();
            model.setRowCount(0);

            System.out.println("Employee DTR tab loaded with " + (comboEmployeeName.getItemCount() - 1) + " employees");

        } catch (Exception e) {
            System.err.println("ERROR loading Employee DTR: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading Employee DTR: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnemployeeDTRActionPerformed

    private void comboTypeRequest1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTypeRequest1ActionPerformed
        // TODO add your handling code here:
        supervisor.setTableData(supervisor.getAllRequestData(comboTypeRequest1.getSelectedItem().toString()));
        supervisor.setTableSize(9);
        supervisor.displayDataTable(jTableAllRequest1);
    }//GEN-LAST:event_comboTypeRequest1ActionPerformed

    private void btnUpdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate1ActionPerformed
        // TODO add your handling code here:
           try {
            System.out.println("=== APPROVED Button Clicked ===");

            int currentRow = jTableAllRequest1.getSelectedRow();

            if (currentRow < 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Please select a request row first by clicking on it.",
                    "No Request Selected",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("DEBUG: Table has " + jTableAllRequest1.getColumnCount() + " columns");
            System.out.println("DEBUG: Table has " + jTableAllRequest1.getRowCount() + " rows");
            System.out.println("DEBUG: Selected row: " + currentRow);

            String displayId = jTableAllRequest1.getValueAt(currentRow, 0).toString();
            String employeeName = jTableAllRequest1.getValueAt(currentRow, 1).toString();
            String requestType = jTableAllRequest1.getValueAt(currentRow, 3).toString();

            System.out.println("DEBUG: Display ID = " + displayId);
            System.out.println("DEBUG: Employee = " + employeeName);
            System.out.println("DEBUG: Request Type = " + requestType);

            int realRequestId = supervisor.getRealIdFromDisplayRow(jTableAllRequest1, currentRow);

            if (realRequestId == -1) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Error: Unable to get request ID for processing.\n\n" +
                    "Debug Info:\n" +
                    "- Selected Row: " + currentRow + "\n" +
                    "- Table Columns: " + jTableAllRequest1.getColumnCount() + "\n" +
                    "- Display ID: " + displayId,
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            String confirmMessage = "Approve this request?\n\n" +
                                   "Request #: " + displayId + "\n" +
                                   "Employee: " + employeeName + "\n" +
                                   "Type: " + requestType + "\n" +
                                   "Database ID: " + realRequestId;

            int choice = javax.swing.JOptionPane.showConfirmDialog(this,
                confirmMessage,
                "Confirm Approval",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);

            if (choice != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }

            boolean success = false;

            if (requestType.toLowerCase().contains("leave") || requestType.toLowerCase().contains("vacation") || requestType.toLowerCase().contains("sick")) {
                success = supervisor.approveLeaveRequest(realRequestId, true);
            } else if (requestType.toLowerCase().contains("overtime")) {
                success = supervisor.approveOvertimeRequest(realRequestId, true);
            } else {
                success = supervisor.approveRequest(realRequestId, true);
            }

            if (success) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Successfully approved request #" + displayId + "\n" +
                    "Employee: " + employeeName,
                    "Approval Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                // Refresh table
                refreshEmployeeRequestTable();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Failed to approve request #" + displayId,
                    "Approval Failed",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("ERROR in approval: " + e.getMessage());
            e.printStackTrace();

            javax.swing.JOptionPane.showMessageDialog(this,
                "Error during approval: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdate1ActionPerformed

    private void btnCancel3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel3ActionPerformed
        // TODO add your handling code here:
        try {
            System.out.println("=== DISAPPROVED Button Clicked ===");

            int[] selectedRows = jTableAllRequest1.getSelectedRows();
            int currentRow = jTableAllRequest1.getSelectedRow();

            System.out.println("DEBUG: selectedRows.length = " + selectedRows.length);
            System.out.println("DEBUG: currentRow = " + currentRow);

            if (selectedRows.length == 0 && currentRow >= 0) {
                selectedRows = new int[]{currentRow};
                System.out.println("DEBUG: Using single row selection: " + currentRow);
            }

            if (selectedRows.length == 0 || currentRow < 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Please click on a request row to select it, then click DISAPPROVED.\n\n" +
                    "Make sure to click directly on the row data (not empty space).",
                    "No Request Selected",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            int row = selectedRows[0];
            String displayId = jTableAllRequest1.getValueAt(row, 0).toString();
            String employeeName = jTableAllRequest1.getValueAt(row, 1).toString();
            String requestType = jTableAllRequest1.getValueAt(row, 3).toString();

            int realRequestId = supervisor.getRealIdFromDisplayRow(jTableAllRequest1, row);

            if (realRequestId == -1) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Error: Unable to get request ID for processing.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            String confirmMessage = "Disapprove this request?\n\n" +
                                   "Request #: " + displayId + "\n" +
                                   "Employee: " + employeeName + "\n" +
                                   "Type: " + requestType + "\n" +
                                   "Database ID: " + realRequestId;

            int choice = javax.swing.JOptionPane.showConfirmDialog(this,
                confirmMessage,
                "Confirm Disapproval",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

            if (choice != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }

            int disapprovedCount = 0;
            int failedCount = 0;

            for (int rowIndex : selectedRows) {
                try {
                    String dispId = jTableAllRequest1.getValueAt(rowIndex, 0).toString();
                    String empName = jTableAllRequest1.getValueAt(rowIndex, 1).toString();
                    String reqType = jTableAllRequest1.getValueAt(rowIndex, 3).toString();

                    int realId = supervisor.getRealIdFromDisplayRow(jTableAllRequest1, rowIndex);

                    if (realId == -1) {
                        System.err.println("Could not get real ID for display row " + dispId);
                        failedCount++;
                        continue;
                    }

                    System.out.println("Processing disapproval for: Display ID=" + dispId + ", Real ID=" + realId + 
                                     ", Employee=" + empName + ", Type=" + reqType);

                    boolean success = false;

                    if (reqType.toLowerCase().contains("leave") || reqType.toLowerCase().contains("vacation") || reqType.toLowerCase().contains("sick")) {
                        success = supervisor.approveLeaveRequest(realId, false);
                    } else if (reqType.toLowerCase().contains("overtime")) {
                        success = supervisor.approveOvertimeRequest(realId, false);
                    } else {
                        success = supervisor.approveRequest(realId, false);
                    }

                    if (success) {
                        disapprovedCount++;
                        System.out.println("Disapproved: Display ID " + dispId + " (Real ID " + realId + ")");
                    } else {
                        failedCount++;
                        System.out.println("Failed to disapprove: Display ID " + dispId + " (Real ID " + realId + ")");
                    }

                } catch (Exception e) {
                    failedCount++;
                    System.err.println("Error processing row " + rowIndex + ": " + e.getMessage());
                }
            }

            String resultMessage = "";
            if (disapprovedCount > 0) {
                resultMessage = "Successfully disapproved " + disapprovedCount + " request(s)";
            }
            if (failedCount > 0) {
                if (!resultMessage.isEmpty()) resultMessage += "\n";
                resultMessage += "Failed to disapprove " + failedCount + " request(s)";
            }

            javax.swing.JOptionPane.showMessageDialog(this,
                resultMessage,
                "Disapproval Results",
                disapprovedCount > 0 ? javax.swing.JOptionPane.INFORMATION_MESSAGE : javax.swing.JOptionPane.ERROR_MESSAGE);

            refreshEmployeeRequestTable();

        } catch (Exception e) {
            System.err.println("ERROR in disapproval: " + e.getMessage());
            e.printStackTrace();

            javax.swing.JOptionPane.showMessageDialog(this,
                "Error during disapproval: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCancel3ActionPerformed

    private void refreshEmployeeRequestTable() {
    try {
        System.out.println("=== Refreshing Employee Request Table ===");

        String selectedType = comboTypeRequest1.getSelectedItem().toString();
        
        ArrayList<ArrayList<String>> requestData;
        
        if (selectedType.equals("All Request")) {
            requestData = supervisor.getPendingRequestsOnly();
        } else {
            requestData = supervisor.getAllRequestData(selectedType);
            requestData.removeIf(row -> {
                if (row.size() > 8) {
                    String status = row.get(8); 
                    return status.equals("Approved") || status.equals("Rejected");
                }
                return false;
            });
        }

        supervisor.setTableData(requestData);
        supervisor.setTableSize(9); 

        supervisor.displayDataTable(jTableAllRequest1);
        
        System.out.println("Employee request table refreshed with " + requestData.size() + " pending requests");

        jTableAllRequest1.clearSelection();
        
    } catch (Exception e) {
        System.err.println("Error refreshing employee request table: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void refreshRequestTable() {
    try {
        System.out.println("=== Refreshing Personal Request Table ===");

        String selectedType = comboTypeRequest.getSelectedItem().toString();
        
        ArrayList<ArrayList<String>> requestData;
        
        if (selectedType.equals("All Request")) {
            requestData = supervisor.getDataAllRequests();
        } else {
            requestData = supervisor.getDataAllRequests();
        }
        
        supervisor.setTableData(requestData);
        supervisor.setTableSize(7); 

        supervisor.displayDataTable(jTableAllRequest);
        
        System.out.println("Personal request table refreshed with " + requestData.size() + " requests");

        jTableAllRequest.clearSelection();
        
    } catch (Exception e) {
        System.err.println("Error refreshing personal request table: " + e.getMessage());
        e.printStackTrace();
    }
}
    private void setupTableSelection() {
       jTableAllRequest.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
       jTableAllRequest1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
       jTableAllRequest1.addMouseListener(new java.awt.event.MouseAdapter() {
           @Override
           public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = jTableAllRequest1.rowAtPoint(evt.getPoint());
            if (row >= 0) {
                jTableAllRequest1.setRowSelectionInterval(row, row);
                System.out.println("Employee request row " + row + " selected via mouse click");

                try {
                    String displayId = jTableAllRequest1.getValueAt(row, 0).toString();
                    String name = jTableAllRequest1.getValueAt(row, 1).toString();
                    int realId = supervisor.getRealIdFromDisplayRow(jTableAllRequest1, row);
                    System.out.println("Selected: Display ID=" + displayId + ", Real ID=" + realId + ", Name=" + name);
                } catch (Exception e) {
                    System.out.println("Error reading selected row: " + e.getMessage());
                }
            }
        }
    });

    jTableAllRequest.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = jTableAllRequest.rowAtPoint(evt.getPoint());
            if (row >= 0) {
                jTableAllRequest.setRowSelectionInterval(row, row);
                System.out.println("Personal request row " + row + " selected via mouse click");
            }
        }
    });
    
    System.out.println("Table selection setup completed for both tables");
}
    private void comboEmployeeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEmployeeNameActionPerformed
        // TODO add your handling code here:
         try {
        System.out.println("=== DEBUG: ComboBox Selection Changed ===");

        Object selectedItem = comboEmployeeName.getSelectedItem();

            if (selectedItem == null) {
                System.out.println("Selected item is null");
                clearDTRTable();
                return;
            }

            String selectedEmployee = selectedItem.toString().trim();
            System.out.println("Selected employee: '" + selectedEmployee + "'");

            if (selectedEmployee.isEmpty() || 
                selectedEmployee.contains("Loading") || 
                selectedEmployee.contains("Select Employee") ||
                selectedEmployee.startsWith("--")) {
                System.out.println("Skipping empty or default selection");
                clearDTRTable();
                return;
            }

            System.out.println("Loading DTR for: " + selectedEmployee);

            supervisor.setSelectedEmployeeName(selectedEmployee);

            Calendar today = Calendar.getInstance();
            int currentDay = today.get(Calendar.DAY_OF_MONTH);

            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();

            if (currentDay <= 15) {
                startCal.set(Calendar.DAY_OF_MONTH, 1);
                endCal.set(Calendar.DAY_OF_MONTH, 15);
            } else {
                startCal.set(Calendar.DAY_OF_MONTH, 16);
                endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
            }

            ArrayList<ArrayList<String>> dtrData = supervisor.getEmployeeDTR(
                selectedEmployee, 
                startCal.getTime(), 
                endCal.getTime()
            );

            supervisor.setTableData(dtrData);
            supervisor.setTableSize(8); 
            supervisor.displayDataTable(jTableDTR);

            System.out.println("Loaded " + dtrData.size() + " DTR records for " + selectedEmployee);

        } catch (Exception e) {
            System.err.println("ERROR in comboEmployeeNameActionPerformed: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employee DTR: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_comboEmployeeNameActionPerformed

    private void btnForwardToPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForwardToPayrollActionPerformed
        try {
            System.out.println("=== Supervisor Forwarding DTR to Payroll ===");

            int[] selectedRows = jTableDTR.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please select DTR entries to forward to payroll.", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) jTableDTR.getModel();

            for (int row : selectedRows) {
                String toPayrollStatus = model.getValueAt(row, 6).toString(); 
                if (toPayrollStatus.equals("Yes")) {
                    JOptionPane.showMessageDialog(this, 
                        "Some entries are already forwarded to payroll.", 
                        "Already Forwarded", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String toSupervisorStatus = model.getValueAt(row, 5).toString(); 
                if (toSupervisorStatus.equals("No")) {
                    JOptionPane.showMessageDialog(this, 
                        "Some entries are not yet submitted to supervisor.\n" +
                        "DTR must be submitted to supervisor before forwarding to payroll.", 
                        "Not Submitted to Supervisor", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            String employeeName = supervisor.getSelectedEmployeeName();
            int choice = JOptionPane.showConfirmDialog(this,
                "Forward " + selectedRows.length + " DTR entries to Payroll Staff?\n\n" +
                "Employee: " + employeeName + "\n" +
                "This action cannot be undone.",
                "Confirm Forward to Payroll", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (choice != JOptionPane.YES_OPTION) return;

            ArrayList<ArrayList<String>> forwardData = new ArrayList<>();
            for (int row : selectedRows) {
                ArrayList<String> rowData = new ArrayList<>();
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object value = model.getValueAt(row, col);
                    rowData.add(value != null ? value.toString() : "");
                }
                forwardData.add(rowData);
            }

            boolean success = supervisor.forwardDTRToPayroll(forwardData);

            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "DTR entries forwarded to Payroll Staff successfully!", 
                    "Forward Successful", 
                    JOptionPane.INFORMATION_MESSAGE);

                refreshSupervisorDTRTable();

            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to forward DTR entries to payroll.", 
                    "Forward Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("ERROR in DTR forwarding: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error forwarding DTR: " + e.getMessage(), 
                "Forward Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnForwardToPayrollActionPerformed

    private void refreshSupervisorDTRTable() {
        try {
            String selectedEmployee = supervisor.getSelectedEmployeeName();
            if (selectedEmployee != null && !selectedEmployee.trim().isEmpty()) {
                comboEmployeeNameActionPerformed(null);
                System.out.println("Supervisor DTR table refreshed for: " + selectedEmployee);
            }
        } catch (Exception e) {
            System.err.println("Error refreshing Supervisor DTR table: " + e.getMessage());
        }
    }
    private void clearDTRTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) jTableDTR.getModel();
            model.setRowCount(0);
            System.out.println("DTR table cleared");
        } catch (Exception e) {
            System.err.println("Error clearing DTR table: " + e.getMessage());
        }
    }

    private void setupEmployeeDTRTable() {
        try {
            DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ATT_ID", "EMPLOYEE", "DATE", "LOGIN", "LOGOUT", "TO SUPERVISOR", "TO PAYROLL", "REMARKS"
                }
            ) {
                boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            };

            jTableDTR.setModel(model);
            jTableDTR.getColumnModel().getColumn(0).setMinWidth(0);
            jTableDTR.getColumnModel().getColumn(0).setMaxWidth(0);
            jTableDTR.getColumnModel().getColumn(0).setWidth(0);
            jTableDTR.getColumnModel().getColumn(0).setPreferredWidth(0);
            jTableDTR.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            System.out.println("Supervisor Employee DTR table setup completed");

        } catch (Exception e) {
            System.err.println("Error setting up Supervisor Employee DTR table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void btnPersonalDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPersonalDetailsActionPerformed
        // TODO add your handling code here:
        mainTabbed.setSelectedIndex(0);
        supervisor.viewPersonalDetails(lblIDSidebar.getText());
        
        txtID.setText(String.valueOf(supervisor.accountDetails.getEmployeeID()));
        txtFName.setText(supervisor.accountDetails.getFirstName());
        txtLName.setText(supervisor.accountDetails.getLastName());
        setDateTextField(txtBDay, supervisor.accountDetails.getBirthday());
        txtPhoneNum.setText(supervisor.accountDetails.getPhoneNumber());
        textAreaAddress.setText(supervisor.accountDetails.getAddress());
        txtBasicSalary.setText(String.valueOf(supervisor.accountDetails.getBasicSalary()));
        txtBiMonthlyRate.setText(String.valueOf(supervisor.accountDetails.getSemiBasicSalary()));
        txtHourlyRate.setText(String.valueOf(supervisor.accountDetails.getHourlyRate()));
        txtRiceSubsidy.setText(String.valueOf(supervisor.accountDetails.getRiceSubsidy()));
        
        // FIXED: Phone allowance was showing rice subsidy
        txtPhoneAllowance.setText(String.valueOf(supervisor.accountDetails.getPhoneAllowance()));
        txtClothingAllowance.setText(String.valueOf(supervisor.accountDetails.getClothingAllowance()));
        
        txtPhilNum.setText(fixNumber(supervisor.accountDetails.getPhilHealthNumber()));
        txtSSSNum.setText(fixNumber(supervisor.accountDetails.getSSSNumber()));
        txtTINNum.setText(fixNumber(supervisor.accountDetails.getTinNumber()));
        txtPagIbigNum.setText(fixNumber(supervisor.accountDetails.getPagibigNumber()));
        
        txtPosition.setText(supervisor.accountDetails.getPosition());
        txtStatus.setText(supervisor.accountDetails.getStatus());
        txtSupervisor.setText(supervisor.accountDetails.getSupervisor());
    }//GEN-LAST:event_btnPersonalDetailsActionPerformed

    private void btnRequestPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRequestPortActionPerformed
        // TODO add your handling code here:.
         try {
            mainTabbed.setSelectedIndex(1);
            comboTypeRequest.setSelectedIndex(0);
            tabbedInsideRequest.setSelectedIndex(0);

            System.out.println("Loading requests for supervisor: " + supervisor.getEmployeeID());

            ArrayList<ArrayList<String>> allRequests = supervisor.getDataAllRequests();
            System.out.println("Requests loaded: " + allRequests.size());

            supervisor.setTableData(allRequests);
            supervisor.setTableSize(7);
            supervisor.displayDataTable(jTableAllRequest);

            System.out.println("Table updated with " + jTableAllRequest.getRowCount() + " rows");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnRequestPortActionPerformed

    private void btnDTRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDTRActionPerformed
        // TODO add your handling code here:
        mainTabbed.setSelectedIndex(2);

        supervisor.viewPersonalDetails(lblIDSidebar.getText());
        lblID2.setText(String.valueOf(supervisor.accountDetails.getEmployeeID()));
        lblMyName4.setText(supervisor.accountDetails.getEmployeeCompleteName());

        Calendar today = Calendar.getInstance();
        int currentDay = today.get(Calendar.DAY_OF_MONTH);

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        if (currentDay <= 15) {
            startCal.set(Calendar.DAY_OF_MONTH, 1);
            endCal.set(Calendar.DAY_OF_MONTH, 15);
        } else {
            startCal.set(Calendar.DAY_OF_MONTH, 16);
            endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
    
        dateFrom2.setDate(startCal.getTime());
        dateTo2.setDate(endCal.getTime());

        supervisor.setTableData(supervisor.getDataAllDTR(startCal.getTime(), endCal.getTime()));
        supervisor.setTableSize(5);
        supervisor.displayDataTable(jTableAllDTR);
    }//GEN-LAST:event_btnDTRActionPerformed

    private void btnLeaveLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveLedgerActionPerformed
        // TODO add your handling code here:
        mainTabbed.setSelectedIndex(3);

        supervisor.viewPersonalDetails(lblIDSidebar.getText());
        lblID3.setText(String.valueOf(supervisor.accountDetails.getEmployeeID()));
        lblMyName5.setText(supervisor.accountDetails.getEmployeeCompleteName());
        supervisor.updateLeaveBalanceLabels(lblVLBalance1, lblSLBalance1);
        
        supervisor.setTableData(supervisor.allApprovedPersonalLeaveLedger());
        supervisor.setTableSize(7);
        supervisor.displayDataTable(jTableAllRequest3);

    }//GEN-LAST:event_btnLeaveLedgerActionPerformed

    private void btnpayslipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpayslipActionPerformed
        // TODO add your handling code here:
        mainTabbed.setSelectedIndex(4);
    }//GEN-LAST:event_btnpayslipActionPerformed

    private void txtBasicSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBasicSalaryActionPerformed

    }//GEN-LAST:event_txtBasicSalaryActionPerformed

    private void txtRiceSubsidyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRiceSubsidyActionPerformed

    }//GEN-LAST:event_txtRiceSubsidyActionPerformed

    private void txtPhoneAllowanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneAllowanceActionPerformed

    }//GEN-LAST:event_txtPhoneAllowanceActionPerformed

    private void txtClothingAllowanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClothingAllowanceActionPerformed

    }//GEN-LAST:event_txtClothingAllowanceActionPerformed

    private void txtPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPositionActionPerformed

    private void txtSupervisorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupervisorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupervisorActionPerformed

    private void comboTypeRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTypeRequestActionPerformed
        // TODO add your handling code here:
            try {
                String selectedItem = comboTypeRequest.getSelectedItem().toString();
                System.out.println("Request type changed to: " + selectedItem);

                if (selectedItem.equals("All Request")) {
                    tabbedInsideRequest.setSelectedIndex(0);

                    ArrayList<ArrayList<String>> requests = supervisor.getPendingRequestsOnly();
                    supervisor.setTableData(requests);
                    supervisor.setTableSize(9);
                    supervisor.displayDataTable(jTableAllRequest);

                } else if (selectedItem.equals("Leave Application")) {
                    tabbedInsideRequest.setSelectedIndex(1);

                    supervisor.leaveBalancesInformation();
                    lblID.setText(String.valueOf(supervisor.accountDetails.getEmployeeID()));
                    lblMyName.setText(supervisor.accountDetails.getEmployeeCompleteName());
                    lblVLBalance.setText(supervisor.getBalanceVL());
                    lblSLBalance.setText(supervisor.getBalanceSL());

                } else {
                    tabbedInsideRequest.setSelectedIndex(2);

                    ArrayList<ArrayList<String>> requests = supervisor.getAllRequestData(selectedItem);

                    requests.removeIf(row -> {
                        if (row.size() > 8) {
                            String status = row.get(8);
                            return status.equals("Approved") || status.equals("Rejected");
                        }
                        return false;
                    });

                    supervisor.setTableData(requests);
                    supervisor.setTableSize(9);
                    supervisor.displayDataTable(jTableAllRequest);
                }

            } catch (Exception e) {
                System.err.println("Error in combo box selection: " + e.getMessage());
                e.printStackTrace();
            }
    }//GEN-LAST:event_comboTypeRequestActionPerformed

    
    
    private void dateToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateToPropertyChange
        // TODO add your handling code here:
        if (supervisor != null && "date".equals(evt.getPropertyName())) {
            updateOvertimeDaysCalculation();
        }
    }//GEN-LAST:event_dateToPropertyChange

    private void dateFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateFromPropertyChange
        // TODO add your handling code here:
        if (supervisor != null && "date".equals(evt.getPropertyName())) {
            updateLeaveDaysCalculation();
        }
    }//GEN-LAST:event_dateFromPropertyChange

    private void updateLeaveDaysCalculation() {
        try {
            if (dateFrom.getDate() != null && dateTo.getDate() != null) {
                if (dateTo.getDate().before(dateFrom.getDate())) {
                    txtDaysNumber.setText("0");
                    return;
                }
                
                int days = supervisor.calculateDaysFromDates(dateFrom.getDate(), dateTo.getDate());
                txtDaysNumber.setText(String.valueOf(days));
            } else {
                txtDaysNumber.setText("0");
            }
        } catch (Exception e) {
            System.err.println("ERROR in updateLeaveDaysCalculation: " + e.getMessage());
            txtDaysNumber.setText("0");
        }
    }
    
    private void updateOvertimeDaysCalculation() {
        try {
            if (dateFromOvertime.getDate() != null && dateToOvertime.getDate() != null) {
                if (dateToOvertime.getDate().before(dateFromOvertime.getDate())) {
                    txtDaysNumber1.setText("0");
                    return;
                }
                
                int days = supervisor.calculateDaysFromDates(dateFromOvertime.getDate(), dateToOvertime.getDate());
                txtDaysNumber1.setText(String.valueOf(days));
            } else {
                txtDaysNumber1.setText("0");
            }
        } catch (Exception e) {
            System.err.println("ERROR in updateOvertimeDaysCalculation: " + e.getMessage());
            txtDaysNumber1.setText("0");
        }
    }
    
    private void txtDaysNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDaysNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDaysNumberActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:    
        if (comboLeaveType.getSelectedIndex() == 0 || dateFrom.getDate() == null || 
            dateTo.getDate() == null || txtReason.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (txtDaysNumber.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "Invalid date range!", "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(supervisor.accountDetails.getEmployeeID()));
        data.add(supervisor.accountDetails.getEmployeeCompleteName());
        data.add(comboLeaveType.getSelectedItem().toString());
        data.add(dateFormat.format(dateFrom.getDate()));
        data.add(dateFormat.format(dateTo.getDate()));
        data.add(txtDaysNumber.getText());
        data.add(txtReason.getText().trim());

        if (supervisor.fileLeaveRequest(data)) {
            clearLeaveForm();
            JOptionPane.showMessageDialog(this, "Leave request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);


            ArrayList<ArrayList<String>> updatedRequests = supervisor.getDataAllRequests();
            supervisor.setTableData(updatedRequests);
            supervisor.setTableSize(7);
            supervisor.displayDataTable(jTableAllRequest);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit leave request!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void dateFromOvertimePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateFromOvertimePropertyChange
        // TODO add your handling code here:
        if(dateToOvertime.getDate() != null && dateFromOvertime.getDate() != null){
            if(supervisor.isValidDateRange(dateFromOvertime.getDate(), dateToOvertime.getDate())){
                txtDaysNumber1.setText(String.valueOf(supervisor.getNumberOfDaysLeave()));
                supervisor.setNumberOfDaysLeave();
            }else{
                supervisor.setNumberOfDaysLeave();
                txtDaysNumber1.setText(String.valueOf(supervisor.getNumberOfDaysLeave()));
            }
        }
    }//GEN-LAST:event_dateFromOvertimePropertyChange

    private void dateToOvertimePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateToOvertimePropertyChange
        // TODO add your handling code here:
        if (supervisor != null && "date".equals(evt.getPropertyName())) {
            updateOvertimeDaysCalculation();
        }
    }//GEN-LAST:event_dateToOvertimePropertyChange

    private void btnSubmit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmit1ActionPerformed
        // TODO add your handling code here:
        if (dateToOvertime.getDate() != null && dateFromOvertime.getDate() != null && !txtReasonOvertime.getText().trim().isEmpty()) {
            if (supervisor.isValidDateRange(dateFromOvertime.getDate(), dateToOvertime.getDate())) {

                ArrayList<String> data = new ArrayList<>();
                data.add(String.valueOf(supervisor.accountDetails.getEmployeeID()));
                data.add(supervisor.accountDetails.getEmployeeCompleteName());
                data.add(dateFormat.format(dateFromOvertime.getDate()));
                data.add(dateFormat.format(dateToOvertime.getDate()));
                data.add(txtDaysNumber1.getText());
                data.add(txtReasonOvertime.getText());

                if (supervisor.fileOvertimeRequest(data)) {
                    clearOvertimeForm();
                    supervisor.setNumberOfDaysLeave();
                    JOptionPane.showMessageDialog(null, "Successfully filed an overtime request!");

                    ArrayList<ArrayList<String>> updatedRequests = supervisor.getDataAllRequests();
                    supervisor.setTableData(updatedRequests);
                    supervisor.setTableSize(7);
                    supervisor.displayDataTable(jTableAllRequest);
                } else {
                    JOptionPane.showMessageDialog(null, "Error filing overtime request!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid date range - From date cannot be after To date!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please provide all the necessary details for overtime!");
        }
    }//GEN-LAST:event_btnSubmit1ActionPerformed

    private void txtDaysNumber1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDaysNumber1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDaysNumber1ActionPerformed

    private void btnSubmitToSepervisorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitToSepervisorActionPerformed
        // TODO add your handling code here:
        ArrayList<ArrayList<String>> tempData = new ArrayList<>();
        int[] selectedRows = jTableAllDTR.getSelectedRows();
        DefaultTableModel model = (DefaultTableModel) jTableAllDTR.getModel();
        
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Please select DTR entries to submit!");
            return;
        }
        
        for (int row : selectedRows) {
            String submittedStatus = model.getValueAt(row, 3).toString();
            if (submittedStatus.equals("Yes")) {
                JOptionPane.showMessageDialog(null, "You have selected a DTR that was already forwarded to supervisor!");
                return;
            }
            
            ArrayList<String> rowData = new ArrayList<>();
            rowData.add(model.getValueAt(row, 0).toString());
            rowData.add(submittedStatus); 
            tempData.add(rowData);
        }
        
        if (!tempData.isEmpty()) {
            try {
                supervisor.forwardDTRToSupervisor(tempData);
                
                supervisor.setTableData(supervisor.getDataAllDTR(dateFrom2.getDate(), dateTo2.getDate()));
                supervisor.setTableSize(5);
                supervisor.displayDataTable(jTableAllDTR);
                
                JOptionPane.showMessageDialog(null, "Successfully submitted " + tempData.size() + " DTR entries to your supervisor!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error submitting DTR: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnSubmitToSepervisorActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        supervisor.setTableData(supervisor.getDataAllDTR(dateFrom2.getDate(), dateTo2.getDate()));
        supervisor.setTableSize(5);
        supervisor.displayDataTable(jTableAllDTR);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        ArrayList<ArrayList<String>> tempData = supervisor.viewPersonalPayslip(dateFrom3.getDate(), dateTo3.getDate(), lblIDSidebar.getText());
    
        if(tempData.isEmpty()){
            JOptionPane.showMessageDialog(null, "No Payroll Found!");
        } else {
            lblID4.setText(tempData.get(0).get(0));
            lblMyName6.setText(tempData.get(0).get(1));
            lblPayrollPeriod.setText(tempData.get(0).get(2));
            lblPositon.setText(tempData.get(0).get(3));
            lblGross.setText(tempData.get(0).get(4));
            lblBenefits.setText(tempData.get(0).get(5));
            lblOvertime.setText(tempData.get(0).get(6));
            lblUndertime.setText(tempData.get(0).get(7));
            lblSSS.setText(tempData.get(0).get(8));
            lblPhilHealth.setText(tempData.get(0).get(9));
            lblPagIbig.setText(tempData.get(0).get(10));
            lblTax.setText(tempData.get(0).get(11));
            lblNetPay.setText(tempData.get(0).get(12));

            int choice = JOptionPane.showConfirmDialog(null, 
                "Would you like to save this payslip as a file?", 
                "Save Payslip", 
                JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                ReportGenerator generator = new ReportGenerator();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String period = sdf.format(dateFrom3.getDate()) + " to " + sdf.format(dateTo3.getDate());
                generator.generatePayslipPDF(Integer.parseInt(lblIDSidebar.getText()), period);
            }
        }
    }//GEN-LAST:event_btnReportActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Logout This Account?", "Confirm", JOptionPane.YES_NO_OPTION);
        if(choice == 0){
            JOptionPane.showMessageDialog(null, "Successfuly Logout Account!");
            dispose();
            LoginGUI login = new LoginGUI();
            login.setVisible(true);
        }
    }//GEN-LAST:event_jLabel8MouseClicked

    private boolean checkDTRSubmissionStatus(String employeeId, String date) {
        return supervisor.isDTRSubmittedToSupervisor(employeeId, date);
    }       

    private boolean submitDTRToSupervisor(int employeeId, List<String> dates) {
            return supervisor.submitMultipleDTRToSupervisor(employeeId, dates);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SupervisorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SupervisorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SupervisorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SupervisorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SupervisorGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel3;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDTR;
    private javax.swing.JButton btnForwardToPayroll;
    private javax.swing.JButton btnLeaveLedger;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPersonalDetails;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnRequestPort;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnSubmit1;
    private javax.swing.JButton btnSubmitToSepervisor;
    private javax.swing.JButton btnUpdate1;
    private javax.swing.JButton btnemployeeDTR;
    private javax.swing.JButton btnemployeeRequest;
    private javax.swing.JButton btnpayslip;
    private javax.swing.JComboBox<String> comboEmployeeName;
    private javax.swing.JComboBox<String> comboLeaveType;
    private javax.swing.JComboBox<String> comboTypeRequest;
    private javax.swing.JComboBox<String> comboTypeRequest1;
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateFrom2;
    private com.toedter.calendar.JDateChooser dateFrom3;
    private com.toedter.calendar.JDateChooser dateFromOvertime;
    private com.toedter.calendar.JDateChooser dateTo;
    private com.toedter.calendar.JDateChooser dateTo2;
    private com.toedter.calendar.JDateChooser dateTo3;
    private com.toedter.calendar.JDateChooser dateToOvertime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTableAllDTR;
    private javax.swing.JTable jTableAllRequest;
    private javax.swing.JTable jTableAllRequest1;
    private javax.swing.JTable jTableAllRequest3;
    private javax.swing.JTable jTableDTR;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblAllRequest;
    private javax.swing.JLabel lblAllRequest1;
    private javax.swing.JLabel lblAllRequest2;
    private javax.swing.JLabel lblBDay;
    private javax.swing.JLabel lblBasicSalary;
    private javax.swing.JLabel lblBenefits;
    private javax.swing.JLabel lblBiMonthlyRate;
    private javax.swing.JLabel lblClothingAllowanes;
    private javax.swing.JLabel lblEmpID;
    private javax.swing.JLabel lblEmpID1;
    private javax.swing.JLabel lblEmpID2;
    private javax.swing.JLabel lblEmpID3;
    private javax.swing.JLabel lblEmpID4;
    private javax.swing.JLabel lblEmpID5;
    private javax.swing.JLabel lblFName;
    private javax.swing.JLabel lblGross;
    private javax.swing.JLabel lblHourlyRate;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblID1;
    private javax.swing.JLabel lblID2;
    private javax.swing.JLabel lblID3;
    private javax.swing.JLabel lblID4;
    private javax.swing.JLabel lblIDSidebar;
    private javax.swing.JLabel lblLName;
    private javax.swing.JLabel lblLeaveBalances;
    private javax.swing.JLabel lblLeaveBalances1;
    private javax.swing.JLabel lblLeaveBalances2;
    private javax.swing.JLabel lblMyName;
    private javax.swing.JLabel lblMyName1;
    private javax.swing.JLabel lblMyName2;
    private javax.swing.JLabel lblMyName3;
    private javax.swing.JLabel lblMyName4;
    private javax.swing.JLabel lblMyName5;
    private javax.swing.JLabel lblMyName6;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblName1;
    private javax.swing.JLabel lblName10;
    private javax.swing.JLabel lblName11;
    private javax.swing.JLabel lblName12;
    private javax.swing.JLabel lblName13;
    private javax.swing.JLabel lblName14;
    private javax.swing.JLabel lblName15;
    private javax.swing.JLabel lblName16;
    private javax.swing.JLabel lblName18;
    private javax.swing.JLabel lblName19;
    private javax.swing.JLabel lblName2;
    private javax.swing.JLabel lblName21;
    private javax.swing.JLabel lblName3;
    private javax.swing.JLabel lblName4;
    private javax.swing.JLabel lblName5;
    private javax.swing.JLabel lblName7;
    private javax.swing.JLabel lblName8;
    private javax.swing.JLabel lblName9;
    private javax.swing.JLabel lblNameSidebar;
    private javax.swing.JLabel lblNetPay;
    private javax.swing.JLabel lblOvertime;
    private javax.swing.JLabel lblPagIbig;
    private javax.swing.JLabel lblPagIbigNum;
    private javax.swing.JLabel lblPayrollPeriod;
    private javax.swing.JLabel lblPeriod;
    private javax.swing.JLabel lblPhilHealth;
    private javax.swing.JLabel lblPhilNum;
    private javax.swing.JLabel lblPhoneAllowances;
    private javax.swing.JLabel lblPhoneNum;
    private javax.swing.JLabel lblPosition;
    private javax.swing.JLabel lblPositon;
    private javax.swing.JLabel lblRequestType;
    private javax.swing.JLabel lblRequestType1;
    private javax.swing.JLabel lblRequestType2;
    private javax.swing.JLabel lblRiceSubsidy;
    private javax.swing.JLabel lblSL;
    private javax.swing.JLabel lblSL1;
    private javax.swing.JLabel lblSLBalance;
    private javax.swing.JLabel lblSLBalance1;
    private javax.swing.JLabel lblSSS;
    private javax.swing.JLabel lblSSSNum;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSupervisor;
    private javax.swing.JLabel lblTINNum;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblUndertime;
    private javax.swing.JLabel lblVL;
    private javax.swing.JLabel lblVL1;
    private javax.swing.JLabel lblVLBalance;
    private javax.swing.JLabel lblVLBalance1;
    private javax.swing.JTabbedPane mainTabbed;
    private javax.swing.JPanel panelAllRequest;
    private javax.swing.JPanel panelAllRequest1;
    private javax.swing.JPanel panelAllRequest2;
    private javax.swing.JPanel panelDTR;
    private javax.swing.JPanel panelLeaveLedger;
    private javax.swing.JPanel panelLeaveRequestDetails;
    private javax.swing.JPanel panelOvertimeRequest;
    private javax.swing.JPanel panelPayslip;
    private javax.swing.JPanel panelPersonalDetails;
    private javax.swing.JPanel panelPersonalDetails1;
    private javax.swing.JPanel panelPersonalDetails2;
    private javax.swing.JPanel panelPersonalDetails3;
    private javax.swing.JPanel panelTypeRequest;
    private javax.swing.JPanel panelTypeRequest1;
    private javax.swing.JPanel panelTypeRequest2;
    private javax.swing.JPanel sideBarPanel;
    private javax.swing.JPanel tabbedDTR;
    private javax.swing.JPanel tabbedEmployeeDTR;
    private javax.swing.JPanel tabbedEmployeeRequest;
    private javax.swing.JTabbedPane tabbedInsideRequest;
    private javax.swing.JTabbedPane tabbedInsideRequest1;
    private javax.swing.JTabbedPane tabbedInsideRequest2;
    private javax.swing.JPanel tabbedLeaveLedger;
    private javax.swing.JPanel tabbedPayslip;
    private javax.swing.JPanel tabbedPersonalDetails;
    private javax.swing.JPanel tabbedRequest;
    private javax.swing.JScrollPane tableDTR;
    private javax.swing.JScrollPane tableDTR1;
    private javax.swing.JTextArea textAreaAddress;
    private javax.swing.JTextField txtBDay;
    private javax.swing.JTextField txtBasicSalary;
    private javax.swing.JTextField txtBiMonthlyRate;
    private javax.swing.JTextField txtClothingAllowance;
    private javax.swing.JTextField txtDaysNumber;
    private javax.swing.JTextField txtDaysNumber1;
    private javax.swing.JTextField txtFName;
    private javax.swing.JTextField txtHourlyRate;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtLName;
    private javax.swing.JTextField txtPagIbigNum;
    private javax.swing.JTextField txtPhilNum;
    private javax.swing.JTextField txtPhoneAllowance;
    private javax.swing.JTextField txtPhoneNum;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextArea txtReason;
    private javax.swing.JTextArea txtReasonOvertime;
    private javax.swing.JTextField txtRiceSubsidy;
    private javax.swing.JTextField txtSSSNum;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtSupervisor;
    private javax.swing.JTextField txtTINNum;
    // End of variables declaration//GEN-END:variables
}

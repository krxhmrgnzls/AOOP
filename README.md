![Image](https://github.com/user-attachments/assets/ec43d712-07cd-478a-bbf0-fd7b38dd4b17)

# MotorPH Payroll System
A Java-based automated payroll management system designed to streamline MotorPH's payroll processes, enhance accuracy, and ensure compliance with labor regulations. The system features role-based access control, automated salary calculations, attendance tracking, and comprehensive reporting capabilities.

  ## Table of Contents
  - [Introduction](#introduction)
  - [Getting Started](#getting-started)
  - [User's Guide](#users-guide)
    - [Employee Login](#employee-login)
    - [Supervisor Login](#supervisor-login)
    - [HR Login](#hr-login)
    - [Payroll Staff Login](#payroll-staff-login)
  - [Technical Information](#technical-information)
  - [Testing](#testing)
  - [Authors](#authors)

## Introduction
MotorPH is committed to modernizing its payroll system to support business expansion and improve operational efficiency. This automated payroll system transitions from manual processes to a streamlined, digital approach that minimizes errors, reduces administrative workload, and ensures timely salary disbursements.

  ### Key Objectives:
  - Automate payroll calculations and processing
  - Improve accuracy and compliance
  - Provide role-based access for different user types
  - Generate comprehensive reports and payslips
  - Track employee attendance and leave management

  ### User Roles
  - 👤 **Employee** - Access personal payroll information and manage attendance
  - 👨‍💼 **Supervisor** - Manage team attedance and approve leave request
  - 👩‍💼 **HR** - Oversee employee management and leav administration
  - 💰 **Payroll Staff** - Process payroll calculations and generate reports


## Getting Started
  ### System Requirements
  - **Proccessor:** Any modern processor capable of running Java applications
  - **Memory:** At least 4GB RAM recommended
  - **Storage:** Sufficient space for project files and database
  - **Operating System:** Windows, macOS, or Linux
  - **Network:** Required for database connectivity

  ### Prerequisites
  - **Java JDK 17 or higher**
  - **MySQL 8.0+**
  - **IDE:** Netbeans
  - **MySQL Workbench** (recommended)
  
## Installation Steps
  **1. Install Required Software:**
  - Install Java Development Kit (JDK 17+)
  - Install MySQL Server and MySQL Workbench
  - Install Netbeans IDE

  **2. Clone the Repository:**
  - git clone https://github.com/krxhmrgnzls/AOOP
  - cd motorph-payroll-system

  **3. Database Setup:**
  - Import the provided SQL files in this order:
    - source database/DB_tables.sql
    - source database/DB_views.sql
    - source database/DB_stored_procedures.sql
  - Update database connection in DatabaseConnection.java:
    - private static final String URL = "jdbc:mysql://localhost:3306/aoop_db";
    - private static final String USERNAME = "root";
    - private static final String PASSWORD = "your_password";
    
  **4. Import Project**
  - Open your IDE and import the project
  - Ensure all dependencies are in the classpath
  - Build the project

**5. Run the Application**
  - Locate the main class com.motorph.gui.Login
  - Right-click and select "Run As" > "Java Application"
  - The login screen should appear

## Dependencies
  The following libraries are included in the project:
  - **MySQL Connector/J** - Database connectivity
  - **JUnit 5** - Unit testing framework
  - **JasperReports** - PDF report generation

## User's Guide
This guide provides step-by-step instructions for using the MotorPH Payroll System. Each user role has specific functionalities designed to meet their operational needs.

### Default Login Credentials
| **Role**      | **Employee ID** | **Password** | **Access Level**         |
|---------------|-----------------|--------------|--------------------------|   
| Employee      | 10003           | 10003        | Basic employee functions |
| Supervisor    | 10004           | 10004        | Team management          |
| HR            | 10001           | 10001        | Employee administration  |
| Payroll Staff | 10002           | 10002        | Payroll processing       |

### 👤Employee Login
insert screenshot

  **Accessing the System:**
  1. Enter your Employee ID and password
  2. Click "Login" to access your dashboard

  **Key Feature:**
  
  **📋 Personal Information Management:**
  - View and update personal detailes
  - Access contact information and employment details
  - View salary structure and benefits information

  **⏰ Time and Attendance:**
  - **Time In/Out:** Record daily attendance with timestamp
  - **Attendance History:** View past attendance records with filtering options
  - **Monthly Reports:** Export attendance data by month/year
  
  **🏖️ Leave Management:**
  - **Leave Balance:** View available vacation and sick leave days
  - **Submit Requests:** Apply for leave with date selection
  - **Request History:** Track status of submitted leave applications

  **💳 Payroll Information:**
  - **View Payslip:** Access detailed salary breakdowns
  - **Exports Payslips:** Download payslips as PDF files
  - **Payroll History:** Review past payroll records

### 👨‍💼Supervisor Login
insert screenshot

  **📊 Team Management Dashboard:**
  - View team member information and attendance
  - Access team payroll summaries
  - Monitor team performance metrics

   **📚Attendance Supervision:**
   - **Review Team DTR:** Monitor daily time records for supervised employees
   - **Attendance Reports:** Generate team attendance summaries
   - **Overtime Tracking:** Review and validate overtime hours

   **🏖️Leave Management:**
   - **Approve/Reject Requests:** Process leave applications from team members
   - **Leave Balance Monitoring:** Track team leave utilization
   - **Leave Calendar:** View team leave schedules

### 👩‍💼HR Login
insert screenshot

  **📋Employee Management:**
  - **Add Employees:** Create new employee records with complete information
  - **Edit Records:** Update employee details, positions, and salary information
  - **Delete Employees:** Remove employees from the system (with confirmation)
 
 **📚Attendance Administration:**
 - **Company-wide Attendance:** View all employee attendance records
 - **Filter by Date:** Search attendance by specific periods
 - **Attendance Reports:**  Generate comprehensive attendance summaries
      
 **🏖️Leave Administration:**
 - **Leave Requests:** Review and process all leave applications
 - **Leave Balances:** Manage and update employee leave entitlements

### 💰Payroll Staff Login
insert screenshot

  **💳Payroll Processing:**
  - **Calculate Payroll:** Process bi-monthly or monthly payroll calculations
  - **Salary Computation:** Automated calculation of gross pay, deductions, and net pay
  - **Tax Calculations:**  Apply appropriate tax brackets and government deductions

  **📈Benefits Administration:**
  -  **Rice Subsidy:** Apply rice allowance to eligible employees
  -  **Phone Allowance:** Process communication allowances
  - **Clothing Allowance:** Calculate uniform allowances
      
  **🗄️Report Generation:**
  - **Individual Payslips:** Generate detailed payslips for each employee
  - **Payroll Summary:** Create comprehensive payroll reports
  - **Tax Reports:** Generate tax withholding reportsCalculate uniform allowances
  - **Export Functions:** Save reports in various formats (PDF)

### Technical Information
**Key Components**

**Core Classes:**
- PayrollStaff.java - Payroll calculations and processing
- Employee.java - Employee data management
- Supervisor.java - Team management functions
- AttendanceService.java - Attendance tracking logic
- ReportGenerator.java - Report creation and export
  
**Database Access:**
- EmployeeDAO.java - Employee data operations
- PayrollDAO.java - Payroll data management
- AttendanceDAO.java - Attendance record handling
- DatabaseConnection.java - Database connectivity management
  
**Models:**
- PayrollRecord.java - Payroll data structure
- AttendanceRecord.java - Attendance data model
- AccountDetails.java - Employee information model
      
**Database Schema**

**Primary Tables:**
- employees - Employee master data
- payroll - Payroll transaction records
- attendance - Daily attendance logs
- login_credentials - User authentication data
  
**Views:**
- employee_profile_view - Consolidated employee information
- payroll_summary_view - Payroll reporting data

**Features Implementation**

**Payroll Calculations:**
- Automated gross pay computation
- Government deduction calculations (SSS, PhilHealth, Pag-IBIG)
- Tax computation based on Philippine tax brackets
- Benefits and allowances processing
- Net pay calculation
- 
**Security Features:**
- Role-based access control
- Secure password authentication
- Session management
- Data validation and sanitization

### Testing
The system includes comprehensive unit tests using JUnit 5 framework.

**Running Tests**

Run all test
- mvn test

Run specific test class
- java -cp "classpath" org.junit.platform.console.ConsoleLauncher --class-path target/classes -- scan-classpath

**Test Coverage**

- **Database Connectivity:** Connection validation and table existence
- **Authentication:** Login validation and role verification
- **Payroll Calculations:** Salary computation accuracy
- **Attendance Processing:** Time tracking and validation
- **Report Generation:** PDF creation and data export

### Authors

![Image](https://github.com/user-attachments/assets/1bb1cbbd-3fe2-41a5-924e-bf98703151bd)

**Krixhia Marie Gonzales (Project Lead/Quality Assurance)**

![Image](https://github.com/user-attachments/assets/776e9cd3-ed55-4458-acf3-508264aa676d)

**Tristan Marc A. Cunanan (Back-end Developer)**

![Image](https://github.com/user-attachments/assets/b0ad9ba9-53b5-4c72-bcb5-242d29947779)

**Shane Mishael Abrasaldo (Database Administrator)**

![Image](https://github.com/user-attachments/assets/d955e2b0-da9d-43f6-b34e-439df93347f8)

**Maria Chesam Leonor (Front-end Developer)**



    









  

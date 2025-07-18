# MotorPH Payroll System
A Java-based automated payroll management system designed to streamline MotorPH's payroll processes, enhance accuracy, and ensure compliance with labor regulations. The system features role-based access control, automated salary calculations, attendance tracking, and comprehensive reporting capabilities.

  ## Table of Contents
  - Introduction
  - User's Guide
    - Employee Login
    - Supervisor Login
    - HR Login
    - Payroll Staff Login
  - Technical Information
  - Testing
  - Authors


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
  ### Sytem Requirements
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
  - git clone https://github.com/your-repo/motorph-payroll-system.git
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

# User's Guide
This guide provides step-by-step instructions for using the MotorPH Payroll System. Each user role has specific functionalities designed to meet their operational needs.

### Default Login Credentials
### Table
| **Role**      | **Employee ID** | **Password** | **Access Level**         |
|---------------|-----------------|--------------|--------------------------|   
| Employee      | 10003           | 10003        | Basic employee functions |
| Supervisor    | 10004           | 10004        | Team management          |
| HR            | 10001           | 10001        | Employee administration  |
| Payroll Staff | 10002           | 10002        | Payroll processing       |

### 🎯Employee Login
  **Accessing the System:**
  1. Entertiyr Employee ID and password
  2. Click "Login" to access your dashboard

  ### **Key Feature:**
  **Personal Information Management:**
    - View and update personal details
    - Access contact information and employment details
    - View salary structure and benefits information 
    





# AOOP Revised Database

This folder contains the **refactored and normalized** version of the AOOP Payroll System Database, updated in response to feedback to meet 3NF standards, and include Views and Stored Procedures for maintainability and performance.

---

## 📁 Folder Structure

| File / Folder           | Description                                  |
|-------------------------|----------------------------------------------|
| `DB_tables.sql`         | All normalized table definitions and sample data |
| `DB_views.sql`          | View definitions used by the Java application |
| `DB_stored_procedures.sql` | Stored Procedures for automated operations like attendance logging and payroll generation |

---

## 🧠 Key Changes from Original

- The original `employees` table was **split into 4 related tables**:
  - `emp_info` – personal details
  - `emp_details` – employment details (position, status, supervisor)
  - `emp_compensations` – salary and allowances
  - `emp_gov_ids` – government numbers (SSS, TIN, PhilHealth, etc.)

- Added lookup tables:
  - `roles`, `positions`, and `statuses` for cleaner foreign key relationships

- Introduced **views** to simplify commonly used joins:
  - Example: `employee_credentials_view` joins `login_credentials`, `emp_info`, and `roles`

- Introduced **stored procedures**:
  - `add_new_employee`
  - `submit_attendance_log`
  - `generate_payroll`
  - `approve_leave_request`

---

## 🔧 Developer To-Do & Reminders (Java Integration)

1. **Update DAO Classes**  
   Reflect new table names and structures (e.g., `emp_info`, `emp_details`, etc.)

2. **Use Views Instead of Long Joins**  
   Replace repeated joins in code with calls to:
   - `employee_credentials_view`
   - `leave_request_summary_view`
   - `payroll_summary_view`

3. **Replace Manual Queries with Procedures**  
   Call stored procedures like:
   ```sql
   CALL submit_attendance_log(...);
   CALL generate_payroll(...);

4. **No More Use of employees Table**  
   That table is removed. All its data is now distributed in normalized tables.



## 📌 How to Import
  
   Import these files in this order (via MySQL Workbench or CLI):  
   1. DB_tables.sql
   2. DB_views.sql
   3. DB_stored_procedures.sql  

   
   (*Make sure FOREIGN_KEY_CHECKS = 0 during import if needed.*)

## 👥 Contributors
**Database Architect:** Shane Mishael Abrasaldo

**Based on:** Java Payroll System (AOOP Project)

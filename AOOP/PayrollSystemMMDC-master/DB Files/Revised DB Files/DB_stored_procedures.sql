-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: aoop_db
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping routines for database 'aoop_db'
--
/*!50003 DROP PROCEDURE IF EXISTS `approve_leave_request` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `approve_leave_request`(
    IN p_leave_id INT
)
BEGIN
    DECLARE v_employee_id INT;
    DECLARE v_leave_type VARCHAR(50);
    DECLARE v_days DECIMAL(5,2);

    -- Get details from the leave request
    SELECT employee_id, leave_type, number_of_days
    INTO v_employee_id, v_leave_type, v_days
    FROM leave_requests
    WHERE leave_id = p_leave_id;

    -- Update the request status to Approved
    UPDATE leave_requests
    SET status = 'Approved'
    WHERE leave_id = p_leave_id;

    -- Deduct from leave_balances
    IF v_leave_type = 'Vacation Leave' THEN
        UPDATE leave_balances
        SET vacation_leave = vacation_leave - v_days
        WHERE employee_id = v_employee_id;
    ELSEIF v_leave_type = 'Sick Leave' THEN
        UPDATE leave_balances
        SET sick_leave = sick_leave - v_days
        WHERE employee_id = v_employee_id;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `generate_payroll` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generate_payroll`(
    IN p_employee_id INT,
    IN p_payroll_period VARCHAR(50),
    IN p_position VARCHAR(100),
    IN p_days_worked INT,
    IN p_overtime DECIMAL(10,2),
    IN p_undertime DECIMAL(10,2)
)
BEGIN
    DECLARE v_gross_income DECIMAL(12,2);
    DECLARE v_benefits DECIMAL(12,2);
    DECLARE v_sss DECIMAL(10,2);
    DECLARE v_philhealth DECIMAL(10,2);
    DECLARE v_pagibig DECIMAL(10,2);
    DECLARE v_tax DECIMAL(10,2);
    DECLARE v_net_pay DECIMAL(12,2);
    DECLARE v_basic_salary DECIMAL(12,2);
    DECLARE v_rice DECIMAL(10,2);
    DECLARE v_phone DECIMAL(10,2);
    DECLARE v_clothing DECIMAL(10,2);

    -- Get salary and benefits from normalized compensation table
    SELECT 
        basic_salary, rice_subsidy, phone_allowance, clothing_allowance
    INTO 
        v_basic_salary, v_rice, v_phone, v_clothing
    FROM emp_compensations
    WHERE employee_id = p_employee_id;

    -- Compute core values
    SET v_gross_income = (v_basic_salary / 22) * p_days_worked;
    SET v_benefits = v_rice + v_phone + v_clothing;

    -- Simple fixed deductions (can be modified later)
    SET v_sss = v_basic_salary * 0.0625;
    SET v_philhealth = v_basic_salary * 0.03;
    SET v_pagibig = 100.00;

    -- Simple flat 10% tax
    SET v_tax = v_gross_income * 0.10;

    -- Compute final net pay
    SET v_net_pay = v_gross_income + v_benefits + p_overtime - p_undertime
                    - v_sss - v_philhealth - v_pagibig - v_tax;

    -- Insert into payroll table
    INSERT INTO payroll (
        employee_id, payroll_period, position, gross_income, benefits,
        overtime, undertime, sss, philhealth, pagibig, tax, net_pay, status
    ) VALUES (
        p_employee_id, p_payroll_period, p_position, v_gross_income, v_benefits,
        p_overtime, p_undertime, v_sss, v_philhealth, v_pagibig, v_tax, v_net_pay, 'Approved'
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_new_employee` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_new_employee`(
    IN p_last_name VARCHAR(100),
    IN p_first_name VARCHAR(100),
    IN p_birthday DATE,
    IN p_address VARCHAR(255),
    IN p_phone_number VARCHAR(20),
    IN p_sss_number VARCHAR(50),
    IN p_philhealth_number VARCHAR(50),
    IN p_tin_number VARCHAR(50),
    IN p_pagibig_number VARCHAR(50),
    IN p_status VARCHAR(50),
    IN p_position VARCHAR(100),
    IN p_supervisor_id INT,
    IN p_basic_salary DECIMAL(12,2),
    IN p_rice_subsidy DECIMAL(10,2),
    IN p_phone_allowance DECIMAL(10,2),
    IN p_clothing_allowance DECIMAL(10,2),
    IN p_gross_rate DECIMAL(12,2),
    IN p_hourly_rate DECIMAL(10,2)
)
BEGIN
    DECLARE new_emp_id INT;

    -- Step 1: Insert into emp_info
    INSERT INTO emp_info (first_name, last_name, birthday, address, phone_number)
    VALUES (p_first_name, p_last_name, p_birthday, p_address, p_phone_number);

    SET new_emp_id = LAST_INSERT_ID();

    -- Step 2: Insert into emp_gov_ids
    INSERT INTO emp_gov_ids (employee_id, sss_number, philhealth_number, tin_number, pagibig_number)
    VALUES (new_emp_id, p_sss_number, p_philhealth_number, p_tin_number, p_pagibig_number);

    -- Step 3: Insert into emp_details
    INSERT INTO emp_details (employee_id, status_id, position_id, supervisor_id)
    SELECT new_emp_id, s.status_id, p.position_id, p_supervisor_id
    FROM statuses s, positions p
    WHERE s.name = p_status AND p.name = p_position
    LIMIT 1;

    -- Step 4: Insert into emp_compensations
    INSERT INTO emp_compensations (
        employee_id, basic_salary, rice_subsidy, phone_allowance,
        clothing_allowance, gross_rate, hourly_rate
    )
    VALUES (
        new_emp_id, p_basic_salary, p_rice_subsidy, p_phone_allowance,
        p_clothing_allowance, p_gross_rate, p_hourly_rate
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `submit_attendance_log` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `submit_attendance_log`(
    IN p_employee_id INT,
    IN p_log_date DATE,
    IN p_login_time TIME,
    IN p_logout_time TIME,
    IN p_submitted_to_supervisor BOOLEAN,
    IN p_submitted_to_payroll BOOLEAN,
    IN p_remarks VARCHAR(255)
)
BEGIN
    INSERT INTO attendance (
        employee_id, log_date, login_time, logout_time,
        submitted_to_supervisor, submitted_to_payroll, remarks
    )
    VALUES (
        p_employee_id, p_log_date, p_login_time, p_logout_time,
        p_submitted_to_supervisor, p_submitted_to_payroll, p_remarks
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-12 16:09:18

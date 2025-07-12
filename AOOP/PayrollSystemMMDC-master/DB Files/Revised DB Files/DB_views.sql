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
-- Temporary view structure for view `employee_credentials_view`
--

DROP TABLE IF EXISTS `employee_credentials_view`;
/*!50001 DROP VIEW IF EXISTS `employee_credentials_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `employee_credentials_view` AS SELECT 
 1 AS `employee_id`,
 1 AS `full_name`,
 1 AS `password`,
 1 AS `role`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `employee_profile_view`
--

DROP TABLE IF EXISTS `employee_profile_view`;
/*!50001 DROP VIEW IF EXISTS `employee_profile_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `employee_profile_view` AS SELECT 
 1 AS `employee_id`,
 1 AS `first_name`,
 1 AS `last_name`,
 1 AS `birthday`,
 1 AS `address`,
 1 AS `phone_number`,
 1 AS `sss_number`,
 1 AS `philhealth_number`,
 1 AS `tin_number`,
 1 AS `pagibig_number`,
 1 AS `position`,
 1 AS `status`,
 1 AS `supervisor_id`,
 1 AS `basic_salary`,
 1 AS `rice_subsidy`,
 1 AS `phone_allowance`,
 1 AS `clothing_allowance`,
 1 AS `gross_rate`,
 1 AS `hourly_rate`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `payroll_summary_view`
--

DROP TABLE IF EXISTS `payroll_summary_view`;
/*!50001 DROP VIEW IF EXISTS `payroll_summary_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `payroll_summary_view` AS SELECT 
 1 AS `payroll_id`,
 1 AS `employee_id`,
 1 AS `full_name`,
 1 AS `position`,
 1 AS `payroll_period`,
 1 AS `gross_income`,
 1 AS `benefits`,
 1 AS `overtime`,
 1 AS `undertime`,
 1 AS `sss`,
 1 AS `philhealth`,
 1 AS `pagibig`,
 1 AS `tax`,
 1 AS `net_pay`,
 1 AS `status`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `employee_credentials_view`
--

/*!50001 DROP VIEW IF EXISTS `employee_credentials_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `employee_credentials_view` AS select `lc`.`employee_id` AS `employee_id`,concat(`i`.`first_name`,' ',`i`.`last_name`) AS `full_name`,`lc`.`password` AS `password`,`r`.`name` AS `role` from ((`login_credentials` `lc` join `emp_info` `i` on((`lc`.`employee_id` = `i`.`employee_id`))) join `roles` `r` on((`lc`.`role_id` = `r`.`role_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `employee_profile_view`
--

/*!50001 DROP VIEW IF EXISTS `employee_profile_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `employee_profile_view` AS select `i`.`employee_id` AS `employee_id`,`i`.`first_name` AS `first_name`,`i`.`last_name` AS `last_name`,`i`.`birthday` AS `birthday`,`i`.`address` AS `address`,`i`.`phone_number` AS `phone_number`,`g`.`sss_number` AS `sss_number`,`g`.`philhealth_number` AS `philhealth_number`,`g`.`tin_number` AS `tin_number`,`g`.`pagibig_number` AS `pagibig_number`,`p`.`title` AS `position`,`s`.`name` AS `status`,`d`.`supervisor_id` AS `supervisor_id`,`c`.`basic_salary` AS `basic_salary`,`c`.`rice_subsidy` AS `rice_subsidy`,`c`.`phone_allowance` AS `phone_allowance`,`c`.`clothing_allowance` AS `clothing_allowance`,`c`.`gross_rate` AS `gross_rate`,`c`.`hourly_rate` AS `hourly_rate` from (((((`emp_info` `i` join `emp_gov_ids` `g` on((`i`.`employee_id` = `g`.`employee_id`))) join `emp_details` `d` on((`i`.`employee_id` = `d`.`employee_id`))) join `positions` `p` on((`d`.`position_id` = `p`.`position_id`))) join `statuses` `s` on((`d`.`status_id` = `s`.`status_id`))) join `emp_compensations` `c` on((`i`.`employee_id` = `c`.`employee_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `payroll_summary_view`
--

/*!50001 DROP VIEW IF EXISTS `payroll_summary_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `payroll_summary_view` AS select `p`.`payroll_id` AS `payroll_id`,`e`.`employee_id` AS `employee_id`,concat(`e`.`first_name`,' ',`e`.`last_name`) AS `full_name`,`pos`.`title` AS `position`,`p`.`payroll_period` AS `payroll_period`,`p`.`gross_income` AS `gross_income`,`p`.`benefits` AS `benefits`,`p`.`overtime` AS `overtime`,`p`.`undertime` AS `undertime`,`p`.`sss` AS `sss`,`p`.`philhealth` AS `philhealth`,`p`.`pagibig` AS `pagibig`,`p`.`tax` AS `tax`,`p`.`net_pay` AS `net_pay`,`p`.`status` AS `status` from (((`payroll` `p` join `emp_info` `e` on((`p`.`employee_id` = `e`.`employee_id`))) join `emp_details` `d` on((`e`.`employee_id` = `d`.`employee_id`))) join `positions` `pos` on((`d`.`position_id` = `pos`.`position_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-12 16:08:42

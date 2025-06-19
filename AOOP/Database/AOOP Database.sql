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
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `attendance_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `log_date` date DEFAULT NULL,
  `login_time` time DEFAULT NULL,
  `logout_time` time DEFAULT NULL,
  `submitted_to_supervisor` tinyint(1) DEFAULT NULL,
  `submitted_to_payroll` tinyint(1) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`attendance_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES (2,10003,'2025-03-01','08:00:00','17:00:00',1,1,' '),(3,10003,'2025-03-03','08:00:00','17:00:00',1,1,' '),(4,10003,'2025-03-04','08:00:00','17:00:00',1,1,' '),(5,10003,'2025-03-05','08:00:00','17:00:00',1,1,' '),(6,10003,'2025-03-06','08:00:00','17:00:00',1,1,' '),(7,10003,'2025-03-07','08:00:00','17:00:00',1,1,' '),(8,10003,'2025-03-08','08:00:00','17:00:00',1,1,' '),(9,10003,'2025-03-10','08:00:00','17:00:00',1,1,' '),(10,10003,'2025-03-11','08:00:00','17:00:00',1,1,' '),(11,10003,'2025-03-12','08:00:00','17:00:00',1,1,' '),(12,10003,'2025-03-13','08:00:00','17:00:00',1,1,' '),(13,10003,'2025-03-14','08:00:00','17:00:00',1,1,' ');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `employee_id` int NOT NULL AUTO_INCREMENT,
  `last_name` varchar(100) DEFAULT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `sss_number` varchar(50) DEFAULT NULL,
  `philhealth_number` varchar(50) DEFAULT NULL,
  `tin_number` varchar(50) DEFAULT NULL,
  `pagibig_number` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `supervisor_id` int DEFAULT NULL,
  `basic_salary` decimal(12,2) DEFAULT NULL,
  `rice_subsidy` decimal(10,2) DEFAULT NULL,
  `phone_allowance` decimal(10,2) DEFAULT NULL,
  `clothing_allowance` decimal(10,2) DEFAULT NULL,
  `gross_rate` decimal(12,2) DEFAULT NULL,
  `hourly_rate` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`employee_id`),
  KEY `supervisor_id` (`supervisor_id`),
  CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`supervisor_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10036 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (10001,'Garcia','Manuel III','1983-10-10','Valero Carpark Building Valero Street 1227 Makati City','966-860-270','44-4506057-3','8.20127E+11','442-605-657-000','6.91295E+11','Regular','Chief Executive Officer',NULL,90000.00,1500.00,2000.00,1000.00,45000.00,535.71),(10002,'Lim','Antonio','1988-06-19','San Antonio De Padua 2 Block 1 Lot 8 and 2 Dasmarinas Cavite','171-867-411','52-2061274-9','3.31736E+11','683-102-776-000','6.63905E+11','Regular','Chief Operating Officer',NULL,60000.00,1500.00,2000.00,1000.00,30000.00,357.14),(10003,'Aquino','Bianca Sofia','1989-08-04','Rm. 402 4/F Jiao Building Timog Avenue Cor. Quezon Avenue 1100 Quezon City','966-889-370','30-8870406-2','1.77451E+11','971-711-280-000','1.7152E+11','Regular','Chief Finance Officer',NULL,60000.00,1500.00,2000.00,1000.00,30000.00,357.14),(10004,'Reyes','Isabella','1994-06-16','460 Solanda Street Intramuros 1000 Manila','786-868-477','40-2511815-0','3.41911E+11','876-809-437-000','4.16947E+11','Regular','Chief Marketing Officer',NULL,60000.00,1500.00,2000.00,1000.00,30000.00,357.14),(10005,'Hernandez','Eduard','1989-09-23','National Highway Gingoog Misamis Occidental','088-861-012','55-7453448-4','9.57436E+11','103-100-522-000','6.31053E+11','Regular','IT Operations and Systems',NULL,52670.00,1500.00,1000.00,1000.00,26335.00,313.51),(10006,'Villanueva','Andrea Mae','1988-02-14','17/85 Stracke Via Suite 042 Poblacion Las Piñas 4783 Dinagat Islands','918-621-603','49-1632020-8','3.82189E+11','112-508-198-000','7.19225E+11','Regular','HR Manager',NULL,52670.00,1500.00,1000.00,1000.00,26335.00,313.51),(10007,'San Jose','Brad','1996-03-15','99 Strosin Hills Poblacion Bislig 5340 Tawi-Tawi','797-009-261','40-2400714-1','2.39193E+11','210-805-911-000','1.14901E+11','Regular','HR Team Leader',NULL,42975.00,1500.00,800.00,800.00,21487.50,255.80),(10008,'Romualdez','Fredrick','1992-06-28','12A/33 Upton Isle Apt. 420 Roxas City 1814 Surigao del Norte','983-606-799','26-9647608-3','1.26445E+11','275-792-513-000','5.05279E+11','Regular','HR Rank and File',NULL,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10009,'Atienza','Cristina','1990-05-11','90A Dibbert Terrace Apt. 190 San Lorenzo 6056 Davao del Norte','266-036-427','55-4476527-2','5.45653E+11','888-572-294-000','3.99665E+11','Regular','HR Rank and File',NULL,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10010,'Alvaro','Roderick','1988-03-09','#284 T. Morato corner Scout Rallos Street Quezon City','053-381-386','26-8768374-1','4.31709E+11','604-997-793-000','6.3113E+11','Regular','Accounting Manager',NULL,52670.00,1500.00,1000.00,1000.00,26335.00,313.51),(10011,'Salcedo','Anthony','1993-09-14','93/54 Shanahan Alley Apt. 183 Santo Tomas 1572 Masbate','070-766-300','26-7145133-4','2.33694E+11','210-835-851-000','7.19807E+11','Regular','Payroll Manager',NULL,50825.00,1500.00,1000.00,1000.00,25412.50,302.53),(10012,'Lopez','Christian','1991-12-20','49 Springs Apt. 266 Poblacion Taguig 3200 Occidental Mindoro','478-355-427','52-0109570-6','5.15741E+11','275-325-334-000','9.1666E+11','Regular','Payroll Team Leader',NULL,38475.00,1500.00,800.00,800.00,19237.50,229.02),(10013,'Mendoza','Angelo','1998-12-30','42/25 Sawayn Stream Ubay 1208 Zamboanga del Norte','526-639-511','60-1152426-4','3.08367E+11','595-800-251-000','1.04908E+11','Regular','Payroll Rank and File',NULL,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10014,'Santos','Angelica','2000-06-15','37/46 Kulas Roads Maragondon 0962 Quirino','329-034-366','60-8359821-7','7.45148E+11','121-203-336-000','9.45358E+11','Regular','Payroll Rank and File',NULL,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10015,'Rosario','Cydney','1996-08-24','22A/52 Lubowitz Meadows Pililla 4895 Zambales','877-110-749','55-5053-122-5','3.08105E+11','122-244-511-000','4.12523E+11','Regular','Account Manager',NULL,53500.00,1500.00,1000.00,1000.00,26750.00,318.45),(10016,'Bautista','Sandra','1993-02-15','90 O\'Keefe Spur Apt. 379 Catigbian 2772 Sulu','683-725-348','49-1629900-2','7.45667E+11','218-489-737-000','9.55072E+11','Regular','Account Team Leader',NULL,42975.00,1500.00,800.00,800.00,21487.50,255.80),(10017,'Villanueva','Andrea Mae','1978-08-27','89A Armstrong Trace Compostela 7874 Maguindanao','179-835-694','45-5866331-6','5.15613E+11','236-346-689-000','2.75229E+11','Regular','Account Team Leader',NULL,42975.00,1500.00,800.00,800.00,21487.50,255.80),(10018,'Ramos','Martha','1984-12-28','08 Grant Drive Suite 406 Poblacion Iloilo City 9186 La Union','975-432-139','41-7544986-3','3.99665E+11','215-973-463-000','7.10312E+11','Regular','Account Rank and File',NULL,25275.00,1500.00,500.00,500.00,12637.50,150.45),(10019,'Gutierrez','Rafael','1978-01-31','93A/21 Berge Points Tapaz 2180 Quezon','687-620-489','45-3251383-0','5.78115E+11','316-180-072-000','1.10368E+11','Regular','Account Rank and File',NULL,25275.00,1500.00,500.00,500.00,12637.50,150.45),(10020,'Montero','Rita','1986-12-18','65 Murphy Centers Poblacion Palayan 5636 Quirino','853-169-957','52-8080486-0','3.99665E+11','317-674-022-000','6.97292E+11','Regular','Account Rank and File',NULL,28475.00,1500.00,500.00,500.00,14237.50,169.49),(10021,'De Leon','Leila','1979-06-15','47 Farrell Ridge Suite 588 Poblacion Caloocan 7142 Guimaras','786-868-477','52-1577535-1','6.06078E+11','210-395-397-000','1.82596E+11','Regular','Account Rank and File',NULL,28475.00,1500.00,500.00,500.00,14237.50,169.49),(10022,'Fernandez','Carl','1986-04-06','75A/11 Jerde Meadow Burgos 4238 Albay','684-809-682','40-9504657-8','1.81857E+11','825-797-959-000','8.50438E+11','Regular','Budget Team Leader',NULL,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10023,'Valdez','Jennie','1976-04-28','17 Schmitt Avenue Mabini 4175 Laguna','070-766-300','45-5866331-6','7.45148E+11','888-572-294-000','8.0783E+11','Regular','Collector',NULL,33000.00,1500.00,800.00,800.00,16500.00,196.43),(10024,'Cruz','Rafael','1997-06-17','22A Leannon Meadow Burgos 3355 Southern Leyte','836-050-317','26-9647608-3','2.33694E+11','604-997-793-000','1.3476E+11','Regular','Cashier',NULL,33000.00,1500.00,800.00,800.00,16500.00,196.43),(10025,'Domingo','Gloria','1985-03-27','72/97 Mayert Track Luisiana 1263 Dinagat Islands','070-784-123','40-2400714-1','2.82277E+11','275-797-513-000','7.45487E+11','Regular','Disbursement Team Leader',NULL,33000.00,1500.00,800.00,800.00,16500.00,196.43),(10026,'Santiago','Selena','1986-10-30','80A/48 Gorczany Ridge Luisiana 1263 Dinagat Islands','778-377-830','45-3251383-0','1.73473E+11','210-835-851-000','6.34905E+11','Regular','Billing Team Leader',NULL,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10027,'Mata','Christian','1981-10-21','442 Padberg Spring San Lorenzo 9866 Quirino','975-262-837','52-1052986-1','8.68337E+11','317-674-022-000','6.71192E+11','Regular','Budget Manager',NULL,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10028,'Mercado','Emma','1981-08-13','89 Sporer Motorway Poblacion Palayan 5646 Quirino','269-720-406','40-2511815-0','3.41911E+11','604-997-793-000','9.2317E+11','Regular','Marketing Team Leader',NULL,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10029,'Navarro','Joshua','1987-06-16','63 O\'Keefe Heights Washington 1967 Camiguin','881-837-523','45-5866331-6','8.82909E+11','210-805-911-000','3.64146E+11','Regular','Marketing Staff',NULL,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10030,'Mendoza','Emilio','1973-05-29','316 Padberg Spring Limay 6024 Iloilo','975-432-139','45-3251383-0','8.04398E+11','218-489-737-000','9.2317E+11','Regular','Graphic Designer',NULL,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10031,'Pineda','Christine','1984-11-22','72A Armstrong Trace Compostela 7874 Maguindanao','234-809-682','49-1629900-2','2.39193E+11','275-792-513-000','6.31053E+11','Regular','Content Writer',NULL,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10032,'Torres','Jayson','1973-11-01','72A Armstrong Trace Compostela 7874 Maguindanao','834-860-270','52-0109570-6','5.15741E+11','598-065-074-000','6.71192E+11','Regular','Junior Developer',NULL,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10033,'Castro','Aurora','1979-03-04','22A Lubowitz Meadows Pililla 4895 Zambales','683-725-348','40-9504657-8','5.17626E+11','103-100-522-000','1.04908E+11','Regular','Systems Administrator',NULL,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10034,'Martinez','Divina','1978-10-11','50A/83 San Juan St. Pantok Binangonan Rizal','464-316-285','45-3251383-0','3.80685E+11','923-630-728-000','6.91295E+11','Regular','Network Administrator',NULL,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10035,'John Paul','Arquita','2001-03-01','Davao CIty','9123456789','123-456-789','123456789','123456789','123456789','Regular','Clerk',NULL,90000.00,500.00,500.00,500.00,45000.00,535.71);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_balances`
--

DROP TABLE IF EXISTS `leave_balances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_balances` (
  `balance_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `vacation_leave` decimal(5,2) DEFAULT NULL,
  `sick_leave` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`balance_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `leave_balances_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_balances`
--

LOCK TABLES `leave_balances` WRITE;
/*!40000 ALTER TABLE `leave_balances` DISABLE KEYS */;
INSERT INTO `leave_balances` VALUES (1,10001,24.00,24.00),(2,10002,24.00,24.00),(3,10003,24.00,24.00),(4,10004,19.00,24.00),(5,10005,24.00,24.00),(6,10006,24.00,24.00),(7,10007,24.00,24.00),(8,10008,24.00,24.00),(9,10009,24.00,24.00),(10,10010,24.00,24.00),(11,10011,24.00,24.00),(12,10012,24.00,24.00),(13,10013,24.00,24.00),(14,10014,24.00,24.00),(15,10015,24.00,24.00),(16,10016,24.00,24.00),(17,10017,24.00,24.00),(18,10018,24.00,24.00),(19,10019,24.00,24.00),(20,10020,24.00,24.00),(21,10021,24.00,24.00),(22,10022,24.00,24.00),(23,10023,24.00,24.00),(24,10024,24.00,24.00),(25,10025,24.00,24.00),(26,10026,24.00,24.00),(27,10027,24.00,24.00),(28,10028,24.00,24.00),(29,10029,24.00,24.00),(30,10030,24.00,24.00),(31,10031,24.00,24.00),(32,10032,24.00,24.00),(33,10033,24.00,24.00),(34,10034,24.00,24.00);
/*!40000 ALTER TABLE `leave_balances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_requests`
--

DROP TABLE IF EXISTS `leave_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_requests` (
  `leave_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `date_filed` date DEFAULT NULL,
  `leave_type` varchar(50) DEFAULT NULL,
  `from_date` date DEFAULT NULL,
  `to_date` date DEFAULT NULL,
  `number_of_days` decimal(5,2) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`leave_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `leave_requests_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_requests`
--

LOCK TABLES `leave_requests` WRITE;
/*!40000 ALTER TABLE `leave_requests` DISABLE KEYS */;
INSERT INTO `leave_requests` VALUES (1,10001,'2025-03-14','Vacation Leave','2025-03-15','2025-03-15',1.00,'First Leave','Approved'),(2,10002,'2025-03-14','Vacation Leave','2025-03-15','2025-03-15',1.00,'First Leave','Approved'),(3,10003,'2025-03-14','Vacation Leave','2025-03-15','2025-03-15',1.00,'First Leave','Approved'),(4,10004,'2025-03-14','Vacation Leave','2025-03-15','2025-03-15',1.00,'First Leave','Approved'),(5,10004,'2025-03-22','Vacation Leave','2025-03-23','2025-03-28',5.00,'Leave','Approved');
/*!40000 ALTER TABLE `leave_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_credentials`
--

DROP TABLE IF EXISTS `login_credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_credentials` (
  `employee_id` int NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`employee_id`),
  CONSTRAINT `login_credentials_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_credentials`
--

LOCK TABLES `login_credentials` WRITE;
/*!40000 ALTER TABLE `login_credentials` DISABLE KEYS */;
INSERT INTO `login_credentials` VALUES (10001,'Garcia Manuel III','10001','HUMAN RESOURCE'),(10002,'Lim Antonio','10002','PAYROLL STAFF'),(10003,'Aquino Bianca Sofia','10003','EMPLOYEE'),(10004,'Reyes Isabella','10004','SUPERVISOR'),(10005,'Eduard Hernandez','10005','EMPLOYEE'),(10006,'Villanueva Andrea Mae','10006','EMPLOYEE'),(10007,'San Jose Brad','10007','SUPERVISOR'),(10008,'Romualdez Fredrick','10008','EMPLOYEE'),(10009,'Atienza Cristina','10009','EMPLOYEE'),(10010,'Alvaro Roderick','10010','HUMAN RESOURCE'),(10011,'Salcedo Anthony','10011','SUPERVISOR'),(10012,'Lopez Christian','10012','EMPLOYEE'),(10013,'Mendoza Angelo','10013','EMPLOYEE'),(10014,'Santos Angelica','10014','PAYROLL STAFF'),(10015,'Rosario Cydney','10015','EMPLOYEE'),(10016,'Bautista Sandra','10016','SUPERVISOR'),(10017,'Villanueva Andrea Mae','10017','EMPLOYEE'),(10018,'Ramos Martha','10018','HUMAN RESOURCE'),(10019,'Gutierrez Rafael','10019','EMPLOYEE'),(10020,'Montero Rita','10020','PAYROLL STAFF'),(10021,'De Leon Leila','10021','EMPLOYEE'),(10022,'Fernandez Carl','10022','SUPERVISOR'),(10023,'Valdez Jennie','10023','EMPLOYEE'),(10024,'Cruz Rafael','10024','EMPLOYEE'),(10025,'Domingo Gloria','10025','HUMAN RESOURCE'),(10026,'Santiago Selena','10026','EMPLOYEE'),(10027,'Mata Christian','10027','PAYROLL STAFF'),(10028,'Mercado Emma','10028','SUPERVISOR'),(10029,'Navarro Joshua','10029','EMPLOYEE'),(10030,'Mendoza Emilio','10030','EMPLOYEE'),(10031,'Pineda Christine','10031','EMPLOYEE'),(10032,'Torres Jayson','10032','EMPLOYEE'),(10033,'Castro Aurora','10033','EMPLOYEE'),(10034,'Martinez Divina','10034','EMPLOYEE'),(10035,'John Paul Arquita','1234','EMPLOYEE');
/*!40000 ALTER TABLE `login_credentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `overtime_requests`
--

DROP TABLE IF EXISTS `overtime_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `overtime_requests` (
  `overtime_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `date_filed` date DEFAULT NULL,
  `type_request` varchar(50) DEFAULT NULL,
  `from_time` datetime DEFAULT NULL,
  `to_time` datetime DEFAULT NULL,
  `number_of_days` decimal(5,2) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`overtime_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `overtime_requests_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `overtime_requests`
--

LOCK TABLES `overtime_requests` WRITE;
/*!40000 ALTER TABLE `overtime_requests` DISABLE KEYS */;
INSERT INTO `overtime_requests` VALUES (1,10001,'2025-03-14','Overtime','2025-03-05 00:00:00','2025-03-06 00:00:00',2.00,'First Overtime','Approved'),(2,10002,'2025-03-14','Overtime','2025-03-05 00:00:00','2025-03-06 00:00:00',2.00,'First Overtime','Approved'),(3,10003,'2025-03-14','Overtime','2025-03-05 00:00:00','2025-03-06 00:00:00',2.00,'First Overtime','Approved'),(4,10004,'2025-03-14','Overtime','2025-03-05 00:00:00','2025-03-06 00:00:00',2.00,'First Overtime','Approved');
/*!40000 ALTER TABLE `overtime_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payroll`
--

DROP TABLE IF EXISTS `payroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payroll` (
  `payroll_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `payroll_period` varchar(50) DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `gross_income` decimal(12,2) DEFAULT NULL,
  `benefits` decimal(12,2) DEFAULT NULL,
  `overtime` decimal(10,2) DEFAULT NULL,
  `undertime` decimal(10,2) DEFAULT NULL,
  `sss` decimal(10,2) DEFAULT NULL,
  `philhealth` decimal(10,2) DEFAULT NULL,
  `pagibig` decimal(10,2) DEFAULT NULL,
  `tax` decimal(10,2) DEFAULT NULL,
  `net_pay` decimal(12,2) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`payroll_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `payroll_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payroll`
--

LOCK TABLES `payroll` WRITE;
/*!40000 ALTER TABLE `payroll` DISABLE KEYS */;
INSERT INTO `payroll` VALUES (1,10003,'03/01/2025 to 03/15/2025','Chief Finance Officer',34285.44,4500.00,0.00,0.00,1552.50,1800.00,1200.00,6922.90,27310.04,'Approved');
/*!40000 ALTER TABLE `payroll` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-19 15:36:39

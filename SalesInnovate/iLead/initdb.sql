-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: saleinnova
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `account_id` int NOT NULL AUTO_INCREMENT,
  `industry_id` int DEFAULT NULL,
  `account_name` varchar(50) NOT NULL,
  `account_type` varchar(50) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `description` varchar(150) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `website` varchar(50) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `postal_code` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `create_date` date NOT NULL,
  `edit_date` date NOT NULL,
  `edit_by` varchar(50) NOT NULL,
  `last_modified_by` varchar(50) NOT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  KEY `role_id` (`role_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
  CONSTRAINT `account_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actives`
--

DROP TABLE IF EXISTS `actives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actives` (
  `activity_id` int NOT NULL AUTO_INCREMENT,
  `activity_name` varchar(50) DEFAULT NULL,
  `activity_type` varchar(50) DEFAULT NULL,
  `activity_icon` text,
  PRIMARY KEY (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actives`
--

LOCK TABLES `actives` WRITE;
/*!40000 ALTER TABLE `actives` DISABLE KEYS */;
/*!40000 ALTER TABLE `actives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campaign`
--

DROP TABLE IF EXISTS `campaign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaign` (
  `campaign_id` int NOT NULL,
  `campaign_name` text,
  `status` int DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT NULL,
  `start_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`campaign_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaign`
--

LOCK TABLES `campaign` WRITE;
/*!40000 ALTER TABLE `campaign` DISABLE KEYS */;
/*!40000 ALTER TABLE `campaign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campaign_member`
--

DROP TABLE IF EXISTS `campaign_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaign_member` (
  `campaign_member_id` int NOT NULL,
  `campaign_id` int DEFAULT NULL,
  `lead_id` int DEFAULT NULL,
  `contact_id` int DEFAULT NULL,
  PRIMARY KEY (`campaign_member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaign_member`
--

LOCK TABLES `campaign_member` WRITE;
/*!40000 ALTER TABLE `campaign_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `campaign_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `contact_id` int NOT NULL AUTO_INCREMENT,
  `account_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `department` varchar(50) DEFAULT NULL,
  `mobile` varchar(10) DEFAULT NULL,
  `fax` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `create_date` date NOT NULL,
  `edit_date` date NOT NULL,
  `edit_by` varchar(50) NOT NULL,
  `last_modified_by` varchar(50) NOT NULL,
  `lead_id` int DEFAULT NULL,
  PRIMARY KEY (`contact_id`),
  KEY `account_id` (`account_id`),
  KEY `lead_id` (`lead_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `contacts_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`),
  CONSTRAINT `contacts_ibfk_2` FOREIGN KEY (`lead_id`) REFERENCES `leads` (`lead_id`),
  CONSTRAINT `contacts_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contract_contact_role`
--

DROP TABLE IF EXISTS `contract_contact_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_contact_role` (
  `Contract_contact_role_id` int NOT NULL AUTO_INCREMENT,
  `contact_id` int DEFAULT NULL,
  `role_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`Contract_contact_role_id`),
  KEY `contact_id` (`contact_id`),
  CONSTRAINT `contract_contact_role_ibfk_1` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`contact_id`),
  CONSTRAINT `contract_contact_role_ibfk_2` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contract_contact_role`
--

LOCK TABLES `contract_contact_role` WRITE;
/*!40000 ALTER TABLE `contract_contact_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `contract_contact_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_contact`
--

DROP TABLE IF EXISTS `event_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_contact` (
  `event_contact_id` int NOT NULL AUTO_INCREMENT,
  `subject` text,
  `description` text,
  `start_date` timestamp NULL DEFAULT NULL,
  `end_date` timestamp NULL DEFAULT NULL,
  `location` text,
  PRIMARY KEY (`event_contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_contact`
--

LOCK TABLES `event_contact` WRITE;
/*!40000 ALTER TABLE `event_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_contact_contact`
--

DROP TABLE IF EXISTS `event_contact_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_contact_contact` (
  `event_contact_contact_id` int NOT NULL AUTO_INCREMENT,
  `event_contact_id` int DEFAULT NULL,
  `contact_id` int DEFAULT NULL,
  PRIMARY KEY (`event_contact_contact_id`),
  KEY `event_contact_id` (`event_contact_id`),
  KEY `contact_id` (`contact_id`),
  CONSTRAINT `event_contact_contact_ibfk_1` FOREIGN KEY (`event_contact_id`) REFERENCES `event_contact` (`event_contact_id`),
  CONSTRAINT `event_contact_contact_ibfk_2` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_contact_contact`
--

LOCK TABLES `event_contact_contact` WRITE;
/*!40000 ALTER TABLE `event_contact_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_contact_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_lead`
--

DROP TABLE IF EXISTS `event_lead`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_lead` (
  `event_lead_id` int NOT NULL AUTO_INCREMENT,
  `lead_id` int DEFAULT NULL,
  `subject` text,
  `description` text,
  `start_date` timestamp NULL DEFAULT NULL,
  `end_date` timestamp NULL DEFAULT NULL,
  `location` text,
  PRIMARY KEY (`event_lead_id`),
  KEY `lead_id` (`lead_id`),
  CONSTRAINT `event_lead_ibfk_1` FOREIGN KEY (`lead_id`) REFERENCES `leads` (`lead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_lead`
--

LOCK TABLES `event_lead` WRITE;
/*!40000 ALTER TABLE `event_lead` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_lead` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `industry`
--

DROP TABLE IF EXISTS `industry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `industry` (
  `industry_id` int NOT NULL AUTO_INCREMENT,
  `industry_status_name` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`industry_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `industry_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `industry`
--

LOCK TABLES `industry` WRITE;
/*!40000 ALTER TABLE `industry` DISABLE KEYS */;
INSERT INTO `industry` VALUES (1,'Goverment',NULL),(2,'Electronics',NULL),(3,'Engineering',NULL),(4,'Entertainment',NULL);
/*!40000 ALTER TABLE `industry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_source`
--

DROP TABLE IF EXISTS `lead_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lead_source` (
  `lead_source_id` int NOT NULL AUTO_INCREMENT,
  `lead_ource_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lead_source_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_source`
--

LOCK TABLES `lead_source` WRITE;
/*!40000 ALTER TABLE `lead_source` DISABLE KEYS */;
INSERT INTO `lead_source` VALUES (1,'Advertisement'),(2,'Customer Event'),(3,'External Referral'),(4,'Other');
/*!40000 ALTER TABLE `lead_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lead_status`
--

DROP TABLE IF EXISTS `lead_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lead_status` (
  `lead_status_id` int NOT NULL AUTO_INCREMENT,
  `lead_status_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lead_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lead_status`
--

LOCK TABLES `lead_status` WRITE;
/*!40000 ALTER TABLE `lead_status` DISABLE KEYS */;
INSERT INTO `lead_status` VALUES (1,'Unqualified'),(2,'New'),(3,'Working'),(4,'Nurturing');
/*!40000 ALTER TABLE `lead_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leads`
--

DROP TABLE IF EXISTS `leads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leads` (
  `lead_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `account_id` int NOT NULL,
  `lead_source_id` int DEFAULT NULL,
  `industry_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  `task_lead_id` int DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `gender` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `edit_date` datetime(6) DEFAULT NULL,
  `edit_by` varchar(255) DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `rating` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lead_id`),
  KEY `task_lead_id` (`task_lead_id`),
  KEY `status_id` (`status_id`),
  KEY `lead_source_id` (`lead_source_id`),
  KEY `industry_id` (`industry_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `leads_ibfk_1` FOREIGN KEY (`task_lead_id`) REFERENCES `task_lead` (`task_lead_id`),
  CONSTRAINT `leads_ibfk_2` FOREIGN KEY (`status_id`) REFERENCES `lead_status` (`lead_status_id`),
  CONSTRAINT `leads_ibfk_3` FOREIGN KEY (`lead_source_id`) REFERENCES `lead_source` (`lead_source_id`),
  CONSTRAINT `leads_ibfk_4` FOREIGN KEY (`industry_id`) REFERENCES `industry` (`industry_id`),
  CONSTRAINT `leads_ibfk_5` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leads`
--

LOCK TABLES `leads` WRITE;
/*!40000 ALTER TABLE `leads` DISABLE KEYS */;
INSERT INTO `leads` VALUES (11,1001,2001,1,1,NULL,1,NULL,'John','Doe',1,'Manager','john.doe@example.com','1234567890','www.example.com','Example Company','123 Example St','Alice','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','Alice','Alice','Excellent'),(12,1002,2002,2,2,NULL,2,NULL,'Jane','Smith',2,'Director','jane.smith@example.com','9876543210','www.test.com','Test Company','456 Test Ave','Bob','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','Bob','Bob','Good'),(13,1003,2003,3,3,NULL,3,NULL,'Michael','Johnson',1,'CEO','michael.johnson@example.com','5555555555','www.company.com','Company XYZ','789 Company Rd','Charlie','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','Charlie','Charlie','Excellent'),(14,1004,2004,4,2,NULL,4,NULL,'Sarah','Williams',2,'CFO','sarah.williams@example.com','1112223333','www.enterprise.com','Enterprise Inc','101 Enterprise Blvd','David','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','David','David','Good'),(15,1005,2005,1,3,NULL,4,NULL,'Emily','Brown',2,'Manager','emily.brown@example.com','7778889999','www.pros.com','Pros Ltd','204 Pros Lane','Emma','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','Emma','Emma','Good'),(16,1006,2006,2,4,NULL,2,NULL,'Alex','Taylor',1,'Engineer','alex.taylor@example.com','444444444','www.techworld.com','Tech World','007 Tech Street','Fiona','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','Fiona','Fiona','Fair'),(17,1007,2007,3,1,NULL,1,NULL,'Chris','Martinez',1,'Analyst','chris.martinez@example.com','6666666666','www.analytics.net','Analytics Corp','123 Analytics Rd','Grace','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','Grace','Grace','Fair'),(18,1008,2008,4,2,NULL,3,NULL,'Liam','Evans',1,'Manager','liam.evans@example.com','1237774567','www.management.biz','Management Solutions','999 Management Ave','Hannah','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','Hannah','Hannah','Excellent'),(19,1009,2009,1,3,NULL,2,NULL,'Olivia','Garcia',2,'Consultant','olivia.garcia@example.com','9998887777','www.consulting.org','Consulting Experts','700 Consult Lane','Ivan','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','Ivan','Ivan','Good'),(20,1010,2010,2,4,NULL,2,NULL,'James','Rodriguez',1,'Sales Rep','james.rodriguez@example.com','3332221111','www.salesguild.com','Sales Guild','321 Sales Ave','Jack','2024-06-02 00:00:00.000000','2024-06-02 00:00:00.000000','Jack','Jack','Fair');
/*!40000 ALTER TABLE `leads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_call_contacts`
--

DROP TABLE IF EXISTS `log_call_contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_call_contacts` (
  `log_call_id` int NOT NULL AUTO_INCREMENT,
  `subject` varchar(100) DEFAULT NULL,
  `comment` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`log_call_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_call_contacts`
--

LOCK TABLES `log_call_contacts` WRITE;
/*!40000 ALTER TABLE `log_call_contacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_call_contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_call_contacts_contacts`
--

DROP TABLE IF EXISTS `log_call_contacts_contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_call_contacts_contacts` (
  `log_call_contacts_contacts_id` int NOT NULL AUTO_INCREMENT,
  `log_call_contacts_id` int DEFAULT NULL,
  `Contacts_id` int DEFAULT NULL,
  PRIMARY KEY (`log_call_contacts_contacts_id`),
  KEY `Contacts_id` (`Contacts_id`),
  CONSTRAINT `log_call_contacts_contacts_ibfk_1` FOREIGN KEY (`Contacts_id`) REFERENCES `contacts` (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_call_contacts_contacts`
--

LOCK TABLES `log_call_contacts_contacts` WRITE;
/*!40000 ALTER TABLE `log_call_contacts_contacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_call_contacts_contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_call_leads`
--

DROP TABLE IF EXISTS `log_call_leads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_call_leads` (
  `log_call_id` int NOT NULL AUTO_INCREMENT,
  `subject` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `lead_id` int DEFAULT NULL,
  PRIMARY KEY (`log_call_id`),
  KEY `lead_id` (`lead_id`),
  CONSTRAINT `log_call_leads_ibfk_1` FOREIGN KEY (`lead_id`) REFERENCES `leads` (`lead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_call_leads`
--

LOCK TABLES `log_call_leads` WRITE;
/*!40000 ALTER TABLE `log_call_leads` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_call_leads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_call_opportunity`
--

DROP TABLE IF EXISTS `log_call_opportunity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_call_opportunity` (
  `log_call_opportunity` int NOT NULL AUTO_INCREMENT,
  `subject` varchar(50) DEFAULT NULL,
  `comment` varchar(100) DEFAULT NULL,
  `opportunity` int DEFAULT NULL,
  PRIMARY KEY (`log_call_opportunity`),
  KEY `opportunity` (`opportunity`),
  CONSTRAINT `log_call_opportunity_ibfk_1` FOREIGN KEY (`opportunity`) REFERENCES `opportunities` (`opportunity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_call_opportunity`
--

LOCK TABLES `log_call_opportunity` WRITE;
/*!40000 ALTER TABLE `log_call_opportunity` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_call_opportunity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oder`
--

DROP TABLE IF EXISTS `oder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oder` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `quote_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `opportunity_id` int DEFAULT NULL,
  `quote_date` date DEFAULT NULL,
  `completion_date` date DEFAULT NULL,
  `unit_price` float DEFAULT NULL,
  `total_price` float DEFAULT NULL,
  `discount` float DEFAULT NULL,
  `tax_rate` float DEFAULT NULL,
  `tax_amount` float DEFAULT NULL,
  `note` varchar(50) DEFAULT NULL,
  `oder_status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `quote_id` (`quote_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `oder_ibfk_1` FOREIGN KEY (`quote_id`) REFERENCES `quote` (`quote_id`),
  CONSTRAINT `oder_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oder`
--

LOCK TABLES `oder` WRITE;
/*!40000 ALTER TABLE `oder` DISABLE KEYS */;
/*!40000 ALTER TABLE `oder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `opportunities`
--

DROP TABLE IF EXISTS `opportunities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `opportunities` (
  `opportunity_id` int NOT NULL AUTO_INCREMENT,
  `opportunity_name` varchar(50) DEFAULT NULL,
  `opportunity_type` varchar(20) DEFAULT NULL,
  `opportunity_source` varchar(50) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `probality` float DEFAULT NULL,
  `next_step` varchar(20) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `close_date` date DEFAULT NULL,
  `contract` varchar(50) DEFAULT NULL,
  `synced_quote` varchar(50) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `create_date` date NOT NULL,
  `edit_date` date NOT NULL,
  `edit_by` varchar(50) NOT NULL,
  `partner_id` int DEFAULT NULL,
  `opportunity_contact_role_id` int DEFAULT NULL,
  PRIMARY KEY (`opportunity_id`),
  KEY `opportunity_contact_role_id` (`opportunity_contact_role_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `opportunities_ibfk_1` FOREIGN KEY (`opportunity_contact_role_id`) REFERENCES `opportunity_contact_role` (`opportunity_contact_role_id`),
  CONSTRAINT `opportunities_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `opportunities`
--

LOCK TABLES `opportunities` WRITE;
/*!40000 ALTER TABLE `opportunities` DISABLE KEYS */;
/*!40000 ALTER TABLE `opportunities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `opportunity_contact`
--

DROP TABLE IF EXISTS `opportunity_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `opportunity_contact` (
  `opportunity_contact_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL,
  `contact_id` int DEFAULT NULL,
  `opportunity` int DEFAULT NULL,
  PRIMARY KEY (`opportunity_contact_id`),
  KEY `opportunity` (`opportunity`),
  CONSTRAINT `opportunity_contact_ibfk_1` FOREIGN KEY (`opportunity`) REFERENCES `opportunities` (`opportunity_id`),
  CONSTRAINT `opportunity_contact_ibfk_2` FOREIGN KEY (`opportunity`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `opportunity_contact`
--

LOCK TABLES `opportunity_contact` WRITE;
/*!40000 ALTER TABLE `opportunity_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `opportunity_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `opportunity_contact_role`
--

DROP TABLE IF EXISTS `opportunity_contact_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `opportunity_contact_role` (
  `opportunity_contact_role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`opportunity_contact_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `opportunity_contact_role`
--

LOCK TABLES `opportunity_contact_role` WRITE;
/*!40000 ALTER TABLE `opportunity_contact_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `opportunity_contact_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `opportunity_product`
--

DROP TABLE IF EXISTS `opportunity_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `opportunity_product` (
  `opportunity_product_id` int NOT NULL AUTO_INCREMENT,
  `opportunity_id` int DEFAULT NULL,
  `price_book_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `note` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`opportunity_product_id`),
  KEY `opportunity_id` (`opportunity_id`),
  KEY `price_book_id` (`price_book_id`),
  CONSTRAINT `opportunity_product_ibfk_1` FOREIGN KEY (`opportunity_id`) REFERENCES `opportunities` (`opportunity_id`),
  CONSTRAINT `opportunity_product_ibfk_2` FOREIGN KEY (`price_book_id`) REFERENCES `price_book` (`price_book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `opportunity_product`
--

LOCK TABLES `opportunity_product` WRITE;
/*!40000 ALTER TABLE `opportunity_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `opportunity_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partner`
--

DROP TABLE IF EXISTS `partner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partner` (
  `partner_id` int NOT NULL AUTO_INCREMENT,
  `account_id` int DEFAULT NULL,
  `opportunity_id` int DEFAULT NULL,
  `partner_role_id` int DEFAULT NULL,
  PRIMARY KEY (`partner_id`),
  KEY `partner_role_id` (`partner_role_id`),
  KEY `opportunity_id` (`opportunity_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `partner_ibfk_1` FOREIGN KEY (`partner_role_id`) REFERENCES `partner_role` (`partner_role_id`),
  CONSTRAINT `partner_ibfk_2` FOREIGN KEY (`opportunity_id`) REFERENCES `opportunities` (`opportunity_id`),
  CONSTRAINT `partner_ibfk_3` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partner`
--

LOCK TABLES `partner` WRITE;
/*!40000 ALTER TABLE `partner` DISABLE KEYS */;
/*!40000 ALTER TABLE `partner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partner_role`
--

DROP TABLE IF EXISTS `partner_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partner_role` (
  `partner_role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`partner_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partner_role`
--

LOCK TABLES `partner_role` WRITE;
/*!40000 ALTER TABLE `partner_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `partner_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `price_book`
--

DROP TABLE IF EXISTS `price_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `price_book` (
  `price_book_id` int NOT NULL AUTO_INCREMENT,
  `price_book_name` varchar(50) DEFAULT NULL,
  `description` varchar(150) DEFAULT NULL,
  `is_active` int DEFAULT NULL,
  `is_standard_price_book` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`price_book_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `price_book_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `price_book`
--

LOCK TABLES `price_book` WRITE;
/*!40000 ALTER TABLE `price_book` DISABLE KEYS */;
/*!40000 ALTER TABLE `price_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `contact_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `product_name` varchar(20) DEFAULT NULL,
  `product_code` varchar(50) DEFAULT NULL,
  `product_description` varchar(100) DEFAULT NULL,
  `is_active` int DEFAULT NULL,
  `product_family` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quote`
--

DROP TABLE IF EXISTS `quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quote` (
  `quote_id` int NOT NULL AUTO_INCREMENT,
  `account_id` int DEFAULT NULL,
  `opportunity_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `quote_date` date DEFAULT NULL,
  `experation_date` date DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `unit_price` float DEFAULT NULL,
  `total_price` float DEFAULT NULL,
  `discount` float DEFAULT NULL,
  `tax_rate` float DEFAULT NULL,
  `tax_amount` float DEFAULT NULL,
  `note` varchar(50) DEFAULT NULL,
  `quote_status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`quote_id`),
  KEY `account_id` (`account_id`),
  KEY `opportunity_id` (`opportunity_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `quote_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`),
  CONSTRAINT `quote_ibfk_2` FOREIGN KEY (`opportunity_id`) REFERENCES `opportunities` (`opportunity_id`),
  CONSTRAINT `quote_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quote`
--

LOCK TABLES `quote` WRITE;
/*!40000 ALTER TABLE `quote` DISABLE KEYS */;
/*!40000 ALTER TABLE `quote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_contact`
--

DROP TABLE IF EXISTS `task_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_contact` (
  `task_contact_id` int NOT NULL AUTO_INCREMENT,
  `task_type_id` int DEFAULT NULL,
  `subject` varchar(30) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  PRIMARY KEY (`task_contact_id`),
  KEY `task_type_id` (`task_type_id`),
  CONSTRAINT `task_contact_ibfk_1` FOREIGN KEY (`task_type_id`) REFERENCES `task_type` (`task_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_contact`
--

LOCK TABLES `task_contact` WRITE;
/*!40000 ALTER TABLE `task_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_contact_contact`
--

DROP TABLE IF EXISTS `task_contact_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_contact_contact` (
  `task_contact_contact_id` int NOT NULL AUTO_INCREMENT,
  `task_contact_id` int DEFAULT NULL,
  `contact_id` int DEFAULT NULL,
  PRIMARY KEY (`task_contact_contact_id`),
  KEY `task_contact_id` (`task_contact_id`),
  KEY `contact_id` (`contact_id`),
  CONSTRAINT `task_contact_contact_ibfk_1` FOREIGN KEY (`task_contact_id`) REFERENCES `task_contact` (`task_contact_id`),
  CONSTRAINT `task_contact_contact_ibfk_2` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_contact_contact`
--

LOCK TABLES `task_contact_contact` WRITE;
/*!40000 ALTER TABLE `task_contact_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_contact_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_lead`
--

DROP TABLE IF EXISTS `task_lead`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_lead` (
  `task_lead_id` int NOT NULL AUTO_INCREMENT,
  `lead_id` int DEFAULT NULL,
  `task_type_id` int DEFAULT NULL,
  `subject` text,
  `due_date` date DEFAULT NULL,
  PRIMARY KEY (`task_lead_id`),
  KEY `task_type_id` (`task_type_id`),
  KEY `lead_id` (`lead_id`),
  CONSTRAINT `task_lead_ibfk_1` FOREIGN KEY (`task_type_id`) REFERENCES `task_type` (`task_type_id`),
  CONSTRAINT `task_lead_ibfk_2` FOREIGN KEY (`lead_id`) REFERENCES `leads` (`lead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_lead`
--

LOCK TABLES `task_lead` WRITE;
/*!40000 ALTER TABLE `task_lead` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_lead` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_type`
--

DROP TABLE IF EXISTS `task_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_type` (
  `task_type_id` int NOT NULL AUTO_INCREMENT,
  `task_type_name` text,
  PRIMARY KEY (`task_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_type`
--

LOCK TABLES `task_type` WRITE;
/*!40000 ALTER TABLE `task_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_userroles`
--

DROP TABLE IF EXISTS `user_userroles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_userroles` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  `assigned_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `user_userroles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_userroles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `userroles` (`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_userroles`
--

LOCK TABLES `user_userroles` WRITE;
/*!40000 ALTER TABLE `user_userroles` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_userroles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userroles`
--

DROP TABLE IF EXISTS `userroles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userroles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  `description` text,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userroles`
--

LOCK TABLES `userroles` WRITE;
/*!40000 ALTER TABLE `userroles` DISABLE KEYS */;
/*!40000 ALTER TABLE `userroles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-02 11:36:03

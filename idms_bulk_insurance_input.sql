CREATE DATABASE  IF NOT EXISTS `idms` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `idms`;
-- MySQL dump 10.13  Distrib 8.0.40, for macos14 (x86_64)
--
-- Host: 127.0.0.1    Database: idms
-- ------------------------------------------------------
-- Server version	9.1.0

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
-- Table structure for table `bulk_insurance_input`
--

DROP TABLE IF EXISTS `bulk_insurance_input`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bulk_insurance_input` (
  `account_id` int NOT NULL AUTO_INCREMENT,
  `effective_date` date DEFAULT NULL,
  `expiration_date` date DEFAULT NULL,
  `insurance_provider` varchar(255) DEFAULT NULL,
  `policy_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bulk_insurance_input`
--

LOCK TABLES `bulk_insurance_input` WRITE;
/*!40000 ALTER TABLE `bulk_insurance_input` DISABLE KEYS */;
INSERT INTO `bulk_insurance_input` VALUES (1,'2025-01-01','2025-12-31','ABC Insurance','POL123456'),(2,'2025-02-01','2025-12-31','XYZ Insurance','POL654321'),(3,'2025-01-01','2025-12-31','ABC Insurance','POL123456'),(4,'2025-02-01','2025-12-31','XYZ Insurance','POL654321'),(5,'2025-01-01','2025-12-31','ABC Insurance','POL123456'),(6,'2025-02-01','2025-12-31','XYZ Insurance','POL654321'),(7,'2025-01-01','2025-12-31','ABC Insurance','POL123456'),(8,'2025-02-01','2025-12-31','XYZ Insurance','POL654321'),(9,'2025-01-01','2025-12-31','ABC Insurance','POL123456'),(10,'2025-02-01','2025-12-31','XYZ Insurance','POL654321'),(11,'2025-01-01','2025-12-31','ABC Insurance','POL123456'),(12,'2025-02-01','2025-12-31','XYZ Insurance','POL654321'),(13,'2025-01-01','2025-12-31','ABC Insurance','POL123456'),(14,'2025-02-01','2025-12-31','XYZ Insurance','POL654321'),(15,'2025-01-01','2025-12-31','ABC Insurance','POL123456'),(16,'2025-02-01','2025-12-31','XYZ Insurance','POL654321');
/*!40000 ALTER TABLE `bulk_insurance_input` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-03 23:42:45

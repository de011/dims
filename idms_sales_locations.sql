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
-- Table structure for table `sales_locations`
--

DROP TABLE IF EXISTS `sales_locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_locations` (
  `sales_location_id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`sales_location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_locations`
--

LOCK TABLES `sales_locations` WRITE;
/*!40000 ALTER TABLE `sales_locations` DISABLE KEYS */;
INSERT INTO `sales_locations` VALUES (1,'Los Angeles','Main Street Branch'),(2,'Los Angeles','Main Street Branch'),(3,'Los Angeles','Main Street Branch'),(4,'Los Angeles','Main Street Branch'),(5,'Los Angeles','Main Street Branch'),(6,'Los Angeles','Main Street Branch'),(7,'Los Angeles','Main Street Branch'),(8,'Los Angeles','Main Street Branch'),(9,'Los Angeles','Main Street Branch'),(10,'Los Angeles','Main Street Branch'),(11,'Los Angeles','Main Street Branch'),(12,'Los Angeles','Main Street Branch'),(13,'Los Angeles','Main Street Branch'),(14,'Los Angeles','Main Street Branch'),(15,'Los Angeles','Main Street Branch'),(16,'Los Angeles','Main Street Branch1');
/*!40000 ALTER TABLE `sales_locations` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-03 23:42:44

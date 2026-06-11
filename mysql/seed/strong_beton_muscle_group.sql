-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: strong_beton
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `muscle_group`
--

DROP TABLE IF EXISTS `muscle_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `muscle_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `muscle_group_name` varchar(45) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `muscle_group_name_UNIQUE` (`muscle_group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `muscle_group`
--

LOCK TABLES `muscle_group` WRITE;
/*!40000 ALTER TABLE `muscle_group` DISABLE KEYS */;
INSERT INTO `muscle_group` VALUES (136,'Chest','2026-06-07 10:13:27'),(137,'Back','2026-06-07 10:13:27'),(138,'Front Shoulder','2026-06-07 10:13:27'),(139,'Back Shoulder','2026-06-07 10:13:27'),(140,'Middle Shoulder','2026-06-07 10:13:27'),(141,'Biceps','2026-06-07 10:13:27'),(142,'Triceps','2026-06-07 10:13:27'),(143,'Quads','2026-06-07 10:13:27'),(144,'Hamstrings','2026-06-07 10:13:27'),(145,'Glutes','2026-06-07 10:13:27'),(146,'Calves','2026-06-07 10:13:27'),(147,'Core','2026-06-07 10:13:27'),(148,'Legs','2026-06-07 10:13:27'),(149,'Shoulders','2026-06-07 10:13:27'),(150,'Abs','2026-06-07 10:13:27');
/*!40000 ALTER TABLE `muscle_group` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-11 20:19:02

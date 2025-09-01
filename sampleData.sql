-- MySQL dump 10.13  Distrib 8.4.5, for Win64 (x86_64)
--
-- Host: localhost    Database: bond_catalog
-- ------------------------------------------------------
-- Server version	8.4.5

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `bond_catalog`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `bond_catalog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `bond_catalog`;

--
-- Table structure for table `asset`
--

DROP TABLE IF EXISTS `asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset` (
  `asset_type` varchar(31) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `currency` varchar(255) DEFAULT NULL,
  `issue_date` date DEFAULT NULL,
  `issuer` varchar(255) DEFAULT NULL,
  `maturity_date` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rating` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset`
--

LOCK TABLES `asset` WRITE;
/*!40000 ALTER TABLE `asset` DISABLE KEYS */;
INSERT INTO `asset` VALUES ('BOND',1,'USD','2023-01-01','US Government','2033-12-31','US Treasury Bond 10Y','AAA'),('BOND',2,'USD','2022-04-15','Apple Inc','2030-06-30','Corporate Bond - Apple Inc','AA+'),('BOND',3,'USD','2021-08-10','GreenFuture Ltd','2029-03-15','Green Energy Bond','A'),('BOND',4,'NGN','2020-11-01','Govt of Nigeria','2031-10-20','Emerging Market Bond','BB'),('BOND',5,'EUR','2019-03-25','Mercedes Benz AG','2028-09-01','Eurobond - Mercedes Benz','AA'),('BOND',7,'GBP','2021-06-01','UK Government','2026-06-01','UK Gilt 5Y','AAA'),('BOND',8,'USD','2022-09-15','Amazon Inc','2032-09-15','Corporate Bond - Amazon','AA'),('BOND',9,'USD','2021-01-01','State of California','2031-01-01','Municipal Bond - California','A+'),('BOND',10,'JPY','2020-02-01','Govt of Japan','2035-02-01','Japanese Government Bond 15Y','AAA'),('BOND',11,'USD','2021-05-10','Tesla Inc','2031-05-10','Corporate Bond - Tesla','BBB'),('BOND',12,'EUR','2022-03-20','Siemens AG','2029-03-20','Green Bond - Siemens','AA'),('BOND',13,'GHS','2021-07-01','Govt of Ghana','2031-07-01','Sovereign Bond - Ghana','B'),('BOND',14,'USD','2023-05-15','Alphabet Inc','2033-05-15','Corporate Bond - Google','AA+'),('BOND',15,'USD','2022-11-01','Microsoft Corp','2032-11-01','Corporate Bond - Microsoft','AAA'),('BOND',16,'USD','2020-08-01','NYC Government','2030-08-01','Municipal Bond - New York City','A'),('BOND',17,'EUR','2021-04-15','BMW AG','2028-04-15','Eurobond - BMW','A+'),('BOND',18,'USD','2022-06-10','Netflix Inc','2032-06-10','Corporate Bond - Netflix','BB+'),('BOND',19,'KES','2020-01-01','Govt of Kenya','2030-01-01','Sovereign Bond - Kenya','B'),('BOND',20,'EUR','2021-09-01','Nestle SA','2031-09-01','Eurobond - Nestle','AA'),('BOND',21,'USD','2022-02-15','Meta Platforms Inc','2032-02-15','Corporate Bond - Meta','A'),('BOND',22,'ZAR','2020-05-01','Govt of South Africa','2030-05-01','Sovereign Bond - South Africa','BB'),('BOND',23,'KRW','2021-10-01','Samsung Electronics','2031-10-01','Corporate Bond - Samsung','A+'),('BOND',24,'USD','2023-01-01','World Bank','2038-01-01','Infrastructure Bond','AAA'),('BOND',25,'USD','2022-12-15','CocaCola Inc','2032-12-15','Corporate Bond - CocaCola','A+'),('BOND',26,'GBP','2016-06-01','UK Government','2021-06-01','UK Gilt 5Y','AAA'),('BOND',27,'USD','2010-01-01','State of California','2020-01-01','Municipal Bond - California','A+'),('BOND',28,'EUR','2014-03-20','Siemens AG','2021-03-20','Green Bond - Siemens','AA'),('BOND',29,'USD','2011-11-01','Microsoft Corp','2021-11-01','Corporate Bond - Microsoft','AAA'),('BOND',30,'USD','2013-06-10','Netflix Inc','2020-06-10','Corporate Bond - Netflix','BB+'),('BOND',31,'EUR','2012-09-01','Nestle SA','2019-09-01','Eurobond - Nestle','AA'),('BOND',32,'USD','2014-12-15','CocaCola Inc','2022-12-15','Corporate Bond - CocaCola','A+'),('BOND',33,'GBP','2021-03-01','Unilever PLC','2029-03-01','Sustainability-Linked Bond - Unilever','A'),('BOND',34,'USD','2019-06-01','Govt of Nigeria','2027-06-01','Nigeria Eurobond 2027','CCC'),('BOND',35,'NGN','2022-01-01','Dangote Cement Plc','2029-12-31','Dangote Cement 2029','BBB'),('BOND',36,'NGN','2015-01-01','Lagos State Government','2020-12-31','Lagos State 2020 Series','BB'),('BOND',37,'USD','2021-09-01','Acme High Yield Ltd','2028-09-01','High Yield Corp 2028','CCC'),('BOND',38,'EUR','2019-05-01','Airbus SE','2024-05-01','Eurobond - Airbus 2019/2024','A');
/*!40000 ALTER TABLE `asset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bonds`
--

DROP TABLE IF EXISTS `bonds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bonds` (
  `coupon_rate` decimal(38,2) DEFAULT NULL,
  `face_value` decimal(38,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKe4qmhb4je109ffm2fbmo7u8tx` FOREIGN KEY (`id`) REFERENCES `asset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bonds`
--

LOCK TABLES `bonds` WRITE;
/*!40000 ALTER TABLE `bonds` DISABLE KEYS */;
INSERT INTO `bonds` VALUES (3.25,1000.00,'Active',1),(4.50,5000.00,'Active',2),(5.00,2000.00,'Active',3),(7.00,1000.00,'Active',4),(2.75,3000.00,'Active',5),(2.50,1000.00,'Active',7),(3.80,4000.00,'Active',8),(2.20,1500.00,'Active',9),(0.80,2000.00,'Active',10),(6.00,3500.00,'Active',11),(2.90,2500.00,'Active',12),(8.25,1000.00,'Active',13),(3.40,6000.00,'Active',14),(3.75,5500.00,'Active',15),(2.60,1200.00,'Active',16),(2.50,2800.00,'Active',17),(5.75,2200.00,'Active',18),(9.00,1000.00,'Active',19),(2.40,2700.00,'Active',20),(4.10,3300.00,'Active',21),(10.50,1000.00,'Active',22),(3.20,3100.00,'Active',23),(2.10,4000.00,'Active',24),(3.60,2600.00,'Active',25),(2.50,1000.00,'Matured',26),(2.20,1500.00,'Matured',27),(2.90,2500.00,'Matured',28),(3.75,5500.00,'Matured',29),(5.75,2200.00,'Matured',30),(2.40,2700.00,'Matured',31),(3.60,2600.00,'Matured',32),(2.95,2400.00,'Active',33),(8.75,1000.00,'Defaulted',34),(12.50,5000.00,'Active',35),(14.00,2000.00,'Matured',36),(11.00,1500.00,'Defaulted',37),(1.80,3000.00,'Matured',38);
/*!40000 ALTER TABLE `bonds` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-01 12:05:24

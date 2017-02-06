-- MySQL dump 10.13  Distrib 5.7.15, for Win32 (AMD64)
--
-- Host: localhost    Database: jjl
-- ------------------------------------------------------
-- Server version	5.7.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `jjlbillacceptor`
--

DROP TABLE IF EXISTS `jjlbillacceptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jjlbillacceptor` (
  `userid` int(10) unsigned NOT NULL,
  `acceptMoney` varchar(50) NOT NULL,
  `acceptTime` varchar(50) NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jjlbillacceptor`
--

LOCK TABLES `jjlbillacceptor` WRITE;
/*!40000 ALTER TABLE `jjlbillacceptor` DISABLE KEYS */;
/*!40000 ALTER TABLE `jjlbillacceptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jjlcustomer`
--

DROP TABLE IF EXISTS `jjlcustomer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jjlcustomer` (
  `userid` int(10) unsigned NOT NULL,
  `CustomerCategory` varchar(50) NOT NULL,
  `TicketQTY` varchar(50) DEFAULT NULL,
  `TicketPrice` varchar(50) DEFAULT NULL,
  `printNO` varchar(50) NOT NULL,
  `addTime` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jjlcustomer`
--

LOCK TABLES `jjlcustomer` WRITE;
/*!40000 ALTER TABLE `jjlcustomer` DISABLE KEYS */;
/*!40000 ALTER TABLE `jjlcustomer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jjlimage`
--

DROP TABLE IF EXISTS `jjlimage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jjlimage` (
  `userid` int(10) unsigned NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `path` varchar(100) NOT NULL,
  `addTime` varchar(50) NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jjlimage`
--

LOCK TABLES `jjlimage` WRITE;
/*!40000 ALTER TABLE `jjlimage` DISABLE KEYS */;
/*!40000 ALTER TABLE `jjlimage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jjlpayment`
--

DROP TABLE IF EXISTS `jjlpayment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jjlpayment` (
  `userid` int(10) unsigned NOT NULL,
  `payType` varchar(50) DEFAULT NULL,
  `payAmount` varchar(50) DEFAULT NULL,
  `addTime` varchar(50) NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jjlpayment`
--

LOCK TABLES `jjlpayment` WRITE;
/*!40000 ALTER TABLE `jjlpayment` DISABLE KEYS */;
/*!40000 ALTER TABLE `jjlpayment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jjlprinter`
--

DROP TABLE IF EXISTS `jjlprinter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jjlprinter` (
  `userid` int(10) unsigned NOT NULL,
  `printNO` varchar(50) DEFAULT NULL,
  `printBalance` varchar(50) DEFAULT NULL,
  `printTime` varchar(50) NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jjlprinter`
--

LOCK TABLES `jjlprinter` WRITE;
/*!40000 ALTER TABLE `jjlprinter` DISABLE KEYS */;
/*!40000 ALTER TABLE `jjlprinter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jjlsettings`
--

DROP TABLE IF EXISTS `jjlsettings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jjlsettings` (
  `userid` int(10) unsigned NOT NULL,
  `ChildPrice` varchar(50) DEFAULT NULL,
  `AdultPrice` varchar(50) DEFAULT NULL,
  `dicounts` varchar(50) DEFAULT NULL,
  `TimePeriod` varchar(50) DEFAULT NULL,
  `MediaType` varchar(50) DEFAULT NULL,
  `ShowText` varchar(100) DEFAULT NULL,
  `addTime` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jjlsettings`
--

LOCK TABLES `jjlsettings` WRITE;
/*!40000 ALTER TABLE `jjlsettings` DISABLE KEYS */;
/*!40000 ALTER TABLE `jjlsettings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jjluser`
--

DROP TABLE IF EXISTS `jjluser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jjluser` (
  `userid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `phoneNumber` varchar(50) DEFAULT NULL,
  `IDcard` varchar(50) DEFAULT NULL,
  `region` varchar(50) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `DeviceNO` varchar(50) DEFAULT NULL,
  `addTime` varchar(50) NOT NULL,
  `loginTime` varchar(50) NOT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jjluser`
--

LOCK TABLES `jjluser` WRITE;
/*!40000 ALTER TABLE `jjluser` DISABLE KEYS */;
INSERT INTO `jjluser` VALUES (3,'番禺店','aaa','15813334423','111111111111111111','番禺区','番禺区市桥','JJL0002','2017-01-08 19:44:14','2017-01-09 22:13:13'),(4,'市桥店','aaa','15813334423','111111111111111111','番禺区','番禺区市桥','JJL0002','2017-01-06 01:16:53','2017-01-06 01:16:53'),(5,'石基店','aaa','15813334423','111111111111111111','番禺区','番禺区市桥','JJL0002','2017-01-06 01:19:39','2017-01-06 01:19:39');
/*!40000 ALTER TABLE `jjluser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jjlvideo`
--

DROP TABLE IF EXISTS `jjlvideo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jjlvideo` (
  `userid` int(10) unsigned NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `path` varchar(100) DEFAULT NULL,
  `addTime` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jjlvideo`
--

LOCK TABLES `jjlvideo` WRITE;
/*!40000 ALTER TABLE `jjlvideo` DISABLE KEYS */;
INSERT INTO `jjlvideo` VALUES (3,'T1.mp4','Video/番禺店/','2017-01-09 21:55:43'),(5,'T1.mp4','Video/石基店/','2017-01-09 20:14:04');
/*!40000 ALTER TABLE `jjlvideo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-09 22:19:17

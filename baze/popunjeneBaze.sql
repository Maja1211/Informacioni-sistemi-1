-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem1
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korisnik` (
  `IdKorisnik` int NOT NULL AUTO_INCREMENT,
  `ime` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `godiste` int NOT NULL,
  `pol` char(1) NOT NULL,
  `IdMesta` int NOT NULL,
  PRIMARY KEY (`IdKorisnik`),
  KEY `IdMesta_idx` (`IdMesta`),
  CONSTRAINT `IdMesta` FOREIGN KEY (`IdMesta`) REFERENCES `mesto` (`IdMesto`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'Ana','ana@example.com',2000,'Z',1),(2,'Marko','marko@example.com',1998,'M',2),(3,'Jelena','jelena@example.com',1999,'Z',1),(4,'Petar','petar@example.com',1995,'M',3),(5,'Ivana','ivana@example.com',2001,'Z',4),(6,'Nikola','nikola@example.com',1997,'M',5),(7,'Sara','sara@example.com',2002,'Z',6),(8,'Vuk','vuk@example.com',1996,'M',7),(9,'Mina','mina@example.com',2003,'Z',8),(10,'Luka','luka@example.com',1994,'M',9),(11,'Teodora','teodora@example.com',2004,'Z',10),(12,'Stefan','stefan@example.com',1993,'M',11),(13,'Katarina','katarina@example.com',1999,'Z',12),(14,'Ognjen','ognjen@example.com',1992,'M',1),(15,'Milica','milica@example.com',2000,'Z',2);
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mesto`
--

DROP TABLE IF EXISTS `mesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mesto` (
  `IdMesto` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`IdMesto`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesto`
--

LOCK TABLES `mesto` WRITE;
/*!40000 ALTER TABLE `mesto` DISABLE KEYS */;
INSERT INTO `mesto` VALUES (1,'Beograd'),(2,'Novi Sad'),(3,'Niš'),(4,'Kragujevac'),(5,'Subotica'),(6,'Trebinje'),(7,'Zrenjanin'),(8,'Leskovac'),(9,'Pančevo'),(10,'Kraljevo'),(11,'Užice'),(12,'Sombor');
/*!40000 ALTER TABLE `mesto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-29 15:15:03
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem2
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `audiokategorija`
--

DROP TABLE IF EXISTS `audiokategorija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audiokategorija` (
  `IdKat` int NOT NULL,
  `IdAS` int NOT NULL,
  PRIMARY KEY (`IdKat`,`IdAS`),
  KEY `FK_audiokategorija_IdAS` (`IdAS`),
  CONSTRAINT `FK_audiokategorija_IdAS` FOREIGN KEY (`IdAS`) REFERENCES `audiosnimak` (`IdAS`),
  CONSTRAINT `FK_audiokategorija_IdKat` FOREIGN KEY (`IdKat`) REFERENCES `kategorija` (`IdKat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audiokategorija`
--

LOCK TABLES `audiokategorija` WRITE;
/*!40000 ALTER TABLE `audiokategorija` DISABLE KEYS */;
INSERT INTO `audiokategorija` VALUES (1,1),(4,1),(1,2),(2,2),(3,3),(10,3),(2,4),(8,5),(1,6),(7,6),(9,7),(1,8),(4,9),(10,9),(4,10),(10,10),(2,11),(5,11),(4,12),(12,12),(2,13),(6,13),(11,14),(8,15);
/*!40000 ALTER TABLE `audiokategorija` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audiosnimak`
--

DROP TABLE IF EXISTS `audiosnimak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audiosnimak` (
  `IdAS` int NOT NULL AUTO_INCREMENT,
  `IdKor` int NOT NULL,
  `naziv` varchar(45) NOT NULL,
  `trajanje` int NOT NULL,
  `datum` date NOT NULL,
  `vreme` time NOT NULL,
  PRIMARY KEY (`IdAS`),
  KEY `IdKor_idx` (`IdKor`),
  CONSTRAINT `IdKor` FOREIGN KEY (`IdKor`) REFERENCES `podsistem1`.`korisnik` (`IdKorisnik`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audiosnimak`
--

LOCK TABLES `audiosnimak` WRITE;
/*!40000 ALTER TABLE `audiosnimak` DISABLE KEYS */;
INSERT INTO `audiosnimak` VALUES (1,2,'Letnji dan',240,'2025-07-01','09:00:00'),(2,3,'Noćni voz',300,'2025-07-03','14:30:00'),(3,1,'Klasika u E molu',420,'2025-07-05','10:00:00'),(4,4,'Rock riff',180,'2025-07-10','16:00:00'),(5,5,'Podcast Epizoda 1',1800,'2025-07-12','12:00:00'),(6,6,'Elektronski val',360,'2025-07-15','21:45:00'),(7,7,'Audioknjiga: Poglavlje 1',2700,'2025-07-18','09:30:00'),(8,8,'Akustik set',420,'2025-07-20','18:10:00'),(9,9,'Jazz Session',600,'2025-07-22','19:00:00'),(10,10,'Ambient Dreams',480,'2025-07-25','23:15:00'),(11,11,'HipHop Beat',540,'2025-07-28','20:05:00'),(12,12,'Blues Night',660,'2025-08-01','17:40:00'),(13,13,'Metal Storm',900,'2025-08-05','13:25:00'),(14,14,'Folk Tales',240,'2025-08-12','08:00:00'),(15,15,'Podcast Epizoda 2',2100,'2025-08-18','11:00:00');
/*!40000 ALTER TABLE `audiosnimak` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kategorija`
--

DROP TABLE IF EXISTS `kategorija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kategorija` (
  `IdKat` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`IdKat`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kategorija`
--

LOCK TABLES `kategorija` WRITE;
/*!40000 ALTER TABLE `kategorija` DISABLE KEYS */;
INSERT INTO `kategorija` VALUES (1,'Pop'),(2,'Rock'),(3,'Klasika'),(4,'Jazz'),(5,'HipHop'),(6,'Metal'),(7,'Electronic'),(8,'Podcast'),(9,'Audiobook'),(10,'Ambient'),(11,'Folk'),(12,'Blues');
/*!40000 ALTER TABLE `kategorija` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-29 15:15:04
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem3
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `ocena`
--

DROP TABLE IF EXISTS `ocena`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ocena` (
  `IdOcena` int NOT NULL AUTO_INCREMENT,
  `broj` int NOT NULL,
  `datum` date NOT NULL,
  `vreme` time NOT NULL,
  `IdKor` int NOT NULL,
  `IdAS` int NOT NULL,
  PRIMARY KEY (`IdOcena`),
  KEY `IdAS_idx` (`IdAS`),
  KEY `IdKor_idx` (`IdKor`),
  CONSTRAINT `IdAS` FOREIGN KEY (`IdAS`) REFERENCES `podsistem2`.`audiosnimak` (`IdAS`) ON UPDATE CASCADE,
  CONSTRAINT `IdKor` FOREIGN KEY (`IdKor`) REFERENCES `podsistem1`.`korisnik` (`IdKorisnik`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ocena`
--

LOCK TABLES `ocena` WRITE;
/*!40000 ALTER TABLE `ocena` DISABLE KEYS */;
INSERT INTO `ocena` VALUES (1,5,'2025-07-04','11:00:00',1,2),(2,4,'2025-07-02','12:00:00',2,1),(3,5,'2025-07-05','12:05:00',3,3),(4,3,'2025-07-13','12:45:00',4,5),(5,4,'2025-07-16','22:30:00',5,6),(6,5,'2025-07-21','19:00:00',6,8),(7,2,'2025-07-10','18:00:00',7,4),(8,4,'2025-07-24','21:00:00',8,9),(9,5,'2025-07-27','00:00:00',9,10),(10,3,'2025-07-29','09:00:00',10,11),(11,4,'2025-08-02','10:00:00',11,12),(12,5,'2025-08-06','14:00:00',12,13),(13,5,'2025-08-18','13:00:00',13,15),(14,4,'2025-08-12','09:00:00',14,14),(15,5,'2025-07-19','12:00:00',15,7);
/*!40000 ALTER TABLE `ocena` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `omiljenepesme`
--

DROP TABLE IF EXISTS `omiljenepesme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `omiljenepesme` (
  `IdAudio` int NOT NULL,
  `IdKorisnik` int NOT NULL,
  PRIMARY KEY (`IdAudio`,`IdKorisnik`),
  KEY `IdKorisnik_idx` (`IdKorisnik`),
  CONSTRAINT `IdAudio` FOREIGN KEY (`IdAudio`) REFERENCES `podsistem2`.`audiosnimak` (`IdAS`) ON UPDATE CASCADE,
  CONSTRAINT `IdKorisnik` FOREIGN KEY (`IdKorisnik`) REFERENCES `podsistem1`.`korisnik` (`IdKorisnik`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `omiljenepesme`
--

LOCK TABLES `omiljenepesme` WRITE;
/*!40000 ALTER TABLE `omiljenepesme` DISABLE KEYS */;
INSERT INTO `omiljenepesme` VALUES (2,1),(5,1),(3,2),(8,2),(1,3),(6,5),(4,6),(9,8),(10,9),(11,10),(12,11),(13,12),(15,13),(14,14),(7,15);
/*!40000 ALTER TABLE `omiljenepesme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paket`
--

DROP TABLE IF EXISTS `paket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paket` (
  `IdPaket` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  `cena` double NOT NULL,
  PRIMARY KEY (`IdPaket`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paket`
--

LOCK TABLES `paket` WRITE;
/*!40000 ALTER TABLE `paket` DISABLE KEYS */;
INSERT INTO `paket` VALUES (1,'Free',0),(2,'Standard',599),(3,'Premium',999),(4,'Family',1499),(5,'Student',399);
/*!40000 ALTER TABLE `paket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pretplata`
--

DROP TABLE IF EXISTS `pretplata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pretplata` (
  `IdPretplata` int NOT NULL AUTO_INCREMENT,
  `IdKor` int NOT NULL,
  `IdPaket` int NOT NULL,
  `datumPoc` date NOT NULL,
  `vremePoc` time NOT NULL,
  `cena` double NOT NULL,
  PRIMARY KEY (`IdPretplata`),
  KEY `IdKor_idx` (`IdKor`),
  KEY `IdPaket_idx` (`IdPaket`),
  CONSTRAINT `IdKor_sk` FOREIGN KEY (`IdKor`) REFERENCES `podsistem1`.`korisnik` (`IdKorisnik`) ON UPDATE CASCADE,
  CONSTRAINT `IdPaket` FOREIGN KEY (`IdPaket`) REFERENCES `paket` (`IdPaket`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pretplata`
--

LOCK TABLES `pretplata` WRITE;
/*!40000 ALTER TABLE `pretplata` DISABLE KEYS */;
INSERT INTO `pretplata` VALUES (1,1,2,'2025-06-01','10:00:00',599),(2,1,2,'2025-07-02','10:00:00',599),(3,2,3,'2025-08-01','09:00:00',999),(4,3,1,'2025-07-15','12:00:00',0),(5,4,3,'2025-05-20','08:00:00',999),(6,4,4,'2025-06-21','08:30:00',1499),(7,5,5,'2025-08-10','14:00:00',399),(8,6,2,'2025-07-01','15:00:00',599),(9,7,3,'2025-06-15','11:00:00',999),(10,7,3,'2025-07-20','11:30:00',999),(11,9,2,'2025-08-20','10:00:00',599),(12,10,4,'2025-07-05','16:00:00',1499),(13,10,4,'2025-08-06','16:00:00',1499),(14,13,2,'2025-05-01','09:00:00',599),(15,13,2,'2025-06-02','09:00:00',599);
/*!40000 ALTER TABLE `pretplata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slusanje`
--

DROP TABLE IF EXISTS `slusanje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slusanje` (
  `IdSlusanje` int NOT NULL AUTO_INCREMENT,
  `IdKor` int NOT NULL,
  `IdAudioS` int NOT NULL,
  `DatumOd` date NOT NULL,
  `VremeOd` time NOT NULL,
  `SekundeOd` int NOT NULL,
  `SekundeUkupno` int NOT NULL,
  PRIMARY KEY (`IdSlusanje`),
  KEY `IdAudioS_idx` (`IdAudioS`),
  KEY `IdKor_idx` (`IdKor`),
  CONSTRAINT `IdAudioS` FOREIGN KEY (`IdAudioS`) REFERENCES `podsistem2`.`audiosnimak` (`IdAS`) ON UPDATE CASCADE,
  CONSTRAINT `IdKor_st` FOREIGN KEY (`IdKor`) REFERENCES `podsistem1`.`korisnik` (`IdKorisnik`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slusanje`
--

LOCK TABLES `slusanje` WRITE;
/*!40000 ALTER TABLE `slusanje` DISABLE KEYS */;
INSERT INTO `slusanje` VALUES (1,1,1,'2025-07-01','10:00:00',0,180),(2,2,5,'2025-07-13','12:30:00',600,900),(3,3,3,'2025-07-05','11:00:00',0,420),(4,4,4,'2025-07-10','17:10:00',20,160),(5,5,6,'2025-07-16','22:00:00',60,240),(6,6,7,'2025-07-18','10:00:00',120,1800),(7,7,2,'2025-07-04','09:00:00',10,250),(8,8,9,'2025-07-23','20:00:00',0,300),(9,9,8,'2025-07-21','18:30:00',0,300),(10,10,10,'2025-07-26','23:30:00',100,350),(11,11,11,'2025-07-28','20:10:00',0,540),(12,12,12,'2025-08-01','18:00:00',60,600),(13,13,13,'2025-08-06','13:30:00',100,700),(14,14,14,'2025-08-12','08:15:00',20,200),(15,15,15,'2025-08-18','12:00:00',200,1800);
/*!40000 ALTER TABLE `slusanje` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-29 15:15:04

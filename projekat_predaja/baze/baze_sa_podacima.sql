CREATE DATABASE  IF NOT EXISTS `podsistem3` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `podsistem3`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: podsistem3
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `gledanje`
--

DROP TABLE IF EXISTS `gledanje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gledanje` (
  `idGle` int NOT NULL AUTO_INCREMENT,
  `datumVremePocetka` datetime NOT NULL,
  `sekundPocetka` int NOT NULL,
  `trajanjeSekundi` int NOT NULL,
  `korisnik` int NOT NULL,
  `videosnimak` int NOT NULL,
  PRIMARY KEY (`idGle`),
  KEY `FK_gledanje_korisnik` (`korisnik`),
  KEY `FK_gledanje_videosnimak` (`videosnimak`),
  CONSTRAINT `FK_gledanje_korisnik` FOREIGN KEY (`korisnik`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE,
  CONSTRAINT `FK_gledanje_videosnimak` FOREIGN KEY (`videosnimak`) REFERENCES `videosnimak` (`idVid`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gledanje`
--

LOCK TABLES `gledanje` WRITE;
/*!40000 ALTER TABLE `gledanje` DISABLE KEYS */;
INSERT INTO `gledanje` VALUES (1,'2024-01-28 15:44:54',0,30,2,1),(2,'2024-01-28 15:45:04',10,100,1,2),(3,'2024-01-28 15:52:47',20,20,3,1);
/*!40000 ALTER TABLE `gledanje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korisnik` (
  `idKor` int NOT NULL,
  `email` varchar(255) NOT NULL,
  `godiste` int NOT NULL,
  `ime` varchar(255) NOT NULL,
  `pol` varchar(255) NOT NULL,
  PRIMARY KEY (`idKor`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'janko',2002,'janko','m'),(2,'stanko',2005,'stanko','m'),(3,'ivan',1974,'ivan','m');
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ocenjuje`
--

DROP TABLE IF EXISTS `ocenjuje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ocenjuje` (
  `datumVreme` datetime NOT NULL,
  `ocena` int NOT NULL,
  `idVid` int NOT NULL,
  `idKor` int NOT NULL,
  PRIMARY KEY (`idVid`,`idKor`),
  KEY `FK_ocenjuje_idKor` (`idKor`),
  CONSTRAINT `FK_ocenjuje_idKor` FOREIGN KEY (`idKor`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE,
  CONSTRAINT `FK_ocenjuje_idVid` FOREIGN KEY (`idVid`) REFERENCES `videosnimak` (`idVid`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ocenjuje`
--

LOCK TABLES `ocenjuje` WRITE;
/*!40000 ALTER TABLE `ocenjuje` DISABLE KEYS */;
INSERT INTO `ocenjuje` VALUES ('2024-01-28 15:46:04',5,1,1),('2024-01-28 15:46:12',3,1,2),('2024-01-28 15:46:39',1,2,1),('2024-01-28 15:54:21',5,2,3);
/*!40000 ALTER TABLE `ocenjuje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paket`
--

DROP TABLE IF EXISTS `paket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paket` (
  `idPak` int NOT NULL AUTO_INCREMENT,
  `cena` double NOT NULL,
  `naziv` varchar(255) NOT NULL,
  PRIMARY KEY (`idPak`),
  UNIQUE KEY `naziv_UNIQUE` (`naziv`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paket`
--

LOCK TABLES `paket` WRITE;
/*!40000 ALTER TABLE `paket` DISABLE KEYS */;
INSERT INTO `paket` VALUES (1,1000,'basic'),(2,2000,'premium');
/*!40000 ALTER TABLE `paket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pretplata`
--

DROP TABLE IF EXISTS `pretplata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pretplata` (
  `idPre` int NOT NULL AUTO_INCREMENT,
  `cenaPlacena` double NOT NULL,
  `datumVremePocetka` datetime NOT NULL,
  `korisnik` int NOT NULL,
  `paket` int NOT NULL,
  PRIMARY KEY (`idPre`),
  KEY `FK_pretplata_korisnik` (`korisnik`),
  KEY `FK_pretplata_paket` (`paket`),
  CONSTRAINT `FK_pretplata_korisnik` FOREIGN KEY (`korisnik`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE,
  CONSTRAINT `FK_pretplata_paket` FOREIGN KEY (`paket`) REFERENCES `paket` (`idPak`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pretplata`
--

LOCK TABLES `pretplata` WRITE;
/*!40000 ALTER TABLE `pretplata` DISABLE KEYS */;
INSERT INTO `pretplata` VALUES (1,1500,'2024-01-28 15:44:11',1,2),(2,2000,'2024-01-28 15:44:32',3,2);
/*!40000 ALTER TABLE `pretplata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videosnimak`
--

DROP TABLE IF EXISTS `videosnimak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videosnimak` (
  `idVid` int NOT NULL,
  `datumVremePostavljanja` datetime NOT NULL,
  `naziv` varchar(255) NOT NULL,
  `trajanje` int NOT NULL,
  `vlasnik` int NOT NULL,
  PRIMARY KEY (`idVid`),
  KEY `FK_videosnimak_vlasnik` (`vlasnik`),
  CONSTRAINT `FK_videosnimak_vlasnik` FOREIGN KEY (`vlasnik`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videosnimak`
--

LOCK TABLES `videosnimak` WRITE;
/*!40000 ALTER TABLE `videosnimak` DISABLE KEYS */;
INSERT INTO `videosnimak` VALUES (1,'2024-01-28 15:42:32','vid1',100,1),(2,'2024-01-28 15:42:45','vid2',120,2);
/*!40000 ALTER TABLE `videosnimak` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-28 15:56:35
CREATE DATABASE  IF NOT EXISTS `podsistem1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `podsistem1`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: podsistem1
-- ------------------------------------------------------
-- Server version	8.0.35

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
  `idKor` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `godiste` int NOT NULL,
  `ime` varchar(255) NOT NULL,
  `pol` varchar(255) NOT NULL,
  `mesto` int NOT NULL,
  PRIMARY KEY (`idKor`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `FK_korisnik_mesto` (`mesto`),
  CONSTRAINT `FK_korisnik_mesto` FOREIGN KEY (`mesto`) REFERENCES `mesto` (`idMes`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'janko',2002,'janko','m',1),(2,'stanko',2005,'stanko','m',1),(3,'ivan',1974,'ivan','m',2);
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mesto`
--

DROP TABLE IF EXISTS `mesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mesto` (
  `idMes` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(255) NOT NULL,
  PRIMARY KEY (`idMes`),
  UNIQUE KEY `naziv_UNIQUE` (`naziv`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesto`
--

LOCK TABLES `mesto` WRITE;
/*!40000 ALTER TABLE `mesto` DISABLE KEYS */;
INSERT INTO `mesto` VALUES (1,'beograd'),(2,'nis');
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

-- Dump completed on 2024-01-28 15:56:35
CREATE DATABASE  IF NOT EXISTS `podsistem2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `podsistem2`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: podsistem2
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `kategorija`
--

DROP TABLE IF EXISTS `kategorija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kategorija` (
  `idKat` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(255) NOT NULL,
  PRIMARY KEY (`idKat`),
  UNIQUE KEY `naziv_UNIQUE` (`naziv`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kategorija`
--

LOCK TABLES `kategorija` WRITE;
/*!40000 ALTER TABLE `kategorija` DISABLE KEYS */;
INSERT INTO `kategorija` VALUES (1,'drama'),(2,'komedija'),(3,'krimi');
/*!40000 ALTER TABLE `kategorija` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korisnik` (
  `idKor` int NOT NULL,
  `email` varchar(255) NOT NULL,
  `godiste` int NOT NULL,
  `ime` varchar(255) NOT NULL,
  `pol` varchar(255) NOT NULL,
  PRIMARY KEY (`idKor`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'janko',2002,'janko','m'),(2,'stanko',2005,'stanko','m'),(3,'ivan',1974,'ivan','m');
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pripada`
--

DROP TABLE IF EXISTS `pripada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pripada` (
  `idKat` int NOT NULL,
  `idVid` int NOT NULL,
  PRIMARY KEY (`idKat`,`idVid`),
  KEY `FK_pripada_idVid` (`idVid`),
  CONSTRAINT `FK_pripada_idKat` FOREIGN KEY (`idKat`) REFERENCES `kategorija` (`idKat`) ON UPDATE CASCADE,
  CONSTRAINT `FK_pripada_idVid` FOREIGN KEY (`idVid`) REFERENCES `videosnimak` (`idVid`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pripada`
--

LOCK TABLES `pripada` WRITE;
/*!40000 ALTER TABLE `pripada` DISABLE KEYS */;
INSERT INTO `pripada` VALUES (1,1),(3,1),(2,2);
/*!40000 ALTER TABLE `pripada` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videosnimak`
--

DROP TABLE IF EXISTS `videosnimak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videosnimak` (
  `idVid` int NOT NULL AUTO_INCREMENT,
  `datumVremePostavljanja` datetime NOT NULL,
  `naziv` varchar(255) NOT NULL,
  `trajanje` int NOT NULL,
  `vlasnik` int NOT NULL,
  PRIMARY KEY (`idVid`),
  KEY `FK_videosnimak_vlasnik` (`vlasnik`),
  CONSTRAINT `FK_videosnimak_vlasnik` FOREIGN KEY (`vlasnik`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videosnimak`
--

LOCK TABLES `videosnimak` WRITE;
/*!40000 ALTER TABLE `videosnimak` DISABLE KEYS */;
INSERT INTO `videosnimak` VALUES (1,'2024-01-28 15:42:32','vid1',100,1),(2,'2024-01-28 15:42:46','vid2',120,2);
/*!40000 ALTER TABLE `videosnimak` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-28 15:56:35

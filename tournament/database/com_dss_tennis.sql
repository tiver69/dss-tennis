CREATE DATABASE  IF NOT EXISTS `dss_tennis_tournament_tables` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `dss_tennis_tournament_tables`;
-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dss_tennis_tournament_tables
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `contest`
--

DROP TABLE IF EXISTS `contest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `contest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `round` tinyint(2) NOT NULL DEFAULT '0',
  `winner` tinyint(4) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `tournament_id` int(11) NOT NULL,
  `score_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`tournament_id`,`score_id`),
  KEY `fk_contest_tournament1_idx` (`tournament_id`),
  KEY `fk_contest_score1_idx` (`score_id`),
  CONSTRAINT `fk_contest_score1` FOREIGN KEY (`score_id`) REFERENCES `score` (`id`),
  CONSTRAINT `fk_contest_tournament1` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contest`
--

LOCK TABLES `contest` WRITE;
/*!40000 ALTER TABLE `contest` DISABLE KEYS */;
INSERT INTO `contest` VALUES (12,0,NULL,NULL,18,29),(13,0,NULL,NULL,18,30),(14,0,NULL,NULL,18,31),(15,0,NULL,NULL,18,32),(16,0,NULL,NULL,18,33),(17,0,NULL,NULL,18,34),(18,0,NULL,NULL,20,35),(19,0,NULL,NULL,20,36),(20,0,NULL,NULL,20,37),(21,0,NULL,NULL,20,38),(22,0,NULL,NULL,20,39),(23,0,NULL,NULL,20,40),(25,0,NULL,NULL,36,42),(26,0,NULL,NULL,36,43),(27,0,NULL,NULL,36,44),(28,0,NULL,NULL,36,45),(29,0,NULL,NULL,36,46),(30,0,NULL,NULL,36,47),(31,0,NULL,NULL,38,48),(32,0,NULL,NULL,38,49),(33,0,NULL,NULL,38,50),(34,0,NULL,NULL,38,51),(35,0,NULL,NULL,38,52),(36,0,NULL,NULL,38,53),(37,0,NULL,NULL,40,54),(38,0,NULL,NULL,40,55),(39,0,NULL,NULL,40,56),(40,0,NULL,NULL,40,57),(41,0,NULL,NULL,40,58),(42,0,NULL,NULL,40,59),(43,0,NULL,NULL,41,60),(44,0,NULL,NULL,41,61),(45,0,NULL,NULL,41,62),(46,0,NULL,NULL,41,63),(47,0,NULL,NULL,41,64),(48,0,NULL,NULL,41,65),(49,0,NULL,NULL,42,66),(50,0,NULL,NULL,42,67),(51,0,NULL,NULL,42,68),(52,0,NULL,NULL,42,69),(53,0,NULL,NULL,42,70),(54,0,NULL,NULL,42,71),(55,0,NULL,NULL,47,72),(56,0,NULL,NULL,47,73),(57,0,NULL,NULL,47,74),(58,0,NULL,NULL,48,75),(59,0,NULL,NULL,48,76),(60,0,NULL,NULL,48,77),(61,0,NULL,NULL,49,78),(62,0,NULL,NULL,49,79),(63,0,NULL,NULL,49,80),(64,0,NULL,NULL,50,81),(65,0,NULL,NULL,50,82),(66,0,NULL,NULL,50,83),(67,0,NULL,NULL,51,84),(68,0,NULL,NULL,51,85),(69,0,NULL,NULL,51,86);
/*!40000 ALTER TABLE `contest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `double_contest`
--

DROP TABLE IF EXISTS `double_contest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `double_contest` (
  `contest_id` int(11) NOT NULL,
  `team_one_id` int(11) NOT NULL,
  `team_two_id` int(11) NOT NULL,
  PRIMARY KEY (`contest_id`,`team_one_id`,`team_two_id`),
  KEY `fk_double_contest_team1_idx` (`team_one_id`),
  KEY `fk_double_contest_team2_idx` (`team_two_id`),
  KEY `fk_double_contest_contest1_idx` (`contest_id`),
  CONSTRAINT `fk_double_contest_contest1` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`id`),
  CONSTRAINT `fk_double_contest_team1` FOREIGN KEY (`team_one_id`) REFERENCES `team` (`id`),
  CONSTRAINT `fk_double_contest_team2` FOREIGN KEY (`team_two_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `double_contest`
--

LOCK TABLES `double_contest` WRITE;
/*!40000 ALTER TABLE `double_contest` DISABLE KEYS */;
/*!40000 ALTER TABLE `double_contest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `player` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `last_name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (13,'test1','test1'),(14,'test2','test2'),(15,'test3','test3'),(16,'test4','test4'),(20,'test','test2');
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `set_one_player_one` tinyint(2) DEFAULT NULL,
  `set_one_player_two` tinyint(2) DEFAULT NULL,
  `set_two_player_one` tinyint(2) DEFAULT NULL,
  `set_two_player_two` tinyint(2) DEFAULT NULL,
  `set_three_player_one` tinyint(2) DEFAULT NULL,
  `set_three_player_two` tinyint(2) DEFAULT NULL,
  `tie_break_player_one` tinyint(2) DEFAULT NULL,
  `tie_break_player_two` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
INSERT INTO `score` VALUES (29,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(30,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(32,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(33,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(34,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(35,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(36,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(37,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(38,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(39,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(40,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(42,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(43,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(44,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(45,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(46,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(47,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(48,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(49,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(50,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(51,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(52,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(53,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(54,0,0,NULL,NULL,NULL,NULL,NULL,NULL),(55,1,1,NULL,NULL,NULL,NULL,NULL,NULL),(56,2,2,NULL,NULL,NULL,NULL,NULL,NULL),(57,2,3,NULL,NULL,NULL,NULL,NULL,NULL),(58,4,5,NULL,NULL,NULL,NULL,NULL,NULL),(59,6,6,NULL,NULL,NULL,NULL,NULL,NULL),(60,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(61,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(62,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(63,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(64,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(65,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(66,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(67,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(68,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(69,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(70,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(71,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(72,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(73,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(74,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(75,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(76,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(77,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(78,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(79,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(80,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(81,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(82,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(83,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(84,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(85,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(86,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `single_contest`
--

DROP TABLE IF EXISTS `single_contest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `single_contest` (
  `contest_id` int(11) NOT NULL,
  `player_one_id` int(11) NOT NULL,
  `player_two_id1` int(11) NOT NULL,
  PRIMARY KEY (`contest_id`,`player_one_id`,`player_two_id1`),
  KEY `fk_single_contest_player1_idx` (`player_one_id`),
  KEY `fk_single_contest_player2_idx` (`player_two_id1`),
  KEY `fk_single_contest_contest1_idx` (`contest_id`),
  CONSTRAINT `fk_single_contest_contest1` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`id`),
  CONSTRAINT `fk_single_contest_player1` FOREIGN KEY (`player_one_id`) REFERENCES `player` (`id`),
  CONSTRAINT `fk_single_contest_player2` FOREIGN KEY (`player_two_id1`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `single_contest`
--

LOCK TABLES `single_contest` WRITE;
/*!40000 ALTER TABLE `single_contest` DISABLE KEYS */;
/*!40000 ALTER TABLE `single_contest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `team` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_one_id` int(11) NOT NULL,
  `player_two_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`player_one_id`,`player_two_id`),
  KEY `fk_team_player1_idx` (`player_one_id`),
  KEY `fk_team_player2_idx` (`player_two_id`),
  CONSTRAINT `fk_team_player1` FOREIGN KEY (`player_one_id`) REFERENCES `player` (`id`),
  CONSTRAINT `fk_team_player2` FOREIGN KEY (`player_two_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tournament`
--

DROP TABLE IF EXISTS `tournament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tournament` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `type` enum('ROUND','ELIMINATION') COLLATE utf8_unicode_ci NOT NULL,
  `status` enum('PLANNED','IN_PROGRESS','PLAYED') COLLATE utf8_unicode_ci NOT NULL DEFAULT 'PLANNED',
  `beginning_date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournament`
--

LOCK TABLES `tournament` WRITE;
/*!40000 ALTER TABLE `tournament` DISABLE KEYS */;
INSERT INTO `tournament` VALUES (1,'r3f','ELIMINATION','PLANNED','1999-03-17'),(15,'r3gg22','ELIMINATION','PLANNED','1999-03-17'),(16,'r3gg','ELIMINATION','PLANNED','1999-03-17'),(18,'r','ELIMINATION','PLANNED','1999-03-17'),(20,'rрасос','ELIMINATION','PLANNED','1999-03-17'),(36,'drntdyjrjtyr7r','ROUND','PLANNED','1999-03-17'),(38,'efjiefiier','ROUND','PLANNED','1999-03-17'),(39,'Tesetkjvernjvenveewfoi23','ROUND','PLANNED','1999-03-17'),(40,'Teseewfoi23','ROUND','PLANNED','1999-03-17'),(41,'Teseewf75goi23','ROUND','PLANNED','1999-03-17'),(42,'Teseewf75guygkuyvkuvkuyvyuviytxoi23','ROUND','PLANNED','1999-03-17'),(47,'Teseewf75uviytxoi23','ROUND','PLANNED','1999-03-17'),(48,'Tesee3','ROUND','PLANNED','1999-03-17'),(49,'T2342t443esee3','ROUND','PLANNED','1999-03-17'),(50,'T3esee3','ROUND','IN_PROGRESS','2021-10-20'),(51,'T3esирманпгиee3','ROUND','PLANNED','2021-10-26');
/*!40000 ALTER TABLE `tournament` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-01-26  0:33:28

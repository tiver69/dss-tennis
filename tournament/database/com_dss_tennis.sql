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
  `winner_id` int(11) DEFAULT NULL,
  `tech_defeat` tinyint(4) NOT NULL DEFAULT '0',
  `date` date DEFAULT NULL,
  `tournament_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`tournament_id`),
  KEY `fk_contest_tournament1_idx` (`tournament_id`),
  CONSTRAINT `fk_contest_tournament1` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contest`
--

LOCK TABLES `contest` WRITE;
/*!40000 ALTER TABLE `contest` DISABLE KEYS */;
INSERT INTO `contest` VALUES (12,0,NULL,0,NULL,18),(13,0,NULL,0,NULL,18),(14,0,NULL,0,NULL,18),(15,0,NULL,0,NULL,18),(16,0,NULL,0,NULL,18),(17,0,NULL,0,NULL,18),(18,0,NULL,0,NULL,20),(19,0,NULL,0,NULL,20),(20,0,NULL,0,NULL,20),(21,0,NULL,0,NULL,20),(22,0,NULL,0,NULL,20),(23,0,NULL,0,NULL,20),(25,0,NULL,0,NULL,36),(26,0,NULL,0,NULL,36),(27,0,NULL,0,NULL,36),(28,0,NULL,0,NULL,36),(29,0,NULL,0,NULL,36),(30,0,NULL,0,NULL,36),(31,0,NULL,0,NULL,38),(32,0,NULL,0,NULL,38),(33,0,NULL,0,NULL,38),(34,0,NULL,0,NULL,38),(35,0,NULL,0,NULL,38),(36,0,NULL,0,NULL,38),(37,0,NULL,0,NULL,40),(38,0,NULL,0,NULL,40),(39,0,NULL,0,NULL,40),(40,0,NULL,0,NULL,40),(41,0,NULL,0,NULL,40),(42,0,NULL,0,NULL,40),(43,0,NULL,0,NULL,41),(44,0,NULL,0,NULL,41),(45,0,NULL,0,NULL,41),(46,0,NULL,0,NULL,41),(47,0,NULL,0,NULL,41),(48,0,NULL,0,NULL,41),(49,0,NULL,0,NULL,42),(50,0,NULL,0,NULL,42),(51,0,NULL,0,NULL,42),(52,0,NULL,0,NULL,42),(53,0,NULL,0,NULL,42),(54,0,NULL,0,NULL,42),(55,0,NULL,0,NULL,47),(56,0,NULL,0,NULL,47),(57,0,NULL,0,NULL,47),(58,0,NULL,0,NULL,48),(59,0,NULL,0,NULL,48),(60,0,NULL,0,NULL,48),(61,0,NULL,0,NULL,49),(62,0,NULL,0,NULL,49),(63,0,NULL,0,NULL,49),(64,0,NULL,0,NULL,50),(65,0,NULL,0,NULL,50),(66,0,NULL,0,NULL,50),(67,0,NULL,0,NULL,51),(68,0,NULL,0,NULL,51),(69,0,NULL,0,NULL,51),(70,0,NULL,0,NULL,52),(75,0,NULL,0,NULL,57),(76,0,NULL,0,NULL,57),(77,0,NULL,0,NULL,57),(78,0,NULL,0,NULL,57),(79,0,NULL,0,NULL,57),(80,0,NULL,0,NULL,57),(81,0,NULL,0,NULL,58),(82,0,NULL,0,NULL,58),(83,0,NULL,0,NULL,58),(84,0,NULL,0,NULL,58),(85,0,NULL,0,NULL,58),(86,0,NULL,0,NULL,58),(87,0,NULL,0,NULL,59),(88,0,NULL,0,NULL,59),(89,0,NULL,0,NULL,59),(90,0,NULL,0,NULL,59),(91,0,NULL,0,NULL,59),(92,0,NULL,0,NULL,59),(93,0,NULL,0,NULL,60),(94,0,NULL,0,NULL,60),(95,0,NULL,0,NULL,60),(96,0,NULL,0,NULL,60),(97,0,NULL,0,NULL,60),(98,0,NULL,0,NULL,60),(99,0,NULL,0,NULL,61),(100,0,NULL,0,NULL,61),(101,0,NULL,0,NULL,61),(102,0,NULL,0,NULL,61),(103,0,NULL,0,NULL,61),(104,0,NULL,0,NULL,61),(105,0,NULL,0,NULL,62),(106,0,NULL,0,NULL,62),(107,0,NULL,0,NULL,62),(108,0,NULL,0,NULL,62),(109,0,NULL,0,NULL,62),(110,0,NULL,0,NULL,62),(111,0,NULL,0,NULL,57),(112,0,NULL,0,NULL,57),(113,0,NULL,0,NULL,57),(114,0,NULL,0,NULL,57),(115,0,NULL,0,NULL,68),(116,0,NULL,0,NULL,68),(117,0,NULL,0,NULL,68),(118,0,NULL,0,NULL,69),(119,0,NULL,0,NULL,69),(120,0,NULL,0,NULL,69),(121,0,NULL,0,NULL,70),(122,0,NULL,0,NULL,70),(123,0,NULL,0,NULL,70),(124,0,NULL,0,NULL,71),(125,0,NULL,0,NULL,71),(126,0,NULL,0,NULL,71),(127,0,NULL,0,NULL,72),(128,0,NULL,0,NULL,72),(129,0,NULL,0,NULL,72),(130,0,NULL,0,NULL,73),(131,0,NULL,0,NULL,73),(132,0,NULL,0,NULL,73),(133,0,NULL,0,NULL,74),(134,0,NULL,0,NULL,74),(135,0,NULL,0,NULL,74),(136,0,NULL,0,NULL,75),(137,0,NULL,0,NULL,75),(138,0,NULL,0,NULL,75),(139,0,NULL,0,NULL,76),(140,0,NULL,0,NULL,76),(141,0,NULL,0,NULL,76),(142,0,NULL,0,NULL,77),(143,0,NULL,0,NULL,77),(144,0,NULL,0,NULL,77),(145,0,NULL,0,NULL,78),(146,0,NULL,0,NULL,78),(147,0,NULL,0,NULL,78),(148,0,NULL,0,NULL,79),(149,0,NULL,0,NULL,79),(150,0,NULL,0,NULL,79),(151,0,NULL,0,NULL,80),(152,0,NULL,0,NULL,80),(153,0,NULL,0,NULL,80),(154,0,NULL,0,NULL,81),(155,0,NULL,0,NULL,81),(156,0,NULL,0,NULL,81),(157,0,NULL,0,NULL,82),(158,0,NULL,0,NULL,82),(159,0,NULL,0,NULL,82),(160,0,NULL,0,NULL,82),(161,0,NULL,0,NULL,82),(162,0,NULL,0,NULL,82),(163,0,NULL,0,NULL,83),(164,0,NULL,0,NULL,83),(165,0,NULL,0,NULL,83),(166,0,NULL,0,NULL,84),(167,0,NULL,0,NULL,84),(168,0,NULL,0,NULL,84),(169,0,NULL,0,NULL,85),(170,0,NULL,0,NULL,85),(171,0,NULL,0,NULL,85),(172,0,NULL,0,NULL,86),(173,0,NULL,0,NULL,86),(174,0,NULL,0,NULL,86),(175,0,NULL,0,NULL,87),(176,0,NULL,0,NULL,87),(177,0,NULL,0,NULL,87),(178,0,NULL,0,NULL,87),(179,0,NULL,0,NULL,87),(180,0,NULL,0,NULL,87),(181,0,NULL,0,NULL,87),(182,0,NULL,0,NULL,87),(183,0,NULL,0,NULL,87),(184,0,NULL,0,NULL,87),(185,0,NULL,0,NULL,87),(186,0,NULL,0,NULL,87),(187,0,NULL,0,NULL,88),(188,0,NULL,0,NULL,88),(189,0,NULL,0,NULL,88),(190,0,NULL,0,NULL,89),(191,0,NULL,0,NULL,91),(192,0,NULL,0,NULL,91),(193,0,NULL,0,NULL,91),(194,0,NULL,0,NULL,95),(195,0,2,0,NULL,96),(196,0,4,0,NULL,97),(197,0,2,0,NULL,98),(198,0,NULL,0,NULL,99),(199,0,NULL,0,NULL,99),(200,0,NULL,0,NULL,99),(201,0,21,0,NULL,100),(202,0,15,1,NULL,100),(203,0,15,1,NULL,100),(204,0,14,1,NULL,101),(205,0,15,1,NULL,101),(206,0,14,1,NULL,101),(207,0,14,0,NULL,102),(208,0,NULL,0,NULL,102),(209,0,NULL,0,NULL,102);
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
  UNIQUE KEY `contest_id_UNIQUE` (`contest_id`),
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
INSERT INTO `double_contest` VALUES (70,1,2),(115,2,1),(116,3,1),(117,3,2),(118,2,1),(119,3,1),(120,3,2),(121,2,1),(122,3,1),(123,3,2),(124,2,1),(125,3,1),(126,3,2),(127,2,1),(128,3,1),(129,3,2),(130,2,1),(131,3,1),(132,3,2),(133,2,1),(134,3,1),(135,3,2),(136,2,1),(137,3,1),(138,3,2),(139,2,1),(140,3,1),(141,3,2),(142,2,1),(143,3,1),(144,3,2),(145,2,1),(146,3,1),(147,3,2),(148,2,1),(149,3,1),(150,3,2),(151,2,1),(152,3,1),(153,3,2),(154,2,1),(155,3,1),(156,3,2),(157,2,1),(158,3,1),(159,3,2),(160,2,1),(161,3,1),(162,3,2),(163,2,1),(164,3,1),(165,3,2),(166,2,1),(167,3,1),(168,3,2),(169,2,1),(170,3,1),(171,3,2),(172,2,1),(173,3,1),(174,3,2),(175,2,1),(176,3,1),(177,3,2),(178,4,1),(179,4,2),(180,4,3),(181,3,2),(182,4,2),(183,4,3),(184,3,2),(185,4,2),(186,4,3),(187,3,2),(188,4,2),(189,4,3),(190,4,2),(194,4,2),(195,4,2),(196,4,2),(197,4,2);
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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (13,'test1','test1'),(14,'test2','test2'),(15,'test3','test3'),(16,'test4','test4'),(20,'test','test2'),(21,'Test','Test');
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `set_score`
--

DROP TABLE IF EXISTS `set_score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `set_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `set_type` enum('SET_ONE','SET_TWO','SET_THREE','TIE_BREAK') NOT NULL,
  `participant_one` tinyint(4) DEFAULT NULL,
  `participant_two` tinyint(4) DEFAULT NULL,
  `contest_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`contest_id`),
  KEY `fk_set_score_contest1_idx` (`contest_id`),
  CONSTRAINT `fk_set_score_contest1` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=173 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `set_score`
--

LOCK TABLES `set_score` WRITE;
/*!40000 ALTER TABLE `set_score` DISABLE KEYS */;
INSERT INTO `set_score` VALUES (106,'SET_TWO',6,7,195),(107,'SET_ONE',7,6,195),(108,'SET_THREE',6,7,195),(109,'TIE_BREAK',6,7,195),(132,'SET_ONE',7,5,196),(133,'SET_TWO',6,4,196),(134,'TIE_BREAK',6,2,196),(135,'SET_THREE',6,3,196),(136,'SET_ONE',6,1,197),(139,'TIE_BREAK',6,7,197),(146,'SET_TWO',6,4,197),(147,'SET_THREE',6,3,197),(148,'SET_TWO',6,7,198),(149,'SET_ONE',6,6,198),(150,'SET_TWO',6,7,201),(151,'SET_ONE',6,7,201),(152,'SET_ONE',6,7,202),(153,'SET_TWO',6,7,202),(154,'SET_ONE',6,7,203),(155,'SET_TWO',6,7,203),(156,'SET_ONE',6,7,204),(157,'SET_TWO',6,7,204),(158,'SET_ONE',6,7,205),(159,'SET_THREE',6,3,205),(160,'SET_TWO',6,7,205),(161,'SET_ONE',6,7,206),(162,'SET_THREE',6,3,206),(163,'SET_TWO',6,7,206),(168,'SET_ONE',6,2,207),(169,'TIE_BREAK',7,5,207),(172,'SET_TWO',6,2,207);
/*!40000 ALTER TABLE `set_score` ENABLE KEYS */;
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
  `player_two_id` int(11) NOT NULL,
  PRIMARY KEY (`contest_id`,`player_one_id`,`player_two_id`),
  UNIQUE KEY `contest_id_UNIQUE` (`contest_id`),
  KEY `fk_single_contest_player1_idx` (`player_one_id`),
  KEY `fk_single_contest_player2_idx` (`player_two_id`),
  KEY `fk_single_contest_contest1_idx` (`contest_id`),
  CONSTRAINT `fk_single_contest_contest1` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`id`),
  CONSTRAINT `fk_single_contest_player1` FOREIGN KEY (`player_one_id`) REFERENCES `player` (`id`),
  CONSTRAINT `fk_single_contest_player2` FOREIGN KEY (`player_two_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `single_contest`
--

LOCK TABLES `single_contest` WRITE;
/*!40000 ALTER TABLE `single_contest` DISABLE KEYS */;
INSERT INTO `single_contest` VALUES (75,13,20),(76,13,15),(77,13,16),(78,20,15),(79,20,16),(80,15,16),(81,13,20),(82,13,15),(83,13,16),(84,20,15),(85,20,16),(86,15,16),(87,13,20),(88,13,15),(89,13,16),(90,20,15),(91,20,16),(92,15,16),(93,13,20),(94,13,15),(95,13,16),(96,20,15),(97,20,16),(98,15,16),(99,13,20),(100,13,15),(101,13,16),(102,20,15),(103,20,16),(104,15,16),(105,13,20),(106,13,15),(107,13,16),(108,20,15),(109,20,16),(110,15,16),(111,21,13),(112,21,20),(113,21,15),(114,21,16),(191,14,21),(192,15,21),(193,15,14),(198,14,21),(199,15,21),(200,15,14),(201,14,21),(202,15,21),(203,15,14),(204,14,21),(205,15,21),(206,15,14),(207,14,21),(208,15,21),(209,15,14);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1,13,14),(4,13,21),(2,15,16),(3,20,21);
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tie_break`
--

DROP TABLE IF EXISTS `tie_break`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tie_break` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `participant_one` tinyint(4) DEFAULT NULL,
  `participant_two` tinyint(4) DEFAULT NULL,
  `set_score_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`set_score_id`),
  UNIQUE KEY `set_score_id_UNIQUE` (`set_score_id`),
  KEY `fk_tie_break_set_score1_idx` (`set_score_id`),
  CONSTRAINT `fk_tie_break_set_score1` FOREIGN KEY (`set_score_id`) REFERENCES `set_score` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tie_break`
--

LOCK TABLES `tie_break` WRITE;
/*!40000 ALTER TABLE `tie_break` DISABLE KEYS */;
/*!40000 ALTER TABLE `tie_break` ENABLE KEYS */;
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
  `tournament_type` enum('ROUND','ELIMINATION') COLLATE utf8_unicode_ci NOT NULL,
  `participant_type` enum('SINGLE','DOUBLE') COLLATE utf8_unicode_ci NOT NULL,
  `status` enum('PLANNED','IN_PROGRESS','PLAYED') COLLATE utf8_unicode_ci NOT NULL DEFAULT 'PLANNED',
  `beginning_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournament`
--

LOCK TABLES `tournament` WRITE;
/*!40000 ALTER TABLE `tournament` DISABLE KEYS */;
INSERT INTO `tournament` VALUES (1,'r3f','ELIMINATION','SINGLE','PLANNED','1999-03-17'),(15,'r3gg22','ELIMINATION','SINGLE','PLANNED','1999-03-17'),(16,'r3gg','ELIMINATION','SINGLE','PLANNED','1999-03-17'),(18,'r','ELIMINATION','SINGLE','PLANNED','1999-03-17'),(20,'rрасос','ELIMINATION','SINGLE','PLANNED','1999-03-17'),(36,'drntdyjrjtyr7r','ROUND','SINGLE','PLANNED','1999-03-17'),(38,'efjiefiier','ROUND','SINGLE','PLANNED','1999-03-17'),(39,'Tesetkjvernjvenveewfoi23','ROUND','SINGLE','PLANNED','1999-03-17'),(40,'Teseewfoi23','ROUND','SINGLE','PLANNED','1999-03-17'),(41,'Teseewf75goi23','ROUND','SINGLE','PLANNED','1999-03-17'),(42,'Teseewf75guygkuyvkuvkuyvyuviytxoi23','ROUND','SINGLE','PLANNED','1999-03-17'),(47,'Teseewf75uviytxoi23','ROUND','SINGLE','PLANNED','1999-03-17'),(48,'Tesee3','ROUND','SINGLE','PLANNED','1999-03-17'),(49,'T2342t443esee3','ROUND','SINGLE','PLANNED','1999-03-17'),(50,'T3esee3','ROUND','SINGLE','IN_PROGRESS','2021-10-20'),(51,'T3esирманпгиee3','ROUND','SINGLE','PLANNED','2021-10-26'),(52,'Double Test','ROUND','DOUBLE','PLANNED','2021-10-26'),(57,'dygvrntdyjrjtyr7r','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(58,'d6ghrjtyr7r','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(59,'d6ghr876rtfgjtyr7r','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(60,'dktcutcutctucktfgjtyr7r','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(61,'dktcunkjtyr7r','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(62,'eoirmcro;c','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(63,'eoirdwdwdwdwmcro;c','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(64,'eoirdwdwdwmcro;c','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(65,'e','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(66,'eукрое','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(67,'ejhcjhc','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(68,'testfirstdoubletournament','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(69,'tement','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(70,'treherement','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(71,'treeberment','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(72,'bwnt','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(73,'werwerwev','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(74,'qads rwev','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(75,'qadsjvjojnrwev','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(76,'qad4teh4ertgv','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(77,'eggw45g','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(78,'wwedeggw45g','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(79,'ww2w645g','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(80,'2323ggrefefwfwe','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(81,'2укмумукмfwfwe','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(82,'2укмумукeweewмfwfwe','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(83,'2уgergetgeeewмfwfwe','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(84,'2уgergetgeeewrrмfwfwe','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(85,'2уgerrмfwfwe','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(86,'2werwerfwe','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(87,'2we,uui,','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(88,'2we,uuiуащд9,','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(89,'2wejbihbi','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(90,'2ewrwwerewrewhbi','ROUND','DOUBLE','IN_PROGRESS','2021-10-26'),(91,'2ewqwcsaewhbi','ROUND','SINGLE','IN_PROGRESS','2021-10-26'),(92,'eoitrudtyckugvko;c','ROUND','DOUBLE','PLANNED',NULL),(93,'eoitrudtyckugvko;weewqc','ROUND','DOUBLE','PLANNED',NULL),(94,'name-1647781448','ROUND','DOUBLE','PLANNED',NULL),(95,'name-1647781558','ROUND','DOUBLE','PLANNED',NULL),(96,'name-1647801820','ROUND','DOUBLE','PLANNED',NULL),(97,'name-1648072186','ROUND','DOUBLE','PLANNED',NULL),(98,'name-1648243304','ROUND','DOUBLE','PLANNED',NULL),(99,'name-1648484732','ROUND','SINGLE','PLANNED',NULL),(100,'name-1648489043','ROUND','SINGLE','PLANNED',NULL),(101,'name-1648491391','ROUND','SINGLE','PLANNED',NULL),(102,'name-1648491977','ROUND','SINGLE','PLANNED',NULL);
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

-- Dump completed on 2022-03-29  2:04:34

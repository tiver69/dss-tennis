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
  `winner_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `tournament_id` int(11) NOT NULL,
  `participant_one_score_id` int(11) NOT NULL,
  `participant_two_score_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`tournament_id`,`participant_one_score_id`,`participant_two_score_id`),
  KEY `fk_contest_tournament1_idx` (`tournament_id`),
  KEY `fk_contest_score1_idx1` (`participant_one_score_id`),
  KEY `fk_contest_score2_idx1` (`participant_two_score_id`),
  CONSTRAINT `fk_contest_score1` FOREIGN KEY (`participant_one_score_id`) REFERENCES `score` (`id`),
  CONSTRAINT `fk_contest_score2` FOREIGN KEY (`participant_two_score_id`) REFERENCES `score` (`id`),
  CONSTRAINT `fk_contest_tournament1` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contest`
--

LOCK TABLES `contest` WRITE;
/*!40000 ALTER TABLE `contest` DISABLE KEYS */;
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
/*!40000 ALTER TABLE `double_contest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elimination_contest`
--

DROP TABLE IF EXISTS `elimination_contest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `elimination_contest` (
  `contest_id` int(11) NOT NULL,
  `first_parent_contest_id` int(11) NOT NULL,
  `second_parent_contest_id` int(11) NOT NULL,
  PRIMARY KEY (`contest_id`,`first_parent_contest_id`,`second_parent_contest_id`),
  KEY `fk_elimination_contest_contest2_idx` (`first_parent_contest_id`),
  KEY `fk_elimination_contest_contest3_idx` (`second_parent_contest_id`),
  KEY `fk_elimination_contest_contest1_idx` (`contest_id`),
  CONSTRAINT `fk_elimination_contest_contest1` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`id`),
  CONSTRAINT `fk_elimination_contest_contest2` FOREIGN KEY (`first_parent_contest_id`) REFERENCES `contest` (`id`),
  CONSTRAINT `fk_elimination_contest_contest3` FOREIGN KEY (`second_parent_contest_id`) REFERENCES `contest` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elimination_contest`
--

LOCK TABLES `elimination_contest` WRITE;
/*!40000 ALTER TABLE `elimination_contest` DISABLE KEYS */;
/*!40000 ALTER TABLE `elimination_contest` ENABLE KEYS */;
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
  `birth_date` date DEFAULT NULL,
  `experience_year` int(11) DEFAULT NULL,
  `leading_hand` enum('LEFT','RIGHT') COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
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
  `set_one` tinyint(4) DEFAULT NULL,
  `set_two` tinyint(4) DEFAULT NULL,
  `set_three` tinyint(4) DEFAULT NULL,
  `tie_break` tinyint(4) DEFAULT NULL,
  `tech_defeat` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
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
  `tournament_type` enum('ROUND','ELIMINATION') COLLATE utf8_unicode_ci NOT NULL,
  `participant_type` enum('SINGLE','DOUBLE') COLLATE utf8_unicode_ci NOT NULL,
  `beginning_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournament`
--

LOCK TABLES `tournament` WRITE;
/*!40000 ALTER TABLE `tournament` DISABLE KEYS */;
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

-- Dump completed on 2022-12-20 16:56:06

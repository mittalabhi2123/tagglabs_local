-- MySQL dump 10.13  Distrib 5.6.14, for Win64 (x86_64)
--
-- Host: localhost    Database: lg_icc
-- ------------------------------------------------------
-- Server version	5.6.14

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
-- Table structure for table `day_college`
--

DROP TABLE IF EXISTS `day_college`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `day_college` (
  `city` varchar(100) DEFAULT NULL,
  `college` varchar(100) DEFAULT NULL,
  `event_date` varchar(1024) DEFAULT NULL,
  `eventId` varchar(1024) DEFAULT NULL,
  `placeId` varchar(1024) DEFAULT NULL,
  `placeCaption` varchar(1024) DEFAULT NULL,
  `picCaption` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pandalusers`
--

DROP TABLE IF EXISTS `pandalusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pandalusers` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `phone_no` varchar(14) DEFAULT NULL,
  `rfid` varchar(50) DEFAULT NULL,
  `fb_auth_token` varchar(500) DEFAULT NULL,
  `twitter_auth_token` varchar(500) DEFAULT NULL,
  `twitter_auth_secret` varchar(500) DEFAULT NULL,
  `fb_email` varchar(200) DEFAULT NULL,
  `twitter_email` varchar(200) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `polls`
--

DROP TABLE IF EXISTS `polls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `polls` (
  `poll_id` int(11) NOT NULL DEFAULT '0',
  `question` varchar(500) DEFAULT NULL,
  `option1` varchar(500) DEFAULT NULL,
  `option2` varchar(500) DEFAULT NULL,
  `option3` varchar(500) DEFAULT NULL,
  `option4` varchar(500) DEFAULT NULL,
  `option1_count` int(11) DEFAULT NULL,
  `option2_count` int(11) DEFAULT NULL,
  `option3_count` int(11) DEFAULT NULL,
  `option4_count` int(11) DEFAULT NULL,
  `orderNum` int(11) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `existsNewData` int(11) DEFAULT '0',
  `done` int(11) DEFAULT '0',
  `ans` varchar(100) DEFAULT NULL,
  `phnNo` varchar(20) DEFAULT NULL,
  `start` int(11) DEFAULT '0',
  PRIMARY KEY (`poll_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts` (
  `userId` int(11) NOT NULL DEFAULT '0',
  `post` varchar(3000) DEFAULT NULL,
  `postId` varchar(200) NOT NULL DEFAULT '',
  `likes` int(11) DEFAULT NULL,
  `comments` int(11) DEFAULT NULL,
  `fb_auth_token` varchar(500) DEFAULT NULL,
  `twitter_auth_token` varchar(500) DEFAULT NULL,
  `instagram_auth_token` varchar(500) DEFAULT NULL,
  `twitter_auth_secret` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`userId`,`postId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `phone_no` varchar(14) DEFAULT NULL,
  `fb_auth_token` varchar(500) DEFAULT NULL,
  `twitter_auth_token` varchar(500) DEFAULT NULL,
  `instagram_auth_token` varchar(500) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `college` varchar(200) DEFAULT NULL,
  `fb_email` varchar(200) DEFAULT NULL,
  `twitter_email` varchar(200) DEFAULT NULL,
  `instagram_email` varchar(200) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `twitter_auth_secret` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uservotes`
--

DROP TABLE IF EXISTS `uservotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uservotes` (
  `phnRfidNo` varchar(50) NOT NULL DEFAULT '',
  `venueId` varchar(50) NOT NULL DEFAULT '',
  `mode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`phnRfidNo`,`venueId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `venue`
--

DROP TABLE IF EXISTS `venue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `venue` (
  `sno` int(11) DEFAULT NULL,
  `venueId` varchar(50) NOT NULL DEFAULT '',
  `mobileNo` varchar(50) DEFAULT NULL,
  `name` varchar(500) DEFAULT NULL,
  `placePage` varchar(500) DEFAULT NULL,
  `placePageId` varchar(500) DEFAULT NULL,
  `imageId` varchar(500) DEFAULT NULL,
  `imageLink` varchar(500) DEFAULT NULL,
  `stallPage` varchar(500) DEFAULT NULL,
  `eventId` varchar(500) DEFAULT NULL,
  `eventLink` varchar(500) DEFAULT NULL,
  `phoneVote` int(11) DEFAULT NULL,
  `rfidVote` int(11) DEFAULT NULL,
  `fbLikeVote` int(11) DEFAULT NULL,
  PRIMARY KEY (`venueId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-12-05 16:11:51

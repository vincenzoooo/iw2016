-- MySQL dump 10.13  Distrib 5.7.10, for Win32 (AMD64)
--
-- Host: localhost    Database: iw2016
-- ------------------------------------------------------
-- Server version	5.7.10-log

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
-- Table structure for table `autore`
--

DROP TABLE IF EXISTS `autore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autore` (
  `idautore` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `cognome` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idautore`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `autore_has_pubblicazione`
--

DROP TABLE IF EXISTS `autore_has_pubblicazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autore_has_pubblicazione` (
  `autore_idautore` int(11) NOT NULL,
  `pubblicazione_idpubblicazione` int(11) NOT NULL,
  PRIMARY KEY (`autore_idautore`,`pubblicazione_idpubblicazione`),
  KEY `idx_autore_has_pubblicazione_pubblicazione` (`pubblicazione_idpubblicazione`),
  KEY `idx_autore_has_pubblicazione_autore` (`autore_idautore`),
  CONSTRAINT `fk_autore_has_pubblicazione_autore` FOREIGN KEY (`autore_idautore`) REFERENCES `autore` (`idautore`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_autore_has_pubblicazione_pubblicazione` FOREIGN KEY (`pubblicazione_idpubblicazione`) REFERENCES `pubblicazione` (`idpubblicazione`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `editore`
--

DROP TABLE IF EXISTS `editore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editore` (
  `ideditore` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ideditore`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `keyword`
--

DROP TABLE IF EXISTS `keyword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `keyword` (
  `idkeyword` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idkeyword`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pubblicazione`
--

DROP TABLE IF EXISTS `pubblicazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pubblicazione` (
  `idpubblicazione` int(11) NOT NULL AUTO_INCREMENT,
  `titolo` varchar(45) DEFAULT NULL,
  `descrizione` varchar(45) DEFAULT NULL,
  `editore` int(11) NOT NULL,
  `indice` varchar(45) DEFAULT NULL,
  `n_consigli` int(11) DEFAULT NULL,
  `isbn` int(11) DEFAULT NULL,
  `n_pagine` int(11) DEFAULT NULL,
  `lingua` varchar(45) DEFAULT NULL,
  `data_pubblicazione` date DEFAULT NULL,
  PRIMARY KEY (`idpubblicazione`),
  KEY `idx_pubblicazione_editore` (`editore`),
  CONSTRAINT `fk_pubblicazione_editore` FOREIGN KEY (`editore`) REFERENCES `editore` (`ideditore`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pubblicazione_has_keyword`
--

DROP TABLE IF EXISTS `pubblicazione_has_keyword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pubblicazione_has_keyword` (
  `pubblicazione_idpubblicazione` int(11) NOT NULL,
  `keyword_idkeyword` int(11) NOT NULL,
  PRIMARY KEY (`pubblicazione_idpubblicazione`,`keyword_idkeyword`),
  KEY `fk_pubblicazione_has_keyword_keyword_idx` (`keyword_idkeyword`),
  CONSTRAINT `fk_pubblicazione_has_keyword_keyword` FOREIGN KEY (`keyword_idkeyword`) REFERENCES `keyword` (`idkeyword`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pubblicazione_has_keyword_pubblicazione` FOREIGN KEY (`pubblicazione_idpubblicazione`) REFERENCES `pubblicazione` (`idpubblicazione`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pubblicazione_has_sorgente`
--

DROP TABLE IF EXISTS `pubblicazione_has_sorgente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pubblicazione_has_sorgente` (
  `pubblicazione_idpubblicazione` int(11) NOT NULL,
  `sorgente_idsorgente` int(11) NOT NULL,
  PRIMARY KEY (`pubblicazione_idpubblicazione`,`sorgente_idsorgente`),
  KEY `idx_pubblicazione_has_sorgente_sorgente` (`sorgente_idsorgente`),
  KEY `idx_pubblicazione_has_sorgente_pubblicazione` (`pubblicazione_idpubblicazione`),
  CONSTRAINT `fk_pubblicazione_has_sorgente_pubblicazione` FOREIGN KEY (`pubblicazione_idpubblicazione`) REFERENCES `pubblicazione` (`idpubblicazione`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pubblicazione_has_sorgente_sorgente` FOREIGN KEY (`sorgente_idsorgente`) REFERENCES `sorgente` (`idsorgente`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recensione`
--

DROP TABLE IF EXISTS `recensione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recensione` (
  `idrecensione` int(11) NOT NULL AUTO_INCREMENT,
  `testo` varchar(45) DEFAULT NULL,
  `moderata` tinyint(1) DEFAULT NULL,
  `utente_autore` int(11) NOT NULL,
  `pubblicazione` int(11) NOT NULL,
  `storico` int(11) DEFAULT NULL,
  PRIMARY KEY (`idrecensione`),
  KEY `idx_recensione_pubblicazione` (`pubblicazione`),
  KEY `idx_recensione_utente` (`utente_autore`),
  KEY `idx_recensione_storico` (`storico`),
  CONSTRAINT `fk_recensione_pubblicazione` FOREIGN KEY (`pubblicazione`) REFERENCES `pubblicazione` (`idpubblicazione`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_recensione_storico` FOREIGN KEY (`storico`) REFERENCES `storico` (`idstorico`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_recensione_utente` FOREIGN KEY (`utente_autore`) REFERENCES `utente` (`idutente`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ristampa`
--

DROP TABLE IF EXISTS `ristampa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ristampa` (
  `idristampa` int(11) NOT NULL AUTO_INCREMENT,
  `numero` int(11) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `pubblicazione` int(11) NOT NULL,
  PRIMARY KEY (`idristampa`),
  KEY `idx_ristampe_pubblicazione` (`pubblicazione`),
  CONSTRAINT `fk_ristampe_pubblicazione` FOREIGN KEY (`pubblicazione`) REFERENCES `pubblicazione` (`idpubblicazione`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sorgente`
--

DROP TABLE IF EXISTS `sorgente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sorgente` (
  `idsorgente` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(45) DEFAULT NULL,
  `URI` varchar(45) DEFAULT NULL,
  `formato` varchar(45) DEFAULT NULL,
  `descrizione` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idsorgente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `storico`
--

DROP TABLE IF EXISTS `storico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `storico` (
  `idstorico` int(11) NOT NULL AUTO_INCREMENT,
  `entry` varchar(45) DEFAULT NULL,
  `tipo` int(11) DEFAULT NULL,
  `data_operazione` datetime DEFAULT NULL,
  `pubblicazione` int(11) NOT NULL,
  `utente` int(11) NOT NULL,
  PRIMARY KEY (`idstorico`),
  KEY `idx_storico_pubblicazione` (`pubblicazione`),
  KEY `idx_storico_utente` (`utente`),
  CONSTRAINT `fk_storico_pubblicazione` FOREIGN KEY (`pubblicazione`) REFERENCES `pubblicazione` (`idpubblicazione`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_storico_utente` FOREIGN KEY (`utente`) REFERENCES `utente` (`idutente`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utente` (
  `idutente` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `cognome` varchar(45) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `stato` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`idutente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-31 10:19:13

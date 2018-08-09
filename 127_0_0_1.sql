-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Aug 09, 2018 at 12:30 PM
-- Server version: 5.7.22
-- PHP Version: 7.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `enterprise`
--
CREATE DATABASE IF NOT EXISTS `enterprise` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `enterprise`;

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE IF NOT EXISTS `accounts` (
  `UUID` varchar(50) DEFAULT NULL,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Salary` varchar(255) NOT NULL,
  `Statue` varchar(255) NOT NULL,
  `RegistrationDate` varchar(255) NOT NULL,
  `Usertype` varchar(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UUID` (`UUID`),
  UNIQUE KEY `accounts_UUID_uindex` (`UUID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`UUID`, `ID`, `Username`, `Password`, `Email`, `Salary`, `Statue`, `RegistrationDate`, `Usertype`) VALUES
('8b9ed3d1-9b23-11e8-880a-0a0027000012', 5, 'rami', 'a7cd345c00ad379d0b665b72a0bb1a90', 'Borni@rami.com', '526330', 'Abandoned', '2018-08-08 03:55:59 PM', 'U');

-- --------------------------------------------------------

--
-- Table structure for table `admin_account`
--

DROP TABLE IF EXISTS `admin_account`;
CREATE TABLE IF NOT EXISTS `admin_account` (
  `UUID` varchar(36) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `admin_account`
--

INSERT INTO `admin_account` (`UUID`, `Username`, `Password`) VALUES
('c61e6442-ef64-4cc3-ac99-1ddcccdd3bd8', 'admin', '21232f297a57a5a743894a0e4a801fc3');

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
CREATE TABLE IF NOT EXISTS `sessions` (
  `login_id` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` varchar(50) NOT NULL,
  `IMEI` varchar(50) NOT NULL,
  `IP` varchar(50) NOT NULL,
  `LoginDate` varchar(100) NOT NULL,
  PRIMARY KEY (`login_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sessions`
--

INSERT INTO `sessions` (`login_id`, `UserID`, `IMEI`, `IP`, `LoginDate`) VALUES
(1, '8b9ed3d1-9b23-11e8-880a-0a0027000012', '358240051111110', '105.106.55.255', '2018-08-09 11:36:58 AM');

-- --------------------------------------------------------

--
-- Table structure for table `users_login_log`
--

DROP TABLE IF EXISTS `users_login_log`;
CREATE TABLE IF NOT EXISTS `users_login_log` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `UserID` varchar(255) NOT NULL,
  `IMEI` varchar(255) NOT NULL,
  `IP` varchar(255) NOT NULL,
  `Login_Date` varchar(50) NOT NULL,
  `Logout_Date` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users_login_log`
--

INSERT INTO `users_login_log` (`id`, `UserID`, `IMEI`, `IP`, `Login_Date`, `Logout_Date`) VALUES
(5, '8f4d2eab-9965-11e8-9e0f-0a0027000012', '358240051111110', '105.106.37.180', '2018-08-07 05:31:04 PM', '2018-08-07 17:31:17'),
(6, '8f4d2eab-9965-11e8-9e0f-0a0027000012', '358240051111110', '105.106.37.180', '2018-08-07 06:06:23 PM', '2018-08-07 18:06:29'),
(7, '8f4d2eab-9965-11e8-9e0f-0a0027000012', '358240051111110', '105.106.37.180', '2018-08-07 06:27:47 PM', '2018-08-07 18:27:53'),
(8, '8b9ed3d1-9b23-11e8-880a-0a0027000012', '358240051111110', '105.106.60.135', '2018-08-08 03:56:19 PM', '2018-08-08 16:56:26'),
(9, 'c61e6442-ef64-4cc3-ac99-1ddcccdd3bd8', '358240051111110', '105.106.60.135', '2018-08-08 05:38:17 PM', '2018-08-08 18:38:19'),
(10, 'c61e6442-ef64-4cc3-ac99-1ddcccdd3bd8', '358240051111110', '105.106.60.135', '2018-08-08 05:38:31 PM', '2018-08-08 18:38:36');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 23, 2020 at 08:45 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `UID` int(11) UNSIGNED NOT NULL,
  `email` varchar(50) NOT NULL DEFAULT '',
  `username` varchar(15) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `salt` varchar(35) DEFAULT NULL,
  `rights` int(1) NOT NULL DEFAULT 0,
  `email_activated` int(1) NOT NULL DEFAULT 0,
  `lastActive` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `donatorType` int(2) NOT NULL DEFAULT -1,
  `donationTotal` double(10,2) NOT NULL DEFAULT 0.00,
  `credits` int(5) NOT NULL DEFAULT 0,
  `icon` int(2) NOT NULL DEFAULT 0,
  `perks` varchar(500) NOT NULL DEFAULT '',
  `ip` longtext DEFAULT NULL,
  `mac` longtext DEFAULT NULL,
  `serial` longtext DEFAULT NULL,
  `computerName` varchar(2000) NOT NULL DEFAULT '',
  `monthlyVotes` int(11) NOT NULL DEFAULT 0,
  `netWorth` bigint(200) NOT NULL DEFAULT 0,
  `forumUID` int(11) NOT NULL DEFAULT -1,
  `ironManMode` varchar(15) NOT NULL DEFAULT 'NONE',
  `bank` longtext DEFAULT NULL,
  `inventory` longtext DEFAULT NULL,
  `equipment` longtext DEFAULT NULL,
  `ge` longtext DEFAULT NULL,
  `muteTime` bigint(20) NOT NULL DEFAULT -1,
  `banTime` bigint(20) NOT NULL DEFAULT -1,
  `profileImage` varchar(300) DEFAULT NULL,
  `contacts` longtext DEFAULT NULL,
  `blocked` longtext DEFAULT NULL,
  `clanName` varchar(15) NOT NULL DEFAULT '',
  `currentClan` varchar(15) DEFAULT NULL,
  `clanReqs` varchar(10) NOT NULL DEFAULT '1,0,8,9',
  `disconnectTime` bigint(20) NOT NULL DEFAULT 0,
  `lastWorld` int(3) NOT NULL DEFAULT -1,
  `chatSettings` varchar(10) NOT NULL DEFAULT '0,0,0',
  `timePlayed` bigint(20) DEFAULT 0,
  `lastLogin` bigint(20) NOT NULL DEFAULT 0,
  `lastGameIp` varchar(15) DEFAULT '',
  `countryCode` int(11) NOT NULL DEFAULT 0,
  `birthday` date DEFAULT NULL,
  `online` tinyint(1) NOT NULL DEFAULT 0,
  `signature` longtext DEFAULT NULL,
  `joined_date` timestamp NULL DEFAULT NULL,
  `posts` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `worlds`
--

CREATE TABLE `worlds` (
  `world` int(2) UNSIGNED NOT NULL,
  `ip` varchar(20) NOT NULL DEFAULT '127.0.0.1',
  `players` int(5) NOT NULL DEFAULT 0,
  `country` int(1) NOT NULL DEFAULT 0,
  `member` int(11) NOT NULL,
  `revision` int(3) NOT NULL DEFAULT 530,
  `lastResponse` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `worlds`
--

INSERT INTO `worlds` (`world`, `ip`, `players`, `country`, `member`, `revision`, `lastResponse`) VALUES
(1, '127.0.0.1', 0, 22, 1, 530, '2020-09-23 18:27:05');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`UID`);

--
-- Indexes for table `worlds`
--
ALTER TABLE `worlds`
  ADD PRIMARY KEY (`world`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `members`
--
ALTER TABLE `members`
  MODIFY `UID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

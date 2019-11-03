-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 02, 2019 at 08:53 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `global`
--

-- --------------------------------------------------------

--
-- Table structure for table `dev_log`
--

CREATE TABLE `dev_log` (
  `id` int(11) UNSIGNED NOT NULL,
  `username` varchar(15) DEFAULT NULL,
  `content` longtext NOT NULL,
  `date` timestamp NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dev_log`
--

INSERT INTO `dev_log` (`id`, `username`, `content`, `date`) VALUES
(1, 'vexia', 'Added Arios voting bonds as a reward', '2015-12-04 04:03:28'),
(3, 'vexia', 'Added profile viewing (forums)', '2015-12-06 00:07:11'),
(4, 'vexia', 'Added herb boxes', '2015-12-06 00:07:23'),
(5, 'vexia', 'Added an NPC to buy low-level skilling items for high price!', '2015-12-06 18:05:16'),
(6, 'vexia', 'Adjusted home shops & prices', '2015-12-06 21:35:31'),
(7, 'vexia', 'Fixed inventory disappearing when using deposit box.', '2015-12-13 18:51:16'),
(8, 'vexia', 'Fixed player safety test completion interface.', '2015-12-13 18:51:43'),
(9, 'vexia', 'Fixed falador agility shortcut.', '2015-12-13 18:58:04'),
(10, 'vexia', 'Fixed Goldsmith gauntlets experience reward.', '2015-12-13 18:59:41'),
(11, 'vexia', 'Zamorakian hasta is now a godwars protection item.', '2015-12-13 19:04:15'),
(12, 'vexia', 'Fixed slayer helmet effect.', '2015-12-13 19:11:37'),
(13, 'vexia', 'Duel arena veng bug has been fixed.', '2015-12-13 19:20:05'),
(14, 'vexia', 'Changed steel bolts from 8gp to 150 gp', '2015-12-15 03:52:43'),
(15, 'vexia', 'Removed astral runes from magic shop', '2015-12-15 03:54:13'),
(16, 'vexia', 'Recent activity for profiles now includes when users create threads', '2015-12-15 04:05:03'),
(17, 'vexia', 'Added beginner level tasks for Lumbridge/Draynor diary.', '2015-12-15 06:23:00'),
(18, 'vexia', 'Added medium level tasks for lumbridge/draynor achievement diary.', '2015-12-16 07:46:23'),
(19, 'vexia', 'Finished the Lumbridge Achievement Diary.', '2015-12-17 05:57:38'),
(20, 'vexia', 'Added the explorer rings mechanics.', '2015-12-17 07:15:54'),
(21, 'empathy', 'Added broad arrow making - must unlock via slayer rewards', '2015-12-17 07:19:54'),
(22, 'empathy', 'Added broad bolt making - must unlock via slayer rewards ', '2015-12-17 07:20:54'),
(23, 'empathy', 'Added middle mouse rotation for the client', '2015-12-17 07:22:54'),
(24, 'empathy', 'Added Dagannoth Kings under dagganoth tasks', '2015-12-17 07:29:54'),
(25, 'vexia', 'Added the gilded altar.', '2015-12-18 18:32:52'),
(26, 'vexia', 'Fixed shooting star mining requirements.', '2015-12-18 18:45:10'),
(27, 'vexia', 'Fixed an iron titan bug.', '2015-12-18 18:45:16'),
(28, 'vexia', 'Changed Arios teleporter to 5 seconds.', '2015-12-18 19:06:53'),
(30, 'vexia', 'Added broad-tipped bolts.', '2016-01-01 21:11:36'),
(31, 'vexia', 'Fixed KQ', '2016-01-01 21:11:43'),
(32, 'vexia', 'Fixed KBD slayer task!', '2016-01-01 21:11:49'),
(33, 'vexia', 'Fixed venenatis & callisto safe spots', '2016-01-01 22:16:26');

-- --------------------------------------------------------

--
-- Table structure for table `highscores`
--

CREATE TABLE `highscores` (
  `id` int(11) UNSIGNED NOT NULL,
  `username` varchar(20) DEFAULT NULL,
  `overall_xp` int(11) NOT NULL DEFAULT '0',
  `total_level` int(11) NOT NULL DEFAULT '0',
  `ironManMode` varchar(15) NOT NULL DEFAULT 'NONE',
  `xp_0` int(11) NOT NULL DEFAULT '0',
  `xp_1` int(11) NOT NULL DEFAULT '0',
  `xp_2` int(11) NOT NULL DEFAULT '0',
  `xp_3` int(11) NOT NULL DEFAULT '0',
  `xp_4` int(11) NOT NULL DEFAULT '0',
  `xp_5` int(11) NOT NULL DEFAULT '0',
  `xp_6` int(11) NOT NULL DEFAULT '0',
  `xp_7` int(11) NOT NULL DEFAULT '0',
  `xp_8` int(11) NOT NULL DEFAULT '0',
  `xp_9` int(11) NOT NULL DEFAULT '0',
  `xp_10` int(11) NOT NULL DEFAULT '0',
  `xp_11` int(11) NOT NULL DEFAULT '0',
  `xp_12` int(11) NOT NULL DEFAULT '0',
  `xp_13` int(11) NOT NULL DEFAULT '0',
  `xp_14` int(11) NOT NULL DEFAULT '0',
  `xp_15` int(11) NOT NULL DEFAULT '0',
  `xp_16` int(11) NOT NULL DEFAULT '0',
  `xp_17` int(11) NOT NULL DEFAULT '0',
  `xp_18` int(11) NOT NULL DEFAULT '0',
  `xp_19` int(11) NOT NULL DEFAULT '0',
  `xp_20` int(11) NOT NULL DEFAULT '0',
  `xp_21` int(11) NOT NULL DEFAULT '0',
  `xp_22` int(11) NOT NULL DEFAULT '0',
  `xp_23` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `highscores`
--

INSERT INTO `highscores` (`id`, `username`, `overall_xp`, `total_level`, `ironManMode`, `xp_0`, `xp_1`, `xp_2`, `xp_3`, `xp_4`, `xp_5`, `xp_6`, `xp_7`, `xp_8`, `xp_9`, `xp_10`, `xp_11`, `xp_12`, `xp_13`, `xp_14`, `xp_15`, `xp_16`, `xp_17`, `xp_18`, `xp_19`, `xp_20`, `xp_21`, `xp_22`, `xp_23`) VALUES
(1, 'uim_alex', 36043583, 763, 'ULTIMATE', 8771558, 7195629, 10692629, 8771558, 65600, 237150, 16700, 19500, 68125, 0, 0, 91000, 8780, 0, 79722, 0, 0, 0, 132, 0, 25500, 0, 0, 0),
(2, 'alex', 312962865, 2376, 'NONE', 13093231, 13038431, 13034431, 13065525, 13058831, 13048458, 13038631, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431, 13034431);

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
  `rights` int(1) NOT NULL DEFAULT '0',
  `email_activated` int(1) NOT NULL DEFAULT '0',
  `lastActive` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `donatorType` int(2) NOT NULL DEFAULT '-1',
  `donationTotal` double(10,2) NOT NULL DEFAULT '0.00',
  `credits` int(5) NOT NULL DEFAULT '0',
  `icon` int(2) NOT NULL DEFAULT '0',
  `perks` varchar(500) NOT NULL DEFAULT '',
  `ip` longtext,
  `mac` longtext,
  `serial` longtext,
  `computerName` varchar(2000) NOT NULL DEFAULT '',
  `monthlyVotes` int(11) NOT NULL DEFAULT '0',
  `netWorth` bigint(200) NOT NULL DEFAULT '0',
  `forumUID` int(11) NOT NULL DEFAULT '-1',
  `ironManMode` varchar(15) NOT NULL DEFAULT 'NONE',
  `bank` longtext,
  `inventory` longtext,
  `equipment` longtext,
  `ge` longtext,
  `muteTime` bigint(20) NOT NULL DEFAULT '-1',
  `banTime` bigint(20) NOT NULL DEFAULT '-1',
  `profileImage` varchar(300) NOT NULL DEFAULT 'http://ariosrsps.com/lib/images/forums/defaultprofile.png',
  `contacts` longtext,
  `blocked` longtext,
  `clanName` varchar(12) NOT NULL DEFAULT '',
  `currentClan` varchar(12) NOT NULL DEFAULT 'arios',
  `clanReqs` varchar(10) NOT NULL DEFAULT '1,0,8,9',
  `disconnectTime` bigint(20) NOT NULL DEFAULT '0',
  `lastWorld` int(3) NOT NULL DEFAULT '-1',
  `chatSettings` varchar(10) NOT NULL DEFAULT '0,0,0',
  `timePlayed` bigint(20) DEFAULT '0',
  `lastLogin` bigint(20) NOT NULL DEFAULT '0',
  `lastGameIp` varchar(15) DEFAULT '',
  `countryCode` int(11) NOT NULL DEFAULT '0',
  `birthday` date DEFAULT NULL,
  `online` tinyint(1) NOT NULL DEFAULT '0',
  `signature` longtext,
  `joined_date` timestamp NULL DEFAULT NULL,
  `posts` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `members`
--

INSERT INTO `members` (`UID`, `email`, `username`, `password`, `salt`, `rights`, `email_activated`, `lastActive`, `donatorType`, `donationTotal`, `credits`, `icon`, `perks`, `ip`, `mac`, `serial`, `computerName`, `monthlyVotes`, `netWorth`, `forumUID`, `ironManMode`, `bank`, `inventory`, `equipment`, `ge`, `muteTime`, `banTime`, `profileImage`, `contacts`, `blocked`, `clanName`, `currentClan`, `clanReqs`, `disconnectTime`, `lastWorld`, `chatSettings`, `timePlayed`, `lastLogin`, `lastGameIp`, `countryCode`, `birthday`, `online`, `signature`, `joined_date`, `posts`) VALUES
(1, '', 'alex', '$2a$12$oQz3KDY/4la77lPpYzr2Aupb8NDp5Q8wixAC073w5M18ntt8P8RC2', '$2a$12$oQz3KDY/4la77lPpYzr2Au', 2, 0, '2016-03-26 00:06:56', -1, 0.00, 0, 0, '', '96.254.196.5', 'D4-3D-7E-97-2C-45', 'To be filled by O.E.M.', 'Alex', 0, -1940633227, -1, 'NONE', '14850,1|14851,1|14853,1|7462,10|14636,1|14638,1|14639,1|560,8000|14640,1|14896,1|561,10000|14641,1|14897,1|562,8000|14642,1|14898,1|14643,1|14899,1|14644,1|14900,1|14645,1|14646,1|4151,102|14647,1|14648,1|14649,1|14650,1|14651,1|14652,1|14653,1|14654,1|14655,1|14656,1|14657,2|14658,1|14660,1|14661,1|14662,1|8007,3|14664,1|14666,1|8010,3|14669,1|14671,1|14673,3|14674,5|14675,1|14936,1|14683,1|861,1|13661,1|14958,1|14962,1|14963,1|14964,1|14965,1|373,50|14966,1|14969,1|14970,1|379,20|14971,1|892,992|14972,102|14977,1|14980,1|14726,1|14983,1|14984,1|14985,1|14988,1|14989,1|14990,1|14991,1|7056,25|14992,1|14993,1|14994,101|14484,4|14742,1|14748,1|15004,1|14749,1|15005,1|15006,1|15007,102|14752,1|15008,2|15010,1000|13734,1|15015,50|15016,3|13736,1|14761,1|6570,10|15018,3|14762,1|13738,1|14763,1|15020,2|14764,1|13740,1|14765,1|14766,1|13742,1|14767,4|14768,100|13744,1|14773,2|14774,2|6585,11|14788,3|14792,1|14793,1|14808,1|14809,1|14810,1|14818,1|995,2147483647|14820,1|14821,1|14822,1|14823,1|14827,1|14828,1|14829,1|14830,1|1775,34|2289,25|14839,1|14845,1|14847,1', '', '14484,1', '', -1, -1, 'http://ariosrsps.com/lib/images/forums/defaultprofile.png', '{uim_alex,1}', '', '', 'arios', '1,0,8,9', 1458950814328, 1, '0,0,0', 11590572, 1458950816126, '96.254.196.5', 225, '3895-01-01', 0, NULL, '2016-03-23 00:03:49', 0),
(2, '', 'uim_alex', '$2a$12$m.rN4wsgmiNi6GHnmMNv5uutUpSS9r9f3GFs.E4pxwsn4MFPDsH9W', '$2a$12$m.rN4wsgmiNi6GHnmMNv5u', 0, 0, '2016-03-25 21:18:31', -1, 0.00, 0, 0, '', '96.254.196.5', 'D4-3D-7E-97-2C-45', 'To be filled by O.E.M.', 'Alex', 0, 141601, -1, 'ULTIMATE', '7056,25|2289,25|115,15|133,15|373,50|121,15', '3105,1|3841,1|7458,1|995,16097|1381,1|8010,3|14764,1|556,169|14765,1|558,237|14766,1|590,1|303,1|8880,1|8882,341|1725,1|1438,1', '', '', -1, -1, 'http://ariosrsps.com/lib/images/forums/defaultprofile.png', '{alex,1}', '', '', 'arios', '1,0,8,9', 1458940709765, 1, '0,0,0', 12479841, 1458940711546, '96.254.196.5', 225, '3890-01-01', 0, NULL, '2016-03-23 00:42:58', 0),
(3, '', 'michael', '$2a$12$f09/0zVyZp.1lF6WpAMCBuKO5ROHnlLHgf4NLv6VUUQfKCvSTMtN2', '$2a$12$f09/0zVyZp.1lF6WpAMCBu', 2, 0, '2019-10-30 22:14:02', -1, 0.00, 0, 0, '', '127.0.0.1', '40-8D-5C-EF-A5-FE', 'To be filled by O.E.M.', 'Michael', 0, 194115277, -1, 'NONE', '1,10|201,1|209,1|5341,1|1121,1|995,49994558|9703,1|9704,1|9705,1|554,49999|555,50000|556,49997|557,50004|1965,1|558,50000|559,50000|560,50000|561,50000|882,7|562,50000|563,49999|9075,50000|755,1|1203,1|564,50000|565,50000|566,50000|4151,1|313,1|5305,1|4155,1|892,2|5310,2', '554,50000|555,50000|556,49995|557,50000|558,50000|559,50000|560,50000|561,50000|562,50000|563,49999|9075,50000|564,50000|565,50000|566,50000', '1139,1|892,78|861,1', '', -1, -1, 'http://ariosrsps.com/lib/images/forums/defaultprofile.png', '', '', '', 'arios', '1,0,8,9', 1572473640839, 1, '0,0,0', 21542919, 1572473642640, '127.0.0.1', 5, '3909-01-01', 0, NULL, '2019-10-29 00:17:26', 0),
(4, '', 'michael2', '$2a$12$HBYVWU66ooT8mt5pDM7dNOPmGKutgRzxx90f/S0knMJxmWTz7V3WO', '$2a$12$HBYVWU66ooT8mt5pDM7dNO', 0, 0, '2019-10-29 02:53:32', -1, 0.00, 0, 0, '', '127.0.0.1', '40-8D-5C-EF-A5-FE', 'To be filled by O.E.M.', 'Michael', 0, 0, -1, 'NONE', '', '', '', '', -1, -1, 'http://ariosrsps.com/lib/images/forums/defaultprofile.png', '', '', '', 'arios', '1,0,8,9', 1572317611170, 1, '0,0,0', 65346, 1572317612972, '127.0.0.1', 5, '3909-01-01', 0, NULL, '2019-10-29 02:52:18', 0),
(5, '', '3', '$2a$12$bXpRnJ.B9D6ho7HINKCWWuFpp4SKK0JWLR0m5q5FW8svKTD/iG3p6', '$2a$12$bXpRnJ.B9D6ho7HINKCWWu', 0, 0, '2019-10-29 02:54:07', -1, 0.00, 0, 0, '', '127.0.0.1', '40-8D-5C-EF-A5-FE', 'To be filled by O.E.M.', 'Michael', 0, 0, -1, 'NONE', '', '', '', '', -1, -1, 'http://ariosrsps.com/lib/images/forums/defaultprofile.png', '', '', '', 'arios', '1,0,8,9', 1572317645662, 1, '0,0,0', 13540, 1572317647523, '127.0.0.1', 5, '3909-01-01', 0, NULL, '2019-10-29 02:53:50', 0),
(6, '', '1', '$2a$12$vpnJNwQA13HcJPdqC8lT3OAXsiW/QoQ783mS5GSiXqKkh895IBs2O', '$2a$12$vpnJNwQA13HcJPdqC8lT3O', 0, 0, '2019-10-30 16:28:41', -1, 0.00, 0, 0, '', '127.0.0.1', '40-8D-5C-EF-A5-FE', 'To be filled by O.E.M.', 'Michael', 0, 0, -1, 'NONE', '', '1925,1|1351,1|1511,8|841,1|1931,1|555,6|556,25|205,1|557,4|590,1|558,15|303,1|559,2|1265,1|1521,3|882,25|1171,1|1205,1|315,1', '1009,1|1189,1|1277,1', '', -1, -1, 'http://ariosrsps.com/lib/images/forums/defaultprofile.png', '', '', '', 'arios', '1,0,8,9', 1572452919260, 1, '0,0,0', 135254856, 1572452921553, '127.0.0.1', 5, '3909-01-01', 0, NULL, '2019-10-29 02:54:23', 0);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` int(11) UNSIGNED NOT NULL,
  `sender` varchar(15) NOT NULL DEFAULT '',
  `recipient` varchar(15) NOT NULL DEFAULT '',
  `subject` varchar(40) NOT NULL DEFAULT '',
  `content` longtext NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_read` tinyint(1) NOT NULL,
  `s_delete` tinyint(11) NOT NULL,
  `r_delete` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `perks`
--

CREATE TABLE `perks` (
  `product_id` int(10) UNSIGNED NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT 'No Name',
  `description` varchar(500) DEFAULT NULL,
  `price` int(10) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `perks`
--

INSERT INTO `perks` (`product_id`, `name`, `description`, `price`) VALUES
(2, 'Stamina Boost', 'Increase your stamina and run regeneration by a total of 40%.', 60),
(4, 'Green Thumb', 'With a 35% lifetime increase in your crops growing up healthy, they will be saved and you will also receive a better crop yield!', 100),
(5, 'Bird Man', 'Increase your rate of receiving a bird nest drop by 35%!', 50),
(6, 'Stoner', 'Increase your rate of receiving a gem stone drop by 35%!', 50),
(11, 'Unbreakable Forge', 'Ring of forging never breaks.', 30),
(12, 'Out of Grave Danger', 'Your gravestone will last up to twice as long with this perk.', 50),
(13, 'Sleight of Hand', 'With sleight of hand you will increase your success rates in all aspects of thieving. This includes pickpocketing and cracking wall safes for gems.', 50),
(14, 'Master Chef', 'As a soon to be Master Chef, you will receive a 20% increase in successfully cooking your food!', 50),
(16, 'Divine Intervention', 'The gods above intervene with your burying of bones. You have a 10% chance while burying a bone to keep it instead.', 70),
(17, 'Familiar Whisperer', 'Get to know your familiar better by increasing their lifespan by 50%.', 70),
(18, 'Barrows Befriender', 'Befriend the barrows brothers & never experience the wretched degrading of their armour again', 300),
(19, 'Abyss Befriender', 'Use the power of the abyss to make your Runecrafting pouches undegradable.', 150),
(21, 'Charge Befriender', 'The God\'s of the Hero\'s guild have blessed you with the power to use your jewerly free of charge.', 250),
(22, 'Golden Needle', 'Gain an extra 10% experience whilst spinning something on a spinning wheel, including flax. Creating an item made out of dragonhide rewards an extra 5% experience. Your crafting needle also never breaks and thread is consumed less often.\n', 50),
(24, 'Slayer Betrayer', 'Obtain the ability through the Slayer Masters to change your slayer task at will. Type ::cleartask to use.', 100),
(26, 'Thirst Quencher', 'The gods have blessed you with the knowledge of the deserts to gain the skills required to tap into an unlimited water supply.', 30),
(27, 'Double Trouble', 'Experience a chance of receiving double the resources through skills such as, mining, woodcutting, fishing, and many more.', 250),
(29, 'Godwars Befriender', 'Now blessed by the gods you have the ability to enter the chambers with a killcount of 30. You will also be granted half the time it takes to recharge at an altar.', 100),
(30, 'Prayer Betrayer', 'Experience half the prayer drain rate when this perk is enabled.', 150),
(31, 'Spell Swap', 'The ability to swap spell books without any charge of runes. Cannot be used in combat or in the wilderness.', 80),
(32, 'Dwarf Befriender', 'Befriended by the dwarfs you now have the ability to use double the cannon balls and experience no decay on your cannon.', 150),
(33, 'Powerpoint', 'This perk grants you double the points in all minigames.', 300),
(35, 'Charm Collector', 'Through the power of summoning you will automatically pick up any charms dropped in battle.', 100),
(36, 'Detective', 'You now have a solid 10% chance of a clue scroll drop from any monster that drops clues as well as a 50% better chance of <strong>super rare</strong> rewards such as 3rd age. You\'ll also experience a 50% increased chance to obtain more loot.', 250),
(40, 'Overcharge', 'The power from the overcharge lords is given to you. Your Dragonfire Shield will recharge fully every 10 minutes. The time between casts is also reduced by 50%.', 170),
(41, 'Unbreakable Crystal', 'This perk allows for your crystal bow to never degrade.', 350),
(42, 'Crusader', 'With this perk you will have a 25% chance to double loot the barrows chest.', 100),
(43, 'Pet Befriender', 'This perk gives you the ability to double your chances on getting boss/skilling pets!', 100),
(60, 'Bone Crusher', 'Automatically crushes your bones as they\'re dropped for prayer experience. Toggle this perk using ::bonecrusher', 100),
(70, 'Runestone Knowledge', 'You are given extended knowledge of the runecrafting skill and can now craft double death, law, cosmic, blood and nature runes.', 200),
(71, 'Coin machine', 'Automatically bank all coins dropped from NPC\'s and gives you 25% extra gold. Toggle this perk using ::coinmachine', 150),
(72, 'Fight Cave Fanatic', 'Eliminates the first 25 waves from the tzhaar fight caves.', 50),
(73, 'Decanter', 'Zahur will decant your noted potions if you have this perk.', 50);

-- --------------------------------------------------------

--
-- Table structure for table `player_logs`
--

CREATE TABLE `player_logs` (
  `username` varchar(22) NOT NULL DEFAULT '',
  `public_chat` longtext,
  `private_chat` longtext,
  `clan_chat` longtext,
  `address_log` longtext,
  `command_log` longtext,
  `trade_log` longtext,
  `ge_log` longtext,
  `duplication_log` longtext,
  `duel_log` longtext
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `player_logs`
--

INSERT INTO `player_logs` (`username`, `public_chat`, `private_chat`, `clan_chat`, `address_log`, `command_log`, `trade_log`, `ge_log`, `duplication_log`, `duel_log`) VALUES
('1', '', '', '', '28/10/2019 21:54:26: 127.0.0.1\n28/10/2019 21:54:26: To be filled by O.E.M.\n28/10/2019 21:54:26: 40-8D-5C-EF-A5-FE\n', '28/10/2019 21:54:59: tele 3200 3200\n28/10/2019 21:55:03: tele 3225 3225\n28/10/2019 21:55:23: pos\n28/10/2019 21:56:06: pos\n28/10/2019 21:56:14: pos\n28/10/2019 21:56:26: pos\n30/10/2019 11:28:06: tele 3232 3219\n30/10/2019 11:28:14: pos\n', '', '', '', ''),
('3', '', '', '', '28/10/2019 21:53:53: 127.0.0.1\n28/10/2019 21:53:53: To be filled by O.E.M.\n28/10/2019 21:53:53: 40-8D-5C-EF-A5-FE\n', '', '', '', '', ''),
('alex', '22/03/2016 20:50:12: a\n22/03/2016 20:54:32: a\n22/03/2016 20:57:25: a\n23/03/2016 00:15:05: a\n23/03/2016 00:16:15: a\n23/03/2016 10:58:38: oo\n23/03/2016 11:11:13: a\n23/03/2016 11:12:42: :item 11784 1\n23/03/2016 15:28:52: a\n23/03/2016 15:38:32: a\n25/03/2016 02:05:12: a\n25/03/2016 12:54:51: a\n25/03/2016 12:57:24: aa\n', '', '', '22/03/2016 20:50:05: 96.254.196.5\n22/03/2016 20:50:05: To be filled by O.E.M.\n22/03/2016 20:50:05: D4-3D-7E-97-2C-45\n22/03/2016 20:54:24: 96.254.196.5\n22/03/2016 20:54:24: To be filled by O.E.M.\n22/03/2016 20:54:24: D4-3D-7E-97-2C-45\n22/03/2016 20:57:17: 96.254.196.5\n22/03/2016 20:57:17: To be filled by O.E.M.\n22/03/2016 20:57:17: D4-3D-7E-97-2C-45\n22/03/2016 20:58:14: 96.254.196.5\n22/03/2016 20:58:14: To be filled by O.E.M.\n22/03/2016 20:58:14: D4-3D-7E-97-2C-45\n23/03/2016 00:13:28: 96.254.196.5\n23/03/2016 00:13:28: To be filled by O.E.M.\n23/03/2016 00:13:28: D4-3D-7E-97-2C-45\n23/03/2016 00:14:30: 96.254.196.5\n23/03/2016 00:14:30: To be filled by O.E.M.\n23/03/2016 00:14:30: D4-3D-7E-97-2C-45\n23/03/2016 10:57:24: 96.254.196.5\n23/03/2016 10:57:24: To be filled by O.E.M.\n23/03/2016 10:57:24: D4-3D-7E-97-2C-45\n23/03/2016 11:03:26: 96.254.196.5\n23/03/2016 11:03:26: To be filled by O.E.M.\n23/03/2016 11:03:26: D4-3D-7E-97-2C-45\n23/03/2016 11:11:08: 96.254.196.5\n23/03/2016 11:11:08: To be filled by O.E.M.\n23/03/2016 11:11:08: D4-3D-7E-97-2C-45\n23/03/2016 15:13:48: 96.254.196.5\n23/03/2016 15:13:48: To be filled by O.E.M.\n23/03/2016 15:13:48: D4-3D-7E-97-2C-45\n23/03/2016 15:58:08: 96.254.196.5\n23/03/2016 15:58:08: To be filled by O.E.M.\n23/03/2016 15:58:08: D4-3D-7E-97-2C-45\n25/03/2016 02:01:15: 96.254.196.5\n25/03/2016 02:01:15: To be filled by O.E.M.\n25/03/2016 02:01:15: D4-3D-7E-97-2C-45\n25/03/2016 02:05:02: 96.254.196.5\n25/03/2016 02:05:02: To be filled by O.E.M.\n25/03/2016 02:05:02: D4-3D-7E-97-2C-45\n25/03/2016 02:06:29: 96.254.196.5\n25/03/2016 02:06:29: To be filled by O.E.M.\n25/03/2016 02:06:29: D4-3D-7E-97-2C-45\n25/03/2016 10:05:04: 96.254.196.5\n25/03/2016 10:05:04: To be filled by O.E.M.\n25/03/2016 10:05:04: D4-3D-7E-97-2C-45\n25/03/2016 12:53:49: 96.254.196.5\n25/03/2016 12:53:49: To be filled by O.E.M.\n25/03/2016 12:53:49: D4-3D-7E-97-2C-45\n25/03/2016 12:57:18: 96.254.196.5\n25/03/2016 12:57:18: To be filled by O.E.M.\n25/03/2016 12:57:18: D4-3D-7E-97-2C-45\n25/03/2016 16:59:34: 96.254.196.5\n25/03/2016 16:59:34: To be filled by O.E.M.\n25/03/2016 16:59:34: D4-3D-7E-97-2C-45\n25/03/2016 19:52:32: 96.254.196.5\n25/03/2016 19:52:32: To be filled by O.E.M.\n25/03/2016 19:52:32: D4-3D-7E-97-2C-45\n', '22/03/2016 20:58:18: master\n22/03/2016 20:58:30: item 995 2147000000\n22/03/2016 20:58:34: item 4152 100\n22/03/2016 20:58:38: item 6585 5\n22/03/2016 20:58:41: item 7462 5\n22/03/2016 20:58:55: item 6570 10\n23/03/2016 11:04:21: item 4151 1\n23/03/2016 11:06:45: to 1241 1250 0\n23/03/2016 11:07:27: to 1303 1327 0\n23/03/2016 11:11:17: tele 1241 1250\n23/03/2016 11:11:57: tele 2205 3055\n23/03/2016 11:12:46: item 11785 1\n23/03/2016 11:12:54: item 861 1\n23/03/2016 11:13:00: item 892 1000\n23/03/2016 11:14:24: tele 2444 9825\n23/03/2016 11:15:28: tele 3736 5809\n23/03/2016 11:17:27: tele 1470 3687\n23/03/2016 11:17:48: tele 3222 3222\n23/03/2016 11:18:18: item 14666 1\n23/03/2016 11:18:37: item 14664 1\n23/03/2016 11:19:13: item 995 10000000\n23/03/2016 11:19:21: item 560 10000\n23/03/2016 11:19:25: item 561 10000\n23/03/2016 11:19:28: item 554 10000\n23/03/2016 11:19:37: item 562 10000\n23/03/2016 11:20:38: item 14839\n23/03/2016 11:20:49: item 14829 1\n23/03/2016 11:21:32: item 14636 1\n23/03/2016 11:21:42: item 14662 1\n23/03/2016 11:21:59: item 14671 1\n23/03/2016 11:22:53: item 14752 1\n23/03/2016 11:23:25: item 14726\n23/03/2016 11:23:45: item 14767\n23/03/2016 11:23:54: tele 0,50,50,62,28\n23/03/2016 11:23:56: tele 0,51,50,10,18\n23/03/2016 11:23:57: tele 0,51,50,18,6\n23/03/2016 11:23:58: tele 0,51,49,26,58\n23/03/2016 11:23:59: tele 0,51,49,30,44\n23/03/2016 11:24:01: tele 0,51,49,28,34\n23/03/2016 15:14:09: item 4151 1\n23/03/2016 15:14:13: item 14958 1\n23/03/2016 15:16:03: item 14821 1\n23/03/2016 15:16:27: item 14822 1\n23/03/2016 15:16:38: item 14827 1\n23/03/2016 15:17:16: item 14748 1\n23/03/2016 15:17:27: item 14749 1\n23/03/2016 15:19:05: item 14845 1\n23/03/2016 15:19:08: item 14846 1\n23/03/2016 15:19:18: item 14847 1\n23/03/2016 15:19:34: item 14851 1\n23/03/2016 15:19:39: item 14853 1\n23/03/2016 15:19:55: item 14850 1\n23/03/2016 15:20:09: item 14693 1\n23/03/2016 15:20:38: item 14638 1\n23/03/2016 15:20:41: item 14639 1\n23/03/2016 15:20:45: item 14640 1\n23/03/2016 15:20:48: item 14641 1\n23/03/2016 15:20:51: item 14642 1\n23/03/2016 15:20:54: item 14643 1\n23/03/2016 15:20:58: item 14644 1\n23/03/2016 15:21:02: item 14645 1\n23/03/2016 15:21:06: item 14646 1\n23/03/2016 15:21:12: item 14647 1\n23/03/2016 15:21:15: item 14648 1\n23/03/2016 15:21:18: item 14649 1\n23/03/2016 15:21:21: item 14650 1\n23/03/2016 15:21:25: item 14651 1\n23/03/2016 15:21:34: item 14652 1\n23/03/2016 15:21:38: item 14653 1\n23/03/2016 15:21:41: item 14654 1\n23/03/2016 15:21:45: item 14655 1\n23/03/2016 15:21:48: item 14656 1\n23/03/2016 15:23:21: empty\n23/03/2016 15:23:29: item 14657 1\n23/03/2016 15:23:33: item 14658 1\n23/03/2016 15:23:37: item 14659 1\n23/03/2016 15:23:45: item 14660 1\n23/03/2016 15:23:47: item 14661 1\n23/03/2016 15:24:00: item 14669 1\n23/03/2016 15:24:06: item 14673 1\n23/03/2016 15:24:43: item 14674 5\n23/03/2016 15:24:46: item 14675 1\n23/03/2016 15:24:49: item 14683 1\n23/03/2016 15:25:07: item 14810 1\n23/03/2016 15:25:14: item 14808 1\n23/03/2016 15:25:18: item 14809 1\n23/03/2016 15:25:27: item 14818 1\n23/03/2016 15:25:37: item 14823 1\n23/03/2016 15:25:44: item 14828 1\n23/03/2016 15:30:22: item 14896 1\n23/03/2016 15:30:28: item 14897 1\n23/03/2016 15:30:36: item 14898 1\n23/03/2016 15:30:38: item 14899 1\n23/03/2016 15:30:49: item 14900 1\n23/03/2016 15:40:36: item 14830 1\n25/03/2016 02:07:06: item 14962 1\n25/03/2016 02:07:10: item 14963 1\n25/03/2016 02:07:13: item 14964 1\n25/03/2016 02:07:19: item 14965 1\n25/03/2016 02:07:26: item 14966 1\n25/03/2016 02:07:35: item 14969 1\n25/03/2016 02:07:40: item 14970 1\n25/03/2016 02:07:43: item 14971 1\n25/03/2016 02:07:48: item 14972 1\n25/03/2016 02:07:51: item 14972 1\n25/03/2016 02:07:55: item 14972 100\n25/03/2016 02:08:00: item 14977 1\n25/03/2016 02:08:09: item 14980 1\n25/03/2016 02:08:15: item 14983 1\n25/03/2016 02:08:18: item 14984 1\n25/03/2016 02:08:29: item 14985 1\n25/03/2016 02:08:34: item 14988 1\n25/03/2016 02:08:37: item 14989 1\n25/03/2016 02:08:40: item 14990 1\n25/03/2016 02:08:42: item 14991 1\n25/03/2016 02:08:45: item 14992 1\n25/03/2016 02:08:48: item 14993 1\n25/03/2016 02:08:59: item 14994 1\n25/03/2016 02:09:03: item 14994 100\n25/03/2016 02:09:11: item 15007 2\n25/03/2016 02:09:15: item 15007 100\n25/03/2016 02:09:22: item 15010 1000\n25/03/2016 02:12:25: item 15015 50\n25/03/2016 02:12:43: item 15008 2\n25/03/2016 02:13:58: item 15004 1\n25/03/2016 02:14:00: item 15005 1\n25/03/2016 02:14:05: item 15006 1\n25/03/2016 02:14:14: item 15016 3\n25/03/2016 02:14:20: item 15018 3\n25/03/2016 02:14:23: item 15020 2\n25/03/2016 02:15:06: item 14936 1\n25/03/2016 02:15:37: item 14820 1\n25/03/2016 02:16:12: item 14792 1\n25/03/2016 02:16:15: item 14793 1\n25/03/2016 02:16:22: item 14788 3\n25/03/2016 02:16:34: item 14773 2\n25/03/2016 02:16:40: item 14774 2\n25/03/2016 02:16:53: item 14768 100\n25/03/2016 02:17:05: item 14767 3\n25/03/2016 02:17:09: item 14761 1\n25/03/2016 02:17:11: item 14762 1\n25/03/2016 02:17:14: item 14763 1\n25/03/2016 02:17:18: ultimatearmour\n25/03/2016 02:18:15: item 14673 2\n25/03/2016 02:19:20: item 14484 5\n25/03/2016 02:19:31: item 7462 5\n25/03/2016 02:19:37: item 6585 6\n25/03/2016 02:21:14: item 13734 1\n25/03/2016 02:21:19: item 13738 1\n25/03/2016 02:21:24: item 13740 1\n25/03/2016 02:21:27: item 14742 1\n25/03/2016 02:21:33: item 13742 1\n25/03/2016 02:21:40: item 13744 1\n25/03/2016 02:21:51: item 13736 1\n25/03/2016 02:22:27: item 13661 1\n25/03/2016 02:22:45: empty\n25/03/2016 12:57:26: npc 1 1\n25/03/2016 12:57:38: npc 8675 1\n25/03/2016 12:57:54: npc 8676 1\n25/03/2016 12:58:05: npc 8677 1\n25/03/2016 12:58:19: npc 8679 1\n25/03/2016 12:58:37: npc 8680 1\n25/03/2016 12:58:50: npc 8681 1\n25/03/2016 12:59:12: npc 8682 1\n25/03/2016 12:59:35: npc 8683 1\n25/03/2016 12:59:46: npc 8484 1\n25/03/2016 12:59:56: npc 8684 1\n25/03/2016 13:00:04: npc 8685 1\n25/03/2016 13:00:24: npc 8686 1\n25/03/2016 13:00:41: npc 8687 1\n25/03/2016 13:00:57: npc 8688 1\n25/03/2016 17:01:48: oskill uim alex 0 99\n25/03/2016 17:01:57: oskill 0 97 uim alex\n25/03/2016 17:03:22: oskill 0 95 uim_alex\n25/03/2016 17:03:52: oskill 1 93 uim_alex\n25/03/2016 17:05:41: oskill 2 97 uim_alex\n25/03/2016 17:06:05: oskill 3 95 uim_alex\n25/03/2016 19:52:53: npc 8679 1\n25/03/2016 19:53:14: npc 8688 1\n', '', '', '22/03/2016 20:58:35: Large networth increase - [incr=2188350000, old=140222, cur=2188490222].\n22/03/2016 20:59:05: Large networth increase - [incr=2224040620, old=140222, cur=2224180842].\n22/03/2016 20:59:23: Large networth increase - [incr=2224040620, old=140222, cur=2224180842].\n23/03/2016 00:13:39: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:13:49: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:14:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:15:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:15:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:16:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:16:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:17:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:17:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:18:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:18:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:19:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:19:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:20:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:20:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:21:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:21:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:22:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:22:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:23:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:23:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:24:12: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:24:42: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 00:25:04: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 10:57:39: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 10:58:09: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 10:58:39: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 10:58:49: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 11:03:47: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 11:04:17: Large networth increase - [incr=4294967296, old=-2070786454, cur=2224180842].\n23/03/2016 11:04:47: Large networth increase - [incr=4295428159, old=-2070786454, cur=2224641705].\n23/03/2016 11:05:17: Large networth increase - [incr=4295428159, old=-2070786454, cur=2224641705].\n23/03/2016 11:05:47: Large networth increase - [incr=4295428159, old=-2070786454, cur=2224641705].\n23/03/2016 11:06:17: Large networth increase - [incr=4295428159, old=-2070786454, cur=2224641705].\n23/03/2016 11:06:47: Large networth increase - [incr=4295428159, old=-2070786454, cur=2224641705].\n23/03/2016 11:07:17: Large networth increase - [incr=4295428159, old=-2070786454, cur=2224641705].\n23/03/2016 11:07:47: Large networth increase - [incr=4295428159, old=-2070786454, cur=2224641705].\n23/03/2016 11:08:17: Large networth increase - [incr=4295428159, old=-2070786454, cur=2224641705].\n23/03/2016 11:08:42: Large networth increase - [incr=4295428159, old=-2070786454, cur=2224641705].\n23/03/2016 11:11:30: Large networth increase - [incr=4294967296, old=-2070325591, cur=2224641705].\n23/03/2016 11:12:00: Large networth increase - [incr=4294967296, old=-2070325591, cur=2224641705].\n23/03/2016 11:12:30: Large networth increase - [incr=4294967296, old=-2070325591, cur=2224641705].\n23/03/2016 11:13:00: Large networth increase - [incr=4294972896, old=-2070325591, cur=2224647305].\n23/03/2016 11:13:30: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:14:00: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:14:30: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:15:00: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:15:30: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:16:00: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:16:30: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:17:00: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:17:30: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:18:00: Large networth increase - [incr=4295478816, old=-2070325591, cur=2225153225].\n23/03/2016 11:18:30: Large networth increase - [incr=4295547316, old=-2070325591, cur=2225221725].\n23/03/2016 11:19:00: Large networth increase - [incr=4295615816, old=-2070325591, cur=2225290225].\n23/03/2016 11:19:30: Large networth increase - [incr=4313525816, old=-2070325591, cur=2243200225].\n23/03/2016 11:20:00: Large networth increase - [incr=4313644316, old=-2070325591, cur=2243318725].\n23/03/2016 11:20:30: Large networth increase - [incr=4313644316, old=-2070325591, cur=2243318725].\n23/03/2016 11:21:00: Large networth increase - [incr=4313842316, old=-2070325591, cur=2243516725].\n23/03/2016 11:21:30: Large networth increase - [incr=4313842316, old=-2070325591, cur=2243516725].\n23/03/2016 11:22:00: Large networth increase - [incr=4315132966, old=-2070325591, cur=2244807375].\n23/03/2016 11:22:30: Large networth increase - [incr=4315132966, old=-2070325591, cur=2244807375].\n23/03/2016 11:23:00: Large networth increase - [incr=4315422766, old=-2070325591, cur=2245097175].\n23/03/2016 11:23:30: Large networth increase - [incr=4316422771, old=-2070325591, cur=2246097180].\n23/03/2016 11:24:00: Large networth increase - [incr=4316490778, old=-2070325591, cur=2246165187].\n23/03/2016 11:24:30: Large networth increase - [incr=4316522194, old=-2070325591, cur=2246196603].\n23/03/2016 11:25:00: Large networth increase - [incr=4316522194, old=-2070325591, cur=2246196603].\n23/03/2016 11:25:30: Large networth increase - [incr=4316522194, old=-2070325591, cur=2246196603].\n23/03/2016 11:26:00: Large networth increase - [incr=4316522194, old=-2070325591, cur=2246196603].\n23/03/2016 11:26:18: Large networth increase - [incr=4316522194, old=-2070325591, cur=2246196603].\n23/03/2016 15:14:00: Large networth increase - [incr=4294967296, old=-2048770693, cur=2246196603].\n23/03/2016 15:14:30: Large networth increase - [incr=4295548159, old=-2048770693, cur=2246777466].\n23/03/2016 15:15:00: Large networth increase - [incr=4295548159, old=-2048770693, cur=2246777466].\n23/03/2016 15:15:30: Large networth increase - [incr=4295548159, old=-2048770693, cur=2246777466].\n23/03/2016 15:16:00: Large networth increase - [incr=4295548159, old=-2048770693, cur=2246777466].\n23/03/2016 15:16:30: Large networth increase - [incr=4295548161, old=-2048770693, cur=2246777468].\n23/03/2016 15:17:00: Large networth increase - [incr=4295548162, old=-2048770693, cur=2246777469].\n23/03/2016 15:17:30: Large networth increase - [incr=4295788164, old=-2048770693, cur=2247017471].\n23/03/2016 15:18:00: Large networth increase - [incr=4295779392, old=-2048770693, cur=2247008699].\n23/03/2016 15:18:30: Large networth increase - [incr=4295779392, old=-2048770693, cur=2247008699].\n23/03/2016 15:19:00: Large networth increase - [incr=4295779392, old=-2048770693, cur=2247008699].\n23/03/2016 15:19:13: Large networth increase - [incr=4295878392, old=-2048770693, cur=2247107699].\n23/03/2016 15:19:30: Large networth increase - [incr=4295977392, old=-2048770693, cur=2247206699].\n23/03/2016 15:20:00: Large networth increase - [incr=4296642243, old=-2048770693, cur=2247871550].\n23/03/2016 15:20:30: Large networth increase - [incr=4296642243, old=-2048770693, cur=2247871550].\n23/03/2016 15:21:00: Large networth increase - [incr=4296642250, old=-2048770693, cur=2247871557].\n23/03/2016 15:21:30: Large networth increase - [incr=4296642257, old=-2048770693, cur=2247871564].\n23/03/2016 15:22:00: Large networth increase - [incr=4296642262, old=-2048770693, cur=2247871569].\n23/03/2016 15:22:15: Large networth increase - [incr=4287120909, old=-2048770693, cur=2238350216].\n23/03/2016 15:22:30: Large networth increase - [incr=4287120909, old=-2048770693, cur=2238350216].\n23/03/2016 15:23:00: Large networth increase - [incr=4287120967, old=-2048770693, cur=2238350274].\n23/03/2016 15:23:30: Large networth increase - [incr=4296602716, old=-2048770693, cur=2247832023].\n23/03/2016 15:24:00: Large networth increase - [incr=4296700670, old=-2048770693, cur=2247929977].\n23/03/2016 15:24:30: Large networth increase - [incr=4296700680, old=-2048770693, cur=2247929987].\n23/03/2016 15:25:00: Large networth increase - [incr=4297303180, old=-2048770693, cur=2248532487].\n23/03/2016 15:25:30: Large networth increase - [incr=4297303381, old=-2048770693, cur=2248532688].\n23/03/2016 15:26:00: Large networth increase - [incr=4297303383, old=-2048770693, cur=2248532690].\n23/03/2016 15:26:30: Large networth increase - [incr=4297300383, old=-2048770693, cur=2248529690].\n23/03/2016 15:27:00: Large networth increase - [incr=4297300383, old=-2048770693, cur=2248529690].\n23/03/2016 15:27:30: Large networth increase - [incr=4297300383, old=-2048770693, cur=2248529690].\n23/03/2016 15:28:00: Large networth increase - [incr=4297300383, old=-2048770693, cur=2248529690].\n23/03/2016 15:28:30: Large networth increase - [incr=4297300383, old=-2048770693, cur=2248529690].\n23/03/2016 15:29:00: Large networth increase - [incr=4297300383, old=-2048770693, cur=2248529690].\n23/03/2016 15:29:30: Large networth increase - [incr=4297300383, old=-2048770693, cur=2248529690].\n23/03/2016 15:30:00: Large networth increase - [incr=4297300383, old=-2048770693, cur=2248529690].\n23/03/2016 15:30:30: Large networth increase - [incr=4297300508, old=-2048770693, cur=2248529815].\n23/03/2016 15:31:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:31:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:32:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:32:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:33:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:33:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:34:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:34:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:35:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:35:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:36:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:36:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:37:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:37:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:38:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:38:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:39:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:39:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:40:00: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:40:30: Large networth increase - [incr=4297300733, old=-2048770693, cur=2248530040].\n23/03/2016 15:41:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:41:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:42:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:42:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:43:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:43:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:44:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:44:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:45:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:45:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:46:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:46:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:47:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:47:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:48:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:48:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:49:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:49:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:50:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:50:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:51:00: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:51:30: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:51:51: Large networth increase - [incr=4297300734, old=-2048770693, cur=2248530041].\n23/03/2016 15:58:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 15:58:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 15:59:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 15:59:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:00:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:00:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:01:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:01:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:02:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:02:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:03:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:03:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:04:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:04:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:05:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:05:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:06:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:06:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:07:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:07:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:08:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:08:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:09:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:09:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:10:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:10:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:11:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:11:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:12:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:12:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:13:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:13:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:14:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:14:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:15:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:15:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:16:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:16:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:17:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:17:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:18:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:18:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:19:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:19:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:20:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:20:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:21:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:21:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:22:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:22:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:23:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:23:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:24:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:24:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:25:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:25:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:26:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:26:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:27:17: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:27:47: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:28:18: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:28:48: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:29:18: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:29:48: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:30:18: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:30:48: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:31:18: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:31:48: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:32:18: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:32:48: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:33:18: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:33:48: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:34:18: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n23/03/2016 16:34:24: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n25/03/2016 02:01:34: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n25/03/2016 02:02:04: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n25/03/2016 02:02:15: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n25/03/2016 02:05:14: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n25/03/2016 02:05:44: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n25/03/2016 02:05:49: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n25/03/2016 02:06:48: Large networth increase - [incr=4294967296, old=-1981014433, cur=2313952863].\n25/03/2016 02:07:18: Large networth increase - [incr=4295188297, old=-1981014433, cur=2314173864].\n25/03/2016 02:07:48: Large networth increase - [incr=4295692860, old=-1981014433, cur=2314678427].\n25/03/2016 02:08:18: Large networth increase - [incr=4296962366, old=-1981014433, cur=2315947933].\n25/03/2016 02:08:48: Large networth increase - [incr=4297532566, old=-1981014433, cur=2316518133].\n25/03/2016 02:09:18: Large networth increase - [incr=4297538787, old=-1981014433, cur=2316524354].\n25/03/2016 02:09:48: Large networth increase - [incr=4297558787, old=-1981014433, cur=2316544354].\n25/03/2016 02:10:18: Large networth increase - [incr=4297558787, old=-1981014433, cur=2316544354].\n25/03/2016 02:10:48: Large networth increase - [incr=4297558787, old=-1981014433, cur=2316544354].\n25/03/2016 02:11:18: Large networth increase - [incr=4297558787, old=-1981014433, cur=2316544354].\n25/03/2016 02:11:48: Large networth increase - [incr=4297558787, old=-1981014433, cur=2316544354].\n25/03/2016 02:12:18: Large networth increase - [incr=4297558787, old=-1981014433, cur=2316544354].\n25/03/2016 02:12:48: Large networth increase - [incr=4297559989, old=-1981014433, cur=2316545556].\n25/03/2016 02:13:18: Large networth increase - [incr=4297559989, old=-1981014433, cur=2316545556].\n25/03/2016 02:13:48: Large networth increase - [incr=4297559989, old=-1981014433, cur=2316545556].\n25/03/2016 02:14:18: Large networth increase - [incr=4297889993, old=-1981014433, cur=2316875560].\n25/03/2016 02:14:48: Large networth increase - [incr=4298196013, old=-1981014433, cur=2317181580].\n25/03/2016 02:15:18: Large networth increase - [incr=4298316013, old=-1981014433, cur=2317301580].\n25/03/2016 02:15:48: Large networth increase - [incr=4298316014, old=-1981014433, cur=2317301581].\n25/03/2016 02:16:18: Large networth increase - [incr=4298371014, old=-1981014433, cur=2317356581].\n25/03/2016 02:16:48: Large networth increase - [incr=4298482824, old=-1981014433, cur=2317468391].\n25/03/2016 02:17:18: Large networth increase - [incr=4298688851, old=-1981014433, cur=2317674418].\n25/03/2016 02:17:48: Large networth increase - [incr=4298688851, old=-1981014433, cur=2317674418].\n25/03/2016 02:18:18: Large networth increase - [incr=4298688871, old=-1981014433, cur=2317674438].\n25/03/2016 02:18:48: Large networth increase - [incr=4298688871, old=-1981014433, cur=2317674438].\n25/03/2016 02:19:18: Large networth increase - [incr=4298688871, old=-1981014433, cur=2317674438].\n25/03/2016 02:19:48: Large networth increase - [incr=4335351555, old=-1981014433, cur=2354337122].\n25/03/2016 02:20:18: Large networth increase - [incr=4335351555, old=-1981014433, cur=2354337122].\n25/03/2016 02:20:48: Large networth increase - [incr=4335351555, old=-1981014433, cur=2354337122].\n25/03/2016 02:21:18: Large networth increase - [incr=4335421555, old=-1981014433, cur=2354407122].\n25/03/2016 02:21:48: Large networth increase - [incr=4343466555, old=-1981014433, cur=2362452122].\n25/03/2016 02:22:18: Large networth increase - [incr=4344866555, old=-1981014433, cur=2363852122].\n25/03/2016 02:22:48: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:23:18: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:23:48: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:24:18: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:24:48: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:25:18: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:25:48: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:26:18: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:26:48: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:27:18: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:27:48: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:28:18: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:28:48: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 02:29:20: Large networth increase - [incr=4335348502, old=-1981014433, cur=2354334069].\n25/03/2016 10:05:16: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:05:46: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:06:16: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:06:46: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:07:16: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:07:46: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:08:16: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:08:46: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:09:16: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:09:46: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:10:16: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:10:46: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:11:16: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:11:46: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:12:16: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:12:46: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 10:13:06: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:54:20: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:54:50: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:55:20: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:55:50: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:56:20: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:56:50: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:56:53: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:57:36: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:58:06: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:58:36: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:59:06: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 12:59:36: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 13:00:06: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 13:00:36: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 13:01:06: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 13:01:36: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 13:02:06: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 13:02:36: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 13:03:06: Large networth increase - [incr=4294967295, old=-1940633227, cur=2354334068].\n25/03/2016 13:03:37: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 16:59:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:00:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:00:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:01:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:01:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:02:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:02:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:03:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:03:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:04:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:04:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:05:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:05:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:06:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:06:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:07:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:07:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:08:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:08:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:09:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:09:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:10:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:10:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:11:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:11:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:12:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:12:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:13:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:13:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:14:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:14:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:15:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:15:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:16:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:16:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:17:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:17:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:18:17: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:18:47: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 17:19:08: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:52:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:53:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:53:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:54:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:54:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:55:14: Large networth increase - [incr=4294967295, old=-1940633227, cur=2354334068].\n25/03/2016 19:55:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:56:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:56:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:57:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:57:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:58:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:58:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:59:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 19:59:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:00:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:00:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:01:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:01:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:02:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:02:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:03:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:03:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:04:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:04:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:05:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:05:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:06:14: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:06:44: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n25/03/2016 20:06:56: Large networth increase - [incr=4294967296, old=-1940633227, cur=2354334069].\n', ''),
('michael', '28/10/2019 19:22:49: g\n', '', '', '28/10/2019 19:17:33: 127.0.0.1\n28/10/2019 19:17:33: To be filled by O.E.M.\n28/10/2019 19:17:33: 40-8D-5C-EF-A5-FE\n28/10/2019 19:22:41: 127.0.0.1\n28/10/2019 19:22:41: To be filled by O.E.M.\n28/10/2019 19:22:41: 40-8D-5C-EF-A5-FE\n28/10/2019 21:44:07: 127.0.0.1\n28/10/2019 21:44:07: To be filled by O.E.M.\n28/10/2019 21:44:07: 40-8D-5C-EF-A5-FE\n28/10/2019 21:46:53: 127.0.0.1\n28/10/2019 21:46:53: To be filled by O.E.M.\n28/10/2019 21:46:53: 40-8D-5C-EF-A5-FE\n28/10/2019 21:51:58: 127.0.0.1\n28/10/2019 21:51:58: To be filled by O.E.M.\n28/10/2019 21:51:58: 40-8D-5C-EF-A5-FE\n30/10/2019 11:30:30: 127.0.0.1\n30/10/2019 11:30:30: To be filled by O.E.M.\n30/10/2019 11:30:30: 40-8D-5C-EF-A5-FE\n', '28/10/2019 19:18:10: fpson\n28/10/2019 19:22:52: master\n28/10/2019 19:23:01: item 4151 1\n28/10/2019 19:24:02: item 892 100\n28/10/2019 19:24:04: item 861\n28/10/2019 19:24:48: runes\n30/10/2019 13:26:40: item weeds 10\n30/10/2019 13:26:47: bank\n30/10/2019 13:42:06: runes\n', '', '', '', ''),
('michael2', '', '', '', '28/10/2019 21:52:22: 127.0.0.1\n28/10/2019 21:52:22: To be filled by O.E.M.\n28/10/2019 21:52:22: 40-8D-5C-EF-A5-FE\n28/10/2019 21:53:25: 127.0.0.1\n28/10/2019 21:53:25: To be filled by O.E.M.\n28/10/2019 21:53:25: 40-8D-5C-EF-A5-FE\n', '', '', '', '', '');
INSERT INTO `player_logs` (`username`, `public_chat`, `private_chat`, `clan_chat`, `address_log`, `command_log`, `trade_log`, `ge_log`, `duplication_log`, `duel_log`) VALUES
('uim_alex', '22/03/2016 20:43:13: a\n23/03/2016 00:08:13: a\n23/03/2016 11:28:55: oo\n23/03/2016 11:29:00: cant even bank niqqa\n23/03/2016 13:43:48: yea\n24/03/2016 23:04:13: a\n25/03/2016 17:13:25: oo\n', '', '', '22/03/2016 20:43:03: 96.254.196.5\n22/03/2016 20:43:03: To be filled by O.E.M.\n22/03/2016 20:43:03: D4-3D-7E-97-2C-45\n22/03/2016 21:00:30: 96.254.196.5\n22/03/2016 21:00:30: To be filled by O.E.M.\n22/03/2016 21:00:30: D4-3D-7E-97-2C-45\n22/03/2016 21:46:43: 96.254.196.5\n22/03/2016 21:46:43: To be filled by O.E.M.\n22/03/2016 21:46:43: D4-3D-7E-97-2C-45\n22/03/2016 22:02:01: 96.254.196.5\n22/03/2016 22:02:01: To be filled by O.E.M.\n22/03/2016 22:02:01: D4-3D-7E-97-2C-45\n23/03/2016 00:06:22: 96.254.196.5\n23/03/2016 00:06:22: To be filled by O.E.M.\n23/03/2016 00:06:22: D4-3D-7E-97-2C-45\n23/03/2016 00:07:33: 96.254.196.5\n23/03/2016 00:07:33: To be filled by O.E.M.\n23/03/2016 00:07:33: D4-3D-7E-97-2C-45\n23/03/2016 08:16:12: 96.254.196.5\n23/03/2016 08:16:12: To be filled by O.E.M.\n23/03/2016 08:16:12: D4-3D-7E-97-2C-45\n23/03/2016 11:28:37: 96.254.196.5\n23/03/2016 11:28:37: To be filled by O.E.M.\n23/03/2016 11:28:37: D4-3D-7E-97-2C-45\n23/03/2016 13:24:24: 96.254.196.5\n23/03/2016 13:24:24: To be filled by O.E.M.\n23/03/2016 13:24:24: D4-3D-7E-97-2C-45\n24/03/2016 23:00:51: 96.254.196.5\n24/03/2016 23:00:51: To be filled by O.E.M.\n24/03/2016 23:00:51: D4-3D-7E-97-2C-45\n25/03/2016 16:57:31: 96.254.196.5\n25/03/2016 16:57:31: To be filled by O.E.M.\n25/03/2016 16:57:31: D4-3D-7E-97-2C-45\n', '22/03/2016 22:02:12: ultimatearmour\n', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `punishments`
--

CREATE TABLE `punishments` (
  `address` varchar(100) NOT NULL DEFAULT '',
  `type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `security`
--

CREATE TABLE `security` (
  `id` int(11) UNSIGNED NOT NULL,
  `ip` longtext NOT NULL,
  `type` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `security`
--

INSERT INTO `security` (`id`, `ip`, `type`, `timestamp`) VALUES
(1, '127.0.0.1', 1, '2016-03-23 00:31:58'),
(2, '127.0.0.1', 2, '2016-03-23 00:32:14'),
(3, '127.0.0.1', 2, '2016-03-23 00:32:20'),
(4, '127.0.0.1', 2, '2016-03-23 00:32:26'),
(5, '127.0.0.1', 1, '2016-03-23 00:32:35'),
(6, '127.0.0.1', 1, '2016-03-23 00:32:40'),
(7, '127.0.0.1', 1, '2016-03-23 00:32:40'),
(8, '127.0.0.1', 1, '2016-03-23 00:32:49'),
(9, '127.0.0.1', 1, '2016-03-23 00:32:51'),
(10, '127.0.0.1', 1, '2016-03-23 00:32:51'),
(11, '127.0.0.1', 1, '2016-03-23 00:33:00'),
(12, '192.168.1.4', 1, '2016-03-23 00:38:03'),
(13, '192.168.1.4', 1, '2016-03-23 00:38:55'),
(14, '192.168.1.4', 1, '2016-03-23 00:38:57'),
(15, '192.168.1.4', 1, '2016-03-23 00:39:38'),
(16, '192.168.1.4', 2, '2016-03-23 00:39:42'),
(17, '192.168.1.4', 1, '2016-03-23 00:39:44'),
(18, '192.168.1.4', 1, '2016-03-23 00:39:46'),
(19, '192.168.1.4', 2, '2016-03-23 00:39:54'),
(20, '192.168.1.4', 2, '2016-03-23 00:39:57'),
(21, '192.168.1.4', 1, '2016-03-23 00:42:22'),
(22, '192.168.1.4', 1, '2016-03-23 00:43:20'),
(23, '192.168.1.4', 2, '2016-03-23 00:43:21'),
(24, '192.168.1.4', 2, '2016-03-23 00:43:29'),
(25, '192.168.1.4', 2, '2016-03-23 00:43:31'),
(26, '192.168.1.4', 1, '2016-03-23 00:54:42'),
(27, '192.168.1.4', 1, '2016-03-23 00:59:33'),
(28, '192.168.1.4', 2, '2016-03-23 00:59:35'),
(29, '192.168.1.4', 1, '2016-03-23 00:59:39'),
(30, '192.168.1.4', 1, '2016-03-23 00:59:41'),
(31, '192.168.1.4', 2, '2016-03-23 00:59:47'),
(32, '192.168.1.4', 2, '2016-03-23 00:59:50'),
(33, '192.168.1.4', 2, '2016-03-23 00:59:55'),
(34, '192.168.1.4', 2, '2016-03-23 00:59:56'),
(35, '192.168.1.4', 2, '2016-03-23 01:00:03'),
(36, '192.168.1.4', 1, '2016-03-23 01:45:19'),
(37, '192.168.1.4', 2, '2016-03-23 01:45:22'),
(38, '192.168.1.4', 2, '2016-03-23 01:45:24'),
(39, '192.168.1.4', 2, '2016-03-23 01:45:25'),
(40, '192.168.1.4', 2, '2016-03-23 01:46:55'),
(41, '192.168.1.4', 1, '2016-03-23 01:47:04'),
(42, '186.222.2.208', 1, '2016-03-23 01:47:04'),
(43, '192.168.1.4', 2, '2016-03-23 01:49:28'),
(44, '184.105.247.195', 1, '2016-03-23 02:36:41'),
(45, '127.0.0.1', 1, '2016-03-23 02:58:42'),
(46, '127.0.0.1', 1, '2016-03-23 02:58:44'),
(47, '127.0.0.1', 2, '2016-03-23 02:58:47'),
(48, '127.0.0.1', 2, '2016-03-23 02:58:54'),
(49, '127.0.0.1', 2, '2016-03-23 02:59:01'),
(50, '127.0.0.1', 2, '2016-03-23 02:59:03'),
(51, '127.0.0.1', 2, '2016-03-23 02:59:07'),
(52, '127.0.0.1', 2, '2016-03-23 03:10:16'),
(53, '127.0.0.1', 2, '2016-03-23 03:10:18'),
(54, '127.0.0.1', 2, '2016-03-23 03:10:19'),
(55, '192.168.1.4', 1, '2016-03-23 04:06:09'),
(56, '192.168.1.4', 1, '2016-03-23 04:06:11'),
(57, '192.168.1.4', 2, '2016-03-23 04:06:13'),
(58, '127.0.0.1', 1, '2016-03-23 12:34:21'),
(59, '127.0.0.1', 1, '2016-03-23 12:34:25'),
(60, '127.0.0.1', 2, '2016-03-23 12:34:28'),
(61, '127.0.0.1', 1, '2016-03-23 14:59:05'),
(62, '127.0.0.1', 1, '2016-03-23 14:59:11'),
(63, '127.0.0.1', 2, '2016-03-23 14:59:14'),
(64, '127.0.0.1', 2, '2016-03-23 14:59:16'),
(65, '127.0.0.1', 2, '2016-03-23 14:59:32'),
(66, '127.0.0.1', 2, '2016-03-23 14:59:37'),
(67, '127.0.0.1', 2, '2016-03-23 14:59:38'),
(68, '127.0.0.1', 2, '2016-03-23 14:59:44'),
(69, '127.0.0.1', 2, '2016-03-23 14:59:45'),
(70, '127.0.0.1', 2, '2016-03-23 14:59:57'),
(71, '127.0.0.1', 2, '2016-03-23 14:59:59'),
(72, '127.0.0.1', 1, '2016-03-23 15:00:59'),
(73, '127.0.0.1', 1, '2016-03-23 15:00:59'),
(74, '127.0.0.1', 1, '2016-03-23 15:01:07'),
(75, '127.0.0.1', 1, '2016-03-23 15:01:09'),
(76, '127.0.0.1', 1, '2016-03-23 15:01:20'),
(77, '127.0.0.1', 1, '2016-03-23 15:01:25'),
(78, '127.0.0.1', 1, '2016-03-23 15:01:25'),
(79, '127.0.0.1', 1, '2016-03-23 15:01:28'),
(80, '127.0.0.1', 1, '2016-03-23 15:01:28'),
(81, '127.0.0.1', 1, '2016-03-23 15:01:30'),
(82, '127.0.0.1', 2, '2016-03-23 15:01:42'),
(83, '127.0.0.1', 1, '2016-03-23 15:34:51'),
(84, '127.0.0.1', 2, '2016-03-23 15:34:53'),
(85, '127.0.0.1', 2, '2016-03-23 15:34:55'),
(86, '95.110.143.69', 1, '2016-03-23 20:15:46'),
(87, '66.249.66.130', 1, '2016-03-25 03:34:11'),
(88, '66.249.66.130', 1, '2016-03-25 03:36:48'),
(89, '66.249.66.130', 1, '2016-03-25 03:36:49'),
(90, '66.249.66.190', 1, '2016-03-25 03:37:22'),
(91, '66.249.66.190', 1, '2016-03-25 03:37:22'),
(92, '66.249.66.130', 1, '2016-03-25 03:38:20'),
(93, '66.249.66.130', 1, '2016-03-25 03:38:20'),
(94, '66.249.66.190', 1, '2016-03-25 03:39:12'),
(95, '179.96.23.6', 1, '2016-03-25 06:24:54'),
(96, '180.76.15.156', 1, '2016-03-25 14:22:58'),
(97, '178.64.31.84', 1, '2016-03-25 16:05:46'),
(98, '54.208.125.234', 1, '2016-03-25 17:45:37'),
(99, '54.208.125.234', 1, '2016-03-25 17:45:38'),
(100, '54.208.125.234', 1, '2016-03-25 17:45:40'),
(101, '187.85.96.60', 1, '2016-03-25 18:57:34'),
(102, '76.195.103.3', 1, '2016-03-25 19:39:32'),
(103, '127.0.0.1', 1, '2016-03-25 20:44:36'),
(104, '192.168.1.4', 1, '2016-03-25 20:44:45'),
(105, '192.168.1.4', 1, '2016-03-25 20:45:16'),
(106, '192.168.1.4', 2, '2016-03-25 20:45:18'),
(107, '192.168.1.4', 2, '2016-03-25 20:45:18'),
(108, '192.168.1.4', 2, '2016-03-25 20:55:29'),
(109, '192.168.1.4', 2, '2016-03-25 20:55:31'),
(110, '192.168.1.4', 2, '2016-03-25 20:55:33'),
(111, '192.168.1.4', 2, '2016-03-25 20:55:37'),
(112, '192.168.1.4', 2, '2016-03-25 20:55:40'),
(113, '192.168.1.4', 2, '2016-03-25 20:55:41'),
(114, '127.0.0.1', 1, '2016-03-25 21:14:41'),
(115, '127.0.0.1', 1, '2016-03-25 21:14:50'),
(116, '127.0.0.1', 2, '2016-03-25 21:14:52'),
(117, '179.215.124.123', 1, '2016-03-25 22:53:35');

-- --------------------------------------------------------

--
-- Table structure for table `sys_logs`
--

CREATE TABLE `sys_logs` (
  `id` int(11) UNSIGNED NOT NULL,
  `message` longtext NOT NULL,
  `log_type` int(2) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `IP_ADDRESS` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `validations`
--

CREATE TABLE `validations` (
  `id` int(11) UNSIGNED NOT NULL,
  `username` varchar(20) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `type` int(2) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `value` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `votes`
--

CREATE TABLE `votes` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL DEFAULT '',
  `site` varchar(50) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `voting_sites`
--

CREATE TABLE `voting_sites` (
  `name` varchar(20) NOT NULL DEFAULT 'Null',
  `wait` int(5) NOT NULL DEFAULT '12',
  `credits` int(2) NOT NULL DEFAULT '1',
  `link` varchar(500) NOT NULL DEFAULT 'http://ariosrsps.com',
  `get_command` varchar(20) NOT NULL DEFAULT '',
  `host_name` varchar(500) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `voting_sites`
--

INSERT INTO `voting_sites` (`name`, `wait`, `credits`, `link`, `get_command`, `host_name`) VALUES
('rune-server', 24, 1, 'http://ariosrsps.com', 'data', ''),
('runelocus', 12, 1, 'http://ariosrsps.com', 'usr', ''),
('top-100-arena', 12, 1, 'http://ariosrsps.com', 'postback', ''),
('topg', 12, 1, 'http://ariosrsps.com', 'p_resp', 'monitor.topg.org');

-- --------------------------------------------------------

--
-- Table structure for table `worlds`
--

CREATE TABLE `worlds` (
  `world` int(2) UNSIGNED NOT NULL,
  `ip` varchar(20) NOT NULL DEFAULT '127.0.0.1',
  `players` int(5) NOT NULL DEFAULT '0',
  `country` int(1) NOT NULL DEFAULT '0',
  `member` int(11) NOT NULL,
  `revision` int(3) NOT NULL DEFAULT '530',
  `lastResponse` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `worlds`
--

INSERT INTO `worlds` (`world`, `ip`, `players`, `country`, `member`, `revision`, `lastResponse`) VALUES
(1, '127.0.0.1', -1, 22, 1, 530, '2019-10-30 22:14:16');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dev_log`
--
ALTER TABLE `dev_log`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `highscores`
--
ALTER TABLE `highscores`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`UID`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `perks`
--
ALTER TABLE `perks`
  ADD PRIMARY KEY (`product_id`);

--
-- Indexes for table `player_logs`
--
ALTER TABLE `player_logs`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `security`
--
ALTER TABLE `security`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sys_logs`
--
ALTER TABLE `sys_logs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `validations`
--
ALTER TABLE `validations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `votes`
--
ALTER TABLE `votes`
  ADD KEY `id` (`id`);

--
-- Indexes for table `voting_sites`
--
ALTER TABLE `voting_sites`
  ADD PRIMARY KEY (`name`);

--
-- Indexes for table `worlds`
--
ALTER TABLE `worlds`
  ADD PRIMARY KEY (`world`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `dev_log`
--
ALTER TABLE `dev_log`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `highscores`
--
ALTER TABLE `highscores`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `members`
--
ALTER TABLE `members`
  MODIFY `UID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `perks`
--
ALTER TABLE `perks`
  MODIFY `product_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=74;

--
-- AUTO_INCREMENT for table `security`
--
ALTER TABLE `security`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=118;

--
-- AUTO_INCREMENT for table `sys_logs`
--
ALTER TABLE `sys_logs`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `validations`
--
ALTER TABLE `validations`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `votes`
--
ALTER TABLE `votes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

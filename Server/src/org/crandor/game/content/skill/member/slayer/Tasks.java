package org.crandor.game.content.skill.member.slayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the tasks for slayer.
 * @author 'Vexia
 */
public enum Tasks {
	ABERRANT_SPECTRES(new Task(new int[] { 1604, 1605, 1606, 1607 }, new String[] { "Aberrant spectres have an extremely potent stench that drains", "stats and life points. A nose peg, protects against the stench." }, 60, new Master[] { Master.DURADEL, Master.CHAELDAR, Master.VANNAKA, Master.NIEVE }, true, Equipment.NOSE_PEG)),
	ABYSSAL_DEMONS(new Task(new int[] { 1615, 4230, }, new String[] { "Abyssal Demons are nasty creatures to deal with, they aren't really part, ", "of this realm, and are able to move very quickly to trap their prey" }, 85, new Master[] { Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false)),
	ANKOU(new Task(new int[] { 4381, 4382, 4383 }, new String[] { "Neither skeleton nor ghost, but a combination of both." }, 1, new Master[] { Master.VANNAKA, Master.NIEVE }, true)),
	AVIANSIES(new Task(new int[] { 6245, 6243, 6235, 6232, 6244, 6246, 6233, 6241, 6238, 6237, 6240, 6242, 6239, 6234 }, new String[] { "Graceful, bird-like creature." }, 1, new Master[] { Master.NIEVE }, false)),
	BANSHEE(new Task(new int[] { 1612 }, new String[] { "Banshees use a piercing scream to shock their enemies, you'll", "need some Earmuffs to protect yourself from them." }, 15, new Master[] { Master.TURAEL, Master.CHAELDAR, Master.VANNAKA, Master.MAZCHNA }, true, Equipment.EARMUFFS)),
	BASILISKS(new Task(new int[] { 1616, 1617, 4228 }, new String[] { "A mirror shield is much neccesary when hunting these", "mad creatures." }, 40, new Master[] { Master.TURAEL, Master.MAZCHNA, Master.VANNAKA, Master.CHAELDAR }, false)),
	BATS(new Task(new int[] { 412, 78, 1005, 2482, 3711, }, new String[] { "These little creatures are incredibly quick.", "make sure you keep your eye on them at all times." }, 1, new Master[] { Master.TURAEL, Master.MAZCHNA }, false)),
	BEARS(new Task(new int[] { 106, 105, 1195, 3645, 3664, 1326, 1327 }, new String[] { "A large animal with a crunching punch." }, 1, new Master[] { Master.TURAEL, Master.MAZCHNA }, false)),
	BIRDS(new Task(new int[] { 1475, 5120, 5121, 5122, 5123, 5133, 1475, 1476, 41, 951, 1017, 1401, 1402, 2313, 2314, 2315, 1016, 1550, 147, 1180, 1754, 1755, 1756, 2252, 4570, 4571, 1911, 6114, 46, 2693, 6113, 6112, 146, 149, 150, 450, 451, 1179, 1322, 1323, 1324, 1325, 1400, 2726, 2727, 3197, 138, 48, 4373, 4374, 4535, 139, 1751, 148, 1181, 6382, 2459, 2460, 2461, 2462, 2707, 2708, 6115, 6116, 3296, 6378, 1996, 3675, 3676, 6792, 6946, 7320, 7322, 7324, 7326, 7328, 1692, 6285, 6286, 6287, 6288, 6289, 6290, 6291, 6292, 6293, 6294, 6295, 6322, 6323, 6324, 6325, 6326, 6327, 6328, 6329, 6330, 6331, 6332, }, new String[] { "irds aren't the most intelligent of creatures, but watch out for their", "sharp, stabbing beaks." }, 1, new Master[] { Master.TURAEL }, false)),
	BLACK_DEMONS(new Task(new int[] { 84, 677, 4702, 4703, 4704, 4705, }, new String[] { "Black Demons are magic creatures that are weak to magic attacks.", "They're the strongest demon and very dangerous." }, 1, new Master[] { Master.DURADEL, Master.NIEVE }, false)),
	BLACK_DRAGONS(new Task(new int[] { 54, 4673, 4674, 4675, 4676, 3376 }, new String[] { "Black dragons are the strongest dragons", "watch out for their firey breath" }, 1, new Master[] { Master.DURADEL, Master.NIEVE }, false, 40 | 80 << 16)),
	BLOODVELDS(new Task(new int[] { 1618, 1619, 6215, 7643, 7642 }, new String[] { "Bloodvelds are strange demonic creatures, they use their long rasping tongue", "to feed on just about anything they can find." }, 50, new Master[] { Master.VANNAKA, Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false)),
	BLUE_DRAGONS(new Task(new int[] { 55, 4681, 4682, 4683, 4684, 5178, 52, 4665, 4666, }, new String[] { "Blue dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their firey breath." }, 1, new Master[] { Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false)),
	BRINE_RATS(new Task(new int[] { 3707 }, new String[] { "Small little creatures they are, yet so very", "powerfull." }, 47, new Master[] { Master.VANNAKA, Master.CHAELDAR, Master.NIEVE }, false)),
	BRONZE_DRAGONS(new Task(new int[] { 1590 }, new String[] { "Bronze dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their firey breath." }, 1, new Master[] { Master.CHAELDAR }, false, 30 | 60 << 16)),
	CATABLEPONS(new Task(new int[] { 4397, 4398, 4399, }, new String[] { "They use the magic spell Weaken to drain up to 15% of their", "opponent's maximum Strength level." }, 1, new Master[] { Master.MAZCHNA }, false)),
	CAVE_BUG(new Task(new int[] { 1832, 5750, }, new String[] { "It regenerates life points quickly and seems to be a good", "herblore monster." }, 7, new Master[] { Master.TURAEL }, false)),
	CAVE_CRAWLERS(new Task(new int[] { 1600, 1601, 1602, 1603, }, new String[] { "The poisonous parts of them are presumably removed." }, 10, new Master[] { Master.MAZCHNA, Master.CHAELDAR }, false)),
	CAVE_HORRORS(new Task(new int[] { 4353, 4354, 4355, 4356, 4357, }, new String[] { "A Cave horror wears a creepy mask, it is", "prefered to wear a witchwood icon." }, 58, new Master[] { Master.CHAELDAR, Master.NIEVE }, false)),
	CAVE_KRAKEN(new Task(new int[] { 8615, 8617, 8618, 8619, 8616, 8614 }, new String[] { "A Cave kraken can be found in Nieve's slayer dungeon." }, 87, new Master[] { Master.NIEVE }, false)),
	CAVE_SLIMES(new Task(new int[] { 1831 }, new String[] { "These are lesser versions of jellies, watch out they can poison you." }, 17, new Master[] { Master.TURAEL, Master.MAZCHNA }, false)),
	COCKATRICES(new Task(new int[] { 1620, 1621, 4227, }, new String[] { "A Mirror shield is necessary when fighting these monsters." }, 25, new Master[] { Master.MAZCHNA, Master.VANNAKA }, false)),
	COWS(new Task(new int[] { 1766, 1768, 2310, 81, 397, 955, 1767, 3309 }, new String[] { "Cow's may seem stupid, however they know more then", "you think. Don't under estimate them." }, 1, new Master[] { Master.TURAEL }, false)),
	CRAWLING_HAND(new Task(new int[] { 1648, 1649, 1650, 1651, 1652, 1653, 1654, 1655, 1656, 1657, 4226, 7640, 7641 }, new String[] { "Crawling Hands are undead severed hands, fast and", "dexterous they claw their victims." }, 5, new Master[] { Master.TURAEL, Master.MAZCHNA, Master.CHAELDAR }, true)),
	CROCODILES(new Task(new int[] { 1993, 6779 }, new String[] { "Crocodiles can be found near water and marshes in and near the Kharidian Desert." }, 1, new Master[] { Master.VANNAKA }, false)),
	CYCLOPES(new Task(new int[] { 116, 4291, 4292, 6078, 6079, 6080, 6081, 6269, 6270 }, new String[] { "Large one eyed creatures who normally wield a", "large mallet." }, 1, new Master[] { Master.MAZCHNA }, false)),
	DAGANNOTHS(new Task(new int[] { 1338, 1339, 1340, 1341, 1342, 1343, 1344, 1345, 1346, 1347, 2454, 2455, 2456, 2881, 2882, 2883, 2887, 2888, 3591, }, new String[] { "There are many types of Dagannoth, the most powerful being the three Dagannoth Kings." }, 1, new Master[] { Master.VANNAKA, Master.NIEVE }, false)),
	DARK_BEASTS(new Task(new int[] { 2783 }, new String[] { "A dark beast can attack using magic or melee." }, 90, new Master[] { Master.DURADEL, Master.NIEVE }, false)),
	DESERT_LIZARDS(new Task(new int[] { 2803, 2804, 2805, 2806, 2807 }, new String[] { "Desert lizards are big Slayer monsters found in the Kharidian Desert." }, 22, new Master[] { Master.TURAEL, Master.MAZCHNA }, false)),
	DOG(new Task(new int[] { 98, 99, 3582, 6374, 1994, 1593, 1594, 3582, 6374 }, new String[] { "Dogs are much like Wolves, they are pack creatures which will hunt in groups." }, 1, new Master[] { Master.TURAEL, Master.MAZCHNA }, false)),
	DUST_DEVILS(new Task(new int[] { 1624 }, new String[] { "Dust Devils use clouds of dust, sand, ash and whatever", "else they can inhale to blind and disorientate", "their victims." }, 65, new Master[] { Master.VANNAKA, Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false)),
	DWARF(new Task(new int[] { 118, 120, 121, 382, 3219, 3220, 3221, 3268, 3269, 3270, 3271, 3272, 3273, 3274, 3275, 3294, 3295, 4316, 5880, 5881, 5882, 5883, 5884, 5885, 2130, 2131, 2132, 2133, 3276, 3277, 3278, 3279, 119, 2423 }, new String[] { "They are slightly resistant to Magic attacks, and", "are not recommended for low levels." }, 1, new Master[] { Master.TURAEL }, false)),
	EARTH_WARRIORS(new Task(new int[] { 124 }, new String[] { "An Earth warrior is a monster made of earth which fights using melee." }, 1, new Master[] { Master.VANNAKA }, false)),
	/*ELVES(new Task(new int[] { 1184, 1183, 1185 }, new String[] { "Elves are quick, agile and vicious fighters which", "often favour bows and polearms." }, 1, new Master[] { Master.VANNAKA, Master.CHAELDAR, Master.NIEVE }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),
	FEVER_SPIDERS(new Task(new int[] { 2850 }, new String[] { "Fever Spiders are giant spiders that carry the deadly Spider Fever.", "If you don't want to catch it I suggest you wear Slayer Gloves to fight them." }, 1, new Master[] { Master.CHAELDAR, Master.DURADEL }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	FIRE_GIANTS(new Task(new int[] { 110, 1582, 1583, 1584, 1585, 1586, 7003, 7004 }, new String[] { "Like other giants, Fire Giants often wield large weapons", "learn to recognise what kind of weapon it is, and act accordingly." }, 1, new Master[] { Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false)),
	FLESH_CRAWLERS(new Task(new int[] { 4389, 4390, 4391 }, new String[] { "Flesh crawlers are medium level monsters found on", "level 2 of the Stronghold of Security." }, 1, new Master[] { Master.MAZCHNA }, false)),
	GARGOYLES(new Task(new int[] { 1610, 1611, 6389 }, new String[] { "Gargoyles are winged creatures of stone. You'll need to fight them to", "near death before breaking them apart with a rock hammer." }, 75, new Master[] { Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false, Equipment.ROCK_HAMMER)),
	GHOSTS(new Task(new int[] { 103, 104, 491, 1541, 1549, 2716, 2931, 4387, 388, 5342, 5343, 5344, 5345, 5346, 5347, 5348, 1698, 5349, 5350, 5351, 5352, 5369, 5370, 5371, 5372, 5373, 5374, 5572, 6094, 6095, 6096, 6097, 6098, 6504, 13645, 13466, 13467, 13468, 13469, 13470, 13471, 13472, 13473, 13474, 13475, 13476, 13477, 13478, 13479, 13480, 13481 }, new String[] { "A Ghost is an undead monster that is found", "in various places and dungeons. " }, 1, new Master[] { Master.TURAEL, Master.MAZCHNA }, false)),
	GHOULS(new Task(new int[] { 1218, 3059 }, new String[] { "Ghouls are a humanoid race and the descendants of a long-dead society", "that degraded to the point that its people ate their dead." }, 1, new Master[] { Master.MAZCHNA, Master.CHAELDAR }, false)),
	GOBLINS(new Task(new int[] { 100, 101, 102, 444, 445, 489, 1769, 1770, 1771, 1772, 1773, 1774, 1775, 1776, 2274, 2275, 2276, 2277, 2278, 2279, 2280, 2281, 2678, 2679, 2680, 2681, 3060, 3264, 3265, 3266, 3267, 3413, 3414, 3415, 3726, 4261, 4262, 4263, 4264, 4265, 4266, 4267, 4268, 4269, 4270, 4271, 4272, 4273, 4274, 4275, 4276, 4407, 4408, 4409, 4410, 4411, 4412, 4479, 4480, 4481, 4482, 4483, 4484, 4485, 4486, 4487, 4488, 4489, 4490, 4491, 4492, 4499, 4633, 4634, 4635, 4636, 4637, 5786, 5824, 5855, 5856, 6125, 6126, 6132, 6133, 6279, 6280, 6281, 6282, 6283, 6402, 6403, 6404, 6405, 6406, 6407, 6408, 6409, 6410, 6411, 6412, 6413, 6414, 6415, 6416, 6417, 6418, 6419, 6420, 6421, 6422, 6423, 6424, 6425, 6426, 6427, 6428, 6429, 6430, 6431, 6432, 6433, 6434, 6435, 6436, 6437, 6438, 6439, 6440, 6441, 6442, 6443, 6444, 6445, 6446, 6447, 6448, 6449, 6450, 6451, 6452, 6453, 6454, 6455, 6456, 6457, 6458, 6459, 6460, 6461, 6462, 6463, 6464, 6465, 6466, 6467, 6490, 6491, 6492, 6493, 6494, 6495, 6496, 6497 }, new String[] { "Goblins are mostly just annoying, but they can be vicious.", "Watch out for the spears they sometimes carry." }, 1, new Master[] { Master.TURAEL }, false)),
	GORAKS(new Task(new int[] { 4418, 6218 }, new String[] { "Goraks can be tough monsters to fight. Be prepared." }, 1, new Master[] { Master.DURADEL }, false)),
	GREATER_DEMONS(new Task(new int[] { 83, 4698, 4699, 4700, 4701, 6208, 6206, 6204 }, new String[] { "Greater Demons are magic creatures so they are weak to magical attacks.", "They're the strongest demon and very dangerous." }, 1, new Master[] { Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false)),
	GREEN_DRAGON(new Task(new int[] { 941, 4677, 4678, 4679, 4680, 5362 }, new String[] { "Green dragons are very powerfull, they have fierce", "fiery breath." }, 1, new Master[] { Master.VANNAKA }, false)),
	HARPIE_BUG_SWARMS(new Task(new int[] { 3153 }, new String[] { "Harpie Bug Swarms are insectoid Slayer monsters." }, 33, new Master[] { Master.VANNAKA, Master.CHAELDAR }, false)),
	HELLHOUNDS(new Task(new int[] { 49, 3586, 6210, }, new String[] { "Hellhounds are mid to high level demons." }, 1, new Master[] { Master.CHAELDAR, Master.NIEVE }, false)),
	HILL_GIANTS(new Task(new int[] { 117, 4689, 4690, 3058, 4691, 4692, 4693 }, new String[] { "Hill giants can hit up to 19 damage, and they only attack with Melee." }, 1, new Master[] { Master.MAZCHNA, Master.VANNAKA }, false)),
	HOBGOBLINS(new Task(new int[] { 122, 123, 2685, 2686, 3061, 6608, 6642, 6661, 6684, 6710, 6722, 6727, 2687, 2688, 3583, 4898, 6275 }, new String[] { "Mysterious goblin like creatures." }, 1, new Master[] { Master.MAZCHNA }, false)),
	ICE_FIENDS(new Task(new int[] { 3406, 6217, 7714, 7715, 7716 }, new String[] { "An Icefiend is a monster found on top of Ice Mountain." }, 1, new Master[] { Master.TURAEL }, false)),
	ICE_GIANTS(new Task(new int[] { 111, 3072, 4685, 4686, 4687 }, new String[] { "Ice Giants often wield large weapons, learn to recognise", "what kind of weapon it is, and act accordingly" }, 1, new Master[] { Master.VANNAKA }, false)),
	ICE_WARRIOR(new Task(new int[] { 125, 145, 3073 }, new String[] { "Ice warriors, are cold magestic creatures." }, 1, new Master[] { Master.MAZCHNA, Master.VANNAKA }, false)),
	INFERNAL_MAGES(new Task(new int[] { 1643, 1644, 1645, 1646, 1647 }, new String[] { "Infernal Mages are dangerous spell users, beware of their magic", "spells an go properly prepared" }, 45, new Master[] { Master.MAZCHNA, Master.VANNAKA, Master.CHAELDAR }, false)),
	IRON_DRAGONS(new Task(new int[] { 1591 }, new String[] { "Iron dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their firey breath." }, 1, new Master[] { Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false, 40 | 59 << 16)),
	JELLIES(new Task(new int[] { 1637, 1638, 1639, 1640, 1641, 1642 }, new String[] { "Jellies are nasty cube-like gelatinous creatures which", "absorb everything they come across into themselves." }, 52, new Master[] { Master.VANNAKA, Master.CHAELDAR }, false)),
	JUNGLE_HORRORS(new Task(new int[] { 4348, 4349, 4350, 4351, 4352 }, new String[] { "Jungle Horrors can be found all over Mos Le'Harmless.", "They are strong and aggressive, so watch out!" }, 1, new Master[] { Master.VANNAKA, Master.CHAELDAR }, false)),
	KALPHITES(new Task(new int[] { 1153, 1154, 1155, 1156, 1157, 1158, 1159, 1160, 1161 }, new String[] { "Kalaphite are large insects which live in great hives under the desert sands." }, 1, new Master[] { Master.MAZCHNA, Master.CHAELDAR, Master.NIEVE }, false)),
	/*KILLERWATTS(new Task(new int[] { 3201, 3202 }, new String[] { "Vexia halfassed this... (this was shadow knight text)" }, 37, new Master[] { Master.VANNAKA }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	KURASKS(new Task(new int[] { 1608, 1609, 4229 }, new String[] { "A kurask is a very quick creature." }, 70, new Master[] { Master.CHAELDAR, Master.NIEVE }, false)),
	LESSER_DEMONS(new Task(new int[] { 82, 3064, 4694, 4695, 6208, 6204, 6206, 3064, 4696, 4697, 6101 }, new String[] { "Lesser Demons are magic creatures so they are weak to magical attacks." }, 1, new Master[] { Master.VANNAKA, Master.CHAELDAR }, false)),
	MITHRIL_DRAGON(new Task(new int[] { 5363 }, new String[] { "Mithril dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their firey breath." }, 1, new Master[] { Master.DURADEL, Master.NIEVE }, false, 5 | 9 << 16)),
	MINOTAURS(new Task(new int[] { 4404, 4405, 4406 }, new String[] { "Minotaurs are large manlike creatures but you'll", "want to be careful of their horns." }, 1, new Master[] { Master.TURAEL }, false)),
	//MOGRES(new Task(new int[] { 114 }, new String[] { "Mogres are Slayer monsters and require level 32 Slayer to kill. " }, 32, new Master[] { Master.MAZCHNA, Master.VANNAKA }, false)),
	/*MOLANISKS(new Task(new int[] { 5751 }, new String[] { "Molanisks are found attached to cave walls and must be lured", "off with a Slayer bell" }, 39, new Master[] { Master.VANNAKA }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	MONKEYS(new Task(new int[] { 132, 1463, 1464, 2301, 4344, 4363, 6943, 7211, 7213, 7215, 7217, 7219, 7221, 7223, 7225, 7227, 1455, 1459, 1460, 1456, 1457, 1458 }, new String[] { "Small agile creatures, watch out they pinch!" }, 1, new Master[] { Master.TURAEL }, false)),
	MOSS_GIANTS(new Task(new int[] { 112, 1587, 1588, 1681, 4534, 4688, 4706 }, new String[] { "They are known to carry large sticks." }, 1, new Master[] { Master.VANNAKA }, false)),
	NECHRYAELS(new Task(new int[] { 1613 }, new String[] { "Nechryael are demons of decay which summon small winged beings which", "help them fight their victems." }, 80, new Master[] { Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false)),
	OGRES(new Task(new int[] { 114, 115, 374, 3587, 6267 }, new String[] { "Ogres are brutal creatures, favouring large blunt maces and clubs", "they often attack without warning." }, 1, new Master[] { Master.VANNAKA }, false)),
	OTHERWORDLY_BEING(new Task(new int[] { 126 }, new String[] { "A creature filled with everlasting power." }, 1, new Master[] { Master.VANNAKA }, false)),
	PYREFIENDS(new Task(new int[] { 1633, 1634, 1635, 1636, 6216, 6631, 6641, 6660, 6668, 6683, 6709, 6721, }, new String[] { "A scorching hot creature, watch out!" }, 30, new Master[] { Master.MAZCHNA, Master.VANNAKA }, false)),
	RATS(new Task(new int[] { 2682, 2980, 2981, 3007, 88, 224, 4928, 4929, 4936, 4937, 3008, 3009, 3010, 3011, 3012, 3013, 3014, 3015, 3016, 3017, 3018, 4396, 4415, 7202, 7204, 7417, 7461, 87, 446, 950, 4395, 4922, 4923, 4924, 4925, 4926, 4927, 4942, 4943, 4944, 4945, 86, 87, 446, 950, 4395, 4922, 4923, 4924, 4925, 4926, 4927, 4942, 4943, 4944, 4945 }, new String[] { "Quick little rodents!" }, 1, new Master[] { Master.TURAEL }, false)),

	/*RED_DRAGONS(new Task(new int[] { 53, 4669, 4670, 4671, 4672, 1589, 3588, 4667, 4668, }, new String[] { "Red dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their firey breath." }, 1, new Master[] { Master.DURADEL }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	ROCK_SLUGS(new Task(new int[] { 1631, 1632 }, new String[] { "A rock slug can leave behind a trail of his presence.." }, 20, new Master[] { Master.MAZCHNA }, false)),
	/*SCABARITES(new Task(new int[] { 2009 }, new String[] { "The Scabarites are fairly strong monsters, and can hit pretty hard." }, 1, new Master[] { Master.CHAELDAR }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	SCORPIONS(new Task(new int[] { 107, 1477, 4402, 4403, 144 }, new String[] { "A scorpion makes a piercing sound, watch out for it's", " long sharp tail." }, 1, new Master[] { Master.TURAEL }, false)),
	/*SEA_SNAKES(new Task(new int[] { 3943, 3939, }, new String[] { "They are quick, make sure you don't let your", "eye off of them for 1 second." }, 1, new Master[] { Master.VANNAKA }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	SHADE(new Task(new int[] { 3617, 1250, 1241, 1246, 1248, 1250, 428, 1240 }, new String[] { "Shades are dark and mysterious, they hide in the shadows so be wary of ambushes." }, 1, new Master[] { Master.VANNAKA }, true)),
	/*SHADOW_WARRIORS(new Task(new int[] { 158 }, new String[] { "Shadow warriors are dark and mysterious, they hide in the shadows so be wary of ambushes." }, 1, new Master[] { Master.VANNAKA, Master.CHAELDAR }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	SKELETONS(new Task(new int[] { 90, 91, 92, 93, 94, 459, 1471, 1575, 1973, 2036, 2037, 2715, 2717, 3065, 3151, 3291, 3581, 3697, 3698, 3699, 3700, 3701, 3702, 3703, 3704, 3705, 3844, 3850, 3851, 4384, 4385, 4386, 5332, 5333, 5334, 5335, 5336, 5337, 5338, 5339, 5340, 5341, 5359, 5365, 5366, 5367, 5368, 5381, 5384, 5385, 5386, 5387, 5388, 5389, 5390, 5391, 5392, 5411, 5412, 5422, 6091, 6092, 6093, 6103, 6104, 6105, 6106, 6107, 6764, 6765, 6766, 6767, 6768 }, new String[] { "Skeletons are undead monsters found in various locations." }, 1, new Master[] { Master.TURAEL, Master.MAZCHNA }, true)),
	SPIDERS(new Task(new int[] { 61, 1004, 1221, 1473, 1474, 63, 4401, 2034, 977, 7207, 134, 1009, 59, 60, 4400, 58, 62, 1478, 2491, 2492, 6376, 6377, }, new String[] { "Level 24 spiders are aggressive and can hit up to 60 life points." }, 1, new Master[] { Master.TURAEL }, false)),
	SPIRTUAL_MAGES(new Task(new int[] { 6221, 6231, 6257, 6278 }, new String[] { "They are dangerous, they hit with mage." }, 83, new Master[] { Master.CHAELDAR, Master.DURADEL }, false)),
	SPIRTUAL_RANGERS(new Task(new int[] { 6220, 6230, 6256, 6276 }, new String[] { "They are dangerous, they hit with range." }, 63, new Master[] { Master.CHAELDAR, Master.DURADEL }, false)),
	SPIRTUAL_WARRIORS(new Task(new int[] { 6219, 6229, 6255, 6277, }, new String[] { "They are dangerous, they hit with melee." }, 68, new Master[] { Master.CHAELDAR, Master.DURADEL }, false)),
	STEEL_DRAGONS(new Task(new int[] { 1592, 3590 }, new String[] { "Steel dragons aren't as strong as other dragons but they're still", "very powerful, watch out for their firey breath." }, 1, new Master[] { Master.DURADEL, Master.NIEVE }, false, 10 | 20 << 16)),
	/*SUQAHS(new Task(new int[] { 4527, 4528, 4529, 4530, 4531, 4532, 4533 }, new String[] {}, 1, new Master[] { Master.DURADEL }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),
	TERROR_DOG(new Task(new int[] { 5417, 5418 }, new String[] { "Terror Dogs are much like Wolves, they are pack creatures which will hunt in groups." }, 1, new Master[] { Master.VANNAKA, Master.DURADEL }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	TROLLS(new Task(new int[] { 72, 3584, 1098, 1096, 1097, 1095, 1101, 1105, 1102, 1103, 1104, 1130, 1131, 1132, 1133, 1134, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1138, 1560, 1561, 1562, 1563, 1564, 1565, 1566, 1935, 1936, 1937, 1938, 1939, 1940, 1941, 1942, 3840, 3841, 3842, 3843, 3845, 1933, 1934, }, new String[] { "Trolls have a crushing attack, it's bets to wear a high crushing defence." }, 1, new Master[] { Master.CHAELDAR, Master.DURADEL, Master.NIEVE }, false)),
	TUROTHS(new Task(new int[] { 1626, 1627, 1628, 1629, 1630 }, new String[] { "Turoths are Slayer monsters that require a Slayer level of 55 to kill" }, 55, new Master[] { Master.CHAELDAR, Master.CHAELDAR, Master.NIEVE }, false)),
	TZHAAR(new Task(new int[] { 2591, 2592, 2593, 2745, 2594, 2595, 2596, 2597, 2604, 2605, 2606, 2607, 2608, 2609, 7755, 7753, 2598, 2599, 2600, 2601, 2610, 2611, 2612, 2613, 2614, 2615, 2616, 2624, 2617, 2618, 2625, 2602, 2603, 7754, 7767, 2610, 2611, 2612, 2613, 2614, 2615, 2616, 2624, 2625, 2627, 2628, 2629, 2630, 2631, 2632, 7746, 7747, 7748, 7749, 7750, 7751, 7752, 7753, 7754, 7755, 7756, 7757, 7758, 7759, 7760, 7761, 7762, 7763, 7764, 7765, 7766, 7767, 7768, 7769, 7770, 7771, 7747, 7747, 7748, 7749, 7750, 7751, 7752, 7753, 7757, 7765, 7769, 7768 }, new String[] { "Young Tzhaar's of the century are furious with your kind." }, 1, new Master[] { Master.DURADEL }, false)),
	VAMPIRES(new Task(new int[] { 1023, 1220, 1223, 1225, 6214 }, new String[] { "Vampies arr equiped with large fangs", "they can do serious damage." }, 1, new Master[] { Master.MAZCHNA, Master.VANNAKA }, false)),
	/*WALL_BEASTS(new Task(new int[] { 7823 }, new String[] { "A spiny helmet or equivalent is required to start fighting one " }, 35, new Master[] { Master.MAZCHNA }, false, Equipment.SPINY_HELMET) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),
	WARPED_TERRORBIRDS(new Task(new int[] { 6285, 6286, 6287, 6288, 6289, 6290, 6291, 6292, 6293, 6294, 6295, 6322, 6323, 6324, 6325, 6326, 6327, 6328, 6329, 6330, 6331, 6332, }, new String[] { "They require 56 Slayer and a Crystal chime to be killed. " }, 56, new Master[] { Master.DURADEL }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),
	WARPED_TORTOISES(new Task(new int[] { 6296, 6297 }, new String[] { "A warped tortoise is not as slow as they look." }, 56, new Master[] { Master.DURADEL }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	WATERFIENDS(new Task(new int[] { 5361 }, new String[] { "A waterfiend takes no damage from fire!" }, 1, new Master[] { Master.MAZCHNA }, false)),
	WEREWOLFS(new Task(new int[] { 1665, 6006, 6007, 6008, 6009, 6010, 6011, 6012, 6013, 6014, 6015, 6016, 6017, 6018, 6019, 6020, 6021, 6022, 6023, 6024, 6025, 6212, 6213, 6607, 6609, 6614, 6617, 6625, 6632, 6644, 6663, 6675, 6686, 6701, 6712, 6724, 6728, }, new String[] { "There temper is alot more nasty then a regular wolf!" }, 1, new Master[] { Master.VANNAKA }, false)),
	WOLVES(new Task(new int[] { 95, 96, 97, 141, 142, 143, 839, 1198, 1330, 1558, 1559, 1951, 1952, 1953, 1954, 1955, 1956, 4413, 4414, 6046, 6047, 6048, 6049, 6050, 6051, 6052, 6829, 6830, 7005 }, new String[] { "Wolves are more agressive then dog's." }, 1, new Master[] { Master.TURAEL, Master.MAZCHNA }, false)),
	ZOMBIES(new Task(new int[] { 73, 74, 75, 76, 2058, 2714, 2863, 2866, 2869, 2878, 3622, 4392, 4393, 4394, 5293, 5294, 5295, 5296, 5297, 5298, 5299, 5300, 5301, 5302, 5303, 5304, 5305, 5306, 5307, 5308, 5309, 5310, 5311, 5312, 5313, 5314, 5315, 5316, 5317, 5318, 5319, 5320, 5321, 5322, 5323, 5324, 5325, 5326, 5327, 5328, 5329, 5330, 5331, 5375, 5376, 5377, 5378, 5379, 5380, 5393, 5394, 5395, 5396, 5397, 5398, 5399, 5400, 5401, 5402, 5403, 5404, 5405, 5406, 5407, 5408, 5409, 5410, 6099, 6100, 6131, }, new String[] { "Zombies are creatures with no brain, they do hit farley", "high though." }, 1, new Master[] { Master.TURAEL, Master.MAZCHNA }, true)),
	/*ZYGOMITES(new Task(new int[] { 3346, 3347 }, new String[] { "Mutated zygomites are monsters that appear as Fungi until provoked." }, 57, new Master[] { Master.CHAELDAR }, false) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	}),*/
	JAD(new Task(new int[] { }, new String[] { "TzTok-Jad is the king of the Fight Caves." }, 1, new Master[] { Master.WISE_OLD_MAN }, false, 1 | 1 << 16)),
	CHAOS_ELEMENTAL(new Task(new int[] { 3200 }, new String[] { "The Chaos Elemental is located in the deep Wilderness." }, 1, new Master[] { Master.WISE_OLD_MAN }, false, 5 | 25 << 16)),
	GIANT_MOLE(new Task(new int[] { 3340 }, new String[] { "Fighting the Giant Mole will require a light source." }, 1, new Master[] { Master.WISE_OLD_MAN }, false, 5 | 25 << 16)),
	KING_BLACK_DRAGON(new Task(new int[] { 50 }, new String[] { "The King Black Dragon is located in the deep wilderness." }, 1, new Master[] { Master.WISE_OLD_MAN }, false, 5 | 25 << 16)),
	COMMANDER_ZILYANA(new Task(new int[] { 6247 }, new String[] { "Commander Zilyana is one of the four Godwars bosses." }, 1, new Master[] { Master.WISE_OLD_MAN }, false, 5 | 25 << 16)),
	GENERAL_GRARDOOR(new Task(new int[] { 6260 }, new String[] { "General Grardoor is one of the four Godwars bosses." }, 1, new Master[] { Master.WISE_OLD_MAN }, false, 5 | 25 << 16)),
	KRIL_TSUTSAROTH(new Task(new int[] { 6203 }, new String[] { "Kril Tsutsaroth is one of the four Godwars bosses." }, 1, new Master[] { Master.WISE_OLD_MAN }, false, 5 | 25 << 16)),
	KREE_ARRA(new Task(new int[] { 6222 }, new String[] { "Kree'arra is one of the four Godwars bosses." }, 1, new Master[] { Master.WISE_OLD_MAN }, false, 5 | 25 << 16) {

	}),
	SKELETAL_WYVERN(new Task(new int[] { 3068, 3069, 3070, 3071 }, new String[] { "A skeletal wyvern requires an elemental, mirror", "or dragonfire shield." }, 72, new Master[] { Master.DURADEL, Master.CHAELDAR, Master.NIEVE }, false, 24 | 39 << 16)),
	/*CERBERUS(new Task(new int[] {8632, 8634}, new String[] {"Cerberus is about the best guard dog there is!"}, 91, new Master[] {Master.VANNAKA, Master.CHAELDAR, Master.NIEVE, Master.DURADEL}, false, 20 | 25 << 16) {
		@Override
		public boolean isDisabled() {
			return true;
		}
	})*/
	;

	/**
	 * The mapping of masters and their tasks.
	 */
	private static Map<Master, List<Task>> MASTERS = new HashMap<>();

	/**
	 * Represents the task.
	 */
	private final Task task;

	/**
	 * Constructs a new {@code Tasks} {@Code Object}.
	 * @param task the task to set.
	 */
	Tasks(Task task) {
		this.task = task;
	}

	/**
	 * Gets the tasks for the master.
	 * @param player the player.
	 * @return the task.
	 */
	public static Task[] getTasks(Master master) {
		if (MASTERS.get(master).size() == 0) {
			for (Tasks tasks : values()) {
				for (Master m : tasks.getTask().getMasters()) {
					MASTERS.get(m).add(tasks.getTask());
				}
			}
		}
		return MASTERS.get(master).toArray(new Task[] {});
	}

	/**
	 * Gets the task by the value.
	 * @param task the task.
	 * @return the tasks enum value.
	 */
	public static Tasks forValue(Task task) {
		for (Tasks taskk : Tasks.values()) {
			if (taskk.getTask().equals(task)) {
				return taskk;
			}
		}
		return null;
	}

	/**
	 * Gets the task for the npc id.
	 * @param id the id.
	 * @return the task.
	 */
	public static Task forId(int id) {
		for (Tasks task : Tasks.values()) {
			for (int i = 0; i < task.getTask().getNpcs().length; i++) {
				if (task.getTask().getNpcs()[i] == id) {
					return task.getTask();
				}
			}
		}
		return null;
	}

	/**
	 * Checks if the given npc is a slayer npc.
	 * @param id the id.
	 * @return {@code True} if so.
	 */
	public static boolean isSlayerNpc(int id) {
		for (Tasks task : Tasks.values()) {
			for (int i = 0; i < task.getTask().getNpcs().length; i++) {
				if (task.getTask().getNpcs()[i] == id) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the task.
	 * @return The task.
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * static-block to add masters for better searching.
	 */
	static {
		for (Master master : Master.values()) {
			List<Task> t = new ArrayList<>();
			MASTERS.put(master, t);
		}
	}
}

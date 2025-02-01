package content.global.skill.slayer;


import core.cache.def.impl.NPCDefinition;

import java.util.Arrays;
import java.util.HashMap;
import core.game.node.entity.player.Player;

import static core.api.ContentAPIKt.hasRequirement;

/**
 * A non-garbage way of representing tasks
 * Slayer level source: <a href="https://web.archive.org/web/20090202035813/http://runescape.com/kbase/viewarticle.ws?article_id=1933">...</a>
 * Combat level source: None
 * @author ceik
 * @author gregf
 */
public enum Tasks {
    ABERRANT_SPECTRES(65, new int[] { 1604, 1605, 1606, 1607, 7801, 7802, 7803, 7804 }, new String[] { "Aberrant Spectres are fetid, vile ghosts. The very", "smell of them will paralyse and harm you. A nose peg", "will help ignore their stink." }, 60, true, false),
    ABYSSAL_DEMONS(85, new int[] { 1615 }, new String[] { "Abyssal Demons are nasty creatures to fight. They", "aren't really part of this realm, and are able to", "move very quickly to trap their prey."}, 85, false, false),
    ANKOU(40, new int[] { 4381, 4382, 4383 }, new String[] { "Ankou are undead skeletal ghosts. They'll fight you", "up close but make sure to take advantage out of their", "limited defence." }, 1, true, false),
    AVIANSIES(60, new int[] { 6245, 6243, 6235, 6232, 6244, 6246, 6233, 6241, 6238, 6237, 6240, 6242, 6239, 6234 }, new String[] { "Aviansies are bird-like creatures found in the icy", "dungeons of the north. Melee weapons can't reach them,", "so use Magic or Ranged attacks." }, 1, false, false),
    BANSHEE(20, new int[] { 1612 }, new String[] { "Banshees use a piercing scream to shock their enemies.", "You'll need some earmuffs to protect yourself from them." }, 15, true, false),
    BASILISKS(40, new int[] { 1616, 1617 }, new String[] { "Basilisks, like Cockatrice, have a gaze which will", "paralyse and harm their prey. You'll need a Mirror", "Shield to protect you." }, 40, false, false),
    BATS(5, new int[] { 412, 78, 3711 }, new String[] { "Bats are rarely found on the ground, so you'll have", "to fight them while they're airborne, which won't be", "easy for melee." }, 1, false, false),
    BEARS(13, new int[] { 106, 105, 1195, 3645, 3664, 1326, 1327 }, new String[] { "Bears are tough creatures and fierce fighters, watch", "out for their powerful claws." }, 1, false, false),
    BIRDS(1, new int[] { 1475, 5120, 5121, 5122, 5123, 5133, 1475, 1476, 41, 951, 1017, 1401, 1402, 2313, 2314, 2315, 1016, 1550, 147, 1180, 1754, 1755, 1756, 2252, 4570, 4571, 1911, 6114, 46, 2693, 6113, 6112, 146, 149, 150, 450, 451, 1179, 1322, 1323, 1324, 1325, 1400, 2726, 2727, 3197, 138, 48, 4373, 4374, 4535, 139, 1751, 148, 1181, 6382, 2459, 2460, 2461, 2462, 2707, 2708, 6115, 6116, 3296, 6378, 1996, 3675, 3676, 6792, 6946, 7320, 7322, 7324, 7326, 7328, 1692, 6322, 3476, 1018, 1403, }, new String[] { "Birds aren't the most intelligent of creatures, but", "watch out for their sharp stabbing beaks." }, 1, false, false),
    BLACK_DEMONS(80, new int[] { 84, 677, 4702, 4703, 4704, 4705, 6208, }, new String[] { "Black Demons are magic creatures that are weak", "to magic attacks. They're a very strong", "demon and very dangerous." }, 1, false, false),
    BLACK_DRAGONS(80, new int[] {54, 4673, 4674, 4675, 4676, 3376, 50 }, new String[] { "Black dragons are the strongest dragons;", "watch out for their fiery breath." }, 1, false, true),
    BLOODVELDS(50, new int[] { 1618, 1619, 6215, 7643, 7642 }, new String[] { "Bloodvelds are strange demonic creatures, they use their", "long rasping tongue to feed on just about", "anything they can find." }, 50, false, false),
    BLUE_DRAGONS(65, new int[] { 55, 4681, 4682, 4683, 4684, 5178, 52, 4665, 4666, }, new String[] { "Blue dragons aren't as strong as other dragons but they're", "still very powerful, watch out for their fiery breath." }, 1, false, true),
    BRINE_RATS(45, new int[] { 3707 }, new String[] { "Brine rats can be found in caves that are near the", "sea. They are hairless, bad-tempered and generally", "unfriendly." }, 47, "Olaf's Quest"),
    BRONZE_DRAGONS(75, new int[] { 1590 }, new String[] { "Bronze Dragons are the weakest of the metallic", "dragons, their bronze scales are far thicker than", "normal bronze armour." }, 1, false, true),
    CATABLEPONS(35, new int[] { 4397, 4398, 4399, }, new String[] { "Catablepon are mythical, cow like, magical creatures", "Beware their weakening glare." }, 1, false, false),
    CAVE_BUG(1, new int[] { 1832, 5750, }, new String[] { "Cave Bugs are like Cave Crawlers, except smaller and", "easier to squish, though they still have a fondness", "for plants." }, 7, false, false),
    CAVE_CRAWLERS(10, new int[] { 1600, 1601, 1602, 1603, }, new String[] { "Cave Crawlers are small and fast, often hiding in", "ambush. Avoid their barbed tongue or you'll", "get poisoned." }, 10, false, false),
    CAVE_HORRORS(85, new int[] { 4353, 4354, 4355, 4356, 4357, }, new String[] { "Cave Horrors can be found under Mos Le'Harmless. You", "will need a Witchwood Icon to fight them effectively." }, 58, "Cabin Fever"),
    CAVE_SLIMES(15, new int[] { 1831 }, new String[] { "Cave Slimes are the lesser cousins of Jellies, though", "don't be fooled they can still be dangerous as", "they're often poisonous." }, 17, false, false),
    COCKATRICES(25, new int[] { 1620, 1621, 4227, }, new String[] { "Cockatrice, like Basilisks, have a gaze which will", "paralyse and harm their prey. You'll need a Mirror", "Shield to protect you." }, 25, false, false),
    COWS(5, new int[] { 81, 1766, 1768, 2310, 397, 955, 1767, 3309 }, new String[] { "Cows are bigger than you, so they'll often hit fairly", "hard but are usually fairly slow to react." }, 1, false, false),
    CRAWLING_HAND(1,new int[] { 1648, 1649, 1650, 1651, 1652, 1653, 1654, 1655, 1656, 1657, 4226, 7640, 7641 }, new String[] { "Crawling Hands are undead severed hands, fast and", "dexterous they claw their victims." }, 5, true, false),
    CROCODILES(50, new int[] { 1993, 6779 }, new String[] { "Crocodiles are large reptiles which live near water.", "You'll want to have a stabbing weapon handy for", "puncturing their thick scaly hides." }, 1, false, false),
    DAGANNOTHS(75, new int[] { 1338, 1339, 1340, 1341, 1342, 1343, 1344, 1345, 1346, 1347, 2454, 2455, 2456, 2881, 2882, 2883, 2887, 2888, 3591, }, new String[] { "Dagannoth are large sea dwelling creatures which are", "very aggressive. You'll often find them in caves", "near sea water." }, 1, "Horror from the Deep"),
    DARK_BEASTS(90, new int[] { 2783 }, new String[] { "Dark Beasts are large, dog-like predators.", "Their massively muscled bodies protect", "them from crushing weapons." }, 90, "Mourning's Ends Part II"),
    DESERT_LIZARDS(15, new int[] { 2803, 2804, 2805, 2806, 2807, 2808 }, new String[] { "Lizards are large reptiles with tough skin. Those", "found in the desert will need you to douse them with", "freezing water to finish them off after a tough battle." }, 22, false, false),
    DOG(15, new int[] { 99, 3582, 1994, 1593, 1594, 3582 }, new String[] { "Dogs are much like Wolves, they are", "pack creatures which will hunt in groups." }, 1, false, false),
    DUST_DEVILS(70, new int[] { 1624 }, new String[] { "Dust Devils use clouds of dust, sand, ash and whatever", "else they can inhale to blind and disorientate", "their victims." }, 65, false, false),
    DWARF(6, new int[] { 118, 120, 121, 382, 3219, 3220, 3221, 3268, 3269, 3270, 3271, 3272, 3273, 3274, 3275, 3294, 3295, 4316, 5880, 5881, 5882, 5883, 5884, 5885, 2130, 2131, 2132, 2133, 3276, 3277, 3278, 3279, 119, 2423 }, new String[] { "Dwarves are a small but tough race of miners, often", "using pickaxes to pierce their opponents armour." }, 1, false, false),
    EARTH_WARRIORS(35, new int[] { 124 }, new String[] { "Earth Warriors are a kind of earth elemental,", "grind them to dust with blunt weapons." }, 1, false, false),
    ELVES(70, new int[] { 1183, 1184, 2359, 2360, 2361, 2362, 2373, 7438, 7439, 7440, 7441 }, new String[]{ "Elves are quick, agile, and vicious fighters which", "often favour bows and polearms."}, 1, "Regicide"),
    // Waiting for either Rum Deal or Pirate Pete and Fever Spiders before adding this assignment
    FEVER_SPIDER(1, new int[] { 2850 }, new String[] { "Fever Spiders are giant spiders that carry the deadly", "Spider Fever. If you don't want to catch it I suggest", "you wear Slayer Gloves to fight them." }, 42, "Rum Deal"),
    FIRE_GIANTS(65, new int[] { 110, 1582, 1583, 1584, 1585, 1586, 7003, 7004 }, new String[] { "Like other giants, Fire Giants often wield large weapons", "learn to recognise what kind of weapon it is,", "and act accordingly." }, 1, false, false),
    FLESH_CRAWLERS(15, new int[] { 4389, 4390, 4391 }, new String[] { "Flesh Crawlers are scavengers and will eat you - and", "anyone else, given the chance." }, 1, false, false),
    GARGOYLES(80, new int[] { 1610, 6389 }, new String[] { "Gargoyles are winged creatures of stone. You'll need", "to fight them to near death before breaking them apart", "with a rock hammer." }, 75, false, false),
    GHOSTS(13, new int[] { 103, 104, 491, 1541, 1549, 2716, 2931, 4387, 388, 5342, 5343, 5344, 5345, 5346, 5347, 5348, 1698, 5349, 5350, 5351, 5352, 5369, 5370, 5371, 5372, 5373, 5374, 5572, 6094, 6095, 6096, 6097, 6098, 6504, 13645, 13466, 13467, 13468, 13469, 13470, 13471, 13472, 13473, 13474, 13475, 13476, 13477, 13478, 13479, 13480, 13481 }, new String[] { "Ghosts are undead so magic is your best bet against", "them, there is even a spell specially for fighting", "the undead." }, 1, true, false),
    GHOULS(25, new int[] { 1218, 3059 }, new String[] { "Ghouls aren't undead but they are stronger and", "tougher than they look. However they're also very", "cowardly and will run if they're losing a fight." }, 1, false, false),
    GOBLINS(1, new int[] { 100, 101, 102, 444, 445, 489, 1769, 1770, 1771, 1772, 1773, 1774, 1775, 1776, 2274, 2275, 2276, 2277, 2278, 2279, 2280, 2281, 2678, 2679, 2680, 2681, 3060, 3264, 3265, 3266, 3267, 3413, 3414, 3415, 3726, 4261, 4262, 4263, 4264, 4265, 4266, 4267, 4268, 4269, 4270, 4271, 4272, 4273, 4274, 4275, 4276, 4407, 4408, 4409, 4410, 4411, 4412, 4479, 4480, 4481, 4482, 4483, 4484, 4485, 4486, 4487, 4488, 4489, 4490, 4491, 4492, 4499, 4633, 4634, 4635, 4636, 4637, 5786, 5824, 5855, 5856, 6125, 6126, 6132, 6133, 6279, 6280, 6281, 6282, 6283, 6402, 6403, 6404, 6405, 6406, 6407, 6408, 6409, 6410, 6411, 6412, 6413, 6414, 6415, 6416, 6417, 6418, 6419, 6420, 6421, 6422, 6423, 6424, 6425, 6426, 6427, 6428, 6429, 6430, 6431, 6432, 6433, 6434, 6435, 6436, 6437, 6438, 6439, 6440, 6441, 6442, 6443, 6444, 6445, 6446, 6447, 6448, 6449, 6450, 6451, 6452, 6453, 6454, 6455, 6456, 6457, 6458, 6459, 6460, 6461, 6462, 6463, 6464, 6465, 6466, 6467, 6490, 6491, 6492, 6493, 6494, 6495, 6496, 6497 }, new String[] { "Goblins are mostly just annoying, but they can be vicious.", "Watch out for the spears they sometimes carry." }, 1, false, false),
    GORAKS(70, new int[] { 4418, 6218 }, new String[] { "Goraks are extremely aggressive creatures. They have", "been imprisoned on an alternative plane, which is", "only accessible by using the fairyrings. Be extremely", "careful, their touch drains health as well as skills!" }, 1, "A Fairy Tale I - Growing Pains"),
    GREATER_DEMONS(75, new int[] { 83, 4698, 4699, 4700, 4701, 6204 }, new String[] { "Greater Demons are magic creatures so they are weak", "to magical attacks. Though not the strongest demon,", "they are still dangerous." }, 1, false, false),
    GREEN_DRAGONS(52, new int[] { 941, 4677, 4678, 4679, 4680, 5362, 742 }, new String[] { "Green Dragons are the weakest dragon but still very", "powerful, watch out for their fiery breath." }, 1, false, true),
    HARPIE_BUG_SWARMS(45, new int[] { 3153 }, new String[] { "Harpie Bug Swarms are pesky critters that are hard to", "hit. You need a lit bug lantern to distract them with", "its hypnotic light." }, 33, false, false),
    HELLHOUNDS(75, new int[] { 49, 3586, 6210, }, new String[] { "Hellhounds are a cross between Dogs and Demons, they", "are dangerous with a fierce bite." }, 1, false, false),
    HILL_GIANTS(25, new int[] { 117, 4689, 4690, 3058, 4691, 4692, 4693 }, new String[] { "Hill Giants often wield large weapons, learn to", "recognise what kind of weapon it is and", "act accordingly." }, 1, false, false),
    HOBGOBLINS(20, new int[] { 122, 123, 2685, 2686, 3061, 6608, 6642, 6661, 6684, 6710, 6722, 6727, 2687, 2688, 3583, 4898, 6275 }, new String[] { "Hobgoblins are sneaky underhanded creatures, they", "often wield spears and some times carry javelins too." }, 1, false, false),
    ICE_FIENDS(20, new int[] { 3406, 6217, 7714, 7715, 7716 }, new String[] { "Icefiends are beings of ice and freezing rock, they're", "quick and agile so you'll want to be careful when", "getting close to them." }, 1, false, false),
    ICE_GIANTS(50, new int[] { 111, 3072, 4685, 4686, 4687 }, new String[] { "Like other giants, Ice Giants often wield large", "weapons, learn to recognise what kind of weapon it is", "and act accordingly." }, 1, false, false),
    ICE_WARRIOR(45, new int[] { 125, 145, 3073 }, new String[] { "Ice Warriors are a kind of ice elemental, shatter them", "with blunt weapons or melt them with fire." }, 1, false, false),
    INFERNAL_MAGES(40, new int[] { 1643, 1644, 1645, 1646, 1647 }, new String[] { "Infernal Mages are dangerous spell users, beware of", "their magic spells and go properly prepared" }, 45, false, false),
    IRON_DRAGONS(80, new int[] { 1591 }, new String[] { "Iron Dragons are some of the weaker metallic dragons,", "their iron scales are far thicker than normal", "iron armour." }, 1, false, true),
    JELLIES(57, new int[] { 1637, 1638, 1639, 1640, 1641, 1642 }, new String[] { "Jellies are nasty cube-like gelatinous creatures which", "absorb everything they come across into themselves." }, 52, false, false),
    JUNGLE_HORRORS(65, new int[] { 4348, 4349, 4350, 4351, 4352 }, new String[] { "Jungle Horrors can be found all over Mos Le'Harmless.", "They are strong and aggressive, so watch out!" }, 1, "Cabin Fever"),
    KALPHITES(15, new int[] { 1153, 1154, 1155, 1156, 1157, 1159, 1160, 1161 }, new String[] { "Kalphites are large insects which live in great hives", "under the desert sands." }, 1, false, false),
    // Waiting for the killer watt plane to be implemented before adding to assignments
    KILLERWATTS(1, new int[] { 3201 }, new String[] { "Killerwatts store huge amounts of energy in their", "bodies, which is released if they are touched. You'll", "need to wear heavily insulated boots to counter this", "shocking effect." }, 37, "Ernest the Chicken"),
    KURASKS(65, new int[] { 1608, 1609, 4229, 7805, 7797 }, new String[] { "Kurasks are large brutal creatures with very thick", "hides. You'll need a Leaf-Tipped Spear Sword or", "Battle-axe, Broad Arrows, or a Magic Dart to harm them." }, 70, false, false),
    LESSER_DEMONS(60, new int[] { 82, 6203, 3064, 4694, 4695, 6206, 3064, 4696, 4697, 6101 }, new String[] { "Lesser Demons are magic creatures so they are weak to", "magical attacks. Though they're relatively weak they", "are still dangerous." }, 1, false, false),
    MINOTAURS(7, new int[] { 4404, 4405, 4406 }, new String[] { "Minotaurs are large manlike creatures but you'll", "want to be careful of their horns." }, 1, false, false),
    MITHRIL_DRAGONS(60, new int[] { 5363 }, new String[] { "Mithril dragons are more vulnerable to magic and to", "stab-based melee attacks than to anything else." }, 1, false, true),
    MOGRES(1, new int[] { 114 }, new String[] { "Mogres are a type of aquatic Ogre that is", "often mistaken for a giant mudskipper. You have to force", "them out of the water with a fishing explosive." }, 32, false, false),
    // Waiting for molanisks transform to be implemented before adding this to assignments
    MOLANISKS(1, new int[] { 5751 }, new String[] { "Molanisks are subterranean creatures. You can find", "them in caves deep below the ground. I heard that the", "goblins have recently had trouble with some, but they", "use a bell of some sort to deal with them."}, 39, "Death to the Dorgeshuun"),
    MONKEYS(1, new int[] { 132, 1463, 1464, 2301, 4344, 4363, 6943, 7211, 7213, 7215, 7217, 7219, 7221, 7223, 7225, 7227, 1455, 1459, 1460, 1456, 1457, 1458 }, new String[] { "Monkeys are tricky creatures, they are agile and", "fairly fast. Learn to anticipate their movements." }, 1, false, false),
    MOSS_GIANTS(40, new int[] { 112, 1587, 1588, 1681, 4534, 4688, 4706 }, new String[] { "Like other giants, Moss Giants often wield large", "weapons, learn to recognise what kind of weapon it is", "and act accordingly." }, 1, false, false),
    // Waiting for zygomite transform to be implemented before adding this to assignments
    MUTATED_ZYGOMITES(1, new int[] { 3346, 3347 }, new String[] { "Mutated Zygomites are hard to destroy. They regenerate", "quickly so you will need to finish them with fungicide." }, 57, "Lost City"),
    NECHRYAELS(85, new int[] { 1613 }, new String[] { "Nechryael are demons of decay which summon small", "winged beings to help them fight their victims." }, 80, false, false),
    OGRES(40, new int[] { 115, 374, 2044, 2045, 2046, 2047, 2048, 2049, 2050, 2051, 2052, 2053, 2054, 2055, 2056, 2057, 2060, 2801, 3419, 7078, 7079, 7080, 7081, 7082 }, new String[] { "Ogres are brutal creatures, favouring large blunt", "maces and clubs they often attack without warning." }, 1, false, false),
    OTHERWORDLY_BEING(40, new int[] { 126 }, new String[] { "Otherworldly Beings are ethereal beings making them", "weak to magical attack." }, 1, false, false),
    PYREFIENDS(25, new int[] { 1633, 1634, 1635, 1636, 6216, 6631, 6641, 6660, 6668, 6683, 6709, 6721, }, new String[] { "Pyrefiends are beings of fire and molten rock,", "they're quick and agile so you'll want to be careful", "when getting close to them." }, 30, false, false),
    RED_DRAGONS(1, new int[] { 53, 1589, 3588, 4667, 4668, 4669, 4670, 4671, 4672 }, new String[] { "Red Dragons are very powerful, stronger than most", "dragons, watch out for their fiery breath." }, 1, false, false),
    ROCK_SLUGS(20, new int[] { 1631, 1632 }, new String[] { "Rockslugs are strange stoney slugs. You'll need to", "fight them to near death before finishing them off", "with Salt." }, 20, false, false),
    // Seems to need Contact or the NPCs added to dungeons before adding this assignment as a task
    SCABARITES(1, new int[] { 2001, 4500, 5251, 5252, 5255, 5256, 5250, 5254, 6777, 6778, 6774, 6780, 6781, 6773 }, new String[] {"Scabarites are insectoid creatures, found beyond the", "Kharidian deserts. They can be extremely dangerous."}, 1, "Contact!"),
    SCORPIONS(7, new int[] { 107, 1477, 4402, 4403, 144 }, new String[] { "Scorpions are almost always poisonous, their hard", "carapace makes them resistant to crushing and", "stabbing attacks." }, 1, false, false),
    SEA_SNAKES(1, new int[] { 3939, 3940 }, new String[] { "Sea Snakes are long and slithery with a venomous bite.", "The larger ones are more poisonous, so keep an eye on", "your health." }, 1, "Royal Trouble"),
    SHADE(30, new int[] { 3617, 1250, 1241, 1246, 1248, 1250, 428, 1240 }, new String[] { "Shades are undead so magic is your best best against", "them, you can find Shades at Mort'ton." }, 1, true, false),
    // Dungeon needs to be connected to the Legend's Guild before adding this assignment as a task
    SHADOW_WARRIORS(1, new int[] { 158 }, new String[] { "Shadow Warriors are dark and mysterious, they hide in", "the shadows so be wary of ambushes." }, 1, "Legends' Quest"),
    SKELETAL_WYVERN(70, new int[] { 3068, 3069, 3070, 3071 }, new String[] { "Skeletal Wyverns are extremely dangerous and they are", "hard to hit with arrows as they slip right through.", "To stand a good chance of surviving you'll need some", "elemental shielding from its icy breath." }, 72, false, false),
    SKELETONS(15, new int[] { 90, 91, 92, 93, 94, 459, 1471, 1575, 1973, 2036, 2037, 2715, 2717, 3065, 3151, 3291, 3581, 3697, 3698, 3699, 3700, 3701, 3702, 3703, 3704, 3705, 3844, 3850, 3851, 4384, 4385, 4386, 5332, 5333, 5334, 5335, 5336, 5337, 5338, 5339, 5340, 5341, 5359, 5365, 5366, 5367, 5368, 5381, 5384, 5385, 5386, 5387, 5388, 5389, 5390, 5391, 5392, 5411, 5412, 5422, 6091, 6092, 6093, 6103, 6104, 6105, 6106, 6107, 6764, 6765, 6766, 6767, 6768, 2050, 2056, 2057, 1539, 7640 }, new String[] { "Skeletons are undead so magic is your best bet against", "them, there is even a spell specially for fighting the undead." }, 1, true, false),
    SPIDERS(1, new int[] { 61, 1004, 1221, 1473, 1474, 63, 4401, 2034, 977, 7207, 134, 1009, 59, 60, 4400, 58, 62, 1478, 2491, 2492, 6376, 6377, }, new String[] { "Spiders are often poisonous, and many varieties are", "camouflaged too." }, 1, false, false),
    SPIRTUAL_MAGES(60, new int[] { 6221, 6231, 6257, 6278 }, new String[] { "Spiritual mages can be found in the icy caverns near", "Trollheim, supporting the cause of their chosen god." }, 83, "Death Plateau"),
    SPIRTUAL_RANGERS(60, new int[] { 6220, 6230, 6256, 6276 }, new String[] { "Spiritual rangers can be found in the icy caverns near", "Trollheim, supporting the cause of their chosen god." }, 63, "Death Plateau"),
    SPIRTUAL_WARRIORS(60, new int[] { 6219, 6229, 6255, 6277, }, new String[] { "Spiritual warriors can be found in the icy caverns near", "Trollheim, supporting the cause of their chosen god." }, 68, "Death Plateau"),
    STEEL_DRAGONS( 85,new int[] { 1592, 3590 }, new String[] { "Steel dragons are dangerous and metallic, with steel", "scales that are far thicker than normal steel armour. As", "you are an accomplished slayer, I am sure you'll be", "able to deal with them easily."}, 1, false, true),
    SUQAHS (65, new int[] { 4527, 4528, 4529, 4530, 4531, 4532, 4533 }, new String[] { "Suqahs can only be found on the mystical Lunar Isle.", "They are capable of melee and magic attacks and often", "drop hide, teeth and herbs!" }, 1, "Lunar Diplomacy"),
    // No access to Lair of Tarn Razorlor but this should be added as a task when there is access
    TERROR_DOGS(1, new int[] { 5417, 5418 }, new String[] { "Terror dogs are the personal pets of Tarn Razorlor.", "Wherever you find him, you will find them. They are", "bad-tempered and generally unfriendly." }, 40, "Haunted Mine"),
    TROLLS(60, new int[] { 72, 3584, 1098, 1096, 1097, 1095, 1101, 1105, 1102, 1103, 1104, 1130, 1131, 1132, 1133, 1134, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1138, 1560, 1561, 1562, 1563, 1564, 1565, 1566, 1935, 1936, 1937, 1938, 1939, 1940, 1941, 1942, 3840, 3841, 3842, 3843, 3845, 1933, 1934, 1115, 1116, 1117, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 391, 392, 393, 394, 395, 396}, new String[] { "Trolls regenerate damage quickly but are still", "vulnerable to poisons, they usually use crushing", "weapons." }, 1, false, false),
    TUROTHS(60, new int[] { 1622, 1611, 1623, 1626, 1627, 1628, 1629, 1630, 7800}, new String[] { "Turoth are large vicious creatures with thick hides.", "You'll need a Leaf-Tipped Spear Sword or Battle-axe,", "Broad Arrows, or a Magic Dart to harm them." }, 55, false, false),
    VAMPIRES(35, new int[] { 1220, 1223, 1225, 6214 }, new String[] { "Vampires are extremely powerful beings. They feed on", "the blood of the living so watch out you don't", "get bitten." }, 1, false, false),
    // Waiting for the wall beast movement bug to be fixed before adding this as a task
    WALL_BEASTS(1, new int[] { 7823 }, new String[] { "Wall Beasts are really much larger creatures but", "you'll only see their arms. You'll want something", "sharp on your head to stop them grabbing you." }, 35, false, false ),
    // No way to get to Poison Swamp Cave
    // If a grapple is implemented from W Castle Wars to E Posion Swamps
    // The dungeon connected and populated
    // then these could be added as a task
    WARPED_TERROR_BIRD(1, new int[] { 6285, 6286, 6287, 6288, 6289, 6290, 6291, 6292, 6293, 6294, 6295, 6323, 6324, 6325, 6326, 6327, 6328, 6329, 6330, 6331, 6332, 6608 }, new String[] { "Warped Creatures can supposedly be found within a", "mysterious dungeon on the eastern edge of the Poison", "Waste. Be aware that to defeat them, you'll need to", "purify them in some way." },56, "The Path of Glouphrie"),
    WARPED_TORTOISE(1, new int[] { 6296, 6297 }, new String[] { "Warped Creatures can supposedly be found within a", "mysterious dungeon on the eastern edge of the Poison", "Waste. Be aware that to defeat them, you'll need to", "purify them in some way." },56, "The Path of Glouphrie"),
    WATERFIENDS(75, new int[] { 5361 }, new String[] { "Waterfiends are creatures of water, which live under", "the Baxtorian Lake. Their watery form is well defended", "against slashing and piercing weapons, so use", "something blunt." }, 1, false, false),
    WEREWOLVES(60, new int[] { 6006, 6007, 6008, 6009, 6010, 6011, 6012, 6013, 6014, 6015, 6016, 6017, 6018, 6019, 6020, 6021, 6022, 6023, 6024, 6025, 6212, 6213, 6607, 6609, 6614, 6617, 6625, 6632, 6644, 6663, 6675, 6686, 6701, 6712, 6724, 6728, }, new String[] { "Werewolves are feral creatures, they are strong and", "tough with sharp claws and teeth." }, 1, false, false),
    WOLVES(20, new int[] { 95, 96, 97, 141, 142, 143, 839, 1198, 1330, 1558, 1559, 1951, 1952, 1953, 1954, 1955, 1956, 4413, 4414, 6046, 6047, 6048, 6049, 6050, 6051, 6052, 6829, 6830, 7005 }, new String[] { "Wolves are pack animals, so you'll always find them", "in groups. Watch out for their bite, it can be nasty." }, 1, false, false),
    ZOMBIES(10, new int[] { 73, 74, 75, 76, 2060, 2714, 2863, 2866, 2869, 2878, 3622, 4392, 4393, 4394, 5293, 5294, 5295, 5296, 5297, 5298, 5299, 5300, 5301, 5302, 5303, 5304, 5305, 5306, 5307, 5308, 5309, 5310, 5311, 5312, 5313, 5314, 5315, 5316, 5317, 5318, 5319, 5320, 5321, 5322, 5323, 5324, 5325, 5326, 5327, 5328, 5329, 5330, 5331, 5375, 5376, 5377, 5378, 5379, 5380, 5393, 5394, 5395, 5396, 5397, 5398, 5399, 5400, 5401, 5402, 5403, 5404, 5405, 5406, 5407, 5408, 5409, 5410, 6099, 6100, 6131, 8149, 8150, 8151, 8152, 8153, 8159, 8160, 8161, 8162, 8163, 8164, 2044, 2045, 2046, 2047, 2048, 2049, 2050, 2051, 2052, 2053, 2054, 2055, 7641, 1465, 1466, 1467, 2837, 2838, 2839, 2840, 2841, 2842, 5629, 5630, 5631, 5632, 5633, 5634, 5635, 5636, 5637, 5638, 5639, 5640, 5641, 5642, 5643, 5644, 5645, 5646, 5647, 5648, 5649, 5650, 5651, 5652, 5653, 5654, 5655, 5656, 5657, 5658, 5659, 5660, 5661, 5662, 5663, 5664, 5665, 2843, 2844, 2845, 2846, 2847, 2848}, new String[] { "Zombies are undead so magic is your best bet against", "them, there is even a spell specially for", "fighting the undead." }, 1, true, false),
    // Bosses need to be handled differently/have a source. They should probably be removed from here
    JAD(90, new int[] { }, new String[] { "You must complete the entire fight cave, including", "TzTok-Jad. Beware, only those skilled in combat should", "attempt this and death will reset your task." }, 1, false, false),
    CHAOS_ELEMENTAL(90, new int[] { 3200 }, new String[] { "The Chaos Elemental roams the deepest Wilderness. It", "can teleport you and make you unequip items randomly." }, 1, false, false),
    GIANT_MOLE(75, new int[] { 3340 }, new String[] { "Dig on the mole-hills in Falador Park to enter the", "mole cave, and bring a lantern he can't extinguish." }, 1, false, false),
    KING_BLACK_DRAGON(75, new int[] { 50 }, new String[] { "The King Black Dragon's cave is reached by pulling a", "lever in the Wilderness. An anti-fire shield is", "strongly recommended." }, 1, false, true),
    COMMANDER_ZILYANA(90, new int[] { 6247 }, new String[] { "Commander Zilyana is in the God Wars Dungeon.", "She frequently uses magic attacks." }, 1, false, false),
    GENERAL_GRARDOOR(90, new int[] { 6260 }, new String[] { "General Graardor is in the God Wars Dungeon.", "He uses melee and ranged attacks." }, 1, false, false),
    KRIL_TSUTSAROTH(90, new int[] { 6203 }, new String[] { "K'ril Tsutsaroth is in the God Wars Dungeon.", "He's poisonous and he can hit through prayers." }, 1, false, false),
    KREE_ARRA(90, new int[] { 6222 }, new String[] { "Kree'arra roosts in the God Wars Dungeon.", "Melee attacks don't work on flying creatures,", "and you'll need a grappling hook to get in." }, 1, false, false),
    ;

    static final HashMap<Integer, Tasks> taskMap = new HashMap<>();
    static{
        Arrays.stream(Tasks.values()).forEach(entry -> Arrays.stream(entry.ids).forEach(id -> taskMap.putIfAbsent(id,entry)));
    }
    public final int levelReq;
    public final int combatCheck;
    public final String[] info;
    public final int[] ids;
    public boolean undead = false;
    public boolean dragon = false;
    public String questReq = "";
    Tasks(int combatCheck, int[] ids, String[] info, int levelReq, boolean undead, boolean dragon){
        this.levelReq = levelReq;
        this.ids = ids;
        this.info = info;
        this.undead = undead;
        this.dragon = dragon;
        this.combatCheck = combatCheck;
    }

    Tasks (int combatCheck, int[] ids, String[] info, int levelReq, String questReq) {
        this.combatCheck = combatCheck;
        this.ids = ids;
        this.info = info;
        this.levelReq = levelReq;
        this.questReq = questReq;
    }

    public int[] getNpcs(){
        return ids;
    }

    public String[] getTip(){
        return info;
    }

    public boolean hasQuestRequirements (Player player) {
        return questReq.isEmpty() || hasRequirement(player, questReq, false);
    }

    public static Tasks forId(int id){
        return taskMap.get(id);
    }

    public String getName(){
        return NPCDefinition.forId(ids[0]).getName();
    }

}

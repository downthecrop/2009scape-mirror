package content.minigame.shadesofmortton

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScrollPlugin
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items

// data class for the chest loot
data class ChestLoot(val itemId: Int, val minAmount: Int, val maxAmount: Int, val weight: Int = 1)

// defines the tiers in each table
data class TierLootTable(
    val common: List<ChestLoot>,
    val uncommon: List<ChestLoot>,
    val rare: List<ChestLoot>
)

// coin roll bounds for each tier. This calculation was taken from OSRS.
enum class ChestTier(val minCoin: Int, val maxCoin: Int) {
    BRONZE(400, 600),
    STEEL(900, 1100),
    BLACK(1500, 2000),
    SILVER(3600, 7000),
}

// higher tier keys have a better chance of rolling better loot. All tiers of key roll the same loot table.
// I can't find any hard data on how this was implemented, so I have separated all the chest tables into my best guess at 'rarity', using RS3 and OSRS table data on their probabilities or 'rare', 'uncommon', 'common' notes.
enum class KeyLoop(val commonChance: Int, val uncommonChance: Int, val rareChance: Int) { // commonChance isn't used because it's just caught at the end of a when statement
    RED(80, 15, 5),      // 5% chance for rare
    BROWN(70, 20, 10),   // 10% chance for rare
    CRIMSON(60, 25, 15), // 15% chance for rare
    BLACK(50, 30, 20),   // 20% chance for rare
    PURPLE(40, 35, 25)   // 25% chance for rare
}

/*
 * Reward lists per runeHQ (historical):
 *
 * Bronze key: Amulet of defence, Black axe, Black dagger, Black dagger(p), Black mace, Black spear, Chaos rune (25), Clue scroll (level 1), Coins (1-495), Gold bar, Magic staff, Mithril bar, Mithril dagger, Mithril dagger(p), Sapphire ring, Silver bar, Steel axe, Steel longsword, Steel mace, Steel med helm, Steel scimitar, Steel spear, Steel spear(p), Steel sword, Swamp paste (6-9)
 * Steel key: Adamantite bar (1-3), Adamant dagger, Adamant dagger (p), Amulet of strength, Black longsword, Black med helm, Black scimitar, Black spear, Black sword, Black warhammer, Chaos rune (10-29), Clue scroll (level 2), Coins (2-338), Emerald ring, Fine cloth, Mithril mace, Mithril med helm, Mithril spear, Mithril spear(p), Mithril sword, Nature rune (10-29), Steel 2h sword, Steel battleaxe, Steel chainbody, Steel kiteshield, Steel warhammer, Studded chaps, Swamp paste (15-25), Willow logs (5-15) (Noted)
 * Black key: Adamant axe, Adamant mace, Adamant med helm, Adamant scimitar, Adamant sword, Amulet of magic, Black 2h sword, Black battleaxe, Black chainbody, Black full helm, Black kiteshield, Black platelegs, Black plateskirt, Black spear, Black sq shield, Clue scroll (level 2), Coins (1-665), Death rune (10-29), Fine cloth, Flamtaer hammer, Mithril 2h sword, Mithril battleaxe, Mithril chainbody, Mithril full helm, Mithril kiteshield, Mithril longsword, Mithril mace, Mithril platelegs, Mithril scimitar, Mithril sq shield, Mithril warhammer, Nature rune (10-29), Ruby ring, Staff of air, Staff of earth, Staff of fire, Staff of water, Steel platebody, Steel plateskirt, Swamp paste (20-35), Willow logs (5-14) (Noted), Yew logs (5-14) (Noted)
 * Silver key: Adamant 2h sword, Adamant battleaxe, Adamant chainbody, Adamant full helm, Adamant kiteshield, Adamant longsword, Adamant platebody, Adamant platelegs, Adamant plateskirt, Adamant spear, Adamant spear (p), Adamant sq shield, Adamant warhammer, Amulet of power, Battlestaff, Black platebody, Black spear, Blood rune (11, 21, 26), Clue scroll (level 3), Coins (68-2080), Death rune (14-28), Diamond ring, Fine cloth, Flamtaer hammer, Magic logs (4-7) (Noted), Mithril platebody, Mithril plateskirt, Rune chainbody, Rune longsword, Rune med helm, Rune scimitar, Rune sword, Swamp paste (32-50), Yew logs (7) (Noted)
 *
 * Some key opening videos:
 * Bronze: https://www.youtube.com/watch?v=mNW8x7rixoU, https://www.youtube.com/watch?v=QMrg9I5lVsc, https://www.youtube.com/watch?v=zwbO_oMWlMk, https://www.youtube.com/watch?v=OXZ3zePiuUA
 * Black: https://www.youtube.com/watch?v=pf9_Jbnf9i4, https://www.youtube.com/watch?v=xtSany9isbg
 */

// My source for the item list was archive RuneHQ, it cross-references to OSRS pretty well as a sanity check.
object ShadeChestRewards {

    // --------------------------------------------
    // BRONZE CHESTS
    // --------------------------------------------

    val bronzeCommon = listOf(
        ChestLoot(Items.STEEL_AXE_1353, 1, 1),
        ChestLoot(Items.STEEL_SPEAR_1241, 1, 1),
        ChestLoot(Items.STEEL_SPEARP_1255, 1, 1),
        ChestLoot(Items.STEEL_SWORD_1281, 1, 1),
        ChestLoot(Items.STEEL_MACE_1424, 1, 1),
        ChestLoot(Items.STEEL_SCIMITAR_1325, 1, 1),
        ChestLoot(Items.STEEL_LONGSWORD_1295, 1, 1),
        ChestLoot(Items.STEEL_MED_HELM_1141, 1, 1),
        ChestLoot(Items.MAGIC_STAFF_1389, 1, 1),
        ChestLoot(Items.SILVER_BAR_2355, 3, 3),
    )

    val bronzeUncommon = listOf(
        ChestLoot(Items.MAGIC_STAFF_1389, 1, 1),
        ChestLoot(Items.BLACK_DAGGER_1217, 1, 1),
        ChestLoot(Items.BLACK_DAGGERP_1233, 1, 1),
        ChestLoot(Items.BLACK_MACE_1426, 1, 1),
        ChestLoot(Items.BLACK_AXE_1361, 1, 1),
        ChestLoot(Items.BLACK_SPEAR_4580, 1, 1),
        ChestLoot(Items.CHAOS_RUNE_562, 12, 29),
        ChestLoot(Items.GOLD_BAR_2357, 3, 3),
    )

    val bronzeRare = listOf(
        ChestLoot(Items.BLACK_SPEAR_4580, 1, 1),
        ChestLoot(Items.CHAOS_RUNE_562, 12, 29),
        ChestLoot(Items.MITHRIL_DAGGER_1209, 1, 1),
        ChestLoot(Items.MITHRIL_DAGGERP_1225, 1, 1),
        ChestLoot(Items.AMULET_OF_DEFENCE_1729, 1, 1),
        ChestLoot(Items.MITHRIL_BAR_2359, 3, 3),
        ChestLoot(ClueScrollPlugin.getClue(ClueLevel.EASY).id, 1, 1),
        ChestLoot(Items.SAPPHIRE_RING_1637, 1, 1),
    )

    // --------------------------------------------
    // STEEL CHESTS
    // --------------------------------------------

    val steelCommon = listOf(
        ChestLoot(Items.WILLOW_LOGS_1520, 5, 15), // noted
        ChestLoot(Items.STUDDED_CHAPS_1097, 1, 1),
        ChestLoot(Items.STEEL_CHAINBODY_1105, 1, 1),
        ChestLoot(Items.STEEL_WARHAMMER_1339, 1, 1),
        ChestLoot(Items.STEEL_BATTLEAXE_1365, 1, 1),
        ChestLoot(Items.STEEL_2H_SWORD_1311, 1, 1),
        ChestLoot(Items.STEEL_KITESHIELD_1193, 1, 1),
        ChestLoot(Items.BLACK_SPEAR_4580, 1, 1),
        ChestLoot(Items.EMERALD_RING_1639, 1, 1),
    )

    val steelUncommon = listOf(
        ChestLoot(Items.NATURE_RUNE_561, 10, 29),
        ChestLoot(Items.CHAOS_RUNE_562, 10, 29),
        ChestLoot(Items.BLACK_SCIMITAR_1327, 1, 1),
        ChestLoot(Items.BLACK_MED_HELM_1151, 1, 1),
        ChestLoot(Items.BLACK_WARHAMMER_1341, 1, 1),
        ChestLoot(Items.BLACK_SWORD_1283, 1, 1),
        ChestLoot(Items.BLACK_LONGSWORD_1297, 1, 1),
        ChestLoot(Items.MITHRIL_SPEAR_1243, 1, 1),
        ChestLoot(Items.MITHRIL_SPEARP_1257, 1, 1),
    )

    val steelRare = listOf(
        ChestLoot(Items.MITHRIL_MACE_1428, 1, 1),
        ChestLoot(Items.MITHRIL_SWORD_1285, 1, 1),
        ChestLoot(Items.MITHRIL_MED_HELM_1143, 1, 1),
        ChestLoot(Items.ADAMANT_DAGGERP_1227, 1, 1),
        ChestLoot(Items.ADAMANTITE_BAR_2361, 3, 3),
        ChestLoot(Items.ADAMANT_DAGGER_1211, 1, 1),
        ChestLoot(Items.AMULET_OF_STRENGTH_1725, 1, 1),
        ChestLoot(Items.FINE_CLOTH_3470, 1, 1),
        ChestLoot(ClueScrollPlugin.getClue(ClueLevel.MEDIUM).id, 1, 1),
    )

    // --------------------------------------------
    // BLACK CHESTS
    // --------------------------------------------

    val blackCommon = listOf(
        ChestLoot(Items.STEEL_PLATEBODY_1119, 1, 1),
        ChestLoot(Items.STEEL_PLATESKIRT_1083, 1, 1),
        ChestLoot(Items.BLACK_SPEAR_4580, 1, 1),
        ChestLoot(Items.BLACK_SQ_SHIELD_1179, 1, 1),
        ChestLoot(Items.BLACK_FULL_HELM_1165, 1, 1),
        ChestLoot(Items.BLACK_CHAINBODY_1107, 1, 1),
        ChestLoot(Items.BLACK_PLATELEGS_1077, 1, 1),
        ChestLoot(Items.BLACK_PLATESKIRT_1089, 1, 1),
        ChestLoot(Items.AMULET_OF_MAGIC_1727, 1, 1),
        ChestLoot(Items.STAFF_OF_AIR_1381, 1, 1),
        ChestLoot(Items.STAFF_OF_WATER_1383, 1, 1),
        ChestLoot(Items.STAFF_OF_EARTH_1385, 1, 1),
        ChestLoot(Items.STAFF_OF_FIRE_1387, 1, 1),
        ChestLoot(Items.WILLOW_LOGS_1520, 5, 14), // Noted
    )

    val blackUncommon = listOf(
        ChestLoot(Items.BLACK_KITESHIELD_1195, 1, 1),
        ChestLoot(Items.BLACK_2H_SWORD_1313, 1, 1),
        ChestLoot(Items.BLACK_BATTLEAXE_1367, 1, 1),
        ChestLoot(Items.MITHRIL_MACE_1428, 1, 1),
        ChestLoot(Items.MITHRIL_LONGSWORD_1299, 1, 1),
        ChestLoot(Items.MITHRIL_WARHAMMER_1343, 1, 1),
        ChestLoot(Items.MITHRIL_BATTLEAXE_1369, 1, 1),
        ChestLoot(Items.MITHRIL_SCIMITAR_1329, 1, 1),
        ChestLoot(Items.MITHRIL_FULL_HELM_1159, 1, 1),
        ChestLoot(Items.MITHRIL_SQ_SHIELD_1181, 1, 1),
        ChestLoot(Items.MITHRIL_PLATELEGS_1071, 1, 1),
        ChestLoot(Items.MITHRIL_CHAINBODY_1109, 1, 1),
        ChestLoot(Items.RUBY_RING_1641, 1, 1),
        ChestLoot(Items.NATURE_RUNE_561, 10, 29),
    )

    val blackRare = listOf(
        ChestLoot(Items.MITHRIL_KITESHIELD_1197, 1, 1),
        ChestLoot(Items.MITHRIL_2H_SWORD_1315, 1, 1),
        ChestLoot(Items.ADAMANT_MACE_1430, 1, 1),
        ChestLoot(Items.ADAMANT_AXE_1357, 1, 1),
        ChestLoot(Items.ADAMANT_SCIMITAR_1331, 1, 1),
        ChestLoot(Items.ADAMANT_SWORD_1287, 1, 1),
        ChestLoot(Items.ADAMANT_MED_HELM_1145, 1, 1),
        ChestLoot(Items.YEW_LOGS_1516, 5, 14), // Noted
        ChestLoot(Items.DEATH_RUNE_560, 10, 29),
        ChestLoot(Items.FINE_CLOTH_3470, 1, 1),
        ChestLoot(ClueScrollPlugin.getClue(ClueLevel.MEDIUM).id, 1, 1),
        ChestLoot(Items.FLAMTAER_HAMMER_3678, 1, 1),
    )

    // --------------------------------------------
    // SILVER CHESTS
    // --------------------------------------------

    val silverCommon = listOf(
        ChestLoot(Items.BLACK_SPEAR_4580, 1, 1),
        ChestLoot(Items.BLACK_PLATEBODY_1125, 1, 1),
        ChestLoot(Items.MITHRIL_PLATESKIRT_1085, 1, 1),
        ChestLoot(Items.MITHRIL_PLATEBODY_1121, 1, 1),
        ChestLoot(Items.ADAMANT_SPEAR_1245, 1, 1),
        ChestLoot(Items.ADAMANT_SPEARP_1259, 1, 1),
        ChestLoot(Items.ADAMANT_FULL_HELM_1161, 1, 1),
        ChestLoot(Items.DEATH_RUNE_560, 10, 30),
        ChestLoot(Items.DIAMOND_RING_1643, 1, 1),
    )

    val silverUncommon = listOf(
        ChestLoot(Items.BATTLESTAFF_1391, 1, 1),
        ChestLoot(Items.ADAMANT_WARHAMMER_1345, 1, 1),
        ChestLoot(Items.ADAMANT_LONGSWORD_1301, 1, 1),
        ChestLoot(Items.ADAMANT_CHAINBODY_1111, 1, 1),
        ChestLoot(Items.ADAMANT_PLATELEGS_1073, 1, 1),
        ChestLoot(Items.ADAMANT_PLATESKIRT_1091, 1, 1),
        ChestLoot(Items.ADAMANT_SQ_SHIELD_1183, 1, 1),
        ChestLoot(Items.AMULET_OF_POWER_1731, 1, 1),
        ChestLoot(Items.BLOOD_RUNE_565, 10, 30),
        ChestLoot(Items.YEW_LOGS_1516, 5, 10), // Noted
    )

    val silverRare = listOf(
        ChestLoot(Items.ADAMANT_2H_SWORD_1317, 1, 1),
        ChestLoot(Items.ADAMANT_BATTLEAXE_1371, 1, 1),
        ChestLoot(Items.ADAMANT_PLATEBODY_1123, 1, 1),
        ChestLoot(Items.ADAMANT_KITESHIELD_1199, 1, 1),
        ChestLoot(Items.RUNE_CHAINBODY_1113, 1, 1),
        ChestLoot(Items.RUNE_SCIMITAR_1333, 1, 1),
        ChestLoot(Items.RUNE_LONGSWORD_1303, 1, 1),
        ChestLoot(Items.RUNE_MED_HELM_1147, 1, 1),
        ChestLoot(Items.RUNE_SWORD_1289, 1, 1),
        ChestLoot(Items.FINE_CLOTH_3470, 1, 1),
        ChestLoot(Items.FLAMTAER_HAMMER_3678, 1, 1),
        ChestLoot(Items.MAGIC_LOGS_1514, 5, 10), // Noted
        ChestLoot(ClueScrollPlugin.getClue(ClueLevel.HARD).id, 1, 1),
    )

    // --------------------------------------------
    // LOOT GEN
    // --------------------------------------------

    // all the tables together
    val tierTables = mapOf(
        ChestTier.BRONZE to TierLootTable(bronzeCommon, bronzeUncommon, bronzeRare),
        ChestTier.STEEL to TierLootTable(steelCommon, steelUncommon, steelRare),
        ChestTier.BLACK to TierLootTable(blackCommon, blackUncommon, blackRare),
        ChestTier.SILVER to TierLootTable(silverCommon, silverUncommon, silverRare)
    )

    /**
     * Rolls the loot a specific tier of chest with the provided key.
     */
    fun generateLoot(tier: ChestTier, loop: KeyLoop): List<Item> {

        // the list of rewards that gets returned
        val rewards = mutableListOf<Item>()

        // fetch the loot tables
        val lootTable = tierTables[tier] ?: return emptyList()

        // roll for rarity. each chest type has a common, uncommon, or rare table. Better tiers of keys have a better chance of rolling better rarity.
        val rarityRoll = RandomFunction.random(1, 101)

        // select the table based on rarity roll
        val selectedArray = when {
            rarityRoll <= loop.rareChance -> lootTable.rare
            rarityRoll <= loop.uncommonChance -> lootTable.uncommon
            else -> lootTable.common
        }

        // pick a random item from the table
        val mainReward = selectedArray.random()

        // select amount
        val mainAmt = if (mainReward.minAmount == mainReward.maxAmount) {
            mainReward.minAmount
        } else {
            RandomFunction.random(mainReward.minAmount, mainReward.maxAmount + 1)
        }

        // the main reward item
        val mainItem = Item(mainReward.itemId, mainAmt)
        rewards.add(mainItem)

        // you can also get coins along with the item. I got this logic from OSRS (ugh)
        val itemValue = mainItem.definition?.value ?: 0
        val mainLootValue = itemValue * mainAmt
        val randomCoinRoll = RandomFunction.random(tier.minCoin, tier.maxCoin + 1)
        if (randomCoinRoll > mainLootValue) {
            val coinAmount = (randomCoinRoll - mainLootValue) / 3
            if (coinAmount > 0) {
                rewards.add(Item(Items.COINS_995, coinAmount))
            }
        }

        // swamp paste is rolled as a 5% chance because that's my best guess from watching vids
        if (RandomFunction.random(100) < 10) {
            when (tier) {
                ChestTier.BRONZE -> {rewards.add(Item(Items.SWAMP_PASTE_1941, RandomFunction.random(6, 10)))} // 6-9
                ChestTier.BLACK -> {rewards.add(Item(Items.SWAMP_PASTE_1941, RandomFunction.random(15, 26)))} // 15-25
                ChestTier.STEEL -> {rewards.add(Item(Items.SWAMP_PASTE_1941, RandomFunction.random(20, 35)))} // 20-35
                ChestTier.SILVER -> {rewards.add(Item(Items.SWAMP_PASTE_1941, RandomFunction.random(32, 51)))} // 32-50
            }
        }

        return rewards
    }
}
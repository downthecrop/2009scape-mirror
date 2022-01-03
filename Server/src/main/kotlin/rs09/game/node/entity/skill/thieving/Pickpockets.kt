package rs09.game.node.entity.skill.thieving

import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem
import java.util.stream.IntStream

enum class Pickpockets(val ids: IntArray, val requiredLevel: Int, val low: Double, val high: Double, val experience: Double, val stunDamageMin: Int, val stunDamageMax: Int, val stunTime: Int, val table: WeightBasedTable) {
    MAN(intArrayOf(1, 2, 3, 4, 5, 6, 16, 24, 25, 170, 3915, 3226, 5924, 5923), 1, 180.0, 240.0, 8.0, 1, 1,5, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,3,3,1.0,true)
    )),
    FARMER(intArrayOf(7, 1757, 1758), 10, 180.0, 240.0, 14.5, 1,1,5,WeightBasedTable.create(
        WeightedItem(Items.COINS_995,9,9,1.0,true),
        WeightedItem(Items.POTATO_SEED_5318,1,1,1.0,true)
    )),
    MALE_HAM_MEMBER(intArrayOf(1714), 20, 117.0, 240.0, 22.5, 1,3,4, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,1,21,5.5),
        WeightedItem(Items.TINDERBOX_590,1,1,5.0),
        WeightedItem(Items.LOGS_1511,1,1,7.0),
        WeightedItem(Items.UNCUT_JADE_1627,1,1,2.5),
        WeightedItem(Items.UNCUT_OPAL_1625,1,1,2.5),
        WeightedItem(Items.RAW_ANCHOVIES_321,1,1,7.0),
        WeightedItem(Items.RAW_CHICKEN_2138,1,1,3.5),
        WeightedItem(Items.HAM_CLOAK_4304,1,1,0.25),
        WeightedItem(Items.HAM_HOOD_4302,1,1,0.25),
        WeightedItem(Items.HAM_LOGO_4306,1,1,0.25),
        WeightedItem(Items.HAM_ROBE_4300,1,1,0.25),
        WeightedItem(Items.HAM_SHIRT_4298,1,1,0.25),
        WeightedItem(Items.BOOTS_4310,1,1,1.0),
        WeightedItem(Items.GLOVES_4308,1,1,1.0),
        WeightedItem(Items.BRONZE_PICKAXE_1265,1,1,5.0),
        WeightedItem(Items.IRON_PICKAXE_1267,1,1,5.0),
        WeightedItem(Items.STEEL_PICKAXE_1269,1,1,2.5),
        WeightedItem(Items.GRIMY_GUAM_199,1,1,2.0),
        WeightedItem(Items.GRIMY_HARRALANDER_205,1,1,2.0),
        WeightedItem(Items.GRIMY_KWUARM_213,1,1,2.0),
        WeightedItem(Items.GRIMY_MARRENTILL_201,1,1,1.5),
        WeightedItem(Items.RUSTY_SWORD_686,1,1,3.5),
        WeightedItem(Items.BROKEN_ARMOUR_698,1,1,3.5),
        WeightedItem(Items.BROKEN_STAFF_689,1,1,3.2),
        WeightedItem(Items.BROKEN_ARROW_687,1,1,3.1),
        WeightedItem(Items.BUTTONS_688,1,1,3.0)
    ).insertEasyClue(1.0)),
    FEMALE_HAM_MEMBER(intArrayOf(1715), 15, 135.0, 240.0, 18.5, 1,3,4, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,1,21,5.5),
        WeightedItem(Items.TINDERBOX_590,1,1,5.0),
        WeightedItem(Items.LOGS_1511,1,1,7.0),
        WeightedItem(Items.UNCUT_JADE_1627,1,1,2.5),
        WeightedItem(Items.UNCUT_OPAL_1625,1,1,2.5),
        WeightedItem(Items.RAW_ANCHOVIES_321,1,1,7.0),
        WeightedItem(Items.RAW_CHICKEN_2138,1,1,3.5),
        WeightedItem(Items.HAM_CLOAK_4304,1,1,0.25),
        WeightedItem(Items.HAM_HOOD_4302,1,1,0.25),
        WeightedItem(Items.HAM_LOGO_4306,1,1,0.25),
        WeightedItem(Items.HAM_SHIRT_4298,1,1,0.25),
        WeightedItem(Items.HAM_ROBE_4300,1,1,0.25),
        WeightedItem(Items.BOOTS_4310,1,1,1.0),
        WeightedItem(Items.GLOVES_4308,1,1,1.0),
        WeightedItem(Items.BRONZE_PICKAXE_1265,1,1,5.0),
        WeightedItem(Items.IRON_PICKAXE_1267,1,1,5.0),
        WeightedItem(Items.STEEL_PICKAXE_1269,1,1,2.5),
        WeightedItem(Items.GRIMY_GUAM_199,1,1,2.0),
        WeightedItem(Items.GRIMY_HARRALANDER_205,1,1,2.0),
        WeightedItem(Items.GRIMY_KWUARM_213,1,1,2.0),
        WeightedItem(Items.GRIMY_MARRENTILL_201,1,1,1.5),
        WeightedItem(Items.RUSTY_SWORD_686,1,1,3.5),
        WeightedItem(Items.BROKEN_ARMOUR_698,1,1,3.5),
        WeightedItem(Items.BROKEN_STAFF_689,1,1,3.2),
        WeightedItem(Items.BROKEN_ARROW_687,1,1,3.1),
        WeightedItem(Items.BUTTONS_688,1,1,3.0)
    ).insertEasyClue(1.0)),
    WARRIOR(intArrayOf(15, 18), 25, 84.0, 240.0, 26.0, 2, 2, 5, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,18,18,1.0,true)
    )),
    ROGUE(intArrayOf(187, 2267, 2268, 2269, 8122), 32, 74.0, 240.0,35.5, 2, 2, 5, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,25,40,5.0,true),
        WeightedItem(Items.JUG_OF_WINE_1993,1,1,6.0),
        WeightedItem(Items.AIR_RUNE_556,8,8,8.0),
        WeightedItem(Items.LOCKPICK_1523,1,1,5.0),
        WeightedItem(Items.IRON_DAGGERP_1219,1,1,1.0)
    )),
    CAVE_GOBLIN(IntStream.rangeClosed(5752, 5768).toArray(), 36, 72.0, 240.0, 40.0, 1,1,5, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,30,30,6.5),
        WeightedItem(Items.OIL_LAMP_4522,1,1,0.5),
        WeightedItem(Items.BULLSEYE_LANTERN_4544,1,1,0.5),
        WeightedItem(Items.UNLIT_TORCH_596,1,1,0.5),
        WeightedItem(Items.TINDERBOX_590,1,1,0.5),
        WeightedItem(Items.SWAMP_TAR_1939,1,1,0.5),
        WeightedItem(Items.IRON_ORE_441,1,4,0.25)
    )),
    MASTER_FARMER(intArrayOf(2234, 2235, NPCs.MARTIN_THE_MASTER_GARDENER_3299), 38, 90.0, 240.0, 43.0, 3, 3, 5, WeightBasedTable.create(
        WeightedItem(Items.POTATO_SEED_5318,1,3,50.0),
        WeightedItem(Items.ONION_SEED_5319,1,3,50.0),
        WeightedItem(Items.CABBAGE_SEED_5324,1,3,50.0),
        WeightedItem(Items.TOMATO_SEED_5322,1,2,50.0),
        WeightedItem(Items.SWEETCORN_SEED_5320,1,2,50.0),
        WeightedItem(Items.STRAWBERRY_SEED_5323,1,1,25.0),
        WeightedItem(Items.WATERMELON_SEED_5321,1,1,8.0),
        WeightedItem(Items.BARLEY_SEED_5305,1,4,50.0),
        WeightedItem(Items.HAMMERSTONE_SEED_5307,1,3,50.0),
        WeightedItem(Items.ASGARNIAN_SEED_5308,1,3,50.0),
        WeightedItem(Items.JUTE_SEED_5306,1,3,50.0),
        WeightedItem(Items.YANILLIAN_SEED_5309,1,2,25.0),
        WeightedItem(Items.KRANDORIAN_SEED_5310,1,2,25.0),
        WeightedItem(Items.WILDBLOOD_SEED_5311,1,1,8.0),
        WeightedItem(Items.MARIGOLD_SEED_5096,1,1,50.0),
        WeightedItem(Items.NASTURTIUM_SEED_5098,1,1,50.0),
        WeightedItem(Items.ROSEMARY_SEED_5097,1,1,50.0),
        WeightedItem(Items.WOAD_SEED_5099,1,1,50.0),
        WeightedItem(Items.LIMPWURT_SEED_5100,1,1,25.0),
        WeightedItem(Items.REDBERRY_SEED_5101,1,1,50.0),
        WeightedItem(Items.CADAVABERRY_SEED_5102,1,1,50.0),
        WeightedItem(Items.DWELLBERRY_SEED_5103,1,1,25.0),
        WeightedItem(Items.JANGERBERRY_SEED_5104,1,1,25.0),
        WeightedItem(Items.WHITEBERRY_SEED_5105,1,1,25.0),
        WeightedItem(Items.GUAM_SEED_5291,1,1,50.0),
        WeightedItem(Items.MARRENTILL_SEED_5292,1,1,50.0),
        WeightedItem(Items.TARROMIN_SEED_5293,1,1,50.0),
        WeightedItem(Items.HARRALANDER_SEED_5294,1,1,25.0),
        WeightedItem(Items.RANARR_SEED_5295,1,1,8.0),
        WeightedItem(Items.TOADFLAX_SEED_5296,1,1,8.0),
        WeightedItem(Items.IRIT_SEED_5297,1,1,8.0),
        WeightedItem(Items.AVANTOE_SEED_5298,1,1,8.0),
        WeightedItem(Items.KWUARM_SEED_5299,1,1,8.0),
        WeightedItem(Items.SNAPDRAGON_SEED_5300,1,1,5.0),
        WeightedItem(Items.CADANTINE_SEED_5301,1,1,8.0),
        WeightedItem(Items.LANTADYME_SEED_5302,1,1,5.0),
        WeightedItem(Items.DWARF_WEED_SEED_5303,1,1,5.0),
        WeightedItem(Items.TORSTOL_SEED_5304,1,1,5.0)
    )),
    GUARD(intArrayOf(9, 32, 206, 296, 297, 298, 299, 344, 345, 346, 368, 678, 812, 9, 32, 296, 297, 298, 299, 2699, 2700, 2701, 2702, 2703, 3228, 3229, 3230, 3231, 3232, 3233, 3241, 3407, 3408, 4307, 4308, 4309, 4310, 4311, 5919, 5920), 40, 50.0, 240.0, 46.5, 2,2,5, WeightBasedTable.create(
       WeightedItem(Items.COINS_995,30,30,1.0,true)
    )),
    FREMENNIK_CITIZEN(intArrayOf(2462), 45, 65.0, 240.0, 65.0, 2, 2, 5, WeightBasedTable.create(
       WeightedItem(Items.COINS_995,40,40,1.0,true)
    )),
    BEARDED_BANDIT(intArrayOf(1880, 1881, 6174), 45, 50.0, 240.0, 65.0, 5,5,5, WeightBasedTable.create(
       WeightedItem(Items.ANTIPOISON4_2446,1,1,1.0),
       WeightedItem(Items.LOCKPICK_1523,1,1,2.0),
       WeightedItem(Items.COINS_995,1,1,4.0)
    )),
    DESERT_BANDIT(intArrayOf(1926, 1921), 53, 50.0, 240.0, 79.5, 3,3,5, WeightBasedTable.create(
       WeightedItem(Items.COINS_995,50,1,3.0),
       WeightedItem(Items.ANTIPOISON4_2446,1,1,1.0),
       WeightedItem(Items.LOCKPICK_1523,1,1,1.0)
    )),
    KNIGHT_OF_ADROUGNE(intArrayOf(23, 26), 55, 50.0, 240.0, 84.3, 3,3,5, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,50,50,1.0,true)
    )),
    YANILLE_WATCHMAN(intArrayOf(34), 65, 137.5, 50.0, 240.0, 3,3,5, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,60,60,1.0,true),
        WeightedItem(Items.BREAD_2309,1,1,1.0,true)
    )),
    MENAPHITE_THUG(intArrayOf(1905), 65, 50.0, 240.0, 137.5, 5,5,5, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,60,60,1.0,true)
    )),
    PALADIN(intArrayOf(20, 2256), 70, 50.0, 150.0,151.75, 3,3,5, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,80,80,1.0,true),
        WeightedItem(Items.CHAOS_RUNE_562,2,2,1.0,true)
    )),
    GNOME(intArrayOf(66, 67, 68, 168, 169, 2249, 2250, 2251, 2371, 2649, 2650, 6002, 6004), 75, 8.0, 120.0, 198.5, 1,1,5, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,300,300,2.5),
        WeightedItem(Items.EARTH_RUNE_557,1,1,3.5),
        WeightedItem(Items.GOLD_ORE_445,1,1,1.0),
        WeightedItem(Items.FIRE_ORB_569,1,1,5.0),
        WeightedItem(Items.SWAMP_TOAD_2150,1,1,8.0),
        WeightedItem(Items.KING_WORM_2162,1,1,9.0)
    )),
    HERO(intArrayOf(21), 80, 6.0, 100.0,273.3, 6,6,4, WeightBasedTable.create(
        WeightedItem(Items.COINS_995,200,300,1.5),
        WeightedItem(Items.DEATH_RUNE_560,2,2,1.0),
        WeightedItem(Items.BLOOD_RUNE_565,1,1,0.5),
        WeightedItem(Items.FIRE_ORB_569,1,1,2.5),
        WeightedItem(Items.DIAMOND_1601,1,1,2.0),
        WeightedItem(Items.GOLD_ORE_444,1,1,1.5),
        WeightedItem(Items.JUG_OF_WINE_1993,1,1,3.0)
    ));


    companion object {
        val idMap = HashMap<Int,Pickpockets>(values().size * 5)

        init {
            values().forEach {
                it.ids.forEach { id -> idMap[id] = it }
            }
        }

        @JvmStatic
        fun forID(id: Int): Pickpockets? {
            return idMap[id]
        }
    }


    fun getSuccessChance(player: Player): Double{
        return RandomFunction.getSkillSuccessChance(low,high,player.skills.getLevel(Skills.THIEVING))
    }
}
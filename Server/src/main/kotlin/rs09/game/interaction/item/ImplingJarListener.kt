package rs09.game.interaction.item

import api.Container
import api.*
import core.game.content.ttrail.ClueScrollPlugin
import org.rs09.consts.Items
import rs09.game.content.global.WeightBasedTable
import rs09.game.content.global.WeightedItem
import rs09.game.interaction.InteractionListener
import rs09.game.system.SystemLogger

class ImplingJarListener : InteractionListener() {

    val JARS = ImplingLoot.values().map { it.jarId }.toIntArray()

    override fun defineListeners() {
        on(JARS, ITEM, "loot"){player, node ->
            val jar = node.asItem()

            val loot = ImplingLoot.forId(jar.id)?.roll()?.first() ?: return@on false

            if(removeItem(player, jar, Container.INVENTORY)) {
                addItemOrDrop(player, loot.id, loot.amount)
                addItemOrDrop(player, Items.IMPLING_JAR_11260, 1)
            }

            return@on true
        }
    }
}


enum class ImplingLoot(val jarId: Int, val table: WeightBasedTable){
    BABY_IMPLING(Items.BABY_IMPLING_JAR_11238, WeightBasedTable.create(
        WeightedItem(Items.CHISEL_1755, 1, 1, 10.0),
        WeightedItem(Items.THREAD_1734, 1, 1, 10.0),
        WeightedItem(Items.NEEDLE_1733, 1, 1, 10.0),
        WeightedItem(Items.KNIFE_946,1, 1, 10.0),
        WeightedItem(Items.CHEESE_1985, 1, 1, 10.0),
        WeightedItem(Items.HAMMER_2347, 1, 1, 10.0),
        WeightedItem(0, 1, 1, 10.0),
        WeightedItem(Items.BALL_OF_WOOL_1759, 1, 1, 10.0),
        WeightedItem(Items.ANCHOVIES_319, 1, 1, 10.0),
        WeightedItem(Items.SPICE_2007, 1, 1, 1.0),
        WeightedItem(Items.FLAX_1779, 1, 1, 1.0),
        WeightedItem(Items.MUD_PIE_7170, 1, 1, 1.0),
        WeightedItem(Items.SEAWEED_401, 1, 1, 1.0),
        WeightedItem(Items.AIR_TALISMAN_1438, 1, 1, 1.0),
        WeightedItem(Items.SILVER_BAR_2355, 1, 1, 1.0),
        WeightedItem(Items.SAPPHIRE_1607, 1, 1, 1.0),
        WeightedItem(Items.HARD_LEATHER_1743, 1, 1, 1.0),
        WeightedItem(Items.LOBSTER_379, 1, 1, 1.0),
        WeightedItem(Items.SOFT_CLAY_1761, 1, 1, 1.0)
    ).insertEasyClue(1.0)),

    YOUNG_IMPLING(Items.YOUNG_IMPLING_JAR_11240, WeightBasedTable.create(
        WeightedItem(Items.STEEL_NAILS_1539, 5, 5, 10.0),
        WeightedItem(Items.LOCKPICK_1523, 1, 1, 10.0),
        WeightedItem(Items.PURE_ESSENCE_7936, 1, 1, 10.0),
        WeightedItem(Items.TUNA_361, 1, 1, 10.0),
        WeightedItem(Items.CHOCOLATE_SLICE_1901, 1, 1, 10.0),
        WeightedItem(Items.STEEL_AXE_1353, 1, 1, 10.0),
        WeightedItem(Items.MEAT_PIZZA_2293, 1, 1, 10.0),
        WeightedItem(Items.COAL_453, 1, 1, 10.0),
        WeightedItem(Items.BOW_STRING_1777, 1, 1, 10.0),
        WeightedItem(Items.SNAPE_GRASS_231, 1, 1, 1.0),
        WeightedItem(Items.SOFT_CLAY_1761, 1, 1, 1.0),
        WeightedItem(Items.STUDDED_CHAPS_1097, 1, 1, 1.0),
        WeightedItem(Items.STEEL_FULL_HELM_1157, 1, 1, 1.0),
        WeightedItem(Items.OAK_PLANK_8778, 1, 1, 1.0),
        WeightedItem(Items.DEFENCE_POTION3_133, 1, 1, 1.0),
        WeightedItem(Items.MITHRIL_BAR_2359, 1, 1, 1.0),
        WeightedItem(Items.YEW_LONGBOW_855, 1, 1, 1.0),
        WeightedItem(Items.GARDEN_PIE_7178, 1, 1, 1.0),
        WeightedItem(Items.JANGERBERRIES_247, 1, 1, 1.0)
    ).insertEasyClue(2.0)),

    GOURMET_IMPLING(Items.GOURM_IMPLING_JAR_11242, WeightBasedTable.create(
        WeightedItem(Items.TUNA_361, 1, 1, 20.0),
        WeightedItem(Items.BASS_365, 1, 1, 10.0),
        WeightedItem(Items.CURRY_2011, 1, 1, 10.0),
        WeightedItem(Items.MEAT_PIE_2327, 1, 1, 10.0),
        WeightedItem(Items.CHOCOLATE_CAKE_1897, 1, 1, 10.0),
        WeightedItem(Items.FROG_SPAWN_5004, 1, 1, 10.0),
        WeightedItem(Items.SPICE_2007, 1, 1, 10.0),
        WeightedItem(Items.CURRY_LEAF_5970, 1, 1, 10.0),
        WeightedItem(Items.UGTHANKI_KEBAB_1883, 1, 1, 1.0),
        WeightedItem(Items.LOBSTER_380, 4, 4, 1.0),
        WeightedItem(Items.SHARK_386, 3, 3, 1.0),
        WeightedItem(Items.FISH_PIE_7188, 1, 1, 1.0),
        WeightedItem(Items.CHEFS_DELIGHT4_5833, 1, 1, 1.0),
        WeightedItem(Items.RAINBOW_FISH_10137, 5, 5, 1.0),
        WeightedItem(Items.GARDEN_PIE_7179, 6, 6, 1.0),
        WeightedItem(Items.SWORDFISH_374, 3, 3, 1.0),
        WeightedItem(Items.STRAWBERRIES5_5406, 1, 1, 1.0),
        WeightedItem(Items.COOKED_KARAMBWAN_3145, 2, 2, 1.0),
    ).insertEasyClue(4.0)),

    EARTH_IMPLING(Items.EARTH_IMPLING_JAR_11244, WeightBasedTable.create(
        WeightedItem(Items.FIRE_TALISMAN_1442, 1, 1, 10.0),
        WeightedItem(Items.EARTH_TALISMAN_1440, 1, 1, 10.0),
        WeightedItem(Items.EARTH_TIARA_5535, 1, 1, 10.0),
        WeightedItem(Items.EARTH_RUNE_557, 32, 32, 10.0),
        WeightedItem(Items.MITHRIL_ORE_447, 1, 1, 10.0),
        WeightedItem(Items.BUCKET_OF_SAND_1784, 4, 4, 10.0),
        WeightedItem(Items.UNICORN_HORN_237, 1, 1, 10.0),
        WeightedItem(Items.COMPOST_6033, 6, 6, 10.0),
        WeightedItem(Items.GOLD_ORE_444, 1, 1, 10.0),
        WeightedItem(Items.STEEL_BAR_2353, 1, 1, 1.0),
        WeightedItem(Items.MITHRIL_PICKAXE_1273, 1, 1, 1.0),
        WeightedItem(Items.WILDBLOOD_SEED_5311, 2, 2, 1.0),
        WeightedItem(Items.JANGERBERRY_SEED_5104, 2, 2, 1.0),
        WeightedItem(Items.SUPERCOMPOST_6035, 2, 2, 1.0),
        WeightedItem(Items.MITHRIL_ORE_448, 3, 3, 1.0),
        WeightedItem(Items.HARRALANDER_SEED_5294, 2, 2, 1.0),
        WeightedItem(Items.COAL_454, 6, 6, 1.0),
        WeightedItem(Items.EMERALD_1606, 2, 2, 1.0),
        WeightedItem(Items.RUBY_1603, 1, 1, 1.0)
    ).insertMediumClue(1.0)),

    ESSENCE_IMPLING(Items.ESS_IMPLING_JAR_11246, WeightBasedTable.create(
        WeightedItem(Items.PURE_ESSENCE_7937, 20, 20, 10.0),
        WeightedItem(Items.WATER_RUNE_555, 30, 30, 10.0),
        WeightedItem(Items.AIR_RUNE_556, 30, 30, 10.0),
        WeightedItem(Items.FIRE_RUNE_554, 50, 50, 10.0),
        WeightedItem(Items.MIND_RUNE_558, 25, 25, 10.0),
        WeightedItem(Items.BODY_RUNE_559, 28, 28, 10.0),
        WeightedItem(Items.CHAOS_RUNE_562, 4, 4, 10.0),
        WeightedItem(Items.COSMIC_RUNE_564, 4, 4, 10.0),
        WeightedItem(Items.MIND_TALISMAN_1448, 1, 1, 10.0),
        WeightedItem(Items.PURE_ESSENCE_7937, 35, 35, 1.0),
        WeightedItem(Items.LAVA_RUNE_4699, 4, 4, 1.0),
        WeightedItem(Items.MUD_RUNE_4698, 4, 4, 1.0),
        WeightedItem(Items.SMOKE_RUNE_4697, 4, 4, 1.0),
        WeightedItem(Items.STEAM_RUNE_4694, 4, 4, 1.0),
        WeightedItem(Items.DEATH_RUNE_560, 13, 13, 1.0),
        WeightedItem(Items.LAW_RUNE_563, 13, 13, 1.0),
        WeightedItem(Items.BLOOD_RUNE_565, 7, 7, 1.0),
        WeightedItem(Items.SOUL_RUNE_566, 11, 11, 1.0),
        WeightedItem(Items.NATURE_RUNE_561, 13, 13, 1.0)
    ).insertMediumClue(2.0)),

    ECLECTIC_IMPLING(Items.ECLECTIC_IMPLING_JAR_11248, WeightBasedTable.create(
        WeightedItem(Items.MITHRIL_PICKAXE_1273, 1, 1, 10.0),
        WeightedItem(Items.CURRY_LEAF_5970, 1, 1, 10.0),
        WeightedItem(Items.SNAPE_GRASS_231, 1, 1, 10.0),
        WeightedItem(Items.AIR_RUNE_556, 30, 58, 10.0),
        WeightedItem(Items.OAK_PLANK_8779, 4, 4, 10.0),
        WeightedItem(Items.CANDLE_LANTERN_4527, 1, 1, 10.0),
        WeightedItem(Items.GOLD_ORE_444, 1, 1, 10.0),
        WeightedItem(Items.GOLD_BAR_2358, 5, 5, 10.0),
        WeightedItem(Items.UNICORN_HORN_237, 1, 1, 10.0),
        WeightedItem(Items.ADAMANT_KITESHIELD_1199, 1, 1, 1.0),
        WeightedItem(Items.BLUE_DHIDE_CHAPS_2493, 1, 1, 1.0),
        WeightedItem(Items.RED_SPIKY_VAMBS_10083, 1, 1, 1.0),
        WeightedItem(Items.RUNE_DAGGER_1213, 1, 1, 1.0),
        WeightedItem(Items.BATTLESTAFF_1391, 1, 1, 1.0),
        WeightedItem(Items.ADAMANTITE_ORE_450, 10, 10, 1.0),
        WeightedItem(Items.SLAYERS_RESPITE4_5842, 2, 2, 1.0),
        WeightedItem(Items.WILD_PIE_7208, 1, 1, 1.0),
        WeightedItem(Items.WATERMELON_SEED_5321, 3, 3, 1.0),
        WeightedItem(Items.DIAMOND_1601, 1, 1, 1.0)
    ).insertMediumClue(4.0)),

    NATURE_IMPLING(Items.NATURE_IMPLING_JAR_11250, WeightBasedTable.create(
        WeightedItem(Items.LIMPWURT_SEED_5100, 1, 1, 10.0),
        WeightedItem(Items.JANGERBERRY_SEED_5104, 1, 1, 10.0),
        WeightedItem(Items.BELLADONNA_SEED_5281, 1, 1, 10.0),
        WeightedItem(Items.HARRALANDER_SEED_5294, 1, 1, 10.0),
        WeightedItem(Items.CACTUS_SPINE_6016, 1, 1, 10.0),
        WeightedItem(Items.MAGIC_LOGS_1513, 1, 1, 10.0),
        WeightedItem(Items.CLEAN_TARROMIN_254, 4, 4, 10.0),
        WeightedItem(Items.COCONUT_5974, 1, 1, 10.0),
        WeightedItem(Items.IRIT_SEED_5297, 1, 1, 10.0),
        WeightedItem(Items.CURRY_TREE_SEED_5286, 1, 1, 1.0),
        WeightedItem(Items.ORANGE_TREE_SEED_5285, 1, 1, 1.0),
        WeightedItem(Items.CLEAN_SNAPDRAGON_3000, 1, 1, 1.0),
        WeightedItem(Items.KWUARM_SEED_5299, 1, 1, 1.0),
        WeightedItem(Items.AVANTOE_SEED_5298, 5, 5, 1.0),
        WeightedItem(Items.WILLOW_SEED_5313, 1, 1, 1.0),
        WeightedItem(Items.TORSTOL_SEED_5304, 1, 1, 1.0),
        WeightedItem(Items.RANARR_SEED_5295, 1, 1, 1.0),
        WeightedItem(Items.CLEAN_TORSTOL_270, 2, 2, 1.0),
        WeightedItem(Items.DWARF_WEED_SEED_5303, 1, 1, 1.0)
    ).insertHardClue(1.0)),

    MAGPIE_IMPLING(Items.MAGPIE_IMPLING_JAR_11252, WeightBasedTable.create(
        WeightedItem(Items.BLACK_DRAGONHIDE_1748, 6, 6, 10.0),
        WeightedItem(Items.DIAMOND_AMULET_1682, 3, 3, 5.0),
        WeightedItem(Items.AMULET_OF_POWER_1732, 3, 3, 5.0),
        WeightedItem(Items.RING_OF_FORGING_2569, 3, 3, 5.0),
        WeightedItem(Items.SPLITBARK_GAUNTLETS_3391, 1, 1, 5.0),
        WeightedItem(Items.MYSTIC_BOOTS_4097, 1, 1, 5.0),
        WeightedItem(Items.MYSTIC_GLOVES_4095, 1, 1, 5.0),
        WeightedItem(Items.RUNE_WARHAMMER_1347, 1, 1, 5.0),
        WeightedItem(Items.RING_OF_LIFE_2571, 4, 4, 5.0),
        WeightedItem(Items.RUNE_SQ_SHIELD_1185, 1, 1, 5.0),
        WeightedItem(Items.DRAGON_DAGGER_1215, 1, 1, 5.0),
        WeightedItem(Items.NATURE_TIARA_5541, 1, 1, 5.0),
        WeightedItem(Items.RUNITE_BAR_2364, 2, 2, 5.0),
        WeightedItem(Items.DIAMOND_1602, 4, 4, 5.0),
        WeightedItem(Items.PINEAPPLE_SEED_5287, 1, 1, 5.0),
        WeightedItem(Items.LOOP_HALF_OF_A_KEY_987, 1, 1, 5.0),
        WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, 5.0),
        WeightedItem(Items.SNAPDRAGON_SEED_5300, 1, 1, 5.0),
        WeightedItem(Items.SINISTER_KEY_993, 1, 1, 5.0)
    ).insertHardClue(2.0)),

    NINJA_IMPLING(Items.NINJA_IMPLING_JAR_11254, WeightBasedTable.create(
        WeightedItem(Items.SNAKESKIN_BOOTS_6328, 1, 1, 1.65),
        WeightedItem(Items.SPLITBARK_HELM_3385, 1, 1, 1.65),
        WeightedItem(Items.MYSTIC_BOOTS_4098, 1, 1, 1.65),
        WeightedItem(Items.RUNE_CHAINBODY_1113, 1, 1, 1.65),
        WeightedItem(Items.MYSTIC_GLOVES_4095, 1, 1, 1.65),
        WeightedItem(Items.OPAL_MACHETE_6313, 1, 1, 1.65),
        WeightedItem(Items.RUNE_CLAWS_3101, 1, 1, 1.65),
        WeightedItem(Items.RUNE_SCIMITAR_1333, 1, 1, 1.65),
        WeightedItem(Items.DRAGON_DAGGERP_PLUS_5680, 1, 1, 1.65),
        WeightedItem(Items.RUNE_ARROW_892, 70, 70, 1.65),
        WeightedItem(Items.RUNE_DART_811, 70, 70, 1.65),
        WeightedItem(Items.RUNE_KNIFE_868, 40, 40, 1.65),
        WeightedItem(Items.RUNE_THROWNAXE_805, 50, 50, 1.65),
        WeightedItem(Items.ONYX_BOLTS_9342, 2, 2, 1.65),
        WeightedItem(Items.ONYX_BOLT_TIPS_9194, 4, 4, 1.65),
        WeightedItem(Items.BLACK_DRAGONHIDE_1748, 10, 10, 1.65),
        WeightedItem(Items.PRAYER_POTION3_140, 4, 4, 1.65),
        WeightedItem(Items.WEAPON_POISON_PLUS_5938, 4, 4, 1.65),
        WeightedItem(Items.DAGANNOTH_HIDE_6156, 3, 3, 1.65)
    ).insertHardClue(1.0)),

    DRAGON_IMPLING(Items.DRAGON_IMPLING_JAR_11256, WeightBasedTable.create(
        WeightedItem(Items.DRAGON_BOLT_TIPS_9193, 10, 30, 2.0),
        WeightedItem(Items.DRAGON_BOLT_TIPS_9193, 36, 36, 2.0),
        WeightedItem(Items.MYSTIC_ROBE_BOTTOM_4093, 1, 1, 2.0),
        WeightedItem(Items.AMULET_OF_GLORY_1705, 3, 3, 2.0),
        WeightedItem(Items.DRAGONSTONE_AMMY_1684, 2, 2, 2.0),
        WeightedItem(Items.DRAGON_ARROW_11212, 100, 250, 2.0),
        WeightedItem(Items.DRAGON_BOLTS_9341, 10, 40, 2.0),
        WeightedItem(Items.DRAGON_LONGSWORD_1305, 1, 1, 2.0),
        WeightedItem(Items.DRAGON_DAGGERP_PLUS_PLUS_5699, 3, 3, 2.0),
        WeightedItem(Items.DRAGON_DART_11230, 100, 250, 2.0),
        WeightedItem(Items.DRAGONSTONE_1616, 3, 3, 2.0),
        WeightedItem(Items.DRAGON_DART_TIP_11232, 100, 350, 2.0),
        WeightedItem(Items.DRAGON_ARROWTIPS_11237, 100, 350, 2.0),
        WeightedItem(Items.BABYDRAGON_BONES_535, 1, 25, 2.0),
        WeightedItem(Items.DRAGON_BONES_537, 1, 25, 2.0),
        WeightedItem(Items.MAGIC_SEED_5316, 1, 1, 2.0),
        WeightedItem(Items.SNAPDRAGON_SEED_5300, 6, 6, 2.0),
        WeightedItem(Items.SUMMER_PIE_7219, 15, 15, 2.0)
    ).insertHardClue(1.0));

    companion object{

        val jarMap = HashMap<Int,WeightBasedTable>()

        init {
            for(impling in values()){
                jarMap[impling.jarId] = impling.table
            }
        }

        fun forId(id: Int): WeightBasedTable?{
            return jarMap[id]
        }
    }
}
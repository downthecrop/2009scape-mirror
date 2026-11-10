package content.global.handlers.item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.Sounds
import java.util.*

/**
 * Handles items with "empty" options
 */
class EmptyOptionListener : InteractionListener {

    override fun defineListeners() {
        on(EmptyItem.emptyItemList.toIntArray(), IntType.ITEM, "empty", "empty bowl", "empty dish", "empty tray", "empty tin") { player, node ->
            if (EmptyItem.emptyItemMap[node.id] != null) {
                replaceSlot(player, node.asItem().slot, Item(EmptyItem.getEmpty(node.id)!!), node.asItem())
                if (EmptyItem.getEmptyAudio(node.id) != -1) playAudio(player, EmptyItem.getEmptyAudio(node.id)!!)
                EmptyItem.getEmptyMessage(node.id)?.let { sendMessage(player, it) }
            }
            return@on true
        }
    }

    enum class EmptyItem(var fullId: Int, var emptyId: Int, var emptyMessage: String, var audioId: Int = -1) {
        POT_OF_FLOUR(Items.POT_OF_FLOUR_1933, Items.EMPTY_POT_1931, "You empty the contents of the pot onto the floor."),
        POT_OF_CORNFLOUR(Items.POT_OF_CORNFLOUR_7468, Items.EMPTY_POT_1931, "You empty the contents of the pot onto the floor."),
        CORNFLOWER_MIX(Items.CORNFLOUR_MIXTURE_7470, Items.EMPTY_POT_1931, "You empty the contents of the pot onto the floor."),
        BONE_MEAL(Items.BONEMEAL_4255, Items.EMPTY_POT_1931, "You empty the pot of crushed bones."),
        BURNT_CRUNCHIES(Items.BURNT_CRUNCHIES_2199, Items.CRUNCHY_TRAY_2165, "You empty the tray of burnt crunchies."), // Check this text for authenticity.
        BURNT_BATTA(Items.BURNT_BATTA_2247, Items.BATTA_TIN_2164, "You empty the tin of burnt batta."), // Check this text for authenticity.
        BUCKET_OF_SAND(Items.BUCKET_OF_SAND_1783, Items.BUCKET_1925, "You empty the contents of the bucket onto the floor.", Sounds.SAND_BUCKET_2584),
        BUCKET_OF_MILK(Items.BUCKET_OF_MILK_1927, Items.BUCKET_1925, "You empty the contents of the bucket onto the floor.", Sounds.VIAL_EMPTY_2610),
        MILKY_MIX(Items.MILKY_MIXTURE_7471, Items.BUCKET_1925, "You empty the contents of the bucket onto the floor.", Sounds.VIAL_EMPTY_2610),
        BUCKET_OF_WATER(Items.BUCKET_OF_WATER_1929, Items.BUCKET_1925, "You empty the contents of the bucket on the floor.", Sounds.VIAL_EMPTY_2610),
        BUCKET_OF_WATER2(Items.BUCKET_OF_WATER_9659, Items.BUCKET_9660, "You empty the contents of the bucket on the floor.", Sounds.VIAL_EMPTY_2610),
        BUCKET_OF_COMPOST(Items.COMPOST_6032, Items.BUCKET_1925, "You empty the bucket of compost."),
        BUCKET_OF_SUPERCOMPOST(Items.SUPERCOMPOST_6034, Items.BUCKET_1925, "You empty the bucket of supercompost."),
        BUCKET_OF_SLIME(Items.BUCKET_OF_SLIME_4286, Items.BUCKET_1925, "You empty the contents of the bucket on the floor.", Sounds.VIAL_EMPTY_2610),
        APPLE_MUSH(Items.APPLE_MUSH_5992,Items.BUCKET_1925, "You empty the bucket of apple mush."),
        VIAL_OF_WATER(Items.VIAL_OF_WATER_227, Items.VIAL_229, "You empty the vial.", Sounds.VIAL_EMPTY_2610),
        BOWL_OF_WATER(Items.BOWL_OF_WATER_1921, Items.BOWL_1923, "You empty the contents of the bowl onto the floor.", Sounds.VIAL_EMPTY_2610),
        BOWL_OF_HOT_WATER(Items.BOWL_OF_HOT_WATER_4456, Items.BOWL_1923, "You empty the contents of the bowl onto the floor.", Sounds.VIAL_EMPTY_2610),
        JUG_OF_WATER(Items.JUG_OF_WATER_1937, Items.JUG_1935, "You empty the contents of the jug onto the floor.", Sounds.VIAL_EMPTY_2610),
        JUG_OF_VINEGAR(Items.JUG_OF_VINEGAR_7810, Items.JUG_1935, "You empty the contents of the jug onto the floor.", Sounds.VIAL_EMPTY_2610),
        BURNT_PIE(Items.BURNT_PIE_2329, Items.PIE_DISH_2313, "You empty the pie dish."),
        BURNT_STEW(Items.BURNT_STEW_2005, Items.BOWL_1923, "You empty the contents of the bowl onto the floor.", Sounds.VIAL_EMPTY_2610),
        NETTLE_TEA(Items.NETTLE_TEA_4239, Items.BOWL_1923, "You empty the contents of the bowl onto the floor.", Sounds.VIAL_EMPTY_2610),
        NETTLE_TEA_CUP(Items.CUP_OF_TEA_4242, Items.EMPTY_CUP_1980, "You empty the cup of tea.", Sounds.VIAL_EMPTY_2610),
        NETTLE_TEA_CUP_MILKY(Items.CUP_OF_TEA_4243, Items.EMPTY_CUP_1980, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        NETTLE_TEA_PORCELAIN(Items.CUP_OF_TEA_4245, Items.PORCELAIN_CUP_4244, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        NETTLE_TEA_PORCELAIN_MILKY(Items.CUP_OF_TEA_4246, Items.PORCELAIN_CUP_4244, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        NETTLE_WATER(Items.NETTLE_WATER_4237, Items.BOWL_1923, "You empty the contents of the bowl onto the floor.", Sounds.VIAL_EMPTY_2610),
        NETTLE_TEA_MILKY(Items.NETTLE_TEA_4240, Items.BOWL_1923, "You empty the contents of the bowl onto the floor.", Sounds.VIAL_EMPTY_2610),
        TEA_CUP_WATER(Items.CUP_OF_WATER_4458, Items.EMPTY_CUP_1980, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        TEA_CUP_HOT_WATER(Items.CUP_OF_HOT_WATER_4460, Items.EMPTY_CUP_1980, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        BUILDERS_TEA1(Items.CUP_OF_TEA_7730, Items.EMPTY_CUP_7728, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        BUILDERS_TEA1_MILKY(Items.CUP_OF_TEA_7731, Items.EMPTY_CUP_7728, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        BUILDERS_TEA2(Items.CUP_OF_TEA_7733, Items.PORCELAIN_CUP_7732, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        BUILDERS_TEA2_MILKY(Items.CUP_OF_TEA_7734, Items.PORCELAIN_CUP_7732, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        BUILDERS_TEA3(Items.CUP_OF_TEA_7736, Items.PORCELAIN_CUP_7735, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        BUILDERS_TEA3_MILKY(Items.CUP_OF_TEA_7737, Items.PORCELAIN_CUP_7735, "You empty the contents of the cup onto the floor.", Sounds.VIAL_EMPTY_2610),
        CLAY_POT_OF_TEA4(Items.POT_OF_TEA_4_7692, Items.TEAPOT_7702, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        CLAY_POT_OF_TEA3(Items.POT_OF_TEA_3_7694, Items.TEAPOT_7702, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        CLAY_POT_OF_TEA2(Items.POT_OF_TEA_2_7696, Items.TEAPOT_7702, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        CLAY_POT_OF_TEA1(Items.POT_OF_TEA_1_7698, Items.TEAPOT_7702, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        PORCELAIN_POT_OF_TEA4(Items.POT_OF_TEA_4_7704, Items.TEAPOT_7714, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        PORCELAIN_POT_OF_TEA3(Items.POT_OF_TEA_3_7706, Items.TEAPOT_7714, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        PORCELAIN_POT_OF_TEA2(Items.POT_OF_TEA_2_7708, Items.TEAPOT_7714, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        PORCELAIN_POT_OF_TEA1(Items.POT_OF_TEA_1_7710, Items.TEAPOT_7714, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        GOLD_PORCELAIN_POT_OF_TEA4(Items.POT_OF_TEA_4_7716, Items.TEAPOT_7726, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        GOLD_PORCELAIN_POT_OF_TEA3(Items.POT_OF_TEA_3_7718, Items.TEAPOT_7726, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        GOLD_PORCELAIN_POT_OF_TEA2(Items.POT_OF_TEA_2_7720, Items.TEAPOT_7726, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        GOLD_PORCELAIN_POT_OF_TEA1(Items.POT_OF_TEA_1_7722, Items.TEAPOT_7726, "You empty the contents of the pot onto the floor", Sounds.VIAL_EMPTY_2610),
        BURNT_CURRY(Items.BURNT_CURRY_2013, Items.BOWL_1923, "You empty the contents of the bowl onto the floor.", Sounds.VIAL_EMPTY_2610),
        BURNT_GNOMEBOWL(Items.BURNT_GNOMEBOWL_2175, Items.GNOMEBOWL_MOULD_2166, "You empty the contents of the gnomebowl onto the floor."),
        BURNT_EGG(Items.BURNT_EGG_7090, Items.BOWL_1923, "You empty the contents of the bowl onto the floor."),
        BURNT_ONION(Items.BURNT_ONION_7092, Items.BOWL_1923, "You empty the contents of the bowl onto the floor."),
        BURNT_MUSHROOM(Items.BURNT_MUSHROOM_7094, Items.BOWL_1923, "You empty the contents of the bowl onto the floor."),
        COCONUT_MILK(Items.COCONUT_MILK_5935, Items.VIAL_229, "You empty the vial.", Sounds.VIAL_EMPTY_2610),
        IMP_REPEL(Items.IMP_REPELLENT_11262, Items.VIAL_229, "You empty the vial.", Sounds.VIAL_EMPTY_2610),
        ;

        companion object {
            var emptyItemMap = HashMap<Int, Int?>()
            var emptyMessageMap = HashMap<Int, String>()
            var emptyAudioMap = HashMap<Int, Int>()
            var emptyItemList = ArrayList<Int>()

            // Potions or other liquids that are stored in vials.
            val emptyToVial = intArrayOf(
                Items.AGILITY_MIX1_11463, Items.AGILITY_MIX2_11461,
                Items.AGILITY_POTION1_3038, Items.AGILITY_POTION2_3036, Items.AGILITY_POTION3_3034, Items.AGILITY_POTION4_3032,
                Items.ANCHOVY_OIL_11264,
                Items.ANTIDOTE_PLUS_MIX1_11503, Items.ANTIDOTE_PLUS_MIX2_11501,
                Items.ANTIFIRE_MIX1_11507, Items.ANTIFIRE_MIX2_11505,
                Items.ANTIFIRE_POTION1_2458, Items.ANTIFIRE_POTION2_2456, Items.ANTIFIRE_POTION3_2454, Items.ANTIFIRE_POTION4_2452,
                Items.ANTIPOISON1_179, Items.ANTIPOISON2_177, Items.ANTIPOISON3_175, Items.ANTIPOISON4_2446,
                Items.ANTIPOISON_MIX1_11435, Items.ANTIPOISON_MIX2_11433,
                Items.ANTIPOISON_PLUS1_5949, Items.ANTIPOISON_PLUS2_5947, Items.ANTIPOISON_PLUS3_5945, Items.ANTIPOISON_PLUS4_5943,
                Items.ANTIPOISON_PLUSUNF_5942,
                Items.ANTIPOISON_PLUS_PLUS1_5958, Items.ANTIPOISON_PLUS_PLUS2_5956, Items.ANTIPOISON_PLUS_PLUS3_5954, Items.ANTIPOISON_PLUS_PLUS4_5952,
                Items.ANTIPOISON_PLUS_PLUSUNF_5951,
                Items.ANTI_P_SUPERMIX1_11475, Items.ANTI_P_SUPERMIX2_11473,
                Items.ATTACK_MIX1_11431, Items.ATTACK_MIX2_11429,
                Items.ATTACK_POTION1_125, Items.ATTACK_POTION2_123, Items.ATTACK_POTION3_121, Items.ATTACK_POTION4_2428,
                Items.AVANTOE_POTIONUNF_103,
                Items.BLACK_MUSHROOM_INK_4622,
                Items.CADANTINE_POTIONUNF_107,
                Items.CAT_ANTIPOISON_6766,
                Items.COMBAT_MIX1_11447, Items.COMBAT_MIX2_11445,
                Items.COMBAT_POTION1_9745, Items.COMBAT_POTION2_9743, Items.COMBAT_POTION3_9741, Items.COMBAT_POTION4_9739,
                Items.COMPOST_POTION1_6476, Items.COMPOST_POTION2_6474, Items.COMPOST_POTION3_6472, Items.COMPOST_POTION4_6470,
                Items.DEFENCE_MIX1_11459, Items.DEFENCE_MIX2_11457,
                Items.DEFENCE_POTION1_137, Items.DEFENCE_POTION2_135, Items.DEFENCE_POTION3_133, Items.DEFENCE_POTION4_2432,
                Items.DWARF_WEED_POTIONUNF_109,
                Items.ENERGY_MIX1_11455, Items.ENERGY_MIX2_11453,
                Items.ENERGY_POTION1_3014, Items.ENERGY_POTION2_3012, Items.ENERGY_POTION3_3010, Items.ENERGY_POTION4_3008,
                Items.FIREWORK_3006,
                Items.FISHING_MIX1_11479, Items.FISHING_MIX2_11477,
                Items.FISHING_POTION1_155, Items.FISHING_POTION2_153, Items.FISHING_POTION3_151, Items.FISHING_POTION4_2438,
                Items.GOBLIN_POTION_1_11812, Items.GOBLIN_POTION_2_11811, Items.GOBLIN_POTION_3_11810, Items.GOBLIN_POTION_4_11809,
                Items.GUAM_POTIONUNF_91,
                Items.GUTHIX_BALANCEUNF_7652, Items.GUTHIX_BALANCEUNF_7654, Items.GUTHIX_BALANCEUNF_7656, Items.GUTHIX_BALANCEUNF_7658,
                Items.GUTHIX_BALANCE4_7660, Items.GUTHIX_BALANCE3_7662, Items.GUTHIX_BALANCE2_7664, Items.GUTHIX_BALANCE1_7666,
                Items.HARRALANDER_POTIONUNF_97,
                Items.HUNTER_POTION1_10004, Items.HUNTER_POTION2_10002, Items.HUNTER_POTION3_10000, Items.HUNTER_POTION4_9998,
                Items.HUNTING_MIX1_11519, Items.HUNTING_MIX2_11517,
                Items.IRIT_POTIONUNF_101,
                Items.KWUARM_POTIONUNF_105,
                Items.LANTADYME_POTIONUNF_2483,
                Items.MAGIC_ESS_MIX1_11491, Items.MAGIC_ESS_MIX2_11489,
                Items.MAGIC_MIX1_11515, Items.MAGIC_MIX2_11513,
                Items.MAGIC_POTION1_3046, Items.MAGIC_POTION2_3044, Items.MAGIC_POTION3_3042, Items.MAGIC_POTION4_3040,
                Items.MAGIC_ESSENCEUNF_9019,
                Items.MAGIC_ESSENCE4_9021, Items.MAGIC_ESSENCE3_9022, Items.MAGIC_ESSENCE2_9023, Items.MAGIC_ESSENCE1_9024,
                Items.MARRENTILL_POTIONUNF_93,
                Items.MIXTURE___STEP_11_10915, Items.MIXTURE___STEP_12_10913, Items.MIXTURE___STEP_13_10911, Items.MIXTURE___STEP_14_10909,
                Items.MIXTURE___STEP_21_10923, Items.MIXTURE___STEP_22_10921, Items.MIXTURE___STEP_23_10919, Items.MIXTURE___STEP_24_10917,
                Items.OLIVE_OIL4_3422, Items.OLIVE_OIL3_3424, Items.OLIVE_OIL2_3426, Items.OLIVE_OIL1_3428,
                Items.PLANT_CURE_6036,
                Items.PRAYER_MIX1_11467, Items.PRAYER_MIX2_11465,
                Items.PRAYER_POTION1_143, Items.PRAYER_POTION2_141, Items.PRAYER_POTION3_139, Items.PRAYER_POTION4_2434,
                Items.RANARR_POTIONUNF_99,
                Items.RANGING_MIX1_11511, Items.RANGING_MIX2_11509,
                Items.RANGING_POTION1_173, Items.RANGING_POTION2_171, Items.RANGING_POTION3_169, Items.RANGING_POTION4_2444,
                Items.RELICYMS_BALM1_4848, Items.RELICYMS_BALM2_4846, Items.RELICYMS_BALM3_4844, Items.RELICYMS_BALM4_4842,
                Items.RELICYMS_MIX1_11439, Items.RELICYMS_MIX2_11437,
                Items.RESTORE_MIX1_11451, Items.RESTORE_MIX2_11449,
                Items.RESTORE_POTION1_131, Items.RESTORE_POTION2_129, Items.RESTORE_POTION3_127, Items.RESTORE_POTION4_2430,
                Items.ROGUES_PURSE_POTIONUNF_4840,
                Items.SACRED_OIL4_3430, Items.SACRED_OIL3_3432, Items.SACRED_OIL2_3434, Items.SACRED_OIL1_3436,
                Items.SANFEW_SERUM1_10931, Items.SANFEW_SERUM2_10929, Items.SANFEW_SERUM3_10927, Items.SANFEW_SERUM4_10925,
                Items.SARADOMIN_BREW1_6691, Items.SARADOMIN_BREW2_6689, Items.SARADOMIN_BREW3_6687, Items.SARADOMIN_BREW4_6685,
                Items.SERUM_207_1_3414, Items.SERUM_207_2_3412, Items.SERUM_207_3_3410, Items.SERUM_207_4_3408,
                Items.SERUM_208_1_3419, Items.SERUM_208_2_3418, Items.SERUM_208_3_3417, Items.SERUM_208_4_3416,
                Items.SHRINK_ME_QUICK_11204,
                Items.MAGIC_UNGUENT_14061,
                Items.BROOM_OINTMENT_14062,
                Items.SNAPDRAGON_POTIONUNF_3004,
                Items.SPIRIT_WEED_POTIONUNF_12181,
                Items.STRENGTH_MIX1_11441, Items.STRENGTH_MIX2_11443,
                Items.STRENGTH_POTION1_119, Items.STRENGTH_POTION2_117, Items.STRENGTH_POTION3_115, Items.STRENGTH_POTION4_113,
                Items.SUMMONING_POTION1_12146, Items.SUMMONING_POTION2_12144, Items.SUMMONING_POTION3_12142, Items.SUMMONING_POTION4_12140,
                Items.SUPERATTACK_MIX1_11471, Items.SUPERATTACK_MIX2_11469,
                Items.SUPER_ANTIPOISON1_185, Items.SUPER_ANTIPOISON2_183, Items.SUPER_ANTIPOISON3_181, Items.SUPER_ANTIPOISON4_2448,
                Items.SUPER_ATTACK1_149, Items.SUPER_ATTACK2_147, Items.SUPER_ATTACK3_145, Items.SUPER_ATTACK4_2436,
                Items.SUPER_DEFENCE1_167, Items.SUPER_DEFENCE2_165, Items.SUPER_DEFENCE3_163, Items.SUPER_DEFENCE4_2442,
                Items.SUPER_ENERGY1_3022, Items.SUPER_ENERGY2_3020, Items.SUPER_ENERGY3_3018, Items.SUPER_ENERGY4_3016,
                Items.SUPER_RESTORE1_3030, Items.SUPER_RESTORE2_3028, Items.SUPER_RESTORE3_3026, Items.SUPER_RESTORE4_3024,
                Items.SUPER_STRENGTH1_161, Items.SUPER_STRENGTH2_159, Items.SUPER_STRENGTH3_157, Items.SUPER_STRENGTH4_2440,
                Items.SUP_DEF_MIX1_11499, Items.SUP_DEF_MIX2_11497,
                Items.SUP_ENERGY_MIX1_11483, Items.SUP_ENERGY_MIX2_11481,
                Items.SUP_RESTORE_MIX1_11495, Items.SUP_RESTORE_MIX2_11493,
                Items.SUP_STR_MIX1_11487, Items.SUP_STR_MIX2_11485,
                Items.TARROMIN_POTIONUNF_95,
                Items.TOADFLAX_POTIONUNF_3002,
                Items.TORSTOL_POTIONUNF_111,
                Items.UNIDENTIFIED_LIQUID_702,
                Items.WEAPON_POISON_187,
                Items.WEAPON_POISON_PLUSUNF_5936,
                Items.WEAPON_POISON_PLUS_5937,
                Items.WEAPON_POISON_PLUS_PLUSUNF_5939,
                Items.WEAPON_POISON_PLUS_PLUS_5940,
                Items.ZAMORAK_BREW1_193, Items.ZAMORAK_BREW2_191, Items.ZAMORAK_BREW3_189, Items.ZAMORAK_BREW4_2450,
                Items.ZAMORAK_MIX1_11523, Items.ZAMORAK_MIX2_11521
            )

            val emptyToGoldenBowl = intArrayOf(
                Items.GOLDEN_BOWL_723,
                Items.GOLDEN_BOWL_724,
                Items.GOLDEN_BOWL_725,
                Items.GOLDEN_BOWL_726
            )

            val emptyToPot = intArrayOf(
                Items.BONEMEAL_4256,
                Items.BONEMEAL_4257,
                Items.BONEMEAL_4258,
                Items.BONEMEAL_4259,
                Items.BONEMEAL_4260,
                Items.BONEMEAL_4261,
                Items.BONEMEAL_4262,
                Items.BONEMEAL_4263,
                Items.BONEMEAL_4264,
                Items.BONEMEAL_4265,
                Items.BONEMEAL_4266,
                Items.BONEMEAL_4267,
                Items.BONEMEAL_4268,
                Items.BONEMEAL_4269,
                Items.BONEMEAL_4270,
                Items.BONEMEAL_4271,
                Items.BONEMEAL_4852,
                Items.BONEMEAL_4853,
                Items.BONEMEAL_4854,
                Items.BONEMEAL_4855,
                Items.BONEMEAL_5615,
                Items.BONEMEAL_6728,
                Items.BONEMEAL_6810,
                Items.BRULEE_7473,
                Items.BRULEE_7474,
                Items.BRULEE_7475,
                Items.BRULEE_SUPREME_7476,
                Items.POT_OF_VINEGAR_7811,
                Items.BONE_IN_VINEGAR_7813,
                Items.BONE_IN_VINEGAR_7816,
                Items.BONE_IN_VINEGAR_7819,
                Items.BONE_IN_VINEGAR_7822,
                Items.BONE_IN_VINEGAR_7825,
                Items.BONE_IN_VINEGAR_7828,
                Items.BONE_IN_VINEGAR_7831,
                Items.BONE_IN_VINEGAR_7834,
                Items.BONE_IN_VINEGAR_7837,
                Items.BONE_IN_VINEGAR_7840,
                Items.BONE_IN_VINEGAR_7843,
                Items.BONE_IN_VINEGAR_7846,
                Items.BONE_IN_VINEGAR_7849,
                Items.BONE_IN_VINEGAR_7852,
                Items.BONE_IN_VINEGAR_7855,
                Items.BONE_IN_VINEGAR_7858,
                Items.BONE_IN_VINEGAR_7861,
                Items.BONE_IN_VINEGAR_7864,
                Items.BONE_IN_VINEGAR_7867,
                Items.BONE_IN_VINEGAR_7870,
                Items.BONE_IN_VINEGAR_7873,
                Items.BONE_IN_VINEGAR_7876,
                Items.BONE_IN_VINEGAR_7879,
                Items.BONE_IN_VINEGAR_7882,
                Items.BONE_IN_VINEGAR_7885,
                Items.BONE_IN_VINEGAR_7888,
                Items.BONE_IN_VINEGAR_7891,
                Items.BONE_IN_VINEGAR_7894,
                Items.BONE_IN_VINEGAR_7897,
                Items.BONE_IN_VINEGAR_7900,
                Items.BONE_IN_VINEGAR_7903,
                Items.BONE_IN_VINEGAR_7906,
                Items.BONE_IN_VINEGAR_7909,
                Items.BONE_IN_VINEGAR_7912,
                Items.BONE_IN_VINEGAR_7915
            )

            val emptyToBucket = intArrayOf(
                Items.UGTHANKI_DUNG_4601,
                Items.UGTHANKI_DUNG_4602,
                Items.BUCKET_OF_SAP_4687,
                Items.FULL_BUCKET_4693,
                Items.BUCKET_OF_WATER_6712,
                Items.BUCKET_OF_RUBBLE_7622,
                Items.BUCKET_OF_RUBBLE_7624,
                Items.BUCKET_OF_RUBBLE_7626,
                Items.MAGIC_GLUE_9592,
                Items.WEIRD_GLOOP_9593,
                Items.BUCKET_OF_WATER_12650,
                Items.BUCKET_OF_WATER_12651,
                Items.BUCKET_OF_COAL_12652,
                Items.BUCKET_OF_COAL_12653,
            )

            val emptyToCocktailGlass = intArrayOf(
                Items.WIZARD_BLIZZARD_9508,
                Items.SHORT_GREEN_GUY_9510,
                Items.PINEAPPLE_PUNCH_9512,
                Items.FRUIT_BLAST_9514,
                Items.DRUNK_DRAGON_9516,
                Items.CHOC_SATURDAY_9518,
                Items.BLURBERRY_SPECIAL_9520,
            )

            init {
                // Add all the above items in the enum above.
                for (item in values()) {
                    emptyItemMap.putIfAbsent(item.fullId, item.emptyId)
                    emptyMessageMap.putIfAbsent(item.fullId, item.emptyMessage)
                    emptyAudioMap.putIfAbsent(item.fullId, item.audioId)
                    emptyItemList.add(item.fullId)
                }

                // Add all the empty-able potion items from the array above.
                for (potion in emptyToVial) {
                    emptyItemMap.putIfAbsent(potion, Items.VIAL_229)
                    emptyMessageMap.putIfAbsent(potion, "You empty the vial.")
                    emptyAudioMap.putIfAbsent(potion, Sounds.VIAL_EMPTY_2610)
                    emptyItemList.add(potion)
                }

                // Add all the empty-able golden bowls.
                for (bowl in emptyToGoldenBowl) {
                    emptyItemMap.putIfAbsent(bowl, Items.BLESSED_GOLD_BOWL_722)
                    emptyMessageMap.putIfAbsent(bowl, "You empty the golden bowl.")
                    emptyAudioMap.putIfAbsent(bowl, Sounds.VIAL_EMPTY_2610)
                    emptyItemList.add(bowl)
                }

                // Add all the empty-able items in pots from the array above.
                for (pot in emptyToPot) {
                    emptyItemMap.putIfAbsent(pot, Items.EMPTY_POT_1931)
                    emptyMessageMap.putIfAbsent(pot, "You empty the contents of the pot onto the floor.")
                    emptyAudioMap.putIfAbsent(pot, -1) // needs source for audio
                    emptyItemList.add(pot)
                }

                // Add all the empty-able items in buckets from the array above.
                for (bucket in emptyToBucket) {
                    emptyItemMap.putIfAbsent(bucket, Items.BUCKET_1925)
                    emptyMessageMap.putIfAbsent(bucket, "You empty the contents of the bucket onto the floor.")
                    emptyAudioMap.putIfAbsent(bucket, -1) // needs source for audio
                    emptyItemList.add(bucket)
                }

                // Add all the empty-able cocktails from the array above.
                for (drinks in emptyToCocktailGlass) {
                    emptyItemMap.putIfAbsent(drinks, Items.COCKTAIL_GLASS_2026)
                    emptyMessageMap.putIfAbsent(drinks, "You empty the contents of the cocktail glass onto the floor.")
                    emptyAudioMap.putIfAbsent(drinks, Sounds.VIAL_EMPTY_2610)
                    emptyItemList.add(drinks)
                }
            }

            fun getEmpty(id: Int): Int? {
                return emptyItemMap[id]
            }

            fun getEmptyMessage(id: Int): String? {
                return emptyMessageMap[id]
            }

            fun getEmptyAudio(id: Int): Int? {
                return emptyAudioMap[id]
            }
        }
    }
}

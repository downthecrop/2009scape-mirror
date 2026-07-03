package core.game.bots

import core.game.node.item.Item
import core.game.world.map.Location
import org.rs09.consts.Items

class SkillingBotAssembler {
    fun produce(type: Wealth, loc: Location): AIPlayer {
        return assembleBot(AIPlayer(loc),type)
    }

    fun assembleBot(bot: AIPlayer, type: Wealth): AIPlayer {
        return when(type){
            Wealth.POOR -> equipSet(bot,POORSETS.random())
            Wealth.AVERAGE -> equipSet(bot,AVGSETS.random())
            Wealth.RICH -> equipSet(bot,RICHSETS.random())
        }
    }

    fun equipSet(bot: AIPlayer, set: List<Int>): AIPlayer {
        for(i in set){
            val item = Item(i)
            val configs = item.definition.handlers
            val slot = configs["equipment_slot"] ?: continue
			if(bot.inventory.get(slot as Int) == null) {
				bot.equipment.add(item, slot as Int,false,false)
			}
            val reqs = configs["requirements"]
            if(reqs != null)
                for(req in configs["requirements"] as HashMap<Int,Int>)
                    bot.skills.setStaticLevel(req.key, req.value)
        }
        bot.skills.updateCombatLevel()
        return bot
    }

    enum class Wealth {
        POOR,
        AVERAGE,
        RICH
    }

    val POORSETS = arrayOf(
            listOf(Items.BLACK_ROBE_581, Items.BLACK_SKIRT_1015, Items.STAFF_OF_AIR_1381),
            listOf(Items.BLACK_ROBE_581, Items.BLACK_SKIRT_1015),
            listOf(Items.BLACK_ROBE_581, Items.BLACK_SKIRT_1015, Items.WIZARD_HAT_1017),
            listOf(),   // No equipment
            listOf(Items.WIZARD_HAT_579),
            listOf(Items.WIZARD_HAT_579, Items.STAFF_OF_AIR_1381),
            listOf(Items.WIZARD_HAT_579, Items.STAFF_1379),
            listOf(Items.WIZARD_HAT_579, Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011),
            listOf(Items.WIZARD_HAT_579, Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011, Items.TIARA_5525),
            listOf(Items.WIZARD_HAT_579, Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011, Items.TEAM_1_CAPE_4315),
            listOf(Items.WIZARD_HAT_579, Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011, Items.STAFF_OF_AIR_1381),
            listOf(Items.WIZARD_HAT_579, Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011, Items.STAFF_OF_WATER_1383),
            listOf(Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011),
            listOf(Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011, Items.TIARA_5525),
            listOf(Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011, Items.TEAM_1_CAPE_4315),
            listOf(Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011, Items.STAFF_OF_AIR_1381),
            listOf(Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011, Items.STAFF_OF_WATER_1383),
            listOf(),   // No equipment
            listOf(Items.STEEL_PLATELEGS_1069, Items.STEEL_PLATEBODY_1119, Items.STEEL_KITESHIELD_1193, Items.STEEL_LONGSWORD_1295, Items.TEAM_29_CAPE_4371),
            listOf(Items.STEEL_PLATESKIRT_1083, Items.STEEL_PLATEBODY_1119, Items.STEEL_KITESHIELD_1193, Items.STEEL_LONGSWORD_1295, Items.TEAM_30_CAPE_4373),
            listOf(Items.STEEL_PLATESKIRT_1083, Items.STEEL_PLATEBODY_1119, Items.STEEL_FULL_HELM_1157, Items.TEAM_30_CAPE_4373),
            listOf(Items.STEEL_PLATELEGS_1069, Items.STEEL_CHAINBODY_1105, Items.STEEL_FULL_HELM_1157, Items.TEAM_31_CAPE_4375),
            listOf(Items.STEEL_PLATELEGS_1069, Items.STEEL_PLATEBODY_1119, Items.TEAM_29_CAPE_4371),
            listOf(Items.STEEL_PLATESKIRT_1083, Items.STEEL_PLATEBODY_1119, Items.TEAM_30_CAPE_4373),
            listOf(Items.STEEL_PLATELEGS_1069, Items.STEEL_CHAINBODY_1105, Items.TEAM_31_CAPE_4375),
            listOf(Items.STEEL_PLATELEGS_1069, Items.STEEL_CHAINBODY_1105, Items.STEEL_2H_SWORD_1311, Items.TEAM_34_CAPE_4381),
            listOf(Items.STEEL_PLATELEGS_1069, Items.STEEL_CHAINBODY_1105, Items.STEEL_SCIMITAR_1325, Items.TEAM_35_CAPE_4383),
            listOf(Items.STEEL_PLATELEGS_1069, Items.STEEL_CHAINBODY_1105, Items.STEEL_BATTLEAXE_1365, Items.TEAM_35_CAPE_4383),
            listOf(Items.STEEL_PLATELEGS_1069, Items.STEEL_CHAINBODY_1105, Items.STEEL_BATTLEAXE_1365, Items.STEEL_SQ_SHIELD_1177, Items.TEAM_35_CAPE_4383),
            listOf(),   // No equipment
            listOf(Items.IRON_PLATELEGS_1067, Items.IRON_PLATEBODY_1115, Items.IRON_FULL_HELM_1153, Items.IRON_KITESHIELD_1191, Items.IRON_LONGSWORD_1293, Items.TEAM_36_CAPE_4385),
            listOf(Items.IRON_PLATELEGS_1067, Items.IRON_PLATEBODY_1115, Items.IRON_FULL_HELM_1153, Items.IRON_2H_SWORD_1309, Items.TEAM_37_CAPE_4387),
            listOf(Items.IRON_PLATESKIRT_1081, Items.IRON_PLATEBODY_1115, Items.IRON_FULL_HELM_1153, Items.IRON_SCIMITAR_1323, Items.TEAM_32_CAPE_4377),
            listOf(Items.IRON_PLATESKIRT_1081, Items.IRON_PLATEBODY_1115, Items.IRON_FULL_HELM_1153, Items.IRON_BATTLEAXE_1363, Items.TEAM_32_CAPE_4377),
            listOf(Items.IRON_PLATELEGS_1067, Items.IRON_CHAINBODY_1101, Items.IRON_KITESHIELD_1191, Items.IRON_LONGSWORD_1293, Items.TEAM_33_CAPE_4379),
            listOf(Items.IRON_PLATESKIRT_1081, Items.IRON_PLATEBODY_1115, Items.IRON_FULL_HELM_1153, Items.TEAM_39_CAPE_4391),
            listOf(Items.IRON_PLATELEGS_1067, Items.IRON_PLATEBODY_1115, Items.IRON_FULL_HELM_1153, Items.TEAM_37_CAPE_4387),
            listOf(Items.IRON_PLATELEGS_1067, Items.IRON_PLATEBODY_1115, Items.IRON_SCIMITAR_1323, Items.TEAM_32_CAPE_4377),
            listOf(Items.IRON_PLATESKIRT_1081, Items.IRON_PLATEBODY_1115, Items.TEAM_32_CAPE_4377),
            listOf(Items.IRON_PLATELEGS_1067, Items.IRON_CHAINBODY_1101, Items.TEAM_33_CAPE_4379),
            listOf(Items.IRON_PLATESKIRT_1081, Items.IRON_PLATEBODY_1115, Items.IRON_FULL_HELM_1153, Items.TEAM_32_CAPE_4377),
            listOf(Items.IRON_PLATESKIRT_1081, Items.IRON_CHAINBODY_1101, Items.IRON_FULL_HELM_1153, Items.TEAM_32_CAPE_4377),
            listOf(Items.IRON_PLATELEGS_1067, Items.IRON_CHAINBODY_1101, Items.IRON_FULL_HELM_1153, Items.TEAM_33_CAPE_4379),
            listOf(),   // No equipment
            listOf(Items.BRONZE_PLATELEGS_1075,Items.BRONZE_PLATEBODY_1117,Items.BRONZE_FULL_HELM_1155,Items.BRONZE_KITESHIELD_1189,Items.BRONZE_LONGSWORD_1291,Items.TEAM_27_CAPE_4367),
            listOf(Items.BRONZE_PLATESKIRT_1087,Items.BRONZE_PLATEBODY_1117,Items.BRONZE_FULL_HELM_1155,Items.BRONZE_KITESHIELD_1189,Items.BRONZE_LONGSWORD_1291,Items.TEAM_27_CAPE_4367),
            listOf(Items.BRONZE_PLATELEGS_1075,Items.BRONZE_PLATEBODY_1117,Items.BRONZE_FULL_HELM_1155,Items.BRONZE_2H_SWORD_1307, Items.TEAM_28_CAPE_4369),
            listOf(Items.BRONZE_PLATELEGS_1075,Items.BRONZE_CHAINBODY_1103),
            listOf(Items.BRONZE_PLATELEGS_1075,Items.BRONZE_CHAINBODY_1103,Items.BRONZE_2H_SWORD_1307),
            listOf(Items.BRONZE_PLATELEGS_1075,Items.BRONZE_CHAINBODY_1103,Items.BRONZE_SCIMITAR_1321),
            listOf(Items.BRONZE_PLATELEGS_1075,Items.BRONZE_CHAINBODY_1103,Items.BRONZE_BATTLEAXE_1375),
            listOf(Items.BRONZE_PLATELEGS_1075,Items.BRONZE_CHAINBODY_1103,Items.BRONZE_BATTLEAXE_1375,Items.BRONZE_SQ_SHIELD_1173),
            listOf(Items.BRONZE_PLATELEGS_1075,Items.BRONZE_PLATEBODY_1117, Items.TEAM_27_CAPE_4367),
            listOf(Items.BRONZE_PLATELEGS_1075,Items.BRONZE_PLATEBODY_1117,Items.BRONZE_FULL_HELM_1155, Items.TEAM_27_CAPE_4367),
            listOf(Items.BRONZE_PLATESKIRT_1087,Items.BRONZE_CHAINBODY_1103),
            listOf(Items.BRONZE_PLATESKIRT_1087,Items.BRONZE_CHAINBODY_1103,Items.BRONZE_FULL_HELM_1155),
            listOf(Items.BRONZE_PLATESKIRT_1087,Items.BRONZE_CHAINBODY_1103,Items.BRONZE_FULL_HELM_1155, Items.TEAM_28_CAPE_4369),
            listOf(Items.BRONZE_PLATESKIRT_1087,Items.BRONZE_PLATEBODY_1117, Items.TEAM_28_CAPE_4369),
            listOf(),   // No equipment
            listOf(Items.LEATHER_BODY_1129,Items.LEATHER_CHAPS_1095),
            listOf(),   // No equipment
            listOf(Items.STUDDED_CHAPS_1097,Items.STUDDED_BODY_1133),
            listOf(Items.MIME_LEGS_3059,Items.MIME_TOP_3058,Items.MIME_BOOTS_3061,Items.MIME_GLOVES_3060,Items.MIME_MASK_3057),
            listOf(Items.MIME_LEGS_3059,Items.MIME_TOP_3058,Items.MIME_BOOTS_3061,Items.MIME_GLOVES_3060),
            listOf(Items.MONKS_ROBE_542,Items.MONKS_ROBE_544),
            listOf(Items.CHICKEN_LEGS_11022,Items.CHICKEN_WINGS_11020,Items.CHICKEN_HEAD_11021,Items.CHICKEN_FEET_11019),
            listOf(Items.TOY_KITE_12844,Items.CHOCATRICE_CAPE_12634),
            listOf(),   // No equipment
            listOf(Items.WHITE_APRON_1005, Items.CHEFS_HAT_1949),
            listOf(Items.TEAM_10_CAPE_4333),
            listOf(Items.TEAM_2_CAPE_4317),
            listOf(),   // No equipment
            listOf(),   // No equipment
            listOf(),   // No equipment
            listOf())

    val AVGSETS = arrayOf(
            listOf(Items.SKELETAL_TOP_6139, Items.SKELETAL_BOTTOMS_6141),
            listOf(Items.SKELETON_LEGGINGS_9923, Items.SKELETON_SHIRT_9924, Items.SKELETON_MASK_9925),
            listOf(Items.BLACK_ELE_SHIRT_10400,Items.BLACK_ELE_LEGS_10402,Items.BROWN_HEADBAND_2649),
            listOf(Items.RED_ELE_SHIRT_10404,Items.RED_ELE_LEGS_10406),
            listOf(Items.COMBAT_ROBE_TOP_100_12971, Items.COMBAT_ROBE_BOTTOM_100_12978),
            listOf(Items.COMBAT_ROBE_TOP_100_12971, Items.COMBAT_ROBE_BOTTOM_100_12978, Items.COMBAT_HOOD_100_12964),
            listOf(Items.COMBAT_ROBE_TOP_100_12971, Items.COMBAT_ROBE_BOTTOM_100_12978, Items.RUNE_BERSERKER_SHIELD_100_12929),
            listOf(Items.COMBAT_ROBE_TOP_100_12971, Items.COMBAT_ROBE_BOTTOM_100_12978, Items.ADAMANT_BERSERKER_SHIELD_100_12915),
            listOf(Items.DRUIDIC_MAGE_TOP_100_12894, Items.DRUIDIC_MAGE_BOTTOM_100_12901, Items.DRUIDIC_MAGE_HOOD_100_12887),
            listOf(),   // No equipment
            listOf(Items.MITHRIL_PLATELEGS_1071, Items.MITHRIL_PLATEBODY_1121, Items.MITHRIL_KITESHIELD_1197, Items.MITHRIL_LONGSWORD_1299, Items.TEAM_29_CAPE_4371),
            listOf(Items.MITHRIL_PLATESKIRT_1085, Items.MITHRIL_PLATEBODY_1121, Items.MITHRIL_KITESHIELD_1197, Items.MITHRIL_LONGSWORD_1299, Items.TEAM_30_CAPE_4373),
            listOf(Items.MITHRIL_PLATELEGS_1071, Items.MITHRIL_PLATEBODY_1121, Items.TEAM_29_CAPE_4371),
            listOf(Items.MITHRIL_PLATESKIRT_1085, Items.MITHRIL_PLATEBODY_1121, Items.TEAM_30_CAPE_4373),
            listOf(Items.MITHRIL_PLATESKIRT_1085, Items.MITHRIL_CHAINBODY_1109, Items.TEAM_30_CAPE_4373),
            listOf(Items.MITHRIL_PLATELEGS_1071, Items.MITHRIL_CHAINBODY_1109, Items.TEAM_31_CAPE_4375),
            listOf(Items.MITHRIL_PLATELEGS_1071, Items.MITHRIL_CHAINBODY_1109, Items.MITHRIL_2H_SWORD_1315, Items.TEAM_34_CAPE_4381),
            listOf(Items.MITHRIL_PLATELEGS_1071, Items.MITHRIL_CHAINBODY_1109, Items.MITHRIL_SCIMITAR_1329, Items.TEAM_35_CAPE_4383),
            listOf(Items.MITHRIL_PLATELEGS_1071, Items.MITHRIL_CHAINBODY_1109, Items.MITHRIL_BATTLEAXE_1369, Items.TEAM_36_CAPE_4385),
            listOf(),   // No equipment
            listOf(Items.ADAMANT_PLATELEGS_1073,Items.ADAMANT_PLATEBODY_1124,Items.ADAMANT_KITESHIELD_1199,Items.ADAMANT_LONGSWORD_1301, Items.TEAM_29_CAPE_4371),
            listOf(Items.ADAMANT_PLATESKIRT_1091,Items.ADAMANT_PLATEBODY_1124,Items.ADAMANT_KITESHIELD_1199,Items.ADAMANT_LONGSWORD_1301, Items.TEAM_30_CAPE_4373),
            listOf(Items.ADAMANT_PLATELEGS_1073,Items.ADAMANT_PLATEBODY_1124, Items.TEAM_29_CAPE_4371),
            listOf(Items.ADAMANT_PLATESKIRT_1091,Items.ADAMANT_PLATEBODY_1124, Items.TEAM_30_CAPE_4373),
            listOf(Items.ADAMANT_PLATESKIRT_1091,Items.ADAMANT_CHAINBODY_1111, Items.TEAM_30_CAPE_4373),
            listOf(Items.ADAMANT_PLATELEGS_1073,Items.ADAMANT_CHAINBODY_1111, Items.TEAM_31_CAPE_4375),
            listOf(Items.ADAMANT_PLATESKIRT_1091,Items.ADAMANT_CHAINBODY_1111,Items.ADAMANT_FULL_HELM_1161, Items.TEAM_30_CAPE_4373),
            listOf(Items.ADAMANT_PLATELEGS_1073,Items.ADAMANT_CHAINBODY_1111,Items.ADAMANT_FULL_HELM_1161, Items.TEAM_31_CAPE_4375),
            listOf(Items.ADAMANT_PLATELEGS_1073,Items.ADAMANT_CHAINBODY_1111,Items.ADAMANT_2H_SWORD_1317, Items.TEAM_34_CAPE_4381),
            listOf(Items.ADAMANT_PLATELEGS_1073,Items.ADAMANT_CHAINBODY_1111,Items.ADAMANT_SCIMITAR_1331, Items.TEAM_35_CAPE_4383),
            listOf(Items.ADAMANT_PLATELEGS_1073,Items.ADAMANT_CHAINBODY_1111,Items.ADAMANT_BATTLEAXE_1371, Items.TEAM_35_CAPE_4383),
            listOf(),   // No equipment
            listOf(Items.GREEN_DHIDE_BODY_1135,Items.GREEN_DHIDE_CHAPS_1099,Items.GREEN_DHIDE_VAMB_1065,Items.GREEN_DHIDE_COIF_100_12936,Items.AVAS_ACCUMULATOR_10499),
            listOf(Items.GREEN_DHIDE_BODY_1135,Items.GREEN_DHIDE_CHAPS_1099,Items.GREEN_DHIDE_VAMB_1065,Items.GREEN_DHIDE_COIF_100_12936,Items.ADAMANT_CROSSBOW_9183,Items.AVAS_ACCUMULATOR_10499),
            listOf(Items.TEAM_10_CAPE_4333),
            listOf(Items.TEAM_1_CAPE_4315),
            listOf(),   // No equipment
            listOf(),   // No equipment
            listOf())

    val RICHSETS = arrayOf(
            listOf(Items.RUNE_PLATELEGS_1079,Items.RUNE_PLATEBODY_1127,Items.RUNE_KITESHIELD_1201,Items.RUNE_FULL_HELM_1163,Items.RUNE_LONGSWORD_1303,Items.TEAM_29_CAPE_4371),
            listOf(Items.RUNE_PLATESKIRT_1093,Items.RUNE_PLATEBODY_1127,Items.RUNE_KITESHIELD_1201,Items.RUNE_FULL_HELM_1163,Items.RUNE_LONGSWORD_1303,Items.TEAM_30_CAPE_4373),
            listOf(Items.RUNE_PLATESKIRT_1093,Items.RUNE_PLATEBODY_1127, Items.TEAM_29_CAPE_4371),
            listOf(Items.RUNE_PLATELEGS_1079,Items.RUNE_PLATEBODY_1127,Items.RUNE_FULL_HELM_1163,Items.TEAM_29_CAPE_4371),
            listOf(Items.RUNE_PLATELEGS_1079,Items.RUNE_PLATEBODY_1127,Items.RUNE_BERSERKER_SHIELD_100_12929,Items.TEAM_29_CAPE_4371),
            listOf(Items.RUNE_PLATESKIRT_1093,Items.RUNE_PLATEBODY_1127,Items.TEAM_30_CAPE_4373),
            listOf(Items.RUNE_PLATESKIRT_1093,Items.RUNE_CHAINBODY_1113,Items.TEAM_30_CAPE_4373),
            listOf(Items.RUNE_PLATELEGS_1079,Items.RUNE_CHAINBODY_1113,Items.TEAM_31_CAPE_4375),
            listOf(Items.RUNE_PLATESKIRT_1093,Items.RUNE_CHAINBODY_1113,Items.RUNE_FULL_HELM_1163,Items.TEAM_30_CAPE_4373),
            listOf(Items.RUNE_PLATELEGS_1079,Items.RUNE_CHAINBODY_1113,Items.RUNE_FULL_HELM_1163,Items.TEAM_31_CAPE_4375),
            listOf(Items.RUNE_PLATELEGS_1079,Items.RUNE_CHAINBODY_1113,Items.RUNE_2H_SWORD_1319,Items.TEAM_34_CAPE_4381),
            listOf(Items.RUNE_PLATELEGS_1079,Items.RUNE_CHAINBODY_1113,Items.RUNE_SCIMITAR_1333,Items.TEAM_35_CAPE_4383),
            listOf(Items.RUNE_PLATELEGS_1079,Items.RUNE_CHAINBODY_1113,Items.RUNE_BATTLEAXE_1373,Items.TEAM_35_CAPE_4383),
            listOf(Items.RUNE_PLATELEGS_1079,Items.RUNE_PLATEBODY_1127,Items.ABYSSAL_WHIP_4151,Items.TEAM_29_CAPE_4371),
            listOf(),   // No equipment
            listOf(Items.RUNE_PLATEBODY_G_2615,Items.RUNE_PLATELEGS_G_2618,Items.RUNE_KITESHIELD_G_2621,Items.RUNE_FULL_HELMG_2619, Items.TEAM_27_CAPE_4367),
            listOf(Items.RUNE_PLATEBODY_T_2623,Items.RUNE_PLATELEGS_T_2625,Items.RUNE_KITESHIELD_T_2629,Items.RUNE_FULL_HELM_T_2627, Items.TEAM_27_CAPE_4367),
            listOf(Items.GILDED_PLATEBODY_3481,Items.GILDED_PLATELEGS_3483,Items.CAPE_1030),
            listOf(Items.ZAMORAK_PLATEBODY_2653,Items.ZAMORAK_PLATELEGS_2655,Items.ZAMORAK_FULL_HELM_2657),
            listOf(Items.SARADOMIN_PLATEBODY_2661,Items.SARADOMIN_PLATELEGS_2663,Items.SARADOMIN_FULL_HELM_2665),
            listOf(Items.GUTHIX_PLATEBODY_2669,Items.GUTHIX_PLATELEGS_2671,Items.GUTHIX_FULL_HELM_2673),
            listOf(Items.ZAMORAK_PLATEBODY_2653,Items.ZAMORAK_PLATELEGS_2655,Items.ZAMORAK_FULL_HELM_2657,Items.ZAMORAK_CAPE_2414),
            listOf(Items.SARADOMIN_PLATEBODY_2661,Items.SARADOMIN_PLATELEGS_2663,Items.SARADOMIN_FULL_HELM_2665,Items.SARADOMIN_CAPE_2412),
            listOf(Items.GUTHIX_PLATEBODY_2669,Items.GUTHIX_PLATELEGS_2671,Items.GUTHIX_FULL_HELM_2673,Items.GUTHIX_CAPE_2413),
            listOf(Items.THIRD_AGE_ROBE_10340,Items.THIRD_AGE_ROBE_TOP_10338,Items.SANTA_HAT_1050),
            listOf(),   // No equipment
            listOf(Items.DRAGON_PLATELEGS_4087,Items.DRAGON_CHAINBODY_3140,Items.ABYSSAL_WHIP_4151,Items.DRAGON_FULL_HELM_11335),
            listOf(Items.DRAGON_PLATELEGS_4087,Items.DRAGON_CHAINBODY_3140,Items.DRAGONFIRE_SHIELD_11283,Items.DRAGON_FULL_HELM_11335),
            listOf(),   // No equipment
            listOf(Items.BLACK_DHIDE_BODY_2503,Items.BLACK_DHIDE_CHAPS_2497,Items.BLACK_DHIDE_VAMB_2491,Items.RUNE_CROSSBOW_9185,Items.AVAS_ACCUMULATOR_10499),
            listOf(Items.BLACK_DHIDE_BODY_2503,Items.BLACK_DHIDE_CHAPS_2497,Items.BLACK_DHIDE_VAMB_2491,Items.BLACK_DHIDE_COIF_100_12957,Items.RUNE_CROSSBOW_9185,Items.RUNE_KITESHIELD_1201),
            listOf(Items.ELITE_BLACK_PLATEBODY_14492,Items.ELITE_BLACK_PLATELEGS_14490, Items.ELITE_BLACK_FULL_HELM_14494),
            listOf(Items.BATTLE_ROBE_BOTTOM_100_12880,Items.BATTLE_ROBE_TOP_100_12873,Items.PURPLE_PARTYHAT_1046),
            listOf(Items.ZURIELS_ROBE_BOTTOM_13862,Items.ZURIELS_ROBE_TOP_13858,Items.ZURIELS_HOOD_13864,Items.ZURIELS_HOOD_13864,Items.ZURIELS_STAFF_13867),
            listOf(Items.VESTAS_CHAINBODY_13887,Items.VESTAS_PLATESKIRT_13893),
            listOf(),   // No equipment
            listOf(),   // No equipment
            listOf())
}

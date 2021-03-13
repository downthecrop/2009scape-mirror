package rs09.game.content.activity.gnomecooking

import core.game.node.item.Item
import core.game.node.item.WeightedChanceItem
import core.tools.RandomFunction
import org.rs09.consts.Items

object GnomeTipper {
    private val easyTips = arrayListOf(
            WeightedChanceItem(995,50,100,30),
            WeightedChanceItem(995,23,76,50),
            WeightedChanceItem(995,10,250,20)
    )

    private val hardTips = arrayListOf(
            //Uniques Weight = 18
            WeightedChanceItem(Items.GNOME_GOGGLES_9472,1,2),
            WeightedChanceItem(Items.GNOME_SCARF_9470,1,2),
            WeightedChanceItem(Items.GRAND_SEED_POD_9469,5,7),
            WeightedChanceItem(Items.MINT_CAKE_9475,1,4),
            WeightedChanceItem(Items.GNOMEBALL_751,1,4),
            //Herbs Weight = 20
            WeightedChanceItem(Items.CLEAN_TOADFLAX_2998,3,10),
            WeightedChanceItem(Items.CLEAN_SNAPDRAGON_3000,1,10),
            //Uncut Gems Weight = 46
            WeightedChanceItem(Items.RED_TOPAZ_1613,1,8),
            WeightedChanceItem(Items.DIAMOND_1601,1,7),
            WeightedChanceItem(Items.UNCUT_EMERALD_1621,3,5,7),
            WeightedChanceItem(Items.UNCUT_JADE_1627,2,3,7),
            WeightedChanceItem(Items.UNCUT_SAPPHIRE_1623,6,10,8),
            WeightedChanceItem(Items.UNCUT_RUBY_1619,2,3,7),
            WeightedChanceItem(Items.UNCUT_OPAL_1625,1,10),
            //Runes Weight = 25
            WeightedChanceItem(Items.COSMIC_RUNE_564,11,5),
            WeightedChanceItem(Items.NATURE_RUNE_561,10,15,5),
            WeightedChanceItem(Items.LAW_RUNE_563,10,5),
            WeightedChanceItem(Items.DEATH_RUNE_560,11,5),
            WeightedChanceItem(Items.SOUL_RUNE_566,9,5),
            //Untipped crossbow bolts Weight = 9
            WeightedChanceItem(Items.MITHRIL_BOLTS_UNF_9379,5,10,3),
            WeightedChanceItem(Items.ADAMANT_BOLTSUNF_9380,3,5,3),
            WeightedChanceItem(Items.RUNITE_BOLTS_UNF_9381,1,3,3),
            //Other tips
            WeightedChanceItem(Items.LOOP_HALF_OF_A_KEY_987,1,9),
            WeightedChanceItem(Items.TOOTH_HALF_OF_A_KEY_985,1,9),
            WeightedChanceItem(Items.PURE_ESSENCE_7937,97,3),
            WeightedChanceItem(Items.BIRDS_NEST_5072,1,2),
            WeightedChanceItem(Items.YEW_SEED_5315,1,6),
            WeightedChanceItem(Items.CALQUAT_TREE_SEED_5290,1,6)

    )

    enum class LEVEL {
        EASY,
        HARD
    }

    @JvmStatic
    fun getTip(level: LEVEL): Item {
        return when(level){
            LEVEL.EASY -> RandomFunction.rollWeightedChanceTable(easyTips)
            LEVEL.HARD -> RandomFunction.rollWeightedChanceTable(hardTips)
        }
    }



}
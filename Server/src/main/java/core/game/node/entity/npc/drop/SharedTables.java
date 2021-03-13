package core.game.node.entity.npc.drop;

import org.rs09.consts.Items;
import core.game.node.item.WeightedChanceItem;

public enum SharedTables {
    COMMON_SEEDS(new WeightedChanceItem[] {
            new WeightedChanceItem(Items.LIMPWURT_SEED_5100,1,137),
            new WeightedChanceItem(Items.STRAWBERRY_SEED_5323,1,131),
            new WeightedChanceItem(Items.MARRENTILL_SEED_5292,1,125),
            new WeightedChanceItem(Items.JANGERBERRY_SEED_5104,1,92),
            new WeightedChanceItem(Items.TARROMIN_SEED_5293,1,85),
            new WeightedChanceItem(Items.WILDBLOOD_SEED_5311,1,83),
            new WeightedChanceItem(Items.WATERMELON_SEED_5321,1,63),
            new WeightedChanceItem(Items.HARRALANDER_SEED_5294,1,56),
            new WeightedChanceItem(Items.RANARR_SEED_5295,1,39),
            new WeightedChanceItem(Items.WHITEBERRY_SEED_5105,1,34),
            new WeightedChanceItem(Items.MUSHROOM_SPORE_5282,1,29),
            new WeightedChanceItem(Items.TOADFLAX_SEED_5296,1,27),
            new WeightedChanceItem(Items.BELLADONNA_SEED_5281,1,18),
            new WeightedChanceItem(Items.IRIT_SEED_5297,1,18),
            new WeightedChanceItem(Items.POISON_IVY_SEED_5106,1,13),
            new WeightedChanceItem(Items.AVANTOE_SEED_5298,1,12),
            new WeightedChanceItem(Items.CACTUS_SEED_5280,1,12),
            new WeightedChanceItem(Items.KWUARM_SEED_5299,1,9),
            new WeightedChanceItem(Items.SNAPDRAGON_SEED_5300,1,5),
            new WeightedChanceItem(Items.CADANTINE_SEED_5301,1,4),
            new WeightedChanceItem(Items.LANTADYME_SEED_5302,1,3),
            new WeightedChanceItem(Items.DWARF_WEED_SEED_5303,1,2),
            new WeightedChanceItem(Items.TORSTOL_SEED_5304,1,1)
    }),

    FIXED_ALLOTMENT_SEEDS(new WeightedChanceItem[]{
            new WeightedChanceItem(Items.POTATO_SEED_5318, 4, 48),
            new WeightedChanceItem(Items.ONION_SEED_5319,4,36),
            new WeightedChanceItem(Items.CABBAGE_SEED_5324,4,24),
            new WeightedChanceItem(Items.TOMATO_SEED_5322,3,12),
            new WeightedChanceItem(Items.SWEETCORN_SEED_5320,3,6),
            new WeightedChanceItem(Items.STRAWBERRY_SEED_5323,2,3),
            new WeightedChanceItem(Items.WATERMELON_SEED_5321,2,2),
    }),

    RARE_SEEDS(new WeightedChanceItem[]{
            new WeightedChanceItem(Items.TOADFLAX_SEED_5296,1,24),
            new WeightedChanceItem(Items.IRIT_SEED_5297,1,16),
            new WeightedChanceItem(Items.BELLADONNA_SEED_5281,1,16),
            new WeightedChanceItem(Items.AVANTOE_SEED_5298,1,11),
            new WeightedChanceItem(Items.POISON_IVY_SEED_5106,1,11),
            new WeightedChanceItem(Items.CACTUS_SEED_5280,1,11),
            new WeightedChanceItem(Items.KWUARM_SEED_5299,1,8),
            new WeightedChanceItem(Items.SNAPDRAGON_SEED_5300,1,5),
            new WeightedChanceItem(Items.CADANTINE_SEED_5301,1,4),
            new WeightedChanceItem(Items.LANTADYME_SEED_5302,1,3),
            new WeightedChanceItem(Items.DWARF_WEED_SEED_5303,1,2),
            new WeightedChanceItem(Items.TORSTOL_SEED_5304,1,1)
    }),

    TREE_HERB_SEEDS(new WeightedChanceItem[]{
            new WeightedChanceItem(Items.RANARR_SEED_5295,1,8),
            new WeightedChanceItem(Items.SNAPDRAGON_SEED_5300,1,7),
            new WeightedChanceItem(Items.TORSTOL_SEED_5304,1,6),
            new WeightedChanceItem(Items.WATERMELON_SEED_5321,15,5),
            new WeightedChanceItem(Items.WILLOW_SEED_5313,1,5),
            new WeightedChanceItem(Items.MAPLE_SEED_5314,1,5),
            new WeightedChanceItem(Items.YEW_SEED_5315,1,5),
            new WeightedChanceItem(Items.PAPAYA_TREE_SEED_5288,1,4),
            new WeightedChanceItem(Items.MAGIC_SEED_5316, 1, 3),
            new WeightedChanceItem(Items.PALM_TREE_SEED_5289,1, 3),
            new WeightedChanceItem(Items.SPIRIT_SEED_5317,1,2)
    }),

    VARIABLE_ALLOTMENT_SEEDS(new WeightedChanceItem[]{
            new WeightedChanceItem(Items.POTATO_SEED_5318,1,4,64),
            new WeightedChanceItem(Items.ONION_SEED_5319,1,3,32),
            new WeightedChanceItem(Items.CABBAGE_SEED_5324,1,3,16),
            new WeightedChanceItem(Items.TOMATO_SEED_5322,1,2,8),
            new WeightedChanceItem(Items.SWEETCORN_SEED_5320,1,2,4),
            new WeightedChanceItem(Items.STRAWBERRY_SEED_5323,1,2),
            new WeightedChanceItem(Items.WATERMELON_SEED_5321,1,1)
    }),

    HOPS_DROP_TABLE(new WeightedChanceItem[]{
            new WeightedChanceItem(Items.BARLEY_SEED_5305,4,35),
            new WeightedChanceItem(Items.HAMMERSTONE_SEED_5307,4,28),
            new WeightedChanceItem(Items.JUTE_SEED_5306,2,19),
            new WeightedChanceItem(Items.ASGARNIAN_SEED_5308,3,17),
            new WeightedChanceItem(Items.YANILLIAN_SEED_5309,2,12),
            new WeightedChanceItem(Items.KRANDORIAN_SEED_5310,2,4),
            new WeightedChanceItem(Items.WILDBLOOD_SEED_5311,1, 1)
    }),

    HERBS(new WeightedChanceItem[]{
            new WeightedChanceItem(Items.GRIMY_GUAM_199, 1, 11),
            new WeightedChanceItem(Items.GRIMY_MARRENTILL_201,1,8),
            new WeightedChanceItem(Items.GRIMY_TARROMIN_203,1,6),
            new WeightedChanceItem(Items.GRIMY_HARRALANDER_205,1,5),
            new WeightedChanceItem(Items.GRIMY_RANARR_208,1,4),
            new WeightedChanceItem(Items.GRIMY_IRIT_209,1,3),
            new WeightedChanceItem(Items.GRIMY_AVANTOE_212,1,2),
            new WeightedChanceItem(Items.GRIMY_KWUARM_213,1,2),
            new WeightedChanceItem(Items.GRIMY_CADANTINE_215,1,2),
            new WeightedChanceItem(Items.GRIMY_LANTADYME_2485,1,1),
            new WeightedChanceItem(Items.GRIMY_DWARF_WEED_217,1,1)
    }),

    USEFUL_HERBS(new WeightedChanceItem[]{
            new WeightedChanceItem(Items.GRIMY_AVANTOE_212,1,2),
            new WeightedChanceItem(Items.GRIMY_SNAPDRAGON_3052,1,1),
            new WeightedChanceItem(Items.GRIMY_RANARR_208,1,1),
            new WeightedChanceItem(Items.GRIMY_TORSTOL_220,1,1)
    });



    WeightedChanceItem[] table;
    SharedTables(WeightedChanceItem[] table){
        this.table = table;
    }


}

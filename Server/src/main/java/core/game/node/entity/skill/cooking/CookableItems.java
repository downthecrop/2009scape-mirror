package core.game.node.entity.skill.cooking;

import org.rs09.consts.Items;
import core.game.node.item.Item;

import java.util.HashMap;

public enum CookableItems {
    /** meats */
    CHICKEN(2140,2138,2144,1,30,30,128,512),
    UGTHANKI(2140,2138,2144,1,40,40,128,512),
    RABBIT(3228,3226,7222,1,30,30,128,512),
    DARK_CRAB(14939, 14937, 14941, 90, 215, 100,0,150),
    CRAB(7521, 7518, 7520,21, 100, 100,85,438),

    /** fish */
    SHRIMP(315, 317, 7954,1, 45, 35,128,512),
    KARAMBWANJI(3151, 3150, 592,1, 10, 30,128,512),
    SARDINE(325, 327, 369,1, 40, 38,118,492),
    ANCHOVIES(319, 321, 323,1, 30, 34,128,512),
    HERRING(347, 345, 357,5, 50, 41,108,472),
    MACKEREL(355, 353, 357,10, 60, 45,98,452),
    TROUT(333, 335, 343,15, 70, 50,88,432),
    COD(339, 341, 343,18, 75, 52,74,420),
    PIKE(351, 349, 343,20, 80, 53,78,412),
    SALMON(329, 331, 343,25, 58, 90,68,392),
    SLIMY_EEL(3381, 3379, 3383,28, 95, 58,72,406),
    TUNA(361, 359, 367,30, 100, 64,58,372),
    RAINBOW_FISH(10136, 10138, 10140,35, 110, 60,48,387),
    CAVE_EEL(5003, 5001, 5002,38, 115, 40,42,386),
    LOBSTER(379, 377, 381,40, 120, 74,38,332),
    BASS(365, 363, 367,43, 130, 80,28,312),
    SWORDFISH(373, 371, 375,45, 140, 86,18,292),
    LAVA_EEL(2149, 2148, 3383,53, 30, 53,12,282),
    MONKFISH(7946, 7944, 7948,62, 150, 92,11,275),
    SHARK(385, 383, 387,80, 210, 100,0,202),
    SEA_TURTLE(397, 395, 399,82, 212, 100,0,202),
    MANTA_RAY(391, 389, 393,91, 216, 100,0,202),
    KARAMBWAN(3144, 3142, 3146,1, 80, 30,70,255),

    /** snails */
    THIN_SNAIL(3369, 3363, 3375,12, 70, 70,109,502),
    LEAN_SNAIL(3371, 3365, 3375,17, 80, 80,100,486),
    FAT_SNAIL(3373, 3367, 3375,22, 95, 95,95,456),

    //Bread
    BREAD(2309, 2307, 2311,1,40,40,118,492),

    //Cake
    CAKE(1891, 1889, 1903, 40, 180, 95,38,332),

    //Beef(s) (Rat, Bear, Cow, Yak)
    BEEF(2142,2132,2146,1,30,30,128,512),
    RAT_MEAT(2142,2134,2146,1,30,30,128,512),
    BEAR_MEAT(2142,2136,2146,1,30,30,128,512),
    YAK_MEAT(2142,10816,2146,1,30,30,128,512),

    //Skewered foods
    SKEWER_1(9988, 9986, 9990,21, 82, 82,108,496),
    SKEWER_2(2878, 2876, 7226,30, 140, 140,85,476),
    SKEWER_3(7568, 7566, 7570,41, 160, 140,75,456),
    SKEWER_4(9980, 9984, 9982,11, 62, 62,115,502),
    SKEWER_5(7223, 7224, 7222,16, 70, 62,113,499),

    //Pies
    PIE_1(2325, 2321, 2329, 10, 78, 60,78,412),
    PIE_2(2327, 2319, 2329, 20, 110, 80,78,412),
    PIE_3(7170, 7169, 2329, 29,128,85,78,412),
    PIE_4(2323,2317,2329,30,130,85,78,412),
    GARDEN_PIE(7178,7176,2329,34,138, 89,78,412),
    FISH_PIE(7188,7186,2329,47,164,110,78,412),
    ADMIRAL_PIE(7198,7196,2329,70,210,140,78,412),
    WILD_PIE(7208,7206,2329,85,240,140,78,412),
    SUMMER_PIE(7218,7216,2329,95,260,160,78,412),

    //Pizzas
    PIZZA_1(2289, 2287,2305, 35, 143, 86,48,352),

    //Bowl foods
    BOWL_1(2003, 2001, 2005,25, 117, 58,68,392),
    BOWL_2(4239, 4237, 4239,20, 52, 1,68,392),
    BOWL_3(7078, 7076, 7090,13, 50, 60,68,392),
    BOWL_4(7084, 1871, 7092,43, 60, 70,68,392),
    BOWL_5(7082, 7080, 7094,57, 120, 80,68,392),

    /* Vegetables */
    BAKED_POTATO(6701, 1942, 6699, 7, 15, 41, 108, 472),
    SWEETCORN(5988, 5986, 5990, 28, 104, 54, 90, 424),

    //Miscellaneous
    RAW_OOMLIE(Items.RAW_OOMLIE_2337, 0, Items.BURNT_OOMLIE_2426, 50, 0, 999,0,0), // always burns
    OOMLIE_WRAP(Items.COOKED_OOMLIE_WRAP_2343, Items.WRAPPED_OOMLIE_2341, Items.BURNT_OOMLIE_WRAP_2345, 50, 110, 999,0,0);

    public static HashMap<Integer,CookableItems>cookingMap = new HashMap<>();
    public static HashMap<Integer, CookableItems>intentionalBurnMap = new HashMap<>();
    public int raw,cooked,level,burnLevel,burnt;
    double experience;
    double low,high;
    CookableItems(int cooked, int raw, int burnt, int level, double experience, int burnLevel, double low, double high){
        this.raw = raw;
        this.cooked = cooked;
        this.burnt = burnt;
        this.level = level;
        this.experience = experience;
        this.burnLevel = burnLevel;
        this.low = low;
        this.high = high;
    }

    static{
        CookableItems[] cookableItems = values();
        int cookableItemsLength = cookableItems.length;
        for(int x = 0; x < cookableItemsLength; x++){
            CookableItems item = cookableItems[x];
            cookingMap.putIfAbsent(item.raw,item);
            intentionalBurnMap.putIfAbsent(item.cooked,item);
        }
    }

    public static CookableItems forId(int id){
        return cookingMap.get(id);
    }

    public static Item getCooked(int id){
        return new Item(cookingMap.get(id).cooked);
    }

    public static Item getBurnt(int id){
        return new Item(cookingMap.get(id).burnt);
    }

    public static Item getRaw(int id){
        return new Item(cookingMap.get(id).raw);
    }

    public static boolean intentionalBurn(int id){
        return (intentionalBurnMap.get(id) != null);
    }

    public static Item getIntentionalBurn(int id){
        return new Item(intentionalBurnMap.get(id).burnt);
    }

    public static int getBurnLevel(int id){
        return cookingMap.get(id).burnLevel;
    }
}

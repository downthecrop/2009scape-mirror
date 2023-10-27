package content.global.skill.fishing

import core.api.asItem
import core.game.node.item.Item
import org.rs09.consts.Items

enum class Fish(val id: Int, val level: Int, val experience: Double, val lowChance: Double, val highChance: Double) {
    CRAYFISH(Items.RAW_CRAYFISH_13435, 1, 10.0, 0.15, 0.5),
    SHRIMP(Items.RAW_SHRIMPS_317, 1, 10.0, 0.191, 0.5),
    SARDINE(Items.RAW_SARDINE_327, 5, 20.0, 0.148, 0.374),
    KARAMBWANJI(Items.RAW_KARAMBWANJI_3150, 5, 5.0, 0.4, 0.98),
    HERRING(Items.RAW_HERRING_345, 10, 30.0, 0.129, 0.504),
    ANCHOVIE(Items.RAW_ANCHOVIES_321, 15, 40.0, 0.098, 0.5),
    MACKEREL(Items.RAW_MACKEREL_353, 16, 20.0, 0.055, 0.258),
    TROUT(Items.RAW_TROUT_335, 20, 50.0, 0.246, 0.468),
    COD(Items.RAW_COD_341, 23, 45.0, 0.063, 0.219),
    PIKE(Items.RAW_PIKE_349, 25, 60.0, 0.14, 0.379),
    SLIMY_EEL(Items.SLIMY_EEL_3379, 28, 65.0, 0.117, 0.216),
    SALMON(Items.RAW_SALMON_331, 30, 70.0, 0.156, 0.378),
    FROG_SPAWN(Items.FROG_SPAWN_5004, 33, 75.0, 0.164, 0.379),
    TUNA(Items.RAW_TUNA_359, 35, 80.0, 0.109, 0.205),
    RAINBOW_FISH(Items.RAW_RAINBOW_FISH_10138, 38, 80.0, 0.113, 0.254),
    CAVE_EEL(Items.RAW_CAVE_EEL_5001, 38, 80.0, 0.145, 0.316),
    LOBSTER(Items.RAW_LOBSTER_377, 40, 90.0, 0.16, 0.375),
    BASS(Items.RAW_BASS_363, 46, 100.0, 0.078, 0.16),
    SWORDFISH(Items.RAW_SWORDFISH_371, 50, 100.0, 0.105, 0.191),
    LAVA_EEL(Items.RAW_LAVA_EEL_2148, 53, 30.0, 0.227, 0.379),
    MONKFISH(Items.RAW_MONKFISH_7944, 62, 120.0, 0.293, 0.356),
    KARAMBWAN(Items.RAW_KARAMBWAN_3142, 65, 105.0, 0.414, 0.629),
    SHARK(Items.RAW_SHARK_383, 76, 110.0, 0.121, 0.16),
    SEA_TURTLE(Items.RAW_SEA_TURTLE_395, 79, 38.0, 0.0, 0.0),
    MANTA_RAY(Items.RAW_MANTA_RAY_389, 81, 46.0, 0.0, 0.0),
    SEAWEED(Items.SEAWEED_401, 16, 1.0, 0.63, 0.219),
    CASKET(Items.CASKET_405, 16, 10.0, 0.63, 0.219),
    OYSTER(Items.OYSTER_407, 16, 10.0, 0.63, 0.219);

    companion object {
        val fishMap: HashMap<Int, Fish> = HashMap()
        val bigFishMap: HashMap<Fish, Int> = HashMap()
        init {
            for(fish in values()) {
                fishMap[fish.id] = fish
            }
            bigFishMap[Fish.BASS]      = Items.BIG_BASS_7989
            bigFishMap[Fish.SWORDFISH] = Items.BIG_SWORDFISH_7991
            bigFishMap[Fish.SHARK]     = Items.BIG_SHARK_7993
        }

        @JvmStatic
        fun forItem(item: Item) : Fish? {
            return fishMap[item.id]
        }

        @JvmStatic
        fun getBigFish(fish: Fish) : Int? {
            return bigFishMap[fish]
        }
    }

    fun getSuccessChance(level: Int): Double {
        return (level.toDouble() - 1.0) * ((highChance - lowChance) / (99.0 - 1.0)) + lowChance
    }

    fun getItem() : Item {
        return this.id.asItem()
    }
}
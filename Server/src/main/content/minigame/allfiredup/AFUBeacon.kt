package content.minigame.allfiredup

import core.api.*
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.tools.Log

/**
 * Various data for beacons, such as varp and offset, required FM level, etc
 * @author Ceikry, Player Name
 */
enum class AFUBeacon(val title: String, val fmLevel: Int, val varbit: Int, val location: Location, val experience: Double, val keeper: Int = 0) {
    RIVER_SALVE("near the River Salve", 43, 5146, Location.create(3396, 3464, 0), 216.2, 8065),
    RAG_AND_BONE("near the Rag and Bone Man", 43, 5147, Location.create(3343, 3510, 0), 235.8, 8066),
    JOLLY_BOAR("north of the Jolly Boar", 48, 5148, Location.create(3278, 3525, 0), 193.8, 8067),
    NORTH_VARROCK_CASTLE("north of Varrock Palace", 53, 5149, Location.create(3236, 3527, 0), 178.5, 8068),
    GRAND_EXCHANGE("north of the Grand Exchange", 59, 5150, Location.create(3170, 3536, 0), 194.3, 8069),
    EDGEVILLE("near Edgeville", 62, 5151, Location.create(3087, 3516, 0), 86.7, 8070),
    MONASTERY("near the Black Knights' Fortress", 68, 5152, Location.create(3034, 3518, 0), 224.4, 8071),
    GOBLIN_VILLAGE("near Goblin Village", 72, 5153, Location.create(2968, 3516, 0), 194.8, 8072),
    BURTHORPE("near Burthorpe", 76, 5154, Location.create(2940, 3565, 0), 195.3, 8073),
    DEATH_PLATEAU("east of Death Plateau", 79, 5155, Location.create(2944, 3622, 0), 249.9, 8074),
    TROLLHEIM("east of Trollheim", 83, 5156, Location.create(2939, 3680, 0), 201.0, 8075),
    GWD("east of the God Wars dungeon temple", 87, 5157, Location.create(2937, 3773, 0), 255.0, 8076),
    TEMPLE("north of the small temple in the Wilderness", 89, 5158, Location.create(2946, 3836, 0), 198.9),
    PLATEAU("on the Frozen Waste Plateau", 92, 5159, Location.create(2964, 3931, 0), 147.9);

    companion object {
        fun forLocation(location: Location): AFUBeacon {
            for (beacon in values()) {
                if (beacon.location.equals(location)) return beacon
            }
            return AFUBeacon.RIVER_SALVE.also { log(this::class.java, Log.WARN, "Unhandled Beacon Location ${location.toString()}") }
        }

        fun resetAllBeacons(player: Player){
            for(beacon in values()){
                setVarbit(player, beacon.varbit, 0)
            }
        }
    }

    fun light(player: Player){
        setVarbit(player, varbit, 2, true)
    }

    fun diminish(player: Player){
        setVarbit(player, varbit, 3, true)
    }

    fun extinguish(player: Player){
        setVarbit(player, varbit, 0, true)
    }

    fun lightGnomish(player: Player){
        setVarbit(player, varbit, 4, true)
    }

    fun fillWithLogs(player: Player){
        setVarbit(player, varbit, 1, true)
    }

    fun getState(player: Player): BeaconState {
        return BeaconState.values()[getVarbit(player, varbit)]
    }
}

enum class BeaconState{
    EMPTY,
    FILLED,
    LIT,
    DYING,
    WARNING
}

package content.global.activity.penguinhns

import core.ServerConstants
import core.api.getServerConfig
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs09.consts.NPCs
import core.game.worldevents.WorldEvents
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit

class PenguinSpawner {

    fun spawnPenguins(weekOffset: Int = 0): ArrayList<Int> {
        val spawnCycleBase = listOf(0, 9, 18, 27, 4)
        val cycleStart = LocalDate.of(2008, 9, 2)
        val today = LocalDate.now()
        val weeksSince = ChronoUnit.WEEKS.between(cycleStart, today)
        val cycleLength = 32
        val curSpawnCycle = spawnCycleBase.map { (it + weeksSince.toInt()+weekOffset)  % cycleLength }
        val spawnedOrdinals = ArrayList<Int>()
        curSpawnCycle.forEach { penguinPair ->
            spawnPenguin(Penguin.onePointPenguins[penguinPair])
            spawnedOrdinals.add(Penguin.onePointPenguins[penguinPair].ordinal)
            if (penguinPair == 8) {
                //8 is currently in the not accessible part of ape atoll, use accessible ape atoll penguin instead
                spawnPenguin(Penguin.twoPointPenguins[11])
                spawnedOrdinals.add(Penguin.twoPointPenguins[11].ordinal)
            } else if (penguinPair == 17) {
                //17 is currently on dragoontooth isle, use port Phasmatys instead
                spawnPenguin(Penguin.twoPointPenguins[27])
                spawnedOrdinals.add(Penguin.twoPointPenguins[27].ordinal)
            } else if (penguinPair == 31) {
                //31 is currently in the not accessible part of Neitiznot, use accessible Neitiznot penguin instead
                spawnPenguin(Penguin.twoPointPenguins[25])
                spawnedOrdinals.add(Penguin.twoPointPenguins[25].ordinal)
            } else {
                //this is otherwise the correct penguin to spawn for 2 points
                spawnPenguin(Penguin.twoPointPenguins[penguinPair])
                spawnedOrdinals.add(Penguin.twoPointPenguins[penguinPair].ordinal)
            }
        }
        return spawnedOrdinals
    }

    fun spawnPenguin(penguin: Penguin) {
        var pengID = penguin.id
        val halloweenStartDate = LocalDate.of(LocalDate.now().year, Month.OCTOBER, 28)
        val halloweenEndDate = LocalDate.of(LocalDate.now().year, Month.NOVEMBER, 11)
        val currentDate = LocalDate.now()
        if ((!currentDate.isBefore(halloweenStartDate) && !currentDate.isAfter(halloweenEndDate)) || ServerConstants.FORCE_HALLOWEEN_EVENTS) {
            pengID = NPCs.PUMPKIN_8077
        }
        NPC(pengID,penguin.location)
            .also { PenguinManager.npcs.add(it); it.isNeverWalks = false; it.isWalks = true; it.walkRadius = 200}.init()
    }

    fun spawnPenguins(ordinals: List<Int>) {
        val penguinCount = Penguin.values().size
        ordinals.forEach {
            var penguinOrdinal = it
            if (it > penguinCount) {
                penguinOrdinal = 0
            }
            val peng = Penguin.values()[penguinOrdinal]
            spawnPenguin(peng)
        }
    }
}

enum class Penguin(val id: Int, val hint: String, val location: Location, val points: Int){
    CACTUS_1(NPCs.CACTUS_8107, "located in the northern desert.", Location(3310, 3157, 0), 1),
    CACTUS_2(NPCs.CACTUS_8107, "located in the southern desert.", Location.create(3259, 3052, 0), 1),
    BUSH_1(NPCs.BUSH_8105, "located between Fremennik and barbarians.", Location.create(2532, 3588, 0), 1),
    BUSH_2(NPCs.BUSH_8105, "located where banana smugglers dwell.", Location.create(2740, 3233, 0), 1),
    BUSH_3(NPCs.BUSH_8105, "located south of Ardougne.", Location.create(2456, 3092, 0), 1),
    BUSH_4(NPCs.BUSH_8105, "located deep in the jungle.", Location.create(2832, 3053, 0), 1),
    BUSH_5(NPCs.BUSH_8105, "located where eagles fly.", Location.create(2326, 3516, 0), 1),
    BUSH_6(NPCs.BUSH_8105, "located south of Ardougne.", Location.create(2513, 3154, 0), 1),
    BUSH_7(NPCs.BUSH_8105, "located near a big tree surrounded by short people.", Location.create(2387, 3451, 0), 1),
    BUSH_8(NPCs.BUSH_8105, "located in the kingdom of Asgarnia.", Location.create(2951, 3511, 0), 1),
    BUSH_9(NPCs.BUSH_8105, "located in the northern desert.", Location.create(3350, 3311, 0), 1),
    BUSH_10(NPCs.BUSH_8105, "located somewhere in the kingdom of Kandarin.", Location.create(2633, 3501, 0), 1),
    BUSH_11(NPCs.BUSH_8105, "located where wizards study.", Location.create(3112, 3149, 0), 1),
    ROCK_1(NPCs.ROCK_8109, "located where the Imperial Guard train.", Location.create(2852, 3578, 0), 1),
    ROCK_2(NPCs.ROCK_8109, "located in the kingdom of Misthalin.", Location.create(3356, 3416, 0), 1),
    ROCK_3(NPCs.ROCK_8109, "located near some ogres.", Location.create(2631, 2980, 0), 1), //potentially incorrect location
    ROCK_4(NPCs.ROCK_8109, "located in the Kingdom of Asgarnia.", Location.create(3013, 3501, 0), 1),
    ROCK_5(NPCs.ROCK_8109, "located between Fremennik and barbarians.", Location.create(2532, 3630, 0), 1),
    CRATE_1(NPCs.CRATE_8108, "located in the kingdom of Misthalin.", Location.create(3112, 3332, 0), 1),
    CRATE_2(NPCs.CRATE_8108, "located in the Kingdom of Misthalin.", Location.create(3305, 3508, 0), 1),
    CRATE_3(NPCs.CRATE_8108, "located south of Ardougne.", Location.create(2440, 3206, 0), 1),
    BARREL_1(NPCs.BARREL_8104, "located where no weapons may go.", Location.create(2806, 3383, 0), 1),
    TOADSTOOL_1(NPCs.TOADSTOOL_8110, "located in the kingdom of Misthalin.", Location.create(3156, 3178, 0), 1),
    TOADSTOOL_2(NPCs.TOADSTOOL_8110, "located in the fairy realm.", Location.create(2409, 4462, 0), 1), //potentially a spawn from 2010

    BUSH_12(NPCs.BUSH_8105, "located on an island.", Location.create(2534, 3871, 0), 2),
    BARREL_2(NPCs.BARREL_8104, "located near the ghost town.", Location.create(3654, 3491, 0), 2),
    BUSH_13(NPCs.BUSH_8105, "located where bloodsuckers rule.", Location.create(3600, 3487, 0), 2),
    BUSH_14(NPCs.BUSH_8105, "located on islands where brothers quarrel.", Location.create(2355, 3848, 0), 2),
    ROCK_6(NPCs.ROCK_8109, "located on a large crescent island.", Location.create(2118, 3943, 0), 2),
    ROCK_7(NPCs.ROCK_8109, "located on islands where brothers quarrel.", Location.create(2357, 3797, 0), 2),
    ROCK_8(NPCs.ROCK_8109, "located in the Wilderness.", Location.create(3169, 3650, 0), 2),
    BARREL_3(NPCs.BARREL_8104, "located where pirates feel mostly harmless.", Location.create(3738, 3001, 0), 2),
    CACTUS_3(NPCs.CACTUS_8107, "located in the southern desert.", Location.create(3276, 2797, 0), 2),
    TOADSTOOL_3(NPCs.TOADSTOOL_8110, "located near the pointy-eared ones.", Location.create(2314, 3174, 0), 2),
    BUSH_15(NPCs.BUSH_8105, "located deep in the jungle.", Location.create(2938, 2978, 0), 1),
    TOADSTOOL_4(NPCs.TOADSTOOL_8110, "located near the pointy-eared ones.", Location.create(2219, 3227, 0), 2),
    BARREL_4(NPCs.BARREL_8104, "located south of Ardougne.", Location.create(2662, 3152, 0), 1),
    BARREL_5(NPCs.BARREL_8104, "located where monkeys rule.", Location.create(2751, 2700, 0), 2),
    BUSH_16(NPCs.BUSH_8105, "located on islands where brothers quarrel.", Location.create(2353, 3834, 0), 2),
    ROCK_9(NPCs.ROCK_8109, "located near a mountain of wolves.", Location.create(2852, 3504, 0), 1),
    ROCK_10(NPCs.ROCK_8109, "located on islands where brothers quarrel.", Location.create(2413, 3846, 0), 2),

    BUSH_17(NPCs.BUSH_8105, "located near some ogres.", Location.create(2578, 2909, 0), 2),
    CRATE_4(NPCs.CRATE_8108, "located where banana smugglers dwell.", Location.create(2869, 3157, 0), 1),
    CRATE_5(NPCs.CRATE_8108, "located near the island of Dragontooth.", Location.create(3824, 3562, 0), 2),
    ROCK_11(NPCs.ROCK_8109, "located where bloodsuckers rule.", Location.create(3550, 3439, 0), 2),
    TOADSTOOL_5(NPCs.TOADSTOOL_8110, "located near the pointy-eared ones.", Location.create(2181, 3172, 0), 2),
    BUSH_18(NPCs.BUSH_8105, "located where bloodsuckers rule.", Location.create(3472, 3392, 0), 2),
    BUSH_19(NPCs.BUSH_8105, "located near Port Sarim.", Location.create(2989, 3121, 0), 1),
    CRATE_6(NPCs.CRATE_8108, "located where fishers colonise.", Location.create(2322, 3658, 0), 1),
    ROCK_12(NPCs.ROCK_8109, "located between Fremennik and barbarians.", Location.create(2675, 3717, 0), 1),
    ROCK_13(NPCs.ROCK_8109, "located near the pointy-eared ones.", Location.create(2296, 3270, 0), 2),
    CRATE_7(NPCs.CRATE_8108, "located where bloodsuckers rule.", Location.create(3637, 3486, 0), 2),
    TOADSTOOL_6(NPCs.TOADSTOOL_8110, "located where bloodsuckers rule.", Location.create(3416, 3437, 0), 2),
    BUSH_20(NPCs.BUSH_8105, "located where monkeys rule.", Location.create(2802, 2806, 0), 2),

    ROCK_14(NPCs.ROCK_8109, "located near some ogres.", Location.create(2438, 3050, 0), 2),
    CACTUS_4(NPCs.CACTUS_8107, "located in the southern desert.", Location.create(3252, 2963, 0), 1),
    ROCK_15(NPCs.ROCK_8109, "located near some ogres.", Location.create(2340, 3064, 0), 2),
    ROCK_16(NPCs.ROCK_8109, "located in the Wilderness.", Location.create(3108, 3837, 0), 2),
    CACTUS_5(NPCs.CACTUS_8107, "located in the southern desert.", Location.create(3433, 3000, 0), 2),
    CACTUS_6(NPCs.CACTUS_8107, "located in the southern desert.", Location.create(3274, 2813, 0), 2),
    ROCK_17(NPCs.ROCK_8109, "located in the Wilderness.", Location.create(3019, 3866, 0), 2),
    ROCK_18(NPCs.ROCK_8109, "located in the Wilderness.", Location.create(2991, 3824, 0), 2),
    BUSH_21(NPCs.BUSH_8105, "located north of Ardougne.", Location.create(2398, 3361, 0), 1),
    ROCK_19(NPCs.ROCK_8109, "located near the coast.", Location.create(2733, 3283, 0), 1),
    ROCK_20(NPCs.ROCK_8109, "located in the Wilderness.", Location.create(3236, 3927, 0), 2);

    companion object {
        private val locationMap = values().map { it.location.toString() to it }.toMap()

        //these are in the 32-week cycle order
        val onePointPenguins = listOf(BUSH_9, BUSH_21, BARREL_4, BUSH_6, BUSH_2, BUSH_19, CACTUS_2, BUSH_1, CRATE_3, ROCK_19, BUSH_15, ROCK_9, TOADSTOOL_1, ROCK_2, ROCK_5, ROCK_3, BUSH_7, CRATE_4, CRATE_2, CACTUS_1, BUSH_11, BUSH_5, BUSH_3, BUSH_4, ROCK_1, CACTUS_4, BUSH_8, ROCK_4, BUSH_10, ROCK_12, BARREL_1, CRATE_1)
        val twoPointPenguins = listOf(ROCK_15, BUSH_13, ROCK_10, BUSH_17, BUSH_18, BUSH_12, CRATE_6, TOADSTOOL_6, BARREL_5, TOADSTOOL_3, ROCK_16, BUSH_20, TOADSTOOL_5, ROCK_17, BARREL_3, TOADSTOOL_4, ROCK_20, CRATE_5, CACTUS_3, ROCK_8, CACTUS_5, CACTUS_6, ROCK_6, ROCK_18, ROCK_14, ROCK_7, ROCK_11, BARREL_2, BUSH_14, ROCK_13, CRATE_7, BUSH_16)

        fun forLocation(location: Location): Penguin? {
            return locationMap[location.toString()]
        }
    }
}

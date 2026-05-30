package content.global.activity.penguinhns

import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs09.consts.NPCs
import core.game.worldevents.WorldEvents

class PenguinSpawner {

    fun spawnPenguins(amount: Int): ArrayList<Int> {
        var counter = 0
        val availableOrdinals = (0 until Penguin.values().size).toMutableList()
        val spawnedOrdinals = ArrayList<Int>()
        while(counter < amount){
            val peng = Penguin.values()[availableOrdinals.random()]
            availableOrdinals.remove(peng.ordinal)
            spawnedOrdinals.add(peng.ordinal)
            NPC(peng.id,peng.location)
                .also { PenguinManager.npcs.add(it);it.isNeverWalks = true; it.isWalks = false}.init()
            counter++
        }
        return spawnedOrdinals
    }

    fun spawnPenguins(ordinals: List<Int>){
        ordinals.forEach {
            val peng = Penguin.values()[it]
            NPC(peng.id,peng.location)
                .also { PenguinManager.npcs.add(it); it.isNeverWalks = true; it.isWalks = false }.init()
        }
    }
}

enum class Penguin(val id: Int, val hint: String, val location: Location){
    CACTUS_1(NPCs.CACTUS_8107,"located in the northern desert.", Location(3310, 3157, 0)),
    CACTUS_2(NPCs.CACTUS_8107,"located in the southern desert.",Location.create(3259, 3052, 0)),
    BUSH_1(NPCs.BUSH_8105,"located between Fremennik and barbarians.",Location.create(2532, 3588, 0)),
    BUSH_2(NPCs.BUSH_8105,"located where banana smugglers dwell.",Location.create(2740, 3233, 0)),
    BUSH_3(NPCs.BUSH_8105,"located south of Ardougne.",Location.create(2456, 3092, 0)),
    BUSH_4(NPCs.BUSH_8105,"located deep in the jungle.",Location.create(2832, 3053, 0)),
    BUSH_5(NPCs.BUSH_8105,"located where eagles fly.",Location.create(2326, 3516, 0)),
    BUSH_6(NPCs.BUSH_8105,"located south of Ardougne.",Location.create(2513, 3154, 0)),
    BUSH_7(NPCs.BUSH_8105,"located near a big tree surrounded by short people.",Location.create(2387, 3451, 0)),
    BUSH_8(NPCs.BUSH_8105,"located in the kingdom of Asgarnia.",Location.create(2951, 3511, 0)),
    BUSH_9(NPCs.BUSH_8105,"located in the northern desert.",Location.create(3350, 3311, 0)),
    BUSH_10(NPCs.BUSH_8105,"located somewhere in the kingdom of Kandarin.",Location.create(2633, 3501, 0)),
    BUSH_11(NPCs.BUSH_8105,"located where wizards study.",Location.create(3112, 3149, 0)),
    ROCK_1(NPCs.ROCK_8109,"located where the Imperial Guard train.",Location.create(2852, 3578, 0)),
    ROCK_2(NPCs.ROCK_8109,"located in the kingdom of Misthalin.",Location.create(3356, 3416, 0)),
    ROCK_3(NPCs.ROCK_8109,"located near some ogres.",Location.create(2631, 2980, 0)), //potentially incorrect location
    ROCK_4(NPCs.ROCK_8109,"located in the Kingdom of Asgarnia.",Location.create(3013, 3501, 0)),
    ROCK_5(NPCs.ROCK_8109,"located between Fremennik and barbarians.",Location.create(2532, 3630, 0)),
    CRATE_1(NPCs.CRATE_8108,"located in the kingdom of Misthalin.",Location.create(3112, 3332, 0)),
    CRATE_2(NPCs.CRATE_8108,"located in the Kingdom of Misthalin.",Location.create(3305, 3508, 0)),
    CRATE_3(NPCs.CRATE_8108,"located south of Ardougne.",Location.create(2440, 3206, 0)),
    BARREL_1(NPCs.BARREL_8104,"located where no weapons may go.",Location.create(2806, 3383, 0)),
    TOADSTOOL_1(NPCs.TOADSTOOL_8110,"located in the kingdom of Misthalin.",Location.create(3156, 3178, 0)),
    TOADSTOOL_2(NPCs.TOADSTOOL_8110,"located in the fairy realm.",Location.create(2409, 4462, 0)), //potentially a spawn from 2010

    BUSH_12(NPCs.BUSH_8105,"located on an island.",Location.create(2534, 3871, 0)),
    BARREL_2(NPCs.BARREL_8104,"located near the ghost town.",Location.create(3654, 3491, 0)),
    BUSH_13(NPCs.BUSH_8105,"located where bloodsuckers rule.",Location.create(3600, 3487, 0)),
    BUSH_14(NPCs.BUSH_8105,"located on islands where brothers quarrel.",Location.create(2355, 3848, 0)),
    ROCK_6(NPCs.ROCK_8109,"located on a large crescent island.",Location.create(2118, 3943, 0)),
    ROCK_7(NPCs.ROCK_8109,"located on islands where brothers quarrel.",Location.create(2357, 3797, 0)),
    ROCK_8(NPCs.ROCK_8109,"located in the Wilderness.",Location.create(3169, 3650, 0)),
    BARREL_3(NPCs.BARREL_8104,"located where pirates feel mostly harmless.",Location.create(3738, 3001, 0)),
    CACTUS_3(NPCs.CACTUS_8107,"located in the southern desert.",Location.create(3276, 2797, 0)),
    TOADSTOOL_3(NPCs.TOADSTOOL_8110,"located near the pointy-eared ones.",Location.create(2314, 3174, 0)),
    BUSH_15(NPCs.BUSH_8105,"located deep in the jungle.",Location.create(2938, 2978, 0)),
    TOADSTOOL_4(NPCs.TOADSTOOL_8110,"located near the pointy-eared ones.",Location.create(2219, 3227, 0)),
    BARREL_4(NPCs.BARREL_8104,"located south of Ardougne.",Location.create(2662, 3152, 0)),
    //BARREL_5(NPCs.BARREL_8104,"located where monkeys rule.",Location.create(2751, 2700, 0)),
    //currently not well accessible, will need to add when Ape Atoll is sorted.
    //BUSH_16(NPCs.BUSH_8105,"located on islands where brothers quarrel.",Location.create(2353, 3834, 0)),
    //currently not well accessible, will need to add when Neitiznot bridges are fixed.
    ROCK_9(NPCs.ROCK_8109,"located near a mountain of wolves.",Location.create(2852, 3504, 0)),
    ROCK_10(NPCs.ROCK_8109,"located on islands where brothers quarrel.",Location.create(2413, 3846, 0)),

    BUSH_17(NPCs.BUSH_8105,"located near some ogres.",Location.create(2578, 2909, 0)),
    CRATE_4(NPCs.CRATE_8108,"located where banana smugglers dwell.",Location.create(2869, 3157, 0)),
    //CRATE_5(NPCs.CRATE_8108,"located near the island of Dragontooth.",Location.create(3824, 3562, 0)),
    //current not accessible, will need to add when Dragontooth island is sorted.
    ROCK_11(NPCs.ROCK_8109,"located where bloodsuckers rule.",Location.create(3550, 3439, 0)),
    TOADSTOOL_5(NPCs.TOADSTOOL_8110,"located near the pointy-eared ones.",Location.create(2181, 3172, 0)),
    BUSH_18(NPCs.BUSH_8105,"located where bloodsuckers rule.",Location.create(3472, 3392, 0)),
    BUSH_19(NPCs.BUSH_8105,"located near Port Sarim.",Location.create(2989, 3121, 0)),
    CRATE_6(NPCs.CRATE_8108,"located where fishers colonise.",Location.create(2322, 3658, 0)),
    ROCK_12(NPCs.ROCK_8109,"located between Fremennik and barbarians.",Location.create(2675, 3717, 0)),
    ROCK_13(NPCs.ROCK_8109,"located near the pointy-eared ones.",Location.create(2296, 3270, 0)),
    CRATE_7(NPCs.CRATE_8108,"located where bloodsuckers rule.",Location.create(3637, 3486, 0)),
    TOADSTOOL_6(NPCs.TOADSTOOL_8110,"located where bloodsuckers rule.",Location.create(3416, 3437, 0)),
    BUSH_20(NPCs.BUSH_8105,"located where monkeys rule.",Location.create(2802, 2806, 0)),

    ROCK_14(NPCs.ROCK_8109,"located near some ogres.",Location.create(2438, 3050, 0)),
    CACTUS_4(NPCs.CACTUS_8107,"located in the southern desert.",Location.create(3252, 2963, 0)),
    ROCK_15(NPCs.ROCK_8109,"located near some ogres.",Location.create(2340, 3064, 0)),
    ROCK_16(NPCs.ROCK_8109,"located in the Wilderness.",Location.create(3108, 3837, 0)),
    CACTUS_5(NPCs.CACTUS_8107,"located in the southern desert.",Location.create(3433, 3000, 0)),
    CACTUS_6(NPCs.CACTUS_8107,"located in the southern desert.",Location.create(3274, 2813, 0)),
    ROCK_17(NPCs.ROCK_8109,"located in the Wilderness.",Location.create(3019, 3866, 0)),
    ROCK_18(NPCs.ROCK_8109,"located in the Wilderness.",Location.create(2991, 3824, 0)),
    BUSH_21(NPCs.BUSH_8105,"located north of Ardougne.",Location.create(2398, 3361, 0)),
    ROCK_19(NPCs.ROCK_8109,"located near the coast.",Location.create(2733, 3283, 0)),
    ROCK_20(NPCs.ROCK_8109,"located in the Wilderness.",Location.create(3236, 3927, 0));

    companion object {
        private val locationMap = values().map { it.location.toString() to it }.toMap()

        fun forLocation(location: Location): Penguin?{
            return locationMap[location.toString()]
        }
    }
}

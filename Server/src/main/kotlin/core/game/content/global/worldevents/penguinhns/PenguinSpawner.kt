package core.game.content.global.worldevents.penguinhns

import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.content.global.worldevents.WorldEvents
import kotlin.collections.ArrayList

class PenguinSpawner {
    val CACTUS = 8107
    val CRATE  = 8108
    val BARREL = 8104
    val BUSH = 8105
    val ROCK = 8109
    val TOADSTOOL = 8110
    class Penguin(val id: Int, val hint: String, val loc: Location)
    val penguins = arrayListOf<Penguin>(
            Penguin(CACTUS,"...located in the northern desert.",Location.create(3310, 3157, 0)),
            Penguin(BUSH,"...located between Fremennik and barbarians.",Location.create(2532, 3588, 0)),
            Penguin(BUSH,"...located where banana smugglers dwell.",Location.create(2740, 3233, 0)),
            Penguin(BUSH,"...located south of Ardougne.",Location.create(2456, 3092, 0)),
            Penguin(BUSH,"...located deep in the jungle.",Location.create(2832, 3053, 0)),
            Penguin(ROCK,"...located where the Imperial Guard train.",Location.create(2852, 3578, 0)),
            Penguin(ROCK,"...located in the kingdom of Misthalin.",Location.create(3356, 3416, 0)),
            Penguin(CRATE,"...located in the kingdom of Misthalin.",Location.create(3112, 3332, 0)),
            Penguin(BUSH,"...located where eagles fly.",Location.create(2326, 3516, 0)),
            Penguin(BARREL,"...located where no weapons may go.",Location.create(2806, 3383, 0)),
            Penguin(ROCK,"...located near some ogres.",Location.create(2631, 2980, 0)),
            Penguin(BUSH,"...located south of Ardougne.",Location.create(2513, 3154, 0)),
            Penguin(BUSH,"...located near a big tree surrounded by short people.",Location.create(2387, 3451, 0)),
            Penguin(BUSH,"...located in the kingdom of Asgarnia.",Location.create(2951, 3511, 0)),
            Penguin(ROCK,"...located in the Kingdom of Asgarnia.",Location.create(3013, 3501, 0)),
            Penguin(ROCK,"...located between Fremennik and barbarians.",Location.create(2532, 3630, 0)),
            Penguin(CRATE,"...located in the Kingdom of Misthalin.",Location.create(3305, 3508, 0)),
            Penguin(TOADSTOOL,"...located in the kingdom of Misthalin.",Location.create(3156, 3178, 0)),
            Penguin(BUSH,"...located in the northern desert.",Location.create(3350, 3311, 0)),
            Penguin(BUSH,"...located somewhere in the kingdom of Kandarin.",Location.create(2633, 3501, 0)),
            Penguin(BUSH,"...located south of Ardougne.",Location.create(2440, 3206, 0)),
            Penguin(CACTUS,"...located in the southern desert.",Location.create(3259, 3052, 0)),
            Penguin(BUSH,"...located where wizards study.",Location.create(3112, 3149, 0)),
            Penguin(TOADSTOOL,"...located in the fairy realm.",Location.create(2409, 4462, 0))
    )

    fun spawnPenguins(amount: Int): ArrayList<Penguin> {
        var event = WorldEvents.get("penguin-hns") as PenguinHNSEvent
        var counter = 0
        val list = penguins.toMutableList()
        val penguinList = ArrayList<Penguin>()
        while(counter < amount){
            val peng = list.random()
            penguinList.add(peng).also { NPC(peng.id,peng.loc).also {PenguinManager.npcs.add(it);it.isNeverWalks = true; it.isWalks = false}.init() }
            list.remove(peng)
            counter++
        }
        return penguinList
    }
}
package rs09.game.content.global.worldevents.holiday.easter

import core.game.node.entity.npc.NPC
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.global.worldevents.WorldEvent
import rs09.game.system.config.GroundSpawnLoader
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import rs09.tools.secondsToTicks
import java.util.*

class EasterEvent : WorldEvent("easter") {
    val LUMBRIDGE_SPOTS = arrayOf(Location.create(3190, 3240, 0),
        Location.create(3219, 3204, 0),Location.create(3212, 3201, 0),Location.create(3205, 3209, 0),
        Location.create(3205, 3217, 0),Location.create(3211, 3213, 0),Location.create(3206, 3229, 0),
        Location.create(3212, 3226, 0),Location.create(3212, 3244, 0),Location.create(3202, 3252, 0),
        Location.create(3197, 3220, 0),Location.create(3189, 3207, 0),Location.create(3229, 3200, 0),
        Location.create(3228, 3205, 0),Location.create(3251, 3212, 0),Location.create(3232, 3237, 0))

    val DRAYNOR_SPOTS = arrayOf(Location.create(3085, 3242, 0),Location.create(3083, 3236, 0),Location.create(3093, 3224, 0),
        Location.create(3099, 3241, 0),Location.create(3095, 3255, 0),Location.create(3089, 3264, 0),Location.create(3089, 3265, 0),
        Location.create(3088, 3268, 0),Location.create(3090, 3274, 0),Location.create(3100, 3281, 0),Location.create(3116, 3265, 0),
        Location.create(3123, 3272, 0),Location.create(3079, 3261, 0))

    val FALADOR_SPOTS = arrayOf(
        Location.create(2961, 3332, 0),Location.create(2967, 3336, 0),Location.create(2974, 3329, 0),
        Location.create(2979, 3346, 0),Location.create(2970, 3348, 0),Location.create(2955, 3375, 0),Location.create(2942, 3386, 0),
        Location.create(2937, 3385, 0),Location.create(3005, 3370, 0),Location.create(3006, 3383, 0),Location.create(3007, 3387, 0),
        Location.create(2985, 3391, 0),Location.create(2984, 3381, 0),Location.create(2980, 3384, 0),Location.create(3025, 3345, 0),
        Location.create(3060, 3389, 0),Location.create(3052, 3385, 0))

    val EDGEVILLE_SPOTS = arrayOf(Location.create(3077, 3487, 0),Location.create(3082, 3487, 0),
        Location.create(3089, 3481, 0),Location.create(3084, 3479, 0),Location.create(3108, 3499, 0),
        Location.create(3110, 3517, 0),Location.create(3091, 3512, 0),Location.create(3092, 3507, 0),
        Location.create(3081, 3513, 0),Location.create(3079, 3513, 1),Location.create(3080, 3508, 1),
        Location.create(3108, 3499, 0),Location.create(3093, 3467, 0))

    val TREE_GNOME_STRONGHOLD_SPOTS = arrayOf(
        Location.create(2480, 3507, 0),Location.create(2486, 3513, 0),Location.create(2453, 3512, 0),
        Location.create(2442, 3484, 0),Location.create(2438, 3486, 0),Location.create(2441, 3506, 0),
        Location.create(2471, 3482, 0),Location.create(2482, 3478, 0),Location.create(2480, 3469, 0),
        Location.create(2489, 3440, 0),Location.create(2470, 3417, 0),Location.create(2478, 3402, 0),
        Location.create(2486, 3407, 0),Location.create(2492, 3404, 0),Location.create(2493, 3413, 0),
        Location.create(2446, 3395, 0),Location.create(2422, 3398, 0),Location.create(2421, 3402, 0),Location.create(2418, 3398, 0),
        Location.create(2401, 3415, 0),Location.create(2397, 3436, 0),Location.create(2409, 3448, 0),Location.create(2482, 3391, 0))

    val locations = arrayOf("Lumbridge","Draynor Village","Falador","Edgeville","Tree Gnome Stronghold")

    val spawnedItems = ArrayList<GroundItem>()
    val eggs = arrayOf(Items.EASTER_EGG_11027,Items.EASTER_EGG_11028,Items.EASTER_EGG_11029,Items.EASTER_EGG_11030)
    var recentLoc = ""

    override fun checkActive(): Boolean {
        val isApril = Calendar.getInstance().get(Calendar.MONTH) == Calendar.APRIL
        val isBefore9th = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 9
        return false
    }

    override fun initialize() {
        super.initialize()
        val easterBunny = NPC(NPCs.EASTER_BUNNY_7197)
        easterBunny.isNeverWalks = true
        easterBunny.direction = Direction.WEST
        easterBunny.location = Location.create(3225, 3212, 0)
        for(i in 0 until 5){
            val bunny = NPC(NPCs.EASTER_BUNNY_3688)
            bunny.location = Location.create(3223, 3213, 0)
            bunny.isWalks = true
            bunny.walkRadius = 4
            bunny.init()
        }
        easterBunny.init()
        GameWorld.settings?.message_model = 715
        GameWorld.settings?.message_string = "Happy Easter!"
        GameWorld.Pulser.submit(object : Pulse(){
            override fun pulse(): Boolean {
                if(delay == 1){
                    delay = secondsToTicks(if(GameWorld.settings?.isDevMode == true) 60 else 3600)
                }
                for(item in spawnedItems){
                    GroundItemManager.destroy(item)
                }
                spawnedItems.clear()
                val locName = locations.random()
                val locs = when(locName){
                    "Lumbridge" -> LUMBRIDGE_SPOTS
                    "Draynor Village" -> DRAYNOR_SPOTS
                    "Falador" -> FALADOR_SPOTS
                    "Edgeville" -> EDGEVILLE_SPOTS
                    "Tree Gnome Stronghold" -> TREE_GNOME_STRONGHOLD_SPOTS
                    else -> LUMBRIDGE_SPOTS
                }.toMutableList()

                Repository.sendNews("A bunch of eggs have been spotted in $locName!")
                recentLoc = locName
                val itemLocs = ArrayList<Location>()
                for(i in 0 until locs.size / 2){
                    val loc = locs.random()
                    locs.remove(loc)
                    itemLocs.add(loc)
                }

                for(i in itemLocs){
                    spawnedItems.add(GroundSpawnLoader.GroundSpawn(-1, Item(eggs.random()),i).init())
                }
                return false
            }
        })
    }
}
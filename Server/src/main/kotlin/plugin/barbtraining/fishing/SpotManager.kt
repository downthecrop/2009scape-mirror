package plugin.barbtraining.fishing

import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import core.plugin.CorePluginTypes.ManagerPlugin
import core.plugin.CorePluginTypes.Managers
import plugin.barbtraining.fishing.SpotManager.Companion.locations
import plugin.barbtraining.fishing.SpotManager.Companion.usedLocations

@Initializable
/**
 * Manages fishing spot spawning and relocation
 * @author Ceikry
 */
class SpotManager : ManagerPlugin() {
    var ticks = 0
    val spots = ArrayList<BarbFishingSpot>()

    companion object{
        val usedLocations = arrayListOf<Location>()
        val locations = listOf(
                Location.create(2506, 3494, 0),
                Location.create(2504, 3497, 0),
                Location.create(2504, 3497, 0),
                Location.create(2500, 3506, 0),
                Location.create(2500, 3509, 0),
                Location.create(2500, 3512, 0),
                Location.create(2504, 3516, 0)
        )
    }

    override fun tick() {
        if(ticks % 50 == 0){
            usedLocations.clear()
            for(spot in spots) usedLocations.add(spot.loc ?: Location(0,0,0))
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        for(i in 0 until 5){
            spots.add(BarbFishingSpot(getNewLoc(), getNewTTL()).also { it.init() })
        }
        Managers.register(this)
        return this
    }
}

fun getNewTTL(): Int{
    return RandomFunction.random(400,2000)
}

fun getNewLoc(): Location {
    var loc = locations.random()
    while(usedLocations.contains(loc)) loc = locations.random()
    usedLocations.add(loc)
    return loc
}

class BarbFishingSpot(var loc: Location? = null, var ttl: Int) : NPC(1176){
    init {
        location = loc
    }
    val locations = listOf(
            Location.create(2506, 3494, 0),
            Location.create(2504, 3497, 0),
            Location.create(2504, 3497, 0),
            Location.create(2500, 3506, 0),
            Location.create(2500, 3509, 0),
            Location.create(2500, 3512, 0),
            Location.create(2504, 3516, 0)
    )
    override fun handleTickActions() {
        if(location != loc) properties.teleportLocation = loc.also { ttl = getNewTTL() }
        if(ttl-- <= 0){
            loc = getNewLoc()
        }
    }
}


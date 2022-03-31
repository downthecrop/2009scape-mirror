package rs09.game.node.entity.skill.gather.fishing.barbfishing

import api.StartupListener
import api.TickListener
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.plugin.CorePluginTypes.ManagerPlugin
import core.plugin.CorePluginTypes.Managers
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import rs09.game.node.entity.skill.gather.fishing.barbfishing.SpotManager.Companion.locations
import rs09.game.node.entity.skill.gather.fishing.barbfishing.SpotManager.Companion.usedLocations

/**
 * Manages fishing spot spawning and relocation
 * @author Ceikry
 */
class SpotManager : TickListener, StartupListener {
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

    override fun startup() {
        for(i in 0 until 5){
            spots.add(BarbFishingSpot(getNewLoc(), getNewTTL()).also { it.init() })
        }
    }
}

fun getNewTTL(): Int{
    return RandomFunction.random(400,2000)
}

fun getNewLoc(): Location {
    val possibleLoc = ArrayList<Location>()
    for(loc in locations) if(usedLocations.contains(loc)) continue else possibleLoc.add(loc)
    val loc = possibleLoc.random()
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
            usedLocations.remove(location)
            loc = getNewLoc()
        }
    }
}

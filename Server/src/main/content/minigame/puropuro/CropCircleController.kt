package content.minigame.puropuro

import core.api.*
import core.game.interaction.*
import core.game.world.map.Location
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType

class CropCircleController : TickListener, InteractionListener, LoginListener {
    override fun tick () {
        if (getWorldTicks() < nextCircle)
            return
        deconstructOldCircle()
        
        val (name, loc) = possibleLocations.random()
        constructCircle(loc)

        sendNews("A crop circle has appeared near $name.")
        nextCircle = getWorldTicks() + 1500
        currentLocName = name
    }

    override fun defineListeners() {
        on (center, IntType.SCENERY, "enter") {player, _ -> 
            if (hasImpBox(player)) {
                sendDialogue (player, "Something prevents you from entering. You think the portal is offended by your imp box. They are not popular on imp and impling planes.")
                return@on true
            }
            teleport (player, puroLocation, TeleportType.PURO_PURO)   
            setAttribute (player, exitLocation, Location.create(player.location))
            return@on true
        }

        on (puroExit, IntType.SCENERY, "leave", "quick-leave") {player, _ -> 
            var exit = getAttribute (player, exitLocation, Location.create(3158, 3300, 0))
            teleport (player, exit, TeleportType.PURO_PURO)
            return@on true
        }
    }

    override fun login (player: Player) {
        sendMessage (player, "A crop circle is active near $currentLocName.")
    }

    private fun constructCircle (location: Location) {
        activeObjects.add (
            addScenery (
                center, 
                location, 
                rotation = 0, 
                type = 10
            )
        )
        for ((index, tile) in location.surroundingTiles.withIndex()) {
            activeObjects.add (
                addScenery (
                    surrounding[index % 4], 
                    tile, 
                    rotation = (index / 4) * 2, 
                    type = 10
                )
            )
        }
    }

    private fun deconstructOldCircle() {
        for (scenery in activeObjects)
            removeScenery(scenery)
        activeObjects.clear()
    }

    private fun hasImpBox (player: Player) : Boolean {
        return inInventory(player, 10025) || inInventory(player, 10027) || inInventory(player, 10028)
    }

    companion object {
        var currentLocName = ""
        val exitLocation = "/save:puro-exit"
        val possibleLocations = arrayOf (
            Pair ("Doric's Hut",        Location.create(2953, 3444, 0)),
            Pair ("Yanille",            Location.create(2583, 3104, 0)),
            Pair ("Draynor",            Location.create(3113, 3274, 0)),
            Pair ("Rimmington",         Location.create(2978, 3216, 0)),
            Pair ("The Grand Exchange", Location.create(3141, 3461, 0)),
            Pair ("Northern Lumbridge", Location.create(3160, 3296, 0)),
            Pair ("Southern Varrock",   Location.create(3218, 3348, 0)),
            Pair ("Northern Ardougne",  Location.create(2644, 3350, 0))
        )
        val activeObjects = ArrayList<Scenery>()
        val surrounding = arrayOf (24984, 24985, 24986, 24987)
        val center = 24991
        val puroExit = 25014
        val puroLocation = Location.create(2591, 4320, 0) 
        var nextCircle = 0
    }
}

package rs09.game.interaction.region

import core.game.world.map.Location
import rs09.game.interaction.InteractionListener

/**
 * File to be used for anything Isafdar related.
 * Handles the summoning/altar cave enter and exit in Isafdar.
 * @author Sir Kermit
 */

class IsafdarListeners : InteractionListener() {

    val CAVE_ENTRANCE = 4006
    val CAVE_EXIT = 4007
    val outside = Location.create(2312, 3217, 0)
    val inside = Location.create(2314, 9624, 0)

    override fun defineListeners() {
        on(CAVE_ENTRANCE,OBJECT,"enter"){player,node ->
            player.teleport(inside)
            return@on true
        }

        on(CAVE_EXIT,OBJECT,"exit"){player,node ->
            player.teleport(outside)
            return@on true
        }
    }
}
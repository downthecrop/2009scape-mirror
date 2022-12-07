package rs09.game.interaction.region.dungeons.edgeville

import api.sendDialogue
import api.teleport
import core.game.world.map.Location
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

class PosterListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.POSTER_29586, IntType.SCENERY, "pull-back") {player, _ ->
            sendDialogue(player, "There appears to be a tunnel behind this poster.")
            teleport(player, Location.create(3140, 4230, 2))
            return@on true
        }
    }
}
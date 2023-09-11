package content.region.misc.keldagrim.handlers

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.Scenery

class KeldagrimListeners  : InteractionListener {
    override fun defineListeners() {
        on(Scenery.DOORWAY_23286, IntType.SCENERY, "enter") { player, _ ->
            player.properties.teleportLocation = Location.create(2941, 10179, 0)
            return@on true
        }
        on(Scenery.DOORWAY_23287, IntType.SCENERY, "enter") { player, _ ->
            player.properties.teleportLocation = Location.create(2435, 5535, 0)
            return@on true
        }
    }
}
package content.region.desert.ullek.handlers

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.Scenery

class UllekListeners : InteractionListener {
    override fun defineListeners() {
        on(Scenery.STAIRS_DOWN_28481, IntType.SCENERY, "enter") { player, _ ->
            player.properties.teleportLocation = Location.create(3448, 9252, 1)
            return@on true
        }
        on(Scenery.EXIT_28401, IntType.SCENERY, "leave through") { player, _ ->
            player.properties.teleportLocation = Location.create(3412, 2848, 1)
            return@on true
        }
        on(Scenery.FALLEN_PILLAR_28516, IntType.SCENERY, "climb") { player, _ ->
            player.properties.teleportLocation = Location.create(3419, 2801, 0)
            return@on true
        }
        on(Scenery.FALLEN_PILLAR_28515, IntType.SCENERY, "climb") { player, _ ->
            player.properties.teleportLocation = Location.create(3419, 2803, 1)
            return@on true
        }
    }
}
package content.region.asgarnia.trollweiss.handlers

import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.world.map.Location
import org.rs09.consts.Scenery

class TrollweissListeners : InteractionListener {
    override fun defineListeners() {
        // Keldagrim side tunnel to trollweiss mountain
        on(Scenery.TUNNEL_5012, IntType.SCENERY, "enter") { player, _ ->
            player.properties.teleportLocation = Location.create(2799, 10134, 0)
            return@on true
        }
        on(Scenery.TUNNEL_5013, IntType.SCENERY, "enter") { player, _ ->
            player.properties.teleportLocation = Location.create(2796, 3719, 0)
            return@on true
        }
        // Trollheim north to trollweiss dungeon
        on(Scenery.CAVE_ENTRANCE_5007, IntType.SCENERY, "enter") { player, _ ->
            player.properties.teleportLocation = Location.create(2803, 10187, 0)
            return@on true
        }
        on(Scenery.CAVE_EXIT_32743, IntType.SCENERY, "exit") { player, _ ->
            player.properties.teleportLocation = Location.create(2822, 3744, 0)
            return@on true
        }
        // Keldagrim side tunnel to trollweiss mountain
        on(Scenery.CREVASSE_33185, IntType.SCENERY, "enter") { player, _ ->
            player.properties.teleportLocation = Location.create(2778, 3869, 0)
            return@on true
        }
        on(Scenery.TUNNEL_5009, IntType.SCENERY, "enter") { player, _ ->
            player.properties.teleportLocation = Location.create(2772, 10232, 0)
            return@on true
        }
    }
}
package rs09.game.interaction.`object`

import api.ContentAPI
import core.game.world.map.Location
import rs09.game.interaction.InteractionListener

class WizardGuildPortals : InteractionListener() {

    val WTOWER_PORTAL = 2156
    val DWTOWER_PORTAL = 2157
    val SORC_TOWER_PORTAL = 2158

    override fun defineListeners() {
        on(WTOWER_PORTAL, SCENERY, "enter"){player, _ ->
            ContentAPI.teleport(player, Location.create(3114, 3171, 0))
            return@on true
        }

        on(DWTOWER_PORTAL, SCENERY, "enter"){player, _ ->
            ContentAPI.teleport(player, Location.create(2916, 3335, 0))
            return@on true
        }

        on(SORC_TOWER_PORTAL, SCENERY, "enter"){player, _ ->
            ContentAPI.teleport(player, Location.create(2701, 3395, 0))
            return@on true
        }
    }
}
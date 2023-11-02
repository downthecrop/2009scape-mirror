package content.region.misthalin.quest.asoulsbane

import core.api.*
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.Scenery

// Temporary access since the monsters in there drop nothing.
class ASoulsBaneListener : InteractionListener {

    companion object {
        private val RIFT_IDS = intArrayOf(13967, 13968, 13969, 13970, 13971, 13972, 13973, 13974, 13975, 13976, 13977, 13978, 13979, 13980, 13981, 13982, 13983, 13984, 13985, 13986, 13987, 13988, 13989, 13990, 13991, 13992, 13993)
    }
    override fun defineListeners() {
        on(RIFT_IDS, SCENERY, "enter") { player, _ ->
            if (hasRequirement(player, "A Soul's Bane")) {
                teleport(player, Location(3297, 9824, 0))
            }
            return@on true
        }
        on(Scenery.ROPE_13999, SCENERY, "climb-up") { player, _ ->
            teleport(player, Location(3309, 3452, 0))
            return@on true
        }
    }
}

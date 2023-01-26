package content.region.misthalin.lumbridge.handlers

import core.api.*
import org.rs09.consts.Scenery
import core.GlobalStats
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

/**
 * @author bushtail
 */

class CowFieldSignListener : InteractionListener {

    val SIGN = Scenery.SIGNPOST_31297

    override fun defineListeners() {
        on(SIGN, IntType.SCENERY, "read") { player, _ ->
            val COW_DEATHS = GlobalStats.getDailyCowDeaths()
            if(COW_DEATHS > 0) {
                sendDialogue(player, "Local cowherders have reported that $COW_DEATHS cows have been slain in this field today by passing adventurers. Farmers throughout the land fear this may be an epidemic.")
            } else {
                sendDialogue(player,"The Lumbridge cow population has been thriving today, without a single cow death to worry about!")
            }
            return@on true
        }
    }
}
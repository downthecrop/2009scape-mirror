package content.region.misthalin.lumbridge.handlers

import core.api.*
import org.rs09.consts.Scenery
import core.GlobalStats
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class ChurchSignListener : InteractionListener {

    val CHURCH_SIGN = Scenery.SIGNPOST_31299

    override fun defineListeners() {
        on(CHURCH_SIGN, IntType.SCENERY, "read"){ player, _ ->
            val deaths = GlobalStats.getDailyDeaths()
            if(deaths > 0) {
                sendDialogue(player, "So far today $deaths unlucky adventurers have died on RuneScape and been sent to their respawn location. Be careful out there.")
            } else {
                sendDialogue(player, "So far today not a single adventurer on RuneScape has met their end grisly or otherwise. Either the streets are getting safer or adventurers are getting warier.")
            }
            return@on true
        }
    }
}
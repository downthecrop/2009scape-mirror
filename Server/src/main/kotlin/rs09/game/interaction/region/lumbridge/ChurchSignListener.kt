package rs09.game.interaction.region.lumbridge

import api.*
import org.rs09.consts.Scenery
import rs09.GlobalStats
import rs09.game.interaction.InteractionListener

class ChurchSignListener : InteractionListener() {

    val CHURCH_SIGN = Scenery.SIGNPOST_31299

    override fun defineListeners() {
        on(CHURCH_SIGN, SCENERY, "read"){player, _ ->
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
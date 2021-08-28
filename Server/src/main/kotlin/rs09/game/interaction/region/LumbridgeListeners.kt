package rs09.game.interaction.region

import api.ContentAPI
import rs09.GlobalStats
import rs09.game.interaction.InteractionListener

class LumbridgeListeners : InteractionListener() {

    val CHURCH_SIGN = 31299

    override fun defineListeners() {
        on(CHURCH_SIGN, SCENERY, "read"){player, _ ->
            val deaths = GlobalStats.getDailyDeaths()
            if(deaths > 0){
                ContentAPI.sendDialogue(player, "So far today $deaths unlucky adventurers have died on RuneScape and been sent to their respawn location. Be careful out there.")
            } else {
                ContentAPI.sendDialogue(player, "So far today not a single adventurer on RuneScape has met their end grisly or otherwise. Either the streets are getting safer or adventurers are getting warier.")
            }
            return@on true
        }
    }

}
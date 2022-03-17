package rs09.game.interaction.region.lumbridge

import api.sendDialogue
import api.sendMessage
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener

/**
 * @author bushtail
 */

class GnomeCopterSignListener : InteractionListener() {
    val SIGN = Scenery.ADVERTISEMENT_30037
    override fun defineListeners() {
        on(SIGN, SCENERY, "read") { player, _ ->
            sendDialogue(player, "Come check our gnome copters up north! Disclaimer: EXTREMELY WIP")
            return@on true
        }
    }
}
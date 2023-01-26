package content.region.misthalin.lumbridge.handlers

import core.api.sendDialogue
import org.rs09.consts.Scenery
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

/**
 * @author bushtail
 */

class GnomeCopterSignListener : InteractionListener {
    val SIGN = Scenery.ADVERTISEMENT_30037
    override fun defineListeners() {
        on(SIGN, IntType.SCENERY, "read") { player, _ ->
            sendDialogue(player, "Come check our gnome copters up north! Disclaimer: EXTREMELY WIP")
            return@on true
        }
    }
}
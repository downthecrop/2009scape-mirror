package rs09.game.interaction.`object`

import api.sendItemDialogue
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * @author bushtail
 */

class CadavaPotionListener : InteractionListener {

    var POTION = Items.CADAVA_POTION_756

    override fun defineListeners() {
        on(POTION, IntType.ITEM, "drink") { player, node ->
            sendItemDialogue(player, POTION, "You dare not drink.")
            return@on true
        }
        on(POTION, IntType.ITEM, "look-at") { player, node ->
            sendItemDialogue(player, POTION, "This looks very colourful.")
            return@on true
        }
    }
}
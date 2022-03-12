package rs09.game.interaction.`object`

import api.sendItemDialogue
import core.plugin.Initializable
import rs09.game.interaction.InteractionListener

/**
 * @author bushtail
 */
@Initializable
class CavadaPotionListener : InteractionListener() {

    var POTION = 756

    override fun defineListeners() {
        on(POTION, ITEM, "drink") { player, node ->
            sendItemDialogue(player, POTION, "You dare not drink.")
            return@on true
        }
        on(POTION, ITEM, "look-at") { player, node ->
            sendItemDialogue(player, POTION, "This looks very colourful.")
            return@on true
        }
    }
}
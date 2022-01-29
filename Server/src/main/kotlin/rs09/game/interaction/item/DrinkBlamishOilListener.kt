package rs09.game.interaction.item

import api.*
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class DrinkBlamishOilListener : InteractionListener() {

    override fun defineListeners() {
        on(Items.BLAMISH_OIL_1582, ITEM, "drink"){player, _ ->
            sendPlayerDialogue(player, "You know... I'd really rather not.")
            return@on true
        }
    }
}
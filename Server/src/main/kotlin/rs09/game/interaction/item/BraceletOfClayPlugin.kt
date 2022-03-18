package rs09.game.interaction.item

import api.*
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

/**
 * Handles the bracelet of clay operate option.
 * @author Ceikry
 */
class BraceletOfClayPlugin : InteractionListener() {

    override fun defineListeners() {

        on(Items.BRACELET_OF_CLAY_11074, ITEM, "operate"){ player, node ->
            var charge = getCharge(node)
            if (charge > 28) setCharge(node, 28).also { charge = 28 }
            sendMessage(player, "You have $charge uses left.")
            return@on true
        }

    }
}
package rs09.game.interaction.item

import api.*
import rs09.game.interaction.InteractionListener

/**
 * Handles the bracelet of clay operate option.
 * @author Ceikry
 */
class BraceletOfClayPlugin : InteractionListener() {

    val BRACELET = 11074

    override fun defineListeners() {

        on(BRACELET,ITEM,"operate"){player,node ->
            var charge = getCharge(node)
            if (charge > 28) setCharge(node, 28).also { charge = 28 }
            player.sendMessage("You have $charge uses left.")
            return@on true
        }

    }
}
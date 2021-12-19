package rs09.game.interaction.item

import api.*
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class LegendsCapeLock : InteractionListener() {
    override fun defineListeners() {
        onEquip(Items.CAPE_OF_LEGENDS_1052){player, node ->
            val points = getQP(player)
            if(points < 40) sendDialogue(player, "You need 40 QP to equip that.")
            return@onEquip points >= 40
        }
    }
}
package rs09.game.interaction.item

import api.ContentAPI
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class LegendsCapeLock : InteractionListener() {
    override fun defineListeners() {
        onEquip(Items.CAPE_OF_LEGENDS_1052){player, node ->
            val points = ContentAPI.getQP(player)
            if(points < 40) ContentAPI.sendDialogue(player, "You need 40 QP to equip that.")
            return@onEquip points >= 40
        }
    }
}
package content.global.handlers.item

import core.api.getAttribute
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class RingForgingOperateListener: InteractionListener {
    override fun defineListeners() {

        on(Items.RING_OF_FORGING_2568, IntType.ITEM, "operate") { player, node ->

            //Attribute set at SmeltingPulse.java
            val charges = getAttribute(player, "ringOfForgingCharges", 140)

            sendMessage(player, "You can smelt $charges more iron ore before the ring disintegrates.")

            return@on true
        }
    }
}
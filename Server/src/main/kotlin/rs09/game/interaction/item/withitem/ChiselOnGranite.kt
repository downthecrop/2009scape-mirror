package rs09.game.interaction.item.withitem

import api.*
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class ChiselOnGranite : InteractionListener() {
    override fun defineListeners() {
        val granite = intArrayOf(Items.GRANITE_5KG_6983, Items.GRANITE_2KG_6981)

        onUseWith(ITEM, granite, Items.CHISEL_1755) {player, used, _ ->
            if (freeSlots(player) < 3) {
                sendMessage(player, "You need four inventory slots to do this.")
            } else {
                if(removeItem(player, used, Container.INVENTORY)) {
                    when (used.id) {
                        Items.GRANITE_5KG_6983 -> {
                            sendMessage(player, "You chisel the 5kg granite into four smaller pieces.")
                            addItemOrDrop(player, Items.GRANITE_2KG_6981, 2)
                            addItemOrDrop(player, Items.GRANITE_500G_6979, 2)
                        }

                        Items.GRANITE_2KG_6981 -> {
                            sendMessage(player, "You chisel the 2kg granite into four smaller pieces.")
                            addItemOrDrop(player, Items.GRANITE_500G_6979, 4)
                        }
                    }
                }
            }
            return@onUseWith true
        }
    }
}
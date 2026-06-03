package content.global.skill.cooking

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class WatermelonSliceListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.WATERMELON_5982, Items.KNIFE_946) { player, used, _ ->
            if(removeItem(player, used.asItem())) {
                addItemOrDrop(player, Items.WATERMELON_SLICE_5984, 3)
                sendMessage(player, "You slice the watermelon into three slices.")
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
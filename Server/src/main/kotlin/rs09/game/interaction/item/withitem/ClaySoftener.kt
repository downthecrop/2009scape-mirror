package rs09.game.interaction.item.withitem

import api.addItem
import api.removeItem
import api.runTask
import api.sendMessage
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class ClaySoftener : InteractionListener() {
    override fun defineListeners() {
        val sources = intArrayOf(Items.JUG_OF_WATER_1937, Items.BUCKET_OF_WATER_1929, Items.BOWL_OF_WATER_1921)

        onUseWith(ITEM, sources, Items.CLAY_434){player, used, with ->
            val returnItem = when(used.id){
                Items.JUG_OF_WATER_1937 -> Items.EMPTY_JUG_3732
                Items.BUCKET_OF_WATER_1929 -> Items.EMPTY_BUCKET_3727
                else -> Items.BOWL_1923
            }

            runTask(player, 2){
                if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())){
                    addItem(player, Items.SOFT_CLAY_1761)
                    addItem(player, returnItem)
                    sendMessage(player, "You mix the clay and water. You now have some soft, workable clay.")
                }
            }
            return@onUseWith true
        }
    }
}
package rs09.game.interaction.item.withitem

import api.addItem
import api.removeItem
import api.sendDialogue
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class SkullSceptreCombiner : InteractionListener() {
    override fun defineListeners() {
        onUseWith(ITEM, Items.LEFT_SKULL_HALF_9008, Items.RIGHT_SKULL_HALF_9007) {player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())){
                addItem(player, Items.STRANGE_SKULL_9009)
                sendDialogue(player, "The two halves of the skull fit perfectly.")
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.BOTTOM_OF_SCEPTRE_9011, Items.TOP_OF_SCEPTRE_9010) {player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())){
                addItem(player, Items.RUNED_SCEPTRE_9012)
                sendDialogue(player, "The two halves of the sceptre fit perfectly.")
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.RUNED_SCEPTRE_9012, Items.STRANGE_SKULL_9009) {player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())){
                addItem(player, Items.SKULL_SCEPTRE_9013)
                sendDialogue(player,"The skull fits perfectly on top of the sceptre.")
            }
            return@onUseWith true
        }
    }
}
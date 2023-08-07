package content.region.desert.quest.shadowofthestorm

import core.api.addItem
import core.api.hasRequirement
import core.api.removeItem
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class DarklightListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.BLACK_MUSHROOM_INK_4622, Items.SILVERLIGHT_2402) { player, used, with ->
            if (!hasRequirement(player, "Shadow of the Storm") || (!player.inventory.contains(Items.BLACK_MUSHROOM_INK_4622, 1) && (!player.inventory.contains(Items.SILVERLIGHT_2402, 1))))
                return@onUseWith false
            if (removeItem(player, used.id) && removeItem(player, with.id))
                addItem(player, Items.DARKLIGHT_6746)
            return@onUseWith true
        }
    }
}

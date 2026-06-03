package content.global.skill.cooking

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

class CoconutListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.COCONUT_5974, Items.HAMMER_2347) { player, used, _ ->
            if(removeItem(player, used.id)) {
                addItem(player, Items.COCONUT_5976)
                sendMessage(player, "You crush the coconut with a hammer.")
                return@onUseWith true
            }
            return@onUseWith false
        }
        onUseWith(IntType.ITEM, Items.COCONUT_5976, Items.VIAL_229) { player, used, with ->
            if(removeItem(player, used.id) && removeItem(player, with.id)) {
                addItem(player, Items.COCONUT_SHELL_5978)
                addItem(player, Items.COCONUT_MILK_5935)
                sendMessage(player, "You overturn the coconut into a vial.")
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
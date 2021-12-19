package rs09.game.interaction.item.withitem

import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import api.*

class CombineCrystalKey : InteractionListener() {
    override fun defineListeners() {
        onUseWith(ITEM, Items.LOOP_HALF_OF_A_KEY_987, Items.TOOTH_HALF_OF_A_KEY_985){player, used, with ->
            if(removeItem(player, used, Container.INVENTORY) && removeItem(player,with,Container.INVENTORY)) {
                addItem(player, Items.CRYSTAL_KEY_989)
                sendMessage(player, "You join the loop half of a key and the tooth half of a key to make a crystal key.")
            }
            return@onUseWith true
        }
    }
}
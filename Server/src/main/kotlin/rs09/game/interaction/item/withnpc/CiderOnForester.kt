package rs09.game.interaction.item.withnpc

import api.inBorders
import api.openDialogue
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class CiderOnForester : InteractionListener() {
    override fun defineListeners() {
        val ids = intArrayOf(1,2,3,4,5)

        onUseWith(NPC, Items.CIDER_5763, *ids){player, used, with ->
            if(inBorders(player, 2689, 3488, 2700, 3498)){
                openDialogue(player, with.id, with.asNpc(), used.asItem())
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
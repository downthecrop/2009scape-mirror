package rs09.game.interaction.item.withnpc

import api.openDialogue
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class PoisonChaliceOnKingArthur : InteractionListener() {
    override fun defineListeners() {
        onUseWith(NPC, Items.POISON_CHALICE_197, NPCs.KING_ARTHUR_251){player, used, with ->
            openDialogue(player, with.id, with.asNpc(), used.asItem())
            return@onUseWith true
        }
    }
}
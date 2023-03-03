package content.region.asgarnia.portsarim.handlers

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC

class SeamanListener : InteractionListener {
    override fun defineListeners() {
        on(IntType.NPC, "pay-fare") { player, node ->
            val npc = node as NPC
            player.dialogueInterpreter.open(npc.id, npc, true)
            return@on true
        }
    }
}
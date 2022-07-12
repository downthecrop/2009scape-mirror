package rs09.game.content.dialogue.region.bountyhunter

import api.openBankAccount
import api.openGrandExchangeCollectionBox
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class MaximillianSackvilleListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.BANKER_6538, NPC, "bank") { player, _ ->
            openBankAccount(player)
            return@on true
        }

        on(NPCs.BANKER_6538, NPC, "collect") { player, _ ->
            openGrandExchangeCollectionBox(player)
            return@on true
        }
    }
}
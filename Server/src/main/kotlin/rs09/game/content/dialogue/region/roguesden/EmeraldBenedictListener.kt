package rs09.game.content.dialogue.region.roguesden

import api.openBankAccount
import api.openGrandExchangeCollectionBox
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * Handles Emerald Benedict's only available interaction.
 *
 * @author vddCore
 */
class EmeraldBenedictListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.EMERALD_BENEDICT_2271, IntType.NPC, "bank") { player, _ ->
            openBankAccount(player)
            return@on true
        }

        on(NPCs.EMERALD_BENEDICT_2271, IntType.NPC, "collect") { player, _ ->
            openGrandExchangeCollectionBox(player)
            return@on true
        }
    }
}
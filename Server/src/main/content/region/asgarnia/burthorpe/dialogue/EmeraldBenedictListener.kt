package content.region.asgarnia.burthorpe.dialogue

import core.api.openBankAccount
import core.api.openGrandExchangeCollectionBox
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

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
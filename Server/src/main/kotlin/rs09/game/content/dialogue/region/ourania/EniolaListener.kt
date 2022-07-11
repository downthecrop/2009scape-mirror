package rs09.game.content.dialogue.region.ourania

import api.openInterface
import api.setAttribute
import org.rs09.consts.Components
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

/**
 * Handles special banking interactions for Eniola at ZMI altar area.
 *
 * @author vddCore
 */
class EniolaListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.ENIOLA_6362, NPC, "bank") { player, _ ->
            setAttribute(player, "zmi:bankaction", "open")
            openInterface(player, Components.BANK_CHARGE_ZMI_619)

            return@on true
        }

        on(NPCs.ENIOLA_6362, NPC, "collect") { player, _ ->
            setAttribute(player, "zmi:bankaction", "collect")
            openInterface(player, Components.BANK_CHARGE_ZMI_619)

            return@on true
        }
    }
}
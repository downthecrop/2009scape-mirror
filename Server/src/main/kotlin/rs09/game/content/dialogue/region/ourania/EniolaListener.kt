package rs09.game.content.dialogue.region.ourania

import api.openInterface
import api.restrictForIronman
import api.setAttribute
import core.game.node.entity.player.link.IronmanMode
import org.rs09.consts.Components
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * Handles special banking interactions for Eniola at ZMI altar area.
 *
 * @author vddCore
 */
class EniolaListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.ENIOLA_6362, IntType.NPC, "bank") { player, _ ->
            restrictForIronman(player, IronmanMode.ULTIMATE) {
                setAttribute(player, "zmi:bankaction", "open")
                openInterface(player, Components.BANK_CHARGE_ZMI_619)
            }

            return@on true
        }

        on(NPCs.ENIOLA_6362, IntType.NPC, "collect") { player, _ ->
            restrictForIronman(player, IronmanMode.ULTIMATE) {
                setAttribute(player, "zmi:bankaction", "collect")
                openInterface(player, Components.BANK_CHARGE_ZMI_619)
            }

            return@on true
        }
    }
}
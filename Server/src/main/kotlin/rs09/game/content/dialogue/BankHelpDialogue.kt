package rs09.game.content.dialogue

import api.*
import core.game.content.dialogue.DialogueInterpreter
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import org.rs09.consts.Components
import org.rs09.consts.Items

/**
 * Represents the dialogue shown when the user presses
 * the "?" button on the Bank Interface.
 *
 * @author vddCore
 */
class BankHelpDialogue : DialogueFile() {

    override fun loaded() {
        options("Check Bank Value", "Banking Assistance", "Close")
    }

    override fun handle(componentID: Int, buttonID: Int) {
        when (buttonID) {
            1 -> player?.let {
                end()

                val wealth = it.bank.wealth

                if (wealth > 0) {
                    val word = if (wealth != 1) "coins" else "coin"

                    sendItemDialogue(
                        it,
                        Items.COINS_995,
                        wealth,
                        "<br>Your bank is worth <col=a52929>${wealth}</col> ${word}."
                    )
                } else {
                    sendDialogue(it, "You have no valuables in your bank.")
                }
            }

            2 -> player?.let {
                end()

                it.bank.close()
                openInterface(it, Components.BANK_V2_HELP_767)
            }

            3 -> end()
        }
    }
}
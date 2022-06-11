package rs09.game.content.dialogue

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * Represents the dialogue shown when the user presses
 * "Deposit Beast of Burden" button on the Bank Interface.
 *
 * @author vddCore
 */
class BankDepositDialogue : DialogueFile() {

    override fun loaded() {
        options("Deposit Inventory", "Deposit Worn Equipment", "Deposit Beast of Burden", "Cancel")
    }

    override fun handle(componentID: Int, buttonID: Int) {
        when (buttonID) {
            1 -> player?.let {
                end()

                if (it.inventory.isEmpty) {
                    sendMessage(it, "You have nothing in your inventory that you can deposit.")
                } else {
                    dumpContainer(it, it.inventory)
                }
            }

            2 -> player?.let {
                end()

                if (it.equipment.isEmpty) {
                    sendMessage(it, "You have no equipment that you can deposit.")
                } else {
                    dumpContainer(it, it.equipment)
                }
            }

            3 -> {
                end()

                player?.familiarManager?.dumpBob()
            }

            4 -> end()
        }
    }
}
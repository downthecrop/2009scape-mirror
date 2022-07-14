package rs09.game.content.dialogue.region.worldwide.bank

import api.dumpBeastOfBurden
import api.dumpContainer
import api.sendMessage
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.START_DIALOGUE

/**
 * Represents the dialogue shown when the user presses
 * "Deposit Beast of Burden" button on the Bank Interface.
 *
 * @author vddCore
 */
class BankDepositDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> options(
                "Deposit Inventory",
                "Deposit Worn Equipment",
                "Deposit Beast of Burden",
                "Cancel"
            ).also { stage++ }

            1 -> when (buttonID) {
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

                3 -> player?.let {
                    end()
                    dumpBeastOfBurden(it)
                }

                4 -> end()
            }
        }
    }
}
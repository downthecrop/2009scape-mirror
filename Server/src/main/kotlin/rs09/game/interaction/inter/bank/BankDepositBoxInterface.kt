package rs09.game.interaction.inter.bank

import api.animate
import api.dumpBeastOfBurden
import api.runWorldTask
import api.sendMessage
import core.game.component.Component
import core.game.node.entity.player.Player
import org.rs09.consts.Animations
import org.rs09.consts.Components
import rs09.game.interaction.InterfaceListener
import rs09.game.interaction.util.BankUtils

/**
 * Allows the user to interact with the
 * Bank Deposit Box interface
 *
 * @author vddCore
 */
class BankDepositBoxInterface : InterfaceListener {
    companion object {
        private const val BUTTON_DEPOSIT_BOB = 13

        private const val MENU_ELEMENT = 11
        private const val OP_AMOUNT_ONE = 155
        private const val OP_AMOUNT_FIVE = 196
        private const val OP_AMOUNT_TEN = 124
        private const val OP_AMOUNT_ALL = 199
        private const val OP_AMOUNT_X = 234
        private const val OP_EXAMINE = 168
    }

    private fun handleDepositBoxMenu(player: Player, component: Component, opcode: Int, buttonID: Int, slot: Int, itemID: Int): Boolean {
        val item = player.inventory.get(slot)
            ?: return true

        if (opcode == OP_EXAMINE) {
            sendMessage(player, item.definition.examine)
            return true
        }

        runWorldTask {
            when (opcode) {
                OP_AMOUNT_ONE -> player.bank.addItem(slot, 1)
                OP_AMOUNT_FIVE -> player.bank.addItem(slot, 5)
                OP_AMOUNT_TEN -> player.bank.addItem(slot, 10)
                OP_AMOUNT_X -> BankUtils.transferX(
                    player,
                    slot,
                    false,
                    // Needs to have a callback here because the depositing moment is independent from the world task.
                    player.bank::refreshDepositBoxInterface
                )
                OP_AMOUNT_ALL -> player.bank.addItem(
                    slot,
                    player.inventory.getAmount(item)
                )
                else -> player.debug("Unknown deposit box menu opcode $opcode")
            }

            player.bank.refreshDepositBoxInterface()
            animate(player, Animations.HUMAN_BANK_DEPOSIT_BOX_834)
        }

        return true
    }

    override fun defineInterfaceListeners() {
        on(Components.BANK_DEPOSIT_BOX_11, ::handleDepositBoxMenu)
        on(
            Components.BANK_DEPOSIT_BOX_11,
            BUTTON_DEPOSIT_BOB
        ) { player, _, _, _, _, _ ->
            dumpBeastOfBurden(player); true
        }
    }
}
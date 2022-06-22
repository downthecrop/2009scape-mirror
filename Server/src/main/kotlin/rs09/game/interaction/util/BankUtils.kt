package rs09.game.interaction.util

import api.InputType
import api.sendInputDialogue
import api.sendMessage
import core.game.node.entity.player.Player

object BankUtils {
    fun transferX(player: Player, slot: Int, withdraw: Boolean, after: (() -> Unit)? = null) {
        sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
            val number = Integer.parseInt(value.toString())

            if (withdraw) {
                player.bank.takeItem(slot, number)
            } else {
                player.bank.addItem(slot, number)
            }

            player.bank.updateLastAmountX(number)
            after?.let { it() }
        }
    }
}
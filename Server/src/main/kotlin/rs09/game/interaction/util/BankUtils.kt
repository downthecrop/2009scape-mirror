package rs09.game.interaction.util

import api.sendInputDialogue
import api.sendMessage
import core.game.node.entity.player.Player

object BankUtils {

    // TODO(I don't like having a *Utils class for singular methods...)
    fun transferX(player: Player, slot: Int, withdraw: Boolean) {
        sendInputDialogue(player, false, "Enter the amount: ") { value ->
            var input = value.toString().lowercase()

            if (!input.matches(Regex("^(\\d+)(k+|m+)?\$"))) {
                sendMessage(player, "Your input was invalid.")
                return@sendInputDialogue
            }

            input = input
                .replace("k", "000")
                .replace("m", "000000")

            val number = Integer.parseInt(input)

            if (withdraw) {
                player.bank.takeItem(slot, number)
            } else {
                player.bank.addItem(slot, number)
            }

            player.bank.updateLastAmountX(number)
        }
    }
}
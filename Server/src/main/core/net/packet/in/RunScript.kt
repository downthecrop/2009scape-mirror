package core.net.packet.`in`

import core.api.*
import core.game.node.entity.player.Player

/**
 * Handles an incoming script execution request packet.
 *
 * @author vddCore
 */
object RunScript {

    fun processInput(player: Player, value: Any, script: ((Any) -> Boolean)) {
        if (value is Int && value <= 0) return
        
        val type = player.getAttribute("input-type", InputType.NUMERIC)

        var input = value

        if (player.getAttribute("parseamount", false)) {
            input = value.toString().lowercase()

            if (!input.matches(Regex("^(\\d+)(k+|m+)?$"))) {
                sendDialogue(player, "That doesn't look right. Please try again.")
                return
            }

            input = input.replace("k", "000").replace("m", "000000")
        }

        if (type == InputType.NUMERIC || type == InputType.AMOUNT)
            input = input.toString().toIntOrNull() ?: input

        try {
            script(input)
        } catch (_: NumberFormatException) {
            sendDialogue(player, "That number's a bit large, don't you think?")
        } catch (_: ClassCastException) {
            sendDialogue(player, "Something went wrong here. Try again.")
        } finally {
            removeAttribute(player, "runscript")
            removeAttribute(player, "input-type")
        }
    }
}

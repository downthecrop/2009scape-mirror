package rs09.net.packet.`in`

import api.sendDialogue
import core.game.node.entity.player.Player
import core.net.packet.IncomingPacket
import core.net.packet.IoBuffer
import core.tools.StringUtils

/**
 * Handles an incoming script execution request packet.
 *
 * @author vddCore
 */
class RunScriptPacketHandler : IncomingPacket {

    private fun processInput(player: Player, value: Any, script: ((Any) -> Boolean)) {
        if (value is Int && value <= 0) return

        var input = value

        if (player.getAttribute("parseamount", false)) {
            input = value.toString().lowercase()

            if (!input.matches(Regex("^(\\d+)(k+|m+)?$"))) {
                sendDialogue(player, "That doesn't look right. Please try again.")
                return
            }

            input = input.replace("k", "000").replace("m", "000000")
        }

        try {
            script(input)
        } catch (_: NumberFormatException) {
            sendDialogue(player, "That number's a bit large, don't you think?")
        }
    }

    override fun decode(player: Player, opcode: Int, buffer: IoBuffer) {
        val script: ((Any) -> Boolean)? = player.getAttribute("runscript", null)

        if (script == null || player.locks.isInteractionLocked)
            return

        val value: Any = when(opcode) {
            244 -> StringUtils.longToString(buffer.long)
            65 -> buffer.string
            else -> buffer.int // Handles OpCode 23 and other cases.
        }

        processInput(player, value, script)

        player.removeAttribute("parseamount")
        player.removeAttribute("runscript")
    }
}

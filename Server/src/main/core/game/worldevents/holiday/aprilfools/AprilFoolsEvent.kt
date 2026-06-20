package core.game.worldevents.holiday.aprilfools

import core.ServerConstants
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.system.command.Privilege
import core.tools.colorize
import java.util.Calendar

class AprilFoolsEvent : Commands, LoginListener {
    private fun isActive(): Boolean {
        if (ServerConstants.FORCE_APRIL_FOOLS) return true
        if (!ServerConstants.APRIL_FOOLS_EVENT) return false
        val cal = Calendar.getInstance()
        return cal.get(Calendar.MONTH) == Calendar.APRIL && cal.get(Calendar.DAY_OF_MONTH) == 1
    }

    override fun login(player: Player) {
        if (!isActive()) return
        sendMessage(player, colorize("Use command ::wheel to spin the new prize wheel!", "00ffff"))
    }

    override fun defineCommands() {
        define("wheel", Privilege.STANDARD) { player, args ->
            if (!isActive()) {
                sendMessage(player, colorize("-->%R${args[0]}: command not found"))
                return@define
            }
            if (getAttribute(player, "aprilfools:wheelspun", false)) {
                sendMessage(player, "I'm not falling for that again.")
                return@define
            }
            sendNews("${player.username} just tried to spin the wheel!")
            sendChat(player, "I'm spinning!")
            emote(player, Emotes.SPIN)
            setAttribute(player, "aprilfools:wheelspun", true)
        }
    }
}
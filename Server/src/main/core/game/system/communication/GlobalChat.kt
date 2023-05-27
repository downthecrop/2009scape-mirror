package core.game.system.communication

import core.api.Commands
import core.api.getAttribute
import core.api.sendMessage
import core.api.setAttribute
import core.game.system.command.Privilege
import core.game.world.repository.Repository
import core.game.node.entity.player.info.Rights
import core.tools.colorize

class GlobalChat : Commands {
    override fun defineCommands() {
        define("muteglobal", Privilege.STANDARD, "", "Toggles global chat on or off.") {player, _ ->
            val original = getAttribute(player, ATTR_GLOBAL_MUTE, false)
            setAttribute(player, ATTR_GLOBAL_MUTE, !original)
            sendMessage(player, "Global chat is now ${if (original) "ON" else "OFF"}.")
            return@define
        }
    }

    companion object {
        val ATTR_GLOBAL_MUTE = "/save:globalmute"
        fun process(sender: String, message: String, rights: Int) {
            val msgSD = prepare(sender, message, false, rights)
            val msgHD = prepare(sender, message, true, rights)
            for (player in Repository.players.filter { !getAttribute(it, ATTR_GLOBAL_MUTE, false) }) {
                if (player.interfaceManager.isResizable)
                    sendMessage(player, msgHD)
                else
                    sendMessage(player, msgSD)
            }
        }

        private fun prepare(sender: String, message: String, isResizable: Boolean, rights: Int): String {
            val baseColor = if (isResizable) "%f1b04c" else "%7512ff"
            val bracketColor = if (isResizable) "%ffffff" else "%000000"
            return colorize("$bracketColor[${baseColor}G$bracketColor] ${if (rights > 0) "<img=${rights - 1}>" else ""}$sender: ${baseColor}$message")
        }
    }
}

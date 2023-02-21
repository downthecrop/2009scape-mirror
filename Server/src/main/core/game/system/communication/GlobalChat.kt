package core.game.system.communication

import core.api.getAttribute
import core.api.sendMessage
import core.game.world.repository.Repository
import core.tools.colorize

object GlobalChat {
    private val ATTR_GLOBAL_MUTE = "/save:globalmute"
    fun process(sender: String, message: String) {
        val msg = prepare(sender, message)
        for (player in Repository.players.filter { !getAttribute(it, ATTR_GLOBAL_MUTE, false) })
            sendMessage(player, msg)
    }

    private fun prepare(sender: String, message: String) : String {
        return colorize("%44e3e0[%GG%44e3e0] %ffffff$sender: $message")
    }
}
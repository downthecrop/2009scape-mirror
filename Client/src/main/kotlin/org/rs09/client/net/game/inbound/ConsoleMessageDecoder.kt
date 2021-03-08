package org.rs09.client.net.game.inbound

import org.rs09.client.console.DeveloperConsole
import org.runite.client.DataBuffer

object ConsoleMessageDecoder: GamePacketDecoder {
    override fun decode(buffer: DataBuffer) {
        val line = buffer.readString()

        DeveloperConsole.println(line.toString(), true)
    }
}
package org.rs09.client.net.game.inbound

import org.rs09.client.console.AutocompletionHints
import org.rs09.client.console.DeveloperConsole
import org.runite.client.DataBuffer

object ConsoleAutocompletionPacketDecoder: GamePacketDecoder {
    override fun decode(buffer: DataBuffer) {
        val line = buffer.readString().toString()
        val size = buffer.readUnsignedShort()
        val list = ArrayList<String>()
        for(i in 0 until size)
            list += buffer.readString().toString()
        val total = buffer.readInt()

        DeveloperConsole.autocompletions = AutocompletionHints(line, list, total)
    }
}
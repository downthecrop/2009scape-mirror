package org.rs09.client.net.game.inbound

import org.runite.client.DataBuffer

interface GamePacketDecoder {
    fun decode(buffer: DataBuffer)
}
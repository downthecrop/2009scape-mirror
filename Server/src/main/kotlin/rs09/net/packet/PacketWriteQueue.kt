package rs09.net.packet

import core.net.packet.OutgoingPacket
import core.net.packet.out.*
import gui.GuiEvent
import rs09.game.system.SystemLogger
import java.util.*

object PacketWriteQueue {
    private val PacketsToWrite: Queue<QueuedPacket<*>> = LinkedList<QueuedPacket<*>>()

    @JvmStatic
    fun <T> handle(packet: OutgoingPacket<T>, context: T){
        when(packet){
            //Dynamic packets need to be sent immediately
            is UpdateSceneGraph,
            is BuildDynamicScene,
            is InstancedLocationUpdate,
            is ClearRegionChunk -> packet.send(context)
            //Rest get queued up and sent at the end of the tick (authentic)
            else -> queue(packet,context)
        }
    }

    @JvmStatic
    fun <T> queue(packet: OutgoingPacket<T>, context: T){
        PacketsToWrite.add(QueuedPacket(packet,context))
    }

    @JvmStatic
    fun flush(){
        while(!PacketsToWrite.isEmpty()){
            val p = PacketsToWrite.poll() ?: continue
            write(p.out,p.context)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> write(out: OutgoingPacket<*>, context: T){
        val pack = out as? OutgoingPacket<T>
        val ctx = context as? T
        if(pack == null || ctx == null){
            SystemLogger.logWarn("Failed packet casting")
            return
        }
        pack.send(ctx)
    }
}

class QueuedPacket<T>(val out: OutgoingPacket<T>, val context: T)
package core.net.packet

import core.api.tryPop
import core.net.packet.out.*
import core.tools.SystemLogger
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.concurrent.locks.ReentrantLock

class PacketWriteQueue {
    companion object {
        private val packetsToWrite = LinkedList<QueuedPacket<*>?>()

        @JvmStatic
        fun <T> handle(packet: OutgoingPacket<T>, context: T) {
            when (packet) {
                //Dynamic packets need to be sent immediately
                is UpdateSceneGraph,
                is BuildDynamicScene,
                is InstancedLocationUpdate,
                is ClearRegionChunk -> packet.send(context)
                //Rest get queued up and sent at the end of the tick (authentic)
                else -> push(packet, context)
            }
        }

        @JvmStatic
        fun <T> push(packet: OutgoingPacket<T>, context: T) {
            packetsToWrite.add(QueuedPacket(packet, context))
        }

        @JvmStatic
        fun flush() {
            var countThisCycle = packetsToWrite.size
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            while (countThisCycle-- > 0) {
                val pkt = packetsToWrite.tryPop(null) ?: continue
                try {
                    write(pkt.out, pkt.context)
                } catch (e: Exception) {
                    e.printStackTrace(pw)
                    SystemLogger.logErr(this::class.java, "Error flushing packet ${pkt.out::class.java}: $sw")
                    continue
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> write(out: OutgoingPacket<*>, context: T) {
            val pack = out as? OutgoingPacket<T>
            val ctx = context as? T
            if (pack == null || ctx == null) {
                throw IllegalStateException("Failed packet casting")
            }
            pack.send(ctx)
        }
    }
}

class QueuedPacket<T>(val out: OutgoingPacket<T>, val context: T)
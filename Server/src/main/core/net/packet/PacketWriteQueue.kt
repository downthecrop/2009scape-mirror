package core.net.packet

import core.api.log
import core.net.packet.out.*
import core.tools.Log
import core.tools.SystemLogger
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

class PacketWriteQueue {
    companion object {
        private var packetsToWrite = LinkedList<QueuedPacket<*>>()
        private var packetsToFlush = LinkedList<QueuedPacket<*>>()
        private val queueLock = Any()

        @JvmStatic
        fun <T> handle(packet: OutgoingPacket<T>, context: T) {
            when (packet) {
                //Dynamic packets need to be sent immediately
                is UpdateSceneGraph,
                is BuildDynamicScene,
                is InstancedLocationUpdate,
                is Logout,
                is ClearRegionChunk -> packet.send(context)
                //Rest get queued up and sent at the end of the tick (authentic)
                else -> push(packet, context)
            }
        }

        @JvmStatic
        fun <T> push(packet: OutgoingPacket<T>, context: T) {
            if (context == null) {
                log(this::class.java, Log.ERR,  "${packet::class.java.simpleName} tried to queue with a null context!")
                return
            }
            synchronized(queueLock) {
                packetsToWrite.add(QueuedPacket(packet, context))
            }
        }

        @JvmStatic
        fun flush() {
            synchronized(queueLock) {
                if (packetsToWrite.isEmpty()) {
                    return
                }
                val queued = packetsToWrite
                packetsToWrite = packetsToFlush
                packetsToFlush = queued
            }
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            while (packetsToFlush.isNotEmpty()) {
                val pkt = packetsToFlush.pollFirst() ?: continue
                try {
                    write(pkt.out, pkt.context)
                } catch (e: Exception) {
                    e.printStackTrace(pw)
                    log(this::class.java, Log.ERR,  "Error flushing packet ${pkt.out::class.java}: $sw")
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

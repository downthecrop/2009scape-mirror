package rs09.net.packet

import api.TickListener
import core.net.packet.OutgoingPacket
import core.net.packet.out.*
import rs09.game.system.SystemLogger
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList

class PacketWriteQueue : TickListener {
    override fun tick() {
        flush()
    }

    companion object {
        private val queueLock = ReentrantLock()
        private val packetsToQueue = ArrayList<QueuedPacket<*>>(1000)
        private val packetsToWrite = LinkedList<QueuedPacket<*>>()

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
            if (queueLock.isLocked)
                packetsToQueue.add(QueuedPacket(packet, context))
            else
                packetsToWrite.add(QueuedPacket(packet, context))
        }

        @JvmStatic
        fun pop(): QueuedPacket<*>? {
            return try {
                packetsToWrite.pop()
            } catch (e: NoSuchElementException) {
                null
            }
        }

        @JvmStatic
        fun flush() {
            queueLock.lock()
            var packet: QueuedPacket<*>?
            while (pop().also { packet = it } != null)
                write(packet?.out ?: break, packet?.context ?: break)
            if (packetsToWrite.isNotEmpty()) {
                SystemLogger.logWarn("Packet queue was NOT empty! Remaining packets: ${packetsToWrite.size}")
                try {
                    for (pkt in packetsToWrite) SystemLogger.logWarn("${pkt.out.javaClass.simpleName} <- ${pkt.context}")
                } catch (e: Exception)
                {
                    e.printStackTrace()
                } finally {
                    packetsToWrite.clear()
                }
            }
            val queueIter = packetsToQueue.iterator()
            while (queueIter.hasNext()) {
                packetsToWrite.add(queueIter.next())
                queueIter.remove()
            }
            queueLock.unlock()
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> write(out: OutgoingPacket<*>, context: T) {
            val pack = out as? OutgoingPacket<T>
            val ctx = context as? T
            if (pack == null || ctx == null) {
                SystemLogger.logWarn("Failed packet casting")
                return
            }
            pack.send(ctx)
        }
    }
}

class QueuedPacket<T>(val out: OutgoingPacket<T>, val context: T)
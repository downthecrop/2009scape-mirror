package rs09.net.packet

import api.TickListener
import core.net.packet.OutgoingPacket
import core.net.packet.out.*
import rs09.game.system.SystemLogger
import java.lang.IndexOutOfBoundsException
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList

class PacketWriteQueue : TickListener {
    override fun tick() {
        flush()
    }

    companion object {
        private val queueLock = ReentrantLock()
        private val packetsToQueue = LinkedList<QueuedPacket<*>?>()
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
            if (queueLock.isHeldByCurrentThread)
                packetsToQueue.add(QueuedPacket(packet, context))
            else
                packetsToWrite.add(QueuedPacket(packet, context))
        }

        @JvmStatic
        fun flush() {
            queueLock.lock()

            var hasEnded = false
            while (!hasEnded) {
                try {
                    val packet = packetsToWrite.pop()
                    write(packet?.out ?: continue, packet.context ?: continue)
                } catch (e: NoSuchElementException) {
                    hasEnded = true
                }
            }

            if (packetsToWrite.isNotEmpty()) {
                SystemLogger.logWarn(this::class.java, "Packet queue was NOT empty! Remaining packets: ${packetsToWrite.size}")
                try {
                    for (pkt: QueuedPacket<*>? in packetsToWrite) SystemLogger.logWarn(this::class.java, "${pkt?.out?.javaClass?.simpleName ?: "NULL"} <- ${pkt?.context ?: "NULL"}")
                } catch (ignored: NullPointerException) {
                    //do nothing, we don't care, this can happen when everything is working as intended.
                } catch (ignored: IndexOutOfBoundsException) {
                    //do nothing, we don't care, this can happen when everything is working as intended.
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    packetsToWrite.clear()
                }
            }

            queueLock.unlock()

            while (packetsToQueue.isNotEmpty()) {
                packetsToWrite.add(packetsToQueue.pop())
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> write(out: OutgoingPacket<*>, context: T) {
            val pack = out as? OutgoingPacket<T>
            val ctx = context as? T
            if (pack == null || ctx == null) {
                SystemLogger.logWarn(this::class.java, "Failed packet casting")
                return
            }
            pack.send(ctx)
        }
    }
}

class QueuedPacket<T>(val out: OutgoingPacket<T>, val context: T)
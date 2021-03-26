package org.rs09.client.net

import org.runite.client.Class64
import org.runite.client.Signlink
import org.runite.client.TimeUtils
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Connection(val socket: Socket, val signlink: Signlink) : Runnable {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    private var inputStream: InputStream
    private var outputStream: OutputStream
    private var closed = false
    private var writeFailed = false
    private var outputBuffer: ByteArray? = null
    private var writeIndex = 0
    private var someOtherIndex = 0
    private var class64: Class64? = null

    init {
        socket.soTimeout = 30_000
        socket.tcpNoDelay = true
        inputStream = socket.getInputStream()
        outputStream = socket.getOutputStream()
    }

    override fun run() {
        while (true) {
            var bufferOffset: Int
            var transmitLength: Int

            lock.lock()
            try {
                if (writeIndex == someOtherIndex) {
                    if (closed) break
                    try {
                        condition.await()
                    } catch (e: InterruptedException) {
                    }
                }

                bufferOffset = someOtherIndex
                transmitLength = if (writeIndex < someOtherIndex) 5000 - someOtherIndex
                else writeIndex - someOtherIndex
            } finally {
                lock.unlock()
            }

            if (transmitLength <= 0) continue

            try {
                outputStream.write(outputBuffer!!, bufferOffset, transmitLength)
            } catch (e: IOException) {
                e.printStackTrace()
                writeFailed = true
            }

            someOtherIndex = (transmitLength + someOtherIndex) % 5000

            try {
                if (someOtherIndex != writeIndex) continue

                outputStream.flush()
            } catch (e: IOException) {
                e.printStackTrace()
                writeFailed = true
            }
        }

        try {
            inputStream.close()
            outputStream.close()
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        outputBuffer = null
    }

    @Throws(IOException::class)
    fun readBytes(buffer: ByteArray, offset: Int, length: Int) {
        check(offset >= 0) { "Offset <= 0 in readBytes" }
        check(length >= 0) { "Read length <= 0 in readBytes" }
        check(offset + length <= buffer.size) { "offset + length is greater than buffer size" }
        check(!closed) { "Attempted to read from closed connection" }

        var remaining = length
        var bufferOffset = offset
        while (remaining > 0) {
            val bytesRead = inputStream.read(buffer, bufferOffset, remaining)
            if (bytesRead <= 0)
                throw EOFException()

            bufferOffset += bytesRead
            remaining -= bytesRead
        }
    }

    @Throws(IOException::class)
    fun readByte(): Int {
        check(!closed) { "Attempted to read from closed connection" }
        return inputStream.read()
    }

    @Throws(IOException::class)
    fun sendBytes(buffer: ByteArray, length: Int) {
        check(!closed) { "Attempted to write to closed connection" }

        if (writeFailed) {
            writeFailed = false
            throw IOException("Attempted to write straight after failed write!")
        }

        val out = outputBuffer ?: ByteArray(5000).apply { outputBuffer = this }

        lock.withLock {
            for (i in 0 until length) {
                out[writeIndex] = buffer[i]
                writeIndex = (writeIndex + 1) % 5000

                if ((4900 + someOtherIndex) % 5000 == writeIndex) {
                    throw IOException("Buffer overflow")
                }
            }

            if (class64 == null) {
                class64 = signlink.startThread(3, this)
            }

            condition.signalAll()
        }
    }

    fun finalize() {
        if (!closed) {
            System.err.println("Finalized connection, but it wasn't closed! Did we leak?")
            close()
        }
    }

    fun checkErrors() {
        if (closed || !writeFailed) return

        writeFailed = false
        throw IOException("So why are we throwing this now?")
    }

    @Throws(IOException::class)
    fun availableBytes(): Int = if (closed) 0 else inputStream.available()

    fun applyDummyStreams() {
        if (closed) return

        inputStream = DummyInputStream()
        outputStream = DummyOutputStream()
    }

    fun close() {
        if (closed) return

        lock.withLock {
            closed = true
            condition.signalAll()
        }

        class64?.let { class64 ->
            while (class64.anInt978 == 0) TimeUtils.sleep(1L)

            if (class64.anInt978 == 1) {
                try {
                    (class64.anObject974 as Thread).join()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        class64 = null
    }

}
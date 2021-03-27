package js5server

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import js5server.ext.readMedium
import kotlinx.coroutines.*
import java.util.concurrent.Executors

class JS5Net(
    private val server: FileServer,
    private val revision: Int,
    private val subRevision: Int
) {

    private val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        System.err.println("$context | $throwable")
    }

    private lateinit var dispatcher: ExecutorCoroutineDispatcher
    private var running = false

    /**
     * Start the server and begin creating a new coroutine for every new connection accepted
     * @param threads a fixed number or 0 to dynamically allocate based on need
     */
    fun start(port: Int, threads: Int) = runBlocking {
        val executor = if (threads == 0) Executors.newCachedThreadPool() else Executors.newFixedThreadPool(threads)
        dispatcher = executor.asCoroutineDispatcher()
        val selector = ActorSelectorManager(dispatcher)
        val supervisor = SupervisorJob()
        val scope = CoroutineScope(coroutineContext + supervisor + exceptionHandler)
        with(scope) {
            val server = aSocket(selector).tcp().bind(port = port)
            running = true
            while (running) {
                val socket = server.accept()
                launch(Dispatchers.IO) {
                    connect(socket)
                }
            }
        }
    }

    suspend fun connect(socket: Socket) {
        val read = socket.openReadChannel()
        val write = socket.openWriteChannel(autoFlush = true)
        synchronise(read, write)
        if (acknowledge(read, write)) {
            readRequests(read, write)
        }
    }

    /**
     * If the client is up-to-date and in the correct state send it the [prefetchKeys] list so it knows what indices are available to request
     */
    private suspend fun synchronise(read: ByteReadChannel, write: ByteWriteChannel) {
        val opcode = read.readByte().toInt()
        println("Received $opcode")
        if (opcode != HANDSHAKE_REQUEST) {
            write.writeByte(REJECT_SESSION)
            write.close()
            return
        }

        val revision = read.readInt()
        val version = read.readInt()
        println("Received ${this.revision}.${this.subRevision} | ${revision}.$version")
        if (revision != this.revision || version != this.subRevision) {
            write.writeByte(GAME_UPDATED)
            write.close()
            return
        }
        write.writeByte(0)
    }

    /**
     * Confirm the client got our message and is ready to start sending file requests
     */
    private suspend fun acknowledge(read: ByteReadChannel, write: ByteWriteChannel): Boolean {
        val opcode = read.readByte().toInt()
        println("Received $opcode")
        if (opcode != ACKNOWLEDGE) {
            write.writeByte(REJECT_SESSION)
            write.close()
            return false
        }

        return verify(read, write, ACKNOWLEDGE_ID)
    }

    /**
     * Confirm a session value send by the client is as the server [expected]
     */
    private suspend fun verify(read: ByteReadChannel, write: ByteWriteChannel, expected: Int): Boolean {
        val id = read.readMedium()
        if (id != expected) {
            write.writeByte(BAD_SESSION_ID)
            write.close()
            return false
        }
        return true
    }

    private suspend fun readRequests(read: ByteReadChannel, write: ByteWriteChannel) = coroutineScope {
        try {
            while (isActive) {
                readRequest(read, write)
            }
        } finally {
            println("Client disconnected js5")
        }
    }

    /**
     * Verify status updates and pass requests onto the [server] to fulfill
     */
    private suspend fun readRequest(read: ByteReadChannel, write: ByteWriteChannel) {
        when (val opcode = read.readByte().toInt()) {
            STATUS_LOGGED_OUT, STATUS_LOGGED_IN -> verify(read, write, STATUS_ID)
            PRIORITY_REQUEST, PREFETCH_REQUEST -> server.fulfill(read, write, opcode == PREFETCH_REQUEST)
            else -> {
                println("Closing write")
                write.close()
            }
        }
    }

    fun stop() {
        running = false
        dispatcher.close()
    }

    companion object {
        // Session ids
        const val ACKNOWLEDGE_ID = 3
        const val STATUS_ID = 0

        // Opcodes
        const val PREFETCH_REQUEST = 0
        const val PRIORITY_REQUEST = 1
        const val HANDSHAKE_REQUEST = 15
        const val STATUS_LOGGED_IN = 2
        const val STATUS_LOGGED_OUT = 3
        const val ACKNOWLEDGE = 6

        // Response codes
        private const val GAME_UPDATED = 6
        private const val BAD_SESSION_ID = 10
        private const val REJECT_SESSION = 11
    }
}
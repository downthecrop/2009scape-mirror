package core.net.websocket

import core.api.log
import core.net.IoSession
import core.tools.Log
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GameWebSocketServer(port: Int, poolSize: Int) : WebSocketServer(InetSocketAddress(port)) {
    private val service: ExecutorService = Executors.newFixedThreadPool(poolSize) { runnable ->
        Thread(runnable, "WebSocketIo").apply {
            isDaemon = true
        }
    }
    private val sessions = ConcurrentHashMap<WebSocket, WebSocketIoSession>()

    init {
        // This pool processes websocket frames; it is not a cap on connected webclients.
        setTcpNoDelay(true)
        setReuseAddr(true)
    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        sessions[conn] = WebSocketIoSession(conn, service)
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        val session = sessions.remove(conn)
        if (session != null && session.isActive) {
            session.disconnect()
        }
    }

    override fun onMessage(conn: WebSocket, message: String) {
        log(javaClass, Log.WARN, "Closing websocket client ${conn.remoteSocketAddress}: text frames are unsupported.")
        conn.close(1003, "Binary frames only")
    }

    override fun onMessage(conn: WebSocket, message: ByteBuffer) {
        val session: IoSession = sessions[conn] ?: run {
            conn.close(1011, "Session not initialized")
            return
        }
        val copy = ByteBuffer.allocate(message.remaining())
        copy.put(message)
        copy.flip()
        service.execute(session.producer.produceReader(session, copy))
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        if (ex != null) {
            log(javaClass, Log.ERR, "WebSocket server error: ${ex.message}")
        }
        if (conn != null) {
            sessions.remove(conn)?.disconnect()
        }
    }

    override fun onStart() {
        log(javaClass, Log.INFO, "WebSocket listener started on port $port.")
    }

    override fun stop(timeout: Int, closeMessage: String) {
        super.stop(timeout, closeMessage)
        service.shutdownNow()
    }
}

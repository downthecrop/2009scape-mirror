package core.net.websocket

import core.net.IoSession
import org.java_websocket.WebSocket
import org.java_websocket.exceptions.WebsocketNotConnectedException
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService

class WebSocketIoSession(
    private val socket: WebSocket,
    service: ExecutorService
) : IoSession(null, service, socket.remoteSocketAddress?.toString() ?: "127.0.0.1") {

    override fun queue(buffer: ByteBuffer?) {
        if (buffer == null || !socket.isOpen) {
            return
        }
        val copy = buffer.slice()
        val data = ByteArray(copy.remaining())
        copy.get(data)
        try {
            socket.send(data)
        } catch (e: WebsocketNotConnectedException) {
            disconnect()
        }
    }

    override fun write() {
        // WebSocket sessions bypass the SelectionKey-backed TCP write queue.
    }

    override fun disconnect() {
        if (!isActive) {
            return
        }
        super.disconnect()
        if (socket.isOpen) {
            socket.close()
        }
    }
}

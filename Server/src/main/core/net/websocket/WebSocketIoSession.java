package core.net.websocket;

import core.net.IoSession;
import org.java_websocket.WebSocket;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;

public final class WebSocketIoSession extends IoSession {

	private final WebSocket socket;

	public WebSocketIoSession(WebSocket socket, ExecutorService service) {
		super(null, service, socket.getRemoteSocketAddress() == null ? "127.0.0.1" : socket.getRemoteSocketAddress().toString());
		this.socket = socket;
	}

	@Override
	public void queue(ByteBuffer buffer) {
		if (buffer == null || !socket.isOpen()) {
			return;
		}
		ByteBuffer copy = buffer.slice();
		byte[] data = new byte[copy.remaining()];
		copy.get(data);
		socket.send(data);
	}

	@Override
	public void write() {
		// WebSocket writes are pushed immediately in queue().
	}

	@Override
	public void disconnect() {
		if (!isActive()) {
			return;
		}
		super.disconnect();
		if (socket.isOpen()) {
			socket.close();
		}
	}
}

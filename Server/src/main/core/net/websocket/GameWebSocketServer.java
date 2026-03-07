package core.net.websocket;

import core.net.IoSession;
import core.tools.Log;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static core.api.ContentAPIKt.log;

public final class GameWebSocketServer extends WebSocketServer {

	private final ExecutorService service;
	private final Map<WebSocket, WebSocketIoSession> sessions = new ConcurrentHashMap<>();

	public GameWebSocketServer(int port, int poolSize) {
		super(new InetSocketAddress(port));
		this.service = Executors.newFixedThreadPool(poolSize);
		setTcpNoDelay(true);
		setReuseAddr(true);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		sessions.put(conn, new WebSocketIoSession(conn, service));
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		WebSocketIoSession session = sessions.remove(conn);
		if (session != null && session.isActive()) {
			session.disconnect();
		}
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		log(this.getClass(), Log.WARN, "Closing websocket client " + conn.getRemoteSocketAddress() + ": text frames are unsupported.");
		conn.close(1003, "Binary frames only");
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		IoSession session = sessions.get(conn);
		if (session == null) {
			conn.close(1011, "Session not initialized");
			return;
		}
		ByteBuffer copy = ByteBuffer.allocate(message.remaining());
		copy.put(message);
		copy.flip();
		service.execute(session.getProducer().produceReader(session, copy));
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		if (ex != null) {
			log(this.getClass(), Log.ERR, "WebSocket server error: " + ex.getMessage());
		}
		if (conn != null) {
			WebSocketIoSession session = sessions.remove(conn);
			if (session != null) {
				session.disconnect();
			}
		}
	}

	@Override
	public void onStart() {
		log(this.getClass(), Log.INFO, "WebSocket listener started on port " + getPort() + ".");
	}

	@Override
	public void stop(int timeout, String closeMessage) throws InterruptedException {
		super.stop(timeout, closeMessage);
		service.shutdownNow();
	}
}

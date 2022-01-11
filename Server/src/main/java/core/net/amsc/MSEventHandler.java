package core.net.amsc;

import rs09.game.system.SystemLogger;
import core.net.IoEventHandler;
import core.net.IoSession;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

/**
 * Handles the management server events.
 * @author Emperor
 */
public final class MSEventHandler extends IoEventHandler {

	/**
	 * Constructs a new {@Code MSEventHandler} {@Code Object}
	 */
	public MSEventHandler() {
		super(Executors.newSingleThreadExecutor());
	}

	@Override
	public void connect(SelectionKey key) throws IOException {
		SocketChannel ch = (SocketChannel) key.channel();
		try {
			if (ch.finishConnect()) {
				key.interestOps(key.interestOps() ^ SelectionKey.OP_CONNECT);
				key.interestOps(key.interestOps() | SelectionKey.OP_READ);
				IoSession session = (IoSession) key.attachment();
				key.attach(session = new IoSession(key, service));
				WorldCommunicator.register(session);
				return;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		SystemLogger.logErr("Failed connecting to Management Server!");
		WorldCommunicator.terminate();
	}

	@Override
	public void accept(SelectionKey key, Selector selector) throws IOException {
		super.write(key);
	}

	@Override
	public void read(SelectionKey key) throws IOException {
		super.read(key);
	}

	@Override
	public void write(SelectionKey key) {
		super.write(key);
	}

	@Override
	public void disconnect(SelectionKey key, Throwable t) {
		super.disconnect(key, t);
		WorldCommunicator.terminate();
	}

}
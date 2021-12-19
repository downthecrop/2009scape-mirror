package core.net.ms;

import core.net.IoEventHandler;
import rs09.net.ms.ManagementServer;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
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
		/**
		 * Empty
		 */
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
		ManagementServer.disconnect();
	}

}
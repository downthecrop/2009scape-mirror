package core.net;

import core.net.amsc.MSEventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles (NIO-based) networking events using the reactor pattern.
 * @author Emperor
 */
public final class NioReactor implements Runnable {

	/**
	 * The executor service.
	 */
	private final ExecutorService service;

	/**
	 * The socket channel.
	 */
	private ServerSocketConnection channel;

	/**
	 * The I/O event handling instance.
	 */
	private IoEventHandler eventHandler;

	/**
	 * If the reactor is running.
	 */
	private boolean running;

	/**
	 * Constructs a new {@code NioReactor}.
	 */
	private NioReactor(IoEventHandler eventHandler) {
		this.service = Executors.newSingleThreadScheduledExecutor();
		this.eventHandler = eventHandler;
	}

	/**
	 * Creates and configures a new {@code NioReactor} with a pool size of 1.
	 * @param port The port.
	 * @return The {@code NioReactor} {@code Object}.
	 * @throws IOException When an I/O exception occurs.
	 */
	public static NioReactor configure(int port) throws IOException {
		return configure(port, 1);
	}

	/**
	 * Creates and configures a new {@code NioReactor}.
	 * @param port The port.
	 * @param poolSize The amount of threads in the thread pool.
	 * @return The {@code NioReactor} {@code Object}.
	 * @throws IOException When an I/O exception occurs.
	 */
	public static NioReactor configure(int port, int poolSize) throws IOException {
		NioReactor reactor = new NioReactor(new IoEventHandler(Executors.newFixedThreadPool(poolSize)));
		ServerSocketChannel channel = ServerSocketChannel.open();
		Selector selector = Selector.open();
		channel.bind(new InetSocketAddress(port));
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_ACCEPT);
		reactor.channel = new ServerSocketConnection(selector, channel);
		return reactor;
	}

	/**
	 * Starts a NIO reactor used for client connections.
	 * @param address The IP-address to connect to.
	 * @param port The port used.
	 * @return The NIO reactor object.
	 * @throws IOException When an exception occurs.
	 */
	public static NioReactor connect(String address, int port) throws IOException {
		NioReactor reactor = new NioReactor(new MSEventHandler());
		Selector selector = Selector.open();
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.socket().setKeepAlive(true);
		channel.socket().setTcpNoDelay(true);
		channel.connect(new InetSocketAddress(address, port));
		channel.register(selector, SelectionKey.OP_CONNECT);
		reactor.channel = new ServerSocketConnection(selector, channel);
		return reactor;
	}

	/**
	 * Starts the reactor.
	 */
	public void start() {
		running = true;
		service.execute(this);
	}

	@Override
	public void run() {
		Thread.currentThread().setName("NioReactor");
		while (running) {
			try {
				channel.getSelector().select();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Iterator<SelectionKey> iterator = channel.getSelector().selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				try {
					if (!key.isValid() || !key.channel().isOpen()) {
						key.cancel();
						continue;
					}
					if (key.isConnectable()) {
						eventHandler.connect(key);
					}
					if (key.isAcceptable()) {
						eventHandler.accept(key, channel.getSelector());
					}
					if (key.isReadable()) {
						eventHandler.read(key);
					} else if (key.isWritable()) {
						eventHandler.write(key);
					}
				} catch (Throwable t) {
					eventHandler.disconnect(key, t);
				}
			}
		}
	}

	/**
	 * Terminates the reactor (once it's done processing current I/O events).
	 */
	public void terminate() {
		running = false;
	}

}
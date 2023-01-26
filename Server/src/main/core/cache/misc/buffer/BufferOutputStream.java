package core.cache.misc.buffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Handles the writing of data on a byte buffer.
 * @author Emperor
 */
public final class BufferOutputStream extends OutputStream {

	/**
	 * The buffer to write on.
	 */
	private final ByteBuffer buffer;

	/**
	 * Constructs a new {@code BufferOutputStream} {@code Object}.
	 * @param buffer The buffer to write on.
	 * @throws IOException When an I/O exception occurs.
	 * @throws SecurityException If a security manager exists and its
	 * checkPermission method denies enabling subclassing.
	 */
	public BufferOutputStream(ByteBuffer buffer) throws IOException, SecurityException {
		super();
		this.buffer = buffer;
	}

	@Override
	public void write(int b) throws IOException {
		buffer.put((byte) b);
	}

	@Override
	public void flush() {
		/*
		 * empty.
		 */
	}

	@Override
	public void close() {
		/*
		 * empty.
		 */
	}

	/**
	 * Gets the buffer.
	 * @return The buffer.
	 */
	public ByteBuffer getBuffer() {
		return buffer;
	}

}
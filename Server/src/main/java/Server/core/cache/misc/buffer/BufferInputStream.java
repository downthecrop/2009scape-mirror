package core.cache.misc.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Handles the reading of data from a byte buffer.
 * @author Emperor
 */
public final class BufferInputStream extends InputStream {

	/**
	 * The buffer to write on.
	 */
	private final ByteBuffer buffer;

	/**
	 * The buffer input stream.
	 * @param buffer The buffer.
	 */
	public BufferInputStream(ByteBuffer buffer) throws IOException {
		this.buffer = buffer;
	}

	@Override
	public int read() throws IOException {
		return buffer.get();
	}

	/**
	 * Gets the buffer.
	 * @return The buffer.
	 */
	public ByteBuffer getBuffer() {
		return buffer;
	}

}

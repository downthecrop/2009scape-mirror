package core.net.event;

import core.net.IoSession;
import core.net.IoWriteEvent;
import core.net.packet.IoBuffer;

import java.nio.ByteBuffer;

/**
 * Handles game packet writing events.
 * @author Emperor
 */
public final class GameWriteEvent extends IoWriteEvent {

	/**
	 * Constructs a new {@code GameWriteEvent}.
	 * @param session The session.
	 * @param context The context.
	 */
	public GameWriteEvent(IoSession session, Object context) {
		super(session, context);
	}

	@Override
	public void write(IoSession session, Object context) {
		if (context instanceof ByteBuffer) {
			session.queue((ByteBuffer) context);
			return;
		}
		IoBuffer buffer = (IoBuffer) context;
		ByteBuffer buf = (ByteBuffer) buffer.toByteBuffer().flip();
		if (buf == null) {
			throw new RuntimeException("Critical networking error: The byte buffer requested was null.");
		}
		if (buffer.opcode() != -1) {
			int packetLength = buf.remaining() + 4;
			ByteBuffer response = ByteBuffer.allocate(packetLength);
			response.put((byte) buffer.opcode());
			switch (buffer.getHeader()) {
			case BYTE:
				response.put((byte) buf.remaining());
				break;
			case SHORT:
				response.putShort((short) buf.remaining());
				break;
			default:
				break;
			}
			buf = (ByteBuffer) response.put(buf).flip();
		}
		session.queue(buf);
	}

}
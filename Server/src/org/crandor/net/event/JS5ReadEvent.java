package org.crandor.net.event;

import org.crandor.cache.misc.buffer.ByteBufferUtils;
import org.crandor.net.IoReadEvent;
import org.crandor.net.IoSession;

import java.nio.ByteBuffer;

/**
 * Handles JS-5 reading events.
 * @author Emperor
 */
public final class JS5ReadEvent extends IoReadEvent {

	/**
	 * Constructs a new {@code JS5ReadEvent}.
	 * @param session The session.
	 * @param buffer The buffer.
	 */
	public JS5ReadEvent(IoSession session, ByteBuffer buffer) {
		super(session, buffer);
	}

	@Override
	public void read(IoSession session, ByteBuffer buffer) {
		while (buffer.hasRemaining()) {
			int opcode = buffer.get() & 0xFF;
			if (buffer.remaining() < 3) {
				queueBuffer(opcode);
				return;
			}
			switch (opcode) {
			case 0:
			case 1:
				int request = ByteBufferUtils.getTriByte(buffer);
				int container = request >> 16 & 0xFF;
				int archive = request & 0xFFFF;
				session.getJs5Queue().queue(container, archive, opcode == 1);
				break;
			case 2: // music
			case 3: // Music
				buffer.get();
				buffer.getShort();
				break;
			case 4:
				session.setJs5Encryption(buffer.get());
				if (buffer.getShort() != 0) {
					session.disconnect();
					return;
				}
				break;
			case 5:
			case 9:
				if (buffer.remaining() < 4) {
					queueBuffer(opcode);
					return;
				}
				buffer.getInt();
				break;
			case 6:
				ByteBufferUtils.getTriByte(buffer); // Value should be 3
				// buffer.getShort(); // Value should be 0
				break;
			case 7:
				buffer.get();
				buffer.getShort();
				session.disconnect();
				return;
			default:
				System.out.println("Unhandled JS5 opcode " + opcode + "!");
				buffer.get();
				buffer.getShort();
				break;
			}
		}
	}
}
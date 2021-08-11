package ms.net.event;

import java.nio.ByteBuffer;

import ms.net.IoReadEvent;
import ms.net.IoSession;
import ms.system.util.ManagementConstants;
import ms.world.WorldDatabase;
import ms.system.util.ByteBufferUtils;

/**
 * Handles handshake read events.
 * @author Emperor
 */
public final class HSReadEvent extends IoReadEvent {
	
	/**
	 * Constructs a new {@code HSReadEvent}.
	 * @param session The session.
	 * @param buffer The buffer.
	 */
	public HSReadEvent(IoSession session, ByteBuffer buffer) {
		super(session, buffer);
	}

	@Override
	public void read(IoSession session, ByteBuffer buffer) {
		int opcode = buffer.get() & 0xFF;
		switch (opcode) {
		case 88:
			String password = ByteBufferUtils.getString(buffer);
			if (!password.equals(ManagementConstants.getSECRET_KEY())) {
				System.out.println("Password mismatch (attempt=" + password + ")!");
				session.disconnect();
				break;
			}
			session.write(opcode);
			break;
		case 255: // World list
			int updateStamp = buffer.getInt();
			WorldDatabase.sendUpdate(session, updateStamp);
			break;
		default:
			System.err.println("Unhandled handshake opcode: " + opcode + ".");
			session.disconnect();
			break;
		}
	}
	
}
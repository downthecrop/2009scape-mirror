package org.crandor.net.event;

import org.crandor.net.Constants;
import org.crandor.net.IoReadEvent;
import org.crandor.net.IoSession;
import org.crandor.net.lobby.WorldList;
import org.crandor.net.registry.AccountRegister;
import org.crandor.tools.RandomFunction;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles handshake read events.
 * @author Emperor
 */
public final class HSReadEvent extends IoReadEvent {

	// debug
	static Map<String, Integer> count = new HashMap<>();

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
		Integer amount = count.get(session.getAddress());
		if (amount == null) {
			amount = 0;
		}
		count.put(session.getAddress(), amount + 1);
		int opcode = buffer.get() & 0xFF;
		switch (opcode) {
		case 14:
			session.setNameHash(buffer.get() & 0xFF);
			session.setServerKey(RandomFunction.RANDOM.nextLong());
			session.write(true);
			break;
		case 15:
			int revision = buffer.getInt();
			int sub_revision = buffer.getInt();
			buffer.flip();
			System.out.println(buffer.limit());
			if (revision != Constants.REVISION || sub_revision != Constants.CLIENT_BUILD) {
				session.disconnect();
				break;
			}
			session.write(false);
			break;
		case 147:
		case 186:
		case 36:
			AccountRegister.read(session, opcode, buffer);
			break;
		case 255: // World list
			int updateStamp = buffer.getInt();
			WorldList.sendUpdate(session, updateStamp);
			break;
		default:
			session.disconnect();
			break;
		}
	}

}
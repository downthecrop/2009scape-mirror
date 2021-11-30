package ms.net.event;

import java.nio.ByteBuffer;
import java.util.Locale;

import ms.Management;
import ms.net.IoReadEvent;
import ms.net.IoSession;
import ms.net.packet.IoBuffer;
import ms.system.util.ManagementConstants;
import ms.world.PlayerSession;
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
		case 35:
			IoBuffer buf = new IoBuffer();
			String username = ByteBufferUtils.getString(buffer).toLowerCase();
			password = ByteBufferUtils.getString(buffer);
			if (!password.equals(ManagementConstants.getSECRET_KEY())){
				System.out.println("Password mismatch (attempt=" + password + ")!");
				buf.put(1);
				ByteBuffer buff = buf.toByteBuffer();
				buff.flip();
				session.queue(buff);
				return;
			}

			PlayerSession player = WorldDatabase.getPlayer(username);
			if (player == null) {
				System.out.println("Player " + username + " was not registered!");
				buf.put(2);
				ByteBuffer buff = buf.toByteBuffer();
				buff.flip();
				session.queue(buff);
				return;
			}
			buf.put(0);
			ByteBuffer buff = buf.toByteBuffer();
			buff.flip();
			session.queue(buff);
			player.getWorld().getPlayers().remove(username);
			player.setWorldId(0);
			break;
		default:
			System.err.println("Unhandled handshake opcode: " + opcode + ".");
			session.disconnect();
			break;
		}
	}
	
}
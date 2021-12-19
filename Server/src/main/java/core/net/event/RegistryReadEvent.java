package core.net.event;

import rs09.game.system.SystemLogger;
import core.net.IoReadEvent;
import core.net.IoSession;
import core.net.producer.MSEventProducer;
import rs09.net.ms.ManagementServer;

import java.nio.ByteBuffer;

/**
 * Handles world registry read events.
 * @author Emperor
 */
public final class RegistryReadEvent extends IoReadEvent {

	/**
	 * The event producer.
	 */
	private static final MSEventProducer PRODUCER = new MSEventProducer();

	/**
	 * Constructs a new {@code RegistryReadEvent} {@code Object}.
	 * @param session The session.
	 * @param buffer The buffer to read.
	 */
	public RegistryReadEvent(IoSession session, ByteBuffer buffer) {
		super(session, buffer);
	}

	@Override
	public void read(IoSession session, ByteBuffer buffer) {
		int opcode = buffer.get() & 0xFF;
		switch (opcode) {
			case 0:
				SystemLogger.logErr("[MS] Registration Denied - World ID out of bounds.");
				ManagementServer.flagCantConnect();
				break;
			case 1:
				session.setProducer(PRODUCER);
				SystemLogger.logInfo("[MS] Successfully Registered to RS09 Management Server.");
				ManagementServer.flagConnected();
				break;
			case 2:
				SystemLogger.logErr("[MS] Registration Denied - World ID already in use.");
				ManagementServer.flagCantConnect();
				break;
			case 3:
				SystemLogger.logErr("[MS] Registration Denied - Internal error encountered by RS09 Management Server.");
				ManagementServer.flagCantConnect();
				break;
			default:
				System.out.println("??" + opcode);
				break;
		}
	}

}
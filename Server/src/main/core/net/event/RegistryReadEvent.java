package core.net.event;

import core.tools.Log;
import core.tools.SystemLogger;
import core.game.world.GameWorld;
import core.net.IoReadEvent;
import core.net.IoSession;
import core.net.amsc.ManagementServerState;
import core.net.amsc.WorldCommunicator;
import core.net.producer.MSEventProducer;

import java.nio.ByteBuffer;

import static core.api.ContentAPIKt.log;

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
			WorldCommunicator.setState(ManagementServerState.NOT_AVAILABLE);
			log(this.getClass(), Log.ERR,  "Failed registering world to AMS - [id=" + GameWorld.getSettings().getWorldId() + ", cause=World id out of bounds]!");
			break;
		case 1:
			session.setProducer(PRODUCER);
			WorldCommunicator.setState(ManagementServerState.AVAILABLE);
			break;
		case 2:
			case 3:
				WorldCommunicator.setState(ManagementServerState.NOT_AVAILABLE);
			break;
			default:

			break;
		}
	}

}
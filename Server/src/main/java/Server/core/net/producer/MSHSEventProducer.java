package core.net.producer;

import core.net.EventProducer;
import core.net.IoReadEvent;
import core.net.IoSession;
import core.net.IoWriteEvent;
import core.net.event.MSHSReadEvent;
import core.net.event.MSHSWriteEvent;

import java.nio.ByteBuffer;

/**
 * Handles the Management server handshake event producing.
 * @author Emperor
 */
public final class MSHSEventProducer implements EventProducer {

	@Override
	public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
		return new MSHSReadEvent(session, buffer);
	}

	@Override
	public IoWriteEvent produceWriter(IoSession session, Object context) {
		return new MSHSWriteEvent(session, context);
	}

}
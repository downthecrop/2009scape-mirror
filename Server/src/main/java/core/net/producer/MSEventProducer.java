package core.net.producer;

import core.net.EventProducer;
import core.net.IoReadEvent;
import core.net.IoSession;
import core.net.IoWriteEvent;
import core.net.event.MSReadEvent;
import core.net.event.MSWriteEvent;

import java.nio.ByteBuffer;

/**
 * Handles Management server events.
 * @author Emperor
 */
public final class MSEventProducer implements EventProducer {

	@Override
	public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
		return new MSReadEvent(session, buffer);
	}

	@Override
	public IoWriteEvent produceWriter(IoSession session, Object context) {
		return new MSWriteEvent(session, context);
	}

}
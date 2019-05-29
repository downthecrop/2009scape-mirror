package org.crandor.net.producer;

import org.crandor.net.EventProducer;
import org.crandor.net.IoReadEvent;
import org.crandor.net.IoSession;
import org.crandor.net.IoWriteEvent;
import org.crandor.net.event.HSReadEvent;
import org.crandor.net.event.HSWriteEvent;

import java.nio.ByteBuffer;

/**
 * Produces I/O events for the handshake protocol.
 * @author Emperor
 */
public final class HSEventProducer implements EventProducer {

	@Override
	public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
		return new HSReadEvent(session, buffer);
	}

	@Override
	public IoWriteEvent produceWriter(IoSession session, Object context) {
		return new HSWriteEvent(session, context);
	}

}
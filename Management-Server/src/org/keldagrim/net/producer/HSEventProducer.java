package org.keldagrim.net.producer;

import java.nio.ByteBuffer;

import org.keldagrim.net.EventProducer;
import org.keldagrim.net.IoReadEvent;
import org.keldagrim.net.IoSession;
import org.keldagrim.net.IoWriteEvent;
import org.keldagrim.net.event.HSReadEvent;
import org.keldagrim.net.event.HSWriteEvent;

/**
 * Produces I/O events for the handshake protocol.
 * @author Emperor
 * 
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
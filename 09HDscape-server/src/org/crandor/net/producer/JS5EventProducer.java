package org.crandor.net.producer;

import org.crandor.net.EventProducer;
import org.crandor.net.IoReadEvent;
import org.crandor.net.IoSession;
import org.crandor.net.IoWriteEvent;
import org.crandor.net.event.JS5ReadEvent;
import org.crandor.net.event.JS5WriteEvent;

import java.nio.ByteBuffer;

/**
 * Produces JS-5 I/O events.
 * @author Tyler
 * @author Emperor
 */
public class JS5EventProducer implements EventProducer {

	@Override
	public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
		return new JS5ReadEvent(session, buffer);
	}

	@Override
	public IoWriteEvent produceWriter(IoSession session, Object context) {
		return new JS5WriteEvent(session, context);
	}

}

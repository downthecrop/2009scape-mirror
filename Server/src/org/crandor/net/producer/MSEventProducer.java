package org.crandor.net.producer;

import org.crandor.net.EventProducer;
import org.crandor.net.IoReadEvent;
import org.crandor.net.IoSession;
import org.crandor.net.IoWriteEvent;
import org.crandor.net.event.MSReadEvent;
import org.crandor.net.event.MSWriteEvent;

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
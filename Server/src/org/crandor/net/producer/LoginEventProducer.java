package org.crandor.net.producer;

import org.crandor.net.EventProducer;
import org.crandor.net.IoReadEvent;
import org.crandor.net.IoSession;
import org.crandor.net.IoWriteEvent;
import org.crandor.net.event.LoginReadEvent;
import org.crandor.net.event.LoginWriteEvent;

import java.nio.ByteBuffer;

/**
 * Produces login I/O events.
 * @author Emperor
 */
public final class LoginEventProducer implements EventProducer {

	@Override
	public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
		return new LoginReadEvent(session, buffer);
	}

	@Override
	public IoWriteEvent produceWriter(IoSession session, Object context) {
		return new LoginWriteEvent(session, context);
	}

}
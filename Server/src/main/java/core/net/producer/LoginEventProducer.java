package core.net.producer;

import core.net.EventProducer;
import core.net.IoReadEvent;
import core.net.IoSession;
import core.net.IoWriteEvent;
import core.net.event.LoginWriteEvent;
import rs09.net.event.LoginReadEvent;

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
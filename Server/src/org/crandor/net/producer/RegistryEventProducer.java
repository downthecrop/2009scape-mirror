package org.crandor.net.producer;

import org.crandor.net.EventProducer;
import org.crandor.net.IoReadEvent;
import org.crandor.net.IoSession;
import org.crandor.net.IoWriteEvent;
import org.crandor.net.event.RegistryReadEvent;
import org.crandor.net.event.RegistryWriteEvent;

import java.nio.ByteBuffer;

/**
 * Handles world server registry.
 * @author Emperor
 */
public final class RegistryEventProducer implements EventProducer {

	@Override
	public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
		return new RegistryReadEvent(session, buffer);
	}

	@Override
	public IoWriteEvent produceWriter(IoSession session, Object context) {
		return new RegistryWriteEvent(session, context);
	}

}
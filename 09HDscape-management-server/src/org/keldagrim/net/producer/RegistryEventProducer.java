package org.keldagrim.net.producer;

import java.nio.ByteBuffer;

import org.keldagrim.net.EventProducer;
import org.keldagrim.net.IoReadEvent;
import org.keldagrim.net.IoSession;
import org.keldagrim.net.IoWriteEvent;
import org.keldagrim.net.event.RegistryReadEvent;
import org.keldagrim.net.event.RegistryWriteEvent;

/**
 * Handles world server registry.
 * @author Emperor
 *
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
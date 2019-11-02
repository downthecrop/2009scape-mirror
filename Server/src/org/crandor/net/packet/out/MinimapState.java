package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.MinimapStateContext;

/**
 * Handles the sending of the minimap state outgoing packet.
 * @author Emperor
 */
public final class MinimapState implements OutgoingPacket<MinimapStateContext> {

	@Override
	public void send(MinimapStateContext context) {
		IoBuffer buffer = new IoBuffer(192).put(context.getState());
		context.getPlayer().getDetails().getSession().write(buffer);
	}

}
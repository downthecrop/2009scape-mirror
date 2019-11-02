package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.PacketHeader;
import org.crandor.net.packet.context.InteractionOptionContext;

/**
 * Handles the interaction option changed outgoing packet.
 * @author Emperor
 */
public final class InteractionOption implements OutgoingPacket<InteractionOptionContext> {

	@Override
	public void send(InteractionOptionContext context) {
		IoBuffer buffer = new IoBuffer(44, PacketHeader.BYTE);
		buffer.putLEShortA(-1);
		buffer.put(context.getIndex() == 0 ? 1 : 0);
		buffer.put(context.getIndex() + 1);
		buffer.putString(context.getName());
		context.getPlayer().getSession().write(buffer);
	}

}
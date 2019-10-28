package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.PacketHeader;
import org.crandor.net.packet.context.StringContext;

/**
 * The outgoing set component string packet.
 * @author Emperor
 */
public class StringPacket implements OutgoingPacket<StringContext> {

	@Override
	public void send(StringContext context) {
		IoBuffer buffer = new IoBuffer(171, PacketHeader.SHORT);
		buffer.putIntB((context.getInterfaceId() << 16) | context.getLineId());
		buffer.putString(context.getString());
		buffer.putShortA(context.getPlayer().getInterfaceManager().getPacketCount(1));
		context.getPlayer().getDetails().getSession().write(buffer);
	}
}

package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;
import core.net.packet.context.StringContext;

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
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getDetails().getSession().write(buffer);
	}
}

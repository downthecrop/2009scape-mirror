package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.ChildPositionContext;

/**
 * Handles the "reposition interface child" outgoing packet.
 * @author Emperor
 */
public final class RepositionChild implements OutgoingPacket<ChildPositionContext> {

	@Override
	public void send(ChildPositionContext context) {
		IoBuffer buffer = new IoBuffer(119)
				.putShortA(context.getPlayer().getInterfaceManager().getPacketCount(1))
				.putLEInt(context.getInterfaceId() << 16 | context.getChildId())
				.putShort(context.getPosition().x)
				.putShortA(context.getPosition().y);
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getSession().write(buffer);
	}

}
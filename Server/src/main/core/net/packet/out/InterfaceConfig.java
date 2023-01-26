package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.InterfaceConfigContext;

/**
 * The outgoing interface configuration packet.
 * @author Emperor
 */
public class InterfaceConfig implements OutgoingPacket<InterfaceConfigContext> {

	@Override
	public void send(InterfaceConfigContext context) {
		IoBuffer buffer = new IoBuffer(21);
		buffer.putC(context.isHidden() ? 1 : 0);
		buffer.putShort(context.getPlayer().getInterfaceManager().getPacketCount(1));
		buffer.putLEInt(context.getInterfaceId() << 16 | context.getChildId());
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getSession().write(buffer);
	}
}

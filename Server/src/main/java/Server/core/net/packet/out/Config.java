package core.net.packet.out;

import core.net.packet.context.ConfigContext;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;

/**
 * The config outgoing packet.
 * @author Emperor
 */
public class Config implements OutgoingPacket<ConfigContext> {

	@Override
	public void send(ConfigContext context) {
		IoBuffer buffer;
		if (context.getValue() < Byte.MIN_VALUE || context.getValue() > Byte.MAX_VALUE) {
			buffer = new IoBuffer(226);
			buffer.putInt(context.getValue());
			buffer.putShortA(context.getId());
		} else {
			buffer = new IoBuffer(60);
			buffer.putShortA(context.getId());
			buffer.putC(context.getValue());
		}
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());if(!context.getPlayer().isArtificial()) {
			context.getPlayer().getDetails().getSession().write(buffer);
		}
	}
}
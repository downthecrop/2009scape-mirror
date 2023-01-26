package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.PlayerContext;

/**
 * Handles the removal of the minimap flag.
 * @author Emperor
 */
public final class ClearMinimapFlag implements OutgoingPacket<PlayerContext> {

	@Override
	public void send(PlayerContext context) {
		IoBuffer buffer = new IoBuffer(153);
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getDetails().getSession().write(buffer);
	}

}
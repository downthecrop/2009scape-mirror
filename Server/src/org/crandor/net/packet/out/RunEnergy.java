package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.PlayerContext;

/**
 * The run energy outgoing packet.
 * @author Emperor
 */
public class RunEnergy implements OutgoingPacket<PlayerContext> {

	@Override
	public void send(PlayerContext context) {
		IoBuffer buffer = new IoBuffer(234);
		buffer.put((byte) context.getPlayer().getSettings().getRunEnergy());
		context.getPlayer().getDetails().getSession().write(buffer);
	}
}
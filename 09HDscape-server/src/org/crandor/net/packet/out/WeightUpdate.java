package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.PlayerContext;

/**
 * Handles the outgoing weight update packet.
 * @author Emperor
 */
public final class WeightUpdate implements OutgoingPacket<PlayerContext> {

	@Override
	public void send(PlayerContext context) {
		IoBuffer buffer = new IoBuffer(174);
		buffer.putShort((int) context.getPlayer().getSettings().getWeight());
		// TODO context.getPlayer().getSession().write(buffer);
	}

}
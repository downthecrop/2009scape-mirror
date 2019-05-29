package org.crandor.net.packet.out;

import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.WalkOptionContext;

/**
 * Handles the sending of the "Set walk-to option" packet.
 * @author Emperor
 */
public final class SetWalkOption implements OutgoingPacket<WalkOptionContext> {

	@Override
	public void send(WalkOptionContext context) {
		// TODO IoBuffer buffer = new IoBuffer(10,
		// PacketHeader.BYTE).putString(context.getOption());
		// context.getPlayer().getDetails().getSession().write(buffer);
	}

}
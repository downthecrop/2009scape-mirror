package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.PacketHeader;
import org.crandor.net.packet.context.GameMessageContext;

/**
 * The game message outgoing packet.
 * @author Emperor
 */
public class GameMessage implements OutgoingPacket<GameMessageContext> {

	@Override
	public void send(GameMessageContext context) {
		IoBuffer buffer = new IoBuffer(70, PacketHeader.BYTE);
		buffer.putString(context.getMessage());
		context.getPlayer().getSession().write(buffer);
	}
}
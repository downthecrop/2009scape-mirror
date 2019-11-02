package org.crandor.net.packet.in;

import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;

/**
 * Handles an incoming client focus changed packet.
 * @author Emperor
 */
public final class ClientFocusPacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		if (player != null) {
			player.getMonitor().setClientFocus(buffer.get() == 1);
		}
	}

}
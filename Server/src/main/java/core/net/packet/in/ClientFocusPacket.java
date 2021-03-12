package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;

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
package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;

/**
 * Handles an incoming ping packet.
 * @author Emperor
 */
public final class PingPacketHandler implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		
	}

}
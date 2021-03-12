package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;

/**
 * Handles the update interface packet counter packet.
 * @author Emperor
 *
 */
public final class UpdateInterfaceCounter implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		int count = buffer.getShort() - player.getInterfaceManager().getPacketCount(0);
		player.getInterfaceManager().getPacketCount(count);
	}

}
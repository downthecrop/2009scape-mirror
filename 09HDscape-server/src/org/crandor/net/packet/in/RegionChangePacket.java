package org.crandor.net.packet.in;

import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;

/**
 * Packet received when a player's region has changed.
 * @author Emperor
 * @author 'Vexia
 */
public class RegionChangePacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		//TODO: no data is sen't so not sure what to do.
	}

}

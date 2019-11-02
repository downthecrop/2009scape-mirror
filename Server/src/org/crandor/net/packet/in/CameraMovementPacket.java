package org.crandor.net.packet.in;

import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;

/**
 * Handles an incoming camera movement changed packet.
 * @author Emperor
 */
public final class CameraMovementPacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		buffer.getShortA();
		buffer.getLEShort();
	}

}
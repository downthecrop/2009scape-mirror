package org.crandor.net.packet.in;

import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;

/**
 * Handles the display update packet.
 * @author Emperor
 *
 */
public class DisplayUpdatePacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		int windowMode = buffer.get(); //Window mode
		int screenWidth = buffer.getShort();
		int screenHeight = buffer.getShort();
		int displayMode = buffer.get(); //Display mode
		player.getSession().getClientInfo().setScreenWidth(screenWidth);
		player.getSession().getClientInfo().setScreenHeight(screenHeight);
		player.getSession().getClientInfo().setDisplayMode(displayMode);
		player.getInterfaceManager().switchWindowMode(windowMode);
	}

}
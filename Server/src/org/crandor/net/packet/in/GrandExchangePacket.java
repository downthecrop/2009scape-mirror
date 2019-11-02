package org.crandor.net.packet.in;

import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;

/**
 * Represents the <b>Incoming</b> packet of the grand exchange.
 * @author Emperor
 */
public class GrandExchangePacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		int itemId = buffer.getShort();
		player.getGrandExchange().constructBuy(itemId);
		player.getInterfaceManager().closeChatbox();
	}

}

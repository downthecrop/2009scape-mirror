package org.crandor.net.packet.in;

import org.crandor.game.node.entity.player.Player;
import org.crandor.net.amsc.MSPacketRepository;
import org.crandor.net.amsc.WorldCommunicator;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;

/**
 * Handles an incoming chat settings update packet.
 * @author Emperor
 */
public final class ChatSettingsPacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		int publicSetting = buffer.get();
		int privateSetting = buffer.get();
		int tradeSetting = buffer.get();
		if (WorldCommunicator.isEnabled()) {
			MSPacketRepository.sendChatSetting(player, publicSetting, privateSetting, tradeSetting);
		}
		player.getSettings().updateChatSettings(publicSetting, privateSetting, tradeSetting);
	}

}
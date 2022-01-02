package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.net.amsc.MSPacketRepository;
import core.net.amsc.WorldCommunicator;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;

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
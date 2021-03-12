package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.game.system.communication.CommunicationInfo;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;
import core.tools.StringUtils;

/**
 * Represents the packet used to handle all incoming packets related to
 * communication.
 * @author 'Vexia
 */
public final class CommunicationPacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		switch (buffer.opcode()) {
		case 120:
			CommunicationInfo.add(player, StringUtils.longToString(buffer.getLong()));
			break;
		case 57:
			CommunicationInfo.remove(player, StringUtils.longToString(buffer.getLong()), false);
			break;
		case 34:
			CommunicationInfo.block(player, StringUtils.longToString(buffer.getLong()));
			break;
		case 213:
			CommunicationInfo.remove(player, StringUtils.longToString(buffer.getLong()), true);
			break;
		case 201:
			String name = StringUtils.longToString(buffer.getLong());
			String message = StringUtils.decryptPlayerChat(buffer, buffer.get() & 0xFF);
			if (player.getDetails().isMuted()) {
				player.getPacketDispatch().sendMessage("You have been " + (player.getDetails().isPermMute() ? "permanently" : "temporarily") + " muted due to breaking a rule.");
				return;
			}
			CommunicationInfo.sendMessage(player, name, message);
			break;
		}
	}

}

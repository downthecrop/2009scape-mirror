package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.game.system.communication.ClanRank;
import core.net.ms.MSPacketRepository;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;
import core.tools.StringUtils;

/**
 * Handles incoming clan packets.
 * @author Emperor
 */
public class ClanPacketHandler implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		switch (buffer.opcode()) {
		case 104:
			long nameLong = buffer.getLong();
			String name = StringUtils.longToString(nameLong);
			if (nameLong != 0l) {
				player.getPacketDispatch().sendMessage("Attempting to join channel...:clan:");
			}
			MSPacketRepository.sendJoinClan(player, name);
			break;
		case 188:
			int rank = buffer.getA();
			name = StringUtils.longToString(buffer.getLong());
			MSPacketRepository.sendContactUpdate(player.getName(), name, false, false, ClanRank.values()[rank + 1]);
			break;
		case 162:
			name = StringUtils.longToString(buffer.getLong());
			MSPacketRepository.sendClanKick(player.getName(), name);
			break;
		}
	}

}
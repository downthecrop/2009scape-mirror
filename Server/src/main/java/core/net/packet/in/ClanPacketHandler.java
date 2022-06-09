package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.game.system.communication.ClanRank;
import core.game.system.communication.ClanRepository;
import core.game.system.communication.CommunicationInfo;
import proto.management.JoinClanRequest;
import rs09.game.world.repository.Repository;
import core.net.amsc.MSPacketRepository;
import core.net.amsc.WorldCommunicator;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;
import core.tools.StringUtils;
import rs09.worker.ManagementEvents;

/**
 * Handles incoming clan packets.
 * @author Emperor
 */
public class ClanPacketHandler implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		switch (buffer.opcode()) {
		case 104:
			if (player.getCommunication().getClan() != null) {
				player.getCommunication().getClan().leave(player, true);
				player.getCommunication().setClan(null);
				return;
			}
			long nameLong = buffer.getLong();
			String name = StringUtils.longToString(nameLong);
			if (nameLong != 0L) {
				player.getPacketDispatch().sendMessage("Attempting to join channel...:clan:");
			}
			JoinClanRequest.Builder builder = JoinClanRequest.newBuilder();
			builder.setClanName(name);
			builder.setUsername(player.getName());
			ManagementEvents.publish(builder.build());
			break;
		case 188:
			int rank = buffer.getA();
			name = StringUtils.longToString(buffer.getLong());
			if (WorldCommunicator.isEnabled()) {
				MSPacketRepository.sendContactUpdate(player.getName(), name, false, false, ClanRank.values()[rank + 1]);
				break;
			}
			CommunicationInfo.updateClanRank(player, name, ClanRank.values()[rank + 1]);
			break;
		case 162:
			name = StringUtils.longToString(buffer.getLong());
			if (WorldCommunicator.isEnabled()) {
				MSPacketRepository.sendClanKick(player.getName(), name);
				break;
			}
			ClanRepository clan = player.getCommunication().getClan();
			Player target = Repository.getPlayerByName(name);
			if (clan == null || target == null) {
				break;
			}
			clan.kick(player, target);
			break;
		}
	}

}
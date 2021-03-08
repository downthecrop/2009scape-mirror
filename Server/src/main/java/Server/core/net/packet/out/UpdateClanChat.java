package core.net.packet.out;

import core.game.system.communication.ClanEntry;
import core.game.system.communication.ClanRepository;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;
import core.net.packet.context.ClanContext;
import core.tools.StringUtils;

/**
 * Handles the update clan chat outgoing packet.
 * @author Emperor
 */
public final class UpdateClanChat implements OutgoingPacket<ClanContext> {

	@Override
	public void send(ClanContext context) {
		IoBuffer buffer = new IoBuffer(55, PacketHeader.SHORT);
		ClanRepository clan = context.getClan();
		if (!context.isLeave()) {
			buffer.putLong(StringUtils.stringToLong(clan.getOwner()));
			buffer.putLong(StringUtils.stringToLong(clan.getName()));
			buffer.put(clan.getKickRequirement().getValue());
			int size = clan.getPlayers().size();
			if (size > 100) {
				size = 100;
			}
			buffer.put(size);
			for (int i = 0; i < size; i++) {
				ClanEntry entry = clan.getPlayers().get(i);
				String name = entry.getName();
				buffer.putLong(StringUtils.stringToLong(name)).putShort(entry.getWorldId());
				buffer.put(clan.getRank(entry).getValue());
				buffer.putString("World " + entry.getWorldId());
			}
		} else {
			buffer.putLong(0);
		}
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getSession().write(buffer);
	}

}
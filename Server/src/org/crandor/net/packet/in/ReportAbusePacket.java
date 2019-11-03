package org.crandor.net.packet.in;

import org.crandor.ServerConstants;
import org.crandor.game.content.global.report.AbuseReport;
import org.crandor.game.content.global.report.Rule;
import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.IncomingPacket;
import org.crandor.net.packet.IoBuffer;
import org.crandor.tools.StringUtils;

import java.io.File;

/**
 * Represents the incoming packet to handle a report against a player.
 * @author Vexia
 */
public class ReportAbusePacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		String target = StringUtils.longToString(buffer.getLong());
		Rule rule = Rule.forId(buffer.get());
		boolean mute = buffer.get() == 1;
		File file = new File(ServerConstants.PLAYER_SAVE_PATH + target + ".save");
		if (!file.exists()) {
			player.getPacketDispatch().sendMessage("Invalid player name.");
			return;
		}
		if (target.equalsIgnoreCase(player.getUsername())) {
			player.getPacketDispatch().sendMessage("You can't report yourself!");
			return;
		}
		AbuseReport abuse = new AbuseReport(player.getName(), target, rule);
		abuse.construct(player, mute);
	}
}

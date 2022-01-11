package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.game.system.monitor.PlayerMonitor;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.ChatMessage;
import core.game.world.update.flag.player.ChatFlag;
import core.net.amsc.MSPacketRepository;
import core.net.amsc.WorldCommunicator;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;
import core.tools.StringUtils;

/**
 * Represents the incoming chat packet.
 * @author Emperor
 */
public class ChatPacket implements IncomingPacket {

	@Override
	public void decode(final Player player, int opcode, IoBuffer buffer) {
		try {
			final int effects = buffer.getShort();
			final int numChars = buffer.getSmart();
			final String message = StringUtils.decryptPlayerChat(buffer, numChars);
			if (player.getDetails().isMuted()) {
				player.getPacketDispatch().sendMessage("You have been " + (player.getDetails().isPermMute() ? "permanently" : "temporarily") + " muted due to breaking a rule.");
				return;
			}
			if (message.startsWith("/") && player.getCommunication().getClan() != null) {
				StringBuilder sb = new StringBuilder(message);
				sb.append(" => ").append(player.getName()).append(" (owned by ").append(player.getCommunication().getClan().getOwner()).append(")");
				String m = sb.toString();
				player.getMonitor().log(m.replace(m.charAt(0), ' ').trim(), PlayerMonitor.CLAN_CHAT_LOG);
				if (WorldCommunicator.isEnabled()) {
					MSPacketRepository.sendClanMessage(player, message.substring(1));
				} else {
					player.getCommunication().getClan().message(player, message.substring(1));
				}
				return;
			}
			player.getMonitor().log(message, PlayerMonitor.PUBLIC_CHAT_LOG);
			ChatMessage ctx = new ChatMessage(player, message, effects, numChars);
			GameWorld.getPulser().submit(new Pulse(0, player) {
				@Override
				public boolean pulse() {
					player.getUpdateMasks().register(new ChatFlag(ctx));
					return true;
				}
			});
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
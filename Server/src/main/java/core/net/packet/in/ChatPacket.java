package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.game.system.monitor.PlayerMonitor;
import core.game.system.task.Pulse;
import proto.management.ClanMessage;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.ChatMessage;
import core.game.world.update.flag.player.ChatFlag;
import core.net.amsc.MSPacketRepository;
import core.net.amsc.WorldCommunicator;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;
import core.tools.StringUtils;
import rs09.worker.ManagementEvents;

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
				ClanMessage.Builder builder = ClanMessage.newBuilder();
				builder.setSender(player.getName());
				builder.setClanName(player.getCommunication().getClan().getOwner().toLowerCase().replace(" ", "_"));
				builder.setMessage(message.substring(1));
				builder.setRank(player.getDetails().getRights().ordinal());
				ManagementEvents.publish(builder.build());
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
package core.net.packet.in;

import core.game.node.entity.player.Player;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;

/**
 * Handles music-related incoming packets.
 * @author Ceikry
 */
public final class MusicPacketHandler implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		int musicId = buffer.getLEShortA();
		if (player.getMusicPlayer().isLooping()) {
			player.getMusicPlayer().replay();
			return;
		}
		player.getMusicPlayer().setPlaying(false);
	}

}
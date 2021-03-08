package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.PlayerContext;

import java.nio.ByteBuffer;

/**
 * Handles the login outgoing packet.
 * @author Emperor
 */
public final class LoginPacket implements OutgoingPacket<PlayerContext> {

	@Override
	public void send(PlayerContext context) {
		Player p = context.getPlayer();
		ByteBuffer buffer = ByteBuffer.allocate(9);
		int right = context.getPlayer().getDetails().getRights() == Rights.PLAYER_MODERATOR ? 1 : context.getPlayer().getDetails().getRights() == Rights.ADMINISTRATOR ? 2 : 0;
		buffer.put((byte) (right));
		buffer.put((byte) 0); // Something with client scripts, maybe login //
		// screen?
		buffer.put((byte) 0); // No idea.
		buffer.put((byte) 0); // No idea.
		buffer.put((byte) 1); // Boolean, possibly members.
		buffer.putShort((short) p.getIndex());
		buffer.put((byte) 1); // No idea. (something with client scripts, again)
		p.getDetails().getSession().write(buffer);
	}

}
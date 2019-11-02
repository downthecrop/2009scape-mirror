package org.crandor.net.packet.out;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.BuildObjectContext;

/**
 * The construct game object packet.
 * @author Emperor
 */
public final class ConstructObject implements OutgoingPacket<BuildObjectContext> {

	/**
	 * Writes the packet.
	 * @param buffer The buffer.
	 * @param objects The objects.
	 */
	public static IoBuffer write(IoBuffer buffer, GameObject object) {
		Location l = object.getLocation();
		buffer.put(179).putA((object.getType() << 2) | (object.getRotation() & 0x3)).put((l.getChunkOffsetX() << 4) | (l.getChunkOffsetY() & 0x7)).putShortA(object.getId());
		return buffer;
	}

	@Override
	public void send(BuildObjectContext context) {
		Player player = context.getPlayer();
		GameObject o = context.getGameObject();
		IoBuffer buffer = write(UpdateAreaPosition.getBuffer(player, o.getLocation().getChunkBase()), o);
		player.getSession().write(buffer);

	}

}
package org.crandor.net.packet.out;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.BuildItemContext;

/**
 * Represents the outgoing packet of clearing ground items.
 * @author Emperor
 */
public final class ClearGroundItem implements OutgoingPacket<BuildItemContext> {

	/**
	 * Writes the packet.
	 * @param buffer The buffer.
	 * @param item The item.
	 */
	public static IoBuffer write(IoBuffer buffer, Item item) {
		Location l = item.getLocation();
		buffer.put(240);
		buffer.putS((l.getChunkOffsetX() << 4) | (l.getChunkOffsetY() & 0x7)).putShort(item.getId());
		return buffer;
	}

	@Override
	public void send(BuildItemContext context) {
		Player player = context.getPlayer();
		Item item = context.getItem();
		IoBuffer buffer = write(UpdateAreaPosition.getBuffer(player, item.getLocation().getChunkBase()), item);
		player.getSession().write(buffer);
	}
}
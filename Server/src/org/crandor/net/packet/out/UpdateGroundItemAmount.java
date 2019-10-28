package org.crandor.net.packet.out;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.BuildItemContext;

/**
 * Updates the ground item amount.
 * @author Emperor
 */
public final class UpdateGroundItemAmount implements OutgoingPacket<BuildItemContext> {

	/**
	 * Writes the packet.
	 * @param buffer The buffer.
	 * @param item The item.
	 */
	public static IoBuffer write(IoBuffer buffer, Item item, int oldAmount) {
		Location l = item.getLocation();
		buffer.put(14).put((l.getChunkOffsetX() << 4) | (l.getChunkOffsetY() & 0x7)).putShort(item.getId()).putShort(oldAmount).putShort(item.getAmount());
		return buffer;
	}

	@Override
	public void send(BuildItemContext context) {
		Player player = context.getPlayer();
		Item item = context.getItem();
		IoBuffer buffer = write(UpdateAreaPosition.getBuffer(player, item.getLocation().getChunkBase()), item, context.getOldAmount());
		player.getDetails().getSession().write(buffer);
	}
}
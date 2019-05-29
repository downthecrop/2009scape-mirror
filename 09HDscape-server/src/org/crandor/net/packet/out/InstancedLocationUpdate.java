package org.crandor.net.packet.out;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.LocationContext;

/**
 * Outgoing packet used for updating a player's location solely on his own
 * client.
 * @author Emperor
 */
public final class InstancedLocationUpdate implements OutgoingPacket<LocationContext> {

	@Override
	public void send(LocationContext context) {
		IoBuffer buffer = new IoBuffer(110);
		Location l = context.getLocation();
		Player player = context.getPlayer();
		int flag = l.getZ() << 1;
		if (context.isTeleport()) {
			flag |= 0x1;
		}
		buffer.putS(flag);
		buffer.put(l.getSceneX(player.getPlayerFlags().getLastSceneGraph()));
		buffer.putA(l.getSceneY(player.getPlayerFlags().getLastSceneGraph()));
		// TODO player.getSession().write(buffer);
	}

}
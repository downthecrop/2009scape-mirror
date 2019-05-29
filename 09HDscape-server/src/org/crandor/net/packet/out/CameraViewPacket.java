package org.crandor.net.packet.out;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.CameraContext;

/**
 * Handles the outgoing camera view packets.
 * @author Emperor
 */
public final class CameraViewPacket implements OutgoingPacket<CameraContext> {

	@Override
	public void send(CameraContext context) {
		CameraContext.CameraType type = context.getType();
		IoBuffer buffer = new IoBuffer(type.opcode());
		Location l = Location.create(context.getX(), context.getY(), 0);
		Player p = context.getPlayer();
		switch (type) {
		case ROTATION:
		case POSITION:
			buffer.putShort(context.getPlayer().getInterfaceManager().getPacketCount(1));
			int x = l.getSceneX(p.getPlayerFlags().getLastSceneGraph());
			int y = l.getSceneY(p.getPlayerFlags().getLastSceneGraph());
			buffer.put(x).put(y).putShort(context.getHeight()).put(context.getSpeed()).put(context.getZoomSpeed());
			break;
		case SET:
			buffer.putLEShort(context.getX())
			.putShort(context.getPlayer().getInterfaceManager().getPacketCount(1)).putShort(context.getY());
			break;
		case SHAKE:
			buffer.putShort(context.getPlayer().getInterfaceManager().getPacketCount(1));
			buffer.put(l.getX()).put(l.getY()).put(context.getSpeed()).put(context.getZoomSpeed()).putShort(context.getHeight());
			break;
		case RESET:
			buffer.putShort(context.getPlayer().getInterfaceManager().getPacketCount(1));
			break;
		}
		p.getSession().write(buffer);
	}

}

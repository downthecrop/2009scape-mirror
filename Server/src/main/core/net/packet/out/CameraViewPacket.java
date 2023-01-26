package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.CameraContext;

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
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());p.getSession().write(buffer);
	}

}

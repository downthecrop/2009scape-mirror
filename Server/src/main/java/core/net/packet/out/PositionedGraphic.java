package core.net.packet.out;

import core.game.world.map.Location;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.PositionedGraphicContext;

/**
 * The positioned graphic outgoing packet.
 * @author Emperor
 */
public final class PositionedGraphic implements OutgoingPacket<PositionedGraphicContext> {

	@Override
	public void send(PositionedGraphicContext context) {
		Location l = context.getLocation();
		Graphics g = context.getGraphic();
		IoBuffer buffer = UpdateAreaPosition.getBuffer(context.getPlayer(), l).put(17).put((l.getChunkOffsetX() << 4) | (l.getChunkOffsetY() & 0x7)).putShort(g.getId()).put(g.getHeight()).putShort(g.getDelay());
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getSession().write(buffer);
	}

}

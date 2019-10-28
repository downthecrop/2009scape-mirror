package org.crandor.net.packet.out;

import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.PositionedGraphicContext;

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
		context.getPlayer().getSession().write(buffer);
	}

}

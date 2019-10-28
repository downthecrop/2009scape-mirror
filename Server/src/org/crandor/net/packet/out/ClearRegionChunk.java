package org.crandor.net.packet.out;

import org.crandor.game.world.map.Location;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.ClearChunkContext;

/**
 * Handles the clear region chunk outgoing packet.
 * @author Emperor
 */
public final class ClearRegionChunk implements OutgoingPacket<ClearChunkContext> {

	@Override
	public void send(ClearChunkContext context) {
		Location l = context.getPlayer().getPlayerFlags().getLastSceneGraph();
		int x = context.getChunk().getCurrentBase().getSceneX(l);
		int y = context.getChunk().getCurrentBase().getSceneY(l);
		if (x >= 0 && y >= 0 && x < 96 && y < 96) {
			IoBuffer buffer = new IoBuffer(112).put(x).putC(y);
			context.getPlayer().getSession().write(buffer);
		}
	}

}

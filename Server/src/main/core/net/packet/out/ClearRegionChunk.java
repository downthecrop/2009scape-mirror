package core.net.packet.out;

import core.game.world.map.Location;
import core.game.world.map.RegionChunk;
import core.game.world.update.MapChunkRenderer;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.ClearChunkContext;

/**
 * Handles the clear region chunk outgoing packet.
 * @author Emperor
 */
public final class ClearRegionChunk implements OutgoingPacket<ClearChunkContext> {
	static final int BUILD_AREA_DEPTH_IN_TILES = MapChunkRenderer.BUILD_AREA_SIZE * RegionChunk.SIZE;

	@Override
	public void send(ClearChunkContext context) {
		Location l = context.getPlayer().getPlayerFlags().getLastSceneGraph();
		int x = context.getChunk().getCurrentBase().getSceneX(l);
		int y = context.getChunk().getCurrentBase().getSceneY(l);
		if (x >= 0 && y >= 0 && x < BUILD_AREA_DEPTH_IN_TILES && y < BUILD_AREA_DEPTH_IN_TILES) {
			IoBuffer buffer = new IoBuffer(112).put(x).putC(y);
			buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());
			context.getPlayer().getSession().write(buffer);
		}
	}
}

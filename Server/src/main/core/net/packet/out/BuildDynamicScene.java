package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.game.world.map.Region;
import core.game.world.map.RegionChunk;
import core.game.world.map.RegionManager;
import core.game.world.map.build.DynamicRegion;
import core.game.world.update.MapChunkRenderer;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;
import core.net.packet.context.DynamicSceneContext;
import core.game.system.config.XteaParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emperor
 */
public final class BuildDynamicScene implements OutgoingPacket<DynamicSceneContext> {

	@Override
	public void send(DynamicSceneContext context) {
		IoBuffer buffer = new IoBuffer(214, PacketHeader.SHORT);
		List<Integer> regionIds = new ArrayList<>(20);
		Player player = context.getPlayer();
		buffer.putLEShortA(player.getLocation().getSceneX());
		buffer.putLEShortA(player.getLocation().getRegionX());
		buffer.putS(player.getLocation().getZ());
		buffer.putLEShortA(player.getLocation().getSceneY());
		buffer.setBitAccess();
		RegionChunk[][][] chunks = new RegionChunk[MapChunkRenderer.BUILD_AREA_SIZE][MapChunkRenderer.BUILD_AREA_SIZE][Region.PLANES];
		int baseX = player.getLocation().getRegionX() - 6;
		int baseY = player.getLocation().getRegionY() - 6;
		for (int x = baseX; x <= player.getLocation().getRegionX() + 6; x++) {
			for (int y = baseY; y <= player.getLocation().getRegionY() + 6; y++) {
				for (int z = 0; z < Region.PLANES; z++) {
					Region r = RegionManager.forId((x >> 3) << 8 | (y >> 3));
					if (r instanceof DynamicRegion) {
						chunks[x - baseX][y - baseY][z] = r.getChunks()[x - (r.getX() << 3)][y - (r.getY() << 3)][z];
					}
				}
			}
		}
		for (int plane = 0; plane < Region.PLANES; plane++) {
			for (int offsetX = 0; offsetX < MapChunkRenderer.BUILD_AREA_SIZE; offsetX++) {
				for (int offsetY = 0; offsetY < MapChunkRenderer.BUILD_AREA_SIZE; offsetY++) {
					RegionChunk c = chunks[offsetX][offsetY][plane];
					if (c == null || c.getBase().getX() < 0 || c.getBase().getY() < 0) {
						buffer.putBits(1, 0);
						continue;
					}
					int realRegionX = c.getBase().getRegionX();
					int realRegionY = c.getBase().getRegionY();
					int realPlane = c.getBase().getZ();
					int rotation = c.getRotation();
					int id = (realRegionX >> 3) << 8 | (realRegionY >> 3);
					if (!regionIds.contains(id)) {
						regionIds.add(id);
					}
					buffer.putBits(1, 1);
					buffer.putBits(26, (rotation << 1) | (realPlane << 24) | (realRegionX << 14) | (realRegionY << 3));
				}
			}
		}
		buffer.setByteAccess();
		for (int id : regionIds) {
			int[] keys = XteaParser.Companion.getRegionXTEA(id);
			buffer.putIntB(keys[0]).putIntB(keys[1]).putIntB(keys[2]).putIntB(keys[3]);
		}
		buffer.putShort(player.getLocation().getRegionY());
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getSession().write(buffer);
		player.getPlayerFlags().setLastSceneGraph(player.getLocation());
	}
}
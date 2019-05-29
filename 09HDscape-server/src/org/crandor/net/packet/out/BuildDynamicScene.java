package org.crandor.net.packet.out;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.mysql.impl.RegionSQLHandler;
import org.crandor.game.world.map.Region;
import org.crandor.game.world.map.RegionChunk;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.build.DynamicRegion;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.PacketHeader;
import org.crandor.net.packet.context.DynamicSceneContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emperor
 */
public final class BuildDynamicScene implements OutgoingPacket<DynamicSceneContext> {

	@Override
	public void send(DynamicSceneContext context) {
		IoBuffer buffer = new IoBuffer(214, PacketHeader.SHORT);
		List<Integer> regionIds = new ArrayList<>();
		Player player = context.getPlayer();
		buffer.putLEShortA(player.getLocation().getSceneX());
		buffer.putLEShortA(player.getLocation().getRegionX());
		buffer.putS(player.getLocation().getZ());
		buffer.putLEShortA(player.getLocation().getSceneY());
		buffer.setBitAccess();
		Region r = player.getViewport().getRegion();
		RegionChunk[][][] chunks = new RegionChunk[4][13][13];
		int baseX = player.getLocation().getRegionX() - 6;
		int baseY = player.getLocation().getRegionY() - 6;
		for (int z = 0; z < 4; z++) {
			for (int x = baseX; x <= player.getLocation().getRegionX() + 6; x++) {
				for (int y = baseY; y <= player.getLocation().getRegionY() + 6; y++) {
					r = RegionManager.forId((x >> 3) << 8 | (y >> 3));
					if (r instanceof DynamicRegion) {
						DynamicRegion dr = (DynamicRegion) r;
						chunks[z][x - baseX][y - baseY] = dr.getChunks()[z][x - (dr.getX() << 3)][y - (dr.getY() << 3)];
					}
				}
			}
		}
		for (int plane = 0; plane < 4; plane++) {
			for (int offsetX = 0; offsetX < 13; offsetX++) {
				for (int offsetY = 0; offsetY < 13; offsetY++) {
					RegionChunk c = chunks[plane][offsetX][offsetY];
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
			int[] keys = RegionSQLHandler.getRegionXTEA(id);
			buffer.putIntB(keys[0]).putIntB(keys[1]).putIntB(keys[2]).putIntB(keys[3]);
		}
		buffer.putShort(player.getLocation().getRegionY());
		context.getPlayer().getSession().write(buffer);
		player.getPlayerFlags().setLastSceneGraph(player.getLocation());
	}

}
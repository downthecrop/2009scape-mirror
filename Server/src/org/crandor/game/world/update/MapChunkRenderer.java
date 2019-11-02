package org.crandor.game.world.update;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.RegionChunk;
import org.crandor.game.world.map.Viewport;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.ClearChunkContext;
import org.crandor.net.packet.out.ClearRegionChunk;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the rendering of the player's surrounding map chunks.
 * @author Emperor
 */
public final class MapChunkRenderer {

	/**
	 * Sends the map chunk rendering packet.
	 * @param player The player.
	 */
	public static void render(Player player) {
		Viewport v = player.getViewport();
		RegionChunk[][] last = player.getPlayerFlags().getLastViewport();
		List<RegionChunk> updated = new ArrayList<>();
		for (int x = 0; x < last.length; x++) {
			RegionChunk[] chunks = last[x];
			for (int y = 0; y < chunks.length; y++) {
				RegionChunk previous = chunks[y];
				if (previous == null) {
					continue;
				}
				if (!containsChunk(v.getChunks(), previous)) {
					PacketRepository.send(ClearRegionChunk.class, new ClearChunkContext(player, previous));
				} else {
					updated.add(previous);
				}
			}
		}
		for (int x = 0; x < v.getChunks().length; x++) {
			RegionChunk[] chunks = v.getChunks()[x];
			for (int y = 0; y < chunks.length; y++) {
				RegionChunk chunk = chunks[y];
				if (!updated.contains(chunk)) {
					chunk.synchronize(player);
				} else {
					chunk.update(player);
				}
				last[x][y] = chunk;
			}
		}
	}

	/**
	 * Checks if the chunks list contains the specified region chunk.
	 * @param list The list to search.
	 * @param c The region chunk.
	 * @return {@code True} if so.
	 */
	private static boolean containsChunk(RegionChunk[][] list, RegionChunk c) {
		for (RegionChunk[] l : list) {
			for (RegionChunk chunk : l) {
				if (chunk == c) {
					return true;
				}
			}
		}
		return false;
	}

}
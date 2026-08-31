package core.game.world.map.build;

import core.game.world.map.Region;
import core.game.world.map.RegionChunk;

import java.nio.ByteBuffer;

/**
 * A utility class used for parsing mapscapes.
 * @author Emperor
 *
 */
public final class MapscapeParser {

	/**
	 * Parses the mapscape buffer.
	 * @param r The region.
	 * @param buffer The buffer.
	 */
	public static void parse(Region r, byte[][][] mapscape, ByteBuffer buffer) {
		if (r.getChunks() == null) {
			return; // this is a DynamicRegion that has been cleared
		}
		// The cache is packed by region, so we need to parse it along the local-x/y coordinates
		for (int z = 0; z < 4; z++) {
			for (int localX = 0; localX < Region.SIZE; localX++) {
				for (int localY = 0; localY < Region.SIZE; localY++) {
					int chunkX = localX >> 3;
					int chunkY = localY >> 3;
					int chunkOffsetX = localX & 7;
					int chunkOffsetY = localY & 7;
					RegionChunk chunk = r.getChunks()[chunkX][chunkY][z];
					boolean[][] landscape;
					if (chunk == null) {
						// Still need to consume the buffer data even for null chunks
						landscape = new boolean[RegionChunk.SIZE][RegionChunk.SIZE];
					} else {
						landscape = chunk.getFlags().getLandscape();
					}
					while (true) {
						int value = buffer.get() & 0xFF;
						if (value == 0) {
							break;
						}
						if (value == 1) {
							buffer.get();
							break;
						}
						if (value <= 49) { //Overlay data
							int val = buffer.get() & 0xFF;
							if (val != 42 && val > 0) {
								landscape[chunkOffsetX][chunkOffsetY] = true;
							}
						} else if (value <= 81) {
							mapscape[z][localX][localY] = (byte) (value - 49);
						} else {
							int val = (byte) (value - 81) & 0xFF; //Underlay data
							if (val != 42 && val > 0) {
								landscape[chunkOffsetX][chunkOffsetY] = true;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Clips the mapscape.
	 * @param r The region.
	 * @param mapscape The mapscape.
	 */
	public static void clipMapscape(Region r, RegionChunk[][][] chunks, byte[][][] mapscape) {
		for (int z = 0; z < Region.PLANES; z++) {
			for (int chunkX = 0; chunkX < RegionChunk.SIZE; chunkX++) {
				for (int chunkY = 0; chunkY < RegionChunk.SIZE; chunkY++) {
					RegionChunk chunk = chunks[chunkX][chunkY][z];
					for (int x = 0; x < RegionChunk.SIZE; x++) {
						for (int y = 0; y < RegionChunk.SIZE; y++) {
							chunk.getFlags().flagEmptyTile(x, y);
							int chunkOffsetX = chunkX * RegionChunk.SIZE + x;
							int chunkOffsetY = chunkY * RegionChunk.SIZE + y;
							if ((mapscape[z][chunkOffsetX][chunkOffsetY] & 0x1) == 1) {
								int plane = z;
								if ((mapscape[1][chunkOffsetX][chunkOffsetY] & 0x2) == 2) {
									plane--;
								}
								if (plane > -1) {
									RegionChunk chunkToFlag = r.getChunks()[chunkX][chunkY][plane];
									chunkToFlag.getFlags().flagSolidTile(x, y);
								}
							}
						}
					}
				}
			}
		}
	}
}

package core.game.world.map.build;

import core.game.world.map.Region;

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
		for (int z = 0; z < 4; z++) {
			boolean[][] landscape = r.getPlanes()[z].getFlags().getLandscape();
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
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
								landscape[x][y] = true;
							}
						}
						else if (value <= 81) {
							mapscape[z][x][y] = (byte) (value - 49);
						}
						else {
							int val = (byte) (value - 81) & 0xFF; //Underlay data
							if (val != 42 && val > 0) {
								landscape[x][y] = true;
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
	public static void clipMapscape(Region r, byte[][][] mapscape) {
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					if ((mapscape[z][x][y] & 0x1) == 1) {
						int plane = z;
						if ((mapscape[1][x][y] & 0x2) == 2) {
							plane--;									
						}
						if (plane > -1) {
							r.getPlanes()[plane].getFlags().flagSolidTile(x, y);
						}
					}
				}
			}
		}
	}
}
package core.game.world.map.build;

import core.game.world.map.RegionManager;
import kotlin.Pair;
import rs09.game.system.SystemLogger;

import static java.lang.Math.max;

/**
 * Holds a region's flags like clipping flags, members, ...
 * @author Emperor
 *
 */
public final class RegionFlags {

	public static final int TILE_OBJECT = 0x40000;
	public static final int EMPTY_TILE = 0;
	public static final int SOLID_TILE = 0x200000;
	public static final int OBJ_10_PROJECTILE = 0x20000;
	public static final int OBJ_10 = 0x100;
	/**
	 * The plane.
	 */
	private final int plane;

	/**
	 * If the region is members only.
	 */
	private boolean members;
	
	/**
	 * The base x-coordinate.
	 */
	private final int baseX;
	
	/**
	 * The base y-coordinate.
	 */
	private final int baseY;
	
	/**
	 * The landscape data.
	 */
	private boolean[][] landscape;
	
	/**
	 * If the flags are set for projectile clipping
	 */
	private final boolean projectile;

	/**
	 * Constructs a new {@code RegionFlags} {@code Object}.
	 * @param x  The base x-coordinate (absolute).
	 * @param y The base y-coordinate (absolute).
     */
	public RegionFlags(int plane, int x, int y) {
		this(plane, x, y, false);
	}

	/**
	 * Constructs a new {@code RegionFlags} {@code Object}.
	 * @param x  The base x-coordinate (absolute).
	 * @param y The base y-coordinate (absolute).
     */
	public RegionFlags(int plane, int x, int y, boolean projectile) {
		this.plane = plane;
		this.baseX = x;
		this.baseY = y;
		this.projectile = projectile;
	}

	/**
	 * Flags a solid tile.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void flagSolidTile(int x, int y) {
		flag(x, y, SOLID_TILE);
	}

	public void flagEmptyTile(int x, int y) {
		flag(x, y, EMPTY_TILE);
	}

	/**
	 * Flags a tile object (object type 22).
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void flagTileObject(int x, int y) {
		flag(x, y, TILE_OBJECT);
	}

	/**
	 * Unflags a tile object.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public void unflagTileObject(int x, int y) {
		unflag(x, y, TILE_OBJECT);
	}

	/**
	 * Flags a solid object (type 10/11).
	 * @param x The x-coordinate
	 * @param y The y-coordinate.
	 * @param sizeX The x-size of the object.
	 * @param sizeY The y-size of the object.
	 * @param projectileClipped If the object is solid.
	 */
	public void flagSolidObject(int x, int y, int sizeX, int sizeY, boolean projectileClipped) {
		int clipdata = OBJ_10;
		if (projectileClipped) {
			clipdata += OBJ_10_PROJECTILE;
		}
		for (int i = x; i < x + sizeX; i++) {
			for (int j = y; j < y + sizeY; j++) {
				flag(i, j, clipdata);
			}
		}
	}

	/**
	 * Adds a flag.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param clipdata The clip data.
	 */
	public void flag(int x, int y, int clipdata) {
		if (x > -1 && x < 64 && y > -1 && y < 64) {
			addFlag(x, y, clipdata);
		} else {
			RegionManager.addClippingFlag(plane, baseX + x, baseY + y, projectile, clipdata);
		}
	}

	/**
	 * Unflags a solid object (type 10/11).
	 * @param x The x-coordinate
	 * @param y The y-coordinate.
	 * @param sizeX The x-size of the object.
	 * @param sizeY The y-size of the object.
	 * @param projectileClipped If the object is solid.
	 */
	public void unflagSolidObject(int x, int y, int sizeX, int sizeY, boolean projectileClipped) {
		int clipdata = OBJ_10;
		if (projectileClipped) {
			clipdata += OBJ_10_PROJECTILE;
		}
		for (int i = x; i < x + sizeX; i++) {
			for (int j = y; j < y + sizeY; j++) {
				unflag(i, j, clipdata);
			}
		}
	}

	/**
	 * Removes a flag.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param clipdata The clip data.
	 */
	public void unflag(int x, int y, int clipdata) {
		if (x > -1 && x < 64 && y > -1 && y < 64) {
			removeFlag(x, y, clipdata);
		} else {
			RegionManager.removeClippingFlag(plane, baseX + x, baseY + y, projectile, clipdata);
		}
	}

	private Pair<Integer, Integer> getFlagIndex(int x, int y) {
		return new Pair<>(((baseX >> 6) << 8) | (baseY >> 6), (plane * 64 * 64) + (x * 64) + y);
	}

	public int getFlag(int x, int y) {
		Pair<Integer, Integer> indices = getFlagIndex(x, y);
		return RegionManager.getFlags(indices.getFirst(), projectile)[indices.getSecond()];
	}

	public void addFlag(int x, int y, int clipdata) {
		int current = getFlag(x, y);
		Pair<Integer, Integer> indices = getFlagIndex(x, y);
		RegionManager.getFlags(indices.getFirst(), projectile)[indices.getSecond()] = max(0, current) | clipdata;
	}

	public void removeFlag(int x, int y, int clipdata) {
		int current = getFlag(x, y);
		Pair<Integer, Integer> indices = getFlagIndex(x, y);
		if ((current & clipdata) == 0) return;
		current = max(0, current) & ~clipdata;

		RegionManager.getFlags(indices.getFirst(), projectile)[indices.getSecond()] = current;
	}

	public void clearFlag(int x, int y) {
		Pair<Integer, Integer> indices = getFlagIndex(x, y);
		RegionManager.getFlags(indices.getFirst(), projectile)[indices.getSecond()] = 0;
	}

	public void invalidateFlag(int x, int y) {
		Pair<Integer, Integer> indices = getFlagIndex(x, y);
		RegionManager.getFlags(indices.getFirst(), projectile)[indices.getSecond()] = -1;
	}

	/**
	 * Flags a door object (type 0-3).
	 * @param x The x-coordinate
	 * @param y The y-coordinate.
	 * @param rotation The rotation.
	 * @param type The type.
	 * @param projectileClipped If the door is solid.
	 */
	public void flagDoorObject(int x, int y, int rotation, int type, boolean projectileClipped) {
		switch (type) {
		case 0:
			switch (rotation) {
			case 0:
				flag(x, y, 0x80);
				flag(x - 1, y, 0x8);
				break;
			case 1:
				flag(x, y, 0x2);
				flag(x, y + 1, 0x20);
				break;
			case 2:
				flag(x, y, 0x8);
				flag(x + 1, y, 0x80);
				break;
			case 3:
				flag(x, y, 0x20);
				flag(x, y - 1, 0x2);
				break;
			}
			break;
		case 1:
		case 3:
			switch (rotation) {
			case 0:
				flag(x, y, 0x1);
				flag(x - 1, y + 1, 0x10);
				break;
			case 1:
				flag(x, y, 0x4);
				flag(x + 1, y + 1, 0x40);
				break;
			case 2:
				flag(x, y, 0x10);
				flag(x + 1, y - 1, 0x1);
				break;
			case 3:
				flag(x, y, 0x40);
				flag(x - 1, y - 1, 0x4);
				break;
			}
			break;
		case 2:
			switch (rotation) {
			case 0:
				flag(x, y, 0x82);
				flag(x - 1, y, 0x8);
				flag(x, y + 1, 0x20);
				break;
			case 1:
				flag(x, y, 0xA);
				flag(x, y + 1, 0x20);
				flag(x + 1, y, 0x80);
				break;
			case 2:
				flag(x, y, 0x28);
				flag(x + 1, y, 0x80);
				flag(x, y - 1, 0x2);
				break;
			case 3:
				flag(x, y, 0xA0);
				flag(x, y - 1, 0x2);
				flag(x - 1, y, 0x8);
				break;
			}
			break;
		}
		if (projectileClipped) {
			switch (type) {
			case 0:
				switch (rotation) {
				case 0:
					flag(x, y, 0x10000);
					flag(x - 1, y, 0x1000);
					break;
				case 1:
					flag(x, y, 0x400);
					flag(x, y + 1, 0x4000);
					break;
				case 2:
					flag(x, y, 0x1000);
					flag(x + 1, y, 0x10000);
					break;
				case 3:
					flag(x, y, 0x4000);
					flag(x, y - 1, 0x400);
					break;
				}
				break;
			case 1:
			case 3:
				switch (rotation) {
				case 0:
					flag(x, y, 0x200);
					flag(x - 1, y + 1, 0x2000);
					break;
				case 1:
					flag(x, y, 0x800);
					flag(x + 1, y + 1, 0x8000);
					break;
				case 2:
					flag(x, y, 0x2000);
					flag(x + 1 , y - 1, 0x200);
					break;
				case 3:
					flag(x, y, 0x8000);
					flag(x - 1, y - 1, 0x800);
					break;
				}
				break;
			case 2:
				switch (rotation) {
				case 0:
					flag(x, y, 0x10400);
					flag(x - 1, y, 0x1000);
					flag(x, y + 1, 0x4000);
					break;
				case 1:
					flag(x, y, 0x1400);
					flag(x, y + 1, 0x4000);
					flag(x + 1, y, 0x10000);
					break;
				case 2:
					flag(x, y, 0x5000);
					flag(x + 1, y, 0x10000);
					flag(x, y - 1, 0x400);
					break;
				case 3:
					flag(x, y, 0x14000);
					flag(x, y - 1, 0x400);
					flag(x - 1, y, 0x1000);
					break;
				}
				break;
			}
		}
	}
	
	/**
	 * Unlags a door object (type 0-3).
	 * @param x The x-coordinate
	 * @param y The y-coordinate.
	 * @param rotation The rotation.
	 * @param type The type.
	 * @param projectileClipped If the door is solid.
	 */
	public void unflagDoorObject(int x, int y, int rotation, int type, boolean projectileClipped) {
		switch (type) {
		case 0:
			switch (rotation) {
			case 0:
				unflag(x, y, 0x80);
				unflag(x - 1, y, 0x8);
				break;
			case 1:
				unflag(x, y, 0x2);
				unflag(x, y + 1, 0x20);
				break;
			case 2:
				unflag(x, y, 0x8);
				unflag(x + 1, y, 0x80);
				break;
			case 3:
				unflag(x, y, 0x20);
				unflag(x, y - 1, 0x2);
				break;
			}
			break;
		case 1:
		case 3:
			switch (rotation) {
			case 0:
				unflag(x, y, 0x1);
				unflag(x - 1, y + 1, 0x10);
				break;
			case 1:
				unflag(x, y, 0x4);
				unflag(x + 1, y + 1, 0x40);
				break;
			case 2:
				unflag(x, y, 0x10);
				unflag(x + 1, y - 1, 0x1);
				break;
			case 3:
				unflag(x, y, 0x40);
				unflag(x - 1, y - 1, 0x4);
				break;
			}
			break;
		case 2:
			switch (rotation) {
			case 0:
				unflag(x, y, 0x82);
				unflag(x - 1, y, 0x8);
				unflag(x, y + 1, 0x20);
				break;
			case 1:
				unflag(x, y, 0xA);
				unflag(x, y + 1, 0x20);
				unflag(x + 1, y, 0x80);
				break;
			case 2:
				unflag(x, y, 0x28);
				unflag(x + 1, y, 0x80);
				unflag(x, y - 1, 0x2);
				break;
			case 3:
				unflag(x, y, 0xA0);
				unflag(x, y - 1, 0x2);
				unflag(x - 1, y, 0x8);
				break;
			}
			break;
		}
		if (projectileClipped) {
			switch (type) {
			case 0:
				switch (rotation) {
				case 0:
					unflag(x, y, 0x10000);
					unflag(x - 1, y, 0x1000);
					break;
				case 1:
					unflag(x, y, 0x400);
					unflag(x, y + 1, 0x4000);
					break;
				case 2:
					unflag(x, y, 0x1000);
					unflag(x + 1, y, 0x10000);
					break;
				case 3:
					unflag(x, y, 0x4000);
					unflag(x, y - 1, 0x400);
					break;
				}
				break;
			case 1:
			case 3:
				switch (rotation) {
				case 0:
					unflag(x, y, 0x200);
					unflag(x - 1, y + 1, 0x2000);
					break;
				case 1:
					unflag(x, y, 0x800);
					unflag(x + 1, y + 1, 0x8000);
					break;
				case 2:
					unflag(x, y, 0x2000);
					unflag(x + 1 , y - 1, 0x200);
					break;
				case 3:
					unflag(x, y, 0x8000);
					unflag(x - 1, y - 1, 0x800);
					break;
				}
				break;
			case 2:
				switch (rotation) {
				case 0:
					unflag(x, y, 0x10400);
					unflag(x - 1, y, 0x1000);
					unflag(x, y + 1, 0x4000);
					break;
				case 1:
					unflag(x, y, 0x1400);
					unflag(x, y + 1, 0x4000);
					unflag(x + 1, y, 0x10000);
					break;
				case 2:
					unflag(x, y, 0x5000);
					unflag(x + 1, y, 0x10000);
					unflag(x, y - 1, 0x400);
					break;
				case 3:
					unflag(x, y, 0x14000);
					unflag(x, y - 1, 0x400);
					unflag(x - 1, y, 0x1000);
					break;
				}
				break;
			}
		}
	}

	/**
	 * Gets the members.
	 * @return The members.
	 */
	public boolean isMembers() {
		return members;
	}

	/**
	 * Sets the members.
	 * @param members The members to set.
	 */
	public void setMembers(boolean members) {
		this.members = members;
	}

	/**
	 * Gets the plane.
	 * @return The plane.
	 */
	public int getPlane() {
		return plane;
	}

	/**
	 * Gets the landscape.
	 * @return The landscape.
	 */
	public boolean[][] getLandscape() {
		return landscape;
	}
	
	/**
	 * Sets the landscape.
	 * @param landscape The landscape to set.
	 */
	public void setLandscape(boolean[][] landscape) {
		this.landscape = landscape;
	}
}
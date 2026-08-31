package core.game.world.map.build;

import core.cache.def.impl.SceneryDefinition;
import core.cache.misc.buffer.ByteBufferUtils;
import core.game.node.scenery.Scenery;
import core.game.world.map.*;

import java.nio.ByteBuffer;

/**
 * A utility class used for parsing landscapes.
 * @author Emperor
 *
 */
public final class LandscapeParser {
	/**
	 * Parses the landscape.
	 * @param r The region.
	 * @param mapscape The mapscape data.
	 * @param storeObjects If all objects should be stored (rather than just the objects with options).
	 */
	public static void parse(Region r, RegionChunk[][][] chunks, byte[][][] mapscape, ByteBuffer buffer, boolean storeObjects) {
		int objectId = -1;
		for (;;) {
			int offset = ByteBufferUtils.getBigSmart(buffer);
			if (offset == 0) {
				break;
			}
			objectId += offset;
			int location = 0;
			for (;;) {
				offset = ByteBufferUtils.getSmart(buffer);
				if (offset == 0) {
					break;
				}
				location += offset - 1;
				int localY = location & 0x3f;
				int localX = location >> 6 & 0x3f;
				int configuration = buffer.get() & 0xFF;
				int rotation = configuration & 0x3;
				int type = configuration >> 2;
				int z = location >> 12;
				r.setObjectCount(r.getObjectCount() + 1);
				if ((mapscape[1][localX][localY] & 0x2) == 2) {
					z--;
				}
				if (z >= 0 && z <= 3) {
					Location loc = Location.create((r.getX() << 6) + localX, (r.getY() << 6) + localY, z);
					Scenery object = new Scenery(objectId, loc, type, rotation);
					int chunkX = loc.getChunkX();
					int chunkY = loc.getChunkY();
					int chunkOffsetX = loc.getChunkOffsetX();
					int chunkOffsetY = loc.getChunkOffsetY();
					RegionChunk chunk = chunks[chunkX][chunkY][z];
					flagScenery(chunk, chunkOffsetX, chunkOffsetY, object, true, storeObjects);
				}
			}
		}
	}

	/**
	 * Adds a scenery temporarily.
	 * @param object The object to add.
	 */
	public static void addScenery(Scenery object) {
		addScenery(object, false);
	}

	/**
	 * Adds a scenery.
	 * @param object The object to add.
	 * @param landscape If the object should be added permanent.
	 */
	public static void addScenery(Scenery object, boolean landscape) {
		Location l = object.getLocation();
		flagScenery(RegionManager.getRegionChunk(l), l.getChunkOffsetX(), l.getChunkOffsetY(), object, landscape, false);
	}

	/**
	 * Flags a scenery on the plane's clipping flags.
	 * @param chunk The chunk.
	 * @param object The object.
	 * @param landscape If we are adding this scenery permanent.
	 * @param storeObjects If all objects should be stored (rather than just the objects with options).
	 */
	public static void flagScenery(RegionChunk chunk, int chunkOffsetX, int chunkOffsetY, Scenery object, boolean landscape, boolean storeObjects) {
		Region r = chunk.getRegion();
		Region.load(r);
		SceneryDefinition def = object.getDefinition();
		object.setActive(true);
		object.setRenderable(true);
		boolean add = storeObjects || !landscape || def.getChildObject(null).hasActions();
		if (add) {
			if (landscape) {
				chunk.addStatDyn(object, chunkOffsetX, chunkOffsetY);
			} else {
				chunk.add(object);
			}
		}
		applyClippingFlagsFor(chunk, chunkOffsetX, chunkOffsetY, object);
	}

	public static boolean applyClippingFlagsFor(RegionChunk chunk, int chunkOffsetX, int chunkOffsetY, Scenery object) {
		SceneryDefinition def = object.getDefinition();
		int sizeX;
		int sizeY;
		if (object.getRotation() % 2 == 0) {
			sizeX = def.sizeX;
			sizeY = def.sizeY;
		} else {
			sizeX = def.sizeY;
			sizeY = def.sizeX;
		}
		int type = object.getType();
		if (type == 22) { //Tile
			chunk.getFlags().getLandscape()[chunkOffsetX][chunkOffsetY] = true;
			if (def.interactable != 0 || def.clipType == 1 || def.secondBool) {
				if (def.clipType == 1) {
					chunk.getFlags().flagTileObject(chunkOffsetX, chunkOffsetY);
					if (def.isProjectileClipped()) {
						chunk.getProjectileFlags().flagTileObject(chunkOffsetX, chunkOffsetY);
					}
				}
			}
		} else if (type >= 9) { //Default objects
			if (def.clipType != 0) {
				chunk.getFlags().flagSolidObject(chunkOffsetX, chunkOffsetY, sizeX, sizeY, def.projectileClipped);
				if (def.isProjectileClipped()) {
					chunk.getProjectileFlags().flagSolidObject(chunkOffsetX, chunkOffsetY, sizeX, sizeY, def.projectileClipped);
				}
			}
		} else if (type >= 0 && type <= 3) { //Doors/walls
			if (def.clipType != 0) {
				chunk.getFlags().flagDoorObject(chunkOffsetX, chunkOffsetY, object.getRotation(), type, def.projectileClipped);
				if (def.isProjectileClipped()) {
					chunk.getProjectileFlags().flagDoorObject(chunkOffsetX, chunkOffsetY, object.getRotation(), type, def.projectileClipped);
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Removes a scenery.
	 * @param object The object.
	 * @return The removed scenery.
	 */
	public static Scenery removeScenery(Scenery object) {
		if (!object.isRenderable()) {
			return null;
		}

		// Get and remove the object
		Location loc = object.getLocation();
		Region region = RegionManager.forId(loc.getRegionId());
		Region.load(region);
		RegionChunk chunk = region.getChunks()[loc.getChunkX()][loc.getChunkY()][loc.getZ()];
		int index = chunk.getIndex(loc.getChunkOffsetX(), loc.getChunkOffsetY(), object.getId(), object.getType());
		if (index == -1) {
			return null;
		}
		Scenery current = chunk.getObjects()[loc.getChunkOffsetX()][loc.getChunkOffsetY()][index];
		current.setActive(false);
		object.setActive(false);
		chunk.getObjects()[loc.getChunkOffsetX()][loc.getChunkOffsetY()][index] = null;
		SceneryDefinition def = object.getDefinition();

		// Remove its clipping flags
		int sizeX;
		int sizeY;
		if (object.getRotation() % 2 == 0) {
			sizeX = def.sizeX;
			sizeY = def.sizeY;
		} else {
			sizeX = def.sizeY;
			sizeY = def.sizeX;
		}
		int chunkOffsetX = loc.getChunkOffsetX();
		int chunkOffsetY = loc.getChunkOffsetY();
		int type = object.getType();
		if (type == 22) { //Tile
			if (def.interactable != 0 || def.clipType == 1 || def.secondBool) {
				if (def.clipType == 1) {
					chunk.getFlags().unflagTileObject(chunkOffsetX, chunkOffsetY);
					if (def.isProjectileClipped()) {
						chunk.getProjectileFlags().unflagTileObject(chunkOffsetX, chunkOffsetY);
					}
				}
			}
		} else if (type >= 9) { //Default objects
			if (def.clipType != 0) {
				chunk.getFlags().unflagSolidObject(chunkOffsetX, chunkOffsetY, sizeX, sizeY, def.projectileClipped);
				if (def.isProjectileClipped()) {
					chunk.getProjectileFlags().unflagSolidObject(chunkOffsetX, chunkOffsetY, sizeX, sizeY, def.projectileClipped);
				}
			}
		} else if (type >= 0 && type <= 3) { //Doors/walls
			if (def.clipType != 0) {
				chunk.getFlags().unflagDoorObject(chunkOffsetX, chunkOffsetY, object.getRotation(), type, def.projectileClipped);
				if (def.isProjectileClipped()) {
					chunk.getProjectileFlags().unflagDoorObject(chunkOffsetX, chunkOffsetY, object.getRotation(), type, def.projectileClipped);
				}
			}
		}
		return current;
	}
}

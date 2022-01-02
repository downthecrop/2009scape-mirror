package core.game.world.map.build;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.music.MusicZone;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.scenery.Scenery;
import core.game.world.map.*;
import core.game.world.map.zone.RegionZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.impl.MultiwayCombatZone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * Represents a dynamically constructed region.
 *
 * @author Emperor
 */
public final class DynamicRegion extends Region {

	/**
	 * The reserved areas.
	 */
	private static final List<ZoneBorders> RESERVED_AREAS = new ArrayList<>(20);

	/**
	 * The region id of the copied region.
	 */
	private final int regionId;

	/**
	 * The region chunks.
	 */
	private final RegionChunk[][][] chunks;

	/**
	 * The zone borders.
	 */
	private ZoneBorders borders;

	/**
	 * If the dynamic region is a multiway combat zone.
	 */
	private boolean multicombat;

	/**
	 * If the region is permanent.
	 */
	private boolean permanent;

	/**
	 * An array of linked regions.
	 */
	private DynamicRegion[] linked;

	/**
	 * The parent region (used for linking regions).
	 */
	private DynamicRegion parentRegion;

	public ArrayList<NPC> NPCs = new ArrayList<>(10);

	/**
	 * Constructs a new {@code DynamicRegion} {@code Object}.
	 * @param regionId The region id of the region to copy.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public DynamicRegion(int regionId, int x, int y) {
		super(x, y);
		this.regionId = regionId;
		this.chunks = new RegionChunk[4][SIZE >> 3][SIZE >> 3];
	}

	/**
	 * Creates a dynamic region copy of the region id.
	 * @param regionId The region id.
	 * @return The dynamic region.
	 */
	public static DynamicRegion create(int regionId) {
		int x = (regionId >> 8) << 6;
		int y = (regionId & 0xFF) << 6;
		return create(new ZoneBorders(x, y, x + SIZE, y + SIZE))[0];
	}

	/**
	 * Creates a dynamic region copy of two regions and everything in between.
	 * @param regionOne The first region.
	 * @param regionTwo The second/last region.
	 * @return The new region.
	 */
	public static DynamicRegion create(int regionOne, int regionTwo) {
		int x = (regionOne >> 8) << 6;
		int y = (regionOne & 0xFF) << 6;
		int x1 = (regionTwo >> 8) << 6;
		int y1 = (regionTwo & 0xFF) << 6;
		return create(new ZoneBorders(x, y, x1 + SIZE, y1 + SIZE))[0];
	}

	/**
	 * Copies the zone into a new dynamic region.
	 * @param copy The zone to copy.
	 * @return The dynamic region.
	 */
	public static DynamicRegion[] create(ZoneBorders copy) {
		int baseX = copy.getSouthWestX() >> 6;
		int baseY = copy.getSouthWestY() >> 6;

		ZoneBorders border = findZoneBorders((copy.getNorthEastX() - copy.getSouthWestX()) >> 3, (copy.getNorthEastY() - copy.getSouthWestY()) >> 3);
		RESERVED_AREAS.add(border);
		Location l = Location.create(border.getSouthWestX(), border.getSouthWestY(), 0);
		List<DynamicRegion> regions = new ArrayList<>(20);
		for (int x = copy.getSouthWestX() >> 6; x < copy.getNorthEastX() >> 6; x++) {
			for (int y = copy.getSouthWestY() >> 6; y < copy.getNorthEastY() >> 6; y++) {
				int regionId = x << 8 | y;
				Location loc = l.transform((x - baseX) << 6, (y - baseY) << 6, 0);
				DynamicRegion region = copy(regionId, loc);
				region.setBorders(border);
				Region r = RegionManager.forId(region.getId());
				if (r != null) {
					for (int z = 0; z < 4; z++) {
						region.getPlanes()[z].getPlayers().addAll(r.getPlanes()[z].getPlayers());
						region.getPlanes()[z].getNpcs().addAll(r.getPlanes()[z].getNpcs());
					}
				}
				RegionManager.addRegion(region.getId(), region);
				regions.add(region);
			}
		}
		for (Region r : regions) {
			for (int z = 0; z < 4; z++) {
				for (Player p : r.getPlanes()[z].getPlayers()) {
					p.updateSceneGraph(false);
				}
			}
		}
		return regions.toArray(new DynamicRegion[regions.size()]);
	}

	/**
	 * Finds and reserves borders for a new dynamic region area.
	 * @param sizeX The x-size of the region (in chunks).
	 * @param sizeY The y-size of the region (in chunks).
	 * @return The zone borders.
	 */
	public static ZoneBorders reserveArea(int sizeX, int sizeY) {
		ZoneBorders borders = findZoneBorders(sizeX, sizeY);
		RESERVED_AREAS.add(borders);
		return borders;
	}

	/**
	 * Finds free borders for a new dynamic region area.
	 * @param sizeX The x-size of the region (in chunks).
	 * @param sizeY The y-size of the region (in chunks).
	 * @return The zone borders.
	 */
	public static ZoneBorders findZoneBorders(int sizeX, int sizeY) {
		int x = 0;
		int y = 0;
		int count = 0;
		int width = (sizeX >> 3) << 6;
		int height = (sizeY >> 3) << 6;
		if (width < 64) {
			width = 64;
		}
		if (height < 64) {
			height = 64;
		}
		while (true) {
			int endX = x + width;
			int endY = y + height;
			boolean reserved = false;
			for (ZoneBorders b : RESERVED_AREAS) {
				if (b.insideBorder(x, y) || b.insideBorder(endX, endY)
						|| b.insideBorder(x, endY) || b.insideBorder(endX, y)) {
					reserved = true;
					break;
				}
			}
			if (!reserved) {
				return new ZoneBorders(x, y, endX, endY);
			}
			if (++count % 15 == 0) {
				y += 64;
				x = 0;
			} else {
				x += 64;
			}
		}
	}

	/**
	 * Copies (but does not initialize) a region.
	 * @param regionId The id of the region to copy.
	 * @param to The location to copy the region to.
	 * @return The {@code DynamicRegion} object.
	 */
	public static DynamicRegion copy(int regionId, Location to) {
		int regionX = ((regionId >> 8) & 0xFF) << 6;
		int regionY = (regionId & 0xFF) << 6;
		DynamicRegion region = new DynamicRegion(regionId, to.getRegionX() >> 3, to.getRegionY() >> 3);
		for (int offsetX = 0; offsetX < 8; offsetX++) {
			for (int offsetY = 0; offsetY < 8; offsetY++) {
				int x = regionX + (offsetX << 3);
				int y = regionY + (offsetY << 3);
				for (int plane = 0; plane < 4; plane++) {
					RegionChunk c = region.chunks[plane][offsetX][offsetY];
					if (c == null) {
						region.chunks[plane][offsetX][offsetY] = c = new RegionChunk(Location.create(0, 0, 0), 0, region.getPlanes()[plane]);
					}
					c.setRotation(0);
					c.setBase(Location.create(x, y, plane));
				}
			}
		}
		return region;
	}

	/**
	 * Links regions with this region as parent region.
	 * @param regions The regions to link.
	 * @warning This region will be flagged as active!
	 */
	public void link(DynamicRegion...regions) {
		for (DynamicRegion r : regions) {
			r.parentRegion = this;
		}
		this.linked = regions;
		flagActive();
	}

	/**
	 * Toggles the multiway combat flag.
	 */
	public void toggleMulticombat() {
		if (multicombat) {
			for (Iterator<RegionZone> it = getRegionZones().iterator(); it.hasNext();) {
				if (it.next().getZone() == MultiwayCombatZone.getInstance()) {
					it.remove();
				}
			}
			multicombat = false;
			return;
		}
		getRegionZones().add(new RegionZone(MultiwayCombatZone.getInstance(), borders));
		multicombat = true;
	}

	/**
	 * Sets the music id for this region.
	 * @param musicId The music id.
	 */
	public void setMusicId(int musicId) {
		getMusicZones().add(new MusicZone(musicId, borders));
	}

	/**
	 * Rotates the region 90% clockwise.
	 * TODO: Finish this
	 */
	public void rotate() {
		for (int z = 0; z < 4; z++) {
			RegionChunk[][] c = Arrays.copyOf(chunks[z], 8);
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					RegionChunk r = chunks[z][x][y] = c[8 - y - 1][x];
					if (r != null) {
						r.setRotation(r.getRotation() + 1);
					}
				}
			}
		}
	}

	/**
	 * Sets a region chunk (without setting objects/clipping flags).
	 * @param z The plane.
	 * @param x The chunk x (0-8).
	 * @param y The chunk y (0-8).
	 * @param chunk The chunk to set.
	 */
	public void setChunk(int z, int x, int y, RegionChunk chunk) {
		chunks[z][x][y] = chunk;
		getPlanes()[z].getChunks()[x][y] = chunk;
		if (chunk != null) {
			chunk.setCurrentBase(getBaseLocation().transform(x << 3, y << 3, 0));
		}
	}

	/**
	 * Replaces a region chunk.
	 * @param z The plane.
	 * @param x The x-coordinate of the chunk. (0-7)
	 * @param y The y-coordinate of the chunk. (0-7)
	 * @param chunk The chunk to replace with.
	 * @param fromRegion The region the chunk is copied from.
	 */
	public void replaceChunk(int z, int x, int y, RegionChunk chunk, Region fromRegion) {
		Region.load(DynamicRegion.this);
		RegionPlane p = getPlanes()[z];
		chunks[z][x][y] = chunk;
		p.getChunks()[x][y] = chunk;
		if (chunk == null) {
			for (int i = x << 3; i < (x + 1) << 3; i++) {
				for (int j = y << 3; j < (y + 1) << 3; j++) {
					p.getFlags().getClippingFlags()[i][j] = -1;
					p.getProjectileFlags().getClippingFlags()[i][j] = -1;
					Scenery object = p.getObjects()[i][j];
					if (object != null) {
						LandscapeParser.removeScenery(object);
					} else {
						p.add(null, i, j, true);
					}
				}
			}
		} else {
			chunk.clear();
			Region.load(fromRegion);
			Location l = chunk.getBase();
			chunk.setCurrentBase(getBaseLocation().transform(x << 3, y << 3, 0));
			Location regionBase = fromRegion.getBaseLocation();
			RegionPlane rp = fromRegion.getPlanes()[l.getZ()];
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					int toX = (x << 3) + i;
					int toY = (y << 3) + j;
					int fromX = (l.getX() - regionBase.getX()) + i;
					int fromY = (l.getY() - regionBase.getY()) + j;
					p.getFlags().getLandscape()[toX][toY] = rp.getFlags().getLandscape()[fromX][fromY];
					p.getFlags().getClippingFlags()[toX][toY] = rp.getFlags().getClippingFlags()[fromX][fromY];
					p.getProjectileFlags().getClippingFlags()[toX][toY] = rp.getProjectileFlags().getClippingFlags()[fromX][fromY];
					Scenery[] objects = { rp.getChunkObject(fromX, fromY) };
					RegionChunk ch = rp.getChunks()[fromX >> 3][fromY >> 3];
					if (ch instanceof BuildRegionChunk) {
						BuildRegionChunk bc = (BuildRegionChunk) ch;
						objects = new Scenery[BuildRegionChunk.ARRAY_SIZE];
						for (int k = 0; k < objects.length; k++) {
							objects[k] = bc.get(i, j, k);
						}
					}
					for (Scenery object : objects) {
						if (object != null) {
							object = object.transform(object.getId(), object.getRotation(), getBaseLocation().transform(toX, toY, 0));
							LandscapeParser.flagScenery(p, toX, toY, object, false, true);//chunk instanceof BuildRegionChunk);//.addGameObject(object, true);
						} else {
							p.add(null, toX, toY, true);
						}
					}
				}
			}

		}
	}

	@Override
	public void flagInactive() {
		if (!permanent) {
			if (parentRegion != null && parentRegion.isActive()) {
				parentRegion.checkInactive();
				return;
			}
			if (linked != null) {
				for (DynamicRegion r : linked) {
					if (!r.isInactive(false)) {
						return;
					}
				}
			}
			super.flagInactive();
			for (RegionPlane plane : getPlanes()) {
				for (int i = 0; i < plane.getNpcs().size(); i++) {
					NPC npc = plane.getNpcs().get(0);
					npc.clear();
				}
				for (RegionChunk[] chunks : getChunks()[plane.getPlane()]) {
					for (RegionChunk chunk : chunks) {
						if (chunk != null) {
							for (Iterator<GroundItem> it = chunk.getItems().iterator(); it.hasNext();) {
								GroundItemManager.getItems().remove(it.next());
							}
						}
					}
				}
			}
			RESERVED_AREAS.remove(borders);
			if (multicombat) {
				toggleMulticombat();
			}
			if (linked != null) {
				for (DynamicRegion r : linked) {
					r.flagInactive();
				}
			}
		}
	}

	@Override
	public int getRegionId() {
		return regionId;
	}

	/**
	 * Gets the chunks.
	 * @return The chunks.
	 */
	public RegionChunk[][][] getChunks() {
		return chunks;
	}

	/**
	 * Gets the borders.
	 * @return The borders.
	 */
	public ZoneBorders getBorders() {
		return borders;
	}

	/**
	 * Sets the borders.
	 * @param borders The borders to set.
	 */
	public void setBorders(ZoneBorders borders) {
		this.borders = borders;
	}

	/**
	 * Gets the multicombat.
	 * @return The multicombat.
	 */
	public boolean isMulticombat() {
		return multicombat;
	}

	/**
	 * Sets the multicombat.
	 * @param multicombat The multicombat to set.
	 */
	public void setMulticombat(boolean multicombat) {
		this.multicombat = multicombat;
	}

	/**
	 * Gets the permanent value.
	 * @return The permanent.
	 */
	public boolean isPermanent() {
		return permanent;
	}

	/**
	 * Sets the permanent value.
	 * @param permanent The permanent to set.
	 */
	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

	/**
	 * Gets the parentRegion value.
	 * @return The parentRegion.
	 */
	public DynamicRegion getParent() {
		return parentRegion;
	}

	public void clear(){
		for(NPC n : NPCs){
			n.clear();
		}
		NPCs.clear();
	}
}
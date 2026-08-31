package core.game.world.map.build;

import content.global.skill.summoning.familiar.Familiar;
import core.game.interaction.QueueStrength;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.music.MusicZone;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.world.map.*;
import core.game.world.map.zone.RegionZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.impl.MultiwayCombatZone;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static core.api.ContentAPIKt.queueScript;
import static core.api.ContentAPIKt.restartScript;
import static core.api.ContentAPIKt.stopExecuting;

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
		// Reset any stale flags at this region's chunk coordinates
		for (int chunkX = 0; chunkX < CHUNKS_SIZE; chunkX++) {
			for (int chunkY = 0; chunkY < CHUNKS_SIZE; chunkY++) {
				for (int cz = 0; cz < Region.PLANES; cz++) {
					RegionChunk chunk = chunks[chunkX][chunkY][cz];
					for (int chunkOffsetX = 0; chunkOffsetX < 8; chunkOffsetX++) {
						for (int chunkOffsetY = 0; chunkOffsetY < 8; chunkOffsetY++) {
							chunk.getFlags().invalidateFlag(chunkOffsetX, chunkOffsetY); // the 'invalid' value of -1 triggers reloading
							chunk.getProjectileFlags().clearFlag(chunkOffsetX, chunkOffsetY); // the 'clear' value of 0 is the default state
						}
					}
				}
			}
		}
	}

	public DynamicRegion(@NotNull ZoneBorders borders) {
		this(-1, borders.getSouthWestX() >> 6, borders.getSouthWestY() >> 6);
		setBorders(borders);
		RegionManager.addRegion(getId(), this);
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
				DynamicRegion dest = copy(regionId, loc);
				dest.setBorders(border);
				RegionManager.addRegion(dest.getId(), dest);
				regions.add(dest);
			}
		}
		for (Region r : regions) {
			for (int z = 0; z < PLANES; z++) {
				for (Player player : r.assemblePlayerList(z)) {
					if (player != null) {
						player.updateSceneGraph(false);
					}
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
		if (width < SIZE) {
			width = SIZE;
		}
		if (height < SIZE) {
			height = SIZE;
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
				y += SIZE;
				x = 0;
			} else {
				x += SIZE;
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
		Region base = RegionManager.forId(regionId);
		Region.load(base);
		for (int offsetX = 0; offsetX < 8; offsetX++) {
			for (int offsetY = 0; offsetY < 8; offsetY++) {
				int x = regionX + (offsetX << 3);
				int y = regionY + (offsetY << 3);
				for (int plane = 0; plane < PLANES; plane++) {
					RegionChunk c = base.getChunks()[offsetX][offsetY][plane];
					if (c == null) {
						c = new RegionChunk(Location.create(0, 0, 0), 0);
						region.chunks[offsetX][offsetY][plane] = c;
					} else {
						c = c.copy();
						region.replaceChunk(plane, offsetX, offsetY, c, base);
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
	 * Replaces a region chunk.
	 * @param z The plane.
	 * @param x The x-coordinate of the chunk. (0-7)
	 * @param y The y-coordinate of the chunk. (0-7)
	 * @param replacement The chunk to replace with.
	 * @param fromRegion The region the chunk is copied from.
	 */
	public void replaceChunk(int z, int x, int y, RegionChunk replacement, Region fromRegion) {
		Region.load(DynamicRegion.this);
		chunks[x][y][z] = replacement;
		Region.load(fromRegion);
		RegionChunk orig = fromRegion.getChunks()[x][y][z];
		Location currentBase = getBaseLocation().transform(x << 3, y << 3, z);
		replacement.setCurrentBase(currentBase);
		replacement.rebaseObjects();
		replacement.resetClippingFlags();
		replacement.rebuildClippingFlags(orig);
		for (NPC npc : orig.getNpcs()) {
			Location newLoc = currentBase.transform(npc.getLocation().getChunkOffsetX(), npc.getLocation().getChunkOffsetY(), 0);
			NPC copy = NPC.create(npc.getId(), newLoc, npc.getDirection());
			copy.setAggressive(npc.isAggressive());
			copy.setRespawn(npc.isRespawn());
			copy.setWalks(npc.isWalks());
			copy.init();
		}
	}

	@Override
	public boolean flagInactive(boolean force) {
		if (!permanent) {
			if (parentRegion != null && parentRegion.isActive()) {
				parentRegion.checkInactive();
				return false;
			}
			if (linked != null) {
				for (DynamicRegion r : linked) {
					if (!r.isInactive(false)) {
						return false;
					}
				}
			}
			if (!super.flagInactive(force)) {
				return false;
			}
			for (int x = 0; x < CHUNKS_SIZE; x++) {
				for (int y = 0; y < CHUNKS_SIZE; y++) {
					for (int z = 0; z < PLANES; z++) {
						RegionChunk chunk = chunks[x][y][z];
						for (NPC npc : new ArrayList<>(chunk.getNpcs())) {
							if (npc instanceof Familiar) {
								Familiar fam = (Familiar) npc;
								Location locOwner = fam.getOwner().getLocation();
								fam.setInvisible(true);
								RegionManager.move(fam, fam.getLocation(), locOwner);
								fam.setLocation(locOwner);
								queueScript(fam, 0, QueueStrength.STRONG, false, (Integer stage) -> {
									if (fam.call()) {
										fam.getWalkingQueue().update();
										return stopExecuting(fam);
									}
									else {
										return restartScript(fam);
									}
								});
							}
							else {
								npc.clear();
							}
						}
						for (GroundItem item : new ArrayList<>(chunk.getItems())) {
							GroundItemManager.destroy(item);
						}
					}
				}
			}
			RESERVED_AREAS.remove(borders);
			if (multicombat) {
				toggleMulticombat();
			}
			boolean allLinkedInactive = true;
			if (linked != null) {
				for (DynamicRegion r : linked) {
					allLinkedInactive &= r.flagInactive(force);
				}
			}
			return allLinkedInactive;
		} else {
			return true;
		}
	}

	@Override
	public int getRegionId() {
		return regionId;
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

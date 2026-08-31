package core.game.world.map;

import core.cache.Cache;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.music.MusicZone;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.communication.CommunicationInfo;
import core.game.system.task.Pulse;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.build.LandscapeParser;
import core.game.world.map.build.MapscapeParser;
import core.game.world.map.zone.RegionZone;
import core.tools.Log;
import core.game.system.config.XteaParser;
import core.game.world.GameWorld;
import core.game.world.repository.Repository;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static core.api.ContentAPIKt.log;

/**
 * Represents a region.
 *
 * @author Emperor
 */
public class Region {
	/**
	 * The number of tiles per region.
	 */
	public static final int SIZE = 64;

	/**
	 * The number of chunks per region.
	 */
	public static final int CHUNKS_SIZE = 8;

	/**
	 * The number of z levels per x,y coordinate.
	 */
	public static final int PLANES = 4;

	/**
	 * The region x-coordinate.
	 */
	private final int x;

	/**
	 * The region y-coordinate.
	 */
	private final int y;

	/**
	 * The chunks in this region.
	 */
	protected RegionChunk[][][] chunks = new RegionChunk[CHUNKS_SIZE][CHUNKS_SIZE][PLANES];

	/**
	 * The activity pulse.
	 */
	private final Pulse activityPulse;

	/**
	 * The region zones lying in this region.
	 */
	private final List<RegionZone> regionZones = new ArrayList<>(20);

	/**
	 * The region-wide music track ID for this region.
	 */
	private int music = -1;

	/**
	 * Any tile-specific music zones lying in this region.
	 */
	private final List<MusicZone> musicZones = new ArrayList<>(20);

	/**
	 * Keeps track of players and time in region for tolerance purposes
	 */
	private final HashMap<String, Long> tolerances = new HashMap<String, Long>();

	/**
	 * If the region is active.
	 */
	private boolean active;

	/**
	 * The amount of objects in this region.
	 */
	private int objectCount;

	/**
	 * If the region has flags.
	 */
	private boolean hasFlags;

	/**
	 * If the region has been loaded.
	 */
	private boolean loaded;

	/**
	 * The amount of players viewing this region.
	 */
	private int viewAmount;

	/**
	 * If the region can be edited.
	 */
	private boolean build;

	/**
	 * Any scenery overrides for this region
	 */
	private final ArrayList<Scenery> addSceneries = new ArrayList<>();
	private final ArrayList<Scenery> removeSceneries = new ArrayList<>();

	/**
	 * Constructs a new {@code Region} {@code Object}.
	 * @param x The x-coordinate of the region.
	 * @param y The y-coordinate of the region.
	 */
	public Region(int x, int y) {
		this.x = x;
		this.y = y;
		Location swCorner = getBaseLocation();
		for (x = 0; x < CHUNKS_SIZE; x++) {
			for (y = 0; y < CHUNKS_SIZE; y++) {
				for (int z = 0; z < PLANES; z++) {
					Location loc = swCorner.transform(x * RegionChunk.SIZE, y * RegionChunk.SIZE, z);
					chunks[x][y][z] = new RegionChunk(loc, 0);
				}
			}
		}
		this.activityPulse = new Pulse(50) {
			@Override
			public boolean pulse() {
				flagInactive();
				return true;
			}
		};
		activityPulse.stop();
	}

	/**
	 * Gets the base location.
	 * @return The base location.
	 */
	public Location getBaseLocation() {
		return Location.create(x << 6, y << 6, 0);
	}

	/**
	 * Gets the chunks.
	 * @return The chunks.
	 */
	public RegionChunk[][][] getChunks() {
		return chunks;
	}

	/**
	 * Adds a region zone to this region.
	 * @param zone The region zone.
	 */
	public void add(RegionZone zone) {
		regionZones.add(zone);
		for (int x = 0; x < CHUNKS_SIZE; x++) {
			for (int y = 0; y < CHUNKS_SIZE; y++) {
				for (int z = 0; z < PLANES; z++) {
					for (NPC npc : chunks[x][y][z].getNpcs()) {
						npc.getZoneMonitor().updateLocation(npc.getLocation());
					}
					for (Player player : chunks[x][y][z].getPlayers()) {
						if (player != null) {
							player.getZoneMonitor().updateLocation(player.getLocation());
						}
					}
				}
			}
		}
	}

	public void remove(RegionZone zone) {
		regionZones.remove(zone);
		for (int x = 0; x < CHUNKS_SIZE; x++) {
			for (int y = 0; y < CHUNKS_SIZE; y++) {
				for (int z = 0; z < PLANES; z++) {
					for (NPC npc : chunks[x][y][z].getNpcs()) {
						npc.getZoneMonitor().updateLocation(npc.getLocation());
					}
					for (Player player : chunks[x][y][z].getPlayers()) {
						player.getZoneMonitor().updateLocation(player.getLocation());
					}
				}
			}
		}
	}

	/**
	 * Checks if player is tolerated by enemies in this region
	 */
	public boolean isTolerated(Player player) {
		return System.currentTimeMillis() - tolerances.getOrDefault(player.getName(), System.currentTimeMillis()) > TimeUnit.MINUTES.toMillis(10);
	}

	/**
	 * Checks if the region is inactive, if so it will start the inactivity flagging.
	 * @return {@code True} if the region is inactive.
	 */
	public boolean checkInactive() {
		return isInactive(true);
	}

	/**
	 * Checks if the region is inactive.
	 * @param runPulse If the pulse for flagging the region as inactive should be ran.
	 * @return {@code True} if so.
	 */
	public boolean isInactive(boolean runPulse) {
		if (isViewed()) {
			return false;
		}
		for (int x = 0; x < CHUNKS_SIZE; x++) {
			for (int y = 0; y < CHUNKS_SIZE; y++) {
				for (int z = 0; z < PLANES; z++) {
					if (!chunks[x][y][z].getPlayers().isEmpty()) {
						return false;
					}
				}
			}
		}
		if (runPulse) {
			if (!activityPulse.isRunning()) {
				activityPulse.restart();
				activityPulse.start();
				GameWorld.getPulser().submit(activityPulse);
			}
		}
		return true;
	}

	/**
	 * Flags the region as active.
	 */
	public void flagActive() {
		activityPulse.stop();
		if (!active) {
			active = true;
			load(this);
			for (int x = 0; x < CHUNKS_SIZE; x++) {
				for (int y = 0; y < CHUNKS_SIZE; y++) {
					for (int z = 0; z < PLANES; z++) {
						for (NPC npc : chunks[x][y][z].getNpcs()) {
							if (npc.isActive()) {
								Repository.addRenderableNPC(npc);
							}
						}
					}
				}
			}
		}
	}

	public boolean flagInactive(boolean force) {
		if (unload(force)) {
			active = false;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Flags the region as inactive.
	 */
	public boolean flagInactive() {
		return flagInactive(false);
	}

	/**
	 * Loads the flags for a region.
	 * @param r The region.
	 */
	public static void load(Region r) {
		load(r, r.build);
	}

	/**
	 * Loads the flags for a region.
	 * @param r The region.
	 * @param build If the region can be edited.
	 */
	public static void load(Region r, boolean build) {
		try {
			if (r.isLoaded() && r.isBuild() == build) {
				return;
			}
			r.build = build;
			boolean dynamic = r instanceof DynamicRegion;
			int regionId = dynamic ? ((DynamicRegion) r).getRegionId() : r.getId();
			int regionX = regionId >> 8 & 0xFF;
			int regionY = regionId & 0xFF;
			int mapscapeId = Cache.getIndexes()[5].getArchiveId("m" + regionX + "_"+ regionY);

			if (mapscapeId < 0 && !dynamic) {
				r.setLoaded(true);
				return;
			}

			byte[][][] mapscapeData = new byte[PLANES][SIZE][SIZE];
			for (int x = 0; x < CHUNKS_SIZE; x++) {
				for (int y = 0; y < CHUNKS_SIZE; y++) {
					for (int z = 0; z < PLANES; z++) {
						r.chunks[x][y][z].getFlags().setLandscape(new boolean[RegionChunk.SIZE][RegionChunk.SIZE]);
					}
				}
			}
			if (mapscapeId > -1) {
				ByteBuffer mapscape = ByteBuffer.wrap(Cache.getIndexes()[5].getCacheFile().getContainerUnpackedData(mapscapeId));
				MapscapeParser.parse(r, mapscapeData, mapscape);
			}
			r.hasFlags = dynamic;
			r.setLoaded(true);
			int landscapeId = Cache.getIndexes()[5].getArchiveId("l" + regionX + "_" + regionY);
			if (landscapeId > -1) {
				byte[] landscape = Cache.getIndexes()[5].getFileData(landscapeId, 0, XteaParser.Companion.getRegionXTEA(regionId));
				if (landscape == null || landscape.length < 4) {
					return;
				}
				r.hasFlags = true;
				try {
					LandscapeParser.parse(r, r.chunks, mapscapeData, ByteBuffer.wrap(landscape), build);
				} catch (Throwable t) {
					new Throwable("Failed parsing region " + regionId + "!", t).printStackTrace();
				}
			}
			MapscapeParser.clipMapscape(r, r.chunks, mapscapeData);
			for (Scenery object : r.removeSceneries) {
				// Get the actual object, not the instance that's kept in the removeScenery array
				Location loc = object.getLocation();
				Scenery realObject = RegionManager.getObject(loc.getX(), loc.getY(), loc.getZ(), object.getId(), object.getType());
				if (realObject != null) {
					SceneryBuilder.remove(realObject);
				}
			}
			for (Scenery object : r.addSceneries) {
				SceneryBuilder.add(object);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unloads the region.
	 */
	public boolean unload(boolean force) {
		if (!force && isViewed()) {
			log(CommunicationInfo.class, Log.ERR, "Players viewing region!");
			flagActive();
			return false;
		}
		for (int x = 0; x < CHUNKS_SIZE; x++) {
			for (int y = 0; y < CHUNKS_SIZE; y++) {
				for (int z = 0; z < PLANES; z++) {
					if (!force && !chunks[x][y][z].getPlayers().isEmpty()) {
						log(CommunicationInfo.class, Log.ERR, "Players still in region! (region id " + getId() + "; " + chunks[x][y][z].getPlayers().size() + " players on a chunk)");
						flagActive();
						return false;
					}
				}
			}
		}
		for (int x = 0; x < CHUNKS_SIZE; x++) {
			for (int y = 0; y < CHUNKS_SIZE; y++) {
				for (int z = 0; z < PLANES; z++) {
					if (!(this instanceof DynamicRegion)) {
						ArrayList<NPC> npcs = new ArrayList<>(chunks[x][y][z].getNpcs());
						for (NPC npc : npcs) {
							npc.onRegionInactivity();
						}
					}
					chunks[x][y][z].clear();
				}
			}
		}
		loaded = false;
		activityPulse.stop();
		return true;
	}

	/**
	 * Checks if the region is being viewed by a player.
	 * @return {@code True} if so.
	 */
	public boolean isViewed() {
		synchronized (this) {
			return viewAmount > 0;
		}
	}

	/**
	 * Increments the view amount.
	 * @return The view amount after incrementing.
	 */
	public int incrementViewAmount() {
		synchronized (this) {
			return ++viewAmount;
		}
	}

	/**
	 * Decrements the amount of viewers.
	 * @return The view amount after decrementing.
	 */
	public int decrementViewAmount() {
		synchronized (this) {
			if (viewAmount < 1) {
				//log(this.getClass(), Log.ERR,  "View amount is " + (viewAmount - 1));
				viewAmount++;
			}
			return --viewAmount;
		}
	}

	/**
	 * Gets the active.
	 * @return The active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Gets the region id.
	 * @return The region id.
	 */
	public int getId() {
		return x << 8 | y;
	}

	/**
	 * Gets the real region id (this returns the copied region id for dynamic regions).
	 * @return The region  id.
	 */
	public int getRegionId() {
		return getId();
	}

	/**
	 * Gets the x.
	 * @return The x.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y.
	 * @return The y.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the region-wide music track.
	 */
	public void setMusic(int music) {
		this.music = music;
	}

	/**
	 * Gets the region-wide music track
	 * @return The music entry ID
	 */
	public int getMusic() {
		return this.music;
	}

	/**
	 * Gets the regionZones.
	 * @return The regionZones.
	 */
	public List<RegionZone> getRegionZones() {
		return regionZones;
	}

	/**
	 * Gets the musicZones.
	 * @return The musicZones.
	 */
	public List<MusicZone> getMusicZones() {
		return musicZones;
	}

	/**
	 * Gets the object count.
	 * @return The object count.
	 */
	public int getObjectCount() {
		return objectCount;
	}

	/**
	 * Sets the object count.
	 * @param objectCount The object count.
	 */
	public void setObjectCount(int objectCount) {
		this.objectCount = objectCount;
	}

	/**
	 * Gets the hasFlags.
	 * @return The hasFlags.
	 */
	public boolean isHasFlags() {
		return hasFlags;
	}

	/**
	 * Sets the region time out duration.
	 * @param ticks The amount of ticks before the region is flagged as inactive.
	 */
	public void setRegionTimeOut(int ticks) {
		activityPulse.setDelay(ticks);
	}

	/**
	 * Gets the loaded.
	 * @return The loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Sets the loaded.
	 * @param loaded The loaded to set.
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * Gets the build.
	 * @return the build
	 */
	public boolean isBuild() {
		return build;
	}

	/**
	 * Sets the build.
	 * @param build the build to set.
	 */
	public void setBuild(boolean build) {
		this.build = build;
	}

	public List<Scenery> assembleObjectList(int z) {
		ArrayList<Scenery> list = new ArrayList<>();
		for (int x = 0; x < CHUNKS_SIZE; x++) {
			for (int y = 0; y < CHUNKS_SIZE; y++) {
				RegionChunk chunk = chunks[x][y][z];
				for (int offsetX = 0; offsetX < RegionChunk.SIZE; offsetX++) {
					for (int offsetY = 0; offsetY < RegionChunk.SIZE; offsetY++) {
						for (int i = 0; i < RegionChunk.ARRAY_SIZE; i++) {
							Scenery object = chunk.getObjects()[offsetX][offsetY][i];
							if (object != null) {
								list.add(object);
							}
						}
					}
				}
			}
		}
		return list;
	}

	public List<NPC> assembleNpcList(int z) {
		ArrayList<NPC> list = new ArrayList<>();
		for (int x = 0; x < CHUNKS_SIZE; x++) {
			for (int y = 0; y < CHUNKS_SIZE; y++) {
				RegionChunk chunk = chunks[x][y][z];
				list.addAll(chunk.getNpcs());
			}
		}
		return list;
	}

	public List<Player> assemblePlayerList(int z) {
		ArrayList<Player> list = new ArrayList<>();
		for (int x = 0; x < CHUNKS_SIZE; x++) {
			for (int y = 0; y < CHUNKS_SIZE; y++) {
				RegionChunk chunk = chunks[x][y][z];
				list.addAll(chunk.getPlayers());
			}
		}
		return list;
	}

	public List<Node> assembleNodeList(int z) {
		ArrayList<Node> list = new ArrayList<>(assembleObjectList(z));
		list.addAll(assemblePlayerList(z));
		list.addAll(assembleNpcList(z));
		return list;
	}

	/**
	 * Getter for region aggro tolerances.
	 */
	public HashMap<String, Long> getTolerances() {
		return tolerances;
	}

	/**
	 * Getters for addScenery and removeScenery.
	 */
	public ArrayList<Scenery> getAddSceneries() {
		return addSceneries;
	}
	public ArrayList<Scenery> getRemoveSceneries() {
		return removeSceneries;
	}
}

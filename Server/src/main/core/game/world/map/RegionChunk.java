package core.game.world.map;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.Item;
import core.game.node.scenery.Constructed;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.build.ChunkFlags;
import core.game.world.update.ChunkDirtyListener;
import core.game.world.update.flag.chunk.ItemUpdateFlag;
import core.net.packet.PacketRepository;
import core.net.packet.context.BuildItemContext;
import core.net.packet.out.*;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.build.LandscapeParser;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;
import core.tools.Log;
import org.rs09.consts.Items;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.log;

/**
 * Represents a region chunk.
 * @author Emperor, Player Name
 *
 */
public class RegionChunk {
	/**
	 * The maximum amount of objects to be stored on one tile in the chunk.
	 * Four objects per tile is the authentic RuneTek engine limit.
	 */
	public static final int ARRAY_SIZE = 4;

	/**
	 * The chunk size.
	 */
	public static final int SIZE = 8;

	/**
	 * Notified whenever a chunk is flagged, so the rendering pipeline can reset only the chunks that were actually touched.
	 */
	public static ChunkDirtyListener dirtyListener = chunk -> {};

	/**
	 * The base location of the copied region chunk.
	 */
	protected Location base;

	/**
	 * The current base location.
	 */
	protected Location currentBase;

	/**
	 * The items in this chunk.
	 */
	protected List<GroundItem> items = new ArrayList<>();

	/**
	 * The static objects in this chunk. Only used as a reference, do not act on these unless you are the landscape-
	 * parsing code.
	 * The game protocol requires us to send separate updates for dynamically added scenery and dynamically
	 * removed scenery. We implement this by maintaining two lists: one of static scenery, which contains the
	 * reference state obtained from the cache, and one of dynamic scenery, which is the actual current state of
	 * the sceneries on a chunk. This means that during landscape-parsing (and, crucially, only then), we need
	 * to add objects to both the static and dynamic lists. All object interactions in the game, however, only
	 * use the dynamic list (hence why that is just called 'objects').
	 */
	protected Scenery[][][] statObjects;

	/**
	 * The (dynamic, actual) objects in this chunk.
	 */
	protected Scenery[][][] objects;

	/**
	 * The rotation.
	 */
	protected int rotation;

	/**
	 * The update flags.
	 */
	private final List<UpdateFlag<?>> updateFlags = new ArrayList<>(20);

	/**
	 * The region flags.
	 */
	private ChunkFlags flags;

	/**
	 * The region projectile flags.
	 */
	private ChunkFlags projectileFlags;

	/**
	 * The players on this chunk.
	 */
	private final List<Player> players = new ArrayList<>();

	/**
	 * The NPCs on this chunk.
	 */
	private final List<NPC> npcs = new ArrayList<>();

	/**
	 * Constructs a new {@code RegionChunk} {@code Object}.
	 * @param base The base location of the region chunk.
	 * @param rotation The rotation.
	 */
	public RegionChunk(Location base, int rotation) {
		this.base = base;
		this.currentBase = base;
		this.rotation = rotation;
		this.statObjects = new Scenery[SIZE][SIZE][ARRAY_SIZE];
		this.objects = new Scenery[SIZE][SIZE][ARRAY_SIZE];
		this.flags = new ChunkFlags(base.getX(), base.getY(), base.getZ());
		this.projectileFlags = new ChunkFlags(base.getX(), base.getY(), base.getZ(), true);
	}

	/**
	 * Corrects objects' Locations when copying them from a template region into an instance - public version.
	 */
	public void rebaseObjects() {
		rebaseObjects(statObjects);
		rebaseObjects(objects);
	}

	/**
	 * Corrects objects' Locations when copying them from a template region into an instance - private version.
	 */
	private void rebaseObjects(Scenery[][][] objects) {
		for (int i = 0; i < ARRAY_SIZE; i++) {
			for (int x = 0; x < SIZE; x++) {
				for (int y = 0; y < SIZE; y++) {
					Scenery o = objects[x][y][i];
					if (o == null) {
						continue;
					}
					o.setLocation(getCurrentBase().transform(x, y, 0));
				}
			}
		}
	}

	/**
	 * Makes a deep copy of an object list - private version.
	 */
	private void copyObjects(Scenery[][][] src, Scenery[][][] dest) {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				for (int i = 0; i < ARRAY_SIZE; i++) {
					Scenery o = src[x][y][i];
					if (o == null) {
						continue;
					}
					Scenery copy = o.transform(o.getId());
					if (o instanceof Constructed) {
						dest[x][y][i] = copy.asConstructed();
					} else {
						dest[x][y][i] = copy;
					}
					dest[x][y][i].setActive(o.isActive());
					dest[x][y][i].setRenderable(o.isRenderable());
				}
			}
		}
	}

	/**
	 * Copies the region chunk.
	 * @return The destination chunk.
	 */
	public RegionChunk copy() {
		RegionChunk chunk = new RegionChunk(base, rotation);
		copyObjects(statObjects, chunk.statObjects); // note that the objects' locations have not been repointed to the new chunk yet, since we don't know _where_ the new chunk will be at this point
		copyObjects(objects, chunk.objects);
		return chunk;
	}

	/**
	 * Adds the scenery to the static and dynamic object lists. You never want this unless you are the landscape-parsing
	 * code.
	 * @param object The object to add.
	 */
	public void addStatDyn(Scenery object, int chunkOffsetX, int chunkOffsetY) {
		for (int i = 0; i < ARRAY_SIZE; i++) {
			Scenery current = statObjects[chunkOffsetX][chunkOffsetY][i];
			if (current == null) {
				statObjects[chunkOffsetX][chunkOffsetY][i] = object;
				objects[chunkOffsetX][chunkOffsetY][i] = object.transform(object.getId()); // deep copy so that active/renderable flags don't synchronize across multiple copies of e.g. a POH
				return;
			}
			if (current.getId() == object.getId()) {
				// It's possible that we already have this object. One instance of this was found at 1906, 5082, 0,
				// where our cache has more than one copy of the same [Scenery 15349, [1906, 5082, 0], type=22, rot=1]
				// object. Since authentically, the engine can only support one of the same type of object per tile,
				// if the ID matches we can be confident that this is a duplicate.
				return;
			}
		}
		throw new IllegalStateException("RC addStatDyn insufficient array length for storing object " + object);
	}

	/**
	 * Adds the scenery to the dynamic object list.
	 * @param object The object to add.
	 * @return The slot it was added into.
	 */
	public int add(Scenery object) {
		int chunkOffsetX = object.getLocation().getChunkOffsetX();
		int chunkOffsetY = object.getLocation().getChunkOffsetY();
		for (int i = 0; i < ARRAY_SIZE; i++) {
			Scenery current = objects[chunkOffsetX][chunkOffsetY][i];
			if (current == null) {
				objects[chunkOffsetX][chunkOffsetY][i] = object.asConstructed();
				object.setRenderable(true);
				object.setActive(true);
				return i;
			}
			if (current.equals(object)) {
				// Just reactivate the old instance
				current.setRenderable(true);
				current.setActive(true);
				return i;
			}
		}
		throw new IllegalStateException("RC add insufficient array length for storing object " + object);
	}

	/**
	 * Adds an NPC to this chunk.
	 * @param npc The NPC to add.
	 */
	public void add(NPC npc) {
		npcs.add(npc);
	}

	/**
	 * Adds a player to this chunk.
	 * @param player The player.
	 */
	public void addPlayer(Player player) {
		players.add(player);
		currentBase.getRegion().flagActive();
	}

	/**
	 * Adds an item to this region.
	 * @param item The item.
	 */
	public void add(GroundItem item) {
		getItems().add(item);
		if (item.isPrivate()) {
			if (item.getDropper() != null) {
				PacketRepository.send(ConstructGroundItem.class, new BuildItemContext(item.getDropper(), item));
			}
			return;
		}
		flag(new ItemUpdateFlag(item, ItemUpdateFlag.CONSTRUCT_TYPE));
	}

	/**
	 * Removes an NPC from this chunk.
	 * @param npc The NPC.
	 */
	public void remove(NPC npc) {
		npcs.remove(npc);
	}

	/**
	 * Removes a player from this chunk.
	 * @param player The player.
	 */
	public void removePlayer(Player player) {
		players.remove(player);
		currentBase.getRegion().checkInactive();
	}

	/**
	 * Removes an item from this chunk.
	 * @param item The ground item.
	 */
	public void remove(GroundItem item) {
		Location l = item.getLocation();
		if (!getItems().remove(item)) { // has null guard
			return;
		}
		if (item.isPrivate()) {
			if (item.getDropper() != null && item.getDropper().isPlaying() && item.getDropper().getLocation().withinDistance(l)) {
				PacketRepository.send(ClearGroundItem.class, new BuildItemContext(item.getDropper(), item));
			}
			return;
		}
		flag(new ItemUpdateFlag(item, ItemUpdateFlag.REMOVE_TYPE));
	}

	/**
	 * Rotation consts. Greg loves these :)
	 */
	private final static int NORTH_ROTATION = 0;
	private final static int EAST_ROTATION = 1;
	private final static int SOUTH_ROTATION = 2;
	private final static int WEST_ROTATION = 3;
	private final static int NUMBER_OF_CARDINAL_ROTATIONS = 4;

	/**
	 * Rotates the chunk.
	 * @param direction The direction.
	 */
	public void rotate(Direction direction) {
		if (rotation != 0) {
			log(this.getClass(), Log.ERR, "Region chunk was already rotated!");
			return;
		}
		Scenery[][][] oldStat = new Scenery[SIZE][SIZE][ARRAY_SIZE];
		Scenery[][][] oldDyn = new Scenery[SIZE][SIZE][ARRAY_SIZE];
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				for (int i = 0; i < ARRAY_SIZE; i++) {
					oldStat[x][y][i] = statObjects[x][y][i];
					Scenery object = objects[x][y][i];
					if (object != null) {
						oldDyn[x][y][i] = object;
						SceneryBuilder.remove(object);
					}
				}
			}
		}
		statObjects = new Scenery[SIZE][SIZE][ARRAY_SIZE];
		objects = new Scenery[SIZE][SIZE][ARRAY_SIZE];
		switch (direction) {
			case NORTH: rotation = NORTH_ROTATION; break;
			case EAST:  rotation = EAST_ROTATION; break;
			case SOUTH: rotation = SOUTH_ROTATION; break;
			case WEST:  rotation = WEST_ROTATION; break;
			default: rotation = (direction.toInteger() + (direction.toInteger() % 2 == 0 ? 2 : 0)) % NUMBER_OF_CARDINAL_ROTATIONS;
				log(this.getClass(), Log.ERR, "Attempted to rotate a chunk in a non-cardinal direction (" + direction.toInteger() + ") - using fallback rotation code. This should be investigated!");
				break;
		};
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				for (int i = 0; i < ARRAY_SIZE; i++) {
					Scenery stat = oldStat[x][y][i];
					if (stat != null) {
						int[] pos = getRotatedPosition(x, y, stat.getDefinition().getSizeX(), stat.getDefinition().getSizeY(), stat.getRotation(), rotation);
						int newX = pos[0];
						int newY = pos[1];
						Scenery obj = stat.transform(stat.getId(), (stat.getRotation() + rotation) % NUMBER_OF_CARDINAL_ROTATIONS, stat.getLocation().transform(newX - x, newY - y, 0));
						for (int j = 0; j < ARRAY_SIZE; j++) {
							if (statObjects[newX][newY][j] == null) {
								statObjects[newX][newY][j] = obj;
								break;
							}
						}
					}
					Scenery dyn = oldDyn[x][y][i];
					if (dyn != null) {
						int[] pos = getRotatedPosition(x, y, dyn.getDefinition().getSizeX(), dyn.getDefinition().getSizeY(), dyn.getRotation(), rotation);
						int newX = pos[0];
						int newY = pos[1];
						Scenery obj = dyn.transform(dyn.getId(), (dyn.getRotation() + rotation) % NUMBER_OF_CARDINAL_ROTATIONS, dyn.getLocation().transform(newX - x, newY - y, 0));
						SceneryBuilder.add(obj);
					}
				}
			}
		}
	}

	/**
	 * Gets the new coordinates for an object/chunk tile when rotating.
	 * @param x The current x-coordinate.
	 * @param y The current y-coordinate.
	 * @param sizeX The x-size of the object.
	 * @param sizeY The y-size of the object.
	 * @param rotation The object rotation.
	 * @param chunkRotation The chunk rotation.
	 * @return The new x-coordinate.
	 */
	public static int[] getRotatedPosition(int x, int y, int sizeX, int sizeY, int rotation, int chunkRotation) {
		if ((rotation & 0x1) == 1) {
			int s = sizeX;
			sizeX = sizeY;
			sizeY = s;
		}
		if (chunkRotation == EAST_ROTATION) {
			return new int[] { y, RegionChunk.SIZE - x - sizeX };
		}
		if (chunkRotation == SOUTH_ROTATION) {
			return new int[] { RegionChunk.SIZE - x - sizeX, RegionChunk.SIZE - y - sizeY };
		}
		if (chunkRotation == WEST_ROTATION) {
			return new int[] { RegionChunk.SIZE - y - sizeY, x };
		}
		return new int[] { x, y }; // NORTH_ROTATION
	}

	/**
	 * Gets the npcs.
	 * @return The npcs.
	 */
	public List<NPC> getNpcs() {
		return npcs;
	}

	/**
	 * Gets the players.
	 * @return The players.
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Registers an update flag.
	 * @param flag The flag.
	 */
	public void flag(UpdateFlag<?> flag) {
		updateFlags.add(flag);
		dirtyListener.onFlagged(this);
	}

	/**
	 * Clears the region chunk.
	 */
	public void clear() {
		updateFlags.clear();
		Region region = RegionManager.forId(currentBase.getRegionId());
		if (items != null && region instanceof DynamicRegion) {
			items.clear();
			items = null;
		}
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				for (int i = 0; i < ARRAY_SIZE; i++) {
					statObjects[x][y][i] = objects[x][y][i] = null;
				}
			}
		}
	}

	/**
	 * Updates the region chunk.
	 * @param player The player.
	 */
	public void synchronize(Player player) {
		IoBuffer buffer = UpdateAreaPosition.getChunkUpdateBuffer(player, currentBase);
		if (appendUpdate(player, buffer)) {
			player.getSession().write(buffer);
		}
	}

	/**
	 * Writes the region chunk update data on the buffer.
	 * @param player The player we're updating for.
	 * @param buffer The buffer to write on.
	 * @return {@code True} if an update occurred.
	 */
	protected boolean appendUpdate(Player player, IoBuffer buffer) {
		boolean updated = false;
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				for (int i = 0; i < ARRAY_SIZE; i++) {
					Scenery dyn = objects[x][y][i];
					Scenery stat = statObjects[x][y][i];
					if (stat == null && dyn == null) {
						continue;
					}
					if (stat != null) {
						if (stat.equals(dyn)) {
							continue;
						}
						boolean hasReplacement = false;
						for (int j = 0; j < ARRAY_SIZE; j++) {
							if (j == i) {
								continue;
							}
							Scenery other = objects[x][y][j];
							if (other == null) {
								continue;
							}
							if (other.getLayer() == stat.getLayer()) {
								hasReplacement = true;
								break;
							}
						}
						if (!hasReplacement) {
							ClearScenery.write(buffer, stat);
							updated = true;
						}
					}
					if (dyn != null && dyn.isRenderable()) {
						ConstructScenery.write(buffer, dyn);
						updated = true;
					}
				}
			}
		}
		ArrayList<GroundItem> totalItems = drawItems(items, player);
		for (GroundItem item : totalItems) {
			if (item != null && item.isActive() && item.getLocation() != null) {
				if (!item.isPrivate() || item.droppedBy(player)) {
					ConstructGroundItem.write(buffer, item);
					updated = true;
				}
			}
		}
		return updated;
	}

	public ArrayList<GroundItem> drawItems(List<GroundItem> items, Player player) {
		ArrayList<GroundItem> totalItems = items != null ? new ArrayList<>(items) : new ArrayList<>();

		if (player.getAttribute("chunkdraw", false)) {
			Location l = currentBase;
			for (int x = 0; x < SIZE; x++) {
				for (int y = 0; y < SIZE; y++) {
					boolean add = false;
					if (y == 0 || y == SIZE - 1)
						add = true;
					else if (x == 0 || x == SIZE - 1)
						add = true;
					if (add)
						totalItems.add(new GroundItem(new Item(Items.ABYSSAL_WHIP_13444), l.transform(x, y, 0), player));
				}
			}
		}

		if (player.getAttribute("regiondraw", false)) {
			Location l = currentBase;
			int localX = l.getLocalX();
			int localY = l.getLocalY();

			for (int x = 0; x < SIZE; x++)
				for (int y = 0; y < SIZE; y++) {
					boolean add = false;
					if (localY == 0 || localY == 56)
						if (localY == 0 && y == 0)
							add = true;
						else if (localY == 56 && y == SIZE - 1)
							add = true;
					if (localX == 0 || localX == 56)
						if (localX == 0 && x == 0)
							add = true;
						else if (localX == 56 && x == SIZE - 1)
							add = true;

					if (add)
						totalItems.add(new GroundItem(new Item(Items.ABYSSAL_WHIP_13444), l.transform(x,y,0), player));
				}
		}

		if (player.getAttribute("clippingdraw", false)) {
			Location l = currentBase;
			for (int x = 0; x < SIZE; x++) {
				for (int y = 0; y < SIZE; y++) {
					int flag = RegionManager.getClippingFlag(l.getZ(), l.getX() + x, l.getY() + y);
					if (flag > 0) {
						totalItems.add(new GroundItem(new Item(flag), l.transform(x, y, 0), player));
					}
				}
			}
		}

		return totalItems;
	}

	/**
	 * Sends all the update flags.
	 */
	public void update(Player player) {
		if (isUpdated()) {
			IoBuffer buffer = UpdateAreaPosition.getChunkUpdateBuffer(player, currentBase);
			Object[] flagsArray = updateFlags.toArray();
			int size = flagsArray.length;
			for (int i = 0; i < size; i++) {
				UpdateFlag<?> flag = (UpdateFlag<?>) flagsArray[i];
				flag.writeDynamic(buffer, player);
			}
			player.getSession().write(buffer);
		}
	}

	/**
	 * Gets the items.
	 * @return The items.
	 */
	public List<GroundItem> getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}
		return items;
	}

	/**
	 * Sets the items.
	 * @param items The items to set.
	 */
	public void setItems(List<GroundItem> items) {
		this.items = items;
	}

	/**
	 * Gets the dynamic sceneries located on the coordinates in this chunk.
	 * @param chunkOffsetX The x coordinate within the chunk (0-7).
	 * @param chunkOffsetY The y coordinate within the chunk (0-7).
	 * @return The objects.
	 */
	public Scenery[] getObjects(int chunkOffsetX, int chunkOffsetY) {
		return objects[chunkOffsetX][chunkOffsetY];
	}

	/**
	 * Gets the static sceneries located on the coordinates in this chunk.
	 * @param chunkOffsetX The x coordinate within the chunk (0-7).
	 * @param chunkOffsetY The y coordinate within the chunk (0-7).
	 * @return The objects.
	 */
	public Scenery[] getStatObjects(int chunkOffsetX, int chunkOffsetY) {
		return statObjects[chunkOffsetX][chunkOffsetY];
	}

	/**
	 * Gets the static objects.
	 * @return The static objects.
	 */
	public Scenery[][][] getStatObjects() {
		return statObjects;
	}

	/**
	 * Gets the objects.
	 * @return The objects.
	 */
	public Scenery[][][] getObjects() {
		return objects;
	}

	/**
	 * Gets the objects index for the given object id and/or scenery type.
	 * @param x The x-coordinate on the region chunk.
	 * @param y The y-coordinate on the region chunk.
	 * @param objectId The object id. -1 if any.
	 * @param type The scenery type. Ignored if objectId is set to >= -1.
	 */
	public int getIndex(int x, int y, int objectId, int type) {
		for (int i = 0; i < ARRAY_SIZE; i++) {
			Scenery o = objects[x][y][i];
			if (o != null) {
				if (objectId < 0) {
					if (type >= 0 && o.getType() != type) {
						continue;
					}
					return i;
				} else {
					if (o.getId() == objectId) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Gets the base.
	 * @return The base.
	 */
	public Location getBase() {
		return base;
	}

	/**
	 * Sets the base location of the region to copy.
	 * @param base The base location.
	 */
	public void setBase(Location base) {
		this.base = base;
	}

	/**
	 * Gets the rotation.
	 * @return The rotation.
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * Sets the rotation of the region chunk.
	 * @param rotation The rotation
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * Gets the updated.
	 * @return The updated.
	 */
	public boolean isUpdated() {
		return !updateFlags.isEmpty();
	}

	/**
	 * Resets the flags.
	 */
	public void resetUpdateFlags() {
		updateFlags.clear();
	}

	/**
	 * Gets the currentBase.
	 * @return The currentBase.
	 */
	public Location getCurrentBase() {
		return currentBase;
	}

	/**
	 * Sets the currentBase.
	 * @param currentBase The currentBase to set.
	 */
	public void setCurrentBase(Location currentBase) {
		this.currentBase = currentBase;
	}

	/**
	 * Reinitializes the clipping flags; used when a region is copied into a new dynamic region.
	 */
	public void resetClippingFlags() {
		flags = new ChunkFlags(currentBase.getX(), currentBase.getY(), currentBase.getZ());
		projectileFlags = new ChunkFlags(currentBase.getX(), currentBase.getY(), currentBase.getZ(), true);
	}

	/**
	 * Rebuilds the clipping flags based on those of a source chunk (used when a chunk is being replaced).
	 * @param from The region plane to get the new clipping flags from.
	 */
	public void rebuildClippingFlags(RegionChunk from) {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				// Import the landscape flags from the template chunk to the new chunk
				flags.getLandscape()[x][y] = from.getFlags().getLandscape()[x][y];
				projectileFlags.getLandscape()[x][y] = from.getProjectileFlags().getLandscape()[x][y];
				// Reflag any objects
				for (int i = 0; i < ARRAY_SIZE; i++) {
					Scenery obj = objects[x][y][i];
					if (obj != null) {
						LandscapeParser.flagScenery(this, x, y, obj, false, true);
					}
				}
			}
		}
	}

	public ChunkFlags getFlags() {
		return flags;
	}

	public ChunkFlags getProjectileFlags() {
		return projectileFlags;
	}

	public Region getRegion() {
		return RegionManager.forId(currentBase.getRegionId());
	}
}

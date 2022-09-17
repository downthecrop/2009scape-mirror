package core.game.node.entity.skill.construction;


//import org.arios.game.content.global.DeadmanTimedAction;
//import org.arios.game.node.entity.player.info.login.SavingModule;

import api.regionspec.RegionSpecification;
import api.regionspec.contracts.FillChunkContract;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.skill.Skills;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.map.*;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.update.flag.context.Animation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rs09.game.node.entity.skill.construction.Hotspot;
import rs09.game.system.SystemLogger;
import rs09.game.world.GameWorld;

import java.awt.*;
import java.nio.ByteBuffer;

import static api.regionspec.RegionSpecificationKt.fillWith;
import static api.regionspec.RegionSpecificationKt.using;

/**
 * Manages the player's house.
 * @author Emperor
 *
 */
public final class HouseManager {

	/**
	 * The current region.
	 */
	private DynamicRegion houseRegion;

	/**
	 * The current region.
	 */
	private DynamicRegion dungeonRegion;

	/**
	 * The house location.
	 */
	private HouseLocation location = HouseLocation.NOWHERE;

	/**
	 * The house style.
	 */
	private HousingStyle style = HousingStyle.BASIC_WOOD;

	/**
	 * The house zone.
	 */
	private final HouseZone zone = new HouseZone(this);

	/**
	 * The player's house rooms.
	 */
	private final Room[][][] rooms = new Room[4][8][8];

	/**
	 * If building mode is enabled.
	 */
	private boolean buildingMode;

	/**
	 * If the player has used the portal to lock their house.
	 */
	private boolean locked;

	/**
	 * The player's servant.
	 */
	private Servant servant;

	/**
	 * If the house has a dungeon.
	 */
	private boolean hasDungeon;

	/**
	 * The player's crest.
	 */
	private CrestType crest = CrestType.ASGARNIA;

	/**
	 * Constructs a new {@code HouseManager} {@code Object}.
	 */
	public HouseManager() {
		/*
		 * empty.
		 */
	}


	public void save(ByteBuffer buffer) {
		buffer.put((byte) location.ordinal());
		buffer.put((byte) style.ordinal());
		if (hasServant()) {
			servant.save(buffer);
		} else {
			buffer.put((byte) -1);
		}
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					Room room = rooms[z][x][y];
					if (room != null) {
						buffer.put((byte) z).put((byte) x).put((byte) y);
						buffer.put((byte) room.getProperties().ordinal());
						buffer.put((byte) room.getRotation().toInteger());
						for (int i = 0; i < room.getHotspots().length; i++) {
							if (room.getHotspots()[i].getDecorationIndex() > -1) {
								buffer.put((byte) i);
								buffer.put((byte) room.getHotspots()[i].getDecorationIndex());
							}
						}
						buffer.put((byte) -1);
					}
				}
			}
		}
		buffer.put((byte) -1);//Eof
	}

	public void parse(JSONObject data){
		location = HouseLocation.values()[Integer.parseInt( data.get("location").toString())];
		style = HousingStyle.values()[Integer.parseInt( data.get("style").toString())];
		Object servRaw = data.get("servant");
		if(servRaw != null){
			servant = Servant.parse((JSONObject) servRaw);
		}
		JSONArray rArray = (JSONArray) data.get("rooms");
		for(int i = 0; i < rArray.size(); i++){
			JSONObject rm = (JSONObject) rArray.get(i);
			int z =  Integer.parseInt(rm.get("z").toString());
			int x = Integer.parseInt(rm.get("x").toString());
			int y = Integer.parseInt(rm.get("y").toString());
			if(z == 3)
				hasDungeon = true;
			Room room = rooms[z][x][y] = new Room(RoomProperties.values()[Integer.parseInt(rm.get("properties").toString())]);
			room.configure(style);
			room.setRotation(Direction.get(Integer.parseInt(rm.get("rotation").toString())));
			JSONArray hotspots = (JSONArray) rm.get("hotspots");
			for(int j = 0; j < hotspots.size(); j++){
				JSONObject spot = (JSONObject) hotspots.get(j);
				room.getHotspots()[Integer.parseInt(spot.get("hotspotIndex").toString())].setDecorationIndex(Integer.parseInt(spot.get("decorationIndex").toString()));
			}
		}
	}


	public void parse(ByteBuffer buffer) {
		location = HouseLocation.values()[buffer.get() & 0xFF];
		style = HousingStyle.values()[buffer.get() & 0xFF];
		servant = Servant.parse(buffer);
		int z = 0;
		while ((z = buffer.get()) != -1) {
			if (z == 3) {
				hasDungeon = true;
			}
			int x = buffer.get();
			int y = buffer.get();
			Room room = rooms[z][x][y] = new Room(RoomProperties.values()[buffer.get() & 0xFF]);
			room.configure(style);
			room.setRotation(Direction.get(buffer.get() & 0xFF));
			int spot = 0;
			while ((spot = buffer.get()) != -1) {
				room.getHotspots()[spot].setDecorationIndex(buffer.get() & 0xFF);
			}
		}
	}

	/**
	 * Enter's the player's house.
	 * @param player
	 * @param buildingMode
	 */
	public void enter(final Player player, boolean buildingMode) {
		if (this.buildingMode != buildingMode || !isLoaded()) {
			this.buildingMode = buildingMode;
			construct();
		}
		player.setAttribute("poh_entry", HouseManager.this);
		player.lock(1);
		player.sendMessage("House location: " + houseRegion.getBaseLocation() + ", entry: " + getEnterLocation());
		player.getProperties().setTeleportLocation(getEnterLocation());
		openLoadInterface(player);
		checkForAndSpawnServant(player);
		updateVarbits(player, buildingMode);
		unlockMusicTrack(player);
	}

	private void openLoadInterface(Player player) {
		player.getInterfaceManager().openComponent(399);
		player.getAudioManager().send(new Audio(984));
		submitCloseLoadInterfacePulse(player);
	}

	private void submitCloseLoadInterfacePulse(Player player) {
		GameWorld.getPulser().submit(new Pulse(1, player) {
			@Override
			public boolean pulse() {
				player.getInterfaceManager().close();
				return true;
			}
		});
	}

	private void checkForAndSpawnServant(Player player) {
		if(!hasServant()) return;

		GameWorld.getPulser().submit(new Pulse(1, player) {
			@Override
			public boolean pulse() {
				spawnServant();
				if (servant.isGreet()){
					player.getDialogueInterpreter().sendDialogues(servant.getType().getId(), servant.getType().getId() == 4243 ? FacialExpression.HALF_GUILTY : null, "Welcome.");
				}
				return true;
			}
		});
	}

	private void updateVarbits(Player player, boolean build) {
		player.varpManager.get(261).setVarbit(0, build ? 1 : 0);
		player.varpManager.get(262).setVarbit(0, getRoomAmount());
	}

	private void unlockMusicTrack(Player player) {
		player.getMusicPlayer().unlock(454, true);
	}

	/**
	 * Leaves this house.
	 * @param player The player leaving.
	 */
	public static void leave(Player player) {
		HouseManager house = player.getAttribute("poh_entry", player.getHouseManager());
		if (house.getHouseRegion() == null){
			return;
		}
		if (house.isInHouse(player)) {
			player.animate(Animation.RESET);
			player.setLocation(house.location.getExitLocation());
		}
	}

	/**
	 * Toggles the building mode.
	 * @param player The house owner.
	 * @param enable If the building mode should be enabled.
	 */
	public void toggleBuildingMode(Player player, boolean enable) {
		if (!isInHouse(player)) {
			player.getPacketDispatch().sendMessage("Building mode really only helps if you're in a house.");
			return;
		}
		if (buildingMode != enable) {
			if (enable) {
				expelGuests(player);
			}
			buildingMode = enable;
			reload(player, enable);
			player.getPacketDispatch().sendMessage("Building mode is now " + (buildingMode ? "on." : "off."));
		}
	}

	/**
	 * Reloads the house.
	 * @param player The player.
	 * @param buildingMode If building mode should be enabled.
	 */
	public void reload(Player player, boolean buildingMode) {
		int diffX = player.getLocation().getLocalX();
		int diffY = player.getLocation().getLocalY();
		int diffZ = player.getLocation().getZ();
		boolean inDungeon = player.getViewport().getRegion() == dungeonRegion;
		this.buildingMode = buildingMode;
		construct();
		Location newLoc = (dungeonRegion == null ? houseRegion : (inDungeon ? dungeonRegion : houseRegion)).getBaseLocation().transform(diffX,diffY,diffZ);
		player.getProperties().setTeleportLocation(newLoc);
	}

	/**
	 * Expels the guests from the house.
	 * @param player The house owner.
	 */
	public void expelGuests(Player player) {
		if (isLoaded()) {
			for (RegionPlane plane : houseRegion.getPlanes()) {
				for (Player p : plane.getPlayers()) {
					if (p != player) {
						leave(p);
					}
				}
			}
			if (dungeonRegion != null) {
				for (RegionPlane plane : dungeonRegion.getPlanes()) {
					for (Player p : plane.getPlayers()) {
						if (p != player) {
							leave(p);
						}
					}
				}
			}
		}
	}

	/**
	 * Gets the entering location.
	 * @return The entering location.
	 */
	public Location getEnterLocation() {
		if (houseRegion == null) {
			SystemLogger.logErr(this.getClass(), "House wasn't constructed yet!");
			return null;
		}
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Room room = rooms[0][x][y];
				if (room != null && (room.getProperties() == RoomProperties.GARDEN || room.getProperties() == RoomProperties.FORMAL_GARDEN)) {
					for (Hotspot h : room.getHotspots()) {
						if (h.getDecorationIndex() > -1) {
							Decoration d = h.getHotspot().getDecorations()[h.getDecorationIndex()];
							if (d == Decoration.PORTAL) {
								return houseRegion.getBaseLocation().transform(x * 8 + h.getChunkX(), y * 8 + h.getChunkY() + 2, 0);
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Redecorates the house.
	 * @param style The new style.
	 */
	public void redecorate(HousingStyle style) {
		this.style = style;
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					Room room = rooms[z][x][y];
					if (room != null) {
						room.decorate(style);
					}
				}
			}
		}
	}

	/**
	 * Clears all the rooms (<b>Including portal room!</b>).
	 */
	@Deprecated
	public void clearRooms() {
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					rooms[z][x][y] = null;
				}
			}
		}
	}

	/**
	 * Creates the default house.
	 * @param location The house location.
	 */
	public void createNewHouseAt(HouseLocation location) {
		clearRooms();
		Room room = rooms[0][4][3] = new Room(RoomProperties.GARDEN);
		room.configure(style);
		room.getHotspots()[0].setDecorationIndex(0);
		this.location = location;
	}

	/**
	 * Constructs the dynamic region for the house.
	 * @return The region.
	 */
	public DynamicRegion construct() {
		houseRegion = getPreparedRegion();
		configureRoofs();
		prepareHouseChunks(style, houseRegion, buildingMode, rooms);

		if (hasDungeon()) {
			dungeonRegion = getPreparedRegion();
			prepareDungeonChunks(style, dungeonRegion, houseRegion, buildingMode, rooms[3]);
		}

		ZoneBuilder.configure(zone);
		return houseRegion;
	}

	private DynamicRegion getPreparedRegion() {
		ZoneBorders borders = DynamicRegion.reserveArea(8,8);
		DynamicRegion region = new DynamicRegion(-1, borders.getSouthWestX() >> 6, borders.getSouthWestY() >> 6);
		region.setBorders(borders);
		region.setUpdateAllPlanes(true);
		RegionManager.addRegion(region.getId(), region);
		return region;
	}

	private class RoomLoadContract extends FillChunkContract {
		Room[][][] rooms;
		HouseManager manager;
		boolean buildingMode;

		public RoomLoadContract(HouseManager manager, boolean buildingMode, Room[][][] rooms) {
			this.rooms = rooms;
			this.manager = manager;
			this.buildingMode = buildingMode;
		}

		@Override
		public BuildRegionChunk getChunk(int x, int y, int plane, @NotNull DynamicRegion dyn) {
			BuildRegionChunk chunk = rooms[plane][x][y].getChunk().copy(dyn.getPlanes()[plane]);
			return chunk;
		}

		@Override
		public void afterSetting(@Nullable BuildRegionChunk chunk, int x, int y, int plane, @NotNull DynamicRegion dyn) {
			rooms[plane][x][y].loadDecorations(dyn != manager.dungeonRegion ? plane : 3, chunk, manager);
		}
	}

	private void prepareHouseChunks(HousingStyle style, DynamicRegion target, boolean buildingMode, Room[][][] rooms) {
		Region from = RegionManager.forId(style.getRegionId());
		Region.load(from, true);
		RegionChunk defaultChunk = from.getPlanes()[style.getPlane()].getRegionChunk(1, 0);
		RegionChunk defaultSkyChunk = from.getPlanes()[1].getRegionChunk(0,0);

		RoomLoadContract loadRooms = new RoomLoadContract(this, buildingMode, rooms);
		RegionSpecification spec = new RegionSpecification(
				using(target),
				fillWith(defaultChunk)
						.from(from)
						.onPlanes(0)
						.onCondition((destX, destY, plane) -> rooms[plane][destX][destY] == null),
				fillWith((RegionChunk) null)
						.from(from)
						.onPlanes(1,2)
						.onCondition((destX, destY, plane) -> rooms[plane][destX][destY] == null),
				fillWith(defaultSkyChunk)
						.from(from)
						.onPlanes(3),
				loadRooms
						.from(from)
						.onPlanes(0,1,2)
						.onCondition((destX,destY,plane) -> rooms[plane][destX][destY] != null)
		);

		spec.build();
	}

	private void prepareDungeonChunks(HousingStyle style, DynamicRegion target, DynamicRegion house, boolean buildingMode, Room[][] rooms) {
		Region from = RegionManager.forId(style.getRegionId());
		Region.load(from, true);
		RegionChunk defaultChunk = from.getPlanes()[style.getPlane()].getRegionChunk(3, 0);

		RoomLoadContract loadRooms = new RoomLoadContract(this, buildingMode, new Room[][][]{rooms});
		RegionSpecification spec = new RegionSpecification(
				using(target),
				fillWith((x,y,plane,region) -> buildingMode ? null : defaultChunk)
						.from(from)
						.onPlanes(0)
						.onCondition((destX, destY, plane) -> rooms[destX][destY] == null),
				loadRooms
						.from(from)
						.onPlanes(0)
						.onCondition((destX, destY, plane) -> rooms[destX][destY] != null)
		);

		spec.build();
		house.link(target);
	}

	/**
	 * Configures the rooftops.
	 */
	public void configureRoofs() {
//		boolean[][][] roofs = new boolean[2][8][8];
//		for (int x = 0; x < 8; x++) {
//			for (int y = 0; y < 8; y++) {
//				Room room = rooms[0][x][y];
//				if (room != null && room.getProperties().isChamber()) {
//					room = rooms[1][x][y];
//					int z = 1;
//					if (room != null && room.getProperties().isChamber()) {
//						z = 2;
//					}
//					if (x > 0 )
//				}
//			}
//		}
	}

	/**
	 * Gets the current room plane.
	 * @param l The location.
	 * @return The plane of the room.
	 */
	public Room getRoom(Location l) {
		int z = l.getZ();
		if (dungeonRegion != null && l.getRegionId() == dungeonRegion.getId()) {
			z = 3;
		}
		return rooms[z][l.getChunkX()][l.getChunkY()];
	}

	/**
	 * Gets the hotspot for the given object.
	 * @param object The object.
	 * @return The hotspot.
	 */
	public Hotspot getHotspot(Scenery object) {
		Room room = getRoom(object.getLocation());
		if (room == null) {
			return null;
		}
		int chunkX = object.getLocation().getChunkOffsetX();
		int chunkY = object.getLocation().getChunkOffsetY();
		switch(room.getRotation()){
			case WEST: {
				int tempChunk = chunkY;
				chunkY = 7 - chunkX;
				chunkX = tempChunk;
				break;
			}
			case EAST: {
				//x = y, y = x, x = 7 - y
				int tempChunk = chunkX;
				chunkX = 7 - chunkY;
				chunkY = tempChunk;
				break;
			}
			case SOUTH: {
				chunkY = 7 - chunkY;
				break;
			}
			default: {

			}
		}
		for (Hotspot h : room.getHotspots()) {
			if ((h.getChunkX() == chunkX || h.getChunkX2() == chunkX) && (h.getChunkY() == chunkY || h.getChunkY2() == chunkY) && h.getHotspot().getObjectId(style) == object.getId()) {
				return h;
			}
		}
		return null;
	}

	/**
	 * Checks if a room exists on the given location.
	 * @param z The plane.
	 * @param roomX The room x-coordinate.
	 * @param roomY The room y-coordinate.
	 * @return {@code True} if so.
	 */
	public boolean hasRoomAt(int z, int roomX, int roomY) {
		Room room = rooms[z][roomX][roomY];
		return room != null && !room.getProperties().isRoof();
	}

	/**
	 * Enters the dungeon.
	 * @param player The player.
	 */
	public void enterDungeon(Player player) {
		if (!hasDungeon()) {
			return;
		}
		int diffX = player.getLocation().getLocalX();
		int diffY = player.getLocation().getLocalY();
		player.getProperties().setTeleportLocation(dungeonRegion.getBaseLocation().transform(diffX, diffY, 0));
	}

	/**
	 * Checks if an exit exists on the given room.
	 * @param roomX The x-coordinate of the room.
	 * @param roomY The y-coordinate of the room.
	 * @param direction The exit direction.
	 * @return {@code True} if so.
	 */
	public boolean hasExit(int z, int roomX, int roomY, Direction direction) {
		Room room = rooms[z][roomX][roomY];
		int index = (direction.toInteger() + 3) % 4;
		return room != null && room.getExits()[index];
	}

	/**
	 * Gets the amount of rooms.
	 * @return The amount of rooms.
	 */
	public int getRoomAmount() {
		int count = 0;
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					Room r = rooms[z][x][y];
					if (r != null && !r.getProperties().isRoof()) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * Gets the amount of portals available.
	 * @return The amount of portals.
	 */
	public int getPortalAmount() {
		int count = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Room room = rooms[0][x][y];
				if (room != null && (room.getProperties() == RoomProperties.GARDEN
						|| room.getProperties() == RoomProperties.FORMAL_GARDEN) && room.getHotspots()[0].getDecorationIndex() == 0) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Gets the current house boundaries.
	 * @return The boundaries.
	 */
	public Rectangle getBoundaries() {
		int startX = 99;
		int startY = 99;
		int endX = 0;
		int endY = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (rooms[0][x][y] != null) {
					if (x < startX) startX = x;
					if (y < startY) startY = y;
					if (x > endX) endX = x;
					if (y > endY) endY = y;
				}
			}
		}
		return new Rectangle(startX, startY, (endX - startX) + 1, (endY - startY) + 1);
	}

	/**
	 * Gets the maximum dimension for the house boundaries.
	 * @param player The player.
	 * @return The dimension value (value X value = dimension)
	 */
	public int getMaximumDimension(Player player) {
		int level = player.getSkills().getStaticLevel(Skills.CONSTRUCTION);
		if (level >= 60) {
			return 7;
		}
		if (level >= 45) {
			return 6;
		}
		if (level >= 30) {
			return 5;
		}
		if (level >= 15) {
			return 4;
		}
		return 3;
	}

	/**
	 * Gets the maximum amount of rooms available for the player.
	 * @param player The player.
	 * @return The maximum amount of rooms.
	 */
	public int getMaximumRooms(Player player) {
		int level = player.getSkills().getStaticLevel(Skills.CONSTRUCTION);
		if (level >= 99) return 30;
		if (level >= 96) return 29;
		if (level >= 92) return 28;
		if (level >= 86) return 27;
		if (level >= 80) return 26;
		if (level >= 74) return 25;
		if (level >= 68) return 24;
		if (level >= 62) return 23;
		if (level >= 56) return 22;
		if (level >= 50) return 21;
		return 20;
	}

	/**
	 * Spawns the servant inside the player's home.
	 */
	private void spawnServant(){
		servant.setLocation(getEnterLocation());
		servant.setWalkRadius(getRoomAmount() * 2);
		servant.setWalks(true);
		servant.init();
	}

	/**
	 * Checks if the player has a servant.
	 * @return {@code True} if so.
	 */
	public boolean hasServant() {
		return servant != null;
	}

	/**
	 * Checks if the player is in his own house (or dungeon).
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public boolean isInHouse(Player player) {
		return isLoaded() && (player.getViewport().getRegion() == houseRegion || player.getViewport().getRegion() == dungeonRegion);
	}

	/**
	 * Checks if the player is in his dungeon.
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public static boolean isInDungeon(Player player) {
		return player.getViewport().getRegion() == player.getHouseManager().dungeonRegion;
	}

	/**
	 * Checks if the house region was constructed and active.
	 * @return {@code True} if an active region for the house exists.
	 */
	public boolean isLoaded() {
		return (houseRegion != null) || (dungeonRegion != null);
	}

	/**
	 * Gets the hasHouse.
	 * @return The hasHouse.
	 */
	public boolean hasHouse() {
		return location != HouseLocation.NOWHERE;
	}

	/**
	 * Checks if the house has a dungeon.
	 * @return {@code True} if so.
	 */
	public boolean hasDungeon() {
		return hasDungeon;
	}

	/**
	 * Sets the has dungeon value.
	 * @param hasDungeon If the house has a dungeon.
	 */
	public void setHasDungeon(boolean hasDungeon) {
		this.hasDungeon = hasDungeon;
	}

	/**
	 * Gets the rooms.
	 * @return The rooms.
	 */
	public Room[][][] getRooms() {
		return rooms;
	}

	/**
	 * Gets the location.
	 * @return The location.
	 */
	public HouseLocation getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * @param location The location to set.
	 */
	public void setLocation(HouseLocation location) {
		this.location = location;
	}

	/**
	 * Checks if the building mode is enabled.
	 * @return {@code True} if so.
	 */
	public boolean isBuildingMode() {
		return buildingMode;
	}

	/**
	 * Checks if the player has locked their house.
	 * @return {@code True} if so.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the house to locked.
	 * @param locked true or false
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * Gets the region.
	 * @return The region.
	 */
	public DynamicRegion getHouseRegion() {
		return houseRegion;
	}

	/**
	 * Gets the dungeon region.
	 * @return The dungeon region.
	 */
	public Region getDungeonRegion() {
		return dungeonRegion;
	}

	/**
	 * Gets the style.
	 * @return the style
	 */
	public HousingStyle getStyle() {
		return style;
	}

	/**
	 * Sets the style.
	 * @param style the style to set.
	 */
	public void setStyle(HousingStyle style) {
		this.style = style;
	}

	/**
	 * Gets the player's servant
	 * @return the servant.
	 */
	public Servant getServant(){
		return servant;
	}

	/**
	 * Sets the player's servant
	 * @param servant The servant to set.
	 */
	public void setServant(Servant servant){
		this.servant = servant;
	}

	/**
	 * Gets the crest value.
	 * @return The crest.
	 */
	public CrestType getCrest() {
		return crest;
	}

	/**
	 * Sets the crest value.
	 * @param crest The crest to set.
	 */
	public void setCrest(CrestType crest) {
		this.crest = crest;
	}

	/**
	 * @return the zone
	 */
	public HouseZone getZone() {
		return zone;
	}
}
package core.game.node.entity.skill.construction.decoration;

import core.cache.def.impl.ObjectDefinition;
import core.plugin.Initializable;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.skill.construction.*;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.object.GameObject;
import core.game.node.object.ObjectBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.plugin.Plugin;
import core.plugin.PluginManager;

/**
 * Handles construction staircases.
 * @author Emperor
 *
 */
@Initializable
public final class StaircasePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		PluginManager.definePlugin(new BuildDialogue());
		PluginManager.definePlugin(new ClimbPohLadder());
		for (int i = 13497; i < 13507; i++) {
			ObjectDefinition.forId(i).getHandlers().put("option:climb", this);
			ObjectDefinition.forId(i).getHandlers().put("option:climb-up", this);
			ObjectDefinition.forId(i).getHandlers().put("option:climb-down", this);
			ObjectDefinition.forId(i).getHandlers().put("option:remove-room", this);
		}
		ObjectDefinition.forId(13409).getHandlers().put("option:enter", this);
		ObjectDefinition.forId(13409).getHandlers().put("option:remove-room", this);
		for (int id = 13328; id < 13331; id++) {
			ObjectDefinition.forId(id).getHandlers().put("option:climb", this);
			ObjectDefinition.forId(id).getHandlers().put("option:remove-room", this);
		}
		for (int id = 13675; id <= 13680; id++) {
			if (id < 13678) {
				ObjectDefinition.forId(id).getHandlers().put("option:open", this);
			} else {
				ObjectDefinition.forId(id).getHandlers().put("option:go-down", this);
				ObjectDefinition.forId(id).getHandlers().put("option:close", this);
			}
			ObjectDefinition.forId(id).getHandlers().put("option:remove-room", this);
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		HouseManager house = player.getAttribute("poh_entry", null);
		if (house == null) {
			player.getPacketDispatch().sendMessage("You're not in your house right now (REPORT).");
			return true;
		}
		GameObject object = (GameObject) node;
		switch (option) {
		case "open":
			ObjectBuilder.replace(object, object.transform(object.getId() + 3), 200);
			return true;
		case "close":
			ObjectBuilder.replace(object, object.transform(object.getId() - 3));
			return true;
		case "remove-room":
			if (player.getLocation().getZ() != 0) {
				player.getPacketDispatch().sendMessage("The room below is supporting this room!");
				return true;
			}
			return false;
		case "climb":
			if (house.getDungeonRegion() == player.getViewport().getRegion()) {
				climb(player, 1, house, object);
				return true;
			}
			if (object.getLocation().getZ() > 0) {
				climb(player, -1, house, object);
				return true;
			}
			player.getDialogueInterpreter().open("con:climbdial", house, object);
			return true;
		case "climb-up":
			climb(player, 1, house, object);
			return true;
		case "enter":
		case "climb-down":
		case "go-down":
			climb(player, -1, house, object);
			return true;
		}
		return false;
	}
	
	/**
	 * Climbs the staircase.
	 * @param player The player.
	 * @param z The plane difference.
	 * @param house The house the player is currently in.
	 * @param object The object.
	 */
	private static void climb(Player player, int z, HouseManager house, GameObject object) {
		Location l = player.getLocation();
		int plane = l.getZ() + z;
		int roomX = l.getChunkX();
		int roomY = l.getChunkY();
		Room current = house.getRooms()[l.getZ()][roomX][roomY];
		if (plane < 0) { //Dungeon
			plane = 3;
		}
		else if (player.getViewport().getRegion() == house.getDungeonRegion() && plane == 1) {//going up
			plane = 0;
		}
		Room room = house.getRooms()[plane][roomX][roomY];
//		boolean stairs = room != null && room.getStairs() != null || room.get; 
		if (room == null || room.getProperties().isRoof()) {
			if (player.getHouseManager().isInHouse(player) && player.getHouseManager().isBuildingMode()) {
				player.getDialogueInterpreter().open("con:nfroom", plane, roomX, roomY, current, object);
			}
			else {
				player.getPacketDispatch().sendMessage("This doesn't seem to lead anywhere.");
			}
		} else {
			Location destination = l.transform(0, 0, z);
			if (player.getViewport().getRegion() == house.getDungeonRegion()) {
				destination = house.getRegion().getBaseLocation().transform(l.getLocalX(), l.getLocalY(), 0);
			}
			else if (plane == 3) {
				destination = house.getDungeonRegion().getBaseLocation().transform(l.getLocalX(), l.getLocalY(), 0);
			}
			Room r = house.getRoom(destination);
			Hotspot h = r.getStairs();
			if (h != null && h.getDecorationIndex() > -1) {
				player.getProperties().setTeleportLocation(destination);
			} else {
				player.getPacketDispatch().sendMessage("This doesn't seem to lead anywhere.");
			}
		}
	}

	/**
	 * Handles the climbing dialogue.
	 * @author Emperor
	 */
	static final class ClimbPohLadder extends DialoguePlugin {

		/**
		 * Represents the object to use.
		 */
		private HouseManager house;
		
		/**
		 * The ladder.
		 */
		private GameObject ladder;

		/**
		 * Constructs a new {@code ClimbPohLadder} {@code Object}.
		 */
		public ClimbPohLadder() {
			super();
		}

		/**
		 * Constructs a new {@code ClimbPohLadder} {@code Object}.
		 * @param player the player.
		 */
		public ClimbPohLadder(final Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new ClimbPohLadder(player);
		}

		@Override
		public boolean open(Object... args) {
			house = (HouseManager) args[0];
			ladder = (GameObject) args[1];
			interpreter.sendOptions("What would you like to do?", "Climb Up.", "Climb Down.");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				switch (buttonId) {
				case 1:
					player.lock(1);
					GameWorld.getPulser().submit(new Pulse(1) {
						@Override
						public boolean pulse() {
							climb(player, 1, house, ladder);
							return true;
						}
					});
					end();
					break;
				case 2:
					player.lock(1);
					GameWorld.getPulser().submit(new Pulse(1) {
						@Override
						public boolean pulse() {
							climb(player, -1, house, ladder);
							return true;
						}
					});
					end();
					break;

				}
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { DialogueInterpreter.getDialogueKey("con:climbdial") };
		}

	}

	/**
	 * Handles the creating a room on different floor dialogue.
	 * @author Emperor
	 */
	static final class BuildDialogue extends DialoguePlugin {
		
		/**
		 * The plane of the room to build.
		 */
		private int plane;
		
		/**
		 * The room x-coordinate.
		 */
		private int roomX;

		/**
		 * The room y-coordinate.
		 */
		private int roomY;
		
		/**
		 * The room we're building on.
		 */
		private Room room;

		/**
		 * The stairs object.
		 */
		private GameObject stairs;
		
		/**
		 * Constructs a new {@code BuildDialogue} {@code Object}.
		 */
		public BuildDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code BuildDialogue} {@code Object}.
		 * @param player the player.
		 */
		public BuildDialogue(final Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new BuildDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			plane = (Integer) args[0];
			roomX = (Integer) args[1];
			roomY = (Integer) args[2];
			room = (Room) args[3];
			stairs = (GameObject) args[4];
			stage = 0;
			if (stairs.getId() >= 13328 && stairs.getId() <= 13330) {
				interpreter.sendPlainMessage(false, "These stairs don't seem to lead anywhere. Do you", "want to build a throne room upstairs?");
				stage = 5;
				return true;
			}
			if (plane == 3) {
				if (room.getProperties() == RoomProperties.THRONE_ROOM) {
					interpreter.sendPlainMessage(false, "These stairs don't seem to lead anywhere. Do you", "want to build an Oubilette?");
				} else {
					interpreter.sendPlainMessage(false, "These stairs don't seem to lead anywhere. Do you", "want to build a dungeon room?");
				}
				return true;
			}
			interpreter.sendPlainMessage(false, "These stairs don't seem to lead anywhere. Do you", "want to build a room at the top?");
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			boolean dungeon = plane == 3;
			switch (stage) {
			case 0:
				interpreter.sendOptions("Select an option", "Yes", "No");
				stage = 1;
				break;
			case 1:
				switch (buttonId) {
				case 1: //yes
					if (room.getProperties() == RoomProperties.THRONE_ROOM) {
						Room r = Room.create(player, RoomProperties.OUBILETTE);
						Direction[] dirs = BuildingUtils.getAvailableRotations(player, r.getExits(), plane, roomX, roomY);
						for (Direction d : dirs) {
							if (d == room.getRotation()) {
								r.setRotation(d);
								Hotspot stairs = room.getStairs();
								int index = stairs != null ? stairs.getDecorationIndex() : -1;
								BuildingUtils.buildRoom(player, r, plane, roomX, roomY, r.getExits(), true);
								r.getStairs().setDecorationIndex(index);
								end();
								return true;
							}
						}
						interpreter.sendPlainMessage(false, "The room you're trying to build doesn't fit.");
						stage = 4;
						return true;
					}
					if (dungeon) {
						interpreter.sendOptions("Select an option", "Skill Hall", "Quest Hall", "Dungeon Stairs");
					} else {
						interpreter.sendOptions("Select an option", "Skill Hall", "Quest Hall");
					}
					stage = 2;
					return true;
				}
				end();
				return true;
			case 2:
				if (dungeon && buttonId == 3) {
					stage = 3;
					return handle(interfaceId, 1);
				}
				RoomProperties props = buttonId == 2 ? RoomProperties.QUEST_HALL_2 : RoomProperties.SKILL_HALL_2;
				if (plane == 3) {
					props = buttonId == 2 ? RoomProperties.QUEST_HALL : RoomProperties.SKILL_HALL;
				}
				Room r = Room.create(player, props);
				Direction[] dirs = BuildingUtils.getAvailableRotations(player, r.getExits(), plane, roomX, roomY);
				for (Direction d : dirs) {
					if (d == room.getRotation()) {
						r.setRotation(d);
						Hotspot stairs = room.getStairs();
						int index = stairs != null ? stairs.getDecorationIndex() : -1;
						r.getStairs().setDecorationIndex(index);
						BuildingUtils.buildRoom(player, r, plane, roomX, roomY, r.getExits(), true);
						end();
						return true;
					}
				}
				interpreter.sendPlainMessage(false, "The room you're trying to build doesn't seem to fit.");
				stage = 4;
				return true;
			case 3:
				switch (buttonId) {
				case 1: //yes
					r = Room.create(player, RoomProperties.DUNGEON_STAIRS);
					dirs = BuildingUtils.getAvailableRotations(player, r.getExits(), plane, roomX, roomY);
					for (Direction d : dirs) {
						if (d == room.getRotation()) {
							r.setRotation(d);
							Hotspot stairs = room.getStairs();
							int index = stairs != null ? stairs.getDecorationIndex() : -1;
							BuildingUtils.buildRoom(player, r, plane, roomX, roomY, r.getExits(), true);
							r.getStairs().setDecorationIndex(index);
							end();
							return true;
						}
					}
					interpreter.sendPlainMessage(false, "The room you're trying to build doesn't fit.");
					stage = 4;
					return true;
				case 2:
					end();
					return true;
				}
				return true;
			case 4:
				end();
				return true;
			case 5:
				interpreter.sendOptions("Select an option", "Yes", "No");
				stage = 6;
				return true;
			case 6:
				switch (buttonId) {
				case 1: //yes
					r = Room.create(player, RoomProperties.THRONE_ROOM);
					dirs = BuildingUtils.getAvailableRotations(player, r.getExits(), plane, roomX, roomY);
					for (Direction d : dirs) {
						if (d == room.getRotation()) {
							r.setRotation(d);
							Hotspot stairs = room.getStairs();
							int index = stairs != null ? stairs.getDecorationIndex() : -1;
							BuildingUtils.buildRoom(player, r, plane, roomX, roomY, r.getExits(), true);
							r.getStairs().setDecorationIndex(index);
							end();
							return true;
						}
					}
					interpreter.sendPlainMessage(false, "The room you're trying to build doesn't fit.");
					stage = 4;
					return true;
				case 2:
					end();
					return true;
				}
				return true;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { DialogueInterpreter.getDialogueKey("con:nfroom") };
		}

	}
}
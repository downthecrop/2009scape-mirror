package core.game.node.entity.skill.construction;


import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.node.entity.skill.construction.Hotspot;
import rs09.game.system.SystemLogger;
import rs09.plugin.ClassScanner;

/**
 * The build option handling plugin.
 * @author Emperor
 *
 */
@Initializable
public final class BuildOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.setOptionHandler("build", this);
		SceneryDefinition.setOptionHandler("remove", this);
		ClassScanner.definePlugin(new RemoveDialogue());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!player.getHouseManager().isBuildingMode()) {
			player.getPacketDispatch().sendMessage("You have to be in building mode to do this.");
			return true;
		}
		Scenery object = ((Scenery) node);
		if (option.equals("remove")) {
			Decoration decoration = Decoration.getDecoration(player, object);
			if (decoration == null || !object.isActive()) {
				return false;
			}
			player.getDialogueInterpreter().open("con:removedec", object);
			return true;
		}
		player.setAttribute("con:hsobject", node);
		if (BuildingUtils.isDoorHotspot(object)) {
			int[] pos = BuildingUtils.roomExists(player, object);
			if (pos != null) {
				player.getDialogueInterpreter().open("con:remove", "room", pos);
				return true;
			}
			if (player.getHouseManager().getRoomAmount() < player.getHouseManager().getMaximumRooms(player)) {
				player.getInterfaceManager().open(new Component(402));
				
				
			} else {
				player.getPacketDispatch().sendMessage("You currently have the maximum amount of rooms available.");
			}
			return true;
		}
		Hotspot hotspot = player.getHouseManager().getHotspot(object);
		if (hotspot == null || !isBuildable(player, object, hotspot)) {
			System.out.println(hotspot == null);
			SystemLogger.logErr("Construction (building):  " + hotspot +  " : " + object + " chunkX = " + object.getLocation().getChunkX() + ", chunkY = " + object.getLocation().getChunkY());
			return true;
		}

		player.setAttribute("con:hotspot", hotspot);
		BuildingUtils.openBuildInterface(player, hotspot.getHotspot());
		return true;
	}
	
	/**
	 * Checks if the hotspot can be used.
	 * @param player The player.
	 * @param hotspot The hotspot.
	 * @return {@code True} if so.
	 */
	private static boolean isBuildable(Player player, Scenery object, Hotspot hotspot) {
		Room room = player.getHouseManager().getRoom(object.getLocation());
		if (room == null) {
			return false;
		}
		switch (hotspot.getHotspot()) {
		case STAIRWAYS:
		case STAIRS_DOWN:
		case STAIRWAYS_DUNGEON:
			if (room.isBuilt(BuildHotspot.HALL_RUG)) {
				player.getPacketDispatch().sendMessage("You can't build a staircase on a rug.");
				return false;
			}
			return true;
		case HALL_RUG:
		case HALL_RUG2:
		case HALL_RUG3:
			if (room.isBuilt(BuildHotspot.STAIRWAYS) || room.isBuilt(BuildHotspot.STAIRS_DOWN) || room.isBuilt(BuildHotspot.STAIRWAYS_DUNGEON)) {
				player.getPacketDispatch().sendMessage("You can't build a rug under a staircase.");
				return false;
			}
			return true;
		case QUEST_STAIRWAYS:
		case STAIRS_DOWN2:
			if (room.isBuilt(BuildHotspot.Q_HALL_RUG)) {
				player.getPacketDispatch().sendMessage("You can't build a staircase on a rug.");
				return false;
			}
			return true;
		case Q_HALL_RUG:
		case Q_HALL_RUG2:
		case Q_HALL_RUG3:
			if (room.isBuilt(BuildHotspot.QUEST_STAIRWAYS) || room.isBuilt(BuildHotspot.STAIRS_DOWN2)) {
				player.getPacketDispatch().sendMessage("You can't build a rug under a staircase.");
				return false;
			}
			return true;
		default:
			return true;
		}
	}
	
	/**
	 * Handles the removing a decoration dialogue.
	 * @author Emperor
	 *
	 */
	private static class RemoveDialogue extends DialoguePlugin {

		/**
		 * The object.
		 */
		private Scenery object;

		/**
		 * Constructs a new {@code RemoveDialogue} {@code Object}.
		 */
		public RemoveDialogue() {
			super();
		}
		
		/**
		 * Constructs a new {@code RemoveDialogue} {@code Object}.
		 * @param player The player.
		 */
		public RemoveDialogue(Player player) {
			super(player);
		}
		
		@Override
		public DialoguePlugin newInstance(Player player) {
			return new RemoveDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			interpreter.sendOptions("Really remove it?", "Yes", "No");
			object = (Scenery) args[0];
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (buttonId) {
			case 1:
				BuildingUtils.removeDecoration(player, object);
				break;
			}
			end();
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { DialogueInterpreter.getDialogueKey("con:removedec") };
		}
		
	}

}
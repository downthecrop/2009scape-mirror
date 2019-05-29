package plugin.skill.construction.decoration;


import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.content.skill.member.construction.BuildHotspot;
import org.crandor.game.content.skill.member.construction.Decoration;
import org.crandor.game.content.skill.member.construction.HousingStyle;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles Construction related doors.
 * @author Emperor
 *
 */
@InitializablePlugin
public final class ConstructionDoorPlugin extends OptionHandler {

	/**
	 * The replacement ids.
	 */
	private static int[][] REPLACEMENT = {
			{ 13100, 13102 },
			{ 13101, 13103 },
			{ 13094, 13095 },
			{ 13096, 13097 },
			{ 13006, 13008 },
			{ 13007, 13008 },
			{ 13109, 13110 },
			{ 13107, 13108 },
			{ 13016, 13018 },
			{ 13015, 13017 },
			{ 13119, 13121 },
			{ 13118, 13120 },
	};
	
	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (HousingStyle style : HousingStyle.values()) {
			ObjectDefinition.forId(style.getDoorId()).getConfigurations().put("option:open", this);
			ObjectDefinition.forId(style.getSecondDoorId()).getConfigurations().put("option:open", this);
		}
		for (Decoration deco : BuildHotspot.DUNGEON_DOOR_LEFT.getDecorations()) {
			ObjectDefinition.forId(deco.getObjectId()).getConfigurations().put("option:open", this);
			ObjectDefinition.forId(deco.getObjectId()).getConfigurations().put("option:pick-lock", this);
			ObjectDefinition.forId(deco.getObjectId()).getConfigurations().put("option:force", this);
		}
		for (Decoration deco : BuildHotspot.DUNGEON_DOOR_RIGHT.getDecorations()) {
			ObjectDefinition.forId(deco.getObjectId()).getConfigurations().put("option:open", this);
			ObjectDefinition.forId(deco.getObjectId()).getConfigurations().put("option:pick-lock", this);
			ObjectDefinition.forId(deco.getObjectId()).getConfigurations().put("option:force", this);
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		GameObject object = (GameObject) node;
		GameObject second = DoorActionHandler.getSecondDoor(object, player);
		switch (option) {
		case "pick-lock":
		case "force":
			return false; //TODO
		}
		DoorActionHandler.open(object, second, getReplaceId(object), getReplaceId(second), true, 500, false);
		return true;
	}

	/**
	 * Gets the replace id for the door.
	 * @param object The door.
	 * @return The replace object id.
	 */
	private int getReplaceId(GameObject object) {
		for (int[] data : REPLACEMENT) {
			if (object.getId() == data[0]) {
				return data[1];
			}
		}
		return object.getId() + 6;
	}

}
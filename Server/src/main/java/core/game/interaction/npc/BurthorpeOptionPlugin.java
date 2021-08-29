package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.ClimbActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the optione plugin to handle interactions with 'nodes' in
 * burthorpe.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BurthorpeOptionPlugin extends OptionHandler {

	/**
	 * Represents the thieving guide location.
	 */
	private static final Location GUIDE_LOCATION = new Location(3061, 4985, 1);

	/**
	 * Represents the bar location.
	 */
	private static final Location BAR_LOCATION = new Location(2906, 3537, 0);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(1063).getHandlers().put("option:talk-to", this);// soldier
		NPCDefinition.forId(1061).getHandlers().put("option:talk-to", this);// sergant
		NPCDefinition.forId(1066).getHandlers().put("option:talk-to", this);// seated
		// soldier
		NPCDefinition.forId(1067).getHandlers().put("option:talk-to", this);// seated
		// soldier
		NPCDefinition.forId(1068).getHandlers().put("option:talk-to", this);// seated
		// soldier
		NPCDefinition.forId(1064).getHandlers().put("option:talk-to", this);// other
		// soldiers
		NPCDefinition.forId(1062).getHandlers().put("option:talk-to", this);// other
		// sergant
		SceneryDefinition.forId(7257).getHandlers().put("option:enter", this);// thieving
		// guide
		// trapdoor.
		SceneryDefinition.forId(7258).getHandlers().put("option:enter", this);// thieving
		// guide
		// passegeway.
		NPCDefinition.forId(1073).getHandlers().put("option:talk-to", this);
		NPCDefinition.forId(1074).getHandlers().put("option:talk-to", this);
		NPCDefinition.forId(1076).getHandlers().put("option:talk-to", this);
		NPCDefinition.forId(1077).getHandlers().put("option:talk-to", this);
		SceneryDefinition.forId(4624).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(4627).getHandlers().put("option:climb-down", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = node instanceof NPC ? ((NPC) node).getId() : ((Scenery) node).getId();
		switch (option) {
		case "climb-down":
			switch (id) {
			case 4624:
				ClimbActionHandler.climb(player, Animation.create(828), Location.create(2205, 4934, 1));
				break;
			}
			break;
		case "climb-up":
			switch (id) {
			case 4627:
				ClimbActionHandler.climb(player, Animation.create(828), Location.create(2899, 3565, 0));
				break;
			}
			break;
		case "enter":
			switch (id) {
			case 7257:
				player.getProperties().setTeleportLocation(GUIDE_LOCATION);
				break;
			case 7258:
				player.getProperties().setTeleportLocation(BAR_LOCATION);
				break;
			}
			break;
		case "talk-to":
			switch (id) {
			case 1064:
			case 1063:// training soldier.
				player.getPacketDispatch().sendMessage("The soldier is busy training.");
				break;
			case 1062:
			case 1061:// sergeant
				player.getPacketDispatch().sendMessage("The Sergeant is busy training the soldiers.");
				break;
			case 1066:// eating soldier.
			case 1067:// eating soldier.
			case 1068:// eating soldier.
				player.getPacketDispatch().sendMessage("The soldier is busy eating.");
				break;
			case 1073:
			case 1074:
				player.getPacketDispatch().sendMessage("The archer won't talk whilst on duty.");
				break;
			case 1076:
			case 1077:
				player.getPacketDispatch().sendMessage("The soldier won't talk whilst on duty.");
				break;
			}
			break;
		}
		return true;
	}

}

package content.region.desert.alkharid.quest.princealirescue;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.scenery.Scenery;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import content.data.Quests;

/**
 * Represents the plugin used to handle prince ali rescue quest interaction nodes.
 * @author Vexia
 * 
 */
@Initializable
public class PrinceAliRescuePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(2881).getHandlers().put("option:open", this);// prison
		// door.
		SceneryDefinition.forId(4639).getHandlers().put("option:open", this);// door
		// to
		// jail
		NPCDefinition.forId(925).getHandlers().put("option:talk-to", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Quest quest = player.getQuestRepository().getQuest(Quests.PRINCE_ALI_RESCUE);
		final int id = node instanceof Scenery ? ((Scenery) node).getId() : node instanceof NPC ? ((NPC) node).getId() : 0;
		switch (id) {
		case 925:
			player.getDialogueInterpreter().open(925, node);
			break;
		case 2881:
			switch (quest.getStage(player)) {
			case 60:
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				break;
			case 50:
				if (player.getAttribute("keli-gone", 0) > GameWorld.getTicks()) {
					if (player.getInventory().contains(2418, 1)) {
						player.getPacketDispatch().sendMessage("You unlock the door.");
						DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
					} else {
						player.getPacketDispatch().sendMessage("The door is locked.");
					}
				} else {
					player.getPacketDispatch().sendMessage("You'd better get rid of Lady Keli before trying to go through there.");
				}
				break;
			default:
				player.getPacketDispatch().sendMessage("You'd better get rid of Lady Keli before trying to go through there.");
				break;
			}
			break;
		}
		return true;
	}

	static Location[] locs = new Location[] { new Location(3268, 3227, 0), Location.create(3268, 3228, 0), Location.create(3267, 3228, 0), Location.create(3267, 3227, 0) };

	@Override
	public Location getDestination(final Node node, Node n) {
		if (n instanceof NPC) {
			NPC npc = ((NPC) n);
			if (npc.getLocation().equals(new Location(3268, 3226, 0))) {
				return locs[0];
			} else if (npc.getLocation().equals(new Location(3268, 3229, 0))) {
				return locs[1];
			} else if (npc.getLocation().equals(new Location(3267, 3229, 0))) {
				return locs[2];
			} else if (npc.getLocation().equals(new Location(3267, 3226, 0))) {
				return locs[3];
			}
		}
		return null;
	}

}
package core.game.interaction.city;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The plugin used to handle bedabin interactions.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BedabinPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(2700).getHandlers().put("option:walk-through", this);
		SceneryDefinition.forId(2672).getHandlers().put("option:use", this);
		NPCDefinition.forId(834).getHandlers().put("option:talk-to", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = node.getId();
		switch (option) {
		case "talk-to":
			player.getDialogueInterpreter().open(node.getId(), node);
			break;
		case "walk-through":
			switch (id) {
			case 2700:
				if (player.getLocation().getY() >= 3046) {
					final Scenery door = RegionManager.getObject(new Location(3169, 3046, 0));
					SceneryBuilder.replace(door, door.transform(2701), 2);
					player.getWalkingQueue().reset();
					player.getWalkingQueue().addPath(3169, 3045);
					player.getPacketDispatch().sendMessage("You walk back out the tent.");
					break;
				}
				player.getDialogueInterpreter().open(834, RegionManager.getNpc(player, 834));
				break;
			}
			break;
		case "use":
			switch (id) {
			case 2672:
				player.getPacketDispatch().sendMessage("To forge items use the metal you wish to work with the anvil.");
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public Location getDestination(Node n, Node node) {
		if (node instanceof NPC) {
			if (node.getId() == 834) {
				return new Location(3169, 3045, 0);
			}
		}
		return null;
	}

}

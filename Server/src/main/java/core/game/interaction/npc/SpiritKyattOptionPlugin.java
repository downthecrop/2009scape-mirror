package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the spirit kyatt familiar and the summoning area.
 * @author Splinter
 */
@Initializable
public final class SpiritKyattOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(7365).getHandlers().put("option:interact", this);
		SceneryDefinition.forId(28741).getHandlers().put("option:open", this);
		SceneryDefinition.forId(28743).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(28743).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(14910).getHandlers().put("option:take", this);
		SceneryDefinition.forId(14912).getHandlers().put("option:take", this);
		return this;
	}

	private final Item BRONZE_AXE = new Item(1351, 1);
	private final Item BRONZE_PICKAXE = new Item(1265, 1);

	@Override
	public boolean handle(final Player player, Node node, String option) {
		switch(node.getId()){
		case 7365:
			player.getDialogueInterpreter().open(7365, node.asNpc());
			break;
		case 28741:
			player.animate(new Animation(827));
			player.teleport(new Location(2333, 10015));
			break;
		case 28743:
			player.animate(new Animation(828));
			player.teleport(new Location(2328, 3646));
			break;
		case 14912:
			if (!player.getInventory().add(BRONZE_AXE)) {
				player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
				return true;
			}
			SceneryBuilder.replace(((Scenery) node), ((Scenery) node).transform(14908), 500);
			break;
		case 14910:
			if (!player.getInventory().add(BRONZE_PICKAXE)) {
				player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
				return true;
			}
			SceneryBuilder.replace(((Scenery) node), ((Scenery) node).transform(14908), 500);
			break;
		}
		return true;
	}

}

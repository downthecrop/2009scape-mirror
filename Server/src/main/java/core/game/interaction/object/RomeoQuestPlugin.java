package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plguin used to handle cadava berries.
 * @author 'Vexia
 */
@Initializable
public class RomeoQuestPlugin extends OptionHandler {

	/**
	 * Represents the cadava berries.
	 */
	private final Item CADAVA_BERRIES = new Item(753);

	/**
	 * Represents the picking berries animation.
	 */
	private final Animation ANIMATION = new Animation(2282);

	/**
	 * Represents the counter.
	 */
	private int counter = 0;

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(23625).getHandlers().put("option:pick-from", this);
		SceneryDefinition.forId(23626).getHandlers().put("option:pick-from", this);
		SceneryDefinition.forId(23627).getHandlers().put("option:pick-from", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (((Scenery) node).getId() == 23627) {
			player.getPacketDispatch().sendMessage("There are no berries left on this bush.");
			player.getPacketDispatch().sendMessage("More berries will grow soon.");
			return true;
		}
		if (!player.getInventory().add(CADAVA_BERRIES)) {
			player.getPacketDispatch().sendMessage("Your inventory is too full to pick the berries from the bush.");
			return true;
		}
		player.animate(ANIMATION);
		if (counter == 2) {
			SceneryBuilder.replace(((Scenery) node), new Scenery(23627, node.getLocation()), 30);
			counter = 0;
			return true;
		}
		counter++;
		return true;
	}

}

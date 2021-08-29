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
 * Represents the plugin used to handle picking from a red berry bush.
 * @author 'Vexia
 */
@Initializable
public class RedberryBushPlugin extends OptionHandler {

	/**
	 * Represents the red berries item.
	 */
	private final Item RED_BERRIES = new Item(1951);

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
		SceneryDefinition.forId(23628).getHandlers().put("option:pick-from", this);
		SceneryDefinition.forId(23629).getHandlers().put("option:pick-from", this);
		SceneryDefinition.forId(23630).getHandlers().put("option:pick-from", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (((Scenery) node).getId() == 23630) {
			player.getPacketDispatch().sendMessage("There are no berries left on this bush.");
			player.getPacketDispatch().sendMessage("More berries will grow soon.");
			return true;
		}
		if (!player.getInventory().add(RED_BERRIES)) {
			player.getPacketDispatch().sendMessage("Your inventory is too full to pick the berries from the bush.");
			return true;
		}
		player.lock(4);
		player.animate(ANIMATION);
		if (counter == 2) {
			SceneryBuilder.replace(((Scenery) node), new Scenery(23630, node.getLocation()), 30);
			counter = 0;
			return true;
		}
		counter++;
		return true;
	}

}

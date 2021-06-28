package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the doogle leaf plugin for this object.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public class DoogleLeafPlugin extends OptionHandler {

	/**
	 * Represents the leaf item.
	 */
	private static final Item LEAF = new Item(1573, 1);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(31155).getHandlers().put("option:pick-leaf", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!player.getInventory().add(LEAF)) {
			player.getPacketDispatch().sendMessage("You don't have have enough space in your inventory.");
		} else {
			player.getPacketDispatch().sendMessage("You pick some doogle leaves.");
		}
		return true;
	}

}

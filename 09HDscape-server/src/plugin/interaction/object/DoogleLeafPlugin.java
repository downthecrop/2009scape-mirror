package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the doogle leaf plugin for this object.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public class DoogleLeafPlugin extends OptionHandler {

	/**
	 * Represents the leaf item.
	 */
	private static final Item LEAF = new Item(1573, 1);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(31155).getConfigurations().put("option:pick-leaf", this);
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

package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents an oyster plugin handler.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class OysterPlugin extends OptionHandler {

	/**
	 * Represents the oyster items.
	 */
	private static final Item[] OYSTERS = new Item[] { new Item(409), new Item(411), new Item(413) };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(407).getConfigurations().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getInventory().replace(RandomFunction.random(5) < 3 ? OYSTERS[RandomFunction.random(OYSTERS.length - 1)] : OYSTERS[RandomFunction.random(OYSTERS.length)], ((Item) node).getSlot());
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}

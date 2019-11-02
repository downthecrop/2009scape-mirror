package plugin.consumable.food;

import org.crandor.game.content.global.consumable.ConsumableProperties;
import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.Food;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents a purple sweet.
 * @author Vexia
 */
@InitializablePlugin
public class PurpleSweetsPlugin extends Food {

	/**
	 * The messages.
	 */
	private static final String[] MESSAGES = new String[] { "The sugary goodness heals some energy.", "The sugary goodness is yummy." };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		Consumables.add(this);
		return this;
	}

	/**
	 * Constructs a new {@Code PurpleSweetsPlugin} {@Code Object}
	 */
	public PurpleSweetsPlugin() {
		super(10476, new ConsumableProperties(3));
	}

	@Override
	public void consume(Item item, Player player) {
		super.consume(item, player);
		double drain = player.getSettings().getRunEnergy() * 0.10;
		player.getSettings().updateRunEnergy(-drain);
	}

	@Override
	public void message(final Player player, final Item item, final int initial, final String... messages) {
		player.sendMessage(MESSAGES[RandomFunction.random(MESSAGES.length)]);
	}

	@Override
	public boolean removed(Item item, Player player) {
		return player.getInventory().remove(new Item(item.getId(), 1));
	}
}

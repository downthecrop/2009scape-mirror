package plugin.consumable.food;

import org.crandor.game.content.global.consumable.ConsumableProperties;
import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.CookingProperties;
import org.crandor.game.content.global.consumable.Food;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents a pizza food.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class PizzaPlugin extends Food {

	/**
	 * Represents the burnt Pizza item.
	 */
	private static final Item BURNT_PIZZA = new Item(2305);

	/**
	 * Constructs a new {@code PizzaPlugin} {@code Object}.
	 */
	public PizzaPlugin() {
		/**
		 * empty.
		 */
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		PizzaPlugin[] PIZZAS = new PizzaPlugin[] { new PizzaPlugin(2289, 2287, 7, 2291, 35, 143), new PizzaPlugin(2291, 7), new PizzaPlugin(2293, 8, 2295), new PizzaPlugin(2295, 8), new PizzaPlugin(2297, 9, 2299), new PizzaPlugin(2299, 9), new PizzaPlugin(2301, 11, 2303), new PizzaPlugin(2303, 11) };
		for (PizzaPlugin pizza : PIZZAS) {
			Consumables.add(pizza);
		}
		return this;
	}

	/**
	 * Constructs a new {@code Pizza} {@code Object}.
	 * @param itemId the item id.
	 * @param raw the raw item id.
	 * @param healing the healing power.
	 * @param newItem the new item.
	 * @param level the level.
	 * @param experience the experience.
	 */
	public PizzaPlugin(final int itemId, final int raw, final int healing, final int newItem, final int level, final int experience) {
		super(new Item(itemId), new Item(raw), BURNT_PIZZA, new ConsumableProperties(healing, newItem), new CookingProperties(level, experience, (experience - 30) > 100 ? 96 : experience - 30));
	}

	/**
	 * Constructs a new {@code Pizza.java} {@code Object}.
	 * @param itemId the item id.
	 * @param health the health.
	 */
	public PizzaPlugin(final int itemId, final int health) {
		super(new Item(itemId), new ConsumableProperties(health), null);
	}

	/**
	 * Constructs a new {@code Pizza.java} {@code Object}.
	 * @param itemId the item id.
	 * @param health the health.
	 * @param newItem the new item id.
	 */
	public PizzaPlugin(final int itemId, final int health, final int newItem) {
		super(new Item(itemId), new ConsumableProperties(health, newItem), null);
	}

	@Override
	public String getEatMessage() {
		return "You eat " + (getItem().getName().toLowerCase().contains("1/2") ? "half the " + getItem().getName().toLowerCase().replace("1/2", "").trim() : "the " + getItem().getName().toLowerCase() + ".");
	}
}

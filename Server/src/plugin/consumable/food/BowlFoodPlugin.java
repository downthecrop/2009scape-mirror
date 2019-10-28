package plugin.consumable.food;

import org.crandor.game.content.global.consumable.ConsumableProperties;
import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.CookingProperties;
import org.crandor.game.content.global.consumable.Food;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle bowl related foods.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public class BowlFoodPlugin extends Food {

	/**
	 * Represents the empty bowl item.
	 */
	private static final Item BOWL = new Item(1923);

	/**
	 * Constructs a new {@code BowlFoodPlugin.java} {@code Object}.
	 */
	public BowlFoodPlugin() {
		/**
		 * empty.
		 */
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		Consumables.add(new BowlFoodPlugin(7086, 2));
		Consumables.add(new BowlFoodPlugin(4239, 4237, 4239, new ConsumableProperties(2), new CookingProperties(20, 52, 1)));
		Consumables.add(new BowlFoodPlugin(7068, 13));
		Consumables.add(new BowlFoodPlugin(7086, 2));
		Consumables.add(new BowlFoodPlugin(2003, 2001, 2005, 5, new CookingProperties(25, 117, 58)));
		Consumables.add(new BowlFoodPlugin(7078, 7076, 7090, 2, new CookingProperties(13, 50, 60)));
		Consumables.add(new BowlFoodPlugin(7072, 2));
		Consumables.add(new BowlFoodPlugin(4239, 4));
		Consumables.add(new BowlFoodPlugin(7062, 2));
		Consumables.add(new BowlFoodPlugin(7064, 4));
		Consumables.add(new BowlFoodPlugin(7084, 1871, 7092, new ConsumableProperties(8, 1923), new CookingProperties(43, 60, 70)));
		Consumables.add(new BowlFoodPlugin(1871, 2));
		Consumables.add(new BowlFoodPlugin(2011, 11));
		Consumables.add(new BowlFoodPlugin(7082, 7080, 7094, new ConsumableProperties(9, 1923), new CookingProperties(57, 120, 80)));
		return this;
	}

	/**
	 * Constructs a new {@code BowlFood} {@code Object}.
	 * @param item the item.
	 * @param raw the raw item.
	 * @param burnt the burnt item.
	 * @param foodProperties the food properties.
	 * @param cookingProperties the cooking properies.
	 */
	public BowlFoodPlugin(int item, int raw, int burnt, ConsumableProperties foodProperties, CookingProperties cookingProperties) {
		super(item, raw, burnt, foodProperties, cookingProperties);
	}

	/**
	 * Constructs a new {@code BowlFood} {@code Object}.
	 * @param item the item.
	 * @param raw the raw item.
	 * @param burnt the burnt item.
	 * @param health the health.
	 * @param cookingProperties the cooking properties.
	 */
	public BowlFoodPlugin(final int item, final int raw, int burnt, int health, CookingProperties cookingProperties) {
		super(item, raw, burnt, new ConsumableProperties(health, BOWL), cookingProperties);
	}

	/**
	 * Constructs a new {@code BowlFood.java} {@code Object}.
	 * @param item the item.
	 * @param health the health.
	 */
	public BowlFoodPlugin(final int item, final int health) {
		this(item, 0, 0, health, null);
	}
}

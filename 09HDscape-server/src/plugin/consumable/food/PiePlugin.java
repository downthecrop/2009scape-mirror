package plugin.consumable.food;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.global.consumable.ConsumableProperties;
import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.CookingProperties;
import org.crandor.game.content.global.consumable.Food;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.StringUtils;

/**
 * Represents a generic pie food.
 * @author 'Vexia
 * @date 23/12/2013
 */
@InitializablePlugin
public class PiePlugin extends Food {

	/**
	 * Represents the burnt pie item.
	 */
	private static final Item BURNT_PIE = new Item(2329);

	/**
	 * Constructs a new {@code PiePlugin.java} {@code Object}.
	 */
	public PiePlugin() {
		/**
		 * empty.
		 */
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		final PiePlugin[] PIES = new PiePlugin[] { new PiePlugin(2325, 2321, 5, 2333, 10, 78), new PiePlugin(2333, 5), new PiePlugin(2327, 2319, 5, 2331, 20, 110), new PiePlugin(2331, 5), new PiePlugin(7170, 7168, 0, 0, 29, 128), new PiePlugin(2323, 2317, 7, 2335, 30, 130), new PiePlugin(2335, 7), new GardenPie(7178, 7176, 0, 7180, 34, 138), new GardenPie(7180, 0), new FishPie(7188, 7186, 0, 7190, 47, 164), new FishPie(7190, 0), new AdmiralPie(7198, 7196, 7, 7200, 70, 210), new AdmiralPie(7200, 7), new WildPie(7208, 7206, 11, 7210, 85, 240), new WildPie(7210, 11), new SummerPie(7218, 7216, 11, 7220, 95, 260), new SummerPie(7220, 11) };
		for (PiePlugin pie : PIES) {
			Consumables.add(pie);
		}
		return this;
	}

	/**
	 * Constructs a new {@code Pie} {@code Object}.
	 * @param itemId the item id.
	 * @param raw the raw item id.
	 * @param healing the healing power.
	 * @param newItem the new item.
	 * @param level the level.
	 * @param experience the experience.
	 */
	public PiePlugin(final int itemId, final int raw, final int healing, final int newItem, final int level, final int experience) {
		super(new Item(itemId), new Item(raw), BURNT_PIE, new ConsumableProperties(healing, newItem), new CookingProperties(level, experience, (experience - 30) > 100 ? 96 : experience - 30, "You successfully bake a delicious " + ItemDefinition.forId(itemId).getName().toLowerCase() + ".", "You accidentally burn the pie."));
	}

	/**
	 * Constructs a new {@code Pie.java} {@code Object}.
	 * @param itemId the item id.
	 * @param health the health.
	 */
	public PiePlugin(final int itemId, final int health) {
		super(new Item(itemId), new PieProperty(health), null);
	}

	@Override
	public String getEatMessage() {
		return "You eat " + (getItem().getName().toLowerCase().contains("half") ? getItem().getName().toLowerCase() : "half " + (StringUtils.isPlusN(getItem().getName().toLowerCase()) ? "an " : "a ") + getItem().getName().toLowerCase()) + ".";
	}

	/**
	 * Represents the garden pie.
	 * @author 'Vexia
	 * @date 23/12/2013
	 */
	public class GardenPie extends PiePlugin {

		/**
		 * Constructs a new {@code GardenPie} {@code Object}.
		 * @param itemId the item id.
		 * @param raw the raw item id.
		 * @param healing the healing amount.
		 * @param newItem the new item.
		 * @param level the level.
		 * @param experience the experience.
		 */
		public GardenPie(int itemId, int raw, int healing, int newItem, int level, int experience) {
			super(itemId, raw, healing, newItem, level, experience);
		}

		/**
		 * Constructs a new {@code GardenPie} {@code Object}.
		 * @param heal the healing amount.
		 * @param itemId the new item id.
		 */
		public GardenPie(int heal, int itemId) {
			super(heal, itemId);
		}

		@Override
		public void consume(final Item item, final Player player) {
			super.consume(item, player);
			player.getSkills().updateLevel(Skills.FARMING, 3, player.getSkills().getStaticLevel(Skills.FARMING) + 3);
		}

	}

	/**
	 * Represents the fish pie.
	 * @author 'Vexia
	 * @date 23/12/2013
	 */
	public class FishPie extends PiePlugin {

		/**
		 * Constructs a new {@code FishPie} {@code Object}.
		 * @param itemId the item id.
		 * @param raw the raw item id.
		 * @param healing the healing amount.
		 * @param newItem the new item.
		 * @param level the level.
		 * @param experience the experience.
		 */
		public FishPie(int itemId, int raw, int healing, int newItem, int level, int experience) {
			super(itemId, raw, healing, newItem, level, experience);
		}

		/**
		 * Constructs a new {@code FishPie} {@code Object}.
		 * @param heal the healing amount.
		 * @param itemId the new item id.
		 */
		public FishPie(int heal, int itemId) {
			super(heal, itemId);
		}

		@Override
		public void consume(final Item item, final Player player) {
			super.consume(item, player);
			player.getSkills().updateLevel(Skills.FISHING, 3, player.getSkills().getStaticLevel(Skills.FISHING) + 3);
		}

	}

	/**
	 * Represents the admiral pie.
	 * @author 'Vexia
	 * @date 23/12/2013
	 */
	public class AdmiralPie extends PiePlugin {

		/**
		 * Constructs a new {@code AdmiralPie} {@code Object}.
		 * @param itemId the item id.
		 * @param raw the raw item id.
		 * @param healing the healing amount.
		 * @param newItem the new item.
		 * @param level the level.
		 * @param experience the experience.
		 */
		public AdmiralPie(int itemId, int raw, int healing, int newItem, int level, int experience) {
			super(itemId, raw, healing, newItem, level, experience);
		}

		/**
		 * Constructs a new {@code AdmiralPie} {@code Object}.
		 * @param heal the healing amount.
		 * @param itemId the new item id.
		 */
		public AdmiralPie(int heal, int itemId) {
			super(heal, itemId);
		}

		@Override
		public void consume(final Item item, final Player player) {
			super.consume(item, player);
			player.getSkills().updateLevel(Skills.FISHING, 5, player.getSkills().getStaticLevel(Skills.FISHING) + 5);

		}

	}

	/**
	 * Represents the wild pie.
	 * @author 'Vexia
	 * @date 23/12/2013
	 */
	public class WildPie extends PiePlugin {

		/**
		 * Constructs a new {@code Wild} {@code Object}.
		 * @param itemId the item id.
		 * @param raw the raw item id.
		 * @param healing the healing amount.
		 * @param newItem the new item.
		 * @param level the level.
		 * @param experience the experience.
		 */
		public WildPie(int itemId, int raw, int healing, int newItem, int level, int experience) {
			super(itemId, raw, healing, newItem, level, experience);
		}

		/**
		 * Constructs a new {@code WildPie} {@code Object}.
		 * @param heal the healing amount.
		 * @param itemId the new item id.
		 */
		public WildPie(int heal, int itemId) {
			super(heal, itemId);
		}

		@Override
		public void consume(final Item item, final Player player) {
			super.consume(item, player);
			player.getSkills().updateLevel(Skills.RANGE, 4, player.getSkills().getStaticLevel(Skills.RANGE) + 4);
			player.getSkills().updateLevel(Skills.SLAYER, 5, player.getSkills().getStaticLevel(Skills.SLAYER) + 5);
		}

	}

	/**
	 * Represents the summer pie.
	 * @author 'Vexia
	 * @date 23/12/2013
	 */
	public class SummerPie extends PiePlugin {

		/**
		 * Constructs a new {@code SummerPie} {@code Object}.
		 * @param itemId the item id.
		 * @param raw the raw item id.
		 * @param healing the healing amount.
		 * @param newItem the new item.
		 * @param level the level.
		 * @param experience the experience.
		 */
		public SummerPie(int itemId, int raw, int healing, int newItem, int level, int experience) {
			super(itemId, raw, healing, newItem, level, experience);
		}

		/**
		 * Constructs a new {@code SummerPie} {@code Object}.
		 * @param heal the healing amount.
		 * @param itemId the new item id.
		 */
		public SummerPie(int heal, int itemId) {
			super(heal, itemId);
		}

		@Override
		public void consume(final Item item, final Player player) {
			super.consume(item, player);
			player.getSettings().updateRunEnergy(-(player.getSettings().getRunEnergy() * 0.10));
			player.getSkills().updateLevel(Skills.AGILITY, 5, player.getSkills().getStaticLevel(Skills.AGILITY) + 5);
		}

	}

	/**
	 * Represents a pie property.
	 * @author 'Vexia
	 * @date 23/12/2013
	 */
	public static class PieProperty extends ConsumableProperties {

		/**
		 * Represents the pie dish item.
		 */
		private static final Item PIE_DISH = new Item(2313);

		/**
		 * Constructs a new {@code PieProperty} {@code Object}.
		 * @param healing the healing power.
		 */
		public PieProperty(int healing) {
			super(healing, PIE_DISH);
		}

		/**
		 * Constructs a new {@code PieProperty.java} {@code Object}.
		 * @param healing the healing power.
		 * @param newItem the new item.
		 */
		public PieProperty(int healing, int newItem) {
			super(healing, newItem);
		}
	}

}

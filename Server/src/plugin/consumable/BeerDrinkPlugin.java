package plugin.consumable;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.global.consumable.ConsumableProperties;
import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.Drink;
import org.crandor.game.content.skill.SkillBonus;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the generic beer drinking plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public class BeerDrinkPlugin extends Drink {

	/**
	 * Represents the skill bonuses for each beer.
	 */
	private SkillBonus[] bonuses;

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		Consumables.add(new BeerDrinkPlugin(1917, 1, new SkillBonus(Skills.STRENGTH, 0.04), new SkillBonus(Skills.ATTACK, -0.07)));
		Consumables.add(new BeerDrinkPlugin(1905, 2, new SkillBonus(Skills.STRENGTH, 0.02), new SkillBonus(Skills.ATTACK, -0.04)));
		Consumables.add(new BeerDrinkPlugin(5751, 4, new SkillBonus(Skills.WOODCUTTING, 0.02), new SkillBonus(Skills.STRENGTH, -0.03), new SkillBonus(Skills.ATTACK, -0.03)));
		Consumables.add(new BeerDrinkPlugin(5753, 2, new SkillBonus(Skills.WOODCUTTING, 0.02)) {
			@Override
			public void consume(final Item item, final Player player) {
				super.consume(item, player);
				int skills[] = new int[] { Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH };
				for (int i : skills) {
					double bonus = -(2 + (player.getSkills().getLevel(i) * 0.05)) / 100;
					addBonus(player, new SkillBonus(i, bonus));
				}
			}
		});
		Consumables.add(new BeerDrinkPlugin(4627, 2, new SkillBonus(Skills.THIEVING, 0.02), new SkillBonus(Skills.ATTACK, 0.02), new SkillBonus(Skills.DEFENCE, -0.06)));
		Consumables.add(new BeerDrinkPlugin(5755, 2, new SkillBonus(Skills.COOKING, 0.02), new SkillBonus(Skills.ATTACK, -0.02), new SkillBonus(Skills.STRENGTH, -0.02)));
		Consumables.add(new BeerDrinkPlugin(5763, 2, new SkillBonus(Skills.FARMING, 0.02), new SkillBonus(Skills.ATTACK, -0.02), new SkillBonus(Skills.STRENGTH, -0.02)));
		Consumables.add(new BeerDrinkPlugin(1911, 1, new SkillBonus(Skills.STRENGTH, 0.03), new SkillBonus(Skills.ATTACK, -0.04)));
		Consumables.add(new BeerDrinkPlugin(1913, 2, new SkillBonus(Skills.SMITHING, 0.02), new SkillBonus(Skills.MINING, 0.02)) {
			@Override
			public void consume(final Item item, final Player player) {
				super.consume(item, player);
				int skills[] = new int[] { Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH };
				for (int i : skills) {
					double bonus = -(2 + (player.getSkills().getLevel(i) * 0.05)) / 100;
					addBonus(player, new SkillBonus(i, bonus));
				}
			}
		});
		Consumables.add(new BeerDrinkPlugin(1909, 2, new SkillBonus(Skills.HERBLORE, 0.02)) {
			@Override
			public void consume(final Item item, final Player player) {
				super.consume(item, player);
				int skills[] = new int[] { Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH };
				for (int i : skills) {
					double bonus = -(2 + (player.getSkills().getLevel(i) * 0.05)) / 100;
					addBonus(player, new SkillBonus(i, bonus));
				}
			}
		});
		Consumables.add(new BeerDrinkPlugin(1915, 3, new SkillBonus(Skills.STRENGTH, 0.03), new SkillBonus(Skills.ATTACK, -0.06)));
		Consumables.add(new BeerDrinkPlugin(2955, 4));
		Consumables.add(new BeerDrinkPlugin(5759, 5, new SkillBonus(Skills.SLAYER, 0.02), new SkillBonus(Skills.ATTACK, -0.03), new SkillBonus(Skills.STRENGTH, -0.03)));
		Consumables.add(new BeerDrinkPlugin(1907, 1, new SkillBonus(Skills.ATTACK, -0.04), new SkillBonus(Skills.STRENGTH, -0.03), new SkillBonus(Skills.DEFENCE, -0.03)) {
			@Override
			public void consume(final Item item, final Player player) {
				super.consume(item, player);
				final SkillBonus bonus = new SkillBonus(Skills.MAGIC, player.getSkills().getStaticLevel(Skills.MAGIC) > 50 ? 0.04 : 0.03);
				addBonus(player, bonus);
			}
		});
		Consumables.add(new BeerDrinkPlugin(7508, 2));
		Consumables.add(new BeerDrinkPlugin(10848, 2, new SkillBonus(Skills.THIEVING, 0.02)));
		Consumables.add(new BeerDrinkPlugin(10849, 2, new SkillBonus(Skills.THIEVING, 0.04)));
		Consumables.add(new BeerDrinkPlugin(10850, 2, new SkillBonus(Skills.THIEVING, 0.03)));
		Consumables.add(new BeerDrinkPlugin(10851, 2));
		return this;
	}

	/**
	 * Constructs a new {@code BeerDrinkPlugin} {@code Object}.
	 */
	public BeerDrinkPlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BeerDrinkPlugin} {@code Object}.
	 * @param item the item.
	 * @param properties the properties.
	 */
	public BeerDrinkPlugin(int item, int health, final SkillBonus... bonuses) {
		super(item, new ConsumableProperties(health, BEER_GLASS), "You drink the " + ItemDefinition.forId(item).getName().toLowerCase() + ". You feel slightly reinvigorated...", "...and slightly dizzy too.");
		this.bonuses = bonuses;
	}

	@Override
	public void consume(final Item item, final Player player) {
		super.consume(item, player);
		if (bonuses != null) {
			for (SkillBonus b : bonuses) {
				addBonus(player, b);
			}
		}
	}

	@Override
	public Item getEmptyItem() {
		return BEER_GLASS;
	}
}

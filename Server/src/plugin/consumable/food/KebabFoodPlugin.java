package plugin.consumable.food;

import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.Food;
import org.crandor.game.content.skill.SkillBonus;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents a kebab food plugin. I'm sorry emperor :'( i dont want to do it
 * either.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class KebabFoodPlugin extends Food {

	/**
	 * The force chats to say.
	 */
	private static final String[] CHATS = new String[] { "Lovely!", "Scrummy!", "Delicious!", "Yum!" };

	/**
	 * Represents the messages to display when eating a kebab.
	 */
	private static final String[] MESSAGES = new String[] { "That kebab didn't seem to do a lot.", "It restores some life points.", "That was a good kebab. You feel a lot better.", "Wow, that was an amazing kebab! You feel invigorated.", "That tasted a bit dodgy. You feel a bit ill.", "Eating the kebab has damaged your Skills stats." };

	@Override
	public KebabFoodPlugin newInstance(final Object object) {
		Consumables.add(new KebabFoodPlugin());
		Consumables.add(new Food(1883, 19) {
			@Override
			public void consume(final Item item, final Player player) {
				player.sendChat(CHATS[RandomFunction.random(CHATS.length)]);
				super.consume(item, player);
			}
		});
		return this;
	}

	/**
	 * Constructs a new {@code KebabFoodPlugin} {@code Object}.
	 */
	public KebabFoodPlugin() {
		super(1971, 1);
	}

	@Override
	public void consume(final Item item, final Player player) {
		int index = RandomFunction.random(MESSAGES.length);
		String message = MESSAGES[index];
		int health = getHealth(player, index);
		super.consume(item, player, health, message);
	}

	/**
	 * Gets the health regain.
	 * @param player the player.
	 * @param index the index.
	 * @return the health.
	 */
	public int getHealth(Player player, int index) {
		switch (index) {
		case 1:
			return (int) (player.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.10);
		case 2:
			return RandomFunction.random(10, 20);
		case 3:
			addBonus(player, new SkillBonus(Skills.ATTACK, 0.02));
			addBonus(player, new SkillBonus(Skills.STRENGTH, 0.02));
			addBonus(player, new SkillBonus(Skills.DEFENCE, 0.02));
			return RandomFunction.random(20, 30);
		case 4:
			addBonus(player, new SkillBonus(Skills.ATTACK, -0.02));
			addBonus(player, new SkillBonus(Skills.STRENGTH, -0.02));
			addBonus(player, new SkillBonus(Skills.DEFENCE, -0.02));
			return 0;
		case 5:
			addBonus(player, new SkillBonus(Skills.ATTACK, -0.02));
			addBonus(player, new SkillBonus(Skills.STRENGTH, -0.02));
			addBonus(player, new SkillBonus(Skills.DEFENCE, -0.02));
			break;
		}
		return 0;
	}
}

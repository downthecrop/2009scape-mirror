package core.game.node.entity.skill.cooking.recipe.topping;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.cooking.recipe.Recipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents a generic topping recipe.
 * @author 'Vexia
 * @date 22/12/2013
 */
public abstract class ToppingRecipe extends Recipe {

	/**
	 * Represents the bowl item.
	 */
	protected static final Item BOWL = new Item(1923);

	@Override
	public void mix(final Player player, final NodeUsageEvent event) {
		if (player.getSkills().getLevel(Skills.COOKING) < getLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a Cooking level of at least " + getLevel() + " in order to do this.");
			return;
		}
		super.mix(player, event);
		player.getSkills().addExperience(Skills.COOKING, getExperience(), true);
	}

	@Override
	public Item getBase() {
		return BOWL;
	}

	@Override
	public String getMixMessage(NodeUsageEvent event) {
		return null;
	}

	@Override
	public boolean isSingular() {
		return true;
	}

	/**
	 * Method used to get the level required.
	 * @return the level.
	 */
	public abstract int getLevel();

	/**
	 * Method used to get the experience gained.
	 * @return the experience.
	 */
	public abstract double getExperience();

}

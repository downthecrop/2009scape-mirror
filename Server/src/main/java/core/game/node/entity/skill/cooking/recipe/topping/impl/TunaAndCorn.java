package core.game.node.entity.skill.cooking.recipe.topping.impl;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.cooking.recipe.Recipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the tuna and corn recipe. This recipe consists of adding cooked
 * sweetcorn to a bowl of chopped tuna.
 * @author 'Vexia
 * @date 22/12/2013
 */
public final class TunaAndCorn extends Recipe {

	/**
	 * Represents the chopped tuna item.
	 */
	private static final Item CHOPPED_TUNA = new Item(7086);

	/**
	 * Represents the cooked corn item.
	 */
	private static final Item COOKED_CORN = new Item(5988);

	/**
	 * Represents the tuna and corn item.
	 */
	private static final Item TUNA_AND_CORN = new Item(7068);

	@Override
	public void mix(final Player player, final NodeUsageEvent event) {
		if (player.getSkills().getLevel(Skills.COOKING) < 67) {
			player.getDialogueInterpreter().sendDialogue("You need a Cooking level of at least " + 57 + " in order to do this.");
			return;
		}
		super.mix(player, event);
		player.getSkills().addExperience(Skills.COOKING, 204, true);
	}

	@Override
	public Item getBase() {
		return CHOPPED_TUNA;
	}

	@Override
	public Item getProduct() {
		return TUNA_AND_CORN;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { COOKED_CORN };
	}

	@Override
	public Item[] getParts() {
		return new Item[] {};
	}

	@Override
	public String getMixMessage(NodeUsageEvent event) {
		return null;
	}

	@Override
	public boolean isSingular() {
		return true;
	}

}

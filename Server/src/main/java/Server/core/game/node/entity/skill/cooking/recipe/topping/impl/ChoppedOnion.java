package core.game.node.entity.skill.cooking.recipe.topping.impl;

import core.game.node.entity.skill.cooking.recipe.topping.ToppingRecipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the chopped onion recipe. This recipe consists of using an onion
 * on a bowl with a knife.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class ChoppedOnion extends ToppingRecipe {

	/**
	 * Represents the chopped onion product item.
	 */
	private static final Item CHOPPED_ONION = new Item(1871);

	/**
	 * Represents the knife used to cut the onion.
	 */
	private static final Item KNIFE = new Item(946);

	/**
	 * Represents the onion item.
	 */
	private static final Item ONION = new Item(1957);

	@Override
	public void mix(final Player player, final NodeUsageEvent event) {
		if (!player.getInventory().containsItem(KNIFE)) {
			player.getDialogueInterpreter().sendDialogue("You need a knife in order to slice up the onion.");
			return;
		}
		super.mix(player, event);
	}

	@Override
	public int getLevel() {
		return 1;
	}

	@Override
	public double getExperience() {
		return 0;
	}

	@Override
	public Item getProduct() {
		return CHOPPED_ONION;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { ONION };
	}

	@Override
	public Item[] getParts() {
		return new Item[] {};
	}

	@Override
	public boolean isSingular() {
		return true;
	}
}

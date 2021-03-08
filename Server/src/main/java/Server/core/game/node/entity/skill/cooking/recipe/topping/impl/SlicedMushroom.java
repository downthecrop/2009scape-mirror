package core.game.node.entity.skill.cooking.recipe.topping.impl;

import core.game.node.entity.skill.cooking.recipe.topping.ToppingRecipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the fried mushroom recipe. This recipe consists of using a
 * mushroom on a bowl.
 * @author 'Vexia
 * @date 22/12/2013
 */
public final class SlicedMushroom extends ToppingRecipe {

	/**
	 * Represents the sliced mushrooms item.
	 */
	private static final Item SLICED_MUSHROOMS = new Item(7080);

	/**
	 * Represents the mushroom item.
	 */
	private static final Item MUSHROOM = new Item(6004);

	/**
	 * Represents the knife item.
	 */
	private static final Item KNIFE = new Item(946);

	@Override
	public void mix(final Player player, final NodeUsageEvent event) {
		if (!player.getInventory().containsItem(KNIFE)) {
			player.getDialogueInterpreter().sendDialogue("You need a knife in order to slice up the mushrooms.");
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
		return SLICED_MUSHROOMS;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { MUSHROOM };
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

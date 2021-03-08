package core.game.node.entity.skill.cooking.recipe.topping.impl;

import core.game.node.entity.skill.cooking.recipe.topping.ToppingRecipe;
import core.game.node.item.Item;

/**
 * Represents the uncooked egg recipe. This recipe consists of adding an
 * uncooked egg into a bowl.
 * @author 'Vexia
 * @date 22/12/2013
 */
public final class UncookedEgg extends ToppingRecipe {

	/**
	 * Represents the egg item.
	 */
	private static final Item EGG = new Item(1944);

	/**
	 * Represents the uncooked egg product.
	 */
	private static final Item UNCOOKED_EGG = new Item(7076);

	@Override
	public int getLevel() {
		return 1;
	}

	@Override
	public double getExperience() {
		return 1;
	}

	@Override
	public Item getProduct() {
		return UNCOOKED_EGG;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { EGG };
	}

	@Override
	public boolean isSingular() {
		return true;
	}

	@Override
	public Item[] getParts() {
		return new Item[] {};
	}

}

package core.game.node.entity.skill.cooking.recipe.potato.impl;

import core.game.node.entity.skill.cooking.recipe.potato.PotatoRecipe;
import core.game.node.item.Item;

/**
 * Represents the cheese potato. This recipe consists of mixing cheese witha
 * baked potato.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class CheesePotato extends PotatoRecipe {

	/**
	 * Represents the cheese potato.
	 */
	private static final Item CHEESE_POTATO = new Item(6705);

	/**
	 * Represents the cheese item.
	 */
	private static final Item CHEESE = new Item(1985);

	@Override
	public Item getProduct() {
		return CHEESE_POTATO;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { CHEESE };
	}

	@Override
	public boolean isTopping() {
		return false;
	}

	@Override
	public int getLevel() {
		return 47;
	}

	@Override
	public double getExperience() {
		return 10;
	}

}

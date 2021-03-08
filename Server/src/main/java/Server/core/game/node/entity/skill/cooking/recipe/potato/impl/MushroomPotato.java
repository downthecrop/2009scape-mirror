package core.game.node.entity.skill.cooking.recipe.potato.impl;

import core.game.node.entity.skill.cooking.recipe.potato.PotatoRecipe;
import core.game.node.item.Item;

/**
 * Represents the mushrrom potato. This recipe consists of mixing a baked potato
 * with mushroom and onion toppings.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class MushroomPotato extends PotatoRecipe {

	/**
	 * Represents the egg potato.
	 */
	private static final Item MUSHROOM_POTATO = new Item(7058);

	/**
	 * Represents the topping item.
	 */
	private static final Item TOPPING = new Item(7066);

	@Override
	public Item getProduct() {
		return MUSHROOM_POTATO;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { TOPPING };
	}

	@Override
	public boolean isTopping() {
		return true;
	}

	@Override
	public int getLevel() {
		return 64;
	}

	@Override
	public double getExperience() {
		return 10;
	}

}

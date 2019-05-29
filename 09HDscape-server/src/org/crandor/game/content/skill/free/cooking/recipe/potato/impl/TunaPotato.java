package org.crandor.game.content.skill.free.cooking.recipe.potato.impl;

import org.crandor.game.content.skill.free.cooking.recipe.potato.PotatoRecipe;
import org.crandor.game.node.item.Item;

/**
 * Represents the tuna potato recipe. This recipe consists of mixing tuna and
 * corn toppings.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class TunaPotato extends PotatoRecipe {

	/**
	 * Represents the tuna potato.
	 */
	private static final Item TUNA_POTATO = new Item(7060);

	/**
	 * Represents the topping item.
	 */
	private static final Item TOPPING = new Item(7068);

	@Override
	public Item getProduct() {
		return TUNA_POTATO;
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
		return 68;
	}

	@Override
	public double getExperience() {
		return 10;
	}

}

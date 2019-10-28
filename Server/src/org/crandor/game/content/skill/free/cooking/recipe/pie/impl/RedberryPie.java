package org.crandor.game.content.skill.free.cooking.recipe.pie.impl;

import org.crandor.game.content.skill.free.cooking.recipe.pie.PieRecipe;
import org.crandor.game.node.item.Item;

/**
 * Represents a redberry pie recipe.
 * @author 'Vexia
 * @date 21/12/2013
 */
public class RedberryPie extends PieRecipe {

	/**
	 * Represents the uncooked redberry pie.
	 */
	private static final Item UNCOOKED_PIE = new Item(2321);

	/**
	 * Represents the redberries pie.
	 */
	private static final Item REDBERRIES = new Item(1951);

	@Override
	public Item getProduct() {
		return UNCOOKED_PIE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { REDBERRIES };
	}

}

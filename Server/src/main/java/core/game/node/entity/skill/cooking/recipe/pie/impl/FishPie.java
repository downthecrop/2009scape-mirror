package core.game.node.entity.skill.cooking.recipe.pie.impl;

import core.game.node.entity.skill.cooking.recipe.pie.PieRecipe;
import core.game.node.item.Item;

/**
 * Represents the garden pie recipe. This pie consists of mixing, tomato, onion,
 * and cabbage together.
 * @author 'Vexia
 * @date 21/12/2013
 */
public class FishPie extends PieRecipe {

	/**
	 * Represents the uncooked redberry pie.
	 */
	private static final Item UNCOOKED_PIE = new Item(7186);

	/**
	 * Represents the trout item ingredient.
	 */
	private static final Item TROUT = new Item(333);

	/**
	 * Represents the cod item ingredient.
	 */
	private static final Item COD = new Item(339);

	/**
	 * Represents the potato item.
	 */
	private static final Item POTATO = new Item(1942);

	/**
	 * Represents the part one pie item.
	 */
	private static final Item PART_ONE = new Item(7182);

	/**
	 * Represents the part two pie item.
	 */
	private static final Item PART_TWO = new Item(7184);

	@Override
	public Item getProduct() {
		return UNCOOKED_PIE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { TROUT, COD, POTATO };
	}

	@Override
	public Item[] getParts() {
		return new Item[] { PIE_SHELL, PART_ONE, PART_TWO, UNCOOKED_PIE };
	}

	@Override
	public boolean isSingular() {
		return false;
	}

}

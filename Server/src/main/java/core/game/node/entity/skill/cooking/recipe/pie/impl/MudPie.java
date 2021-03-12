package core.game.node.entity.skill.cooking.recipe.pie.impl;

import core.game.node.entity.skill.cooking.recipe.pie.PieRecipe;
import core.game.node.item.Item;

/**
 * Represents the mud pie recipe. A mud pie consists of mixing compost, water
 * and clay together.
 * @author 'Vexia
 * @date 21/12/2013
 */
public class MudPie extends PieRecipe {

	/**
	 * Represents the uncooked redberry pie.
	 */
	private static final Item UNCOOKED_PIE = new Item(7168);

	/**
	 * Represents the compost item.
	 */
	private static final Item COMPOST = new Item(6032);

	/**
	 * Represents the bucket of water item.
	 */
	private static final Item BUCKET_OF_WATER = new Item(1929);

	/**
	 * Represents the clay item.
	 */
	private static final Item CLAY = new Item(434);

	/**
	 * Represents the part one pie item.
	 */
	private static final Item PART_ONE = new Item(7164);

	/**
	 * Represents the part two pie item.
	 */
	private static final Item PART_TWO = new Item(7166);

	@Override
	public Item getProduct() {
		return UNCOOKED_PIE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { COMPOST, BUCKET_OF_WATER, CLAY };
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

package core.game.node.entity.skill.cooking.recipe.pie.impl;

import core.game.node.entity.skill.cooking.recipe.pie.PieRecipe;
import core.game.node.item.Item;

/**
 * Represents the garden pie recipe. This pie consists of mixing, tomato, onion,
 * and cabbage together.
 * @author 'Vexia
 * @date 21/12/2013
 */
public class GardenPie extends PieRecipe {

	/**
	 * Represents the uncooked redberry pie.
	 */
	private static final Item UNCOOKED_PIE = new Item(7176);

	/**
	 * Represents the tomato ingredient item.
	 */
	private static final Item TOMATO = new Item(1982);

	/**
	 * Represents the onion ingredient item.
	 */
	private static final Item ONION = new Item(1957);

	/**
	 * Represents the cabbage ingredient item.
	 */
	private static final Item CABBAGE = new Item(1965);

	/**
	 * Represents the part one pie item.
	 */
	private static final Item PART_ONE = new Item(7172);

	/**
	 * Represents the part two pie item.
	 */
	private static final Item PART_TWO = new Item(7174);

	@Override
	public Item getProduct() {
		return UNCOOKED_PIE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { TOMATO, ONION, CABBAGE };
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

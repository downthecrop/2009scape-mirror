package core.game.node.entity.skill.cooking.recipe.pie.impl;

import core.game.node.entity.skill.cooking.recipe.pie.PieRecipe;
import core.game.node.item.Item;

/**
 * Represents the summer pie recipe. This recipe consists of mixing stawberry,
 * watermelon, and an apple.
 * @author 'Vexia
 * @date 21/12/2013
 */
public class SummerPie extends PieRecipe {

	/**
	 * Represents the uncooked redberry pie.
	 */
	private static final Item UNCOOKED_PIE = new Item(7216);

	/**
	 * Represents the strawberry item.
	 */
	private static final Item STRAWBERRY = new Item(5504);

	/**
	 * Represents the watermelon item.
	 */
	private static final Item WATERMELON = new Item(5982);

	/**
	 * Represents the cooking apple item.
	 */
	private static final Item COOKING_APPLE = new Item(1955);

	/**
	 * Represents the part one pie item.
	 */
	private static final Item PART_ONE = new Item(7212);

	/**
	 * Represents the part two pie item.
	 */
	private static final Item PART_TWO = new Item(7214);

	@Override
	public Item getProduct() {
		return UNCOOKED_PIE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { STRAWBERRY, WATERMELON, COOKING_APPLE };
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

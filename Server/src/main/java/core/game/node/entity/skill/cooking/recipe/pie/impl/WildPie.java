package core.game.node.entity.skill.cooking.recipe.pie.impl;

import core.game.node.entity.skill.cooking.recipe.pie.PieRecipe;
import core.game.node.item.Item;

/**
 * Represents the wild pie recipe. This recipe consists of mixing raw beat meat,
 * raw chomp, and raw rabbit into a pie shell.
 * @author 'Vexia
 * @date 21/12/2013
 */
public class WildPie extends PieRecipe {

	/**
	 * Represents the uncooked redberry pie.
	 */
	private static final Item UNCOOKED_PIE = new Item(7206);

	/**
	 * Represents the raw bear meat item.
	 */
	private static final Item BEAR_MEAT = new Item(2136);

	/**
	 * Represents the raw chompy meat item.
	 */
	private static final Item CHOMPY_MEAT = new Item(2876);

	/**
	 * Represents the raw rabbit meat item.
	 */
	private static final Item RABBIT_MEAT = new Item(3226);

	/**
	 * Represents the part one pie item.
	 */
	private static final Item PART_ONE = new Item(7202);

	/**
	 * Represents the part two pie item.
	 */
	private static final Item PART_TWO = new Item(7204);

	@Override
	public Item getProduct() {
		return UNCOOKED_PIE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { BEAR_MEAT, CHOMPY_MEAT, RABBIT_MEAT };
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

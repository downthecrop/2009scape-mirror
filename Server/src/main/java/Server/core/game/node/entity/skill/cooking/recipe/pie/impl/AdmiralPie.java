package core.game.node.entity.skill.cooking.recipe.pie.impl;

import core.game.node.entity.skill.cooking.recipe.pie.PieRecipe;
import core.game.node.item.Item;

/**
 * Represents the admiral pie recipe. This recipe conists of pixing a salmon,
 * tuna and potato together.
 * @author 'Vexia
 * @date 21/12/2013
 */
public class AdmiralPie extends PieRecipe {

	/**
	 * Represents the uncooked redberry pie.
	 */
	private static final Item UNCOOKED_PIE = new Item(7196);

	/**
	 * Represents the salmon ingredient item.
	 */
	private static final Item SALMON = new Item(329);

	/**
	 * Represents the tuna ingredient item.
	 */
	private static final Item TUNA = new Item(361);

	/**
	 * Represents the potato item.
	 */
	private static final Item POTATO = new Item(1942);

	/**
	 * Represents the part one pie item.
	 */
	private static final Item PART_ONE = new Item(7192);

	/**
	 * Represents the part two pie item.
	 */
	private static final Item PART_TWO = new Item(7194);

	@Override
	public Item getProduct() {
		return UNCOOKED_PIE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { SALMON, TUNA, POTATO };
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

package core.game.node.entity.skill.cooking.recipe.pizza.impl;

import core.game.node.entity.skill.cooking.recipe.pizza.PizzaRecipe;
import core.game.node.item.Item;

/**
 * Represents the meat pizza recipe. This recipe consists of adding cooked meat
 * to a plain pizza.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class MeatPizza extends PizzaRecipe {

	/**
	 * Represents the meat pizza item.
	 */
	private static final Item MEAT_PIZZA = new Item(2293);

	/**
	 * Represents the cooked meat item.
	 */
	private static final Item COOKED_MEAT = new Item(2142);

	/**
	 * Represents the cooked chicken item.
	 */
	private static final Item COOKED_CHICKEN = new Item(2140);

	@Override
	public double getExperience() {
		return 26;
	}

	@Override
	public int getLevel() {
		return 45;
	}

	@Override
	public Item getProduct() {
		return MEAT_PIZZA;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { COOKED_MEAT, COOKED_CHICKEN };
	}

}

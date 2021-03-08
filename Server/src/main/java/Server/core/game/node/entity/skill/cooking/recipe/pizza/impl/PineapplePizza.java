package core.game.node.entity.skill.cooking.recipe.pizza.impl;

import core.game.node.entity.skill.cooking.recipe.pizza.PizzaRecipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.item.Item;

/**
 * Represents the pineapple pizza recipe. This recipe consists of mixing either
 * pineapple chunks or pineapple rings.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class PineapplePizza extends PizzaRecipe {

	/**
	 * Represents the pineapple pizza item.
	 */
	private static final Item PINEAPPLE_PIZZA = new Item(2301);

	/**
	 * Represents the pineapple ring item.
	 */
	private static final Item PINEAPPLE_RING = new Item(2118);

	/**
	 * Represents the pineapple chunk item.
	 */
	private static final Item PINEAPPLE_CHUNKS = new Item(2116);

	@Override
	public double getExperience() {
		return 52;
	}

	@Override
	public int getLevel() {
		return 65;
	}

	@Override
	public Item getProduct() {
		return PINEAPPLE_PIZZA;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { PINEAPPLE_CHUNKS, PINEAPPLE_RING };
	}

	@Override
	public String getMixMessage(NodeUsageEvent event) {
		return "You add the " + event.getBaseItem().getName().toLowerCase() + " to the pizza.";
	}

}

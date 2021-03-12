package core.game.node.entity.skill.cooking.recipe.pizza.impl;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.cooking.recipe.Recipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the plain pizza recipe. This recipe consists of mixing tomato, and
 * cheese to a pizza base.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class PlainPizza extends Recipe {

	/**
	 * Represents the pizza base.
	 */
	private static final Item PIZZA_BASE = new Item(2283);

	/**
	 * Represents the uncooked pizza.
	 */
	private static final Item UNCOOKED_PIZZA = new Item(2287);

	/**
	 * Represents the incomplete pizza.
	 */
	private static final Item INCOMPLETE_PIZZA = new Item(2285);

	/**
	 * Represents the tomato ingredient item.
	 */
	private static final Item TOMATO = new Item(1982);

	/**
	 * Represents the cheese item.
	 */
	private static final Item CHEESE = new Item(1985);

	@Override
	public void mix(final Player player, final NodeUsageEvent event) {
		if (player.getSkills().getLevel(Skills.COOKING) < 35) {
			player.getDialogueInterpreter().sendDialogue("You need a Cooking level of at least " + 35 + " in order to do this.");
			return;
		}
		super.mix(player, event);
	}

	@Override
	public Item getBase() {
		return PIZZA_BASE;
	}

	@Override
	public Item getProduct() {
		return UNCOOKED_PIZZA;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { TOMATO, CHEESE };
	}

	@Override
	public Item[] getParts() {
		return new Item[] { PIZZA_BASE, INCOMPLETE_PIZZA, UNCOOKED_PIZZA };
	}

	@Override
	public String getMixMessage(NodeUsageEvent event) {
		return "You add the " + event.getBaseItem().getName().toLowerCase() + " to the pizza.";
	}

	@Override
	public boolean isSingular() {
		return false;
	}

}

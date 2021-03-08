package core.game.node.entity.skill.cooking.recipe.pie;

import core.game.node.entity.skill.cooking.recipe.Recipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.item.Item;

/**
 * Represents the generic recipe for a pie.
 * @author 'Vexia
 * @date 21/12/2013
 */
public abstract class PieRecipe extends Recipe {

	/**
	 * Represents the pie shell item.
	 */
	protected static final Item PIE_SHELL = new Item(2315);

	@Override
	public Item[] getParts() {
		return new Item[] {};
	}

	@Override
	public Item getBase() {
		return PIE_SHELL;
	}

	@Override
	public String getMixMessage(final NodeUsageEvent event) {
		return "You fill the pie with " + (event.getBaseItem().getId() == 2315 ? event.getUsedItem().getName().toLowerCase() : event.getBaseItem().getName().toLowerCase()) + ".";
	}

	@Override
	public boolean isSingular() {
		return true;
	}
}

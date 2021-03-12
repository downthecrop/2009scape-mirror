package core.game.node.entity.skill.cooking.recipe.pie.impl;

import core.game.node.entity.skill.cooking.recipe.pie.PieRecipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the meat pie recipe.
 * @author 'Vexia
 * @date 21/12/2013
 */
public class MeatPie extends PieRecipe {

	/**
	 * Represents the uncooked redberry pie.
	 */
	private static final Item UNCOOKED_PIE = new Item(2319);

	/**
	 * Represents the cooked meat item.
	 */
	private static final Item COOKED_MEAT = new Item(2142);

	/**
	 * Represents the cooked chicken item.
	 */
	private static final Item COOKED_CHICKEN = new Item(2140);

	/**
	 * Represents the cooked rabbit.
	 */
	private static final Item COOKED_RABBIT = new Item(3228);

	@Override
	public void mix(final Player player, final NodeUsageEvent event) {
		if (player.getInventory().remove(event.getUsedItem()) && player.getInventory().remove(event.getBaseItem())) {
			player.getInventory().add(getProduct());
			player.getPacketDispatch().sendMessage(getMixMessage(event));
			return;
		}
	}

	@Override
	public Item getProduct() {
		return UNCOOKED_PIE;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { COOKED_MEAT, COOKED_CHICKEN, COOKED_RABBIT };
	}

	@Override
	public String getMixMessage(final NodeUsageEvent event) {
		return "You fill the pie with meat.";
	}
}

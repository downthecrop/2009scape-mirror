package core.game.node.entity.skill.cooking.recipe.stew;

import core.game.node.entity.skill.cooking.recipe.Recipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the stew recipe. This recipe consists of mixing meat and a raw
 * potato in a bowl of water.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class StewRecipe extends Recipe {

	/**
	 * Represents the uncooked stew item.
	 */
	private static final Item UNCOOKED_STEW = new Item(2001);

	/**
	 * Represents the bowl of water item.
	 */
	private static final Item BOWL_OF_WATER = new Item(1921);

	/**
	 * Represents the meat item.
	 */
	private static final Item MEAT = new Item(2142);

	/**
	 * Represents the potato item.
	 */
	private static final Item POTATO = new Item(1942);

	/**
	 * Represents the incomplete stew.
	 */
	private static final Item INCOMPLETE_STEW = new Item(1997);

	/**
	 * Represents the second incomplete stew.
	 */
	private static final Item INCOMPLETE_STEW2 = new Item(1999);

	@Override
	public void mix(Player player, NodeUsageEvent event) {
		Item first = event.getUsedItem();
		Item second = event.getBaseItem();
		if(first != null && second != null) {
			if (player.getInventory().remove(first) && player.getInventory().remove(second)) {
				if (first.getId() == BOWL_OF_WATER.getId() || second.getId() == BOWL_OF_WATER.getId()) {
					player.getInventory().add(first.getId() == POTATO.getId() ? INCOMPLETE_STEW : first.getId() == MEAT.getId() ? INCOMPLETE_STEW2 : second.getId() == POTATO.getId() ? INCOMPLETE_STEW : second.getId() == MEAT.getId() ? INCOMPLETE_STEW2 : null);
				} else {
					player.getInventory().add(UNCOOKED_STEW);
				}
				player.getPacketDispatch().sendMessage(getMixMessage(event));
			}
		}
	}

	@Override
	public Item getBase() {
		return BOWL_OF_WATER;
	}

	@Override
	public Item getProduct() {
		return UNCOOKED_STEW;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { MEAT, POTATO, MEAT, POTATO };
	}

	@Override
	public Item[] getParts() {
		return new Item[] { BOWL_OF_WATER, BOWL_OF_WATER, INCOMPLETE_STEW, INCOMPLETE_STEW2 };
	}

	@Override
	public String getMixMessage(NodeUsageEvent event) {
		return "You cut up the " + (event.getUsedItem().getName().toLowerCase().contains("incomplete") ? event.getBaseItem().getName().toLowerCase() : event.getUsedItem().getName().toLowerCase().replace("cooked", "").trim()) + " and put it into the stew.";
	}

	@Override
	public boolean isSingular() {
		return false;
	}

}

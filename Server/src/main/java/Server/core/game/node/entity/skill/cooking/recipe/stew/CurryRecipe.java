package core.game.node.entity.skill.cooking.recipe.stew;

import core.game.node.entity.skill.cooking.recipe.Recipe;
import core.game.interaction.NodeUsageEvent;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents the curry recipe. This recipe consists of mixing 3 curry leaves or
 * a spice into a stew.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class CurryRecipe extends Recipe {

	/**
	 * Represents the uncooked curry item.
	 */
	private static final Item UNCOOKED_CURRY = new Item(2009);

	/**
	 * Represents the uncooked stew item.
	 */
	private static final Item UNCOOKED_STEW = new Item(2001);

	/**
	 * Represents the spice item.
	 */
	private static final Item SPICE = new Item(2007);

	/**
	 * Represents the curry leaf.
	 */
	private static final Item CURRY_LEAF = new Item(5970);

	@Override
	public void mix(Player player, NodeUsageEvent event) {
		if (event.getBaseItem().getId() == CURRY_LEAF.getId() || event.getUsedItem().getId() == CURRY_LEAF.getId()) {
			Item stew = event.getBaseItem().getId() == UNCOOKED_STEW.getId() ? event.getBaseItem() : event.getUsedItem();
			if (stew.getCharge() == 1000) {
				stew.setCharge(1);
			}
			int charge = stew.getCharge();
			if (charge < 3) {
				player.getInventory().remove(CURRY_LEAF);
				stew.setCharge(charge + 1);
			} else {
				if (player.getInventory().remove(stew) && player.getInventory().remove(CURRY_LEAF)) {
					player.getInventory().add(UNCOOKED_CURRY);
				}
			}
			return;
		}
		if (player.getInventory().remove(event.getBaseItem()) && player.getInventory().remove(event.getUsedItem())) {
			player.getInventory().add(getProduct());
		}
	}

	@Override
	public Item getBase() {
		return UNCOOKED_STEW;
	}

	@Override
	public Item getProduct() {
		return UNCOOKED_CURRY;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { SPICE, CURRY_LEAF };
	}

	@Override
	public Item[] getParts() {
		return new Item[] { UNCOOKED_STEW };
	}

	@Override
	public String getMixMessage(NodeUsageEvent event) {
		return "You mix the spice with the stew.";
	}

	@Override
	public boolean isSingular() {
		return true;
	}

}

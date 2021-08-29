package core.game.interaction.item.withitem;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the creating of an imp jar.
 * @author Vexia
 */
@Initializable
public final class ImpJarCreatePlugin extends UseWithHandler {

	/**
	 * The flower ids.
	 */
	private static final int[] FLOWERS = new int[] { 2460, 2461, 2462, 2463, 2464, 2465, 2466, 2467, 2468, 2469, 2470, 2471, 2472, 2473, 2474, 2475, 2476, 2477 };

	/**
	 * Constructs a new {@code ImpJarCreatePlugin} {@code Object}.
	 */
	public ImpJarCreatePlugin() {
		super(6097, 11264, 1939, 4525, 4535, 4546, 4700, 11262, 10012);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(11266, ITEM_TYPE, this);
		for (int i : FLOWERS) {
			addHandler(i, ITEM_TYPE, this);
		}
		for (int i : getValidChildren(5908)) {
			addHandler(i, OBJECT_TYPE, this);
		}
		addHandler(5909, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		Player player = event.getPlayer();
		if (event.getUsedWith() instanceof Scenery) {
			if ((event.getUsedItem().getId() >= 4525 && event.getUsedItem().getId() <= 4700) || event.getUsedItem().getId() == 1939 || event.getUsedItem().getId() == 11262 || event.getUsedItem().getId() == 10012) {
				fillOilStill(player, event.getUsedItem());
			}
			return true;
		}
		if (((Item) event.getUsedWith()).getId() == 6097) {
			makeOil(player);
		} else if (event.getUsedItem().getId() == 11264) {
			makeRepellent(player, event.getUsedItem(), (Item) event.getUsedWith());
		}
		return true;
	}

	/**
	 * Fills the oil still.
	 * @param player the player.
	 */
	private void fillOilStill(Player player, Item used) {
		int configValue = player.getConfigManager().get(425);
		if (used.getId() == 11262 || used.getId() == 10012) {
			if (configValue == 0 && used.getId() == 11262) {
				player.getInventory().replace(new Item(229), used.getSlot());
				player.sendMessage("You refine some imp repellent.");
				player.getConfigManager().set(425, 64, true);
				return;
			} else if (configValue == 32) {
				player.sendMessage("There is already lamp oil in the still.");
				return;
			} else if (configValue == 64) {
				if (used.getId() == 11262) {
					player.sendMessage("There is already imp repellent in the still.");
				} else {
					player.getInventory().replace(new Item(11260), used.getSlot());
					player.getConfigManager().set(425, 0, true);
					player.sendMessage("You turn the butterfly jar into an impling jar.");
				}
				return;
			}
			player.sendMessage("There is no refined imp repellent in the still.");
			return;
		}
		if (configValue == 64) {
			player.sendMessage("There is already imp repellent in the still.");
			return;
		}
		if (configValue == 32 && used.getId() == 1939) {
			player.sendMessage("There is already lamp oil in the still.");
			return;
		}
		if (used.getId() == 1939) {
			if (player.getInventory().remove(new Item(1939))) {
				player.getConfigManager().set(425, 32, true);
				player.sendMessage("You refine some swamp tar into lamp oil.");
			}
		} else {
			if (configValue == 0) {
				player.sendMessage("There is no oil in the still.");
			} else {
				if (player.getInventory().contains(4525, 1) || player.getInventory().contains(4535, 1) || player.getInventory().contains(4546, 1) || player.getInventory().contains(4700, 1)) {
					Item replace = new Item(used.getId() == 4525 ? 4522 : used.getId() == 4535 ? 4537 : used.getId() == 4546 ? 4548 : 4701);
					player.getInventory().replace(replace, used.getSlot());
					player.getConfigManager().set(425, 0, true);
					player.sendMessage("You fill the item with oil.");
				}
			}
		}
	}

	/**
	 * Makes imp repellent.
	 * @param player the player.
	 * @param usedWith the used item.
	 */
	private void makeRepellent(Player player, Item oil, Item usedWith) {
		if (isFlower(usedWith.getId())) {
			if (player.getInventory().remove(usedWith)) {
				player.getInventory().replace(new Item(11262), oil.getSlot());
				player.sendMessage("You mix the flower petals with the anchovy oil to make a very strange-smelling concoction.");
				;
			}
		}
	}

	/**
	 * Makes anchovy oil.
	 * @param player the player.
	 */
	private void makeOil(Player player) {
		if (!player.getInventory().contains(229, 1)) {
			player.sendMessage("You need an empty vial to put your anchovy oil into.");
			return;
		}
		if (!player.getInventory().contains(11266, 8)) {
			player.sendMessage("You need 8 anchovy paste's in order to make anchovy oil.");
			return;
		}
		if (player.getInventory().remove(new Item(11266, 8), new Item(229))) {
			player.getInventory().add(new Item(11264));
		}
	}

	/**
	 * Checks if the id is a flower.
	 * @param id the id.
	 * @return {@code True} if so.
	 */
	private boolean isFlower(int id) {
		for (int i : FLOWERS) {
			if (i == id) {
				return true;
			}
		}
		return false;
	}
}

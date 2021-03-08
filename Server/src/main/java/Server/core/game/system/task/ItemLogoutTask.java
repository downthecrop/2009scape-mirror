package core.game.system.task;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Logout task used for adding items when the player logs out.
 * @author Emperor
 */
public final class ItemLogoutTask extends LogoutTask {

	/**
	 * The items.
	 */
	private final Item[] items;

	/**
	 * Constructs a new {@code ItemLogoutTask} {@code Object}.
	 * @param ticks The amount of ticks.
	 * @param items The items to add on logout.
	 */
	public ItemLogoutTask(int ticks, Item... items) {
		super(ticks);
		this.items = items;
	}

	@Override
	public void run(Player player) {
		for (Item item : items) {
			player.getInventory().add(item);
		}
	}

}
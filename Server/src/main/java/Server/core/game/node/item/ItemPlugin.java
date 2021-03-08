package core.game.node.item;

import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Plugin;

/**
 * Represents a plugin for an item.
 * @author Vexia
 */
public abstract class ItemPlugin implements Plugin<Object> {

	/**
	 * The drop type identifier.
	 */
	protected static final int DROP = 1;

	/**
	 * Constructs a new {@Code ItemPlugin} {@Code Object}
	 */
	public ItemPlugin() {
		/**
		 * empty.
		 */
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return this;
	}

	/**
	 * Registers items to this plugin.
	 * @param ids the ids.
	 */
	public void register(int... ids) {
		for (int id : ids) {
			ItemDefinition.forId(id).setItemPlugin(this);
		}
	}

	/**
	 * Called when the item is removed from the player.
	 * @param player the player.
	 * @param item the item.
	 * @param type the type. (1=drop)
	 */
	public void remove(Player player, Item item, int type) {

	}

	/**
	 * Checks if the item can be picked up.
	 * @param player the player.
	 * @param item the item.
	 * @param type the pickup type (1=ground, 2=telegab)
	 * @return {@code True} if so.
	 */
	public boolean canPickUp(Player player, GroundItem item, int type) {
		return true;
	}

	/**
	 * Checks if the item can be made as a drop.
	 * @param item the item.
	 * @param player the player.
	 * @param npc TODO
	 * @param l the location.
	 * @return
	 */
	public boolean createDrop(Item item, Player player, NPC npc, Location l) {
		return true;
	}

	/**
	 * Changes an item if needed.
	 * @param item the item.
	 * @param npc the npc.
	 * @return the item.
	 */
	public Item getItem(Item item, NPC npc) {
		return item;
	}

	/**
	 * Gets the death item.
	 * @param item the item.
	 * @return the item.
	 */
	public Item getDeathItem(Item item) {
		return item;
	}
}

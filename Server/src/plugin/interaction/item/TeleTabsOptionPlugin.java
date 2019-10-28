package plugin.interaction.item;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle teletabs.
 * @author 'Vexia
 */
@InitializablePlugin
public class TeleTabsOptionPlugin extends OptionHandler {

	/**
	 * Represents the enum of tele tabs.
	 * @author 'Vexia
	 */
	public enum TeleTabs {
		VARROCK_TELEPORT(8007, Location.create(3212, 3423, 0), 35), LUMBRIDGE_TELEPORT(8008, Location.create(3222, 3218, 0), 41), FALADOR_TELEPORT(8009, Location.create(2966, 3380, 0), 47), CAMELOT_TELEPORT(8010, Location.create(2757, 3477, 0), 55.5), ADDOUGNE_TELEPORT(8011, Location.create(2662, 3307, 0), 61), WATCH_TOWER_TELEPORT(8012, Location.create(2548, 3114, 0), 68);

		/**
		 * Metho dused to get the teletab by the id.
		 * @param id the id.
		 * @return the teletab.
		 */
		public static TeleTabs forId(int id) {
			for (TeleTabs tab : TeleTabs.values()) {
				if (tab == null)
					continue;
				if (tab.getItem() == id)
					return tab;
			}
			return null;
		}

		/**
		 * The location.
		 */
		private Location location;

		/**
		 * The item id.
		 */
		private int item;

		/**
		 * The experience gained.
		 */
		private double exp;

		/**
		 * Constructs a new {@code TeleTabsOptionPlugin} {@code Object}.
		 * @param item the item.
		 * @param location the location.
		 * @param exp the exp.
		 */
		TeleTabs(int item, Location location, double exp) {
			setItem(item);
			setLocation(location);
			this.exp = exp;
		}

		/**
		 * @return the item
		 */
		public int getItem() {
			return item;
		}

		/**
		 * @return the location
		 */
		public Location getLocation() {
			return location;
		}

		/**
		 * @param item the item to set
		 */
		public void setItem(int item) {
			this.item = item;
		}

		/**
		 * @param location the location to set
		 */
		public void setLocation(Location location) {
			this.location = location;
		}

		/**
		 * @return the exp.
		 */
		public double getExp() {
			return exp;
		}

		/**
		 * @param exp the exp to set.
		 */
		public void setExp(double exp) {
			this.exp = exp;
		}
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final Item item = (Item) node;
		if (item == null) {
			return false;
		}
		final TeleTabs tab = TeleTabs.forId(item.getId());
		if (tab == null) {
			return false;
		}
		player.getInterfaceManager().close();
		player.lock(5);
		if (player.getInventory().contains(item.getId(), 1)) {
			if (player.getTeleporter().send(tab.getLocation(), TeleportType.TELETABS, 1)) {
				player.getInventory().remove(new Item(item.getId()), item.getSlot(), true);
			}
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("break", this);
		return this;
	}
}

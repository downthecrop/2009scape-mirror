package plugin.interaction.item.withitem;

import org.crandor.game.content.global.Dyes;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the dyeing of a cape.
 * @author Vexia
 */
@InitializablePlugin
public final class CapeDyeingPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code CapeDyeingPlugin} {@code Object}.
	 */
	public CapeDyeingPlugin() {
		super(1019);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (Cape c : Cape.values()) {
			addHandler(c.getDye().getItem().getId(), ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final Item item = event.getUsedItem();
		final Cape cape = Cape.forDye(item);
		if (cape == null) {
			return false;
		}
		if (player.getInventory().remove(cape.getDye().getItem(), (Item) event.getUsedWith())) {
			player.getInventory().replace(cape.getCape(), item.getSlot());
		}
		return true;
	}

	/**
	 * A cape to dye.
	 * @author Vexia
	 */
	public enum Cape {
		RED(Dyes.RED, new Item(1007)), BLUE(Dyes.BLUE, new Item(1021)), YELLOW(Dyes.YELLOW, new Item(1023)), GREEN(Dyes.GREEN, new Item(1027)), PURPLE(Dyes.PURPLE, new Item(1029)), ORANGE(Dyes.ORANGE, new Item(1031)), PINK(Dyes.PINK, new Item(6959));

		/**
		 * The dye for the cape.
		 */
		private final Dyes dye;

		/**
		 * The cape item.
		 */
		private final Item cape;

		/**
		 * Constructs a new {@code Cape} {@code Object}.
		 * @param dye the dye.
		 * @param cape the cape.
		 */
		private Cape(Dyes dye, Item cape) {
			this.dye = dye;
			this.cape = cape;
		}

		/**
		 * Gets the cape.
		 * @param dye the dye.
		 */
		public static Cape forDye(final Item dye) {
			for (Cape c : values()) {
				if (c.getDye().getItem().getId() == dye.getId()) {
					return c;
				}
			}
			return null;
		}

		/**
		 * Gets the dye.
		 * @return The dye.
		 */
		public Dyes getDye() {
			return dye;
		}

		/**
		 * Gets the cape.
		 * @return The cape.
		 */
		public Item getCape() {
			return cape;
		}

	}
}

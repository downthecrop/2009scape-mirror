package content.global.skill.cooking;

import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs09.consts.Items;

/**
 * Represents the plugin used to make skwered items.
 * @author 'Vexia
 * @date 22/12/2013
 */
@Initializable
public class SkeweredFoodPlugin extends UseWithHandler {

	/**
	 * Represents the level required.
	 */
	private final int LEVEL = 20;

	/**
	 * Constructs a new {@code SkeweredFoodPlugin} {@code Object}.
	 */
	public SkeweredFoodPlugin() {
		super(7225);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (SkeweredSet set : SkeweredSet.values()) {
			addHandler(set.getRaw().getId(), ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (player.getSkills().getLevel(Skills.FIREMAKING) < LEVEL) {
			player.getPacketDispatch().sendMessage("You meed a Firemaking level of at least " + LEVEL + " in order to do this.");
			return true;
		}
		final SkeweredSet set = SkeweredSet.forItem(event.getBaseItem().getId() == 7225 ? event.getUsedItem() : event.getBaseItem());
		if (player.getInventory().remove(event.getBaseItem()) && player.getInventory().remove(event.getUsedItem())) {
			player.getInventory().add(set.getProduct());
		}
		return true;
	}

	/**
	 * Represents a set of skwered items.
	 * @author 'Vexia
	 * @date 22/12/2013
	 */
	public enum SkeweredSet {
		CHOMPY(new Item(Items.RAW_CHOMPY_2876), new Item(Items.SKEWERED_CHOMPY_7230)),
		RABBIT(new Item(Items.RAW_RABBIT_3226), new Item(Items.SKEWERED_RABBIT_7224)),
		BIRD(new Item(Items.RAW_BIRD_MEAT_9978), new Item(Items.SKEWERED_BIRD_MEAT_9984)),
		BEAST(new Item(Items.RAW_BEAST_MEAT_9986), new Item(Items.SKEWERED_BEAST_9992));

		/**
		 * Represents the raw item.
		 */
		private final Item raw;

		/**
		 * Represents the product item.
		 */
		private final Item product;

		/**
		 * Constructs a new {@code SkeweredFoodPlugin} {@code Object}.
		 * @param raw the raw item.
		 * @param product the product.
		 */
		private SkeweredSet(Item raw, Item product) {
			this.raw = raw;
			this.product = product;
		}

		/**
		 * Gets the raw.
		 * @return The raw.
		 */
		public Item getRaw() {
			return raw;
		}

		/**
		 * Gets the product.
		 * @return The product.
		 */
		public Item getProduct() {
			return product;
		}

		/**
		 * Gets the skwered set.
		 * @param item the item.
		 * @return the set.
		 */
		public static SkeweredSet forItem(final Item item) {
			for (SkeweredSet set : values()) {
				if (set.getRaw().getId() == item.getId()) {
					return set;
				}
			}
			return null;
		}
	}
}

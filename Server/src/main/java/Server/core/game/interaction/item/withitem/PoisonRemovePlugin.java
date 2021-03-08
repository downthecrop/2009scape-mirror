package core.game.interaction.item.withitem;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to remove poision from a weapon.
 * @author 'Vexia
 * @date 29/11/2013
 */
@Initializable
public class PoisonRemovePlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code PoisonRemovePlugin} {@code Object}.
	 */
	public PoisonRemovePlugin() {
		super(883, 5616, 883, 885, 5617, 885, 887, 5618, 887, 889, 5619, 889, 891, 5620, 891, 893, 5621, 893, 871, 5655, 5662, 870, 5654, 5661, 872, 5656, 5663, 873, 5657, 5664, 875, 5659, 5666, 876, 5660, 5667, 874, 5658, 5665, 812, 5628, 5635, 813, 5629, 5636, 814, 5630, 5637, 3094, 5631, 5638, 815, 5632, 5639, 816, 5633, 5640, 817, 5634, 5641, 831, 5641, 5648, 832, 5642, 5649, 833, 5643, 5650, 834, 5644, 5651, 835, 5645, 5652, 836, 5646, 5653, 1219, 5668, 5686, 1221, 5670, 5688, 1223, 5672, 5690, 1225, 5674, 5692, 1227, 5676, 5694, 1229, 5678, 5696, 1231, 5680, 5698, 1233, 5682, 5700, 1251, 5704, 5618, 1253, 5706, 5620, 1255, 5708, 5622, 1257, 5710, 5624, 1259, 5712, 5626, 1261, 5714, 5628, 1263, 5716, 5630, 4582, 5734, 5636);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(3188, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final PoisonedWeapon weapon = PoisonedWeapon.forItem(event.getBaseItem().getName().contains("Cleaning") ? event.getUsedItem() : event.getBaseItem());
		if (weapon == null) {
			return true;
		}
		if (player.getInventory().remove(event.getBaseItem().getName().contains("Cleaning") ? event.getUsedItem() : event.getBaseItem())) {
			player.getInventory().add(new Item(weapon.getItem(), 1));
		}
		return true;
	}

	/**
	 * Represents the poisioning of a weapon.
	 * @author 'Vexia
	 */
	public enum PoisonedWeapon {

		/**
		 * Represents a poisioned weapon.
		 */
		BRONZE_ARROW(882, 883, 5616, 883),
		/**
		 * Represents a poisioned weapon.
		 */
		IRON_ARROW(884, 885, 5617, 885),
		/**
		 * Represents a poisioned weapon.
		 */
		STEEL_ARROW(886, 887, 5618, 887),
		/**
		 * Represents a poisioned weapon.
		 */
		MITHRIL_ARROW(888, 889, 5619, 889),
		/**
		 * Represents a poisioned weapon.
		 */
		ADAMANT_ARROW(890, 891, 5620, 891),
		/**
		 * Represents a poisioned weapon.
		 */
		RUNE_ARROW(892, 893, 5621, 893),
		/**
		 * Represents a poisioned weapon.
		 */
		IRON_KNIFE(863, 871, 5655, 5662),
		/**
		 * Represents a poisioned weapon.
		 */
		RONZE_KNIFE(864, 870, 5654, 5661),
		/**
		 * Represents a poisioned weapon.
		 */
		STEEL_KNIFE(865, 872, 5656, 5663),
		/**
		 * Represents a poisioned weapon.
		 */
		MITHRIL_KNIFE(866, 873, 5657, 5664),
		/**
		 * Represents a poisioned weapon.
		 */
		ADAMANT_KNIFE(867, 875, 5659, 5666),
		/**
		 * Represents a poisioned weapon.
		 */
		RUNE_KNIFE(868, 876, 5660, 5667),
		/**
		 * Represents a poisioned weapon.
		 */
		BLACK_KNIFE(869, 874, 5658, 5665),
		/**
		 * Represents a poisioned weapon.
		 */
		BRONZE_DART(806, 812, 5628, 5635),
		/**
		 * Represents a poisioned weapon.
		 */
		IRON_DART(807, 813, 5629, 5636),
		/**
		 * Represents a poisioned weapon.
		 */
		STEEL_DART(808, 814, 5630, 5637),
		/**
		 * Represents a poisioned weapon.
		 */
		BLACK_DART(3093, 3094, 5631, 5638),
		/**
		 * Represents a poisioned weapon.
		 */
		MITHRIL_DART(809, 815, 5632, 5639),
		/**
		 * Represents a poisioned weapon.
		 */
		ADAMANT_DART(810, 816, 5633, 5640),
		/**
		 * Represents a poisioned weapon.
		 */
		RUNE_DART(811, 817, 5634, 5641),
		/**
		 * Represents a poisioned weapon.
		 */
		IRON_JAVELIN(825, 831, 5641, 5648),
		/**
		 * Represents a poisioned weapon.
		 */
		BRONZE_JAVELIN(826, 832, 5642, 5649),
		/**
		 * Represents a poisioned weapon.
		 */
		STEEL_JAVELIN(827, 833, 5643, 5650),
		/**
		 * Represents a poisioned weapon.
		 */
		MITHRIL_JAVELIN(828, 834, 5644, 5651),
		/**
		 * Represents a poisioned weapon.
		 */
		ADAMANT_JAVELIN(829, 835, 5645, 5652),
		/**
		 * Represents a poisioned weapon.
		 */
		RUNE_JAVELIN(830, 836, 5646, 5653),
		/**
		 * Represents a poisioned weapon.
		 */
		IRON_DAGGER(1203, 1219, 5668, 5686),
		/**
		 * Represents a poisioned weapon.
		 */
		BRONZE_DAGGER(1205, 1221, 5670, 5688),
		/**
		 * Represents a poisioned weapon.
		 */
		STEEL_DAGGER(1207, 1223, 5672, 5690),
		/**
		 * Represents a poisioned weapon.
		 */
		MITHRIL_DAGGER(1209, 1225, 5674, 5692),
		/**
		 * Represents a poisioned weapon.
		 */
		ADAMANT_DAGGER(1211, 1227, 5676, 5694),
		/**
		 * Represents a poisioned weapon.
		 */
		RUNE_DAGGER(1213, 1229, 5678, 5696),
		/**
		 * Represents a poisioned weapon.
		 */
		DRAGON_DAGGER(1215, 1231, 5680, 5698),
		/**
		 * Represents a poisioned weapon.
		 */
		BLACK_DAGGER(1217, 1233, 5682, 5700),
		/**
		 * Represents a poisioned weapon.
		 */
		BRONZE_SPEAR(1237, 1251, 5704, 5618),
		/**
		 * Represents a poisioned weapon.
		 */
		IRON_SPEAR(1239, 1253, 5706, 5620),
		/**
		 * Represents a poisioned weapon.
		 */
		STEEL_SPEAR(1241, 1255, 5708, 5622),
		/**
		 * Represents a poisioned weapon.
		 */
		MITHRIL_SPEAR(1243, 1257, 5710, 5624),
		/**
		 * Represents a poisioned weapon.
		 */
		ADAMANT_SPEAR(1245, 1259, 5712, 5626),
		/**
		 * Represents a poisioned weapon.
		 */
		RUNE_SPEAR(1247, 1261, 5714, 5628),
		/**
		 * Represents a poisioned weapon.
		 */
		DRAGON_SPEAR(1249, 1263, 5716, 5630),
		/**
		 * Represents a poisioned weapon.
		 */
		BLACK_SPEAR(4580, 4582, 5734, 5636);

		/**
		 * @param id the id.
		 * @return the value.
		 */
		public static PoisonedWeapon forId(int id) {
			for (PoisonedWeapon weapon : PoisonedWeapon.values()) {
				if (weapon.getItem() == id) {
					return weapon;
				}
			}
			return null;
		}

		/**
		 * The item.
		 */
		private int item;

		/**
		 * The first stage.
		 */
		private int first;

		/**
		 * The second stage.
		 */
		private int second;

		/**
		 * The third stage.
		 */
		private int third;

		/**
		 * Constructs a new </code>PoisionWeapon</code>.
		 */
		PoisonedWeapon(int item, int first, int second, int third) {
			this.item = item;
			this.first = first;
			this.second = second;
			this.third = third;
		}

		/**
		 * @return the first.
		 */
		public int getFirst() {
			return first;
		}

		/**
		 * @return the item.
		 */
		public int getItem() {
			return item;
		}

		/**
		 * @return the second.
		 */
		public int getSecond() {
			return second;
		}

		/**
		 * @return the third.
		 */
		public int getThird() {
			return third;
		}

		/**
		 * @param first the first to set.
		 */
		public void setFirst(int first) {
			this.first = first;
		}

		/**
		 * @param item the item to set.
		 */
		public void setItem(int item) {
			this.item = item;
		}

		/**
		 * @param second the second to set.
		 */
		public void setSecond(int second) {
			this.second = second;
		}

		/**
		 * @param third the third to set.
		 */
		public void setThird(int third) {
			this.third = third;
		}

		/**
		 * Method used to get the poisioned weapon for the id.
		 * @param i the id.
		 * @return the weapon.
		 */
		public static PoisonedWeapon forItem(final Item i) {
			for (PoisonedWeapon weapon : values()) {
				if (weapon.getFirst() == i.getId() || weapon.getSecond() == i.getId() || weapon.getThird() == i.getId()) {
					return weapon;
				}
			}
			return null;
		}
	}
}

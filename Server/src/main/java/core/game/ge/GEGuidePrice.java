package core.game.ge;

import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.tools.StringUtils;
import rs09.game.ge.GrandExchange;

/**
 * Represents the glass used to open the guide prices for the different type of
 * trade(herbs, logs, runes, etc...)
 * @author 'Vexia
 * @date 30/11/2013
 */
public final class GEGuidePrice {

	/**
	 * Represents the guide price component.
	 */
	private static final Component COMPONENT = new Component(642);

	/**
	 * Method used to open a Grand Exchange guide price type.
	 * @param player the player.
	 * @param type the type.
	 */
	public static final void open(final Player player, final GuideType type) {
		player.getInterfaceManager().open(COMPONENT);
		type.display(player);
	}

	/**
	 * Method used to clear the current items being showed.
	 * @param player the player.
	 */
	public static final void clear(final Player player) {
		for (int i = 135; i < 165; i++) {
			player.getPacketDispatch().sendInterfaceConfig(COMPONENT.getId(), i, true);
		}
	}

	/**
	 * Represents a guide item shown on the Guide Price interface.
	 * @author 'Vexia
	 * @date 01/12/2013
	 */
	public static class GuideItem {

		/**
		 * Represents the item of this type.
		 */
		private final int item;

		/**
		 * Represents the unlock child.
		 */
		private final int[] childData;

		/**
		 * Constructs a new {@code GEGuidePrice} {@code Object}.
		 * @param item the item.
		 * @param childData the child data.
		 */
		public GuideItem(final int item, final int... childData) {
			this.item = item;
			this.childData = childData;
		}

		/**
		 * Gets the item.
		 * @return The item.
		 */
		public int getItem() {
			return item;
		}

		/**
		 * Gets the childData.
		 * @return The childData.
		 */
		public int[] getChildData() {
			return childData;
		}
	}

	/**
	 * Represents a guide price type of trade(herbs, logs, etc...)
	 * @author 'Vexia
	 * @date 30/11/2013
	 */
	public enum GuideType {
		LOGS(new int[] { 0, 0 }, new GuideItem(1511, 155), new GuideItem(2862, 156), new GuideItem(1521, 157), new GuideItem(1519, 158), new GuideItem(6333, 159), new GuideItem(1517, 160), new GuideItem(6332, 161), new GuideItem(12581, 162), new GuideItem(1515, 163), new GuideItem(1513, 164)), ORES(new int[] { 40, 44 }, new GuideItem(436, 33), new GuideItem(438, 34), new GuideItem(440, 35), new GuideItem(442, 36), new GuideItem(453, 37), new GuideItem(444, 38), new GuideItem(447, 39), new GuideItem(449, 40), new GuideItem(451, 41)), RUNES(new int[] { 215, 216 }, new GuideItem(1436, 183), new GuideItem(7936, 184), new GuideItem(556, 185), new GuideItem(558, 186), new GuideItem(555, 187), new GuideItem(557, 188), new GuideItem(554, 189), new GuideItem(559, 190), new GuideItem(564, 191), new GuideItem(562, 192), new GuideItem(9075, 193), new GuideItem(561, 194), new GuideItem(563, 195), new GuideItem(560, 196), new GuideItem(565, 197), new GuideItem(566, 198)), HERBS(new int[] { 130, 135 }, new GuideItem(249, 119), new GuideItem(251, 120), new GuideItem(253, 121), new GuideItem(255, 122), new GuideItem(257, 123), new GuideItem(2998, 124), new GuideItem(259, 125), new GuideItem(12172, 126), new GuideItem(261, 127), new GuideItem(263, 128), new GuideItem(3000, 129), new GuideItem(265, 130), new GuideItem(2481, 131), new GuideItem(267, 132), new GuideItem(269, 133)), WEAPONS_AND_ARMOUR(new int[] { 88, 89 }, new GuideItem(11834, 73), new GuideItem(11838, 74), new GuideItem(11842, 75), new GuideItem(11864, 76), new GuideItem(11870, 77), new GuideItem(11846, 78), new GuideItem(11848, 79), new GuideItem(11850, 80), new GuideItem(11856, 81), new GuideItem(11732, 82), new GuideItem(4151, 83), new GuideItem(11235, 84), new GuideItem(6739, 85), new GuideItem(4587, 86), new GuideItem(4153, 87));

		/**
		 * Constructs a new {@code GEGuidePrice} {@code Object}.
		 * @param childData the childData.
		 * @param items the guide items.
		 */
		GuideType(final int[] childData, final GuideItem... items) {
			this.childData = childData;
			this.items = items;
		}

		/**
		 * Represents the guide items.
		 */
		private final GuideItem[] items;

		/**
		 * Represents the child data for the guide type.
		 */
		private final int childData[];

		/**
		 * Gets the items.
		 * @return The items.
		 */
		public GuideItem[] getItems() {
			return items;
		}

		/**
		 * Gets the childData.
		 * @return The childData.
		 */
		public int[] getChildData() {
			return childData;
		}

		/**
		 * Method used to display the guide type.
		 * @param player the player.
		 */
		public void display(final Player player) {
			player.setAttribute("guide-price", this);
			if (this != LOGS) {
				clear(player);
			}
			player.getPacketDispatch().sendString("Guide Prices: " + StringUtils.formatDisplayName(name()), COMPONENT.getId(), 14);
			for (int i = getChildData()[0]; i < getChildData()[1]; i++) {
				player.getPacketDispatch().sendInterfaceConfig(642, i, false);
			}
			for (GuideItem item : getItems()) {
				player.getPacketDispatch().sendString("" + GrandExchange.getRecommendedPrice(item.item, false) + " gp", COMPONENT.getId(), item.getChildData()[0]);
			}
		}
	}

}

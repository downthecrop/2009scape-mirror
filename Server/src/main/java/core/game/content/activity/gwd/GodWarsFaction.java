package core.game.content.activity.gwd;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * The god wars factions.
 * @author Emperor
 */
public enum GodWarsFaction {
	ARMADYL(6222, 6246, 87, 11694, 11718, 11720, 11722, 12670, 12671, 14671),
	BANDOS(6260, 6283, 11061, 11696, 11724, 11726, 11728), 
	SARADOMIN(6247, 6259, 1718, 2412, 2415, 2661, 2663, 2665, 2667, 3479, 3675, 3489, 3840, 4682, 6762, 8055, 10384, 10386, 10388, 10390, 10440, 10446, 10452, 10458, 10464, 10470, 11181, 11698, 11730,542,544),
	ZAMORAK(6203, 6221, 11716, 11700, 1724, 2414, 2417, 2653, 2655, 2657, 2659, 3478, 3674, 3841, 3842, 3852, 4683, 6764, 8056, 10368, 10370, 10372, 10374, 10444, 10450, 10456, 10460, 10468, 10474, 10776, 10786, 10790, 14662);

	/**
	 * The start NPC id.
	 */
	private final int startId;

	/**
	 * The end NPC id.
	 */
	private final int endId;

	/**
	 * The protection items.
	 */
	private final int[] protectionItems;

	/**
	 * Constructs a new {@code GodWarsFaction} {@code Object}.
	 * @param startId The start NPC id.
	 * @param endId The end NPC id.
	 * @param protectionItems The protection items for this faction.
	 */
	private GodWarsFaction(int startId, int endId, int... protectionItems) {
		this.startId = startId;
		this.endId = endId;
		this.protectionItems = protectionItems;
	}

	/**
	 * Gets the god wars faction for the given NPC id.
	 * @param npcId The NPC id.
	 * @return The faction for this NPC.
	 */
	public static GodWarsFaction forId(int npcId) {
		for (GodWarsFaction faction : values()) {
			if (npcId >= faction.getStartId() && npcId <= faction.getEndId()) {
				return faction;
			}
		}
		return null;
	}

	/**
	 * Checks if the player is protected from this faction.
	 * @param player The player.
	 * @return {@code True} if no NPCs of this faction should attack the
	 * player.
	 */
	public boolean isProtected(Player player) {
		for (Item item : player.getEquipment().toArray()) {
			if (item != null) {
				for (int id : protectionItems) {
					if (item.getId() == id) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets the startId.
	 * @return The startId.
	 */
	public int getStartId() {
		return startId;
	}

	/**
	 * Gets the endId.
	 * @return The endId.
	 */
	public int getEndId() {
		return endId;
	}

	/**
	 * Gets the protectionItems.
	 * @return The protectionItems.
	 */
	public int[] getProtectionItems() {
		return protectionItems;
	}
}
package core.game.node.entity.skill.construction;


import core.game.node.entity.player.Player;
import core.tools.RandomFunction;

/**
 * Holds information of all nail types.
 * @author Emperor
 *
 */
public enum NailType {

	BRONZE(4819, 5),
	IRON(4820, 7),
	STEEL(1539, 10),
	MITHRIL(4822, 13),
	ADAMANT(4823, 15),
	RUNE(4824, 20)
	;
	
	/**
	 * The nail item id.
	 */
	private final int itemId;
	
	/**
	 * The bend rate.
	 */
	private final int bendRate;

	/**
	 * Constructs a new {@code NailType} {@code Object}.
	 * @param itemId The item id.
	 * @param bendRate The bending rate.
	 */
	private NailType(int itemId, int bendRate) {
		this.itemId = itemId;
		this.bendRate = bendRate;
	}
	
	/**
	 * Checks if the nail will bend.
	 * @return {@code True} if Random.nextInt(bendRate) equals 0.
	 */
	public boolean isBend() {
		return RandomFunction.getRandom(bendRate) == 0;
	}

	/**
	 * Gets the nail type used by the player.
	 * @param player The player.
	 * @param amount The nails required.
	 * @return The nail type used, or null if the player didn't have the nails.
	 */
	public static NailType get(Player player, int amount) {
		for (int i = values().length - 1; i >= 0; i--) {
			NailType type = values()[i];
			if (player.getInventory().contains(type.itemId, amount)) {
				return type;
			}
		}
		return null;
	}

	/**
	 * Gets the itemId value.
	 * @return The itemId.
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Gets the bendRate value.
	 * @return The bendRate.
	 */
	public int getBendRate() {
		return bendRate;
	}
}
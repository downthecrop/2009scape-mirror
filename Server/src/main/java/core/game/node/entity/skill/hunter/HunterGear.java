package core.game.node.entity.skill.hunter;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents a set of hunter gear.
 * @author Vexia
 */
public enum HunterGear {
	GLOVE_OF_SILENCE(3.0, new Item(10075)), SPOTIER_CAPE(5.0, new Item(10071)), SPOTTED_CAPE(5.0, new Item(10069)), LARUPIA(10.00, new Item(10045), new Item(10043), new Item(10041)), DESERT_GEAR(10.00, new Item(12568), new Item(10063), new Item(10061)), GRAAHK_GEAR(10.00, new Item(10051), new Item(10047), new Item(10049)), KYATT_GEAR(10.00, new Item(10039), new Item(10035), new Item(10037)), JUNGLE_GEAR(8.00, new Item(10059), new Item(10057)), POLAR_GEAR(8.00, new Item(10065), new Item(10067));

	/**
	 * Constructs a new {@code Trap} {@code Object}.
	 * @param equipment the equipment.
	 * @param chanceRate the rate.
	 */
	HunterGear(double chanceRate, Item... equipment) {
		this.equipment = equipment;
		this.chanceRate = chanceRate;
	}

	/**
	 * Represents the equipment.
	 */
	private final Item[] equipment;

	/**
	 * Represents the chance it increased.
	 */
	private final double chanceRate;

	/**
	 * Method used to check if the player in the gear.
	 * @param player the player.
	 * @return the gear.
	 */
	public static HunterGear inGear(final Player player) {
		int contained = 0;
		for (HunterGear type : values()) {
			for (Item i : type.getEquipment()) {
				if (player.getEquipment().containsItem(i)) {
					contained += 1;
				}
				if (contained == type.getEquipment().length) {
					return type;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the chance rate the player has.
	 * @param player the player.
	 * @return the rate.
	 */
	public static double getChanceRate(Player player) {
		final HunterGear gear = inGear(player);
		if (gear == null) {
			return 0.0;
		}
		return gear.getChanceRate();
	}

	/**
	 * Gets the equipment.
	 * @return The equipment.
	 */
	public Item[] getEquipment() {
		return equipment;
	}

	/**
	 * Gets the increase.
	 * @return The increase.
	 */
	public double getChanceRate() {
		return chanceRate;
	}
}
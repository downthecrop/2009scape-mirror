package core.game.content.activity.duel;

import core.game.container.impl.EquipmentContainer;

/**
 * Represents a dueling rule.
 * @author Emperor
 */
public enum DuelRule {
	NO_RANGE(4, "You cannot use Ranged attacks."), NO_MELEE(5, "You cannot use Melee attacks."), NO_MAGIC(6, "You cannot use Magic attacks."), FUN_WEAPONS(12, "You can only attack with 'fun' weapons."), NO_FORFEIT(0, "You cannot forfeit the duel."), NO_DRINKS(7, "You cannot use potions."), NO_FOOD(8, "You cannot use food."), NO_PRAYER(9, "You cannot use Prayer."), NO_MOVEMENT(1, "You cannot move."), OBSTACLES(10, "There will be obstacles in the arena."), ENABLE_SUMMONING(28, "Familiars will be allowed in the arena."), NO_SPECIAL_ATTACKS(13, "You cannot use special attacks."), NO_HATS(14, "", EquipmentContainer.SLOT_HAT), NO_CAPES(15, "", EquipmentContainer.SLOT_CAPE), NO_AMULET(16, "", EquipmentContainer.SLOT_AMULET), NO_WEAPON(17, "You can't use 2H weapons such as bows.", EquipmentContainer.SLOT_WEAPON), NO_SHIELD(19, "You can't use 2H weapons such as bows.", EquipmentContainer.SLOT_SHIELD), NO_BODY(18, "", EquipmentContainer.SLOT_CHEST), NO_LEGS(21, "", EquipmentContainer.SLOT_LEGS), NO_GLOVES(23, "", EquipmentContainer.SLOT_HANDS), NO_RINGS(26, "", EquipmentContainer.SLOT_RING), NO_BOOTS(24, "", EquipmentContainer.SLOT_FEET), NO_ARROWS(27, "", EquipmentContainer.SLOT_ARROWS);

	/**
	 * The config index.
	 */
	private final int configIndex;

	/**
	 * The information to display on the second screen.
	 */
	private final String info;

	/**
	 * The equpment slot.
	 */
	private int equipmentSlot = -1;

	/**
	 * Constructs a new {@code DuelRule} {@code Object}.
	 * @param configIndex The config index.
	 * @param info the info for the second screen.
	 */
	private DuelRule(int configIndex, String info) {
		this.configIndex = configIndex;
		this.info = info;
	}

	/**
	 * Constructs a new {@code DuelRule} {@code Object}.
	 * @param configIndex The config index.
	 * @param info the info for the second screen.
	 * @param equipmentSlot the equipment slot.
	 */
	private DuelRule(int configIndex, String info, int equipmentSlot) {
		this.configIndex = configIndex;
		this.info = info;
		this.equipmentSlot = equipmentSlot;
	}

	/**
	 * Gets the configIndex.
	 * @return The configIndex.
	 */
	public int getConfigIndex() {
		return configIndex;
	}

	/**
	 * Gets the info.
	 * @return The info.
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Gets the equipmentSlot.
	 * @return the equipmentSlot.
	 */
	public int getEquipmentSlot() {
		return equipmentSlot;
	}
}
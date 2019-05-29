package org.crandor.game.content.global;

import org.crandor.game.node.item.Item;

/**
 * Represents an experience lamp.
 * @author Vexia
 */
public enum Lamps {
	GENIE_LAMP(new Item(2528), 10), 
	STRONGHOLD_LAMP(new Item(4447), 500), 
	K_ACHIEVEMENT_1(new Item(11137), 1000),
	K_ACHIEVEMENT_2(new Item(11139), 5000), 
	K_ACHIEVEMENT_3(new Item(11141), 10000), 
	V_ACHIEVEMENT_1(new Item(11185), 2500, 30),
	V_ACHIEVEMENT_2(new Item(11186), 7500, 40), 
	V_ACHIEVEMENT_3(new Item(11187), 15000, 50),
	ULTRA_LAMP(new Item(14820), 30000, 30);

	/**
	 * The item id.
	 */
	private final Item item;

	/**
	 * The experience gained.
	 */
	private final int experience;

	/**
	 * The level requirement.
	 */
	private final int levelRequirement;

	/**
	 * Constructs a new {@code Lamps} {@code Object}
	 * @param item the item.
	 * @param experience the exp.
	 * @param levelRequirement the level requirement to meet.
	 */
	private Lamps(Item item, int experience, int levelRequirement) {
		this.item = item;
		this.experience = experience;
		this.levelRequirement = levelRequirement;
	}

	/**
	 * Constructs a new {@code Lamps} {@code Object}
	 * @param item the item.
	 * @param experience the exp.
	 */
	private Lamps(Item item, int experience) {
		this(item, experience, 0);
	}

	/**
	 * Gets the lamp by the item.
	 * @param item the item.
	 * @return the lamp.
	 */
	public static Lamps forItem(Item item) {
		for (Lamps l : values()) {
			if (l.getItem().getId() == item.getId()) {
				return l;
			}
		}
		return null;
	}

	/**
	 * Gets the item.
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Gets the mod.
	 * @return the mod
	 */
	public int getExp() {
		return experience;
	}

	/**
	 * Gets the levelRequirement.
	 * @return the levelRequirement
	 */
	public int getLevelRequirement() {
		return levelRequirement;
	}

}

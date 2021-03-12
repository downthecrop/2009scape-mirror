package core.game.node.entity.combat.equipment;

import core.game.node.item.Item;

/**
 * Represents the type of weapon used.
 * @author Emperor
 */
public class Weapon {

	/**
	 * The item id.
	 */
	private final int id;

	/**
	 * The item.
	 */
	private final Item item;

	/**
	 * The weapon's name.
	 */
	private final String name;

	/**
	 * The ammunition equipment slot (if any).
	 */
	private final int ammunitionSlot;

	/**
	 * The ammunition item (if any).
	 */
	private final Item ammunition;

	/**
	 * The weapon type.
	 */
	private WeaponType type;

	/**
	 * Represents the weapon types.
	 * @author Emperor
	 */
	public static enum WeaponType {
		DEFAULT, CROSSBOW, DOUBLE_SHOT, THROWN, DEGRADING, STAFF, CHINCHOMPA;
	}

	/**
	 * Constructs a new {@code Weapon} {@Code Object}.
	 * @param item The item.
	 */
	public Weapon(Item item) {
		this(item, -1, null, WeaponType.DEFAULT);
	}

	/**
	 * Constructs a new {@code Weapon} {@Code Object} using ammunition.
	 * @param item The item.
	 * @param ammunitionSlot The ammunition equipment slot.
	 * @param ammunition The ammunition item.
	 */
	public Weapon(Item item, int ammunitionSlot, Item ammunition) {
		this(item, ammunitionSlot, ammunition, WeaponType.DEFAULT);
	}

	/**
	 * Constructs a new {@code Weapon} {@Code Object} using ammunition.
	 * @param item The item.
	 * @param ammunitionSlot The ammunition equipment slot.
	 * @param ammunition The ammunition item.
	 * @param type The weapon type.
	 */
	public Weapon(Item item, int ammunitionSlot, Item ammunition, WeaponType type) {
		this.id = item == null ? -1 : item.getId();
		this.item = item;
		this.name = item == null ? "null" : item.getName();
		this.ammunition = ammunition;
		this.ammunitionSlot = ammunitionSlot;
	}

	/**
	 * @return the item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the ammunitionSlot.
	 */
	public int getAmmunitionSlot() {
		return ammunitionSlot;
	}

	/**
	 * @return the ammunition.
	 */
	public Item getAmmunition() {
		return ammunition;
	}

	/**
	 * @return the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the type.
	 */
	public WeaponType getType() {
		return type;
	}

	/**
	 * @param type the type to set.
	 */
	public void setType(WeaponType type) {
		this.type = type;
	}

	/**
	 * @return the name.
	 */
	public String getName() {
		return name;
	}
}
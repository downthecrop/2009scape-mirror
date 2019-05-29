package org.crandor.game.node.item;

import org.crandor.game.node.entity.npc.drop.DropFrequency;
import org.crandor.game.node.entity.npc.drop.NPCDropTables;
import org.crandor.tools.RandomFunction;

/**
 * Represents an item with a chance-rate.
 * @author Emperor
 */
public final class ChanceItem extends Item {

	/**
	 * The chance rate of this item.
	 */
	private double chanceRate;

	/**
	 * The minimum amount.
	 */
	private int minimumAmount;

	/**
	 * The maximum amount.
	 */
	private int maximumAmount;

	/**
	 * The table slot.
	 */
	private int tableSlot;

	/**
	 * The drop frequency.
	 */
	private DropFrequency dropFrequency;

	/**
	 * The set rate.
	 */
	private int setRate = -1;

	/**
	 * Constructs a new {@code ChanceItem} {@code Object}.
	 * @param id The item id.
	 */
	public ChanceItem(int id) {
		this(id, 1, 1, 1000, 1.0);
	}

	/**
	 * Constructs a new {@code ChanceItem} {@code Object}.
	 * @param id The item id.
	 * @param minimumAmount The minimum amount.
	 * @param chanceRatio The chance rate.
	 */
	public ChanceItem(int id, int minimumAmount, double chanceRate) {
		this(id, minimumAmount, minimumAmount, 1000, chanceRate);
	}

	/**
	 * Constructs a new {@code ChanceItem} {@code Object}.
	 * @param id The item id.
	 * @param minimumAmount The minimum amount.
	 * @param maximumAmount The maximum amount.
	 * @param chanceRatio The chance rate.
	 */
	public ChanceItem(int id, int minimumAmount, int maximumAmount, double chanceRate) {
		this(id, minimumAmount, maximumAmount, 1000, chanceRate);
	}

	/**
	 * Constructs a new {@code ChanceItem} {@code Object}.
	 * @param id the id.
	 * @param minimumAmount the min amount.
	 * @param maximumAmount the max amount.
	 * @param frequency the frequency.
	 */
	public ChanceItem(int id, int minimumAmount, int maximumAmount, DropFrequency frequency) {
		this(id, minimumAmount, maximumAmount, 1000, NPCDropTables.DROP_RATES[frequency.ordinal() - 1]);
	}

	/**
	 * Constructs a new {@code ChanceItem} {@code Object}.
	 * @param id The item id.
	 * @param minimumAmount The minimum amount.
	 * @param maximumAmount The maximum amount.
	 * @param charge The charge.
	 * @param chanceRatio The chance rate.
	 */
	public ChanceItem(int id, int minimumAmount, int maximumAmount, int charge, double chanceRate) {
		this(id, minimumAmount, maximumAmount, charge, chanceRate, DropFrequency.UNCOMMON);
	}

	/**
	 * Constructs a new {@code ChanceItem} {@code Object}.
	 * @param id The item id.
	 * @param minimumAmount The minimum amount.
	 * @param maximumAmount The maximum amount.
	 * @param charge The charge.
	 * @param frequency The drop frequency.
	 * @param chanceRatio The chance rate.
	 */
	public ChanceItem(int id, int minimumAmount, int maximumAmount, int charge, double chanceRate, DropFrequency frequency) {
		this(id, minimumAmount, maximumAmount, charge, chanceRate, frequency, -1);
	}

	/**
	 * Constructs a new {@code ChanceItem} {@code Object}.
	 * @param id The item id.
	 * @param minimumAmount The minimum amount.
	 * @param maximumAmount The maximum amount.
	 * @param charge The charge.
	 * @param frequency The drop frequency.
	 * @param chanceRatio The chance rate.
	 * @param set rate the rate.
	 */
	public ChanceItem(int id, int minimumAmount, int maximumAmount, int charge, double chanceRate, DropFrequency frequency, int setRate) {
		super(id, minimumAmount, charge);
		this.minimumAmount = minimumAmount;
		this.maximumAmount = maximumAmount;
		this.chanceRate = chanceRate;
		this.dropFrequency = frequency;
		this.setRate = setRate;
	}

	/**
	 * Gets the item instance.
	 * @return the item.
	 */
	public Item getRandomItem() {
		if (minimumAmount == maximumAmount) {
			return new Item(getId(), minimumAmount);
		}
		return new Item(getId(), RandomFunction.random(minimumAmount, maximumAmount));
	}

	/**
	 * Gets a random chance item from the table.
	 * @param table The table.
	 * @return The chance item.
	 */
	public static ChanceItem getItem(ChanceItem... table) {
		return getItem(RandomFunction.getRandomDouble(75.0), table);
	}

	/**
	 * Gets a random chance item from the table.
	 * @param table The table.
	 * @return The chance item.
	 */
	public static ChanceItem getItem(double chance, ChanceItem... table) {
		// TODO:
		return table[0];
	}

	/**
	 * Gets a copy.
	 * @return the copy.
	 */
	public ChanceItem getCopy() {
		ChanceItem newItem = new ChanceItem(getId(), minimumAmount, maximumAmount, getCharge(), chanceRate, dropFrequency);
		return newItem;
	}

	/**
	 * @return the chanceRate.
	 */
	public double getChanceRate() {
		return chanceRate;
	}

	/**
	 * @param chanceRate the chanceRate to set.
	 */
	public void setChanceRate(double chanceRate) {
		this.chanceRate = chanceRate;
	}

	/**
	 * @return the minimumAmount.
	 */
	public int getMinimumAmount() {
		return minimumAmount;
	}

	/**
	 * @param minimumAmount the minimumAmount to set.
	 */
	public void setMinimumAmount(int minimumAmount) {
		this.minimumAmount = minimumAmount;
	}

	/**
	 * @return the maximumAmount.
	 */
	public int getMaximumAmount() {
		return maximumAmount;
	}

	/**
	 * @param maximumAmount the maximumAmount to set.
	 */
	public void setMaximumAmount(int maximumAmount) {
		this.maximumAmount = maximumAmount;
	}

	/**
	 * @return the dropFrequency.
	 */
	public DropFrequency getDropFrequency() {
		return dropFrequency;
	}

	/**
	 * @param dropFrequency the dropFrequency to set.
	 */
	public void setDropFrequency(DropFrequency dropFrequency) {
		this.dropFrequency = dropFrequency;
	}

	/**
	 * @return the tableSlot.
	 */
	public int getTableSlot() {
		return tableSlot;
	}

	/**
	 * @param tableSlot the tableSlot to set.
	 */
	public void setTableSlot(int tableSlot) {
		this.tableSlot = tableSlot;
	}

	@Override
	public String toString() {
		return "ChanceItem " + super.toString() + " [chanceRate=" + chanceRate + ", minimumAmount=" + minimumAmount + ", maximumAmount=" + maximumAmount + ", tableSlot=" + tableSlot + ", dropFrequency=" + dropFrequency + "]";
	}

	/**
	 * Gets the setRate.
	 * @return the setRate
	 */
	public int getSetRate() {
		return setRate;
	}

	/**
	 * Sets the basetRate.
	 * @param setRate the setRate to set.
	 */
	public void setSetRate(int setRate) {
		this.setRate = setRate;
	}
}
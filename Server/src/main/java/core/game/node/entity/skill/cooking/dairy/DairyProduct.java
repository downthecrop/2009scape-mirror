package core.game.node.entity.skill.cooking.dairy;

import java.util.Arrays;

import core.game.node.item.Item;
import org.rs09.consts.Items;

/**
 * Represents an enumeration of dairy products.
 * @author 'Vexia
 */
public enum DairyProduct {
    POT_OF_CREAM(21, 18, new Item(Items.POT_OF_CREAM_2130, 1), new Integer[] { Items.BUCKET_OF_MILK_1927 }),
    PAT_OF_BUTTER(38, 40.5, new Item(Items.PAT_OF_BUTTER_6697, 1), new Integer[] { Items.BUCKET_OF_MILK_1927, Items.POT_OF_CREAM_2130 }),
    CHEESE(48, 64, new Item(Items.CHEESE_1985, 1), new Integer[] { Items.BUCKET_OF_MILK_1927, Items.POT_OF_CREAM_2130, Items.PAT_OF_BUTTER_6697 });

	/**
	 * The prodct <code>Item</code>.
	 */
	private Item product;

	/**
	 * The level required.
	 */
	private int level;

	/**
	 * /** The experience gained.
	 */
	private double experience;

    /** 
     * The possible inputs for making this dairy product
     */
    private Item[] inputs;

	/**
	 * Constructs a new {@code DairyProduct.java} {@code Object}.
	 * @param level
	 * @param experience
	 * @param product
	 */
	DairyProduct(int level, double experience, Item product, Integer[] inputs) {
		this.level = level;
		this.experience = experience;
		this.product = product;
        this.inputs = Arrays.stream(inputs).map(id -> new Item(id, 1)).toArray(len -> new Item[len]);
	}

	/**
	 * @return the product.
	 */
	public Item getProduct() {
		return product;
	}

	/**
	 * @return the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the experience.
	 */
	public double getExperience() {
		return experience;
	}

    public Item[] getInputs() {
        return inputs;
    }
}

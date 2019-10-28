package org.crandor.game.content.skill.member.farming.patch;

import org.crandor.game.content.skill.member.farming.FarmingNode;
import org.crandor.game.node.item.Item;

/**
 * Represents a herb node.
 * @author 'Vexia
 */
public final class HerbNode extends FarmingNode {

	/**
	 * Constructs a new {@code HerbNode} {@code Object}.
	 * @param seed the sed.
	 * @param product the product.
	 * @param base the base.
	 * @param growthCycles the cycles.
	 * @param minutes the minutes.
	 * @param level the level.
	 * @param experiences the experiences.
	 */
	public HerbNode(Item seed, Item product, int base, int growthCycles, int minutes, int level, double[] experiences) {
		super(seed, product, base, growthCycles, minutes, level, experiences);
	}

	@Override
	public int getDeathBase() {
		return 159;
	}

}

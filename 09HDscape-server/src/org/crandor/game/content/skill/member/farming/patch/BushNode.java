package org.crandor.game.content.skill.member.farming.patch;

import org.crandor.game.node.item.Item;

/**
 * Represents the bush node.
 * @author 'Vexia
 */
public class BushNode extends PickingNode {

	/**
	 * Constructs a new {@code BushNode} {@code Object}.
	 * @param seed the seed.
	 * @param product the product.
	 * @param base the base.
	 * @param growthCycles the cycles.
	 * @param minutes the minutes.
	 * @param level the level.
	 * @param experiences the experiences.
	 */
	public BushNode(Item seed, Item product, int base, int growthCycles, int minutes, int level, double[] experiences, final Item... protection) {
		super(seed, product, base, growthCycles, minutes, level, experiences, 4, protection);
	}

	@Override
	public int getDeathBase() {
		return 130;
	}

	@Override
	public int getDiseaseBase() {
		return 65;
	}
}

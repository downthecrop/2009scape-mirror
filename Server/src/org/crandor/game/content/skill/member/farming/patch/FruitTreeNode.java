package org.crandor.game.content.skill.member.farming.patch;

import org.crandor.game.content.skill.member.farming.wrapper.PatchCycle;
import org.crandor.game.node.item.Item;

/**
 * Represents a fruit tree node.
 * @author 'Vexia
 */
public class FruitTreeNode extends PickingNode {

	/**
	 * Constructs a new {@code FruitTreeNode} {@code Object}.
	 * @param seed the sed.
	 * @param product the product.
	 * @param base the base.
	 * @param growthCycles the cycles.
	 * @param minutes the minutes.
	 * @param level the level.
	 * @param experiences the experiences.
	 */
	public FruitTreeNode(Item seed, Item product, int base, int growthCycles, int minutes, int level, double[] experiences, final Item... protection) {
		super(seed, product, base, growthCycles, minutes, level, experiences, 6, protection);
	}

	@Override
	public boolean canDisease(final PatchCycle cycle) {
		return super.canDisease(cycle);
	}

	@Override
	public int getCheckHealthBase() {
		return getBase() + 26;
	}

	@Override
	public boolean isStump(PatchCycle cycle) {
		return cycle.getState() == getStumpBase();
	}

	@Override
	public int getStumpBase() {
		return getBase() + 25;
	}

	@Override
	public int getDiseaseBase() {
		return 12;
	}

	@Override
	public int getDeathBase() {
		return 19;
	}
}

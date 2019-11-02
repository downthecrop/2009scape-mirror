package org.crandor.game.content.skill.member.hunter;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.item.Item;

/**
 * Handles the box trap node.
 * @author Vexia
 */
public final class BoxTrapNode extends TrapNode {

	/**
	 * The summoning level required.
	 */
	private final int summoningLevel;
			
	/**
	 * Constructs a new {@code BoxTrapNode} {@code Object}.
	 * @param npcIds the npc ids.
	 * @param level the level.
	 * @param experience the experience.
	 * @param objectIds the objectids.
	 * @param rewards the rewards.
	 */
	public BoxTrapNode(int[] npcIds, int level, double experience, Item[] rewards, final int summoningLevel) {
		super(npcIds, level, experience, new int[] { 19188, 19189 }, rewards);
		this.summoningLevel = summoningLevel;
	}

	@Override
	public boolean canCatch(TrapWrapper wrapper, final NPC npc) {
		if (wrapper.getPlayer().getSkills().getStaticLevel(Skills.SUMMONING) < summoningLevel) {
			return false;
		}
		return super.canCatch(wrapper, npc);
	}
}

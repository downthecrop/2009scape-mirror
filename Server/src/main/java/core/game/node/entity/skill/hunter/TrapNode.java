package core.game.node.entity.skill.hunter;

import core.game.content.global.SkillingPets;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Represents a node for a trap.
 * @author Vexia
 */
public class TrapNode {

	/**
	 * The npc ids related to the node.
	 */
	private final int[] npcIds;

	/**
	 * The level required.
	 */
	private final int level;

	/**
	 * The experience received.
	 */
	private final double experience;

	/**
	 * The object ids for this node.
	 */
	private final int[] objectIds;

	/**
	 * The rewards received.
	 */
	private final Item[] rewards;

	/**
	 * Constructs a new {@code TrapNode} {@code Object}.
	 * @param npcIds the npc ids.
	 * @param level the level.
	 * @param experience the experience.
	 * @param rewards the rewards.
	 */
	public TrapNode(int[] npcIds, int level, double experience, int[] objectIds, Item[] rewards) {
		this.npcIds = npcIds;
		this.level = level;
		this.experience = experience;
		this.objectIds = objectIds;
		this.rewards = rewards;
	}

	/**
	 * Has the requirements to catch the node.
	 * @param wrapper the wrapper.
	 * @param npc the npc.
	 * @return {@code True} if so.
	 * @note Override for quests.
	 */
	public boolean canCatch(TrapWrapper wrapper, final NPC npc) {
		final Player player = wrapper.getPlayer();
		if (wrapper.isCaught() || wrapper.isBusy() || wrapper.isFailed()) {
			return false;
		}
		return player.getHunterManager().getStaticLevel() >= level && !npc.isInvisible();
	}

	/**
	 * Gets the transform id.
	 * @return the id.
	 */
	public int getTransformId() {
		return objectIds[0];
	}

	/**
	 * Gets the final id.
	 * @return the id.
	 */
	public int getFinalId() {
		return objectIds[1];
	}

	/**
	 * Gets the npcIds.
	 * @return The npcIds.
	 */
	public int[] getNpcIds() {
		return npcIds;
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the experience.
	 * @return The experience.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the rewards.
	 * @return The rewards.
	 */
	public Item[] getRewards() {
		return rewards;
	}

	/**
	 * Gets the objectIds.
	 * @return The objectIds.
	 */
	public int[] getObjectIds() {
		return objectIds;
	}

	public SkillingPets getPet() {
		return null;
	}

}

package core.game.node.entity.skill.hunter.falconry;

import core.game.node.entity.npc.NPC;
import core.game.node.item.Item;

/**
 * Represents a falcon catch.
 * @author Vexia
 */
public enum FalconCatch {
	SPOTTED_KEBBIT(5098, 43, 104, new Item(10125)), DARK_KEBBIT(5099, 57, 132, new Item(10115)), DASHING_KEBBIT(5100, 69, 156, new Item(10127));

	/**
	 * Represents the npc.
	 */
	private final int npc;

	/**
	 * Represents the level.
	 */
	private final int level;

	/**
	 * Represents the experience gained.
	 */
	private final double experience;

	/**
	 * Represents the item reward.
	 */
	private final Item item;

	/**
	 * Constructs a new {@code FalconCatch} {@code Object}.
	 * @param npc the npc.
	 * @param level the level.
	 * @param experience the experience.
	 * @param item the item.
	 */
	FalconCatch(int npc, int level, double experience, Item item) {
		this.npc = npc;
		this.level = level;
		this.experience = experience;
		this.item = item;
	}

	/**
	 * Gets the falcon catch.
	 * @param item the item.
	 * @return the falcon catch.
	 */
	public static FalconCatch forItem(final Item item) {
		for (FalconCatch falconCatch : FalconCatch.values()) {
			if (item.getId() == falconCatch.getItem().getId()) {
				return falconCatch;
			}
		}
		return null;
	}

	/**
	 * Gets the falcon catch.
	 * @param npc the npc.
	 * @return the falcon catch.
	 */
	public static FalconCatch forNPC(final NPC npc) {
		for (FalconCatch falconCatch : FalconCatch.values()) {
			if (npc.getId() == falconCatch.getNpc()) {
				return falconCatch;
			}
		}
		return null;
	}

	/**
	 * Gets the npc.
	 * @return The npc.
	 */
	public int getNpc() {
		return npc;
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
	 * Gets the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

}

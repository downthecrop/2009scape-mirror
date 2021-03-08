package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The skill context.
 * @author Emperor
 */
public final class SkillContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The skill id.
	 */
	private final int skillId;

	/**
	 * Constructs a new {@code SkillContext} {@code Object}.
	 * @param player The player.
	 * @param skillId The skill id.
	 */
	public SkillContext(Player player, int skillId) {
		this.player = player;
		this.skillId = skillId;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the skillId.
	 * @return The skillId.
	 */
	public int getSkillId() {
		return skillId;
	}

}
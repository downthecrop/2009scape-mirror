package core.game.content.activity.gwd;

import api.ContentAPIKt;
import api.God;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

import static api.ContentAPIKt.hasGodItem;

/**
 * The god wars factions.
 * @author Emperor
 */
public enum GodWarsFaction {
	ARMADYL(6222, 6246, God.ARMADYL),
	BANDOS(6260, 6283, God.BANDOS),
	SARADOMIN(6247, 6259, God.SARADOMIN),
	ZAMORAK(6203, 6221, God.ZAMORAK);

	/**
	 * The start NPC id.
	 */
	private final int startId;

	/**
	 * The end NPC id.
	 */
	private final int endId;

	/**
	 * The god this faction represents
	 */
	private final God god;

	/**
	 * Constructs a new {@code GodWarsFaction} {@code Object}.
	 * @param startId The start NPC id.
	 * @param endId The end NPC id.
	 * @param god The god this faction represents.
	 */
	private GodWarsFaction(int startId, int endId, God god) {
		this.startId = startId;
		this.endId = endId;
		this.god = god;
	}

	/**
	 * Gets the god wars faction for the given NPC id.
	 * @param npcId The NPC id.
	 * @return The faction for this NPC.
	 */
	public static GodWarsFaction forId(int npcId) {
		for (GodWarsFaction faction : values()) {
			if (npcId >= faction.getStartId() && npcId <= faction.getEndId()) {
				return faction;
			}
		}
		return null;
	}

	/**
	 * Checks if the player is protected from this faction.
	 * @param player The player.
	 * @return {@code True} if no NPCs of this faction should attack the
	 * player.
	 */
	public boolean isProtected(Player player) {
		return hasGodItem(player, god);
	}

	/**
	 * Gets the startId.
	 * @return The startId.
	 */
	public int getStartId() {
		return startId;
	}

	/**
	 * Gets the endId.
	 * @return The endId.
	 */
	public int getEndId() {
		return endId;
	}
}
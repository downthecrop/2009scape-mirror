package core.game.node.entity.combat;

import core.game.node.entity.combat.handlers.MagicSwingHandler;
import core.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.combat.handlers.RangeSwingHandler;
import core.game.node.entity.player.link.prayer.PrayerType;

/**
 * Represents the different styles of combat.
 * @author Emperor
 */
public enum CombatStyle {

	/**
	 * The melee combat style.
	 */
	MELEE(new MeleeSwingHandler(), PrayerType.PROTECT_FROM_MELEE),

	/**
	 * The range combat style.
	 */
	RANGE(new RangeSwingHandler(), PrayerType.PROTECT_FROM_MISSILES),

	/**
	 * The magic combat style.
	 */
	MAGIC(new MagicSwingHandler(), PrayerType.PROTECT_FROM_MAGIC);

	/**
	 * The combat swing handler used by the combat style.
	 */
	private final CombatSwingHandler swingHandler;

	/**
	 * The protection prayer for this combat type.
	 */
	private final PrayerType protectionPrayer;

	/**
	 * Constructs a new {@code CombatStyle} {@code Object}.
	 * @param swingHandler The combat swing handler.
	 * @param protectionPrayer The protection prayer.
	 */
	private CombatStyle(CombatSwingHandler swingHandler, PrayerType protectionPrayer) {
		this.swingHandler = swingHandler;
		this.protectionPrayer = protectionPrayer;
		swingHandler.setType(this);
	}

	/**
	 * Gets the swingHandler.
	 * @return The swingHandler.
	 */
	public CombatSwingHandler getSwingHandler() {
		return swingHandler;
	}

	/**
	 * Gets the protectionPrayer.
	 * @return The protectionPrayer.
	 */
	public PrayerType getProtectionPrayer() {
		return protectionPrayer;
	}
}
package core.game.node.entity.state;

import core.game.node.entity.state.impl.*;

/**
 * Represents the statuses.
 * @author Emperor
 */
public enum EntityState {

	/**
	 * The entity is poisoned.
	 */
	POISONED(new PoisonStatePulse(null)),

	/**
	 * The entity is stunned.
	 */
	STUNNED(new StunStatePulse(null, 0)),

	/**
	 * The entity is frozen.
	 */
	FROZEN(new FrozenStatePulse(null, 0)),

	/**
	 * The entity is skulled.
	 */
	SKULLED(new SkullStatePulse(null, 0)),

	/**
	 * The entity is under teleblock.
	 */
	TELEBLOCK(new TeleblockStatePulse(null, 0, 0)),
	
	/**
	 * The entity has decreased weapon speeds.
	 */
	MIASMIC(new MiasmicStatePulse(null, 0)),

	/**
	 * The entity is healing over time
	 */
	HEALOVERTIME(new HealOverTimePulse(null,0,0,0,0));

	/**
	 * The state pulse used for this state.
	 */
	private final StatePulse pulse;

	/**
	 * Constructs a new {@code EntityState} {@code Object}.
	 * @param pulse The state pulse.
	 */
	private EntityState(StatePulse pulse) {
		this.pulse = pulse;
	}

	/**
	 * Gets the pulse.
	 * @return The pulse.
	 */
	public StatePulse getPulse() {
		return pulse;
	}
}
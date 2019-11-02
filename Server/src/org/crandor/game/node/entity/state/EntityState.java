package org.crandor.game.node.entity.state;

import org.crandor.game.node.entity.state.impl.*;

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
	 * The entity is diseased.
	 */
	DISEASED(new DiseaseStatePulse(null)),

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
	 * The entity is charged (god spells).
	 */
	CHARGED(new ChargedStatePulse(null, 0)),

	/**
	 * The entity is under the ava device effect.
	 */
	AVA_DEVICE(new AvaDevicePulse(null, 0)),

	/**
	 * The entity is under fire resistant.
	 */
	FIRE_RESISTANT(new FireResistantPulse(null, 0, 0, false)),

	/**
	 * The entity is incubating an egg.
	 */
	INCUBATION(new IncubatorStatePulse()),

	/**
	 * The entity is under teleblock.
	 */
	TELEBLOCK(new TeleblockStatePulse(null, 0, 0)),

	/**
	 * The entity is under Double XP.
	 */
	DOUBLE_EXPERIENCE(new DoubleExperiencePulse(null, 0, 0)),
	
	/**
	 * The entity has the staff of the dead special effect.
	 */
	STAFF_OF_THE_DEAD(new StaffOfTheDeadPulse(null, 0, 0)),
	
	/**
	 * The entity has decreased weapon speeds.
	 */
	MIASMIC(new MiasmicStatePulse(null, 0));

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
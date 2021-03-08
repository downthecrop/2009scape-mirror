package core.game.node.entity.state;

import core.game.node.entity.Entity;
import core.game.node.entity.player.info.login.SavingModule;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles an entity's status (eg. poisoned, stunned, frozen, skulled, ...)
 * @author Emperor
 */
public final class StateManager implements SavingModule {

	/**
	 * The entity.
	 */
	private final Entity entity;

	/**
	 * The entity's current states.
	 */
	private final Map<EntityState, StatePulse> states = new HashMap<>();

	/**
	 * Constructs a new {@code StateManager} {@code Object}.
	 * @param entity The entity.
	 */
	public StateManager(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void save(ByteBuffer buffer) {
		for (EntityState state : states.keySet()) {
			StatePulse pulse = states.get(state);
			if (pulse == null) {
				System.out.println("Pulse for state " + state + " is null!");
				continue;
			}
			if (pulse.isSaveRequired()) {
				buffer.put((byte) state.ordinal());
				pulse.save(buffer);
			}
		}
		buffer.put((byte) -1);
	}

	@Override
	public void parse(ByteBuffer buffer) {
		int ordinal = 0;
		while ((ordinal = buffer.get()) != -1) {
			EntityState state = EntityState.values()[ordinal];
			StatePulse pulse = state.getPulse().parse(entity, buffer);
			if (pulse == null) {
				System.out.println("Pulse for state " + state + " is null!");
			}
			states.put(state, pulse);
		}
	}

	/**
	 * Initializes the pulses.
	 */
	public void init() {
		for (StatePulse pulse : states.values()) {
			pulse.run();
		}
	}

	/**
	 * Checks if a save is required.
	 * @return {@code True} if so.
	 */
	public boolean isSaveRequired() {
		return !states.isEmpty();
	}

	/**
	 * Registers a state.
	 * @param state The state.
	 * @param args The arguments.
	 */
	public void set(EntityState state, Object... args) {
		register(state, true, args);
	}

	/**
	 * Registers a state.
	 * @param state The state.
	 * @param override If the previous pulse (if any) should be overriden.
	 * @param args The arguments.
	 */
	public void register(EntityState state, boolean override, Object... args) {
		if (!state.getPulse().canRun(entity)) {
			return;
		}
		StatePulse pulse = states.get(state);
		if (pulse != null) {
			if (!override) {
				return;
			}
			pulse.stop();
		}
		pulse = state.getPulse().create(entity, args);
		pulse.run();
		states.put(state, pulse);
	}

	/**
	 * Resets all the states.
	 */
	public void reset() {
		for (StatePulse pulse : states.values()) {
			if (pulse.isRunning()) {
				pulse.remove();
				pulse.stop();
			}
		}
		states.clear();
	}

	/**
	 * Resets the state pulse.
	 * @param state The state.
	 */
	public void remove(EntityState state) {
		StatePulse pulse = states.get(state);
		if (pulse != null && pulse.isRunning()) {
			pulse.remove();
			pulse.stop();
		}
		states.remove(state);
	}

	/**
	 * Checks if the entity has a pulse running for the given state.
	 * @param state The state.
	 * @return {@code True} if so.
	 */
	public boolean hasState(EntityState state) {
		StatePulse pulse = states.get(state);
		return pulse != null && pulse.isRunning();
	}

	/**
	 * Gets the pulse for the given state.
	 * @param state The state to get the pulse for.
	 * @return The state pulse.
	 */
	public StatePulse get(EntityState state) {
		return states.get(state);
	}

	/**
	 * Gets the entity.
	 * @return The entity.
	 */
	public Entity getEntity() {
		return entity;
	}

	public Map<EntityState, StatePulse> getStates() {
		return states;
	}
}
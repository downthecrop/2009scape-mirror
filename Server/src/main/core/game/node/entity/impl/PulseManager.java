package core.game.node.entity.impl;

import core.game.interaction.MovementPulse;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.node.entity.combat.CombatPulse;
import core.game.world.GameWorld;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents an entity's pulse manager.
 * @author Emperor
 */
public final class PulseManager {

    private final Entity entity;

    public PulseManager(Entity entity) {
        this.entity = entity;
    }

	/**
	 * The movement pulse.
	 */
	private final HashMap<PulseType, Pulse> currentPulses = new HashMap<>();

	public void run(Pulse pulse) {
		run(pulse, PulseType.STANDARD);
	}

	/**
	 * Runs a pulse.
	 * @param pulse The pulse.
	 * @param pulseType The pulse type we're trying to run.
	 */
	public void run(Pulse pulse, PulseType pulseType) {
		ArrayList<PulseType> toRemove = new ArrayList<>(currentPulses.size());
		currentPulses.forEach((key, value) -> {
			if (value != null && !value.isRunning()) {
				toRemove.add(key);
			}
		});
		for (PulseType t : toRemove) currentPulses.remove(t);

		if (currentPulses.get(PulseType.STRONG) != null) {
			//strong pulses cannot be interrupted or ran alongside anything else. They are the ONLY pulse type when they are present.
			return;
		}

		if (!clear(pulseType)) {
			return;
		}

		if (pulseType == PulseType.STRONG) {
			 clear();
		}

		currentPulses.put(pulseType, pulse);
		pulse.start();
		if (pulse.isRunning()) {
			GameWorld.getPulser().submit(pulse);
		}
	}

	public void clear() {
		entity.scripts.removeWeakScripts();
		entity.scripts.removeNormalScripts();

		currentPulses.forEach((type, pulse) -> {
			if (type != PulseType.STRONG && pulse != null) pulse.stop();
		});
	}

	/**
	 * Clears the pulses.
     */
	public boolean clear(PulseType pulseType) {
		entity.scripts.removeWeakScripts();
		entity.scripts.removeNormalScripts();

		Pulse pulse = currentPulses.get(pulseType);

		if (pulse != null) {
			pulse.stop();
			currentPulses.remove(pulseType);
		}
		return true;
	}

	/**
	 * Runs the unhandled reward pulse ("Nothing interesting happens.")
	 * @param player The player.
	 * @return The pulse.
	 * @param pulseType The pulse type.
	 */
	public Pulse runUnhandledAction(final Player player, PulseType pulseType) {
		Pulse pulse = new Pulse(1, player) {
			@Override
			public boolean pulse() {
				player.getPacketDispatch().sendMessage("Nothing interesting happens.");
				return true;
			}
		};
		run(pulse, pulseType);
		return pulse;
	}

	/**
	 * Checks if the current pulse moves the entity.
	 * @return {@code True} if so.
	 */
	public boolean isMovingPulse() {
		if (!hasPulseRunning()) {
			return false;
		}

		Pulse current = getCurrent();
		return current instanceof MovementPulse || current instanceof CombatPulse;
	}

	/**
	 * Checks if a pulse is running.
	 * @return {@code True} if so.
	 */
	public boolean hasPulseRunning() {
		return getCurrent() != null && getCurrent().isRunning();
	}

	/**
	 * Cancels the death task, if any.
	 * @param e The entity.
	 */
	public static void cancelDeathTask(Entity e) {
		if (!DeathTask.isDead(e) || e.getPulseManager().getCurrent() == null) {
			return;
		}
		e.getPulseManager().getCurrent().stop();
	}

	/**
	 * Gets the current.
	 * @return The current.
	 */
	public Pulse getCurrent() {
		PulseType[] types = PulseType.values();
		for (PulseType type : types) {
			if (currentPulses.get(type) != null) {
				return currentPulses.get(type);
			}
		}
		return null;
	}

}


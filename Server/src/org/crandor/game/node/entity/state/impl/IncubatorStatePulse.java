package org.crandor.game.node.entity.state.impl;

import org.crandor.game.content.skill.member.summoning.pet.IncubatorEgg;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.StatePulse;
import org.crandor.game.world.GameWorld;

import java.nio.ByteBuffer;

/**
 * The pulse used for incubating an egg.
 * @author Vexia
 */
public class IncubatorStatePulse extends StatePulse {

	/**
	 * The egg.
	 */
	private final IncubatorEgg egg;

	/**
	 * The current tick.
	 */
	private int currentTick;

	/**
	 * The end time.
	 */
	private int endTime;

	/**
	 * Constructs a new {@Code IncubatorStatePulse} {@Code Object}
	 * @param entity entity.
	 * @param ticks the ticks.
	 * @param currentTick the current tick.
	 * @param egg the egg.
	 */
	public IncubatorStatePulse(Entity entity, int ticks, int currentTick, IncubatorEgg egg) {
		super(entity, ticks);
		this.egg = egg;
		this.currentTick = currentTick;
		this.endTime = getEndTime();
	}

	/**
	 * Constructs a new {@Code IncubatorStatePulse} {@Code Object}
	 */
	public IncubatorStatePulse() {
		this(null, -1, -1, null);
	}

	@Override
	public boolean isSaveRequired() {
		return currentTick < endTime;
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.putInt(currentTick);
		buffer.put((byte) egg.ordinal());
	}

	@Override
	public StatePulse parse(Entity entity, ByteBuffer buffer) {
		IncubatorStatePulse state = new IncubatorStatePulse(entity, 1, buffer.getInt(), IncubatorEgg.values()[buffer.get()]);
		return state;
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		Player p = entity.asPlayer();
		IncubatorEgg egg = (IncubatorEgg) args[0];
		p.removeAttribute("inc");
		p.getInventory().remove(egg.getEgg());
		p.sendMessage("You store the egg in the incubator. It will hatch after a while.");
		return new IncubatorStatePulse(entity, 1, 0, egg);
	}

	@Override
	public void start() {
		super.start();
		entity.asPlayer().getFamiliarManager().setConfig(1 << 4);
	}

	@Override
	public boolean pulse() {
		if (++currentTick >= endTime) {
			entity.asPlayer().setAttribute("/save:inc", egg.ordinal());
			entity.asPlayer().sendMessage("Your " + egg.getProduct().getName().toLowerCase() + " has finished hatching.");
			return true;
		}
		return false;
	}

	/**
	 * Gets the end time.
	 * @return the time.
	 */
	private int getEndTime() {
		if (GameWorld.getSettings().isDevMode()) {
			return 30;
		}
		if (egg == null) {
			return 60000;
		}
		return (int) ((egg.getInucbationTime() * 100));
	}

	/**
	 * Gets the incubator egg.
	 * @return the egg.
	 */
	public IncubatorEgg getEgg() {
		return egg;
	}
}
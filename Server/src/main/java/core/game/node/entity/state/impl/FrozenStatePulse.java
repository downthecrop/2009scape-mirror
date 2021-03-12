package core.game.node.entity.state.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.StatePulse;
import core.game.world.GameWorld;

import java.nio.ByteBuffer;

/**
 * Handles the frozen state pulse.
 * @author Emperor
 */
public final class FrozenStatePulse extends StatePulse {

	/**
	 * The amount of ticks immunity lasts.
	 */
	private static final int IMMUNITY_TICK = 7;

	/**
	 * The message when frozen.
	 */
	private final String message;

	/**
	 * Constructs a new {@code FrozenStatePulse} {@code Object}.
	 * @param entity The entity.
	 * @param ticks The ticks to freeze for.
	 */
	public FrozenStatePulse(Entity entity, String message, int ticks) {
		super(entity, ticks);
		this.message = message;
	}

	/**
	 * Constructs a new {@code FrozenStatePulse} {@code Object}.
	 * @param entity The entity.
	 * @param ticks The ticks to freeze for.
	 */
	public FrozenStatePulse(Entity entity, int ticks) {
		this(entity, "You have been frozen!", ticks);
	}

	@Override
	public boolean canRun(Entity entity) {
		return entity.getAttribute("freeze_immunity", -1) < GameWorld.getTicks();
	}

	@Override
	public void start() {
		super.start();
		if (entity.getPulseManager().isMovingPulse()) {
			entity.getPulseManager().clear();
		}
		entity.getWalkingQueue().reset();
		entity.getLocks().lockMovement(getDelay());
		entity.setAttribute("freeze_immunity", GameWorld.getTicks() + getDelay() + IMMUNITY_TICK);
		if (entity instanceof Player) {
			((Player) entity).getPacketDispatch().sendMessage(message);
		}
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		if (args.length > 1) {
			return new FrozenStatePulse(entity, (String) args[1], (Integer) args[0]);
		} else {
			return new FrozenStatePulse(entity, (Integer) args[0]);
		}
	}

	@Override
	public boolean pulse() {
		return true;
	}

	@Override
	public boolean isSaveRequired() {
		return false;
	}

	@Override
	public void save(ByteBuffer buffer) {
		/*
		 * empty.
		 */
	}

	@Override
	public StatePulse parse(Entity entity, ByteBuffer buffer) {
		return null;
	}

}

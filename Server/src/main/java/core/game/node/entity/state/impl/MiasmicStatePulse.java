package core.game.node.entity.state.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.StatePulse;
import rs09.game.world.GameWorld;

import java.nio.ByteBuffer;

/**
 * Handles the Miasmic state pulse.
 * @author Emperor
 */
public final class MiasmicStatePulse extends StatePulse {

	/**
	 * The amount of ticks immunity lasts.
	 */
	private static final int IMMUNITY_TICK = 7;

	/**
	 * The message when frozen.
	 */
	private final String message;

	/**
	 * Constructs a new {@code MiasmicStatePulse} {@code Object}.
	 * @param entity The entity.
	 * @param ticks The ticks to freeze for.
	 */
	public MiasmicStatePulse(Entity entity, String message, int ticks) {
		super(entity, ticks);
		this.message = message;
	}

	/**
	 * Constructs a new {@code MiasmicStatePulse} {@code Object}.
	 * @param entity The entity.
	 * @param ticks The ticks to freeze for.
	 */
	public MiasmicStatePulse(Entity entity, int ticks) {
		this(entity, null, ticks);
	}

	@Override
	public boolean canRun(Entity entity) {
		return entity.getAttribute("miasmic_immunity", -1) < GameWorld.getTicks();
	}

	@Override
	public void start() {
		super.start();
		entity.setAttribute("miasmic_immunity", GameWorld.getTicks() + getDelay() + IMMUNITY_TICK);
		if (entity instanceof Player) {
			((Player) entity).getPacketDispatch().sendMessage(message);
		}
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		if (args.length > 1) {
			return new MiasmicStatePulse(entity, (String) args[1], (Integer) args[0]);
		} else {
			return new MiasmicStatePulse(entity, (Integer) args[0]);
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
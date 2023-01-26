package core.game.node.entity.state.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.StatePulse;

import java.nio.ByteBuffer;

/**
 * Handles the skulled state.
 * @author Emperor
 */
public final class SkullStatePulse extends StatePulse {

	/**
	 * The amount of ticks.
	 */
	public static final int TICKS = 2000;

	/**
	 * Constructs a new {@code SkullStatePulse} {@code Object}.
	 * @param entity The entity.
	 * @param ticks The amount of ticks.
	 */
	public SkullStatePulse(Entity entity, int ticks) {
		super(entity, ticks);
	}

	@Override
	public void start() {
		super.start();
		Player player = (Player) entity;
		player.getSkullManager().setSkullIcon(0);
		player.getSkullManager().setSkulled(true);
	}

	@Override
	public boolean isSaveRequired() {
		return getTicksPassed() < getDelay();
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.putShort((short) (getDelay() - getTicksPassed()));
	}

	@Override
	public StatePulse parse(Entity entity, ByteBuffer buffer) {
		return create(entity, buffer.getShort() & 0xFFFF);
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		int ticks = args.length > 0 ? (Integer) args[0] : TICKS;
		return new SkullStatePulse(entity, ticks);
	}

	@Override
	public boolean pulse() {
		setTicksPassed(getDelay());
		remove();
		return true;
	}

	@Override
	public void remove() {
		((Player) entity).getSkullManager().reset();
	}

}
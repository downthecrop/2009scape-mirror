package org.crandor.game.node.entity.state.impl;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.StatePulse;

import java.nio.ByteBuffer;

/**
 * Handles the charged state (god spells).
 * @author Emperor
 */
public final class ChargedStatePulse extends StatePulse {

	/**
	 * The amount of ticks.
	 */
	public static final int TICKS = 700;

	/**
	 * Constructs a new {@code ChargedStatePulse} {@code Object}.
	 * @param entity The entity.
	 * @param ticks The amount of ticks.
	 */
	public ChargedStatePulse(Entity entity, int ticks) {
		super(entity, ticks);
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
		return new ChargedStatePulse(entity, ticks);
	}

	@Override
	public boolean pulse() {
		setTicksPassed(getDelay());
		if (entity instanceof Player) {
			((Player) entity).getPacketDispatch().sendMessage("Your magical charge fades away.");
		}
		return true;
	}
}
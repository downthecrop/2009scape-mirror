package core.game.node.entity.state.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.StatePulse;

import java.nio.ByteBuffer;

/**
 * Handles the teleblock state pulse.
 * @author Vexia
 */
public final class TeleblockStatePulse extends StatePulse {

	/**
	 * The ticks needed to pass.
	 */
	private final int ticks;

	/**
	 * The current tick.
	 */
	private int currentTick;

	/**
	 * Constructs a new {@Code TeleblockStatePulse} {@Code Object}
	 * @param entity the entity.
	 * @param ticks the ticks.
	 * @param currentTick the current tick.
	 */
	public TeleblockStatePulse(Entity entity, int ticks, int currentTick) {
		super(entity, 1);
		this.ticks = ticks;
		this.currentTick = currentTick;
	}

	@Override
	public boolean isSaveRequired() {
		return currentTick < ticks;
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.putInt(ticks);
		buffer.putInt(currentTick);
	}

	@Override
	public StatePulse parse(Entity entity, ByteBuffer buffer) {
		return new TeleblockStatePulse(entity, buffer.getInt(), buffer.getInt());
	}

	@Override
	public void start() {
		if (currentTick == 0) {
			if (entity instanceof Player) {
				entity.asPlayer().getAudioManager().send(203, true);
				entity.asPlayer().sendMessage("You have been teleblocked.");
			}
		}
		super.start();
	}

	@Override
	public boolean pulse() {
		return ++currentTick >= ticks;
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		return new TeleblockStatePulse(entity, (int) args[0], 0);
	}

}

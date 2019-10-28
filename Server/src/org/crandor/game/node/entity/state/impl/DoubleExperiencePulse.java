package org.crandor.game.node.entity.state.impl;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.StatePulse;

import java.nio.ByteBuffer;

/**
 * Monitors the state of being in a Double EXP mode.
 * @author Splinter
 * 
 */
public final class DoubleExperiencePulse extends StatePulse {

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
	public DoubleExperiencePulse(Entity entity, int ticks, int currentTick) {
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
		return new DoubleExperiencePulse(entity, buffer.getInt(), buffer.getInt());
	}

	@Override
	public void start() {
		if (currentTick == 0) {
			if (entity instanceof Player) {
				if(entity.asPlayer().getSavedData().getGlobalData().hasDoubleExp()){
					entity.asPlayer().sendMessage("<col=006600>You have activated Double Experience.</col>");   
				}
			}
		}
		super.start();
	}

	@Override
	public boolean pulse() {
		long milliseconds = entity.asPlayer().getSavedData().getGlobalData().getDoubleExp() - System.currentTimeMillis();
		int minutes = (int) Math.round(milliseconds / 60000);
		currentTick = minutes * 100;
		if(milliseconds / 600 == 300){
			entity.asPlayer().sendMessage("<col=FF0000>You have 3 more minutes of Double Experience remaining.</col>");
		}
		if(milliseconds / 600 < 1){
			if(entity.asPlayer().getSavedData().getGlobalData().hasDoubleExp()){
				entity.asPlayer().sendMessage("<col=FF0000>Your Double Experience has run out!</col>");
			}
		}
		return !entity.asPlayer().getSavedData().getGlobalData().hasDoubleExp();
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		return new DoubleExperiencePulse(entity, (int) args[0], 0);
	}

}

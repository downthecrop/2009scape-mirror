package org.crandor.game.node.entity.state.impl;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.node.entity.state.StatePulse;

import java.nio.ByteBuffer;

/**
 * Monitors the state of being under the special effect of the Staff of the Dead.
 * @author Splinter
 * 
 */
public final class StaffOfTheDeadPulse extends StatePulse {

	/**
	 * The ticks needed to pass.
	 */
	private final int ticks;

	/**
	 * The current tick.
	 */
	private int currentTick;

	/**
	 * Constructs a new {@Code StaffOfTheDeadPulse} {@Code Object}
	 * @param entity the entity.
	 * @param ticks the ticks.
	 * @param currentTick the current tick.
	 */
	public StaffOfTheDeadPulse(Entity entity, int ticks, int currentTick) {
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
		return new StaffOfTheDeadPulse(entity, buffer.getInt(), buffer.getInt());
	}

	@Override
	public void start() {
		if (currentTick == 0) {
			if (entity instanceof Player) {
				entity.asPlayer().sendMessage("<col=006600>Spirits of deceased evildoers offer you their protection.</col>");   
			}
		}
		super.start();
	}
	
	/**
	 * The pulse will end early if the player unequips their staff or
	 * they equip another weapon.
	 */
	@Override
	public boolean pulse() {
		Player player = entity.asPlayer();
		if((++currentTick >= ticks) || player.getEquipment().get(3) == null || (player.getEquipment().get(3) != null && player.getEquipment().get(3).getId() != 14726)){
			player.sendMessage("<col=006600>Your protection fades away.</col>");
			player.getStateManager().remove(EntityState.STAFF_OF_THE_DEAD);
			return true;
		}
		return false;
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		return new StaffOfTheDeadPulse(entity, (int) args[0], 0);
	}

}

package core.game.node.entity.state.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.StatePulse;
import core.game.world.update.flag.context.Graphics;

import java.nio.ByteBuffer;

/**
 * Handles the stunned state.
 * @author Emperor
 */
public final class StunStatePulse extends StatePulse {

	/**
	 * The stun graphic.
	 */
	private static final Graphics STUN_GRAPHIC = new Graphics(80, 96);
	
	/**
	 * The stun message.
	 */
	private String stunMessage;

	/**
	 * Constructs a new {@code FrozenStatePulse} {@code Object}.
	 * @param entity The entity.
	 * @param ticks The ticks to freeze for.
	 * @param stunMessage The stun message.
	 */
	public StunStatePulse(Entity entity, int ticks, String stunMessage) {
		super(entity, ticks);
		this.stunMessage = stunMessage;
	}
	
	/**
	 * Constructs a new @{Code StunStatePulse} object.
	 * @param entity The entity.
	 * @param ticks The ticks to freeze for.
	 */
	public StunStatePulse(Entity entity, int ticks) {
		this(entity, ticks, "You have been stunned!");
	}

	@Override
	public void start() {
		super.start();
		entity.getWalkingQueue().reset();
		entity.getLocks().lock(getDelay());
		entity.graphics(STUN_GRAPHIC);
		if (entity instanceof Player) {
			entity.asPlayer().getAudioManager().send(3201);
			((Player) entity).getPacketDispatch().sendMessage(stunMessage);
		}
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		return new StunStatePulse(entity, (Integer) args[0], args.length > 1 ? (String) args[1] : "You have been stunned!");
	}

	@Override
	public boolean pulse() {
		if (entity.getAnimator().getGraphics() == STUN_GRAPHIC) {
			entity.graphics(Graphics.create(-1));
		}
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
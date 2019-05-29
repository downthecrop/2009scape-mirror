package org.crandor.game.world.update.flag.player;

import org.crandor.game.world.update.flag.UpdateFlag;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.net.packet.IoBuffer;

/**
 * Handles the animation update flag.
 * @author Emperor
 */
public final class AnimationFlag extends UpdateFlag<Animation> {

	/**
	 * Constructs a new {@code AnimationFlag} {@code Object}.
	 * @param id The animation id.
	 * @param delay The animation delay.
	 */
	public AnimationFlag(Animation context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.putShort(context.getId());
		buffer.put(context.getDelay());
	}

	@Override
	public int data() {
		return maskData();
	}

	@Override
	public int ordinal() {
		return 2;
	}

	/**
	 * Gets the mask data of the animation update flag.
	 * @return The mask data.
	 */
	public static int maskData() {
		return 0x8;
	}
}
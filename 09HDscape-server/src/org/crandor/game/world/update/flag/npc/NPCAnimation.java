package org.crandor.game.world.update.flag.npc;

import org.crandor.game.world.update.flag.UpdateFlag;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.net.packet.IoBuffer;

/**
 * The NPC animation update flag.
 * @author Emperor
 *
 */
public final class NPCAnimation extends UpdateFlag<Animation> {

	/**
	 * Constructs a new {@code NPCAnimation} {@code Object}.
	 * @param context The animation.
	 */
	public NPCAnimation(Animation context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.putShort(context.getId()).put(context.getDelay());
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
	 * Gets the mask data.
	 * @return The mask data.
	 */
	public static int maskData() {
		return 0x10;
	}

}
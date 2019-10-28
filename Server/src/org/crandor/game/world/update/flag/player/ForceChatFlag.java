package org.crandor.game.world.update.flag.player;

import org.crandor.game.world.update.flag.UpdateFlag;
import org.crandor.net.packet.IoBuffer;

/**
 * The force chat update flag.
 * @author Emperor
 *
 */
public final class ForceChatFlag extends UpdateFlag<String> {

	/**
	 * Constructs a new {@code ForceChatFlag} {@code Object}.
	 * @param context The chat message.
	 */
	public ForceChatFlag(String context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.putString(context);
	}

	@Override
	public int data() {
		return maskData();
	}

	@Override
	public int ordinal() {
		return 6;
	}

	/**
	 * Gets the mask data.
	 * @return The mask data.
	 */
	public static int maskData() {
		return 0x20;
	}

}
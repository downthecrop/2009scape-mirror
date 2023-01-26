package core.game.world.update.flag.npc;

import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;

/**
 * The switch NPC id update flag.
 * @author Emperor
 *
 */
public final class NPCSwitchId extends UpdateFlag<Integer> {

	/**
	 * Constructs a new {@code NPCSwitchId} {@code Object}.
	 * @param context The new NPC id.
	 */
	public NPCSwitchId(int context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.putLEShort(context);
	}

	@Override
	public int data() {
		return 0x1;
	}

	@Override
	public int ordinal() {
		return getOrdinal();
	}

	/**
	 * Gets the mask ordinal.
	 * @return The ordinal.
	 */
	public static int getOrdinal() {
		return 5;
	}

}
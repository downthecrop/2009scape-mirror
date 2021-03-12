package core.game.world.update.flag.player;

import core.game.world.update.flag.UpdateFlag;
import core.game.world.update.flag.context.HitMark;
import core.net.packet.IoBuffer;

/**
 * The supportive hit update flag.
 * @author Emperor
 *
 */
public final class HitUpdateFlag1 extends UpdateFlag<HitMark> {

	/**
	 * Constructs a new {@code HitUpdateFlag1} {@code Object}.
	 * @param context The hit mark.
	 */
	public HitUpdateFlag1(HitMark context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.putSmart(context.getDamage()).putS(context.getType());
	}

	@Override
	public int data() {
		return maskData();
	}

	@Override
	public int ordinal() {
		return 7;
	}

	/**
	 * Gets the mask data.
	 * @return The mask data.
	 */
	public static int maskData() {
		return 0x200;
	}

}
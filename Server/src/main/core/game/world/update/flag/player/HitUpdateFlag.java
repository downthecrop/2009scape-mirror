package core.game.world.update.flag.player;

import core.game.node.entity.Entity;
import core.game.world.update.flag.UpdateFlag;
import core.game.world.update.flag.context.HitMark;
import core.net.packet.IoBuffer;

/**
 * The main hit update flag.
 * @author Emperor
 *
 */
public final class HitUpdateFlag extends UpdateFlag<HitMark> {

	/**
	 * Constructs a new {@code HitUpdateFlag} {@code Object}.
	 * @param context The hit mark.
	 */
	public HitUpdateFlag(HitMark context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		Entity e = context.getEntity();
		int max = e.getSkills().getMaximumLifepoints();
		int ratio = 255;
		if (max > e.getSkills().getLifepoints()) {
			ratio = e.getSkills().getLifepoints() * 255 / max;
		}
		buffer.putSmart(context.getDamage()).putA(context.getType()).putS(ratio);
	}

	@Override
	public int data() {
		return maskData();
	}

	@Override
	public int ordinal() {
		return 1;
	}

	/**
	 * Gets the mask data.
	 * @return The mask data.
	 */
	public static int maskData() {
		return 0x1;
	}

}
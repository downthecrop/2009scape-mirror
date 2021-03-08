package core.game.world.update.flag.npc;

import core.game.node.entity.Entity;
import core.game.world.update.flag.UpdateFlag;
import core.game.world.update.flag.context.HitMark;
import core.net.packet.IoBuffer;

/**
 * The NPC's main hit update flag.
 * @author Emperor
 *
 */
public final class NPCHitFlag extends UpdateFlag<HitMark> {

	/**
	 * Constructs a new {@code NPCHitFlag} {@code Object}.
	 * @param context The hit mark.
	 */
	public NPCHitFlag(HitMark context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		Entity e = context.getEntity();
		int max = e.getSkills().getMaximumLifepoints();
		int ratio = 0;
		if (max > 0) {
			if (max < e.getSkills().getLifepoints()) {
				ratio = 255;
			} else {
				ratio = e.getSkills().getLifepoints() * 255 / max;
			}
		}
		buffer.put(context.getDamage()).putC(context.getType()).putS(ratio);
	}

	@Override
	public int data() {
		return maskData();
	}

	@Override
	public int ordinal() {
		return 0;
	}

	/**
	 * Gets the mask data.
	 * @return The mask data.
	 */
	public static int maskData() {
		return 0x40;
	}

}
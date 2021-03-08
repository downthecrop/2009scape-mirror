package core.game.world.update.flag.player;

import core.game.node.entity.Entity;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;

/**
 * The face entity update flag.
 * @author Emperor
 *
 */
public final class FaceEntityFlag extends UpdateFlag<Entity> {

	/**
	 * Constructs a new {@code FaceEntityFlag} {@code Object}.
	 * @param context The entity to face.
	 */
	public FaceEntityFlag(Entity context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.putShortA(context == null ? -1 : context.getClientIndex());
	}

	@Override
	public int data() {
		return 0x2;
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
		return 4;
	}

}
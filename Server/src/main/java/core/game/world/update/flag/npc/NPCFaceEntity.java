package core.game.world.update.flag.npc;

import core.game.node.entity.Entity;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;

/**
 * The face entity update flag for NPCs.
 * @author Emperor
 *
 */
public final class NPCFaceEntity extends UpdateFlag<Entity> {

	/**
	 * Constructs a new {@code NPCFaceEntity} {@code Object}.
	 * @param context The context.
	 */
	public NPCFaceEntity(Entity context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.putShortA(context == null ? -1 : context.getClientIndex());
	}

	@Override
	public int data() {
		return 0x4;
	}

	@Override
	public int ordinal() {
		return getOrdinal();
	}

	/**
	 * Gets the mask data.
	 * @return The mask data.
	 */
	public static int getOrdinal() {
		return 3;
	}

}
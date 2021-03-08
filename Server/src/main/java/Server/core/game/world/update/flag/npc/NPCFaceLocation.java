package core.game.world.update.flag.npc;

import core.game.world.map.Location;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;

/**
 * The NPC face coordinates update flag.
 * @author Emperor
 *
 */
public final class NPCFaceLocation extends UpdateFlag<Location> {

	/**
	 * Constructs a new {@code NPCFaceLocation} {@code Object}.
	 * @param context The location to face.
	 */
	public NPCFaceLocation(Location context) {
		super(context);
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.putShortA((context.getX() << 1) + 1).putShort((context.getY() << 1) + 1);
	}

	@Override
	public int data() {
		return 0x200;
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
		return 8;
	}

}
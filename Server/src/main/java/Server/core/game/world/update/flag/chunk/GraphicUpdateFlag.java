package core.game.world.update.flag.chunk;

import core.game.world.map.Location;
import core.game.world.update.flag.UpdateFlag;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.IoBuffer;

/**
 * Handles the location graphic update.
 * @author Emperor
 */
public final class GraphicUpdateFlag extends UpdateFlag<Graphics> {

	/**
	 * The location.
	 */
	private final Location location;

	/**
	 * Constructs a new {@code GraphicUpdateFlag} {@code Object}.
	 * @param graphic The graphic.
	 * @param location The location.
	 */
	public GraphicUpdateFlag(Graphics graphic, Location location) {
		super(graphic);
		this.location = location;
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.put((byte) 17); // opcode
		buffer.put((location.getChunkOffsetX() << 4) | (location.getChunkOffsetY() & 0x7));
		buffer.putShort(context.getId());
		buffer.put(context.getHeight());
		buffer.putShort(context.getDelay());
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public int ordinal() {
		return 3;
	}

}
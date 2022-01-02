package core.game.world.update.flag.chunk;

import core.game.node.scenery.Scenery;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;
import core.net.packet.out.ClearScenery;
import core.net.packet.out.ConstructScenery;

/**
 * The object update flag.
 * @author Emperor
 */
public class ObjectUpdateFlag extends UpdateFlag<Object> {

	/**
	 * The object to update.
	 */
	private final Scenery object;

	/**
	 * If we should remove the object.
	 */
	private final boolean remove;

	/**
	 * Constructs a new {@code ObjectUpdateFlag} {@code Object}.
	 * @param object The object to update.
	 * @param remove If we should remove the object.
	 */
	public ObjectUpdateFlag(Scenery object, boolean remove) {
		super(null);
		this.object = object;
		this.remove = remove;
	}

	@Override
	public void write(IoBuffer buffer) {
		if (remove) {
			ClearScenery.write(buffer, object);
		} else {
			ConstructScenery.write(buffer, object);
		}
	}

	@Override
	public int data() {
		return 0;
	}

	@Override
	public int ordinal() {
		return 0;
	}

}
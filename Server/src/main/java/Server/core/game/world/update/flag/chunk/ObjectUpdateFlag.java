package core.game.world.update.flag.chunk;

import core.game.node.object.GameObject;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;
import core.net.packet.out.ClearObject;
import core.net.packet.out.ConstructObject;

/**
 * The object update flag.
 * @author Emperor
 */
public class ObjectUpdateFlag extends UpdateFlag<Object> {

	/**
	 * The object to update.
	 */
	private final GameObject object;

	/**
	 * If we should remove the object.
	 */
	private final boolean remove;

	/**
	 * Constructs a new {@code ObjectUpdateFlag} {@code Object}.
	 * @param object The object to update.
	 * @param remove If we should remove the object.
	 */
	public ObjectUpdateFlag(GameObject object, boolean remove) {
		super(null);
		this.object = object;
		this.remove = remove;
	}

	@Override
	public void write(IoBuffer buffer) {
		if (remove) {
			ClearObject.write(buffer, object);
		} else {
			ConstructObject.write(buffer, object);
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
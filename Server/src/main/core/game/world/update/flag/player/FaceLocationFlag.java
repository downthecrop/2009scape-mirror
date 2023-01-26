package core.game.world.update.flag.player;

import core.game.node.Node;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.game.world.update.flag.UpdateFlag;
import core.net.packet.IoBuffer;

/**
 * Handles the face coordinates update flag.
 * @author Emperor
 */
public class FaceLocationFlag extends UpdateFlag<Location> {

	/**
	 * Constructs a new {@code FaceLocationFlag} {@code Object}.
	 * @param location The location to face.
	 */
	public FaceLocationFlag(Location location) {
		super(location);
	}

	/**
	 * Gets the face location of the node.
	 * @param n The facing node.
	 * @param node The node to face.
	 * @return The location.
	 */
	public static Location getFaceLocation(Node n, Node node) {
		int x = node.size() >> 1;
		int y = node.size() >> 1;
		if (node instanceof Scenery) {
			Scenery o = (Scenery) node;
			x = o.getDefinition().sizeX >> 1;
			y = o.getDefinition().sizeY >> 1;
			if (o.getRotation() % 2 != 0) {
				x = y;
				y = o.getDefinition().sizeX >> 1;
			}
			if (n.getLocation().equals(o.getLocation()) && node.size() == 1) {
				switch (o.getRotation()) {
				case 0:
					x -= 1;
					break;
				case 1:
					y += 1;
					break;
				case 2:
					x += 1;
					break;
				case 3:
					y -= 1;
					break;
				}
			}
		}
		return node.getLocation().transform(x, y, 0);
	}

	@Override
	public void write(IoBuffer buffer) {
		buffer.putShort((context.getX() << 1) + 1);
		buffer.putLEShortA((context.getY() << 1) + 1);
	}

	@Override
	public int data() {
		return 0x40;
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
		return 10;
	}

}
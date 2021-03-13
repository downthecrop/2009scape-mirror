package core.game.world.map.path;

import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.Point;
import core.game.world.map.RegionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A pathfinder implementation used for checking projectile paths.
 * @author Emperor
 */
public final class ProjectilePathfinder extends Pathfinder {

	/**
	 * If a path can be found.
	 */
	private boolean found;

	/**
	 * The plane.
	 */
	private int z;

	/**
	 * The x-coordinate.
	 */
	private int x;

	/**
	 * The y-coordinate.
	 */
	private int y;

	@Override
	public Path find(Location start, int size, Location end, int sizeX, int sizeY, int rotation, int type, int walkingFlag, boolean near, ClipMaskSupplier clipMaskSupplier) {
		Path path = new Path();
		z = start.getZ();
		x = start.getX();
		y = start.getY();
		List<Point> points = new ArrayList<>(20);
		path.setSuccesful(true);
		while (x != end.getX() || y != end.getY()) {
			Direction[] directions = getDirection(x, y, end);
			found = true;
			checkSingleTraversal(points, directions);
			if (!found) {
				path.setMoveNear(x != start.getX() || y != start.getY());
				path.setSuccesful(false);
				break;
			}
		}
		if (!points.isEmpty()) {
			for (int i = 0; i < points.size() - 1; i++) {
				Point p = points.get(i);
				if (p.getDirection() != null) {
					path.getPoints().add(p);
				}
			}
			path.getPoints().add(points.get(points.size() - 1));
		}
		return path;
	}

	/**
	 * Checks traversal for a size 1 entity.
	 *
	 * @param points     The points list.
	 * @param directions The directions.
	 */
	private void checkSingleTraversal(List<Point> points, Direction... directions) {
		dir:
		for (Direction dir : directions) {
			found = true;
			switch (dir) {
				case NORTH:
					if (flagged(z, x, y + 1, 0x12c0120)) {
						found = false;
						break dir;
					}
					points.add(new Point(x, y + 1, dir));
					y++;
					break;
				case NORTH_EAST:
					if (flagged(z, x + 1, y, 0x12c0180)
							|| flagged(z, x, y + 1, 0x12c0120)
							|| flagged(z, x + 1, y + 1, 0x12c01e0)) {
						found = false;
						break dir;
					}
					points.add(new Point(x + 1, y + 1, dir));
					x++;
					y++;
					break;
				case EAST:
					if (flagged(z, x + 1, y, 0x12c0180)) {
						found = false;
						break dir;
					}
					points.add(new Point(x + 1, y, dir));
					x++;
					break;
				case SOUTH_EAST:
					if (flagged(z, x + 1, y, 0x12c0180)
							|| flagged(z, x, y - 1, 0x12c0102)
							|| flagged(z, x + 1, y - 1, 0x12c0183)) {
						found = false;
						break dir;
					}
					points.add(new Point(x + 1, y - 1, dir));
					x++;
					y--;
					break;
				case SOUTH:
					if (flagged(z, x, y - 1, 0x12c0102)) {
						found = false;
						break dir;
					}
					points.add(new Point(x, y - 1, dir));
					y--;
					break;
				case SOUTH_WEST:
					if (flagged(z, x - 1, y, 0x12c0108)
							|| flagged(z, x, y - 1, 0x12c0102)
							|| flagged(z, x - 1, y - 1, 0x12c010e)) {
						found = false;
						break dir;
					}
					points.add(new Point(x - 1, y - 1, dir));
					x--;
					y--;
					break;
				case WEST:
					if (flagged(z, x - 1, y, 0x12c0108)) {
						found = false;
						break dir;
					}
					points.add(new Point(x - 1, y, dir));
					x--;
					break;
				case NORTH_WEST:
					if (flagged(z, x - 1, y, 0x12c0108)
							|| flagged(z, x, y + 1, 0x12c0120)
							|| flagged(z, x - 1, y + 1, 0x12c0138)) {
						found = false;
						break dir;
					}
					points.add(new Point(x - 1, y + 1, dir));
					x--;
					y++;
					break;
			}
			if (found) {
				break;
			}
		}
	}

	private static boolean flagged(int z, int x, int y, int pFlagMask) {
		int pFlag = RegionManager.getProjectileFlag(z, x, y);
		return (pFlag & pFlagMask) != 0 || (pFlag & 0x20000) != 0 || (RegionManager.getClippingFlag(z, x, y) & 0x20000) != 0;
	}

	/**
	 * Gets the direction.
	 * @param startX The startX.
	 * @param startY The startY.
	 * @param end The end direction.
	 * @return The direction.
	 */
	private static Direction[] getDirection(int startX, int startY, Location end) {
		int endX = end.getX();
		int endY = end.getY();
		if (startX == endX) {
			if (startY > endY) {
				return new Direction[] { Direction.SOUTH };
			} else if (startY < endY) {
				return new Direction[] { Direction.NORTH };
			}
		} else if (startY == endY) {
			if (startX > endX) {
				return new Direction[] { Direction.WEST };
			}
			return new Direction[] { Direction.EAST };
		} else {
			if (startX < endX && startY < endY) {
				return new Direction[] { Direction.NORTH_EAST, Direction.EAST, Direction.NORTH };
			} else if (startX < endX && startY > endY) {
				return new Direction[] { Direction.SOUTH_EAST, Direction.EAST, Direction.SOUTH };
			} else if (startX > endX && startY < endY) {
				return new Direction[] { Direction.NORTH_WEST, Direction.WEST, Direction.NORTH };
			} else if (startX > endX && startY > endY) {
				return new Direction[] { Direction.SOUTH_WEST, Direction.WEST, Direction.SOUTH };
			}
		}
		return new Direction[0];
	}
}
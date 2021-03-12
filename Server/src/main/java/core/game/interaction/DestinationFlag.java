package core.game.interaction;

import core.game.content.global.action.DoorActionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.object.GameObject;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;

/**
 * Holds the destination flags for all types of nodes.
 * @author Emperor
 */
public class DestinationFlag {

	/**
	 * The Location destination flag.
	 */
	public static final DestinationFlag LOCATION = new DestinationFlag();

	/**
	 * The entity destination flag.
	 */
	public static final DestinationFlag ENTITY = new DestinationFlag() {

		@Override
		public Location getDestination(Entity mover, Node n) {
			Location l = getClosestTo(mover, n, n.getLocation().transform(0, -1, 0));
			if (mover.size() > 1) {
				if (l.getX() < n.getLocation().getX()) {
					l = l.transform(-(mover.size() - 1), 0, 0);
				}
				if (l.getY() < n.getLocation().getY()) {
					l = l.transform(0, -(mover.size() - 1), 0);
				}
			}
			return l;
		}
	};

	/**
	 * The following an entity destination flag.
	 */
	public static final DestinationFlag FOLLOW_ENTITY = new DestinationFlag() {

		@Override
		public Location getDestination(Entity mover, Node n) {
			Direction dir = n.getDirection();
			Location l = n.getLocation().transform(-dir.getStepX(), -dir.getStepY(), 0);
			if (!checkTraversal(l, dir)) {
				l = getClosestTo(mover, n, l);
			}
			if (mover.size() > 1) {
				if (l.getX() < n.getLocation().getX()) {
					l = l.transform(-(mover.size() - 1), 0, 0);
				}
				if (l.getY() < n.getLocation().getY()) {
					l = l.transform(0, -(mover.size() - 1), 0);
				}
			}
			return l;
		}
	};

	/**
	 * The entity destination flag.
	 */
	public static final DestinationFlag COMBAT = new DestinationFlag() {

		@Override
		public Location getDestination(Entity mover, Node n) {
			return null;// MovementPulse.getClosest(mover, (Entity) n);
		}
	};

	/**
	 * The item destination flag.
	 */
	public static final DestinationFlag ITEM = new DestinationFlag() {

		@Override
		public Location getDestination(Entity mover, Node n) {
			if (!RegionManager.isTeleportPermitted(n.getLocation())) {
				return getClosestTo(mover, n, n.getLocation().transform(1, 0, 0));
			}
			return n.getLocation();
		}

	};

	/**
	 * The object destination flag.
	 */
	public static final DestinationFlag OBJECT = new DestinationFlag() {

		@Override
		public Location getDestination(Entity mover, Node n) {
			GameObject object = (GameObject) n;
			if (object.getType() < 4 || object.getType() == 9) {
				return DoorActionHandler.getDestination(mover, object);
			}
			if (object.getType() == 4 || object.getType() == 5) { // Wall
				// decoration
				return object.getLocation();
			}
			int sizeX = object.getDefinition().sizeX;
			int sizeY = object.getDefinition().sizeY;
			if (object.getRotation() % 2 != 0) {
				int switcher = sizeX;
				sizeX = sizeY;
				sizeY = switcher;
			}
			Direction dir = Direction.forWalkFlag(object.getDefinition().getWalkingFlag(), object.getRotation());
			if (dir != null) {
				return getDestination(mover, object, sizeX, sizeY, dir, 3);
			}
			// System.out.println(dir);
			return getDestination(mover, object, sizeX, sizeY, Direction.getLogicalDirection(object.getLocation(), mover.getLocation()), 0);
		}

		/**
		 * Gets the destination for the given object.
		 * @param object The object.
		 * @param dir The preferred direction from the object.
		 * @return The teleporting destination.
		 */
		private Location getDestination(Entity mover, GameObject object, int sizeX, int sizeY, Direction dir, int count) {
			Location closest = null;
			double distance = 9999.9;
			Location loc = object.getLocation();
			for (int i = count; i < 4; i++) {
				if (dir.toInteger() % 2 != 0) {
					int x = dir.getStepX();
					if (x > 0) {
						x *= sizeX;
					}
					for (int y = 0; y < sizeY; y++) {
						Location l = loc.transform(x, y, 0);
						if (checkTraversal(l, dir)) {
							double dist = mover.getLocation().getDistance(l);
							if (dist < distance) {
								distance = dist;
								closest = l;
							}
						}
					}
				} else {
					int y = dir.getStepY();
					if (y > 0) {
						y *= sizeY;
					}
					for (int x = 0; x < sizeX; x++) {
						Location l = loc.transform(x, y, 0);
						if (checkTraversal(l, dir)) {
							double dist = mover.getLocation().getDistance(l);
							if (dist < distance) {
								distance = dist;
								closest = l;
							}
						}
					}
				}
				dir = Direction.get((dir.toInteger() + 1) % 4);
			}
			return closest;
		}

		@Override
		public boolean checkTraversal(Location l, Direction dir) {
			return RegionManager.isTeleportPermitted(l) && dir.canMove(l);
		}
	};

	/**
	 * Constructs a new {@code DestinationFlag} {@code Object}.
	 */
	public DestinationFlag() {
		/*
		 * empty.
		 */
	}

	/**
	 * Gets the default destination location.
	 * @param mover The moving entity.
	 * @param node The node to move to.
	 * @return The location to walk to.
	 */
	public Location getDestination(Entity mover, Node node) {
		return node.getLocation();
	}

	/**
	 * Checks if traversal is permitted.
	 * @param l The location to check.
	 * @param dir The direction to move.
	 * @return {@code True}.
	 */
	public boolean checkTraversal(Location l, Direction dir) {
		return Direction.get((dir.toInteger() + 2) % 4).canMove(l);
	}

	/**
	 * Gets the closest destination to the current destination, to reach the
	 * node.
	 * @param mover The moving entity.
	 * @param node The node to move to.
	 * @param suggestion The suggested destination location.
	 * @return The destination location.
	 */
	public Location getClosestTo(Entity mover, Node node, Location suggestion) {
		Location nl = node.getLocation();
		int diffX = suggestion.getX() - nl.getX();
		int diffY = suggestion.getY() - nl.getY();
		Direction moveDir = Direction.NORTH;
		if (diffX < 0) {
			moveDir = Direction.EAST;
		} else if (diffX >= node.size()) {
			moveDir = Direction.WEST;
		} else if (diffY >= node.size()) {
			moveDir = Direction.SOUTH;
		}
		double distance = 9999.9;
		Location destination = suggestion;
		for (int c = 0; c < 4; c++) {
			for (int i = 0; i < node.size() + 1; i++) {
				for (int j = 0; j < (i == 0 ? 1 : 2); j++) {
					Direction current = Direction.get((moveDir.toInteger() + (j == 1 ? 3 : 1)) % 4);
					Location loc = suggestion.transform(current.getStepX() * i, current.getStepY() * i, 0);
					if (moveDir.toInteger() % 2 == 0) {
						if (loc.getX() < nl.getX() || loc.getX() > nl.getX() + node.size() - 1) {
							continue;
						}
					} else {
						if (loc.getY() < nl.getY() || loc.getY() > nl.getY() + node.size() - 1) {
							continue;
						}
					}
					if (checkTraversal(loc, moveDir)) {
						double dist = mover.getLocation().getDistance(loc);
						if (dist < distance) {
							distance = dist;
							destination = loc;
						}
					}
				}
			}
			moveDir = Direction.get((moveDir.toInteger() + 1) % 4);
			int offsetX = Math.abs(moveDir.getStepY() * (node.size() >> 1)); // Not
			// a
			// mixup
			// between
			// x
			// &
			// y!
			int offsetY = Math.abs(moveDir.getStepX() * (node.size() >> 1));
			if (moveDir.toInteger() < 2) {
				suggestion = node.getLocation().transform(-moveDir.getStepX() + offsetX, -moveDir.getStepY() + offsetY, 0);
			} else {
				suggestion = node.getLocation().transform(-moveDir.getStepX() * node.size() + offsetX, -moveDir.getStepY() * node.size() + offsetY, 0);
			}
		}
		return destination;
	}

}
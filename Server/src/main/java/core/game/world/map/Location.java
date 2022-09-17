package core.game.world.map;

import core.game.interaction.DestinationFlag;
import core.game.node.Node;
import core.game.world.map.path.Path;
import core.game.world.map.path.Pathfinder;
import core.tools.RandomFunction;
import org.jetbrains.annotations.NotNull;
import rs09.game.system.SystemLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a location on the world map.
 * @author Emperor
 */
public final class Location extends Node {

	/**
	 * The x-coordinate.
	 */
	private int x;

	/**
	 * The y-coordinate.
	 */
	private int y;

	/**
	 * The plane.
	 */
	private int z;

	/**
	 * Constructs a new {@code Location} {@code Object}.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 */
	public Location(int x, int y, int z) {
		super(null, null);
		super.destinationFlag = DestinationFlag.LOCATION;
		this.x = x;
		this.y = y;
		if (z < 0) {
			z += 4;
		}
		this.z = z;
	}

	/**
	 * Constructs a new {@code Location} {@code Object}
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public Location(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * Constructs a new {@code Location} {@code Object}.
	 * @param x The x-coordinate.
	 * @param y The y coordinate.
	 * @param z The z-coordinate.
	 * @param randomizer The amount we should randomize the x and y coordinates
	 * with (x + random(randomizer), y + random(randomizer)).
	 */
	public Location(int x, int y, int z, int randomizer) {
		this(x + RandomFunction.getRandom(randomizer), y + RandomFunction.getRandom(randomizer), z);
	}

	/**
	 * Construct a new Location.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @return The constructed location.
	 */
	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}

	public static Location create(int x, int y) {
		return new Location(x, y, 0);
	}

	/**
	 * Creates a new instance of the given location.
	 * @param location The given location.
	 * @return The new instance.
	 */
	public static Location create(Location location) {
		return create(location.getX(), location.getY(), location.getZ());
	}

	/**
	 * Creates a location instance with coordinates being the difference between
	 * {@code other} & {@code location}.
	 * @param location The first location.
	 * @param other The other location.
	 * @return The delta location.
	 */
	public static Location getDelta(Location location, Location other) {
		return Location.create(other.x - location.x, other.y - location.y, other.z - location.z);
	}

	/**
	 * Gets a random location near the main location.
	 * @param main The main location.
	 * @param radius The radius.
	 * @param reachable If the locations should be able to reach eachother.
	 * @return The location.
	 */
	public static Location getRandomLocation(Location main, int radius, boolean reachable) {
		Location location = RegionManager.getTeleportLocation(main, radius);
		if (!reachable) {
			return location;
		}
		Path path = Pathfinder.find(main, location, false, Pathfinder.DUMB);
		if (!path.isSuccessful()) {
			location = main;
			if (!path.getPoints().isEmpty()) {
				Point p = path.getPoints().getLast();
				location = Location.create(p.getX(), p.getY(), main.getZ());
			}
		}
		return location;
	}

	@Override
	public Location getLocation() {
		return this;
	}

	/**
	 * Checks if this location is right next to the node (assuming the node is
	 * size 1x1).
	 * @param node The node to check.
	 * @return {@code True} if this location is 1 tile north, west, south or
	 * east of the node location.
	 */
	public boolean isNextTo(Node node) {
		Location l = node.getLocation();
		if (l.getY() == y) {
			return l.getX() - x == -1 || l.getX() - x == 1;
		}
		if (l.getX() == x) {
			return l.getY() - y == -1 || l.getY() - y == 1;
		}
		return false;
	}

	/**
	 * Gets the region id.
	 * @return The region id.
	 */
	public int getRegionId() {
		return (x >> 6) << 8 | (y >> 6);
	}

    /**
     * Compares the users region with the one given
     * @return True if user is in given region
     */
	public boolean isInRegion(int region) {
        return getRegionId() == region;
    }

	/**
	 * Gets the location incremented by the given coordinates.
	 * @param dir The direction to transform this location.
	 * @return The location.
	 */
	public Location transform(Direction dir) {
		return transform(dir, 1);
	}

	/**
	 * Gets the location incremented by the given coordinates.
	 * @param dir The direction to transform this location.
	 * @param steps The amount of steps to move in this direction.
	 * @return The location.
	 */
	public Location transform(Direction dir, int steps) {
		return new Location(x + (dir.getStepX() * steps), y + (dir.getStepY() * steps), this.z);
	}

	/**
	 * Gets the location incremented by the given coordinates.
	 * @param l incremental location
	 * @return The location.
	 */
	public Location transform(Location l) {
		return new Location(x + l.getX(), y + l.getY(), this.z + l.getZ());
	}

	/**
	 * Gets the location incremented by the given coordinates.
	 * @param diffX The x-difference.
	 * @param diffY The y-difference.
	 * @param z The height difference.
	 * @return The location.
	 */
	public Location transform(int diffX, int diffY, int z) {
		return new Location(x + diffX, y + diffY, this.z + z);
	}

	/**
	 * Checks if the other location is within viewing distance.
	 * @param other The other location.
	 * @return If you're within the other distance.
	 */
	public boolean withinDistance(Location other) {
		return withinDistance(other, MapDistance.RENDERING.getDistance());
	}

    /**
     * Returns if a player is within a specified distance.
     * @param other The other location.
     * @param dist The amount of distance.
     * @return If you're within the other distance.
     */
	public boolean withinDistance(Location other, int dist) {
	    if (other.z != z) {
	        return false;
        }

		int a = (other.x - x);
		int b = (other.y - y);
		double product = Math.sqrt((a*a) + (b*b));
		return product <= dist;
	}

    /**
     * Returns if a player is within a specified distance using max norm distance.
     * @param other The other location.
     * @param dist The amount of distance.
     * @return If you're within the other distance.
     */
	public boolean withinMaxnormDistance(Location other, int dist) {
	    if (other.z != z) {
	        return false;
        }

		int a = Math.abs(other.x - x);
		int b = Math.abs(other.y - y);
		double max = Math.max(a, b);
		return max <= dist;
	}

	/**
	 * Returns the distance between you and the other.
	 * @param other The other location.
	 * @return The amount of distance between you and other.
	 */
	public double getDistance(Location other) {
		int xdiff = this.getX() - other.getX();
		int ydiff = this.getY() - other.getY();
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

	/**
	 * Returns the distance between the first and the second specified distance.
	 * @param first The first location.
	 * @param second The other location.
	 * @return The amount of distance between first and other.
	 */
	public static double getDistance(Location first, Location second) {
		int xdiff = first.getX() - second.getX();
		int ydiff = first.getY() - second.getY();
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

	/**
	 * Gets the 8 tiles surrounding this location as an ArrayList<Location>
	 */
	public ArrayList<Location> getSurroundingTiles() {
		ArrayList<Location> locs = new ArrayList<>();

		locs.add(transform(0,1,0)); //N
		locs.add(transform(1,1,0)); //NE
		locs.add(transform(1,0,0)); //E
		locs.add(transform(1,-1,0)); //SE
		locs.add(transform(0,-1,0)); //S
		locs.add(transform(-1,-1,0));//SW
		locs.add(transform(-1,0,0));//W
		locs.add(transform(-1,1,0));//NW

		return locs;
	}

	/**
	 * Gets a square of 3 x 3 tiles as an ArrayList<Location>
	 */
	public ArrayList<Location> get3x3Tiles() {
		ArrayList<Location> locs = new ArrayList<>();
		locs.add(transform(0,0,0)); //Center
		locs.add(transform(0,1,0)); //N
		locs.add(transform(1,1,0)); //NE
		locs.add(transform(1,0,0)); //E
		locs.add(transform(1,-1,0)); //SE
		locs.add(transform(0,-1,0)); //S
		locs.add(transform(-1,-1,0));//SW
		locs.add(transform(-1,0,0));//W
		locs.add(transform(-1,1,0));//NW
		return locs;
	}

	/**
	 * Gets the x position on the region chunk.
	 * @return The x position on the region chunk.
	 */
	public int getChunkOffsetX() {
		int x = getLocalX();
		//return x - ((x / RegionChunk.SIZE) * RegionChunk.SIZE);
        return x & 7;
	}

	/**
	 * Gets the y position on the region chunk.
	 * @return The y position on the region chunk.
	 */
	public int getChunkOffsetY() {
		int y = getLocalY();
		//return y - ((y / RegionChunk.SIZE) * RegionChunk.SIZE);
        return y & 7;
	}

	/**
	 * Gets the base location for the chunk this location is in.
	 * @return The base location.
	 */
	public Location getChunkBase() {
		return create(getRegionX() << 3, getRegionY() << 3, z);
	}

	/**
	 * Gets the region x-coordinate.
	 * @return The region x-coordinate.
	 */
	public int getRegionX() {
		return x >> 3;
	}

	/**
	 * Gets the region y-coordinate.
	 * @return The region y-coordinate.
	 */
	public int getRegionY() {
		return y >> 3;
	}

	/**
	 * Gets the local x-coordinate on the current region in [0, 64).
	 * @return The local x-coordinate.
	 */
	public int getLocalX() {
		return x & 63;
	}

	/**
	 * Gets the local y-coordinate on the current region in [0, 64).
	 * @return The local y-coordinate.
	 */
	public int getLocalY() {
		return y & 63;
	}

	/**
	 * Gets the scene x-coordinate in [48, 55] (note that 104/2 = 52).
	 * @return The local x-coordinate.
	 */
	public int getSceneX() {
		return x - ((getRegionX() - 6) << 3);
	}

	/**
	 * Gets the local y-coordinate in [48, 55] (note that 104/2 = 52).
	 * @return The local y-coordinate.
	 */
	public int getSceneY() {
		return y - ((getRegionY() - 6) << 3);
	}

	/**
	 * Gets the local x-coordinate.
	 * @param loc The location containing the regional coordinates.
	 * @return The local x-coordinate.
	 */
	public int getSceneX(Location loc) {
		return x - ((loc.getRegionX() - 6) << 3);
	}

	/**
	 * Gets the local y-coordinate.
	 * @param loc The location containing the regional coordinates.
	 * @return The local y-coordinate.
	 */
	public int getSceneY(Location loc) {
		return y - ((loc.getRegionY() - 6) << 3);
	}

	/**
	 * Gets the chunk's x-coordinate (0-7).
	 * @return The x in the (8x8) region.
	 */
	public int getChunkX() {
		return getLocalX() >> 3;
	}

	/**
	 * Gets the chunk's y-coordinate (0-7).
	 * @return The y in the (8x8) region.
	 */
	public int getChunkY() {
		return getLocalY() >> 3;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.x == x && loc.y == y && loc.z == z;
	}

	/**
	 * Checks if these coordinates equal this location.
	 * @param x the x.
	 * @param y the y.
	 * @param z the x.
	 * @return {@code True} if so.
	 */
	public boolean equals(int x, int y, int z) {
		return equals(new Location(x, y, z));
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}

	public static Location fromString(String locString) {
		String trimmed = locString.replace("[", "").replace("]", "");
		String[] tokens = trimmed.split(",");
		return Location.create(
				Integer.parseInt(tokens[0].trim()),
				Integer.parseInt(tokens[1].trim()),
				Integer.parseInt(tokens[2].trim())
		);
	}

	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}

	/**
	 * Gets the x.
	 * @return The x.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 * @param x The x to set.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 * @return The y.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 * @param y The y to set.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the z.
	 * @return The z.
	 */
	public int getZ() {
		return z % 4;
	}

	/**
	 * Sets the z.
	 * @param z The z to set.
	 */
	public void setZ(int z) {
		this.z = z;
	}

	@NotNull
	public List<Location> getStepComponents(Direction dir) {
		List<Location> output = new ArrayList<>(2);
		int stepX = dir.getStepX();
		int stepY = dir.getStepY();

		if (stepX != 0) output.add(transform(stepX, 0, 0));
		if (stepY != 0) output.add(transform(0, stepY, 0));
		return output;
	}

	public Direction deriveDirection(Location location) {
		int diffX = location.x - this.x;
		int diffY = location.y - this.y;

		diffX = diffX >= 0 ? Math.min(diffX, 1) : -1;
		diffY = diffY >= 0 ? Math.min(diffY, 1) : -1;

		StringBuilder sb = new StringBuilder();

		if (diffY != 0) {
			if (diffY > 0) {
				sb.append("NORTH");
			} else {
				sb.append("SOUTH");
			}
		}

		if (diffX != 0) {
			if (sb.length() > 0) sb.append("_");
			if (diffX > 0) {
				sb.append("EAST");
			} else {
				sb.append("WEST");
			}
		}

		if (sb.length() == 0) return null;
		return Direction.valueOf(sb.toString());
	}
}

package core.game.world.map

import core.api.utils.Vector
import core.game.interaction.DestinationFlag
import core.game.node.Node
import core.game.world.map.RegionManager.getTeleportLocation
import core.game.world.map.path.Pathfinder
import core.tools.RandomFunction
import kotlin.math.*

/**
 * Specify a position (x,y,z), implements [Node].
 * - Coordinates x and y can be negative for delta values. Values ±16,383.
 * - Coordinate z (level) has a quirk, this class doesn't let you init with a negative z. Values 0 - 3.
 */
/*
Official Terms
    - Zones: 8x8 tiles
    - Map Squares: 64x64 tiles
    - Build Area: 104x104 tiles (13 x 13 chunks centered around player's chunk) - client's loaded area

Some Definitions:
    - Cardinal: Four Base Direction (N E S W)
    - Diagonal: Four Diagonal directions (NE SE SW NW)
    - Compass: All 8 directions (For this game, compass in relation to 8-point Cardinal+Diagonal)
    - Euclidean distance: Straight-line length of two points. Direct calculation. e.g. [2,3] distance=√13(≈3.61)
    - Manhattan distance: Cardinal length of two points. Count of steps along grid lines. e.g. [2,3] distance=5
    - Chebyshev distance: Cardinal+Diagonal length of two points. Count of king moves to make. e.g. [2,3] distance=3
 */
class Location(var hash: LocationHash) : Node(null, null) {
    @JvmOverloads
    constructor(x: Int, y: Int, z: Int = 0) : this(
        LocationHash(x, y, (z % 4 + 4) % 4) // The previous z calc didn't make sense since they play around with negative numbers.
    )
    constructor(location: Location) : this(location.hash)
    constructor(x: Int, y: Int, z: Int, randomizer: Int) :
        this(x + RandomFunction.getRandom(randomizer), y + RandomFunction.getRandom(randomizer), z)
    init { super.destinationFlag = DestinationFlag.LOCATION } // This is to set the type of [Node].

    // Map properties directly to the underlying hash for convenience
    var x: Int
        get() = hash.x
        set(value) { hash = LocationHash(value, y, z) }

    var y: Int
        get() = hash.y
        set(value) { hash = LocationHash(x, value, z) }

    var z: Int
        get() = hash.z
        set(value) { hash = LocationHash(x, y, (value % 4 + 4) % 4) }

    companion object {
        // Alternate static constructors
        @JvmStatic @JvmOverloads fun create(x: Int, y: Int, z: Int = 0): Location = Location(x, y, z)
        @JvmStatic fun create(location: Location): Location = Location(location)
        /** @return new Location parsed from a string format "[x, y, z]". */
        fun fromString(locString: String): Location {
            val coordinates = locString.removeSurrounding("[", "]").split(",").map { it.trim().toInt() }
            return create(coordinates[0], coordinates[1], coordinates[2])
        }

        /** @return Straight-line(Euclidean) distance between two locations. */
        @JvmStatic fun getDistance(first: Location, second: Location): Double {
            val xDiff: Int = first.x - second.x
            val yDiff: Int = first.y - second.y
            return sqrt((xDiff * xDiff + yDiff * yDiff).toDouble())
        }

        /** @return A location representing the delta between two locations. This is not a good function because it doesn't care about z. */
        @JvmStatic fun getDelta(location: Location, other: Location): Location = create(other.x - location.x, other.y - location.y, other.z - location.z)

        /** @return A random location within radius, optionally verifying reachability. */
        fun getRandomLocation(main: Location, radius: Int, reachable: Boolean): Location {
            val loc = getTeleportLocation(main, radius)
            if (!reachable) return loc
            val path = Pathfinder.find(main, loc, false, Pathfinder.DUMB)
            if (!path.isSuccessful && path.points.isNotEmpty()) {
                return create(path.points.last.x, path.points.last.y, main.z)
            } else if (path.isSuccessful) {
                return loc
            } else {
                return main
            }
        }
    }

    // Function overrides of Node class and Java class
    override fun getLocation(): Location = this
    override fun toString(): String = "[$x, $y, $z]"
    override fun hashCode(): Int = hash.hash
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Location -> other.hash == this.hash
            is LocationHash -> other == this.hash
            else -> false
        }
    }
    fun equals(x: Int, y: Int, z: Int): Boolean = hash == LocationHash(x, y, z)

    // Derived variables. Please note the following isn't a 1-1 representation of what is officially used.

    // Region - (Officially: Map Square) 64x64
    /** Unique index "key" (Officially: region_uid) for a 64x64 area. Made of x and y joined bitwise together. Used in xteas.json. */
    val regionId: Int get() = x shr 6 shl 8 or (y shr 6) // Cuts off the end 6 bits of both x y and joins them together. E.g. 10392
    /** The chunk x index for this position (x/8). For sets of 8x8 area. THIS IS NAMED WRONG, THIS IS A CHUNK(8) NOT REGION(64). */
    val regionX: Int get() = x shr 3 // shr 3 is essentially divide by 8
    /** The chunk y index for this position (y/8). For sets of 8x8 area. THIS IS NAMED WRONG, THIS IS A CHUNK(8) NOT REGION(64). */
    val regionY: Int get() = y shr 3 // shr 3 is essentially divide by 8
    // Local - x,y position inside a region
    /** The relative x position inside a region(64x64). */
    val localX: Int get() = x and 63 // and 63 is essentially modulo 64
    /** The relative y position inside a region(64x64). */
    val localY: Int get() = y and 63 // and 63 is essentially modulo 64
    /** @return Check if it is in the same region id. */
    fun isInRegion(region: Int): Boolean = regionId == region

    // Chunk - (Officially: Zones) 8x8
    val chunkBase: Location get() = create(regionX shl 3, regionY shl 3, z)
    /** The chunk x index for this position RELATIVE to a region[localX](x/8). THIS IS NOT GLOBAL CHUNK X. */
    val chunkX: Int get() = localX shr 3 // shr 3 is essentially divide by 8 (note this is localX)
    /** The chunk y index for this position RELATIVE to a region[localY](y/8). THIS IS NOT GLOBAL CHUNK Y. */
    val chunkY: Int get() = localY shr 3 // shr 3 is essentially divide by 8 (note this is localX)
    /** The relative x position inside a chunk(8x8). */
    val chunkOffsetX: Int get() = localX and 7 // and 7 is essentially modulo 8
    /** The relative y position inside a chunk(8x8). */
    val chunkOffsetY: Int get() = localY and 7 // and 7 is essentially modulo 8

    // Scene - (Officially: Build Area) 104x104 - Made of 13x13 chunks(8x8)
    /** The relative x position inside a scene(104x104). Used for client viewport rendering. */
    val sceneX: Int get() = x - (regionX - 6 shl 3) // Centers the current region at [7chunk,7chunk]
    /** The relative y position inside a scene(104x104). Used for client viewport rendering. */
    val sceneY: Int get() = y - (regionY - 6 shl 3) // Centers the current region at [7chunk,7chunk]
    /** @return The local scene x-coordinate relative to another location's regionX. */
    fun getSceneX(loc: Location): Int = x - (loc.regionX - 6 shl 3)
    /** @return The local scene y-coordinate relative to another location's regionY. */
    fun getSceneY(loc: Location): Int = y - (loc.regionY - 6 shl 3)



    /** @return The location incremented by another location's coordinates. (POTENTIAL Z PROBLEMS HERE) */
    // TODO: THE FOLLOWING TRANSFORM HAS SHIT Z HANDLING.
    fun transform(l: Location): Location = Location(x + l.x, y + l.y, (z + l.z) % 4)
    /** @return The location incremented by specific x, y, z differences. */
    fun transform(diffX: Int, diffY: Int, z: Int): Location = Location(x + diffX, y + diffY, (this.z + z) % 4)
    /** @return The location incremented by direction and steps. */
    @JvmOverloads fun transform(dir: Direction, steps: Int = 1): Location = Location(x + dir.stepX * steps, y + dir.stepY * steps, z)
    /** Location transformed by a Vector. Limit usage to more complicated functionality. */
    fun transform(vector: Vector): Location = create(x + floor(vector.x).toInt(), y + floor(vector.y).toInt())


    /** @return True if this location is 1 tile N, W, S, or E of the node. THIS CAN BE REFACTORED OUT. */
    fun isNextTo(node: Node): Boolean {
        val l = node.location
        return if (l.y == y) abs(l.x - x) == 1 else if (l.x == x) abs(l.y - y) == 1 else false
    }

    /** @return True if this other location is within dist of this location. */
    @JvmOverloads fun withinDistance(other: Location, dist: Int = MapDistance.RENDERING.distance): Boolean {
        if (other.z != z) return false
        return getDistance(this, other) <= dist
    }

    /** @return True if within specified distance using max norm (Chebyshev) distance. */
    fun withinMaxnormDistance(other: Location, dist: Int): Boolean = if (other.z != z) false else max(abs(other.x - x), abs(other.y - y)) <= dist

    /** @return The straight-line(Euclidean) distance between this and another location. */
    fun getDistance(other: Location): Double = getDistance(this, other)

    /** @returns ArrayList of the 8 cardinal and diagonal tiles. Order is important and follows [Direction] indexing **/
    val surroundingTiles: ArrayList<Location> get() {
        val locations = ArrayList<Location>()
        locations.add(transform(-1, -1, 0))
        locations.add(transform(0, -1, 0))
        locations.add(transform(1, -1, 0))
        locations.add(transform(1, 0, 0))
        locations.add(transform(1, 1, 0))
        locations.add(transform(0, 1, 0))
        locations.add(transform(-1, 1, 0))
        locations.add(transform(-1, 0, 0))
        return locations
    }

    /** @returns ArrayList of the 4 cardinal direction tiles. Order is important and follows [Direction] indexing **/
    val cardinalTiles: ArrayList<Location> get() {
        val locations = ArrayList<Location>()
        locations.add(transform(-1, 0, 0))
        locations.add(transform(0, -1, 0))
        locations.add(transform(1, 0, 0))
        locations.add(transform(0, 1, 0))
        return locations
    }


    /**
     * Gets the directional components of a movement step.
     * TODO: would prefer this in the other place. This doesn't exactly belong here since this isn't a common function for other instances of Location
     *
     *     we can move diagonally but there's no melee attacking diagonally. its gotta be from N, W, S or E.
     *     if we are moving diagonally, we then need to check if we can attack from one of the valid directions.
     *     in other words we need to check the two tiles that share a corner with our destination and make sure they can be attacked from
     *
     *     picture this: you are at (0,0). im at (1,1). you want to strangle my head off, so you got to check
     *     1. to the west of destination: (1,1) + (-1,0) = (0,1) or north from where you started
     *     2. to the south of the destination: (1,1) + (0,-1) = (1,0) or east from where you started
     *     this function is used only by CombatSwingHandler.kt and assumes that we return the X tiles, west or east, FIRST
     *     and then the Y tiles South and North.
     */
    fun getStepComponents(dir: Direction): List<Location> {
        val output = ArrayList<Location>(2)
        if (dir.stepX != 0) output.add(transform(-dir.stepX, 0, 0))
        if (dir.stepY != 0) output.add(transform(0, -dir.stepY, 0))
        return output
    }

    /** @return the Direction of the another location relative to this one. */
    fun deriveDirection(location: Location): Direction? {
        val dx = (location.x - x).let { if (it >= 0) min(it, 1) else -1 }
        val dy = (location.y - y).let { if (it >= 0) min(it, 1) else -1 }
        val sb = StringBuilder()
        if (dy != 0) sb.append(if (dy > 0) "NORTH" else "SOUTH")
        if (dx != 0) { if (sb.isNotEmpty()) sb.append("_"); sb.append(if (dx > 0) "EAST" else "WEST") }
        return if (sb.isEmpty()) null else Direction.valueOf(sb.toString())
    }

    /**
     * Locations as an Integer rather than a Location object. This is for space efficiency.
     * Inner class, because this is only used here, and to avoid adding another standard until this replaces Location
     *
     * 32 bits for an int (z z  x x x x x  x x x x x  x x x x x  y y y y y  y y y y y  y y y y y)
     * - Rightmost set of 15 bits is for y (can handle negative numbers)
     * - Middle set of 15 bits is for x (can handle negative numbers)
     * - Leftmost set of 2 bits is for z
     *
     * Since x and y occupy 15 bits (0 to 32,767) it becomes => 1 sign + 14 bits (e.g., 16,384) for range of ±16,383.
     */
    @JvmInline
    value class LocationHash(val hash: Int) {
        constructor(x: Int, y: Int, z: Int = 0) : this(
            ((z and 0x3) shl 30) or
                    ((x and 0x7FFF) shl 15) or
                    (y and 0x7FFF)
        )
        val x: Int get() {
            val raw = (hash ushr 15) and 0x7FFF
            // If the 15th bit is set, it's a negative number in 15-bit range
            return if (raw >= 0x4000) raw - 0x8000 else raw
        }

        val y: Int get() {
            val raw = hash and 0x7FFF
            // If the 15th bit is set, it's a negative number
            return if (raw >= 0x4000) raw - 0x8000 else raw
        }
        val z: Int get() = hash ushr 30
    }
}

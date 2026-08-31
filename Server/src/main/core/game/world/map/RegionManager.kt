package core.game.world.map

import core.api.log
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.zone.ZoneBorders
import core.tools.Log
import core.tools.RandomFunction
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * Manages the regions.
 * @author Emperor
 */
object RegionManager {
    /**
     * The region cache mapping.
     */
    private val REGION_CACHE: MutableMap<Int, Region> = HashMap()
    @JvmStatic val CLIPPING_FLAGS = HashMap<Int, Array<Int>>()
    @JvmStatic val PROJECTILE_FLAGS = HashMap<Int, Array<Int>>()

    private val LOCK = ReentrantLock()

    /**
     * Gets the region for the given region id.
     * @param regionId The region id.
     * @return The region.
     */
    @JvmStatic
    fun forId(regionId: Int): Region {
        if (LOCK.tryLock() || LOCK.tryLock(10000, TimeUnit.MILLISECONDS)) {
            var region = REGION_CACHE[regionId]
            if (region == null) {
                region = Region((regionId shr 8) and 0xFF, regionId and 0xFF)
                REGION_CACHE[regionId] = region
            }
            LOCK.unlock()
            return REGION_CACHE[regionId]!!
        }
        log(this::class.java, Log.ERR, "UNABLE TO OBTAIN LOCK WHEN GETTING REGION BY ID. RETURNING BLANK REGION.")
        return Region(0, 0)
    }

    /**
     * Applies a function to all active regions
     */
    @JvmStatic
    fun apply(callback: (r: Region) -> Unit) {
        if (LOCK.tryLock() || LOCK.tryLock(10000,TimeUnit.MILLISECONDS)) {
            for (r in REGION_CACHE.values) {
                if (r.isActive) {
                    callback(r)
                }
            }
            LOCK.unlock()
        }
    }

    /**
     * Gets the clipping flag.
     * @param z The plane.
     * @param x The absolute x-coordinate.
     * @param y The absolute y-coordinate.
     * @return The clipping flags.
     */
    @JvmStatic
    fun getClippingFlag(z: Int, x: Int, y: Int): Int {
        return getClippingFlag(Location(x, y, z))
    }

    /**
     * Gets the clipping flag.
     * @param loc The location.
     * @param projectile Clipping flags or projectile flags.
     */
    @JvmStatic
    fun getClippingFlag(loc: Location, projectile: Boolean = false) : Int {
        val region = forId(loc.regionId)
        Region.load(region)
        if (!region.isHasFlags) {
            return -1
        }
        // Index by absChunkX and absChunkY coordinates; these give the position of the chunk on the world map. Together with chunkOffsetX and chunkOffsetY, they identify individual tiles.
        val (key, index) = getFlagIndex(loc.absChunkX, loc.absChunkY, loc.z, loc.chunkOffsetX, loc.chunkOffsetY)
        return getFlags(key, projectile)[index]
    }

    private fun getFlagIndex(absChunkX: Int, absChunkY: Int, z: Int, chunkOffsetX: Int, chunkOffsetY: Int) : Pair<Int, Int> {
        return Pair((z shl 22) or (absChunkX shl 11) or absChunkY, (chunkOffsetX * 8) + chunkOffsetY)
    }

    @JvmStatic
    fun getFlags(chunkId: Int, projectile: Boolean) : Array<Int> {
        return if (projectile)
            PROJECTILE_FLAGS.getOrPut(chunkId) { Array(64){0} }
        else
            CLIPPING_FLAGS.getOrPut(chunkId) { Array(64){-1} }
    }

    /**
     * Gets the water variant of a tile's clipping flag
     * Essentially strips the landscape flag off a tile and keeps other flags, and makes normally walkable tiles unwalkable.
     * @author Ceikry
     */
    @JvmStatic
    fun getWaterClipFlag(z: Int, x: Int, y: Int): Int {
        return getWaterClipFlag(Location(x, y, z))
    }

    /**
     * Gets the water variant of a tile's clipping flag
     * Essentially strips the landscape flag off a tile and keeps other flags, and makes normally walkable tiles unwalkable.
     */
    @JvmStatic
    fun getWaterClipFlag(loc: Location): Int {
        val flag = getClippingFlag(loc)
        return if (!isClipped(loc)) {
            flag or 0x100
        } else flag and 0x200000.inv()
    }

    /**
     * Checks if the tile is part of the landscape.
     * @param loc The location.
     * @return `True` if so.
     */
    @JvmStatic
    fun isLandscape(loc: Location): Boolean {
        val region = forId(loc.regionId)
        Region.load(region)
        if (!region.isHasFlags) {
            return false
        }
        if (loc.chunk.flags.landscape == null) {
            return false
        }
        return loc.chunk.flags.landscape[loc.chunkOffsetX][loc.chunkOffsetY]
    }

    /**
     * Adds a clipping flag.
     * @param z The plane.
     * @param x The absolute x-coordinate.
     * @param y The absolute y-coordinate.
     * @param projectile If the flag is being set for projectile pathfinding.
     * @param flag The clipping flag.
     */
    @JvmStatic
    fun addClippingFlag(z: Int, x: Int, y: Int, projectile: Boolean, flag: Int) {
        return addClippingFlag(Location(x, y, z), projectile, flag)
    }

    /**
     * Adds a clipping flag.
     * @param loc The location.
     * @param projectile If the flag is being set for projectile pathfinding.
     * @param flag The clipping flag.
     */
    @JvmStatic
    fun addClippingFlag(loc: Location, projectile: Boolean, flag: Int) {
        val region = forId(loc.regionId)
        Region.load(region)
        if (!region.isHasFlags) {
            return
        }
        if (projectile) {
            loc.chunk.projectileFlags.flag(loc.chunkOffsetX, loc.chunkOffsetY, flag)
        } else {
            loc.chunk.flags.flag(loc.chunkOffsetX, loc.chunkOffsetY, flag)
        }
    }

    /**
     * Removes a clipping flag.
     * @param z The plane.
     * @param x The absolute x-coordinate.
     * @param y The absolute y-coordinate.
     * @param projectile If the flag is being set for projectile pathfinding.
     * @param flag The clipping flag.
     */
    @JvmStatic
    fun removeClippingFlag(z: Int, x: Int, y: Int, projectile: Boolean, flag: Int) {
        return removeClippingFlag(Location(x, y, z), projectile, flag)
    }

    /**
     * Removes a clipping flag.
     * @param loc The location.
     * @param projectile If the flag is being set for projectile pathfinding.
     * @param flag The clipping flag.
     */
    @JvmStatic
    fun removeClippingFlag(loc: Location, projectile: Boolean, flag: Int) {
        val region = forId(loc.regionId)
        Region.load(region)
        if (!region.isHasFlags) {
            return
        }
        if (projectile) {
            loc.chunk.projectileFlags.unflag(loc.chunkOffsetX, loc.chunkOffsetY, flag)
        } else {
            loc.chunk.flags.unflag(loc.chunkOffsetX, loc.chunkOffsetY, flag)
        }
    }

    /**
     * Gets the projectile flag.
     * @param z The plane.
     * @param x The absolute x-coordinate.
     * @param y The absolute y-coordinate.
     * @return The clipping flags.
     */
    @JvmStatic
    fun getProjectileFlag(z: Int, x: Int, y: Int): Int {
        return getClippingFlag(Location(x, y, z), true)
    }

    /**
     * Checks if teleport is permitted.
     * @param loc The Location.
     * @return If teleport is permitted.
     */
    @JvmStatic
    fun isTeleportPermitted(loc: Location): Boolean {
        if (!isLandscape(loc)) {
            return false
        }
        val flag = getClippingFlag(loc)
        return flag and 0x12c0102 == 0 || flag and 0x12c0108 == 0 || flag and 0x12c0120 == 0 || flag and 0x12c0180 == 0
    }

    /**
     * Checks if the location has any clipping flags.
     * @param loc The location.
     * @return `True` if a clipping flag disables access for this location.
     */
    @JvmStatic
    fun isClipped(loc: Location): Boolean {
        if (!isLandscape(loc)) {
            return true
        }
        val flag = getClippingFlag(loc)
        return flag and 0x12c0102 != 0 || flag and 0x12c0108 != 0 || flag and 0x12c0120 != 0 || flag and 0x12c0180 != 0
    }

    /**
     * Gets the spawn location of a node.
     * @param owner the owner.
     * @param node the node.
     * @return the location.
     */
    @JvmStatic
    fun getSpawnLocation(owner: Player?, node: Node?): Location? {
        if (owner == null || node == null) {
            return null
        }
        outer@ for (i in 0..7) {
            val dir = Direction.get(i)
            var stepX = dir.stepX
            var stepY = dir.stepY
            // For objects that are larger than 1, the below corrects for the fact that their origin is on the SW tile
            if (dir.stepX < 0) {
                stepX -= (node.size() - 1)
            }
            if (dir.stepY < 0) {
                stepY -= (node.size() - 1)
            }
            if (owner.size() > 1) { //e.g. if you used ::pnpc to morph yourself into a large NPC
                if (dir.stepX > 0) {
                    stepX += (owner.size() - 1)
                }
                if (dir.stepY > 0) {
                    stepY += (owner.size() - 1)
                }
            }
            val l = owner.location.transform(stepX, stepY, 0)
            // Check if ALL target tiles are unclipped
            for (x in 0 until node.size()) {
                for (y in 0 until node.size()) {
                    if (isClipped(l.transform(x, y, 0))) {
                        continue@outer
                    }
                }
            }
            return l
        }
        return null
    }

    /**
     * Gets the scenery on the current location.
     * @param l The location.
     * @return The scenery, or `null` if no object was found.
     */
    @JvmStatic
    fun getObject(l: Location): Scenery? {
        return getObject(l.x, l.y, l.z)
    }

    /**
     * Gets the scenery on the current absolute coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The height.
     * @return The scenery, or `null` if no object was found.
     */
    @JvmStatic
    fun getObject(x: Int, y: Int, z: Int): Scenery? {
        return getObject(x, y, z, -1, -1)
    }

    /**
     * Gets the object on the given absolute coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The height.
     * @param objectId The object id. May be -1, which means 'any'.
     * @param type The scenery type. May be -1, which means 'any'.
     * @return The scenery, or `null` if no object was found.
     */
    @JvmStatic
    fun getObject(x: Int, y: Int, z: Int, objectId: Int = -1, type: Int = -1): Scenery? {
        val loc = Location(x, y, z)
        val region = forId(loc.regionId)
        Region.load(region)
        val chunk = region.chunks[loc.chunkX][loc.chunkY][loc.z]
        val index = chunk.getIndex(loc.chunkOffsetX, loc.chunkOffsetY, objectId, type)
        if (index == -1) {
            return null
        }
        val obj = chunk.objects[loc.chunkOffsetX][loc.chunkOffsetY][index]
        if (obj != null && !obj.isRenderable) {
            return null
        }
        return obj
    }

    /**
     * Gets a region chunk.
     * @param l The location.
     * @return The region chunk.
     */
    @JvmStatic
    fun getRegionChunk(l: Location): RegionChunk {
        val regionId = ((l.x shr 6) shl 8) or (l.y shr 6)
        return forId(regionId).chunks[l.chunkX][l.chunkY][l.z]
    }

    /**
     * Moves the entity from the current chunk to the new one.
     * @param entity The entity.
     */
    @JvmStatic
    fun move(entity: Entity, src: Location?, dst: Location) {
        if (src?.chunk != dst.chunk) {
            when (entity) {
                is Player -> {
                    val player = entity.asPlayer()
                    val sameRegion = src?.region == dst.region
                    if (src != null && sameRegion) {
                        src.chunk.removePlayer(player)
                        dst.chunk.addPlayer(player)
                    } else {
                        if (src != null) {
                            src.chunk.removePlayer(player)
                            src.region.tolerances.remove(player.name)
                            src.region.decrementViewAmount()
                        }
                        dst.chunk.addPlayer(entity)
                        dst.region.tolerances[entity.asPlayer().name] = System.currentTimeMillis()
                        dst.region.incrementViewAmount()
                    }
                }
                is NPC -> {
                    src?.chunk?.remove(entity)
                    dst.chunk.add(entity)
                }
                else -> log(this::class.java, Log.ERR, "Tried to move an Entity that was neither Player nor NPC, but $entity of class ${entity.javaClass}! It tried to move from $src to $dst.")
            }
        }
        entity.zoneMonitor.updateLocation(entity.walkingQueue.footPrint)
    }

    /**
     * Gets local entities. You never call the below function directly (it's inlined); instead, you use one of the typed helpers defined directly below this function.
     * @param location the location.
     * @param distance the distance.
     * @return the list.
     */
    private inline fun<reified T: Entity> getLocalEntitiesOfType(location: Location, distance: Int): List<T> {
        val entities: ArrayList<T> = ArrayList(20)
        val radius = (distance / 8) + 1
        for (cx in (location.absChunkX - radius)..(location.absChunkX + radius)) {
            for (cy in (location.absChunkY - radius)..(location.absChunkY + radius)) {
                val chunk = Location(cx shl 3, cy shl 3, location.z).chunk
                if (T::class.java.isAssignableFrom(Player::class.java)) {
                    entities.addAll(chunk.players.filterIsInstance<T>())
                }
                if (T::class.java.isAssignableFrom(NPC::class.java)) {
                    entities.addAll(chunk.npcs.filterIsInstance<T>())
                }
            }
        }
        val b = ZoneBorders(location.x - distance, location.y - distance, location.x + distance, location.y + distance)
        return entities.filter { b.insideBorder(it.location.x, it.location.y) }
    }
    @JvmStatic fun getLocalEntities(location: Location, distance: Int) = getLocalEntitiesOfType<Entity>(location, distance)
    @JvmStatic fun getLocalPlayers(location: Location, distance: Int) = getLocalEntitiesOfType<Player>(location, distance)
    @JvmStatic fun getLocalNPCs(location: Location, distance: Int) = getLocalEntitiesOfType<NPC>(location, distance)
    @JvmStatic fun getLocalPlayers(location: Location): List<Player> = getLocalPlayers(location, MapDistance.RENDERING.distance)
    @JvmStatic fun getLocalNPCs(location: Location): List<NPC> = getLocalNPCs(location, MapDistance.RENDERING.distance)

    /**
     * Gets a random teleport location in the radius around the given location.
     * @param location The centre location.
     * @param radius The radius.
     * @return A random teleport location.
     */
    @JvmStatic
    fun getTeleportLocation(location: Location, radius: Int): Location {
        var radius = radius
        var mod = radius shr 1
        if (mod == 0) {
            mod++
            radius--
        }
        return getTeleportLocation(location.transform(-mod, -mod, 0), mod + radius, mod + radius)
    }

    /**
     * Gets a random teleport location in the radius around the given location.
     * @param location The centre location.
     * @return A random teleport location.
     */
    @JvmStatic
    fun getTeleportLocation(location: Location, areaX: Int, areaY: Int): Location {
        var destination = location
        var x: Int = RandomFunction.random(1 + areaX)
        var y: Int = RandomFunction.random(1 + areaY)
        var count = 0
        while (!isTeleportPermitted(location.transform(x, y, 0).also { destination = it })) {
            x = RandomFunction.random(1 + areaX)
            y = RandomFunction.random(1 + areaY)
            if (count++ >= areaX * 2) {
                //This would be able to keep looping for several seconds otherwise (this actually happens).
                x = 0
                while (x < areaX + 1) {
                    y = 0
                    while (y < areaY + 1) {
                        if (isTeleportPermitted(location.transform(x, y, 0).also { destination = it })) {
                            return destination
                        }
                        y++
                    }
                    x++
                }
                break
            }
        }
        return destination
    }

    /**
     * Gets an npc near a location.
     * @param id the id,
     * @param distance the distance.
     * @return the npc.
     */
    @JvmStatic
    fun getNpc(location: Location, id: Int, distance: Int): NPC? {
        val npcs: List<NPC> = getLocalNPCs(location, distance)
        for (n in npcs) {
            if (n.id == id) {
                return n
            }
        }
        return null
    }

    @JvmStatic
    fun addRegion(id: Int, region: Region){
        if(lock.tryLock() || LOCK.tryLock(10000, TimeUnit.MILLISECONDS)) {
            REGION_CACHE[id] = region
            LOCK.unlock()
        }
    }

    @JvmStatic
    fun removeRegion(id: Int) {
        if (lock.tryLock() || LOCK.tryLock(10000, TimeUnit.MILLISECONDS)) {
            val r = REGION_CACHE.remove(id)
            r?.flagInactive(true)
            LOCK.unlock()
        }
    }

    /**
     * Gets the regionCache.
     * @return The regionCache.
     */
    val regionCache: Map<Int, Region>
       @JvmStatic get(){
            return REGION_CACHE
       }

    val lock: ReentrantLock
        @JvmStatic get() = LOCK
}

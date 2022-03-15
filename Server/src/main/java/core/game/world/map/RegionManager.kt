package core.game.world.map

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import rs09.game.system.SystemLogger
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

    public val LOCK = ReentrantLock()

    /**
     * Gets the region for the given region id.
     * @param regionId The region id.
     * @return The region.
     */
    @JvmStatic
    fun forId(regionId: Int): Region {
        if(LOCK.tryLock() || LOCK.tryLock(10000, TimeUnit.MILLISECONDS)) {
            var region = REGION_CACHE[regionId]
            if (region == null) {
                region = Region((regionId shr 8) and 0xFF, regionId and 0xFF)
                REGION_CACHE[regionId] = region
            }
            LOCK.unlock()
            return REGION_CACHE[regionId]!!
        }
        SystemLogger.logErr("UNABLE TO OBTAIN LOCK WHEN GETTING REGION BY ID. RETURNING BLANK REGION.")
        return Region(0,0)
    }

    /**
     * Pulses the active regions.
     */
    @JvmStatic
    fun pulse() {
        if(LOCK.tryLock() || LOCK.tryLock(10000,TimeUnit.MILLISECONDS)) {
            for (r in REGION_CACHE.values) {
                if (r.isActive) {
                    for (p in r.planes) {
                        p.pulse()
                    }
                }
            }
            LOCK.unlock()
        }
    }

    /**
     * Gets the clipping flag on the given location.
     * @param l The location.
     * @return The clipping flag.
     */
    @JvmStatic
    fun getClippingFlag(l: Location): Int {
        return getClippingFlag(l.z, l.x, l.y)
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
        var x = x
        var y = y
        val region = forId(((x shr 6) shl 8) or (y shr 6))
        Region.load(region)
        if (!region.isHasFlags) {
            return -1
        }
        x -= (x shr 6) shl 6
        y -= (y shr 6) shl 6
        return region.planes[z].flags.clippingFlags[x][y]
    }

    /**
     * Gets the water variant of a tile's clipping flag
     * Essentially strips the landscape flag off a tile and keeps other flags, and makes normally walkable tiles unwalkable.
     * @author Ceikry
     */
    @JvmStatic
    fun getWaterClipFlag(z: Int, x: Int, y: Int): Int {
        val flag = getClippingFlag(z, x, y)
        return if (!isClipped(z, x, y)) {
            flag or 0x100
        } else flag and 0x200000.inv()
    }

    /**
     * Checks if the tile is part of the landscape.
     * @param l The location.
     * @return `True` if so.
     */
    @JvmStatic
    fun isLandscape(l: Location): Boolean {
        return isLandscape(l.z, l.x, l.y)
    }

    /**
     * Checks if the tile is part of the landscape.
     * @param z The plane.
     * @param x The absolute x-coordinate.
     * @param y The absolute y-coordinate.
     * @return `True` if so.
     */
    @JvmStatic
    fun isLandscape(z: Int, x: Int, y: Int): Boolean {
        var x = x
        var y = y
        val region = forId(((x shr 6) shl 8) or (y shr 6))
        Region.load(region)
        if (!region.isHasFlags || region.planes[z].flags.landscape == null) {
            return false
        }
        x -= x shr 6 shl 6
        y -= y shr 6 shl 6
        return region.planes[z].flags.landscape[x][y]
    }

    /**
     * Sets the clipping flag on the given location.
     * @param l The location.
     * @param flag The flag to set.
     */
    @JvmStatic
    fun setClippingFlag(l: Location, flag: Int) {
        var x = l.x
        var y = l.y
        val z = l.z
        val region = forId(((x shr 6) shl 8) or (y shr 6))
        Region.load(region)
        if (!region.isHasFlags) {
            return
        }
        x -= x shr 6 shl 6
        y -= y shr 6 shl 6
        region.planes[z].flags.clippingFlags[x][y] = flag
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
        var x = x
        var y = y
        val region = forId(((x shr 6) shl 8) or (y shr 6))
        Region.load(region)
        if (!region.isHasFlags) {
            return
        }
        x -= (x shr 6) shl 6
        y -= (y shr 6) shl 6
        if (projectile) {
            region.planes[z].projectileFlags.flag(x, y, flag)
        } else {
            region.planes[z].flags.flag(x, y, flag)
        }
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
    fun removeClippingFlag(z: Int, x: Int, y: Int, projectile: Boolean, flag: Int) {
        var x = x
        var y = y
        val region = forId(((x shr 6) shl 8) or (y shr 6))
        Region.load(region)
        if (!region.isHasFlags) {
            return
        }
        x -= (x shr 6) shl 6
        y -= (y shr 6) shl 6
        if (projectile) {
            region.planes[z].projectileFlags.unflag(x, y, flag)
        } else {
            region.planes[z].flags.unflag(x, y, flag)
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
    fun getProjectileFlag(z: Int, x: Int, y: Int): Int {
        var x = x
        var y = y
        val region = forId(((x shr 6) shl 8) or (y shr 6))
        Region.load(region)
        if (!region.isHasFlags) {
            return -1
        }
        x -= (x shr 6) shl 6
        y -= (y shr 6) shl 6
        return region.planes[z].projectileFlags.clippingFlags[x][y]
    }

    /**
     * Gets the clipping flag
     * @param location the Location
     * @return the clipping flag
     */
    @JvmStatic
    fun isTeleportPermitted(location: Location): Boolean {
        return isTeleportPermitted(location.z, location.x, location.y)
    }

    /**
     * Gets the clipping flag.
     * @param z The plane.
     * @param x The absolute x-coordinate.
     * @param y The absolute y-coordinate.
     * @return The clipping flags.
     */
    @JvmStatic
    fun isTeleportPermitted(z: Int, x: Int, y: Int): Boolean {
        if (!isLandscape(z, x, y)) {
            return false
        }
        val flag = getClippingFlag(z, x, y)
        return flag and 0x12c0102 == 0 || flag and 0x12c0108 == 0 || flag and 0x12c0120 == 0 || flag and 0x12c0180 == 0
    }

    /**
     * Checks if the location has any clipping flags.
     * @param location The location.
     * @return `True` if a clipping flag disables access for this location.
     */
    @JvmStatic
    fun isClipped(location: Location): Boolean {
        return isClipped(location.z, location.x, location.y)
    }

    /**
     * Checks if the location has any clipping flags.
     * @param z The plane.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return `True` if a clipping flag disables access for this location.
     */
    @JvmStatic
    fun isClipped(z: Int, x: Int, y: Int): Boolean {
        if (!isLandscape(z, x, y)) {
            return true
        }
        val flag = getClippingFlag(z, x, y)
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
        var destination: Location? = null
        outer@ for (i in 0..7) {
            val dir = Direction.get(i)
            inner@for(j in 0 until node.size()) {
                val l = owner.location.transform(dir, j)
                for (x in 0 until node.size()) {
                    for (y in 0 until node.size()) {
                        if (isClipped(l.transform(x, y, 0))) {
                            continue@inner
                        }
                    }
                }
                destination = l
                break@outer
            }
        }
        return destination
    }

    /**
     * Gets the scenery on the current location.
     * @param l The location.
     * @return The scenery, or `null` if no object was found.
     */
    @JvmStatic
    fun getObject(l: Location): Scenery? {
        return getObject(l.z, l.x, l.y)
    }

    /**
     * Gets the scenery on the current absolute coordinates.
     * @param z The height.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The scenery, or `null` if no object was found.
     */
    @JvmStatic
    fun getObject(z: Int, x: Int, y: Int): Scenery? {
        return getObject(z, x, y, -1)
    }

    /**
     * Gets the object on the given absolute coordinates.
     * @param z The height.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param objectId The object id.
     * @return The scenery, or `null` if no object was found.
     */
    @JvmStatic
    fun getObject(z: Int, x: Int, y: Int, objectId: Int): Scenery? {
        var x = x
        var y = y
        val regionId = ((x shr 6) shl 8) or (y shr 6)
        x -= (x shr 6) shl 6
        y -= (y shr 6) shl 6
        val region = forId(regionId)
        Region.load(region)
        val `object`: Scenery? = region.planes[z].getChunkObject(x, y, objectId)
        return if (`object` != null && !`object`.isRenderable) {
            null
        } else `object`
    }

    /**
     * Gets the region plane for this location.
     * @param l The location.
     * @return The region plane.
     */
    @JvmStatic
    fun getRegionPlane(l: Location): RegionPlane {
        val regionId = ((l.x shr 6) shl 8) or (l.y shr 6)
        return forId(regionId).planes[l.z]
    }

    /**
     * Gets a region chunk.
     * @param l The location.
     * @return The region chunk.
     */
    @JvmStatic
    fun getRegionChunk(l: Location): RegionChunk {
        val plane = getRegionPlane(l)
        return plane.getRegionChunk(l.localX / RegionChunk.SIZE, l.localY / RegionChunk.SIZE)
    }

    /**
     * Moves the entity from the current region to the new one.
     * @param entity The entity.
     */
    @JvmStatic
    fun move(entity: Entity) {
        val player = entity is Player
        val regionId = ((entity.location.regionX shr 3) shl 8) or (entity.location.regionY shr 3)
        val viewport = entity.viewport
        val current = forId(regionId)
        val z = entity.location.z
        val plane = current.planes[z]
        viewport.updateViewport(entity)
        if (plane == viewport.currentPlane) {
            entity.zoneMonitor.updateLocation(entity.walkingQueue.footPrint)
            return
        }
        viewport.remove(entity)
        if (player) {
            current.add(entity as Player)
        } else {
            current.add(entity as NPC)
        }
        viewport.region = current
        viewport.currentPlane = plane
        val view: MutableList<RegionPlane> = LinkedList()
        for (regionX in ((entity.location.regionX shr 3) - 1)..((entity.location.regionX shr 3) + 1)) {
            for (regionY in ((entity.location.regionY shr 3) - 1)..((entity.location.regionY shr 3) + 1)) {
                if (regionX < 0 || regionY < 0) {
                    continue
                }
                val region = forId((regionX shl 8) or regionY)
                val p = region.planes[z]
                if (player) {
                    region.incrementViewAmount()
                    region.flagActive()
                }
                view.add(p)
            }
        }
        viewport.viewingPlanes = view
        entity.zoneMonitor.updateLocation(entity.walkingQueue.footPrint)
    }

    /**
     * Gets the list of local NPCs with a maximum distance of 16.
     * @param n The entity.
     * @return The list of local NPCs.
     */
    @JvmStatic
    fun getLocalNpcs(n: Entity): List<NPC> {
        return getLocalNpcs(n, MapDistance.RENDERING.distance)
    }

    /**
     * Gets the location entitys.
     * @param location the location.
     * @param distance the distance.
     * @return the list.
     */
    @JvmStatic
    fun getLocalEntitys(location: Location, distance: Int): List<Entity> {
        val entitys: MutableList<Entity> = ArrayList(20)
        entitys.addAll(getLocalNpcs(location, distance))
        entitys.addAll(getLocalPlayers(location, distance))
        return entitys
    }

    /**
     * Gets the location entitys.
     * @param entity the entity.
     * @param distance the distance.
     * @return the list.
     */
    @JvmStatic
    fun getLocalEntitys(entity: Entity, distance: Int): List<Entity> {
        return getLocalEntitys(entity.location, distance)
    }

    /**
     * Gets the local entitys.
     * @param entity the entity.
     * @return the entitys.
     */
    @JvmStatic
    fun getLocalEntitys(entity: Entity): List<Entity> {
        return getLocalEntitys(entity.location, MapDistance.RENDERING.distance)
    }

    /**
     * Gets the list of local NPCs.
     * @param n The entity.
     * @param distance The distance to the entity.
     * @return The list of local NPCs.
     */
    @JvmStatic
    fun getLocalNpcs(n: Entity, distance: Int): List<NPC> {
        val npcs: MutableList<NPC> = LinkedList()
        for (r in n.viewport.viewingPlanes) {
            for (npc in r.npcs) {
                if (npc.location.withinDistance(n.location, distance)) {
                    npcs.add(npc)
                }
            }
        }
        return npcs
    }

    /**
     * Gets the list of local players with a maximum distance of 15.
     * @param n The entity.
     * @return The list of local players.
     */
    @JvmStatic
    fun getLocalPlayers(n: Entity): List<Player> {
        return getLocalPlayers(n, MapDistance.RENDERING.distance)
    }

    /**
     * Gets the list of local players.
     * @param n The entity.
     * @param distance The distance to the entity.
     * @return The list of local players.
     */
    @JvmStatic
    fun getLocalPlayers(n: Entity, distance: Int): List<Player> {
        val players: MutableList<Player> = LinkedList()
        for (r in n.viewport.viewingPlanes) {
            for (p in r.players) {
                if (p.location.withinDistance(n.location, distance)) {
                    players.add(p)
                }
            }
        }
        return players
    }

    /**
     * Gets the surrounding players.
     * @param n The node the players should be surrounding.
     * @param ignore The nodes not to add to the list.
     * @return The list of players.
     */
    @JvmStatic
    fun getSurroundingPlayers(n: Node, vararg ignore: Node): List<Player> {
        return getSurroundingPlayers(n, 9, *ignore)
    }

    /**
     * Gets the surrounding players.
     * @param n The node the players should be surrounding.
     * @param ignore The nodes not to add to the list.
     * @return The list of players.
     */
    @JvmStatic
    fun getSurroundingPlayers(n: Node, maximum: Int, vararg ignore: Node): List<Player> {
        val players = getLocalPlayers(n.location, 2)
        var count = 0
        val it = players.iterator()
        while (it.hasNext()) {
            val p = it.next()
            if(p.isInvisible()) {
                it.remove()
            }
            if(!p.location.withinMaxnormDistance(n.location, 1)) {
                it.remove()
                continue
            }
            if (++count >= maximum) {
                it.remove()
                continue
            }
            for (node in ignore) {
                if (p === node) {
                    count--
                    it.remove()
                    break
                }
            }
        }
        return players
    }

    /**
     * Gets the surrounding players.
     * @param n The node the players should be surrounding.
     * @param ignore The nodes not to add to the list.
     * @return The list of players.
     */
    @JvmStatic
    fun getSurroundingNPCs(n: Node, vararg ignore: Node): List<NPC> {
        return getSurroundingNPCs(n, 9, *ignore)
    }

    /**
     * Gets the surrounding players.
     * @param n The node the npcs should be surrounding.
     * @param ignore The nodes not to add to the list.
     * @return The list of npcs.
     */
    @JvmStatic
    fun getSurroundingNPCs(n: Node, maximum: Int, vararg ignore: Node): List<NPC> {
        val npcs = getLocalNpcs(n.location, 2)
        var count = 0
        val it = npcs.iterator()
        while (it.hasNext()) {
            val p = it.next()
            if(p.isInvisible()) {
                it.remove()
            }
            if(!p.location.withinMaxnormDistance(n.location, 1)) {
                it.remove()
                continue
            }
            if (++count > maximum) {
                it.remove()
                continue
            }
            for (node in ignore) {
                if (p === node) {
                    count--
                    it.remove()
                    break
                }
            }
        }
        return npcs
    }

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
     * Gets the current viewport for the location.
     * @param l The location.
     * @return The viewport.
     */
    @JvmStatic
    fun getViewportPlayers(l: Location): List<Player> {
        var l = l
        val players: MutableList<Player> = LinkedList()
        l = l.chunkBase.transform(-8, -8, 0)
        val b = ZoneBorders(l.x, l.y, l.x + 24, l.y + 24)
        for (regionX in ((l.regionX - 6) shr 3)..((l.regionX + 6) shr 3)) {
            for (regionY in ((l.regionY - 6) shr 3)..((l.regionY + 6) shr 3)) {
                for (player in forId(regionX shl 8 or regionY).planes[l.z].players) {
                    l = player.location
                    if (b.insideBorder(l.x, l.y)) {
                        players.add(player)
                    }
                }
            }
        }
        return players
    }

    /**
     * Gets a list of players in the given region.
     * @param regionId The region id.
     * @return The list of players in this region.
     */
    @JvmStatic
    fun getRegionPlayers(regionId: Int): List<Player> {
        val r = forId(regionId)
        val players: MutableList<Player> = ArrayList(20)
        for (plane in r.planes) {
            players.addAll(plane.players)
        }
        return players
    }

    /**
     * Gets a list of local players within rendering distance of the location.
     * @param l The location.
     * @return The list of players.
     */
    @JvmStatic
    fun getLocalPlayers(l: Location): List<Player> {
        return getLocalPlayers(l, MapDistance.RENDERING.distance)
    }

    /**
     * Gets a list of local players.
     * @param l The location.
     * @param distance The distance to that location.
     * @return The list of players.
     */
    @JvmStatic
    fun getLocalPlayers(l: Location, distance: Int): MutableList<Player> {
        val players: MutableList<Player> = LinkedList()
        for (regionX in ((l.regionX - 6) shr 3)..((l.regionX + 6) shr 3)) {
            for (regionY in ((l.regionY - 6) shr 3)..((l.regionY + 6) shr 3)) {
                for (player in forId((regionX shl 8) or regionY).planes[l.z].players) {
                    if (player.location.withinDistance(l, distance)) {
                        players.add(player)
                    }
                }
            }
        }
        return players
    }

    /**
     * Gets a list of local players.
     * @param l The location.
     * @param distance The distance to that location.
     * @return The list of players.
     */
    @JvmStatic
    fun getLocalPlayersMaxNorm(l: Location, distance: Int): MutableList<Player> {
        val players: MutableList<Player> = LinkedList()
        for (regionX in ((l.regionX - 6) shr 3)..((l.regionX + 6) shr 3)) {
            for (regionY in ((l.regionY - 6) shr 3)..((l.regionY + 6) shr 3)) {
                for (player in forId((regionX shl 8) or regionY).planes[l.z].players) {
                    if (player.location.withinMaxnormDistance(l, distance)) {
                        players.add(player)
                    }
                }
            }
        }
        return players
    }

    /**
     * Gets a list of local players within 16 tile distance of the location.
     * @param l The location.
     * @return The list of players.
     */
    @JvmStatic
    fun getLocalNpcs(l: Location): List<NPC> {
        return getLocalNpcs(l, MapDistance.RENDERING.distance)
    }

    /**
     * Gets the npc.
     * @param entity the entity.
     * @param id the id.
     */
    @JvmStatic
    fun getNpc(entity: Entity, id: Int): NPC? {
        return getNpc(entity, id, 16)
    }

    /**
     * Gets the npc.
     * @param entity the entity.
     * @param id the id.
     * @param distance the distance.
     * @return the npc.
     */
    @JvmStatic
    fun getNpc(entity: Entity, id: Int, distance: Int): NPC? {
        return getNpc(entity.location, id, distance)
    }

    /**
     * Gets an npc near the entity.
     * @param id the id,
     * @param distance the dinstance.
     * @return the npc.
     */
    @JvmStatic
    fun getNpc(location: Location, id: Int, distance: Int): NPC? {
        val npcs: List<NPC> = getLocalNpcs(location, distance)
        for (n in npcs) {
            if (n.id == id) {
                return n
            }
        }
        return null
    }

    /**
     * Gets a list of local players.
     * @param l The location.
     * @param distance The distance to that location.
     * @return The list of players.
     */
    @JvmStatic
    fun getLocalNpcs(l: Location, distance: Int): MutableList<NPC> {
        val npcs: MutableList<NPC> = LinkedList()
        for (regionX in ((l.regionX - 6) shr 3)..((l.regionX + 6) shr 3)) {
            for (regionY in ((l.regionY - 6) shr 3)..((l.regionY + 6) shr 3)) {
                for (n in forId(regionX shl 8 or regionY).planes[l.z].npcs) {
                    if (n.location.withinDistance(l, (n.size() shr 1) + distance)) {
                        npcs.add(n)
                    }
                }
            }
        }
        return npcs
    }

    @JvmStatic
    fun addRegion(id: Int, region: Region){
        if(lock.tryLock() || LOCK.tryLock(10000, TimeUnit.MILLISECONDS)) {
            REGION_CACHE[id] = region
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

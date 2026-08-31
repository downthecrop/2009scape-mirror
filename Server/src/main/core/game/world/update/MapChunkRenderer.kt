package core.game.world.update

import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionChunk
import core.net.packet.PacketRepository
import core.net.packet.context.ClearChunkContext
import core.net.packet.out.ClearRegionChunk
import java.util.*

/**
 * Handles the rendering of the player's surrounding map chunks.
 * @author Emperor
 */
object MapChunkRenderer {
    const val BUILD_AREA_DEPTH = 6
    const val BUILD_AREA_SIZE = 2 * BUILD_AREA_DEPTH + 1

    /**
     * Sends the map chunk rendering packet.
     * @param player The player.
     */
    @JvmStatic
    fun render(player: Player) {
        val last = player.playerFlags.lastViewport
        val updated: ArrayList<RegionChunk> = ArrayList()
        val anchor = player.playerFlags.lastSceneGraph ?: player.location
        val center = Location.create(anchor.x, anchor.y, player.location.z) // z is correct, the client does not tear down the build area on z scene transitions (and WalkingQueue.java similarly does not invalidate the scene graph on z transitions, only on x/y transitions)
        val current = List(BUILD_AREA_SIZE) { x ->
            val dcx = x - BUILD_AREA_DEPTH // so it becomes -6..+6 instead of 0..12
            List(BUILD_AREA_SIZE) { y ->
                val dcy = y - BUILD_AREA_DEPTH
                center.transform(dcx * RegionChunk.SIZE, dcy * RegionChunk.SIZE, 0).chunk
            }
        }

        var sizeX = last.size
        for (x in 0 until sizeX) {
            val sizeY: Int = last[x].size
            for (y in 0 until sizeY) {
                val previous = last[x][y] ?: continue
                if (containsChunk(current, previous)) {
                    updated.add(previous)
                } else {
                    PacketRepository.send(ClearRegionChunk::class.java, ClearChunkContext(player, previous))
                }
            }
        }

        sizeX = current.size
        for (x in 0 until sizeX) {
            val sizeY: Int = current[x].size
            for (y in 0 until sizeY) {
                val chunk = current[x][y]
                if (updated.contains(chunk)) {
                    chunk.update(player)
                } else {
                    chunk.synchronize(player)
                }
                last[x][y] = chunk
            }
        }
    }

    /**
     * Checks if the chunks list contains the specified region chunk.
     * @param list The list to search.
     * @param c The region chunk.
     * @return `True` if so.
     */
    private fun containsChunk(list: List<List<RegionChunk>>, c: RegionChunk): Boolean {
        val sizeList = list.size
        for (x in 0 until sizeList) {
            val chunkSize: Int = list[x].size
            for (y in 0 until chunkSize) {
                if (list[x][y] === c) {
                    return true
                }
            }
        }
        return false
    }
}

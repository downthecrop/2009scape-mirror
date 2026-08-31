package core.game.world.update

import core.game.world.map.RegionChunk
import java.util.Collections
import java.util.IdentityHashMap

object ChunkUpdateTracker : ChunkDirtyListener {
    private val dirty = Collections.newSetFromMap(IdentityHashMap<RegionChunk, Boolean>())

    override fun onFlagged(chunk: RegionChunk) {
        dirty.add(chunk)
    }

    /** Resets flags on exactly the chunks flagged this tick. Must run after all per-player flushes. */
    fun resetAll() {
        dirty.forEach { it.resetUpdateFlags() }
        dirty.clear()
    }
}
fun interface ChunkDirtyListener {
    fun onFlagged(chunk: RegionChunk)
}
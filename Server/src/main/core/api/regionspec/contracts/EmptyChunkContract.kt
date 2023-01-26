package core.api.regionspec.contracts

import core.game.world.map.build.DynamicRegion

class EmptyChunkContract : ChunkSpecContract {
    override fun populateChunks(dyn: DynamicRegion) {}
}

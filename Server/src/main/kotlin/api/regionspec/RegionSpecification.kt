package api.regionspec

import api.regionspec.contracts.*
import core.game.world.map.Region
import core.game.world.map.RegionChunk
import core.game.world.map.RegionPlane
import core.game.world.map.build.DynamicRegion

class RegionSpecification(val regionContract: RegionSpecContract = EmptyRegionContract(), vararg val chunkContracts: ChunkSpecContract = arrayOf(EmptyChunkContract())) {
    constructor(vararg chunkContracts: ChunkSpecContract) : this(EmptyRegionContract(), *chunkContracts)

    fun build(): DynamicRegion {
        val dyn = regionContract.instantiateRegion()
        Region.load(dyn)
        chunkContracts.forEach { it.populateChunks(dyn) }
        return dyn
    }
}

fun fillWith(chunk: RegionChunk?): FillChunkContract {
    return FillChunkContract(chunk)
}

fun fillWith(delegate: (Int, Int, Int, Region) -> RegionChunk?) : FillChunkContract {
    return FillChunkContract(delegate)
}

fun copyOf(regionId: Int): RegionSpecContract {
    return CloneRegionContract(regionId)
}

fun using(region: DynamicRegion) : UseExistingRegionContract {
    return UseExistingRegionContract(region)
}

package api.regionspec.contracts

import core.game.world.map.build.DynamicRegion

class CloneRegionContract(val regionId: Int) : RegionSpecContract {
    override fun instantiateRegion(): DynamicRegion {
        return DynamicRegion.create(regionId)
    }
}

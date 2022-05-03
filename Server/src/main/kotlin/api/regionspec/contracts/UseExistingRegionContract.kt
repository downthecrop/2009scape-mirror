package api.regionspec.contracts

import core.game.world.map.build.DynamicRegion

class UseExistingRegionContract(val region: DynamicRegion) : RegionSpecContract {
    override fun instantiateRegion(): DynamicRegion {
        return region;
    }
}
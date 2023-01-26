package core.api.regionspec.contracts

import core.game.world.map.build.DynamicRegion

interface RegionSpecContract {
    fun instantiateRegion(): DynamicRegion
}

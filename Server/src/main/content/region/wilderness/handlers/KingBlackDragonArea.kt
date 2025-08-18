package content.region.wilderness.handlers

import core.api.*
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

class KingBlackDragonArea : MapArea {
    override fun defineAreaBorders() : Array<ZoneBorders> { 
        return arrayOf(ZoneBorders(2256, 4680, 2287, 4711, 0, true)) 
    }
    override fun getRestrictions() : Array<ZoneRestriction> { 
        return arrayOf(ZoneRestriction.RANDOM_EVENTS) 
    }
}
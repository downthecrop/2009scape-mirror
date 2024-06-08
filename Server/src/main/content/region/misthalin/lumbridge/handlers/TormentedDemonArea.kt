package content.region.misthalin.lumbridge

import core.api.*
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

class TormentedDemonArea : MapArea {
    override fun defineAreaBorders() : Array<ZoneBorders> { return arrayOf (ZoneBorders.forRegion(10329)) }
    override fun getRestrictions() : Array<ZoneRestriction> { return arrayOf (ZoneRestriction.CANNON, ZoneRestriction.RANDOM_EVENTS) }
}

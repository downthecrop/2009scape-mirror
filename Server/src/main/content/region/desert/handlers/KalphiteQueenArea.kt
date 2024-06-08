package content.region.desert.handlers

import core.api.*
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

class KalphiteQueenArea : MapArea {
    override fun defineAreaBorders() : Array<ZoneBorders> { return arrayOf(ZoneBorders(3456, 9472, 3519, 9535, 0, true)) }
    override fun getRestrictions() : Array<ZoneRestriction> { return arrayOf(ZoneRestriction.RANDOM_EVENTS) }
}

package content.region.misc.tutisland.handlers

import core.api.*
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

class TutorialArea : MapArea {
    override fun defineAreaBorders() : Array<ZoneBorders> { return arrayOf(12079, 12080, 12335, 12336, 12436, 12592).map { ZoneBorders.forRegion(it) }.toTypedArray() }
    override fun getRestrictions() : Array<ZoneRestriction> { return arrayOf(ZoneRestriction.RANDOM_EVENTS) }
}

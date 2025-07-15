package content.minigame.barbassault

import core.api.MapArea
import core.api.getRegionBorders
import core.game.node.entity.Entity
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneType

class BarbAssaultArea : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(7509))
    }

    override fun areaEnter(entity: Entity) {
        zone.zoneType = ZoneType.BARBARIAN_ASSAULT.id
        super.areaEnter(entity)
    }
}
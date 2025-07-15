package content.minigame.troublebrewing

import core.api.MapArea
import core.api.getRegionBorders
import core.game.node.entity.Entity
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneType

class TroubleBrewingArea : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(15150))
    }

    override fun areaEnter(entity: Entity) {
        zone.zoneType = ZoneType.TROUBLE_BREWING.id
        super.areaEnter(entity)
    }
}

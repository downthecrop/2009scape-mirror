package content.region.misthalin.varrock.handlers

import core.api.MapArea
import core.api.closeOverlay
import core.api.openOverlay
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Components

class MuseumMapArea : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        val vmArea = ZoneBorders(3253, 3442, 3267, 3455)
        val vmBasementArea = ZoneBorders(1730, 4932, 1788, 4988)
        return arrayOf(vmArea, vmBasementArea)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            openOverlay(entity.asPlayer(), Components.VM_KUDOS_532)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            closeOverlay(entity.asPlayer())
        }
    }
}
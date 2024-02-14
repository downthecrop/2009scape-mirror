package content.region.kandarin.quest.templeofikov

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders

class BridgeAgility: MapArea {
    companion object {
        private fun checkWeightOrFailBridge(entity: Entity) {
            // If player weighs more than 0kg at any point on the bridge, the bridge breaks.
            // The boots of lightness may be needed here as it makes the player -4kg lighter.
            if (entity is Player && entity.settings.weight > 0) {
                entity.impactHandler.manualHit(entity, 20, ImpactHandler.HitsplatType.NORMAL)
                sendMessage(entity, "The bridge gives way under your weight!")
                sendChat(entity, "Ow! Hot! Hot!")
                sendMessage(entity, "Good thing that lava was shallow!")
                entity.teleport(Location.create(2647, 9826, 0))
            }
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2648, 9828, 2649, 9829))
    }
    override fun areaEnter(entity: Entity) {
        checkWeightOrFailBridge(entity)
    }
    override fun areaLeave(entity: Entity, logout: Boolean) {
        checkWeightOrFailBridge(entity)
    }
    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        checkWeightOrFailBridge(entity)
    }
}

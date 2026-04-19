package content.global.skill.hunter.falconry

import core.api.*
import core.api.MapArea
import core.game.activity.ActivityManager
import core.game.node.entity.Entity
import core.game.world.map.zone.ZoneBorders
import core.game.node.entity.player.Player
import org.rs09.consts.Items

class PiscFalconryListener: MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return FalconryZone.piscatorisFalconryZones
    }

    /**
     * When player leaves by any method other than logout/disconnect.
     */
    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            if (!logout) {
                // Legacy compatibility of FalconryActivityPlugin.java
                removeAttribute(entity, "falconry")

                // Compatibility with StileShortcut.kt
                ActivityManager.getActivity("falconry").leave(entity, false)

                removeAll(entity, Items.FALCONERS_GLOVE_10023)
                removeAll(entity, Items.FALCONERS_GLOVE_10023, Container.EQUIPMENT)

                removeAll(entity, Items.FALCONERS_GLOVE_10024)
                removeAll(entity, Items.FALCONERS_GLOVE_10024, Container.EQUIPMENT)
            }
        }
    }
}
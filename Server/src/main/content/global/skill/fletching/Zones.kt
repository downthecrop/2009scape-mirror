package content.global.skill.fletching

import core.game.node.Node
import core.game.world.map.zone.ZoneBorders

object Zones {
    val seersMagicShortbowAchievementZones = arrayOf(
        ZoneBorders(2721, 3489, 2724, 3493, 0),
        ZoneBorders(2727, 3487, 2730, 3490, 0),
        ZoneBorders(2721, 3493, 2730, 3487)
    )

    fun inAnyZone(node: Node, zones: Array<ZoneBorders>): Boolean {
        for (zone in zones) {
            if (zone.insideBorder(node)) {
                return true
            }
        }
        return false
    }
}

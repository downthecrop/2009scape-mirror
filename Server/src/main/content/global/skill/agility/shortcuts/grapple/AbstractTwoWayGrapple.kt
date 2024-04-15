package content.global.skill.agility.shortcuts.grapple

import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location

abstract class AbstractTwoWayGrapple : AbstractGrappleShortcut(){

    abstract var direction: Direction?

    abstract var startLoc: Location?
    abstract var endLoc: Location?

    protected abstract fun setStartEndSide(player: Player, margin: Int = 5)
    protected abstract fun getGrappleScenery(direction: Direction): List<core.game.node.scenery.Scenery?>
}
package content.minigame.blastfurnace

import core.api.resetAnimator
import core.api.teleport
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer

class BFTempEntranceTimer : PersistTimer(BlastConsts.FEE_ENTRANCE_DURATION, "bf-tempentrance") {
    override fun run(entity: Entity): Boolean {
        if (entity !is Player) return false
        if (BlastFurnace.insideBorders(entity)) {
            teleport(entity, BlastConsts.EXIT_LOC)
            entity.pulseManager.clear()
            resetAnimator(entity)
        }
        return false
    }
}
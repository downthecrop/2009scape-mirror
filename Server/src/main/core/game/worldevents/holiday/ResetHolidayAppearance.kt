package core.game.worldevents.holiday

import core.api.playAudio
import core.api.sendMessage
import core.api.visualize
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.RSTimer
import core.tools.minutesToTicks
import core.tools.secondsToTicks
import org.rs09.consts.Sounds

class ResetHolidayAppearance() : RSTimer(minutesToTicks(1), "reset-holiday-appearance") {
    override fun run(entity: Entity): Boolean {
        if (entity is Player) {
            entity.asPlayer().appearance.transformNPC(-1)
            playAudio(entity.asPlayer(), Sounds.WEAKEN_HIT_3010)
            visualize(entity, -1, 86)
        }
        entity.timers.removeTimer(this)
        return true
    }
}
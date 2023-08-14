package core.game.system.timer.impl

import core.api.hasTimerActive
import core.api.removeTimer
import core.game.node.entity.Entity
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag

class MiasmicImmunity : PersistTimer (1, "miasmic:immunity", flags = arrayOf(TimerFlag.ClearOnDeath)) {
    override fun run (entity: Entity) : Boolean {
        return false
    }

    override fun onRegister (entity: Entity) {
        if (hasTimerActive<Miasmic>(entity))
            removeTimer<Miasmic>(entity)
    }

    override fun getTimer (vararg args: Any) : RSTimer {
        val t = MiasmicImmunity()
        t.runInterval = args.getOrNull(0) as? Int ?: 100
        return t
    }
}

package core.game.system.timer.impl

import core.api.hasTimerActive
import core.api.registerTimer
import core.api.removeTimer
import core.api.spawnTimer
import core.game.node.entity.Entity
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag

class Miasmic : PersistTimer (1, "miasmic", flags = arrayOf(TimerFlag.ClearOnDeath)) {
    override fun run (entity: Entity) : Boolean {
        registerTimer (entity, spawnTimer<MiasmicImmunity>(entity, 7))
        return false
    }

    override fun onRegister (entity: Entity) {
        if (hasTimerActive<MiasmicImmunity>(entity))
            removeTimer(entity, this)
        if (hasTimerActive<Miasmic>(entity))
            removeTimer(entity, this)
    }   

    override fun getTimer (vararg args: Any) : RSTimer {
        val t = Miasmic()
        t.runInterval = args.getOrNull(0) as? Int ?: 100
        return t
    }
}

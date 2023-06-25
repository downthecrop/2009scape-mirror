package core.game.system.timer.impl

import core.game.system.timer.*
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import org.json.simple.*

class MiasmicImmunity : PersistTimer (1, "miasmic:immunity") {
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

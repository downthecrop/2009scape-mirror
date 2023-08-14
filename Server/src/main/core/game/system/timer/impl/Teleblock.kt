package core.game.system.timer.impl

import core.game.system.timer.*
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import org.json.simple.*

class Teleblock : PersistTimer (1, "teleblock", flags = arrayOf(TimerFlag.ClearOnDeath)) {
    override fun run (entity: Entity) : Boolean {
        return false
    }

    override fun onRegister (entity: Entity) {
        if (entity !is Player) return
        sendMessage (entity, "You have been teleblocked.")
    }

    override fun getTimer (vararg args: Any) : RSTimer {
        val t = Teleblock()
        t.runInterval = args.getOrNull(0) as? Int ?: 100
        return t
    }
}

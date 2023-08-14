package core.game.system.timer.impl

import core.game.system.timer.*
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.repository.Repository
import org.json.simple.*

class FrozenImmunity : PersistTimer (1, "frozen:immunity", flags = arrayOf(TimerFlag.ClearOnDeath)) {
    var ticksRemaining = 0

    override fun save (root: JSONObject, entity: Entity) {
        root["ticksLeft"] = (nextExecution - getWorldTicks()).toString()
    }

    override fun parse (root: JSONObject, entity: Entity) {
        runInterval = root["ticksLeft"].toString().toInt()
    }

    override fun onRegister (entity: Entity) {
        if (hasTimerActive<Frozen>(entity)) {
            removeTimer<Frozen>(entity)
        }
        (entity as? Player)?.debug("Applied frozen immunity for $runInterval ticks.")
    }

    override fun run (entity: Entity) : Boolean {
        (entity as? Player)?.debug("Removed frozen immunity")
        return false
    }

    override fun getTimer (vararg args: Any) : RSTimer {
        val inst = FrozenImmunity()
        inst.runInterval = args.getOrNull(0) as? Int ?: 7
        return inst
    }
}

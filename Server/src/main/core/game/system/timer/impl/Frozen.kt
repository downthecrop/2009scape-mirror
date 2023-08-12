package core.game.system.timer.impl

import core.game.system.timer.*
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.repository.Repository
import org.json.simple.*

class Frozen : PersistTimer (1, "frozen", flags = arrayOf(TimerFlag.ClearOnDeath)) {
    var shouldApplyImmunity = false

    override fun save (root: JSONObject, entity: Entity) {
        root["ticksLeft"] = (nextExecution - getWorldTicks()).toString()
        root["applyImmunity"] = shouldApplyImmunity
    }

    override fun parse (root: JSONObject, entity: Entity) {
        runInterval = root["ticksLeft"].toString().toInt()
        shouldApplyImmunity = root["applyImmunity"] as? Boolean ?: false
    }

    override fun onRegister (entity: Entity) {
        if (hasTimerActive<FrozenImmunity>(entity)) {
            removeTimer(entity, this)
            return
        }
        if (hasTimerActive<Frozen>(entity)) {
            removeTimer(entity, this)
            return
        }
    }

    override fun run (entity: Entity) : Boolean {
        if (shouldApplyImmunity) {
            registerTimer (entity, spawnTimer<FrozenImmunity>(7))
        } else (entity as? Player)?.debug ("Can't apply immunity")
        return false
    }

    override fun getTimer (vararg args: Any) : RSTimer {
        val inst = Frozen()
        inst.runInterval = args.getOrNull(0) as? Int ?: 10
        inst.shouldApplyImmunity = args.getOrNull(1) as? Boolean ?: false
        return inst
    }
}

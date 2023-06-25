package core.game.system.timer

import core.api.*
import org.json.simple.*
import core.game.node.entity.Entity
import kotlin.reflect.full.createInstance

abstract class PersistTimer (runInterval: Int, identifier: String, isSoft: Boolean = false) : RSTimer (runInterval, identifier, isSoft) {
    open fun save (root: JSONObject, entity: Entity) {
        root["ticksLeft"] = (nextExecution - getWorldTicks()).toString()
    }

    open fun parse (root: JSONObject, entity: Entity) {
        runInterval = root["ticksLeft"].toString().toInt()
    }

    override fun retrieveInstance() : RSTimer {
        return this::class.createInstance()
    }
}

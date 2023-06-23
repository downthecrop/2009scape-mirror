package core.game.system.timer

import org.json.simple.*
import core.game.node.entity.Entity
import kotlin.reflect.full.createInstance

abstract class PersistTimer (runInterval: Int, identifier: String, isSoft: Boolean = false) : RSTimer (runInterval, identifier, isSoft) {
    abstract fun save  (root: JSONObject, entity: Entity)
    abstract fun parse (root: JSONObject, entity: Entity)

    override fun retrieveInstance() : RSTimer {
        return this::class.createInstance()
    }
}

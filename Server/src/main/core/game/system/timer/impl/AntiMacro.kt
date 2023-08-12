package core.game.system.timer.impl

import core.api.getWorldTicks
import core.game.node.entity.Entity
import core.game.system.timer.PersistTimer
import core.tools.RandomFunction
import org.json.simple.JSONObject

class AntiMacro : PersistTimer(0, "antimacro", isAuto = true) {
    override fun run(entity: Entity): Boolean {
        setNextExecution()
        return true
    }

    override fun onRegister(entity: Entity) {
        if (nextExecution == getWorldTicks())
            setNextExecution()
    }

    private fun setNextExecution() {
        runInterval = RandomFunction.random(MIN_DELAY_TICKS, MAX_DELAY_TICKS + 1)
        nextExecution = getWorldTicks() + runInterval
    }

    override fun save(root: JSONObject, entity: Entity) {
        root["ticksRemaining"] = (nextExecution - getWorldTicks()).toString()
    }

    override fun parse(root: JSONObject, entity: Entity) {
        nextExecution = getWorldTicks() + (root["ticksRemaining"]?.toString()?.toIntOrNull() ?: 0)
    }

    companion object {
        const val MIN_DELAY_TICKS = 3000
        const val MAX_DELAY_TICKS = 9000
    }
}
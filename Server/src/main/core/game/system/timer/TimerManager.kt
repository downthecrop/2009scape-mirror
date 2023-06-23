package core.game.system.timer

import core.api.*
import core.tools.*
import java.util.ArrayList
import org.json.simple.JSONObject
import core.game.node.entity.Entity
import core.game.node.entity.player.Player

class TimerManager (val entity: Entity) {
    val activeTimers = ArrayList<RSTimer>()
    val newTimers = ArrayList<RSTimer>()
    val toRemoveTimers = ArrayList<RSTimer>()

    fun registerTimer (timer: RSTimer) {
        newTimers.add (timer)
        timer.onRegister(entity)
    }

    fun processTimers () {
        activeTimers.removeAll(toRemoveTimers)
        newTimers.removeAll(toRemoveTimers)
        toRemoveTimers.clear()

        val canRunNormalTimers = (entity !is Player) || !(entity.asPlayer().hasModalOpen() || entity.scripts.delay > getWorldTicks())
        for (timer in activeTimers) {
            if (timer.nextExecution > getWorldTicks()) continue
            if (!canRunNormalTimers && !timer.isSoft) continue

            if (timer.run(entity)) {
                timer.nextExecution = getWorldTicks() + timer.runInterval
            } else {
                timer.nextExecution = Int.MAX_VALUE
                toRemoveTimers.add(timer)
            }
        }

        for (timer in newTimers) {
            activeTimers.add(timer)
            timer.nextExecution = timer.getInitialRunDelay() + getWorldTicks()
        }

        newTimers.clear()
    }

    fun clearTimers () {
        activeTimers.clear()
        newTimers.clear()
        toRemoveTimers.clear()
    }

    fun saveTimers (root: JSONObject) {
        for (timer in activeTimers) {
            if (timer !is PersistTimer) continue
            val obj = JSONObject()
            timer.save(obj, entity)
            root [timer.identifier] = obj
        }
    }

    fun parseTimers (root: JSONObject) {
        for ((identifier, dataObj) in root) {
            val data = dataObj as JSONObject
            val timer = TimerRegistry.getTimerInstance (identifier.toString())

            if (timer == null) {
                log (this::class.java, Log.ERR, "Tried to load data for timer identified by $identifier, but no such timer seems to exist.")
                continue
            }

            timer.parse(data, entity)
            registerTimer(entity, timer)
        }
    }

    inline fun <reified T> removeTimer () {
        for (timer in activeTimers)
            if (timer is T)
                toRemoveTimers.add(timer)
        for (timer in newTimers)
            if (timer is T)
                toRemoveTimers.add(timer)
    }

    inline fun <reified T> getTimer () : T? {
        for (timer in activeTimers)
            if (timer is T)
                return timer
        for (timer in newTimers)
            if (timer is T)
                return timer
        return null
    }

    fun getTimer (identifier: String): RSTimer? {
        for (timer in activeTimers)
            if (timer.identifier == identifier)
                return timer
        for (timer in newTimers)
            if (timer.identifier == identifier)
                return timer
        return null
    }

    fun removeTimer (identifier: String) {
        for (timer in activeTimers)
            if (timer.identifier == identifier)
                toRemoveTimers.add(timer)
        for (timer in newTimers)
            if (timer.identifier == identifier)
                toRemoveTimers.add(timer)
    }
}

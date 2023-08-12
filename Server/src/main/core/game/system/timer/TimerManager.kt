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
        timer.onRegister(entity)
        newTimers.add (timer)
    }

    fun processTimers () {
        activeTimers.removeAll(toRemoveTimers)
        newTimers.removeAll(toRemoveTimers)
        toRemoveTimers.clear()

        val canRunNormalTimers = (entity !is Player) || !(entity.asPlayer().hasModalOpen() || entity.scripts.delay > getWorldTicks())
        for (timer in activeTimers) {
            if (timer.nextExecution > getWorldTicks()) continue
            if (!canRunNormalTimers && !timer.isSoft) continue

            try {
                if (timer.run(entity)) {
                    timer.nextExecution = getWorldTicks() + timer.runInterval
                } else removeTimer(timer)
            } catch (e: Exception) {
                log (this::class.java, Log.ERR, "Prematurely removing timer ${timer::class.java.simpleName} from ${entity.name} because it threw an exception when ran. Exception follows:")
                e.printStackTrace()
                removeTimer(timer)
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

    fun onEntityDeath() {
        for (timer in activeTimers) {
            if (timer.flags.contains(TimerFlag.ClearOnDeath))
                removeTimer(timer)
        }
    }

    fun saveTimers (root: JSONObject) {
        for (timer in activeTimers) {
            if (timer !is PersistTimer) continue
            val obj = JSONObject()
            timer.save(obj, entity)
            root [timer.identifier] = obj
        }
        for (timer in newTimers) {
            if (timer !is PersistTimer) continue
            val obj = JSONObject()
            timer.save(obj, entity)
            root [timer.identifier] = obj
        }
    }

    fun parseTimers (root: JSONObject) {
        for ((identifier, dataObj) in root) {
            val data = dataObj as JSONObject
            val timer = TimerRegistry.getTimerInstance (identifier.toString()) as? PersistTimer

            if (timer == null) {
                log (this::class.java, Log.ERR, "Tried to load data for persistent timer identified by $identifier, but no such timer seems to exist.")
                continue
            }

            timer.parse(data, entity)
            registerTimer(entity, timer)
        }
    }

    inline fun <reified T: RSTimer> removeTimer () {
        for (timer in activeTimers)
            if (timer is T)
                removeTimer(timer)
        for (timer in newTimers)
            if (timer is T)
                removeTimer(timer)
    }

    inline fun <reified T: RSTimer> getTimer () : T? {
        var t: T? = null
        for (timer in activeTimers)
            if (timer is T)
                t = timer
        for (timer in newTimers)
            if (timer is T)
                t = timer
        if (t == null) return null
        if (toRemoveTimers.contains(t))
            return null
        return t
    }

    fun getTimer (identifier: String): RSTimer? {
        var t: RSTimer? = null
        for (timer in activeTimers)
            if (timer.identifier == identifier)
                t = timer
        for (timer in newTimers)
            if (timer.identifier == identifier)
                t = timer
        if (t == null) return null
        if (toRemoveTimers.contains(t)) return null
        return t
    }

    fun removeTimer (identifier: String) {
        for (timer in activeTimers)
            if (timer.identifier == identifier)
                removeTimer(timer)
        for (timer in newTimers)
            if (timer.identifier == identifier)
                removeTimer(timer)
    }

    fun removeTimer (timer: RSTimer) {
        timer.nextExecution = Int.MAX_VALUE
        toRemoveTimers.add(timer)
        try { timer.onRemoval(entity) } catch (e: Exception) { e.printStackTrace() }
    }
}

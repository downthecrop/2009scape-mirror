package core.game.system.timer

import core.api.*
import core.tools.*
import java.util.ArrayList
import org.json.simple.JSONObject
import core.game.node.entity.Entity
import core.game.node.entity.player.Player

abstract class TimerManager {
    val activeTimers = ArrayList<Timer>()
    val newTimers = ArrayList<Timer>()
    val toRemoveTimers = ArrayList<Timer>()

    abstract fun registerTimer(timer: Timer)
    abstract fun runTimer(timer: Timer): Boolean? // should return null if timer should not run at all, e.g. user has interface open
    abstract fun parseTimers(root: JSONObject)
    abstract fun saveTimers(root: JSONObject)
    abstract fun removeTimer(timer: Timer)
    abstract fun onEntityDeath()

    fun processTimers() {
        activeTimers.removeAll(toRemoveTimers.toSet())
        newTimers.removeAll(toRemoveTimers.toSet())
        toRemoveTimers.clear()

        for (timer in activeTimers) {
            if (timer.nextExecution > getWorldTicks()) {
                continue
            }
            try {
                val result = runTimer(timer) ?: continue
                if (result) {
                    timer.nextExecution = getWorldTicks() + timer.runInterval
                } else removeTimer(timer)
            } catch (e: Exception) {
                log(this::class.java, Log.ERR, "Prematurely removing timer ${timer::class.java.simpleName} because it threw an exception when ran. Exception follows:")
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

    fun clearTimers() {
        activeTimers.clear()
        newTimers.clear()
        toRemoveTimers.clear()
    }

    inline fun<reified T: Timer> removeTimer() {
        for (timer in activeTimers) {
            if (timer is T) {
                removeTimer(timer)
            }
        }
        for (timer in newTimers) {
            if (timer is T) {
                removeTimer(timer)
            }
        }
    }

    inline fun<reified T: Timer> getTimer() : T? {
        var t: T? = null
        for (timer in activeTimers) {
            if (timer is T) {
                t = timer
            }
        }
        for (timer in newTimers) {
            if (timer is T) {
                t = timer
            }
        }
        if (t == null) {
            return null
        }
        if (toRemoveTimers.contains(t)) {
            return null
        }
        return t
    }

    fun getTimer(identifier: String): Timer? {
        var t: Timer? = null
        for (timer in activeTimers) {
            if (timer.identifier == identifier) {
                t = timer
            }
        }
        for (timer in newTimers) {
            if (timer.identifier == identifier) {
                t = timer
            }
        }
        if (t == null) {
            return null
        }
        if (toRemoveTimers.contains(t)) {
            return null
        }
        return t
    }

    fun removeTimer(identifier: String) {
        for (timer in activeTimers) {
            if (timer.identifier == identifier) {
                removeTimer(timer)
            }
        }
        for (timer in newTimers) {
            if (timer.identifier == identifier) {
                removeTimer(timer)
            }
        }
    }
}

class EntityTimerManager(val entity: Entity): TimerManager() {
    override fun registerTimer(timer: Timer) {
        val timer = timer as RSTimer
        timer.beforeRegister(entity)
        newTimers.add(timer)
        timer.onRegister(entity)
    }

    override fun runTimer(timer: Timer): Boolean? {
        val timer = timer as RSTimer
        val canRunNormalTimers = (entity !is Player) || !(entity.asPlayer().hasModalOpen() || entity.scripts.delay > getWorldTicks())
        if (!canRunNormalTimers && !timer.isSoft) {
            return null
        }
        return timer.run(entity)
    }

    override fun onEntityDeath() {
        for (timer in activeTimers) {
            if (timer is RSTimer && timer.flags.contains(TimerFlag.ClearOnDeath))
                removeTimer(timer)
        }
    }

    override fun parseTimers(root: JSONObject) {
        for ((identifier, dataObj) in root) {
            val data = dataObj as JSONObject
            val timer = TimerRegistry.getTimerInstance(identifier.toString()) as? PersistTimer

            if (timer == null) {
                log(this::class.java, Log.ERR, "Tried to load data for persistent timer identified by $identifier, but no such timer seems to exist.")
                continue
            }

            timer.parse(data, entity)
            registerTimer(entity, timer)
        }
    }

    override fun saveTimers(root: JSONObject) {
        for (timer in activeTimers) {
            if (timer !is PersistTimer) {
                continue
            }
            val obj = JSONObject()
            timer.save(obj, entity)
            root [timer.identifier] = obj
        }
        for (timer in newTimers) {
            if (timer !is PersistTimer) {
                continue
            }
            val obj = JSONObject()
            timer.save(obj, entity)
            root[timer.identifier] = obj
        }
    }

    override fun removeTimer(timer: Timer) {
        val timer = timer as RSTimer
        timer.nextExecution = Int.MAX_VALUE
        toRemoveTimers.add(timer)
        try {
            timer.onRemoval(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class WorldTimerManager: TimerManager() {
    override fun registerTimer(timer: Timer) {
        newTimers.add(timer)
    }

    override fun runTimer(timer: Timer): Boolean {
        val timer = timer as WorldTimer
        return timer.run()
    }

    override fun removeTimer(timer: Timer) {
        timer.nextExecution = Int.MAX_VALUE
        toRemoveTimers.add(timer)
    }

    override fun onEntityDeath() {}
    override fun parseTimers(root: JSONObject) {}
    override fun saveTimers(root: JSONObject) {}
}

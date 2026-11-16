package core.game.system.timer

import java.util.*
import core.api.*
import core.tools.*
import core.game.node.entity.Entity

object TimerRegistry {
    val timerMap = HashMap<String, Timer>()
    val autoTimers = ArrayList<Timer>()

    fun registerTimer(timer: Timer) {
        log(this::class.java, Log.WARN, "Registering timer ${timer::class.java.simpleName}")
        if (timerMap.containsKey(timer.identifier.lowercase())) {
            log(this::class.java, Log.ERR, "Timer identifier ${timer.identifier} already in use by ${timerMap[timer.identifier.lowercase()]!!::class.java.simpleName}! Not loading ${timer::class.java.simpleName}!")
            return
        }
        timerMap[timer.identifier.lowercase()] = timer
        if (timer is RSTimer && timer.isAuto) {
            autoTimers.add(timer)
        }
    }

    fun getTimerInstance(identifier: String, vararg args: Any): Timer? {
        val t = timerMap[identifier.lowercase()]
        return if (args.isNotEmpty()) t?.getTimer(*args) else t?.retrieveInstance()
    }

    @JvmStatic
    fun addAutoTimers(entity: Entity) {
        for (timer in autoTimers) {
           if (!hasTimerActive(entity, timer.identifier)) {
               registerTimer(entity, timer.retrieveInstance() as RSTimer?)
           }
        }
    }

    inline fun <reified T> getTimerInstance(vararg args: Any): T? {
        for ((_, inst) in timerMap) {
            if (inst is T) {
                return if (args.isNotEmpty()) inst.getTimer(*args) as? T else inst.retrieveInstance() as? T
            }
        }
        return null
    }
}

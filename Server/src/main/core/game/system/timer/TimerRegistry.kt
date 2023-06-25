package core.game.system.timer

import java.util.*
import core.api.*
import core.tools.*

object TimerRegistry {
    val timerMap = HashMap<String, RSTimer>()

    fun registerTimer (timer: RSTimer) {
        if (timerMap.containsKey(timer.identifier.lowercase())) {
            log (this::class.java, Log.ERR, "Timer identifier ${timer.identifier} already in use by ${timerMap[timer.identifier.lowercase()]!!::class.java.simpleName}! Not loading ${timer::class.java.simpleName}!")
            return
        }
        timerMap[timer.identifier.lowercase()] = timer
    }

    fun getTimerInstance (identifier: String, vararg args: Any) : RSTimer? {
        var t = timerMap[identifier.lowercase()]
        if (args.size > 0)
            return t?.getTimer(*args)
        else
            return t?.retrieveInstance()
    }

    inline fun <reified T> getTimerInstance (vararg args: Any) : T? {
        for ((_, inst) in timerMap)
            if (inst is T) {
                if (args.size > 0)
                    return inst.getTimer(*args) as? T
                else
                    return inst.retrieveInstance() as? T
            }
        return null
    }
}

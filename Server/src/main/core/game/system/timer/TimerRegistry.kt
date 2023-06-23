package core.game.system.timer

import java.util.*

object TimerRegistry {
    val timerMap = HashMap<String, PersistTimer>()

    fun registerTimer (timer: PersistTimer) {
        timerMap[timer.identifier.lowercase()] = timer
    }

    fun getTimerInstance (identifier: String) : PersistTimer? {
        return timerMap[identifier.lowercase()]?.retrieveInstance() as? PersistTimer
    }
}

package core.game.world

import core.api.log
import core.game.system.task.Pulse
import core.game.bots.GeneralBotCreator
import core.tools.Log
import core.tools.SystemLogger
import java.util.concurrent.LinkedBlockingQueue

class PulseRunner {
    private val pulses = LinkedBlockingQueue<Pulse>()

    val currentPulses: Array<Pulse> get() = pulses.toTypedArray()

    fun submit(pulse: Pulse) {
        pulses.add(pulse)
    }

    fun updateAll() {
        val pulseCount = pulses.size

        for (i in 0 until pulseCount) {
            val pulse = pulses.take()

            val elapsedTime = measure {
                try {
                    if (!pulse.update()) {
                        pulses.add(pulse)
                    }
                } catch (e: Exception) {
                    log(this::class.java, Log.ERR,  "Pulse execution error. Stack trace below.")
                    e.printStackTrace()
                }
            }

            notifyIfTooLong(pulse, elapsedTime)
        }
    }

    private fun measure(logic: () -> Unit): Long {
        val startTime = System.currentTimeMillis()

        logic()

        return System.currentTimeMillis() - startTime
    }

    private fun notifyIfTooLong(pulse: Pulse, elapsedTime: Long) {
        if (elapsedTime >= 100) {
            if (pulse is GeneralBotCreator.BotScriptPulse) {
                log(this::class.java, Log.WARN,  "CRITICALLY long bot-script tick - ${pulse.botScript.javaClass.name} took $elapsedTime ms")
            } else {
                log(this::class.java, Log.WARN,  "CRITICALLY long running pulse - ${pulse.javaClass.name} took $elapsedTime ms")
            }
        } else if (elapsedTime >= 30) {
            if (pulse is GeneralBotCreator.BotScriptPulse) {
                log(this::class.java, Log.WARN,  "Long bot-script tick - ${pulse.botScript.javaClass.name} took $elapsedTime ms")
            } else {
                log(this::class.java, Log.WARN,  "Long running pulse - ${pulse.javaClass.name} took $elapsedTime ms")
            }
        }
    }
}
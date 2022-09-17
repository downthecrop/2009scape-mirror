package rs09.game.world

import core.game.system.task.Pulse
import rs09.game.ai.general.GeneralBotCreator
import rs09.game.system.SystemLogger
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
                    SystemLogger.logErr(this::class.java, "Pulse execution error. Stack trace below.")
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
                SystemLogger.logWarn(this::class.java, "CRITICALLY long bot-script tick - ${pulse.botScript.javaClass.name} took $elapsedTime ms")
            } else {
                SystemLogger.logWarn(this::class.java, "CRITICALLY long running pulse - ${pulse.javaClass.name} took $elapsedTime ms")
            }
        } else if (elapsedTime >= 30) {
            if (pulse is GeneralBotCreator.BotScriptPulse) {
                SystemLogger.logWarn(this::class.java, "Long bot-script tick - ${pulse.botScript.javaClass.name} took $elapsedTime ms")
            } else {
                SystemLogger.logWarn(this::class.java, "Long running pulse - ${pulse.javaClass.name} took $elapsedTime ms")
            }
        }
    }
}
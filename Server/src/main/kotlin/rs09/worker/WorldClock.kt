package rs09.worker

import api.*
import core.game.system.task.Pulse
import core.plugin.CorePluginTypes.Managers
import gui.GuiEvent
import gui.ServerMonitor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import rs09.Server
import rs09.ServerConstants
import rs09.ServerStore
import rs09.game.ai.general.GeneralBotCreator
import rs09.game.system.SystemLogger
import rs09.game.world.World
import rs09.game.world.repository.Repository
import rs09.game.world.update.UpdateSequence
import rs09.net.packet.PacketWriteQueue
import rs09.tools.stringtools.colorize
import java.lang.Long.max
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

/**
 * Handles the running of pulses and writing of masks, etc
 * @author Ceikry
 */
class WorldClock {
    var started = false
    private val sdf = SimpleDateFormat("HHmmss")
    private val worker = Thread {
        Thread.currentThread().name = "Major Update Worker"
        started = true
        Thread.sleep(600L)
        while(true){
            val start = System.currentTimeMillis()
            try {
                World.tick()
            } catch(e: Exception){
                SystemLogger.logErr(e.stackTraceToString())
            }
            //Handle daily restart if enabled
            if(sdf.format(Date()).toInt() == 0){

                if(checkDay() == 1) {//monday
                    ServerStore.clearWeeklyEntries()
                }

                ServerStore.clearDailyEntries()
                if(ServerConstants.DAILY_RESTART) {
                    sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN 5 MINUTES!"))
                    ServerConstants.DAILY_RESTART = false
                    submitWorldPulse(object : Pulse(100) {
                        var counter = 0
                        override fun pulse(): Boolean {
                            counter++
                            if (counter == 5) {
                                exitProcess(0)
                            }
                            sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN ${5 - counter} MINUTE${if (counter < 4) "S" else ""}!"))
                            return false
                        }
                    })
                }
            }
            val end = System.currentTimeMillis()
            ServerMonitor.eventQueue.add(GuiEvent.UpdateTickTime(end - start))
            ServerMonitor.eventQueue.add(GuiEvent.UpdatePulseCount(World.Pulser.TASKS.size))
            Thread.sleep(max(600 - (end - start), 0))
        }
    }

    fun start() {
        if(!started){
            worker.start()
        }

        if (ServerConstants.ALLOW_GUI)
            ServerMonitor.open()
    }

    fun checkDay(): Int {
        val weeklySdf = SimpleDateFormat("u")
        return weeklySdf.format(Date()).toInt()
    }
}
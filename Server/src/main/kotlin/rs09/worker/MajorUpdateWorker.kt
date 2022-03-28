package rs09.worker

import api.submitWorldPulse
import core.game.system.SystemManager
import core.game.system.SystemState
import core.game.system.task.Pulse
import core.plugin.CorePluginTypes.Managers
import gui.GuiEvent
import gui.ServerMonitor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rs09.Server
import rs09.ServerConstants
import rs09.ServerStore
import rs09.game.ai.general.GeneralBotCreator
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import rs09.game.world.update.UpdateSequence
import rs09.net.packet.PacketWriteQueue
import rs09.tools.stringtools.colorize
import java.lang.Long.max
import java.lang.Long.min
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

/**
 * Handles the running of pulses and writing of masks, etc
 * @author Ceikry
 */
class MajorUpdateWorker {
    var started = false
    val sequence = UpdateSequence()
    val sdf = SimpleDateFormat("HHmmss")
    val worker = Thread {
        Thread.currentThread().name = "Major Update Worker"
        started = true
        Thread.sleep(600L)
        while(true){
            val start = System.currentTimeMillis()
            val rmlist = ArrayList<Pulse>()
            val list = ArrayList(GameWorld.Pulser.TASKS)
            Server.heartbeat()

            GlobalScope.launch {
                //run our pulses
                for (pulse in list) {
                    val b = System.currentTimeMillis()
                    if (pulse == null || pulse.update()) rmlist.add(pulse)

                    val time = System.currentTimeMillis() - b

                    if (time >= 100) {
                        if (pulse is GeneralBotCreator.BotScriptPulse) {
                            SystemLogger.logWarn("CRITICALLY Long Botscript Tick: ${pulse.botScript.javaClass.name} - $time ms")
                        } else {
                            SystemLogger.logWarn("CRITICALLY long running pulse: ${pulse.javaClass.name} - $time ms")
                        }
                    } else if (time >= 30) {
                        if (pulse is GeneralBotCreator.BotScriptPulse) {
                            SystemLogger.logWarn("Long Botscript Tick: ${pulse.botScript.javaClass.name} - $time ms")
                        } else {
                            SystemLogger.logWarn("Long Running Pulse: ${pulse.javaClass.name} - $time ms")
                        }
                    }
                }

                //remove all null or finished pulses from the list
                rmlist.forEach {
                    if (GameWorld.Pulser.TASKS.contains(it)) GameWorld.Pulser.TASKS.remove(it)
                }

                rmlist.clear()
                //perform our update sequence where we write masks, etc
                try {
                    sequence.start()
                    sequence.run()
                    sequence.end()
                    GlobalScope.launch {
                        PacketWriteQueue.flush()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //increment global ticks variable
                GameWorld.pulse()
                //disconnect all players waiting to be disconnected
                Repository.disconnectionQueue.update()
                GameWorld.tickListeners.forEach { it.tick() }
                //tick all manager plugins
                Managers.tick()
            }

            //Handle daily restart if enabled
            if(sdf.format(Date()).toInt() == 0){

                if(GameWorld.checkDay() == 1) {//monday
                    ServerStore.clearWeeklyEntries()
                }

                ServerStore.clearDailyEntries()
                if(ServerConstants.DAILY_RESTART ) {
                    Repository.sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN 5 MINUTES!"))
                    ServerConstants.DAILY_RESTART = false
                    submitWorldPulse(object : Pulse(100) {
                        var counter = 0
                        override fun pulse(): Boolean {
                            counter++
                            if (counter == 5) {
                                exitProcess(0)
                            }
                            Repository.sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN ${5 - counter} MINUTE${if (counter < 4) "S" else ""}!"))
                            return false
                        }
                    })
                }
            }

            val end = System.currentTimeMillis()
/*            ServerMonitor.eventQueue.add(GuiEvent.UpdateTickTime(end - start))
            ServerMonitor.eventQueue.add(GuiEvent.UpdatePulseCount(GameWorld.Pulser.TASKS.size))*/
            Thread.sleep(max(600 - (end - start), 0))
        }
    }

    fun start() {
        if(!started){
            worker.start()
        }

        //if (ServerConstants.ALLOW_GUI)
        //    ServerMonitor.open()
    }
}
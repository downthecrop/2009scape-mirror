package rs09.worker

import api.ContentAPI
import core.game.system.SystemManager
import core.game.system.SystemState
import core.game.system.task.Pulse
import core.plugin.CorePluginTypes.Managers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rs09.Server
import rs09.ServerConstants
import rs09.ServerStore
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
    fun start() = GlobalScope.launch {
        started = true
        delay(600L)
        while(true){
            val start = System.currentTimeMillis()
            val rmlist = ArrayList<Pulse>()
            val list = ArrayList(GameWorld.Pulser.TASKS)
            Server.heartbeat()

            //run our pulses
            for(pulse in list) {
                if (pulse == null || pulse.update()) rmlist.add(pulse)
            }

            //remove all null or finished pulses from the list
            rmlist.forEach {
                if(GameWorld.Pulser.TASKS.contains(it)) GameWorld.Pulser.TASKS.remove(it)
            }

            rmlist.clear()
            //perform our update sequence where we write masks, etc
            try {
                sequence.start()
                sequence.run()
                sequence.end()
                PacketWriteQueue.flush()
            } catch (e: Exception){
                e.printStackTrace()
            }
            //increment global ticks variable
            GameWorld.pulse()
            //disconnect all players waiting to be disconnected
            Repository.disconnectionQueue.update()
            //tick all manager plugins
            Managers.tick()

            //Handle daily restart if enabled
            if(sdf.format(Date()).toInt() == 0){

                if(GameWorld.checkDay() == 7) {//sunday
                    ServerStore.clearWeeklyEntries()
                }

                ServerStore.clearDailyEntries()
                if(ServerConstants.DAILY_RESTART ) {
                    Repository.sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN 5 MINUTES!"))
                    ServerConstants.DAILY_RESTART = false
                    ContentAPI.submitWorldPulse(object : Pulse(100) {
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
            delay(max(600 - (end - start), 0))
        }
    }
}
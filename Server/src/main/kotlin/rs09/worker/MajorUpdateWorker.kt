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
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import rs09.game.world.update.UpdateSequence
import rs09.tools.stringtools.colorize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Handles the running of pulses and writing of masks, etc
 * @author Ceikry
 */
class MajorUpdateWorker {
    var started = false
    val sequence = UpdateSequence()
    val sdf = SimpleDateFormat("HHmm")
    fun start() = GlobalScope.launch {
        started = true
        while(true){
            delay(600L)
            val rmlist = ArrayList<Pulse>()
            val list = ArrayList(GameWorld.Pulser.TASKS)

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
            sequence.start()
            sequence.run()
            sequence.end()
            //increment global ticks variable
            GameWorld.pulse()
            //disconnect all players waiting to be disconnected
            Repository.disconnectionQueue.update()
            //tick all manager plugins
            Managers.tick()
            Server.heartbeat()

            //Handle daily restart if enabled
            if(ServerConstants.DAILY_RESTART && sdf.format(Date()).toInt() == 0){
                Repository.sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN 5 MINUTES!"))
                ServerConstants.DAILY_RESTART = false
                ContentAPI.submitWorldPulse(object : Pulse(100) {
                    var counter = 0
                    override fun pulse(): Boolean {
                        counter++
                        if(counter == 5){
                            SystemManager.flag(SystemState.TERMINATED)
                            return true
                        }
                        Repository.sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN ${5 - counter} MINUTE${if(counter < 4) "S" else ""}!"))
                        return false
                    }
                })
            }
        }
    }
}
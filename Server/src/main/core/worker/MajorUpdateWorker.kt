package core.worker

import core.api.submitWorldPulse
import core.game.system.task.Pulse
import core.plugin.CorePluginTypes.Managers
import core.Server
import core.ServerConstants
import core.ServerStore
import core.tools.SystemLogger
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.game.world.update.UpdateSequence
import core.net.packet.PacketProcessor
import core.net.packet.PacketWriteQueue
import core.tools.colorize
import java.lang.Long.max
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * Handles the running of pulses and writing of masks, etc
 * @author Ceikry
 */
class MajorUpdateWorker {
    var running: Boolean = false
    var started = false
    val sequence = UpdateSequence()
    val sdf = SimpleDateFormat("HHmmss")
    val worker = Thread {
        Thread.currentThread().name = "Major Update Worker"
        started = true
        Thread.sleep(600L)
        while (running) {
            val start = System.currentTimeMillis()
            Server.heartbeat()

            handleTickActions()

            for (player in Repository.players.filter { !it.isArtificial }) {
                if (System.currentTimeMillis() - player.session.lastPing > 20000L) {
                    player?.details?.session?.disconnect()
                    player?.session?.lastPing = Long.MAX_VALUE
                    player?.clear(true)
                }
            }

            //Handle daily restart if enabled
            if (sdf.format(Date()).toInt() == 0) {

                if (GameWorld.checkDay() == 1) {//monday
                    ServerStore.clearWeeklyEntries()
                }

                ServerStore.clearDailyEntries()
                if (ServerConstants.DAILY_RESTART) {
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

        SystemLogger.logInfo(this::class.java, "Update worker stopped.")
    }

    fun handleTickActions(skipPulseUpdate: Boolean = false) {
        PacketProcessor.processQueue()

        if (!skipPulseUpdate) {
            GameWorld.Pulser.updateAll()
        }
        GameWorld.tickListeners.forEach { it.tick() }

        try {
            sequence.start()
            sequence.run()
            sequence.end()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //increment global ticks variable
        GameWorld.pulse()
        //disconnect all players waiting to be disconnected
        Repository.disconnectionQueue.update()
        //tick all manager plugins
        Managers.tick()

        PacketWriteQueue.flush()
    }

    fun start() {
        if (!started) {
            running = true
            worker.start()
        }
    }

    fun stop() {
        running = false
        worker.interrupt()
    }
}
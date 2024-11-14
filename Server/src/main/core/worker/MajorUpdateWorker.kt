package core.worker

import core.Server
import core.ServerConstants
import core.ServerStore
import core.api.log
import core.api.submitWorldPulse
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.game.world.update.UpdateSequence
import core.integrations.grafana.Grafana
import core.net.packet.PacketProcessor
import core.net.packet.PacketWriteQueue
import core.plugin.CorePluginTypes.Managers
import core.tools.Log
import core.tools.NetworkReachability
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
            Grafana.startTick()
            val start = System.currentTimeMillis()
            Server.heartbeat()

            if (Server.networkReachability == NetworkReachability.Reachable)
                handleTickActions()
            else
                tickOffline()

            for (player in Repository.players.filter { !it.isArtificial }) {
                if (System.currentTimeMillis() - player.session.lastPing > 20000L) {
                    player?.session?.lastPing = Long.MAX_VALUE
                    player?.session?.disconnect()
                }
                if (!player.isActive && !Repository.disconnectionQueue.contains(player.name) && player.getAttribute("logged-in-fully", false)) {
                    //if player has somehow been set as inactive without being queued for disconnection, do that now. This is a failsafe, and should not be relied on.
                    //if you made a change, and now this is suddenly getting triggered a lot, your change is probably bad.
                    player?.session?.disconnect()
                    log(MajorUpdateWorker::class.java, Log.WARN, "Manually disconnecting ${player.name} because they were set as inactive without being disconnected. This is bad.")
                }
            }

            //Handle daily restart if enabled
            if (sdf.format(Date()).toInt() == 0) {

                if (GameWorld.checkDay() == 1) {//monday
                    ServerStore.clearWeeklyEntries()
                }

                ServerStore.clearDailyEntries()
                if (ServerConstants.DAILY_RESTART) {
                    for (player in Repository.players.filter { !it.isArtificial }) {
                        player.packetDispatch.sendSystemUpdate(500)
                    }
                    Repository.sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN 5 MINUTES!"))
                    ServerConstants.DAILY_RESTART = false
                    submitWorldPulse(object : Pulse(100) {
                        var counter = 0
                        override fun pulse(): Boolean {
                            counter++
                            for (player in Repository.players.filter { !it.isArtificial }) {
                                player.packetDispatch.sendSystemUpdate((5 - counter) * 100)
                            }
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
            Grafana.totalTickTime = (end - start).toInt()
            Grafana.endTick()
/*            ServerMonitor.eventQueue.add(GuiEvent.UpdateTickTime(end - start))
            ServerMonitor.eventQueue.add(GuiEvent.UpdatePulseCount(GameWorld.Pulser.TASKS.size))*/
            Thread.sleep(max(600 - (end - start), 0))
        }

        log(this::class.java, Log.FINE,  "Update worker stopped.")
    }

    fun tickOffline()
    {
        Repository.disconnectionQueue.update() //continue processing disconnection queue
        GameWorld.pulse() //continue incrementing the global tick count
    }

    fun handleTickActions(skipPulseUpdate: Boolean = false) {
        try {
            var packetStart = System.currentTimeMillis()
            PacketProcessor.processQueue()
            Grafana.packetProcessTime = (System.currentTimeMillis() - packetStart).toInt()

            //disconnect all players waiting to be disconnected
            Repository.disconnectionQueue.update()

            if (!skipPulseUpdate) {
                GameWorld.Pulser.updateAll()
            }
            GameWorld.tickListeners.forEach { it.tick() }

            sequence.start()
            sequence.run()
            sequence.end()

            //increment global ticks variable
            GameWorld.pulse()
            //tick all manager plugins
            Managers.tick()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                PacketWriteQueue.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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

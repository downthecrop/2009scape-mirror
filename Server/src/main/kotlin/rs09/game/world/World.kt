package rs09.game.world

import api.ContentAPI
import core.cache.Cache
import core.cache.def.impl.SceneryDefinition
import core.game.ge.GrandExchangeDatabase
import core.game.node.entity.npc.drop.RareDropTable
import core.game.node.entity.player.Player
import core.game.system.SystemManager
import core.game.system.SystemState
import core.game.system.task.Pulse
import core.game.system.task.TaskExecutor
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.CorePluginTypes.Managers
import core.plugin.CorePluginTypes.StartupPlugin
import core.tools.RandomFunction
import core.tools.mysql.DatabaseManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import rs09.Server
import rs09.game.ai.general.scriptrepository.PlayerScripts
import rs09.ServerConstants
import rs09.ServerStore
import rs09.game.ai.general.GeneralBotCreator
import rs09.game.node.entity.state.newsys.StateRepository
import rs09.game.system.SystemLogger
import rs09.game.system.SystemLogger.logInfo
import rs09.game.system.config.ConfigParser
import rs09.game.world.callback.CallbackHub
import rs09.game.world.repository.Repository
import rs09.game.world.update.UpdateSequence
import rs09.net.packet.PacketWriteQueue
import rs09.plugin.PluginManager
import rs09.tools.stringtools.colorize
import rs09.worker.WorldClock
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer
import kotlin.system.exitProcess

/**
 * Represents the game world.
 * @author Ceikry
 */
object World {
    @JvmStatic
    val clock = WorldClock()

    @JvmStatic
    val STARTUP_PLUGINS: List<StartupPlugin> = ArrayList()

    @JvmStatic
    var PCnBotsSpawned = false

    @JvmStatic
    var PCiBotsSpawned = false

    @JvmStatic
    var settings: WorldSettings? = null

    @JvmStatic
    var ticks = 0

    @JvmStatic
    var databaseManager: DatabaseManager? = null
        private set

    val sequence = UpdateSequence()

    @JvmStatic
    var Pulser = PulseRunner()

    /**
     * Submits a pulse.
     *
     * @param pulse the pulse.
     */
    @Deprecated("", ReplaceWith("Pulser.submit(pulse!!)", "core.game.world.GameWorld.Pulser"))
    fun submit(pulse: Pulse?) {
        Pulser.submit(pulse!!)
    }

    fun tick() {
        ticks++
        if (ticks % 50 == 0) {
            TaskExecutor.execute {
                val player = Repository.players
                try {
                    player.stream().filter { obj: Player? -> Objects.nonNull(obj) }.filter { p: Player -> !p.isArtificial && p.isPlaying }.forEach { p: Player? -> Repository.disconnectionQueue.save(p!!, false) }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }

        val rmlist = ArrayList<Pulse>()
        val list = ArrayList(Pulser.TASKS)

        //run our pulses
        for(pulse in list) {
            val b = System.currentTimeMillis()
            if (pulse == null || pulse.update()) rmlist.add(pulse)

            val time = System.currentTimeMillis() - b

            if(time >= 100){
                if(pulse is GeneralBotCreator.BotScriptPulse){
                    SystemLogger.logWarn("CRITICALLY Long Botscript Tick: ${pulse.botScript.javaClass.name} - $time ms")
                } else {
                    SystemLogger.logWarn("CRITICALLY long running pulse: ${pulse.javaClass.name} - $time ms")
                }
            }
            else if(time >= 30){
                if(pulse is GeneralBotCreator.BotScriptPulse){
                    SystemLogger.logWarn("Long Botscript Tick: ${pulse.botScript.javaClass.name} - $time ms")
                } else {
                    SystemLogger.logWarn("Long Running Pulse: ${pulse.javaClass.name} - $time ms")
                }
            }
        }

        //remove all null or finished pulses from the list
        rmlist.forEach {
            if(Pulser.TASKS.contains(it)) World.Pulser.TASKS.remove(it)
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
        } catch (e: Exception){
            e.printStackTrace()
        }
        //disconnect all players waiting to be disconnected
        Repository.disconnectionQueue.update()
        //tick all manager plugins
        Managers.tick()
    }

    /**
     * Checks if its the economy world.
     *
     * @return `True` if so.
     */
    @JvmStatic
    val isEconomyWorld: Boolean
        get() = false

}
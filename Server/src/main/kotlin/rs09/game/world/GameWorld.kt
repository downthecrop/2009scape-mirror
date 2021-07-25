package rs09.game.world

import core.cache.Cache
import core.cache.AriosStore
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
import core.plugin.CorePluginTypes.StartupPlugin
import core.tools.RandomFunction
import core.tools.mysql.DatabaseManager
import rs09.game.ai.general.scriptrepository.PlayerScripts
import rs09.ServerConstants
import rs09.game.node.entity.state.newsys.StateRepository
import rs09.game.system.SystemLogger
import rs09.game.system.SystemLogger.logInfo
import rs09.game.system.config.ConfigParser
import rs09.game.world.callback.CallbackHub
import rs09.game.world.repository.Repository
import rs09.plugin.PluginManager
import rs09.worker.MajorUpdateWorker
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer

/**
 * Represents the game world.
 * @author Ceikry
 */
object GameWorld {

    /**
     * The major update worker.
     */
    @JvmStatic
    val majorUpdateWorker = MajorUpdateWorker()

    @JvmStatic
    val STARTUP_PLUGINS: List<StartupPlugin> = ArrayList()
    private val configParser = ConfigParser()
    @JvmStatic
    var PCBotsSpawned = false
    @JvmStatic
    var PCnBotsSpawned = false
    @JvmStatic
    var PCiBotsSpawned = false
    /**
     * The game settings to use.
     */
    @JvmStatic
    var settings: GameSettings? = null
    /**
     * The current amount of (600ms) cycles elapsed.
     */
    @JvmStatic
    var ticks = 0
    @JvmStatic
    var databaseManager: DatabaseManager? = null
        private set

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

    fun pulse() {
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
    }

    fun checkDay(): Int {
        val weeklySdf = SimpleDateFormat("u")
        return weeklySdf.format(Date()).toInt()
    }

    /**
     * Prompts the [GameWorld] to begin it's initialization.
     *
     * @param directory the directory to the properties.
     * @throws Throwable when the exception occurs.
     */
    @Throws(Throwable::class)
    fun prompt(directory: String?) {
        prompt(true, directory)
    }

    /**
     * Prompts the game world.
     *
     * @param running if running.
     * @throws Throwable the throwable.
     */
    @Throws(Throwable::class)
    @JvmStatic
    fun prompt(running: Boolean) {
        prompt(running, "server.properties")
    }

    /**
     * Prompts the [GameWorld] to begin its initialization.
     *
     * @param run       If the server should be running.
     * @param directory the path to the dir.
     * @throws Throwable When an exception occurs.
     */
    @Throws(Throwable::class)
    fun prompt(run: Boolean, directory: String?){
        SystemLogger.logInfo("Prompting ${settings?.name} Game World...")
        Cache.init(ServerConstants.CACHE_PATH)
        AriosStore.init(ServerConstants.STORE_PATH)
        databaseManager = DatabaseManager(ServerConstants.DATABASE)
        databaseManager!!.connect()
        GrandExchangeDatabase.init()
        configParser.prePlugin()
        PluginManager.init()
        configParser.postPlugin()
        RareDropTable.init()
        logInfo("Initialized Rare Drop Table from " + ServerConstants.RDT_DATA_PATH)
        if (settings!!.enable_bots) {
            ImmerseWorld.init()
        }
        CallbackHub.call()
        STARTUP_PLUGINS.forEach(Consumer { plugin: StartupPlugin? ->
            plugin?.run()
        })
        if (run) {
            SystemManager.flag(if (settings?.isDevMode == true) SystemState.PRIVATE else SystemState.ACTIVE)
        }
        SceneryDefinition.getDefinitions().values.forEach(Consumer { obj: SceneryDefinition -> obj.examine })
        System.gc()
        PlayerScripts.init()
        StateRepository.init()
        SystemLogger.initTradeLogger()
    }

    /**
     * Called when the server shuts down.
     *
     * @throws Throwable When an exception occurs.
     */
    @Throws(Throwable::class)
    fun shutdown() {
        SystemManager.flag(SystemState.TERMINATED)
    }

    /**
     * Checks if its the economy world.
     *
     * @return `True` if so.
     */
    @JvmStatic
    val isEconomyWorld: Boolean
        get() = false

    private fun generateLocation(): Location {
        val random_location = Location(3075 + RandomFunction.random(-15, 15), 3954 + RandomFunction.random(-15, 15), 0)
        if (!RegionManager.isTeleportPermitted(random_location)) {
            return generateLocation()
        }
        return if (RegionManager.getObject(random_location) != null) {
            generateLocation()
        } else random_location
    }
}
package rs09

import core.game.system.SystemManager
import core.game.system.SystemState
import core.game.system.mysql.SQLManager
import core.gui.ConsoleFrame
import core.net.NioReactor
import core.net.amsc.WorldCommunicator
import core.tools.TimeStamp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rs09.game.ge.GEAutoStock
import rs09.game.system.SystemLogger
import rs09.game.system.config.ServerConfigParser
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import java.io.File
import java.net.BindException
import java.util.*
import kotlin.system.exitProcess

/**
 * The main class, for those that are unable to read the class' name.
 * @author Emperor
 * @author Vexia
 * @author Ceikry
 */
object Server {
    /**
     * The time stamp of when the server started running.
     */
	@JvmField
	var startTime: Long = 0

    var lastHeartbeat = System.currentTimeMillis()

    var running = false

    /**
     * The NIO reactor.
     */
    var reactor: NioReactor? = null

    /**
     * The main method, in this method we load background utilities such as
     * cache and our world, then end with starting networking.
     * @param args The arguments cast on runtime.
     * @throws Throwable When an exception occurs.
     */
    @Throws(Throwable::class)
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isNotEmpty()) {
            SystemLogger.logInfo("Using config file: ${args[0]}")
            ServerConfigParser(args[0])
        } else {
            SystemLogger.logInfo("Using config file: ${"worldprops" + File.separator + "default.json"}")
            ServerConfigParser("worldprops" + File.separator + "default.json")
        }
        if (GameWorld.settings?.isGui == true) {
            try {
                ConsoleFrame.getInstance().init()
            } catch (e: Exception) {
                SystemLogger.logWarn("X11 server missing - launching server with no GUI!")
            }
        }
        startTime = System.currentTimeMillis()
        val t = TimeStamp()
        GameWorld.prompt(true)
        SQLManager.init()
        Runtime.getRuntime().addShutdownHook(ServerConstants.SHUTDOWN_HOOK)
        SystemLogger.logInfo("Starting networking...")
        try {
            NioReactor.configure(43594 + GameWorld.settings?.worldId!!).start()
        } catch (e: BindException) {
            SystemLogger.logErr("Port " + (43594 + GameWorld.settings?.worldId!!) + " is already in use!")
            throw e
        }
        WorldCommunicator.connect()
        SystemLogger.logInfo(GameWorld.settings?.name + " flags " + GameWorld.settings?.toString())
        SystemLogger.logInfo(GameWorld.settings?.name + " started in " + t.duration(false, "") + " milliseconds.")

        GEAutoStock.autostock()
        val scanner = Scanner(System.`in`)
        running = true
        GlobalScope.launch {
            while(scanner.hasNextLine()){
                val command = scanner.nextLine()
                when(command){
                    "stop" -> SystemManager.flag(SystemState.TERMINATED)
                    "players" -> System.out.println("Players online: " + (Repository.LOGGED_IN_PLAYERS.size))
                    "update" -> SystemManager.flag(SystemState.UPDATING)
                    "help","commands" -> printCommands()
                    "restartworker" -> SystemManager.flag(SystemState.ACTIVE)

                }
            }
        }
        GlobalScope.launch {
            delay(20000)
            while(running){
                if(System.currentTimeMillis() - lastHeartbeat > 1800){
                    running = false
                    exitProcess(0)
                }
                delay(625)
            }
        }
    }

    @JvmStatic
    fun heartbeat() {
        lastHeartbeat = System.currentTimeMillis()
    }

    fun printCommands(){
        println("stop - stop the server (saves all accounts and such)")
        println("players - show online player count")
        println("update - initiate an update with a countdown visible to players")
        println("help, commands - show this")
        println("restartworker - Reboots the major update worker in case of a travesty.")
    }

    fun autoReconnect() {
        /*SystemLogger.log("Attempting autoreconnect of server")
        WorldCommunicator.connect()*/
    }
    /**
     * Gets the startTime.
     * @return the startTime
     */
    fun getStartTime(): Long {
        return startTime
    }

    /**
     * Sets the bastartTime.ZZ
     * @param startTime the startTime to set.
     */
    fun setStartTime(startTime: Long) {
        Server.startTime = startTime
    }
}

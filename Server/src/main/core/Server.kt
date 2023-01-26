package core

import core.game.system.SystemManager
import core.game.system.SystemState
import core.net.NioReactor
import core.tools.TimeStamp
import kotlinx.coroutines.*
import core.tools.SystemLogger
import core.game.system.config.ServerConfigParser
import core.game.world.GameWorld
import core.game.world.repository.Repository
import java.io.File
import java.io.FileWriter
import java.lang.management.ManagementFactory
import java.lang.management.ThreadMXBean
import java.net.BindException
import java.util.*
import kotlin.system.exitProcess


/**
 * The main class, for those that are unable to read the class' name.
 * @author Emperor
 * @author Ceikry
 */
object Server {
    /**
     * The time stamp of when the server started running.
     */
    @JvmField
    var startTime: Long = 0

    var lastHeartbeat = System.currentTimeMillis()

    @JvmStatic
    var running = false

    /**
     * The NIO reactor.
     */
    @JvmStatic
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
            SystemLogger.logInfo(this::class.java, "Using config file: ${args[0]}")
            ServerConfigParser.parse(args[0])
        } else {
            SystemLogger.logInfo(this::class.java, "Using config file: ${"worldprops" + File.separator + "default.conf"}")
            ServerConfigParser.parse("worldprops" + File.separator + "default.conf")
        }
        startTime = System.currentTimeMillis()
        val t = TimeStamp()
        GameWorld.prompt(true)
        Runtime.getRuntime().addShutdownHook(ServerConstants.SHUTDOWN_HOOK)
        SystemLogger.logInfo(this::class.java, "Starting networking...")
        try {
            reactor = NioReactor.configure(43594 + GameWorld.settings?.worldId!!)
            reactor!!.start()
        } catch (e: BindException) {
            SystemLogger.logErr(this::class.java, "Port " + (43594 + GameWorld.settings?.worldId!!) + " is already in use!")
            throw e
        }
        //WorldCommunicator.connect()
        SystemLogger.logInfo(this::class.java, GameWorld.settings?.name + " flags " + GameWorld.settings?.toString())
        SystemLogger.logInfo(this::class.java, GameWorld.settings?.name + " started in " + t.duration(false, "") + " milliseconds.")
        val scanner = Scanner(System.`in`)

        running = true
        GlobalScope.launch {
            while(scanner.hasNextLine()){
                val command = scanner.nextLine()
                when(command){
                    "stop" -> exitProcess(0)
                    "players" -> System.out.println("Players online: " + (Repository.LOGGED_IN_PLAYERS.size))
                    "update" -> SystemManager.flag(SystemState.UPDATING)
                    "help","commands" -> printCommands()
                    "restartworker" -> SystemManager.flag(SystemState.ACTIVE)

                }
            }
        }

        if (ServerConstants.WATCHDOG_ENABLED) {
            GlobalScope.launch {
                delay(20000)
                while (running) {
                    if (System.currentTimeMillis() - lastHeartbeat > 7200 && running) {
                        SystemLogger.logErr(this::class.java, "Triggering reboot due to heartbeat timeout")
                        SystemLogger.logErr(this::class.java, "Creating thread dump...")
                        val dump = threadDump(true, true)

                        withContext(Dispatchers.IO) {
                            FileWriter("latestdump.txt").use {

                                if (dump != null) {
                                    it.write(dump)
                                }

                                it.flush()
                                it.close()
                            }
                        }

                        if (!SystemManager.isTerminated())
                            exitProcess(0)
                    }
                    delay(625)
                }
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

    private fun threadDump(lockedMonitors: Boolean, lockedSynchronizers: Boolean): String? {
        val threadDump = StringBuffer(System.lineSeparator())
        val threadMXBean: ThreadMXBean = ManagementFactory.getThreadMXBean()
        for (threadInfo in threadMXBean.dumpAllThreads(lockedMonitors, lockedSynchronizers)) {
            threadDump.append(threadInfo.toString())
        }
        return threadDump.toString()
    }

    /**
     * Sets the bastartTime.ZZ
     * @param startTime the startTime to set.
     */
    fun setStartTime(startTime: Long) {
        Server.startTime = startTime
    }
}

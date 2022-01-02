package rs09

import core.cache.Cache
import core.game.ge.GrandExchangeDatabase
import core.game.interaction.`object`.dmc.DMCHandler
import core.game.node.entity.npc.drop.RareDropTable
import core.game.system.mysql.SQLManager
import core.game.world.map.zone.ZoneBuilder
import core.net.IoEventHandler
import core.net.IoSession
import core.net.ServerSocketConnection
import core.plugin.CorePluginTypes.StartupPlugin
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rs09.game.node.entity.state.newsys.StateRepository
import rs09.game.system.SystemLogger
import rs09.game.system.config.ConfigParser
import rs09.game.system.config.ServerConfigParser
import rs09.game.world.ImmerseWorld
import rs09.game.world.World
import rs09.game.world.callback.CallbackHub
import rs09.game.world.repository.Repository
import rs09.game.world.repository.Repository.players
import rs09.net.ms.ManagementServer
import rs09.plugin.PluginManager
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.management.ManagementFactory
import java.lang.management.ThreadMXBean
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.exitProcess

/**
 * The primary processing backbone for the server.
 * @author Ceikry
 */
object Server {
    /**
     * Various fields used for handling and processing incoming connections
     */
    private var service: ExecutorService? = null
    private var channel: ServerSocketConnection? = null
    private var eventHandler: IoEventHandler? = null
    var lastHeartbeat = System.currentTimeMillis()

    /**
     * The current server state.
     * In BOOTING state, no networking is active. The only thing taking place is the parsing of cache and config data.
     * In RUNNING state, networking is active and primary gameplay loops are being ran.
     * In SHUTDOWN state, players are being processed and disconnected, and things are being cleaned up. Networking is disabled at this time.
     * Nothing occurs in CLOSED state because that indicates the server is DONE running, entirely, and should be shutdown at this time automatically.
     */
    var state = ServerState.BOOTING
        @JvmStatic
        set(value) {
            field = value
            if(channel != null){
                channel!!.selector.wakeup() //Wakes up the selector and triggers the state update
            }
        }

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isNotEmpty()) {
            SystemLogger.logInfo("Using config file: ${args[0]}")
            ServerConfigParser.parse(args[0])
        } else {
            SystemLogger.logInfo("Using config file: ${"worldprops" + File.separator + "default.conf"}")
            ServerConfigParser.parse("worldprops" + File.separator + "default.conf")
        }

        GlobalScope.launch {
            delay(20000)
            lastHeartbeat = System.currentTimeMillis()
            while(true){
                if(System.currentTimeMillis() - lastHeartbeat > 7200 && state == ServerState.RUNNING){
                    SystemLogger.logErr("Triggering reboot due to heartbeat timeout")
                    SystemLogger.logErr("Creating thread dump...")
                    val dump = threadDump(true, true)
                    FileWriter("latestdump.txt").use {
                        it.write(dump)
                        it.flush()
                        it.close()
                    }
                    exitProcess(0)
                }
                delay(625)
            }
        }

        while (state != ServerState.CLOSED) {
            when (state) {

                ServerState.BOOTING -> {
                    val start = System.nanoTime()
                    Cache.init(ServerConstants.CACHE_PATH)
                    ConfigParser().prePlugin()
                    PluginManager.init()
                    ConfigParser().postPlugin()
                    ZoneBuilder.init()
                    ServerStore.init()
                    RareDropTable.init()
                    if(World.settings!!.enable_bots) {
                        ImmerseWorld.init()
                    }
                    CallbackHub.call()
                    StateRepository.init()
                    GrandExchangeDatabase.init()
                    World.STARTUP_PLUGINS.forEach { plugin: StartupPlugin? ->
                        plugin?.run()
                    }
                    System.gc() //Cleanup any abandoned references
                    SystemLogger.initTradeLogger() //Start up the trade logger
                    World.clock.start() //Start the world clock, which begins ticking the game
                    Runtime.getRuntime().addShutdownHook(ServerConstants.SHUTDOWN_HOOK)
                    SQLManager.init()
                    state = ServerState.RUNNING
                    val finish = System.nanoTime()

                    SystemLogger.logInfo("Server started in ${(finish - start) / 1000000} ms.")
                }

                ServerState.RUNNING -> {
                    //Start up networking
                    if(service == null || channel == null || eventHandler == null){
                        service = Executors.newSingleThreadScheduledExecutor()
                        eventHandler = IoEventHandler(Executors.newFixedThreadPool(1))

                        val socket = ServerSocketChannel.open()
                        val selector = Selector.open()
                        socket.bind(InetSocketAddress(World.settings!!.worldId + 43594))
                        socket.configureBlocking(false)
                        socket.register(selector, SelectionKey.OP_ACCEPT)
                        channel = ServerSocketConnection(selector, socket)
                        ManagementServer.connect()
                    }

                    try {
                        channel!!.selector.select() //wait for a connection to be ready
                    } catch (e: IOException) {e.printStackTrace()}

                    val keyIterator = channel!!.selector.selectedKeys().iterator()

                    while(keyIterator.hasNext()){
                        val key = keyIterator.next()
                        keyIterator.remove()
                        try {
                            if (!key.isValid || !key.channel().isOpen) {
                                key.cancel()
                                continue
                            }

                            if (key.isConnectable) eventHandler!!.connect(key)
                            if (key.isAcceptable) eventHandler!!.accept(key, channel!!.selector)

                            if (key.isReadable) {
                                eventHandler!!.read(key)
                            } else if (key.isWritable) {
                                eventHandler!!.write(key)
                            }
                        } catch (t: Throwable){
                            val session = key.attachment() as? IoSession ?: continue
                            session.disconnect()
                            if(session.player != null){
                                Repository.disconnectionQueue.add(session.player, true)
                            }
                        }
                    }
                }


                ServerState.SHUTDOWN -> {
                    if(players.any { !it.isArtificial }){
                        for(player in players.filter { !it.isArtificial }){
                            Repository.disconnectionQueue.add(player, true)
                        }
                        Repository.disconnectionQueue.update()
                    } else {
                        SystemLogger.flushLogs()
                        ServerStore.save()
                        ManagementServer.disconnect()
                        SystemLogger.logInfo("Shutdown complete.")
                        state = ServerState.CLOSED
                    }
                }

                else -> break
            }
        }

        Runtime.getRuntime().removeShutdownHook(ServerConstants.SHUTDOWN_HOOK)
        exitProcess(0)
    }


    private fun threadDump(lockedMonitors: Boolean, lockedSynchronizers: Boolean): String? {
        val threadDump = StringBuffer(System.lineSeparator())
        val threadMXBean: ThreadMXBean = ManagementFactory.getThreadMXBean()
        for (threadInfo in threadMXBean.dumpAllThreads(lockedMonitors, lockedSynchronizers)) {
            threadDump.append(threadInfo.toString())
        }
        return threadDump.toString()
    }
}

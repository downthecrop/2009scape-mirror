package rs09.net.ms

import core.game.node.entity.player.info.login.Response
import core.net.IoEventHandler
import core.net.IoSession
import core.net.ServerSocketConnection
import core.net.ms.MSPacketRepository
import core.net.producer.MSHSEventProducer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import rs09.game.node.entity.player.info.login.LoginParser
import rs09.game.system.SystemLogger
import rs09.game.world.World
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.concurrent.Executors
import kotlin.Exception
import kotlin.system.exitProcess

/**
 * Server-side model of the management server.
 * Responsible for connecting to and processing interactions with the MS.
 * @author Ceikry
 */
object ManagementServer {
    val service = Executors.newSingleThreadExecutor()
    val eventHandler = IoEventHandler(service)
    var channel: ServerSocketConnection? = null

    @JvmField
    var session: IoSession? = null

    @JvmField
    val handshakeProducer = MSHSEventProducer()

    private val loginAttempts = HashMap<String,LoginParser>()

    /**
     * The current state of our connection with the management server.
     * In DISCONNECTED state, we are not communicating with the MS at all.
     * In CONNECTED state, we have made connection with the MS and are sending and receiving info.
     * In CONNECTING state, we are attempting to form a connection with the MS and are not yet sending or receiving data.
     */
    var state = ManagementState.CONNECTING
        @JvmStatic
        set(value) {
            if(value != ManagementState.DISCONNECTED && (field == ManagementState.DISCONNECTED || field == ManagementState.DISCONNECTING)){
                return
            }
            field = value
            if(channel != null) channel!!.selector.wakeup()
        }

    @JvmStatic
    fun attemptLogin(parser: LoginParser){
        if(state == ManagementState.DISCONNECTED){
            parser.details.session!!.write(Response.LOGIN_SERVER_OFFLINE, true)
            return
        }

        val lastAttempt = loginAttempts[parser.details.username]?.timeStamp ?: -50

        if(World.ticks - lastAttempt < 50){
            SystemLogger.logAlert("User tried to log in too frequently: ${parser.details.username}")
            parser.details.session!!.write(Response.ALREADY_ONLINE, true)
            return
        }

        loginAttempts[parser.details.username] = parser

        GlobalScope.launch {
            if(!parser.details.parse()){
                parser.details.session!!.write(Response.LOGIN_SERVER_OFFLINE, true)
                return@launch
            }
            MSPacketRepository.sendPlayerRegistry(parser)
        }
    }

    @JvmStatic
    fun finishLoginAttempt(username: String): LoginParser? {
        return loginAttempts.remove(username)
    }

    fun connect() {
        SystemLogger.logInfo("[MS] Sending world registration request")
        Thread {
            while(state != ManagementState.DISCONNECTED){
                when(state){

                    ManagementState.CONNECTING, ManagementState.CONNECTED -> {
                        if(session == null || this.channel == null) {
                            val selector = Selector.open()
                            val channel = SocketChannel.open()
                            channel.configureBlocking(false)
                            channel.socket().keepAlive = true
                            channel.socket().tcpNoDelay = true
                            channel.connect(InetSocketAddress(World.settings!!.msAddress, 5555))
                            channel.register(selector, SelectionKey.OP_CONNECT)
                            this.channel = ServerSocketConnection(selector, channel)
                        }

                        try {
                            channel!!.selector.select() //Await a connection response
                        } catch (e: Exception) {e.printStackTrace()}

                        val keyIterator = channel!!.selector.selectedKeys().iterator()
                        while(keyIterator.hasNext()){
                            val key = keyIterator.next()
                            keyIterator.remove()
                            try {
                                if(!key.isValid || !key.channel().isOpen){
                                    key.cancel()
                                    continue
                                }

                                if(key.isConnectable && state == ManagementState.CONNECTING){
                                    val ch = key.channel() as SocketChannel
                                    if(ch.finishConnect()){
                                        key.interestOps(key.interestOps() xor SelectionKey.OP_CONNECT)
                                        key.interestOps(key.interestOps() or SelectionKey.OP_READ)
                                        session = IoSession(key, service)
                                        key.attach(session)
                                        session!!.producer = handshakeProducer
                                        session!!.write(true)
                                    }
                                }

                                if(key.isReadable) eventHandler.read(key)
                                if(key.isWritable) eventHandler.write(key)
                            } catch (e: Exception){
                                key.cancel()
                                if(session != null){
                                    session!!.disconnect()
                                    state = ManagementState.DISCONNECTED
                                }
                            }
                        }
                    }

                    ManagementState.DISCONNECTING -> {
                        if(session != null) session!!.disconnect()
                        if(channel != null) channel!!.socket.close()
                        state = ManagementState.DISCONNECTED
                    }

                    ManagementState.DISCONNECTED -> {}
                }
            }
        }.start()
    }

    @JvmStatic
    fun flagConnected(){
        state = ManagementState.CONNECTED
    }

    @JvmStatic
    fun flagCantConnect(){
        exitProcess(0)
    }

    @JvmStatic
    fun disconnect() {
        state = ManagementState.DISCONNECTING
    }

    @JvmStatic
    fun getStateValue(): Int {
        return when(state){
            ManagementState.CONNECTING -> 1
            else -> 2
        }
    }
}
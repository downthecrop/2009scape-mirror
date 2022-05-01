package rs09.game.node.entity.player.info.login

import api.LoginListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.info.login.LoginConfiguration
import core.game.node.entity.player.info.login.LoginType
import core.game.node.entity.player.info.login.PlayerParser
import core.game.node.entity.player.info.login.Response
import core.game.system.SystemManager
import core.game.system.monitor.PlayerMonitor
import core.game.system.task.Pulse
import core.net.amsc.MSPacketRepository
import core.net.amsc.ManagementServerState
import core.net.amsc.WorldCommunicator
import rs09.auth.AuthResponse
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import rs09.game.world.GameWorld.loginListeners
import rs09.game.world.repository.Repository
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Consumer

/**
 * Parses the login of a player.
 * @author Emperor
 * @author Vexia
 */
class LoginParser(
        /**
         * The player details file.
         */
        val details: PlayerDetails,
        /**
         * The login type.
         */
        private val type: LoginType
) : Runnable {
    /**
     * Gets the player details.
     * @return The player details.
     */

    /**
     * The player in the game, used for reconnect login type.
     */
    private var gamePlayer: Player? = null

    /**
     * Gets the timeStamp.
     * @return the timeStamp
     */
    /**
     * The time stamp.
     */
    val timeStamp: Int
    override fun run() {
        try {
            LOCK.tryLock(1000L, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            println(e)
            LOCK.unlock()
            return
        }
        try {
            if (validateRequest()) {
                handleLogin()
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            try {
                flag(AuthResponse.ErrorLoadingProfile)
                Repository.LOGGED_IN_PLAYERS.remove(details.username)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        LOCK.unlock()
    }

    /**
     * Handles the actual login.
     */
    private fun handleLogin() {
        val p = worldInstance
        val player = p ?: Player(details)
        player.setAttribute("login_type", type)
        if (p != null) { // Reconnecting
            p.updateDetails(details)
            reconnect(p, type)
            return
        }
        initialize(player, false)
    }

    /**
     * Initializes the player.
     * @param player The player.
     * @param reconnect If the player data should be parsed.
     */
    fun initialize(player: Player, reconnect: Boolean) {
        if (reconnect) {
            reconnect(player, type)
            return
        }
        try {
            val parser = PlayerParser.parse(player)
                ?: throw IllegalStateException("Failed parsing save for: " + player.username) //Parse core
            loginListeners.forEach(Consumer { listener: LoginListener -> listener.login(player) }) //Run our login hooks
            parser.runContentHooks() //Run our saved-content-parsing hooks
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            Repository.removePlayer(player)
            Repository.LOGGED_IN_PLAYERS.remove(player.username)
            Repository.lobbyPlayers.remove(player)
            Repository.playerNames.remove(player.name)
            MSPacketRepository.sendPlayerRemoval(player.name)
            flag(AuthResponse.ErrorLoadingProfile)
        }
        //Repository.getPlayerNames().put(player.getName(), player);
        GameWorld.Pulser.submit(object : Pulse(1) {
            override fun pulse(): Boolean {
                try {
                    if (details.session.isActive) {
                        val p = Repository.getPlayerByName(player.name)
                        if (p != null) {
                            p.clear()
                            Repository.playerNames.remove(p.name)
                            Repository.lobbyPlayers.remove(p)
                            Repository.removePlayer(p)
                        }
                        if (!Repository.players.contains(player)) {
                            Repository.addPlayer(player)
                        }
                        player.details.session.setObject(player)
                        flag(AuthResponse.Success)
                        player.init()
                        player.monitor.log(player.details.ipAddress, PlayerMonitor.ADDRESS_LOG)
                        player.monitor.log(player.details.serial, PlayerMonitor.ADDRESS_LOG)
                        player.monitor.log(player.details.macAddress, PlayerMonitor.ADDRESS_LOG)
                    } else {
                        Repository.playerNames.remove(player.name)
                        MSPacketRepository.sendPlayerRemoval(player.name)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                    Repository.removePlayer(player)
                    Repository.LOGGED_IN_PLAYERS.remove(player.username)
                    Repository.lobbyPlayers.remove(player)
                    Repository.playerNames.remove(player.name)
                    MSPacketRepository.sendPlayerRemoval(player.name)
                    flag(AuthResponse.ErrorLoadingProfile)
                }
                return true
            }
        })
    }

    /**
     * Gets the player instance in the current world.
     * @return The player instance, if found.
     */
    private val worldInstance: Player?
        private get() {
            var player = Repository.disconnectionQueue[details.username]
            if (player == null) {
                player = gamePlayer
            }
            return player
        }

    /**
     * Initializes a reconnecting player.
     * @param player The player.
     * @param type The login type.
     */
    private fun reconnect(player: Player, type: LoginType) {
        Repository.disconnectionQueue.remove(details.username)
        player.initReconnect()
        player.isActive = true
        flag(AuthResponse.Success)
        player.updateSceneGraph(true)
        player.configManager.init()
        LoginConfiguration.configureGameWorld(player)
        Repository.playerNames[player.name] = player
        GameWorld.Pulser.submit(object : Pulse(1) {
            override fun pulse(): Boolean {
                if (!Repository.players.contains(player)) {
                    Repository.addPlayer(player)
                }
                return true
            }
        })
    }

    /**
     * Checks if the login request is valid.
     * @return `True` if the request is valid.
     */
    private fun validateRequest(): Boolean {
        //This is supposed to prevent the double-logging issue. Will it work? Who knows.
        if (Repository.LOGGED_IN_PLAYERS.contains(details.username)) {
            SystemLogger.logWarn("LOGGED_IN_PLAYERS contains ${details.username}")
            return flag(AuthResponse.AlreadyOnline)
        }
        if (WorldCommunicator.getState() == ManagementServerState.CONNECTING) {
            return flag(AuthResponse.LoginServerOffline)
        }
        if (!details.session.isActive) {
            return false
        }
        if (SystemManager.isUpdating()) {
            return flag(AuthResponse.Updating)
        }
        if (Repository.getPlayerByName(details.username).also { gamePlayer = it } != null && gamePlayer!!.session.isActive) {
            return flag(AuthResponse.AlreadyOnline)
        }
        return if (details.isBanned) {
            flag(AuthResponse.AccountDisabled)
        } else true
    }

    /**
     * Flags a response.
     * @param response the [Response].
     * @return `True` if successfully logged in.
     */
    fun flag(response: AuthResponse): Boolean {
        details.session.write(response, true)
        return response == AuthResponse.Success
    }

    companion object {
        /**
         * The lock used to disable 2 of the same player being logged in.
         */
        private val LOCK: Lock = ReentrantLock()
    }

    /**
     * Constructs a new `LoginParser` `Object`.
     * @param details the player details.
     * @param type The login type.
     */
    init {
        timeStamp = GameWorld.ticks
    }
}
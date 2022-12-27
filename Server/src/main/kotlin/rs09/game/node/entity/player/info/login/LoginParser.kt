package rs09.game.node.entity.player.info.login

import api.LoginListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.info.login.LoginConfiguration
import core.game.node.entity.player.info.login.PlayerParser
import core.game.node.entity.player.info.login.Response
import core.game.system.SystemManager
import core.game.system.task.Pulse
import rs09.auth.AuthResponse
import rs09.game.world.GameWorld
import rs09.game.world.GameWorld.loginListeners
import rs09.game.world.repository.Repository
import java.util.function.Consumer

/**
 * Parses the login of a player.
 */
class LoginParser(val details: PlayerDetails) {
    /**
     * Initializes the player.
     * @param player The player.
     * @param reconnect If the player data should be parsed.
     */
    fun initialize(player: Player, reconnect: Boolean) {
        if(!validateRequest()) return
        lateinit var parser: PlayerSaveParser
        try {
            parser = PlayerParser.parse(player)
                ?: throw IllegalStateException("Failed parsing save for: " + player.username) //Parse core
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            Repository.removePlayer(player)
            Repository.LOGGED_IN_PLAYERS.remove(player.username)
            Repository.lobbyPlayers.remove(player)
            Repository.playerNames.remove(player.name)
            flag(AuthResponse.ErrorLoadingProfile)
        }
        GameWorld.Pulser.submit(object : Pulse(1) {
            override fun pulse(): Boolean {
                try {
                    if (details.session.isActive) {
                        loginListeners.forEach(Consumer { listener: LoginListener -> listener.login(player) }) //Run our login hooks
                        parser.runContentHooks() //Run our saved-content-parsing hooks
                        player.details.session.setObject(player)
                        if (reconnect) {
                            reconnect(player)
                        } else {
                            flag(AuthResponse.Success)
                            player.init()
                        }
                    } else {
                        Repository.playerNames.remove(player.name)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                    Repository.removePlayer(player)
                    Repository.LOGGED_IN_PLAYERS.remove(player.username)
                    Repository.lobbyPlayers.remove(player)
                    Repository.playerNames.remove(player.name)
                    flag(AuthResponse.ErrorLoadingProfile)
                }
                return true
            }
        })
    }

    /**
     * Initializes a reconnecting player.
     * @param player The player.
     * @param type The login type.
     */
    private fun reconnect(player: Player) {
        Repository.disconnectionQueue.remove(details.username)
        player.initReconnect()
        player.isActive = true
        flag(AuthResponse.Success)
        player.updateSceneGraph(true)
        player.configManager.init()
        LoginConfiguration.configureGameWorld(player)
        if (!Repository.players.contains(player)) {
            Repository.addPlayer(player)
        }
    }

    /**
     * Checks if the login request is valid.
     * @return `True` if the request is valid.
     */
    private fun validateRequest(): Boolean {
        if (!details.session.isActive) {
            return false
        }
        if (SystemManager.isUpdating()) {
            return flag(AuthResponse.Updating)
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
}
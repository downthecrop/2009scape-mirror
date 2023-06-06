package core.game.node.entity.player.info.login

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.system.SystemManager
import core.game.system.task.Pulse
import core.auth.AuthResponse
import core.game.world.GameWorld
import core.game.world.GameWorld.loginListeners
import core.game.world.repository.Repository
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
                            reinitVarps(player)
                        }
                    } else {
                        Repository.removePlayer(player)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                    Repository.removePlayer(player)
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
        reinitVarps(player)
        LoginConfiguration.configureGameWorld(player)
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

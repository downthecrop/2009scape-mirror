import core.api.*
import core.game.node.entity.player.Player

/**
 * Handles updating the Castle Wars overlay interface
 * @author dginovker
 */
object CastleWarsOverlay {
    @JvmStatic
    fun sendLobbyUpdate(player: Player, bothTeamsHavePlayers: Boolean, gameStartMinutes: Int) {
        setVarp(player, 380, if (bothTeamsHavePlayers) gameStartMinutes else 0)
    }

    @JvmStatic
    fun sendGameUpdate(player: Player) {
        // Todo - Figure out underground mine/etc
        setVarbit(player, 143, 0) // Flag status - safe = 0, taken = 1, dropped = 2
        setVarbit(player, 145, 5) // Saradomin's score
        setVarbit(player, 153, 0) // Flag status - safe = 0, taken = 1, dropped = 2
        setVarbit(player, 155, 7) // Zamorak's score
    }
}

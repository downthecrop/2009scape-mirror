import core.api.setVarbit
import core.game.node.entity.player.Player

/**
 * Handles updating the Castle Wars overlay interface
 * @author dginovker
 */
object CastleWarsOverlay {
    @JvmStatic
    fun sendLobbyUpdate(player: Player, bothTeamsHavePlayers: Boolean, gameStartMinutes: Int) {
        player.varpManager.get(380).setVarbit(0, if (bothTeamsHavePlayers) gameStartMinutes else 0).send(player)
    }

    @JvmStatic
    fun sendGameUpdate(player: Player) {
        // Todo - Figure out underground mine/etc
        setVarbit(player, 143, 0); // Flag status - safe = 0, taken = 1, dropped = 2
        setVarbit(player, 145, 5); // Saradomin's score
        setVarbit(player, 153, 0); // Flag status - safe = 0, taken = 1, dropped = 2
        setVarbit(player, 155, 7); // Zamorak's score
    }
}

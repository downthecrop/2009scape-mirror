package core.game.interaction.player

import core.game.interaction.Option._P_ASSIST
import core.game.interaction.Option._P_TRADE
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestType
import core.plugin.Initializable
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

/**
 * Represents the plugin used to handle the player option interacting.
 * @author 'Vexia
 * @author dginovker
 * @version 2.0
 */
@Initializable
class RequestOption : InteractionListener {
    override fun defineListeners() {
        on(_P_TRADE.name, IntType.PLAYER) { player, node ->
            player.requestManager.request((node as Player), RequestType.TRADE)
            return@on true
        }
        on(_P_ASSIST.name, IntType.PLAYER) { player, node ->
            player.requestManager.request((node as Player), RequestType.ASSIST)
            return@on true
        }
    }
}
package content.global.handlers.player

import core.game.interaction.Option._P_ASSIST
import core.game.interaction.Option._P_TRADE
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestType
import core.plugin.Initializable
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.bots.*

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
            if (node is AIPlayer)
                AIRepository.sendBotInfo(player, node)
            player.requestManager.request((node as Player), RequestType.ASSIST)
            return@on true
        }
    }
}

package core.game.node.entity.player.link.request

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.assist.AssistSession
import core.game.node.entity.player.link.request.trade.TradeModule

/**
 * Represents a request type.
 * @author 'Vexia
 * @author Emperor
 * @author dginovker
 * @version 2.0
 */
open class RequestType
/**
 * Constructs a new `RequestManager { Object}.
 * message the message.
 * requestMessage the requesting message.`
 */(
    /**
     * Represents the message to send for the player when requesting.
     */
    val message: String,
    /**
     * Represents the requesting message type.
     */
    val requestMessage: String,
    /**
     * Represents the module used for this type of requesting.
     */
    val module: RequestModule
) {
    /**
     * Checks if the request can be made.
     * @param player The player.
     * @param target The target.
     * @return `True` if so.
     */
    open fun canRequest(player: Player?, target: Player?): Boolean {
        return true
    }

    /**
     * Gets the requesting message formated with the targets name.
     * @param target the target.
     * @return the message to send.
     */
    fun getRequestMessage(target: Player): String {
        return target.username + requestMessage
    }

    companion object {
        /**
         * The trade request type.
         */
        val TRADE = RequestType("Sending a trade offer...", ":tradereq:", TradeModule(null, null))

        /**
         * The assist request type.
         */
        val ASSIST = RequestType("Sending assistance request...", ":assistreq:", AssistSession())
    }
}
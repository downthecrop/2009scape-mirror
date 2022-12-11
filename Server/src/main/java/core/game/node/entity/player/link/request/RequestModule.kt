package core.game.node.entity.player.link.request

import core.game.node.entity.player.Player

/**
 * Represents the module used for all request types.
 * @author 'Vexia
 * @author dginovker
 * @version 2.0
 */
interface RequestModule {
    /**
     * Method invoked when the targeting player accepts a request.
     * @param player the player.
     * @param target the target.
     */
    fun open(player: Player?, target: Player?)
}
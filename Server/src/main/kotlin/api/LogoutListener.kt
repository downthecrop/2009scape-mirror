package api

import core.game.node.entity.player.Player

/**
 * An interface for writing content that allows code to be executed by the class when a player logs out.
 *
 * Logout listeners are called *before* [PersistPlayer] data is saved.
 */
interface LogoutListener : ContentInterface {
    /**
     * NOTE: This should NOT reference any non-static class-local variables.
     * If you need to access a player's specific instance, use an attribute.
     */
    fun logout(player: Player)
}
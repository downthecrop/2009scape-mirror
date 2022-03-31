package api

import core.game.node.entity.player.Player

interface LogoutListener {
    /**
     * NOTE: This should NOT reference any non-static class-local variables.
     * If you need to access a player's specific instance, use an attribute.
     */
    fun logout(player: Player)
}
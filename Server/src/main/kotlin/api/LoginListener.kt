package api

import core.game.node.entity.player.Player

interface LoginListener {
    /**
     * NOTE: This should NOT reference any non-static class-local variables.
     * If you need to access a player's specific instance, use an attribute.
     * Alternatively, consider using an [api.events.EventHook] if applicable.
     */
    fun login(player: Player)
}
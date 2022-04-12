package api

import core.game.node.entity.player.Player

/**
 * An interface for writing content that allows the class to execute some code when a player logs in.
 *
 * Login listeners are called *before* [PersistPlayer] data is parsed.
 */
interface LoginListener : ContentInterface {
    /**
     * NOTE: This should NOT reference any non-static class-local variables.
     * If you need to access a player's specific instance, use an attribute.
     * Alternatively, consider using an [api.events.EventHook] if applicable.
     */
    fun login(player: Player)
}
package core.game.world.update.flag.context

import core.game.node.entity.player.Player

/**
 * Represents a chat message.
 * @author Emperor
 */
class ChatMessage
/**
 * Constructs a new `ChatMessage` `Object`.
 * @param player The player.
 * @param text The chat text.
 * @param effects The chat effects.
 * @param numChars The num chars.
 */(player: Player, text: String, effects: Int, numChars: Int) {
    /**
     * The player reference.
     */
    var player: Player = player
        private set
    /**
     * The chat text.
     */
    var text: String = text
        private set
    /**
     * The effects.
     */
    var effects = effects
        private set
    /**
     * The numChars.
     */
    var numChars = numChars
        private set

    @JvmField
	var isQuickChat = false
}
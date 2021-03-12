package core.game.system.command

import core.game.node.entity.player.Player

/**
 * Represents a managing system used to dispatch incoming commands.
 * @author Vexia
 */
class CommandSystem {
    /**
     * Method used to parse an incomming command packet.
     * @param player the player.
     * @param message the command message.
     */
    fun parse(player: Player?, message: String): Boolean {
        player ?: return false
        val arguments = message.split(" ").toTypedArray()
        val command = CommandMapping.get(arguments[0])

        if(command == null) {
            for (set in CommandSet.values()) {
                if (set.interpret(player, arguments[0], *arguments)) {
                    return true
                }
            }
        } else {
            command.attemptHandling(player,arguments)
        }
        return false
    }

    companion object {
        /**
         * Gets the command system instance.
         * @return the instance.
         */
        /**
         * Represents the singleton instance of this class.
         */
        val commandSystem = CommandSystem()

    }
}
package rs09.game.system.command

import core.game.node.entity.player.Player
import core.game.system.command.CommandSet
import core.plugin.Plugin
import rs09.game.world.repository.Repository

/**
 * Represents a command plugin that can be linked to a command set.
 * @author Vexia
 */
abstract class CommandPlugin : Plugin<Any?> {
    /**
     * Method used to wrap around a command sets pase method.
     * @param player the player.
     * @param name the name.
     * @param args the arguments.
     * @return `True` if so.
     */
    abstract fun parse(player: Player?, name: String?, args: Array<String?>?): Boolean

    /**
     * Used to override for specific plugins.
     * @param player the player.
     * @return `True` if so.
     */
    fun validate(player: Player?): Boolean {
        return true
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any {
        return Unit
    }

    /**
     * Method used to link this command plugin to a command set.
     * @param sets the sets to link to.
     */
    fun link(vararg sets: CommandSet) {
        for (set in sets) {
            set.plugins.add(this)
        }
    }

    companion object {
        /**
         * Method used to parse the string as an integer.
         * @param string the string.
         * @return the integer.
         */
		@JvmStatic
		fun toInteger(string: String): Int {
            return try {
                string.toInt()
            } catch (exception: NumberFormatException) {
                1
            }
        }

        /**
         * Gets the argument line (starting at index 1).
         * @param args The arguments.
         * @return The argument line.
         */
        fun getArgumentLine(args: Array<String?>): String {
            return getArgumentLine(args, 1, args.size)
        }

        /**
         * Gets the argument line from the given arguments.
         * @param args The arguments.
         * @param offset The start index.
         * @param length The end index.
         * @return the line.
         */
        fun getArgumentLine(args: Array<String?>, offset: Int, length: Int): String {
            val sb = StringBuilder()
            for (i in offset until length) {
                if (i != offset) {
                    sb.append(" ")
                }
                sb.append(args[i])
            }
            return sb.toString()
        }

        /**
         * Gets the target player.
         * @param name The name.
         * @param load If we load the file.
         * @return The player.
         */
        @JvmStatic
        fun getTarget(name: String?, load: Boolean): Player? {
            return Repository.getPlayerByName(name)
        }

        /**
         * Gets the target player.
         * @param name The name.
         * @return The player.
         */
		@JvmStatic
		fun getTarget(name: String?): Player? {
            return Repository.getPlayerByName(name)
        }
    }
}
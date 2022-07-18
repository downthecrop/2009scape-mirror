package api

import core.game.node.entity.player.Player
import rs09.game.system.command.Command
import rs09.game.system.command.CommandMapping
import rs09.game.system.command.Privilege
import rs09.tools.stringtools.colorize

/**
 * An interface for writing content that allows the class to define commands.
 *
 * Includes methods [reject] and [notify] for notifying the player of a command rejection or some output text respectively.
 *
 * Includes the method [define] to define individual commands
 *
 * Commands should ideally be defined in the required [defineCommands] method.
 */
interface Commands : ContentInterface {
    /**
     * Glorified player.sendMessage with red text coloring. Please use this
     * rather than just player.sendMessage for anything that involves informing the player
     * of proper usage or invalid syntax. Throws an IllegalStateException and ends command execution immediately.
     */
    fun reject(player: Player, vararg message: String){
        for(msg in message) {
            player.sendMessage(colorize("-->%R$msg"))
        }
        throw IllegalStateException()
    }

    /**
     * Glorified player.sendMessage with black text coloring and an arrow. Use this when you need to
     * notify/inform a player of some information from within the command without ending execution.
     */
    fun notify(player: Player, message: String){
        player.sendMessage(colorize("-->$message"))
    }

    /**
     * Used to define commands. define("name",(optional) privilege override){ handle }
     * @param name the name of the command. Example: ::example would be just "example"
     * @param privilege the rights level needed to execute the command. Options are [Privilege.STANDARD], [Privilege.MODERATOR], [Privilege.ADMIN]. Defaults to [Privilege.STANDARD]
     */
    fun define(name: String, privilege: Privilege = Privilege.ADMIN, usage: String = "", description: String = "", handle: (Player, Array<String>) -> Unit){
        CommandMapping.register(Command(name, privilege, usage, description, handle))
    }

    fun defineCommands()
}

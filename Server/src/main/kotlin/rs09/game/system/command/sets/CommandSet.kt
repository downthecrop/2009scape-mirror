package rs09.game.system.command.sets

import core.game.node.entity.player.Player
import rs09.game.system.command.Command
import rs09.game.system.command.CommandMapping
import core.plugin.Plugin
import rs09.game.system.command.Privilege
import rs09.tools.stringtools.colorize

/**
 * Command sets allow you to organize sets of commands by category/type. It also provides
 * a clean way to define them and get them registered to the command mapping, as well as
 * a couple other small utiltiies.
 * @author Ceikry
 * @param defaultPrivilege the default privilege level for all commands in this set.
 */
abstract class CommandSet(val defaultPrivilege: Privilege) : Plugin<Any?> {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        defineCommands()
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    abstract fun defineCommands()

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
     * @param privilege (optional) privilege override from the set default.
     */
    fun define(name: String, privilege: Privilege = defaultPrivilege, handle: (Player, Array<String>) -> Unit){
        CommandMapping.register(Command(name, privilege, handle))
    }
}
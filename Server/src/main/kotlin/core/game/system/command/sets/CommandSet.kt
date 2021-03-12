package core.game.system.command.sets

import core.game.node.entity.player.Player
import core.plugin.Plugin
import core.tools.stringtools.colorize
import core.game.system.command.Command
import core.game.system.command.CommandMapping

/**
 * Command sets allow you to organize sets of commands by category/type. It also provides
 * a clean way to define them and get them registered to the command mapping, as well as
 * a couple other small utiltiies.
 * @author Ceikry
 * @param defaultPrivilege the default privilege level for all commands in this set.
 */
abstract class CommandSet(val defaultPrivilege: Command.Privilege) : Plugin<Any?> {
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
     * of proper usage or invalid syntax.
     */
    fun reject(player: Player, message: String){
        player.sendMessage(colorize("%R$message"))
    }

    /**
     * Used to define commands. define("name",(optional) privilege override){ handle }
     * @param name the name of the command. Example: ::example would be just "example"
     * @param privilege (optional) privilege override from the set default.
     */
    fun define(name: String, privilege: Command.Privilege = defaultPrivilege, handle: (Player, Array<String>) -> Unit){
        CommandMapping.register(Command(name, privilege, handle))
    }
}
package core.game.system.command

import core.game.node.entity.player.Player
import core.game.world.GameWorld

/**
 * Base class for Commands in the new system. Can pass a lambda as part of the constructor or after the constructor.
 * @author Ceikry
 */
class Command(val name: String, val privilege: Privilege, val handle: (Player, Array<String>) -> Unit) {
    fun attemptHandling(player: Player, args: Array<String>?){
        args ?: return
        if(player.rights.ordinal >= privilege.ordinal || GameWorld.settings?.isDevMode == true){
            handle(player,args)
        }
    }

    enum class Privilege{
        STANDARD,
        MODERATOR,
        ADMIN
    }
}

object CommandMapping {
    private val mapping = hashMapOf<String, Command>()

    fun get(name: String): Command?{
        return mapping[name]
    }

    fun register(command: Command){
        mapping[command.name] = command
    }

    fun getCommands(): Array<Command> {
        return mapping.values.toTypedArray()
    }

    fun getNames(): Array<String> {
        return mapping.keys.toTypedArray()
    }
}
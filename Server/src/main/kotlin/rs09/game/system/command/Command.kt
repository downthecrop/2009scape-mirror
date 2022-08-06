package rs09.game.system.command

import core.game.node.entity.player.Player
import rs09.ServerConstants
import rs09.game.world.GameWorld
import kotlin.collections.ArrayList

/**
 * Base class for Commands in the new system. Can pass a lambda as part of the constructor or after the constructor.
 * @author Ceikry
 */
class Command(val name: String, val privilege: Privilege, val usage: String = "UNDOCUMENTED", val description: String = "UNDOCUMENTED", val handle: (Player, Array<String>) -> Unit) {
    fun attemptHandling(player: Player, args: Array<String>?){
        args ?: return
        if(player.rights.ordinal >= privilege.ordinal || GameWorld.settings?.isDevMode == true || ServerConstants.I_AM_A_CHEATER){
            handle(player,args)
        }
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

    fun getPageIndices(rights: Int): IntArray {
        val list = ArrayList<Int>()
        list.add(0)

        var lineCounter = 0
        for ((index, command) in getCommands().filter { it.privilege.ordinal <= rights }.withIndex()) {

            lineCounter += 2
            if (command.usage.isNotEmpty()) lineCounter++
            if (command.description.isNotEmpty()) lineCounter++

            if (lineCounter > 306) {
                list.add(index)
                lineCounter = 0
            }
        }

        return list.toIntArray()
    }
}

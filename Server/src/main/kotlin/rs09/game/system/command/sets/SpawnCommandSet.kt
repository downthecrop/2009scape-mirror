package rs09.game.system.command.sets

import core.cache.Cache
import core.game.node.`object`.GameObject
import core.game.node.`object`.ObjectBuilder
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import rs09.game.system.SystemLogger
import rs09.game.system.command.Command
import rs09.game.system.command.CommandPlugin
import core.plugin.Initializable
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Initializable
class SpawnCommandSet : CommandSet(Command.Privilege.ADMIN){
    override fun defineCommands() {
        /**
         * Spawns an npc with the given ID
         */
        define("npc"){player,args ->
            if (args.size < 2) {
                reject(player,"syntax error: id (optional) direction")
                return@define
            }
            val npc = NPC.create(CommandPlugin.toInteger(args[1]!!), player!!.location)
            npc.setAttribute("spawned:npc", true)
            npc.isRespawn = false
            npc.direction = player.direction
            npc.init()
            npc.isWalks = args.size > 2
            val npcString = "{" + npc.location.x + "," + npc.location.y + "," + npc.location.z + "," + (if (npc.isWalks) "1" else "0") + "," + npc.direction.ordinal + "}"
            val clpbrd = Toolkit.getDefaultToolkit().systemClipboard
            clpbrd.setContents(StringSelection(npcString), null)
            println(npcString)
        }

        /**
         * Spawns an item with the given ID
         */
        define("item"){player,args ->
            if (args.size < 2) {
                reject(player,"You must specify an item ID")
                return@define
            }
            val id = args[1].toIntOrNull() ?: return@define
            var amount = (args.getOrNull(2) ?: "1").toInt()
            if (id > Cache.getItemDefinitionsSize()) {
                reject(player,"Item ID '$id' out of range.")
                return@define
            }
            val item = Item(id, amount)
            val max = player.inventory.getMaximumAdd(item)
            if (amount > max) {
                amount = max
            }
            item.setAmount(amount)
            player.inventory.add(item)
        }

        /**
         * Spawn object with given ID at the player's location
         */
        define("object"){player,args ->
            if (args!!.size < 2) {
                reject(player,"syntax error: id (optional) type rotation or rotation")
                return@define
            }
            val `object` = if (args.size > 3) GameObject(CommandPlugin.toInteger(args[1]!!), player!!.location, CommandPlugin.toInteger(args[2]!!), CommandPlugin.toInteger(args[3]!!)) else if (args.size == 3) GameObject(CommandPlugin.toInteger(args[1]!!), player!!.location, CommandPlugin.toInteger(args[2]!!)) else GameObject(CommandPlugin.toInteger(args[1]!!), player!!.location)
            ObjectBuilder.add(`object`)
            SystemLogger.logInfo("object = $`object`")
        }
    }
}
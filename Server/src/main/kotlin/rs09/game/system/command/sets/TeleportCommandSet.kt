package rs09.game.system.command.sets

import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.plugin.Initializable
import rs09.ServerConstants
import rs09.game.system.command.Command
import rs09.game.system.command.Privilege
import rs09.game.world.repository.Repository

@Initializable
class TeleportCommandSet : CommandSet(Privilege.ADMIN){
    override fun defineCommands() {
        /**
         * Allows teleporting by location name
         */
        define("to"){player,args ->
            var destination: Location? = null
            val place = args.slice(1 until args.size).joinToString(" ")
            for (destinations in ServerConstants.TELEPORT_DESTINATIONS) {
                var i = 1
                while (i < destinations.size) {
                    if (place == destinations[i]) {
                        destination = destinations[0] as Location
                        break
                    }
                    i++
                }
            }
            if (destination != null) {
                player.teleporter.send(destination, TeleportManager.TeleportType.NORMAL)
            } else {
                reject(player,"Could not locate teleport destination [name=$place]!")
            }
        }

        /**
         * Teleport to location using coordinates
         */
        define("tele"){player,args ->
            if (args.size == 2 && args[1].contains(",")) {
                val args2 = args[1].split(",".toRegex()).toTypedArray()
                val x = args2[1].toInt() shl 6 or args2[3].toInt()
                val y = args2[2].toInt() shl 6 or args2[4].toInt()
                val z = args2[0].toInt()
                player.properties.teleportLocation = Location.create(x, y, z)
                return@define
            }
            if (args.size < 2) {
                reject(player,"syntax error: x, y, (optional) z")
            }
            player.properties.teleportLocation = Location.create(args[1].toInt(), args[2].toInt(), if (args.size > 3) args[3].toInt() else 0)
        }


        /**
         * Teleport to a specific player
         */
        define("teleto"){player,args ->
            if (args.size < 1) {
                reject(player,"syntax error: name")
            }
            val n = args.slice(1 until args.size).joinToString("_")
            val target = Repository.getPlayerByName(n)
            if (target == null) {
                reject(player,"syntax error: name")
            }
            if (target!!.getAttribute<Any?>("fc_wave") != null) {
                reject(player,"You cannot teleport to a player who is in the Fight Caves.")
            }
            player.properties.teleportLocation = target.location
        }


        /**
         * Teleport a specific player to you
         */
        define("teletome"){player,args ->
            if (args.size < 1) {
                reject(player,"syntax error: name")
            }
            val n = args.slice(1 until args.size).joinToString("_")
            val target = Repository.getPlayerByName(n)
            if (target == null) {
                reject(player,"syntax error: name")
            }
            if (target!!.getAttribute<Any?>("fc_wave") != null) {
                reject(player,"You cannot teleport to a player who is in the Fight Caves.")
            }
            target.properties.teleportLocation = player.location
        }


        /**
         * Teleports to the server's home location
         */
        define("home"){player,_ ->
            player.properties.teleportLocation = ServerConstants.HOME_LOCATION
        }
    }
}
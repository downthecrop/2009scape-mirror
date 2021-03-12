package core.game.system.command.sets

import core.game.node.entity.npc.NPC
import core.plugin.Initializable
import core.game.system.command.Command

@Initializable
class SlayerCommandSet : CommandSet(Command.Privilege.ADMIN){
    override fun defineCommands() {
        /**
         * Finishes a player's slayer task (the correct way)
         */
        define("finishtask"){player,_ ->
            player.debug("Kill the npc that spawned to finish your task.")
            player.slayer.amount = 1
            val finisher = NPC(player.slayer.task.npcs[0], player.location)
            finisher.isRespawn = false
            finisher.init()
        }

        /**
         * Set slayer points
         */
        define("setslayerpoints"){player,args ->
            if(args.size < 2){
                reject(player,"Usage: ::setslayerpoints amount")
                return@define
            }

            val amount = args[1].toIntOrNull()
            if(amount == null){
                reject(player,"Amount needs to be a valid integer!")
                return@define
            }

            player.slayer.slayerPoints = amount
            player.sendMessage("Set slayer points to $amount.")
        }
    }
}
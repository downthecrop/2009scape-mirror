package rs09.game.system.command.sets

import core.game.node.entity.npc.NPC
import core.plugin.Initializable
import rs09.game.node.entity.skill.slayer.SlayerManager
import rs09.game.system.command.Command
import rs09.game.system.command.Privilege

@Initializable
class SlayerCommandSet : CommandSet(Privilege.ADMIN){
    override fun defineCommands() {
        /**
         * Finishes a player's slayer task (the correct way)
         */
        define("finishtask"){player,_ ->
            notify(player, "Kill the npc that spawned to finish your task.")
            SlayerManager.getInstance(player).amount = 1
            val finisher = NPC(SlayerManager.getInstance(player).task?.npcs?.get(0) ?: 0, player.location)
            finisher.isRespawn = false
            finisher.init()
        }

        /**
         * Set slayer points
         */
        define("setslayerpoints"){player,args ->
            if(args.size < 2){
                reject(player,"Usage: ::setslayerpoints amount")
            }

            val amount = args[1].toIntOrNull()
            if(amount == null){
                reject(player,"Amount needs to be a valid integer!")
            }

            SlayerManager.getInstance(player).slayerPoints = amount!!
            notify(player, "Set slayer points to $amount.")
        }
    }
}
package core.game.system.command.sets

import core.game.node.entity.npc.NPC
import core.game.system.command.CommandPlugin.Companion.toInteger
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.game.system.command.Command
import java.util.*

@Initializable
class AnimationCommandSet : CommandSet(Command.Privilege.ADMIN) {

    protected var npcs: List<NPC> = ArrayList()

    override fun defineCommands() {

        /**
         * Force the player to play animation <Animation ID>
         */
        define("anim"){ player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::anim <Animation ID>")
                return@define
            }
            val animation = Animation(args[1].toInt())
            player.animate(animation)
        }

        /**
         * Force the player to loop animation <Animation ID>
         */
        define("loopanim"){ player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::loopanim <Animation ID> <Loop Amount>")
                return@define
            }
            val start = toInteger(args[1])
            var end = toInteger((args[2]))
            if (end > 25) {
                player.sendMessage("Really...? $end times...? Looping 25 times instead.")
                end = 25
            }
            GameWorld.Pulser.submit(object : Pulse(3, player) {
                var id = start
                override fun pulse(): Boolean {
                    player.animate(Animation.create(id))
                    return ++id >= end
                }
            })
        }

        /**
         * Change the player's render animation to <Render Animation ID>
         */
        define("ranim"){ player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::ranim <Render Animation ID>")
                return@define
            }
            try {
                player.appearance.setAnimations(Animation.create(args[1].toInt()))
                player.appearance.sync()
            } catch (e: NumberFormatException) {
                reject(player, "Syntax error: ::ranim <Render Animation ID>")
                return@define
            }
        }


        /**
         * Reset the player's render animation to default
         */
        define("ranimreset"){ player, _ ->
            player.appearance.prepareBodyData(player)
            player.appearance.setDefaultAnimations()
            player.appearance.setAnimations()
            player.appearance.sync()
        }
    }
}
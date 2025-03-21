package core.game.system.command.sets

import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.game.system.command.CommandPlugin.Companion.toInteger
import core.game.system.command.Privilege
import core.game.world.GameWorld
import java.util.*

@Initializable
class AnimationCommandSet : CommandSet(Privilege.ADMIN) {

    private var npcs: List<NPC> = ArrayList()

    override fun defineCommands() {

        /**
         * Force the player to play animation <Animation ID>
         */
        define("anim", Privilege.ADMIN, "::anim <lt>Animation ID<gt>", "Plays the animation with the given ID."){ player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::anim <Animation ID>")
            }
            val animation = Animation(args[1].toInt())
            player.animate(animation)
        }

        /**
         * Force the player to play animation <Animation ID>
         */
        define("anims", Privilege.ADMIN, "::anim <lt>Animation ID<gt>", "Plays the animation with the given ID."){ player, args ->
            if (args.size < 3) {
                reject(player, "Syntax error: ::anim <Animation ID>")
            }
            val animation = args[1].toInt()
            val animationTo = args[2].toInt()
            GameWorld.Pulser.submit(object : Pulse(3, player) {
                var someId = animation
                override fun pulse(): Boolean {
                    player.animate(Animation.create(someId))
                    someId += 1
                    return someId >= animationTo
                }
            })
        }

        /**
         * Force the player to loop animation <Animation ID>
         */
        define("loopanim", Privilege.ADMIN, "::loopanim <lt>Animation ID<gt> <lt>Times<gt>", "Plays the animation with the given ID the given number of times"){ player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::loopanim <Animation ID> <Loop Amount>")
            }
            val start = toInteger(args[1])
            var end = toInteger((args[2]))
            if (end > 25) {
                notify(player, "Really...? $end times...? Looping 25 times instead.")
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
        define("ranim", Privilege.ADMIN, "::ranim <lt>Render Anim ID<gt>", "Sets the player's render (walk/idle) animation."){ player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::ranim <Render Animation ID>")
            }
            if (args.size > 2) {
                GameWorld.Pulser.submit(object : Pulse(3, player) {
                    var id = args[1].toInt()
                    override fun pulse(): Boolean {
                        player.appearance.setAnimations(Animation.create(id))
                        player.appearance.sync()
                        player.sendChat("Current: $id")
                        return ++id >= args[2].toInt()
                    }
                })
            } else {
                try {
                    player.appearance.setAnimations(Animation.create(args[1].toInt()))
                    player.appearance.sync()
                } catch (e: NumberFormatException) {
                    reject(player, "Syntax error: ::ranim <Render Animation ID>")
                }
            }
        }


        /**
         * Reset the player's render animation to default
         */
        define("ranimreset", Privilege.ADMIN, "", "Resets the player's render (walk/idle) animation to default."){ player, _ ->
            player.appearance.prepareBodyData(player)
            player.appearance.setDefaultAnimations()
            player.appearance.setAnimations()
            player.appearance.sync()
        }
    }
}

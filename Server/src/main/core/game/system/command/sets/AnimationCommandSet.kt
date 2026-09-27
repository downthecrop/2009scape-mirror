package core.game.system.command.sets

import core.api.addScenery
import core.api.animate
import core.api.animateScenery
import core.api.delayScript
import core.api.log
import core.api.queueScript
import core.api.stopExecuting
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.game.system.command.CommandPlugin.Companion.toInteger
import core.game.system.command.Privilege
import core.game.world.GameWorld
import core.tools.Log
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
                reject(player, "Syntax error: ::anim <lt>Animation ID<gt>")
                return@define
            }
            val animation = Animation(args[1].toInt())
            player.animate(animation)
        }

        define("anims", Privilege.ADMIN, "::anims <lt>From Animation ID<gt> <lt>To Animation ID<gt> <lt>(opt) Duration Per Animation<gt>", "Plays animations from the From ID to the To ID, with a delay of 3 between animations, unless specified otherwise"){ player, args ->
            if (args.size < 3) {
                reject(player, "Syntax error: ::anims <lt>From Animation ID<gt> <lt>To Animation ID<gt> <lt>(opt) Duration Per Animation<gt>")
                return@define
            }
            val animationFrom = args[1].toInt()
            val animationTo = args[2].toInt()
            val animationDelay = (args.getOrNull(3) ?: "3").toInt()

            queueScript(player, 1, QueueStrength.STRONG) { stage: Int ->
                val animationId = animationFrom + stage
                animate(player, animationId, true)
                notify(player, "Playing animation $animationId")

                if (animationId == animationTo) {
                    return@queueScript stopExecuting(player)
                }

                return@queueScript delayScript(player, animationDelay)
            }
        }

        /**
         * Force the player to loop animation <Animation ID>
         */
        define("loopanim", Privilege.ADMIN, "::loopanim <lt>Animation ID<gt> <lt>Times<gt>", "Plays the animation with the given ID the given number of times"){ player, args ->
            if (args.size < 2) {
                reject(player, "Syntax error: ::loopanim <lt>Animation ID<gt> <lt>Loop Amount<gt>")
                return@define
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
                reject(player, "Syntax error: ::ranim <lt>Render Animation ID<gt>")
                return@define
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

        /**
         * Dumps the animations contained within a given render animation, by ID.
         */
        define("ranimdump", Privilege.ADMIN, "::ranimdump <lt>Animation ID<gt>", "Dumps the render animation sub-animations for the given animation ID.") { player, args ->
            if (args.size < 2 || args[1].toIntOrNull() == null) {
                reject(player, "Syntax error: ::ranimdump <lt>Animation ID<gt>")
                return@define
            }
            val argAnimId = args[1].toInt()
            val def = core.cache.def.impl.RenderAnimationDefinition.forId(argAnimId)
            notify(player, "Sub-animations of render anim ID $argAnimId: stand=${def.standAnimationId}, walk=${def.walkAnimationId}, run=${def.runAnimationId},")
            notify(player, "Sub-animations of render anim ID $argAnimId: turn180=${def.turn180Animation}, turnCW=${def.turnCWAnimation}, turnCCW=${def.turnCCWAnimation}")
        }

        /**
         * Searches all animations for render animations containing a given animation as a sub-animation, by ID.
         */
        define("ranimsearch", Privilege.ADMIN, "::ranimsearch <lt>Animation ID<gt>", "Searches all animation definitions for render animations that incorporate the given animation ID.") { player, args ->
            if (args.size < 2 || args[1].toIntOrNull() == null) {
                reject(player, "Syntax error: ::ranimsearch <lt>Animation ID<gt>")
                return@define
            }
            val argAnimId = args[1].toInt()
            val everyRAnim = core.cache.Cache.getIndexes()[2].getFilesSize(32)
            var hits = 0
            for (animId in 0 until everyRAnim) {
                val def = core.cache.def.impl.RenderAnimationDefinition.forId(animId) ?: continue
                if (def.standAnimationId == argAnimId || def.walkAnimationId == argAnimId || def.runAnimationId == argAnimId ||
                    def.turn180Animation == argAnimId || def.turnCWAnimation == argAnimId || def.turnCCWAnimation == argAnimId) {
                    hits++
                    val output1 = "Sub-animations of render anim ID $animId: stand=${def.standAnimationId}, walk=${def.walkAnimationId}, run=${def.runAnimationId}"
                    val output2 = "Sub-animations of render anim ID $animId: turn180=${def.turn180Animation}, turnCW=${def.turnCWAnimation}, turnCCW=${def.turnCCWAnimation}"
                    if (hits <= 20) {
                        notify(player, output1)
                        notify(player, output2)
                    }
                    log(this::class.java, Log.DEBUG, output1)
                    log(this::class.java, Log.DEBUG, output2)
                }
            }
            val total = "$hits render animation(s) found referencing animation ID $argAnimId."
            notify(player, total)
            log(this::class.java, Log.DEBUG, total)
            if (hits > 20) {
                notify(player, "Displayed results truncated to 20, see console for full list.")
            }
        }

        /**
         * Prints the render animation ID of a given NPC, and its sub-animations, by NPC ID.
         */
        define("ranimnpc", Privilege.ADMIN, "::ranimnpc <lt>NPC ID<gt>", "Prints the render animation ID (and sub-animation IDs) of an NPC.") { player, args ->
            if (args.size < 2 || args[1].toIntOrNull() == null) {
                reject(player, "Syntax error: ::ranimnpc <lt>NPC ID<gt>")
                return@define
            }
            val targetNPC = args[1].toInt()
            val rAnimId = core.cache.def.impl.NPCDefinition.forId(targetNPC).renderAnimationId
            val def = core.cache.def.impl.RenderAnimationDefinition.forId(rAnimId)
            notify(player, "Render animation for NPC ID $targetNPC: animation ID $rAnimId")
            notify(player, "Sub-animations of render anim ID $rAnimId: stand=${def.standAnimationId}, walk=${def.walkAnimationId}, run=${def.runAnimationId},")
            notify(player, "Sub-animations of render anim ID $rAnimId: turn180=${def.turn180Animation}, turnCW=${def.turnCWAnimation}, turnCCW=${def.turnCCWAnimation}")
        }

        /**
         * Spawns an object with a given ID at the player's location, and animates it.
         */
        define("objanim", usage = "::objanim <lt>objectId<gt> <lt>animFrom<gt> <lt>(opt) animTo<gt> <lt>(opt) delay<gt>", description = "Spawns an object and animates it with the provided anim ID. If a second ID is provided, it iterates through the anims.") { player, args ->
            if (args.size < 3 || args[1].toIntOrNull() == null || args[2].toIntOrNull() == null) {
                reject(player, "Syntax error: ::objanim <lt>objectId<gt> <lt>animFrom<gt> <lt>(opt) animTo<gt> <lt>(opt) delay<gt>")
                return@define
            }

            val objectId = args[1].toInt()
            val animFrom = args[2].toInt()
            var animTo = args.getOrNull(3)?.toIntOrNull()
            val delay = args.getOrNull(4)?.toIntOrNull() ?: 3

            // spawn the object
            val obj = core.game.node.scenery.Scenery(objectId, player.location)
            addScenery(obj)

            // set the range if it's just a single anim
            if (animTo == null) animTo = animFrom

            // loop through the anims if it's a range
            notify(player, "Spawned object $objectId, playing anims $animFrom to $animTo.")
            queueScript(player, 1, QueueStrength.WEAK) { stage: Int ->
                val currentAnimId = animFrom + stage
                animateScenery(player, obj, currentAnimId)
                notify(player, "Playing object animation $currentAnimId")
                if (currentAnimId >= animTo) {
                    return@queueScript stopExecuting(player)
                }
                return@queueScript delayScript(player, delay)
            }
        }
    }

}

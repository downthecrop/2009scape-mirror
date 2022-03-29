package rs09.game.node.entity.skill.agility

import api.*
import core.cache.def.impl.SceneryDefinition
import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.agility.AgilityCourse
import core.game.node.entity.skill.agility.AgilityHandler
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import rs09.game.world.GameWorld

/**
 * Handles the gnome stronghold agility course.
 * @author Woah
 */
@Initializable
class GnomeStrongholdCourse
/**
 * Constructs a new `GnomeStrongholdCourse` `Object`.
 * @param player The player.
 */
/**
 * Constructs a new `GnomeStrongholdCourse` `Object`.
 */
@JvmOverloads constructor(player: Player? = null) : AgilityCourse(player, 7, 39.0) {
    override fun handle(player: Player, node: Node, option: String): Boolean {
        getCourse(player) // Sets the extension.
        val `object` = node as Scenery
        when (`object`.id) {
            2295 -> {
                TRAINERS[0]!!.sendChat("Okay get over that log, quick quick!")
				sendMessage(player, "You walk carefully across the slippery log...")
                AgilityHandler.walk(player, 0, Location.create(2474, 3436, 0), Location.create(2474, 3429, 0), Animation.create(155), 7.5, "...You make it safely to the other side.")
                return true
            }
            2285 -> {
                TRAINERS[1]!!.sendChat("Move it, move it, move it!")
				sendMessage(player, "You climb the netting...")
                AgilityHandler.climb(player, 1, Animation.create(828), `object`.location.transform(0, -1, 1), 7.5, null)
                return true
            }
            35970 -> {
                TRAINERS[2]!!.sendChat("That's it - straight up.")
				sendMessage(player, "You climb the tree..")
                AgilityHandler.climb(player, 2, Animation.create(828), Location.create(2473, 3420, 2), 5.0, "...To the platform above.")
                return true
            }
            2312 -> {
                TRAINERS[3]!!.sendChat("Come on scaredy cat, get across that rope!")
				sendMessage(player, "You carefully cross the tightrope.")
                AgilityHandler.walk(player, 3, Location.create(2477, 3420, 2), Location.create(2483, 3420, 2), Animation.create(155), 7.5, null)
                return true
            }
            4059 -> {
				sendMessage(player, "You can't do that from here.")
                return true
            }
            2314, 2315 -> {
				sendMessage(player, "You climb down the tree..")
                AgilityHandler.climb(player, 4, Animation.create(828), Location.create(2487, 3420, 0), 5.0, "You land on the ground.")
                return true
            }
            2286 -> {
                TRAINERS[4]!!.sendChat("My Granny can move faster than you.")
                player.faceLocation(player.location.transform(0, 2, 0))
				sendMessage(player, "You climb the netting...")
                AgilityHandler.climb(player, 5, Animation.create(828), player.location.transform(0, 2, 0), 7.5, null)
                return true
            }
            4058, 154 -> {
                val index = if (`object`.id == 154) 0 else 1 //If the player clicks on the left pipe, set index to 0, otherwise 1
                val x = 2484 + index * 3 //change the x coordinates for walking/animations depending on index multiplier
                if (`object`.location.y == 3435) {
					sendMessage(player, "You can't do that from here.")
                    return true
                }
                if (USED_PIPES[index] > GameWorld.ticks) {
					sendMessage(player, "The pipe is being used.")
                    return true
                }
                USED_PIPES[index] = GameWorld.ticks + 10

                player.lock()
                //Animations and force walking
                //X variable is determined by both index and x variables before
                AgilityHandler.forceWalk(player, -1, Location.create(x, 3430, 0), Location.create(x, 3433, 0), Animation.create(10580), 10, 0.0, null)
                player.lock()
                AgilityHandler.forceWalk(player, -1, Location.create(x, 3433, 0), Location.create(x, 3435, 0), Animation.create(844), 10, 0.0, null, 5)
                player.lock()
                AgilityHandler.forceWalk(player, 6, Location.create(x, 3435, 0), Location.create(x, 3437, 0), Animation.create(10579), 20, 7.5, null, 8)
                return true
            }
        }
        return false
    }

    override fun getDestination(node: Node, n: Node): Location? {
        val `object` = n as Scenery
        when (`object`.id) {
            2295 -> return Location.create(2474, 3436, 0)
            2286 -> {
                var x = node.location.x
                if (x < n.getLocation().x) {
                    x = n.getLocation().x
                } else if (x > n.getLocation().x + 1) {
                    x = n.getLocation().x + 1
                }
                return Location.create(x, n.getLocation().y - 1, 0)
            }
            4058, 154 -> if (n.getLocation().y == 3431) {
                return n.getLocation().transform(0, -1, 0)
            }
        }
        return null
    }

    override fun configure() {
        TRAINERS[0] = NPC.create(162, Location.create(2473, 3438, 0))
        TRAINERS[1] = NPC.create(162, Location.create(2478, 3426, 0))
        TRAINERS[2] = NPC.create(162, Location.create(2474, 3422, 1))
        TRAINERS[3] = NPC.create(162, Location.create(2472, 3419, 2))
        TRAINERS[4] = NPC.create(162, Location.create(2489, 3425, 0))
        for (npc in TRAINERS) {
            npc!!.init()
            npc.walkRadius = 3
        }
        SceneryDefinition.forId(2295).handlers["option:walk-across"] = this
        SceneryDefinition.forId(2285).handlers["option:climb-over"] = this
        SceneryDefinition.forId(35970).handlers["option:climb"] = this
        SceneryDefinition.forId(2312).handlers["option:walk-on"] = this
        SceneryDefinition.forId(4059).handlers["option:walk-on"] = this
        SceneryDefinition.forId(2314).handlers["option:climb-down"] = this
        SceneryDefinition.forId(2315).handlers["option:climb-down"] = this
        SceneryDefinition.forId(2286).handlers["option:climb-over"] = this
        SceneryDefinition.forId(4058).handlers["option:squeeze-through"] = this
        SceneryDefinition.forId(154).handlers["option:squeeze-through"] = this
    }

    override fun createInstance(player: Player): AgilityCourse {
        return GnomeStrongholdCourse(player)
    }

    companion object {
        /**
         * The pipes currently in usage.
         */
        private val USED_PIPES = IntArray(2)

        /**
         * The trainer NPCs.
         */
        private val TRAINERS = arrayOfNulls<NPC>(5)
    }
}
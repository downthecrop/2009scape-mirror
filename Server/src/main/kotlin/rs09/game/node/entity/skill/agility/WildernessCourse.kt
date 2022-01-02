package rs09.game.node.entity.skill.agility

import api.*
import core.cache.def.impl.SceneryDefinition
import core.game.content.global.action.DoorActionHandler
import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.agility.AgilityCourse
import core.game.node.entity.skill.agility.AgilityHandler
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import rs09.game.world.GameWorld

/**
 * Handles the wilderness agility course.
 * @author Woah
 */
@Initializable
class WildernessCourse

/**
 * Constructs a new `WildernessCourse` `Object`.
 */
@JvmOverloads constructor(player: Player? = null) : AgilityCourse(player, 5, 499.0) {
    override fun handle(player: Player, node: Node, option: String): Boolean {
        getCourse(player) // Sets the extension.
        val `object` = node as Scenery
        when (`object`.id) {
            2309 -> handleEntrance(player, `object`)
            2307, 2308 -> {
                DoorActionHandler.handleAutowalkDoor(player, `object`)
                handleEntranceObstacle(player, `object`)
            }
            2288 -> handlePipe(player, `object`)
            2283 -> handleRopeSwing(player, `object`)
            37704 -> handleSteppingStones(player, `object`)
            2297 -> handleLogBalance(player, `object`)
            2328 -> handleRockClimb(player, `object`)
        }
        return true
    }

    /**
     * Handles the door entrance.
     * @param player the player.
     * @param object the object.
     */
    private fun handleEntrance(player: Player, `object`: Scenery) {
        if (player.location.y > 3916 || player.skills.getLevel(Skills.AGILITY) >= 52) {
            DoorActionHandler.handleAutowalkDoor(player, `object`)
            if (player.location.y <= 3916) {
                handleEntranceObstacle(player, `object`)
            }
        } else {
            player.dialogueInterpreter.sendDialogue("You need an Agility level of at least 52 to enter.")
        }
    }

    /**
     * Handles the entrance obstacle.
     * @param player the player.
     * @param object the object.
     */
    private fun handleEntranceObstacle(player: Player, `object`: Scenery) {
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            var counter = 0
            val fail = AgilityHandler.hasFailed(player, 1, 0.3)
            override fun pulse(): Boolean {
                when (++counter) {
                    2 -> {
                        val end = if (fail) Location.create(2998, 3924, 0) else if (`object`.id < 2309) Location.create(2998, 3917, 0) else Location.create(2998, 3930, 0)
                        val start = if (`object`.id < 2309) player.location else Location.create(2998, 3917, 0)
						sendMessage(player, "You go through the gate and try to edge over the ridge...")
                        AgilityHandler.walk(player, -1, start, end, Animation.create(155), if (fail) 0.0 else 15.00, if (fail) "You lose your footing and fail into the wolf pit." else "You skillfully balance across the ridge...")
                    }
                    9 -> {
                        if (fail) {
                            AgilityHandler.fail(player, 0, player.location.transform(if (`object`.id < 2309) -2 else 2, 0, 0), Animation.create(if (`object`.id < 2309) 771 else 771), getHitAmount(player), null)
                        }
                        return fail
                    }
                    15 -> player.lock(3)
                    16 -> {
                        val doorLoc = if (`object`.id < 2309) Location(2998, 3917, 0) else Location(2998, 3931, 0)
                        DoorActionHandler.handleAutowalkDoor(player, RegionManager.getObject(doorLoc))
                        return true
                    }
                }
                return false
            }
        })
    }

    /**
     * Handles the pipe obstacle.
     * @param player the pipe.
     * @param object the object.
     */
    private fun handlePipe(player: Player, `object`: Scenery) {
        if (`object`.location.y == 3948) {
			sendMessage(player, "You can't do that from here.")
            return
        }
        if (player.skills.getLevel(Skills.AGILITY) < 49) {
            player.dialogueInterpreter.sendDialogue("You need an Agility level of at least 49 to do this.")
            return
        }
        player.lock(10)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            var counter = 0
            override fun pulse(): Boolean {
                val x = 3004
                when (counter++) {
                    0 -> {
                        AgilityHandler.forceWalk(player, -1, Location.create(x, 3937, 0), Location.create(x, 3940, 0), Animation.create(10580), 15, 0.0, null, 1) //10
                        println("1")
                        player.teleporter.send(Location.create(3004, 3947, 0), TeleportManager.TeleportType.INSTANT, TeleportManager.WILDY_TELEPORT)
                        println("tele")
                        counter++
                        AgilityHandler.forceWalk(player, 0, Location.create(x, 3948, 0), Location.create(x, 3950, 0), Animation.create(10579), 20, 12.5, null, 5) //20
                        println("3")
                        return true
                    }
                    2 -> {
                        player.teleporter.send(Location.create(3004, 3947, 0), TeleportManager.TeleportType.INSTANT, TeleportManager.WILDY_TELEPORT)
                        println("tele")
                        counter++
                        AgilityHandler.forceWalk(player, 0, Location.create(x, 3948, 0), Location.create(x, 3950, 0), Animation.create(10579), 20, 12.5, null, 5)
                        println("3")
                        return true
                    }
                    3 -> {
                        AgilityHandler.forceWalk(player, 0, Location.create(x, 3948, 0), Location.create(x, 3950, 0), Animation.create(10579), 20, 12.5, null, 5)
                        println("3")
                        return true
                    }
                }
                return true
            }
        })
    }

    /**
     * Handles the rope swing obstacle.
     * @param player the player.
     * @param object the object.
     */
    private fun handleRopeSwing(player: Player, `object`: Scenery) {
        if (player.location.y < 3554) {
			sendMessage(player, "You cannot do that from here.")
            return
        }
        if (ropeDelay > GameWorld.ticks) {
			sendMessage(player, "The rope is being used.")
            return
        }
        if (AgilityHandler.hasFailed(player, 1, 0.1)) {
            AgilityHandler.fail(player, 0, Location.create(3005, 10357, 0), null, getHitAmount(player), "You slip and fall to the pit below.")
            return
        }
        ropeDelay = GameWorld.ticks + 2
        player.packetDispatch.sendSceneryAnimation(`object`, Animation.create(497), true)
        AgilityHandler.forceWalk(player, 1, player.location, Location.create(3005, 3958, 0), Animation.create(751), 50, 20.0, "You skillfully swing across.", 1)
    }

    /**
     * Handles the stepping stone obstacle.
     * @param player the player.
     * @param object the object.
     */
    private fun handleSteppingStones(player: Player, `object`: Scenery) {
        lock(player, 50)
        val fail = AgilityHandler.hasFailed(player, 1, 0.3)
        val origLoc = player.location
        registerLogoutListener(player, "steppingstone"){p ->
            teleport(p, origLoc)
        }
        submitWorldPulse(object : Pulse(2, player){
            var counter = 0
            override fun pulse(): Boolean {
                if (counter == 3 && fail) {
                    AgilityHandler.fail(player, -1, Location.create(3001, 3963, 0), Animation.create(771), (player.skills.lifepoints * 0.26).toInt(), "...You lose your footing and fall into the lava.")
                    return true
                }
                AgilityHandler.forceWalk(player, if (counter == 5) 2 else -1, player.location, player.location.transform(-1, 0, 0), Animation.create(741), 10, if (counter == 5) 20.0 else 0.0, if (counter != 0) null else "You carefully start crossing the stepping stones...")
                if(++counter == 6){
                    unlock(player)
                    clearLogoutListener(player, "steppingstone")
                }
                return counter == 6
            }
        })
    }

    /**
     * Handles the log balance obstacle.
     * @param player the player.
     * @param object the object.
     */
    private fun handleLogBalance(player: Player, `object`: Scenery) {
        val failed = AgilityHandler.hasFailed(player, 1, 0.5)
        val end = if (failed) Location.create(2998, 3945, 0) else Location.create(2994, 3945, 0)
		sendMessage(player, "You walk carefully across the slippery log...")
        AgilityHandler.walk(player, if (failed) -1 else 3, player.location, end, Animation.create(155), if (failed) 0.0 else 20.0, if (failed) null else "You skillfully edge across the gap.")
        if (failed) {
            GameWorld.Pulser.submit(object : Pulse(5, player) {
                override fun pulse(): Boolean {
                    player.faceLocation(Location.create(2998, 3944, 0))
                    AgilityHandler.fail(player, 3, Location.create(2998, 10345, 0), Animation.create(770), getHitAmount(player), "You slip and fall onto the spikes below.")
                    return true
                }
            })
            return
        }
    }

    /**
     * Handles the rock climbing obstacle.
     * @param player the player.
     * @param object the object.
     */
    private fun handleRockClimb(player: Player, `object`: Scenery) {
        AgilityHandler.forceWalk(player, 4, Location.create(2994, 3937, 0), Location.create(2994, 3933, 0), Animation.create(740), 8, 0.0, "You reach the top.")
        GameWorld.Pulser.submit(object : Pulse(4, player) {
            override fun pulse(): Boolean {
                player.animator.reset()
                return true
            }
        })
    }

    override fun getDestination(node: Node, n: Node): Location? {
        when (n.id) {
            2283 -> return Location.create(3005, 3953, 0)
            37704 -> return Location.create(3002, 3960, 0)
            2297 -> return Location.create(3002, 3945, 0)
            2328 -> return n.location.transform(0, 1, 0)
        }
        return null
    }

    override fun configure() {
        SceneryDefinition.forId(2309).handlers["option:open"] = this
        SceneryDefinition.forId(2308).handlers["option:open"] = this
        SceneryDefinition.forId(2307).handlers["option:open"] = this
        SceneryDefinition.forId(2288).handlers["option:squeeze-through"] = this
        SceneryDefinition.forId(2283).handlers["option:swing-on"] = this
        SceneryDefinition.forId(37704).handlers["option:cross"] = this
        SceneryDefinition.forId(2297).handlers["option:walk-across"] = this
        SceneryDefinition.forId(2328).handlers["option:climb"] = this
    }

    override fun createInstance(player: Player): AgilityCourse {
        return WildernessCourse(player)
    }

    companion object {
        /**
         * The rope delay.
         */
        private var ropeDelay = 0
    }
}
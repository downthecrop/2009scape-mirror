package rs09.game.node.entity.skill.agility.shortcuts

import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import core.game.node.entity.skill.agility.AgilityHandler
import core.game.node.entity.skill.agility.AgilityShortcut
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.player.FaceLocationFlag
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles the Basalt Rock shortcut.
 * @author Woah
 */
@Initializable
class BasaltRockShortcut : AgilityShortcut {

    /**
     * Constructs a new {@Code BasaltRockShortcut} {@Code Object}
     */
    constructor() : super(intArrayOf(), 0, 0.0, "")

    val noJump = "I can't jump from here."

    /**
     * Constructs a new {@Code BasaltRockShortcut} {@Code Object}
     *
     * @param ids        the ids.
     * @param level      the level.
     * @param experience the experience.
     * @param options    the options.
     */
    constructor(ids: IntArray?, level: Int, experience: Double, vararg options: String?) : super(ids, level, experience, *options)

    /**
     * Basalt Rock Index : Direction South (Barbarian Outpost) to North (Lighthouse)
     * Beach -> Rock 1 -> Rock etc... -> Rocky Shore
     * 2522, 3600 R1, 3601, 3602 R2
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(BasaltRockShortcut(intArrayOf(4550), 1, 0.0, "jump-to")) //Beach South*
        configure(BasaltRockShortcut(intArrayOf(4551), 1, 0.0, "jump-across")) //Beach South Rock 1*
        configure(BasaltRockShortcut(intArrayOf(4552), 1, 0.0, "jump-across")) //South Rock 2
        configure(BasaltRockShortcut(intArrayOf(4553), 1, 0.0, "jump-across")) //South Rock 2 (other side)
        configure(BasaltRockShortcut(intArrayOf(4554), 1, 0.0, "jump-across")) //Middle Rock 3
        configure(BasaltRockShortcut(intArrayOf(4555), 1, 0.0, "jump-across")) //Middle Rock 3 (other side)
        configure(BasaltRockShortcut(intArrayOf(4556), 1, 0.0, "jump-across")) //North Rock 4
        configure(BasaltRockShortcut(intArrayOf(4557), 1, 0.0, "jump-across")) //North Rock 4 (other side)
        configure(BasaltRockShortcut(intArrayOf(4558), 1, 0.0, "jump-across")) //Rocky Shore North Rock 5*
        configure(BasaltRockShortcut(intArrayOf(4559), 1, 0.0, "jump-to")) //Rocky Shore North*
        return this
    }

    /**
     * This method enables users to jump across rocks by clicking on the one that they want to jump to
     * instead of the rock below them
     */
    override fun getDestination(n: Node?, node: Node): Location? {
        val obj = node as Scenery
        if (obj.id == 4550) {
            return Location.create(2522, 3597, 0) //Beach South*
        } else if (obj.id == 4551) {
            return Location.create(2522, 3595, 0) //Beach South Rock 1*
        } else if (obj.id == 4552) {
            return Location.create(2522, 3602, 0) //South Rock 2
        } else if (obj.id == 4553) {
            return Location.create(2522, 3600, 0) //South Rock 2 (other side)
        } else if (obj.id == 4554) {
            return Location.create(2516, 3611, 0) //Middle Rock 3
        } else if (obj.id == 4555) {
            return Location.create(2518, 3611, 0) //Middle Rock 3 (other side)
        } else if (obj.id == 4556) {
            return Location.create(2514, 3615, 0) //North Rock 4
        } else if (obj.id == 4557) {
            return Location.create(2514, 3613, 0) //North Rock 4 (other side)
        } else if (obj.id == 4558) {
            return Location.create(2514, 3619, 0) //Rocky Shore North Rock 5*
        } else if (obj.id == 4559) {
            return Location.create(2514, 3617, 0) //Rocky Shore North*
        }
        return super.getDestination(obj, node)
    }

    override fun run(player: Player, obj: Scenery, option: String, failed: Boolean) {
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                when (obj.id) {

                    4550 -> {
                        if (player.location.y <= 3596) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            AgilityHandler.forceWalk(player, -1, Location.create(2522, 3597, 0), Location.create(2522, 3595, 0), Animation.create(769), 20, 0.0, null, 1)
                        }
                        return true
                    }

                    4551 -> {
                        if (player.location == Location.create(2522, 3597, 0)) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            AgilityHandler.forceWalk(player, -1, Location.create(2522, 3595, 0), Location.create(2522, 3597, 0), Animation.create(769), 20, 0.0, null, 1)
                        }
                        return true
                    }

                    4552 -> {
                        if (player.location == Location.create(2522, 3600, 0)) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            AgilityHandler.forceWalk(player, -1, Location.create(2522, 3602, 0), Location.create(2522, 3600, 0), Animation.create(769), 20, 0.0, null)
                        }
                        return true
                    }

                    4553 -> {
                        if (player.location == Location.create(2522, 3602, 0)) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            AgilityHandler.forceWalk(player, -1, Location.create(2522, 3600, 0), Location.create(2522, 3602, 0), Animation.create(769), 20, 0.0, null)
                        }
                        return true
                    }

                    4554 -> {
                        if (player.location == Location.create(2518, 3611, 0)) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            player.faceLocation(FaceLocationFlag.getFaceLocation(player, Location.create(2518, 3611, 0)))
                            AgilityHandler.forceWalk(player, -1, Location.create(2516, 3611, 0), Location.create(2518, 3611, 0), Animation.create(769), 20, 0.0, null, 1)
                        }
                        return true
                    }

                    4555 -> {
                        if (player.location == Location.create(2516, 3611, 0)) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            player.faceLocation(FaceLocationFlag.getFaceLocation(player, Location.create(2516, 3611, 0)))
                            AgilityHandler.forceWalk(player, -1, Location.create(2518, 3611, 0), Location.create(2516, 3611, 0), Animation.create(769), 20, 0.0, null, 1)
                        }
                        return true
                    }

                    4556 -> {
                        if (player.location == Location.create(2514, 3613, 0)) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            AgilityHandler.forceWalk(player, -1, Location.create(2514, 3615, 0), Location.create(2514, 3613, 0), Animation.create(769), 20, 0.0, null)
                        }
                        return true
                    }

                    4557 -> {
                        if (player.location == Location.create(2514, 3615, 0)) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            AgilityHandler.forceWalk(player, -1, Location.create(2514, 3613, 0), Location.create(2514, 3615, 0), Animation.create(769), 20, 0.0, null)
                        }
                        return true
                    }

                    4558 -> {
                        if (player.location == Location.create(2514, 3617, 0)) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            AgilityHandler.forceWalk(player, -1, Location.create(2514, 3619, 0), Location.create(2514, 3617, 0), Animation.create(769), 20, 0.0, null)
                        }
                        return true
                    }

                    4559 -> {
                        if (player.location.y >= 3618) {
                            player.sendMessage(noJump)
                        } else {
                            player.lock(3)
                            AgilityHandler.forceWalk(player, -1, Location.create(2514, 3617, 0), Location.create(2514, 3619, 0), Animation.create(769), 20, 0.0, null, 1)
                        }
                        return true
                    }


                }
                return false
            }
        })
    }
}
package content.region.morytania.werewolfagility

import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import org.rs09.consts.NPCs

class SkullballCourse : MapArea {

    companion object {
        val attributeSkullballInstance = "skullball-instance"
        val attributeSkullballCurrentGoal = "skullball-currentgoal" // 0 to 10
        val attributeSkullballStartTime = "skullball-starttime"

        val skullballGoalIface = 379

        val startingBall = arrayOf(
                Location.create(3552, 9859),
                Location.create(3554, 9860),
                Location.create(3555, 9860),
                Location.create(3557, 9859),
        )
        /** Array of ZoneBorders with Skullball Goals */
        val skullballGoals = arrayOf(
            ZoneBorders(3555,9870,3555,9870), // rot 0
            ZoneBorders(3556,9883,3556,9883),
            ZoneBorders(3558,9891,3558,9891),
            ZoneBorders(3557,9900,3557,9900),
            ZoneBorders(3558,9906,3558,9906),
            ZoneBorders(3563,9911,3563,9911), // rot 1
            ZoneBorders(3575,9905,3575,9905), // rot 2
            ZoneBorders(3574,9888,3574,9888),
            ZoneBorders(3575,9878,3575,9878),
            ZoneBorders(3568,9864,3568,9864), // rot 3
            ZoneBorders(3563,9866,3563,9866), // End goal tunnel
        )

        /** Extract Location from ZoneBorders **/
        fun extractLoc(z :ZoneBorders): Location {
            return Location(z.northEastX, z.northEastY, 0)
        }

        /** Creates a skullball for the player to kick around. */
        fun startBall(player: Player) {
            if (getAttribute<NPC?>(player, attributeSkullballInstance, null) == null) {
                val npc = NPC(NPCs.SKULLBALL_1659)
                setAttribute(npc, "target", player)
                setAttribute(player, attributeSkullballInstance, npc)
                npc.isRespawn = false
                npc.isWalks = false
                npc.location = startingBall.random()
                npc.direction = Direction.NORTH
                npc.walkRadius = 100
                npc.init()
                clearHintIcon(player)
                registerHintIcon(player, npc)
                npc.lock(5)
                // Force walk doesn't work here because this npc isn't flagged to be forced walked.
                npc.walkingQueue.reset()
                val newLoc = npc.location.transform(Location(0, 4, 0))
                npc.walkingQueue.addPath(newLoc.x, newLoc.y)
            }
        }

        /** Clears the skullball from the world for that player. */
        fun clearBall(player: Player) {
            val npcBall = getAttribute<NPC?>(player, attributeSkullballInstance, null)
            if (npcBall != null) {
                clearHintIcon(player)
                removeAttribute(npcBall, "target")
                npcBall.clear()
                removeAttribute(player, attributeSkullballCurrentGoal)
                removeAttribute(player, attributeSkullballStartTime)
                removeAttribute(player, attributeSkullballInstance)
            }
        }

        fun nearestWerewolfSay(entity: Entity, chatText: String) {
            var werewolfNpc = findLocalNPCs(entity, intArrayOf(NPCs.SKULLBALL_TRAINER_1662)).filter { npc ->
                npc.id == NPCs.SKULLBALL_TRAINER_1662
            }.sortedWith { a, b ->
                (a.location.getDistance(entity.location) - b.location.getDistance(entity.location)).toInt()
            }.getOrNull(0)
            if (werewolfNpc != null) {
                sendChat(werewolfNpc, chatText, 0)
            }
        }

        fun randomWerewolfSay(): String {
            return listOf(
                "You have truly gifted paws!",
                "I've never seen anything like it!",
                "Claws first - think later.",
                "You need a few more skullball lessons.",
                "Keep it up!",
                "Don't give up the day job!",
//                "Look at @g[her,him] go!", // I can't get the gender to work.
                "Pathetic!",
                "What - a - goal !!!",
                "That was just plain lucky.",
            ).random()
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return skullballGoals.copyOf(skullballGoals.lastIndex).filterNotNull().toTypedArray() // remove last goal
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        // When leaving the area with the goals, the scenery of the goal is one tile adjacent to it.
        val surroundingScenery =
                getScenery(entity.location.transform(1,0,0)) ?:
                getScenery(entity.location.transform(0,1,0)) ?:
                getScenery(entity.location.transform(-1,0,0)) ?:
                getScenery(entity.location.transform(0,-1,0))
        // If that tile is the actual goal,
        if (surroundingScenery != null && surroundingScenery.id == 5146) {
            animateScenery(surroundingScenery, 1598) // anim 1599 is when skullball enters from behind.

            val player = getAttribute<Player?>(entity, "target", null)
            if (player == null) { return }
            // On the first goal, start the time.
            if (surroundingScenery.location.equals(extractLoc(skullballGoals[0]))) {
                if (getAttribute<Long?>(player, attributeSkullballStartTime, null) == null) {
                    setAttribute(player, attributeSkullballStartTime, System.currentTimeMillis())
                }
            }
            val currGoal = getAttribute(player, attributeSkullballCurrentGoal, 0)
            if (surroundingScenery.location.equals(extractLoc(skullballGoals[currGoal]))) {
                setAttribute(player, attributeSkullballCurrentGoal, currGoal + 1)
                val nextGoal = getAttribute(player, attributeSkullballCurrentGoal, 0)
                clearHintIcon(player)
                if (nextGoal < skullballGoals.size ) {
                    if (nextGoal == 10) {
                        registerHintIcon(player, extractLoc(skullballGoals[nextGoal]), 5)
                    } else {
                        registerHintIcon(player, getScenery(extractLoc(skullballGoals[nextGoal]))!!)
                    }
                }
                if (nextGoal < skullballGoals.size - 1) {
                    nearestWerewolfSay(player, randomWerewolfSay())
                }
            }
        }
    }
}

class SkullballBehavior : NPCBehavior(NPCs.SKULLBALL_1659), InteractionListener, MapArea {

    companion object {

        /** Generates a movement queue for a kicked/pushed thing. Bounces against walls. **/
        private fun generatePath(npc: NPC, kickDirection: Location, distanceToMove: Int) {
            npc.walkingQueue.reset()
            var moveDirection = kickDirection
            var nextTile = npc.location
            // For each tile to move,
            for (i in 1..distanceToMove) {
                nextTile = nextTile.transform(moveDirection)
                if (!(RegionManager.isTeleportPermitted(nextTile) || //  walkable square
                            nextTile.equals(3563, 9865, 0) || // final goal
                            nextTile.equals(3563, 9866, 0) ||  // final goal
                            getScenery(nextTile)?.id == 5146 // skeleton goal
                            )
                ) {
                    // Tile is blocked, reverse ball direction and set a walkingQueue Path.
                    // Since the ball can only move orthogonality we can flip both x and y since one will be 0
                    moveDirection = Location(-moveDirection.x, -moveDirection.y, moveDirection.z)
                    nextTile = nextTile.transform(moveDirection)
                    npc.walkingQueue.addPath(nextTile.x, nextTile.y)
                    nextTile = nextTile.transform(moveDirection)
                    if (nextTile.equals(3563, 9866, 0)) { break }
                }
            }
            npc.walkingQueue.addPath(nextTile.x, nextTile.y)
        }

        /** Moves the ball a certain distance (1,4,9 as authentic). */
        fun moveBall(player: Player, ballNpc: NPC, distance: Int) {
            if (getAttribute<Player?>(ballNpc, "target", null)?.username == player.username) {
                clearHintIcon(player)
                registerHintIcon(player, ballNpc)
                animate(player, 1606)
                generatePath(ballNpc, Location.getDelta(player.location, ballNpc.location), distance)
            } else {
                sendMessage(player, "That is not your skullball.")
            }
        }
        /** Show current goal to score. */
        fun showGoal(player: Player, ballNpc: NPC) {
            if (getAttribute<Player?>(ballNpc, "target", null)?.username == player.username) {
                val currGoal = getAttribute(player, SkullballCourse.attributeSkullballCurrentGoal, 0)
                clearHintIcon(player)
                if (currGoal < SkullballCourse.skullballGoals.size) {
                    if (currGoal == 10) {
                        registerHintIcon(player, SkullballCourse.extractLoc(SkullballCourse.skullballGoals[currGoal]), 5)
                    } else {
                        registerHintIcon(player, getScenery(SkullballCourse.extractLoc(SkullballCourse.skullballGoals[currGoal]))!!)
                    }
                }

            } else {
                sendMessage(player, "That is not your skullball.")
            }
        }

        /** Calculate the amount of time from the first goal to the final goal. */
        fun calcTime(player: Player) : String {
            val startTime = getAttribute(player, SkullballCourse.attributeSkullballStartTime, System.currentTimeMillis())
            val endTime = System.currentTimeMillis()
            val timeDiffInSecs = (endTime - startTime) / 1000
            val finalMins = timeDiffInSecs / 60
            val finalSecs = timeDiffInSecs % 60
            return String.format("%01d:%02d", finalMins, finalSecs)
        }

        /** Calculate the amount exp earned when kicked into the last goal. */
        fun calcExp(player: Player) : Int {
            val startTime = getAttribute(player, SkullballCourse.attributeSkullballStartTime, System.currentTimeMillis())
            val endTime = System.currentTimeMillis()
            val timeDiffInSecs = (((endTime - startTime) / 1000) - 240).toInt().coerceAtLeast(0)
            return (750 - timeDiffInSecs / 3).coerceAtLeast(0) // Kotlin what the fuck is this function
        }
    }

    var clearTime = 0

    override fun onRemoval(self: NPC) {
        clearTime = 0
    }

    override fun tick(self: NPC): Boolean {
        // You have 800 ticks = 8 mins to kick the ball into the goal.
        if (clearTime++ > 800) {
            clearTime = 0
            val player = getAttribute<Player?>(self, "target", null)
            if (player != null) {
                removeAttribute(player, SkullballCourse.attributeSkullballInstance)
                removeAttribute(player, SkullballCourse.attributeSkullballCurrentGoal)
                removeAttribute(player, SkullballCourse.attributeSkullballStartTime)
            }
            removeAttribute(self, "target")
            self.clear()
        }
        if (!self.location.equals(3563, 9866, 0)) {
            return true
        }
        val player = getAttribute<Player?>(self, "target", null)
        if (player == null) {
            return true
        }
        if (getAttribute(player, SkullballCourse.attributeSkullballCurrentGoal, 0) != 10) {
            sendMessage(player, "You did not score all the goals.")
            return true
        }
        SkullballCourse.nearestWerewolfSay(player, "${if(player.isMale){"He"} else {"She"}} shoots - ${if(player.isMale){"He"} else {"She"}} scores!!!!!")
        // Do all the calculation upfront:
        val calcedTime = calcTime(player)
        val finalExp = calcExp(player)
        removeAttribute(player, SkullballCourse.attributeSkullballInstance)
        removeAttribute(player, SkullballCourse.attributeSkullballCurrentGoal)
        removeAttribute(player, SkullballCourse.attributeSkullballStartTime)
        removeAttribute(self, "target")
        self.clear()

        sendMessage(player, "Well done - you've finished the skullball course!!!")
        lock(player, Animation(1605).duration)
        queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    animate(player, 1605)
                    return@queueScript delayScript(player, Animation(1605).duration)
                }

                1 -> {
                    setInterfaceText(player, calcedTime, SkullballCourse.skullballGoalIface, 5)
                    setInterfaceText(player, finalExp.toString(), SkullballCourse.skullballGoalIface, 6)
                    rewardXP(player, Skills.AGILITY, finalExp.toDouble())
                    openInterface(player, SkullballCourse.skullballGoalIface)
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
        return true
    }

    override fun defineListeners() {
        on(NPCs.SKULLBALL_1659, NPC, "tap") { player, node ->
            moveBall(player, node as NPC, 1)
            return@on true
        }

        on(NPCs.SKULLBALL_1659, NPC, "kick") { player, node ->
            moveBall(player, node as NPC, 4)
            return@on true
        }

        on(NPCs.SKULLBALL_1659, NPC, "shoot") { player, node ->
            moveBall(player, node as NPC, 9)
            return@on true
        }

        on(NPCs.SKULLBALL_1659, NPC, "show-goal") { player, node ->
            showGoal(player, node as NPC)
            return@on true
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(14234))
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            SkullballCourse.clearBall(entity)
        }
    }
}
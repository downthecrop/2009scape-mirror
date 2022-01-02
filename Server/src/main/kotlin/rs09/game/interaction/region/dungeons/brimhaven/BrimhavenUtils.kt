package rs09.game.interaction.region.dungeons.brimhaven

import api.*
import core.game.content.global.action.ClimbActionHandler
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.agility.AgilityHandler
import core.game.node.entity.skill.gather.SkillingTool
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import rs09.game.world.GameWorld

object BrimhavenUtils {
    fun getVineDestination(player: Player, node: Scenery): Location {
        if (node.rotation % 2 != 0) {
            return if (player.location.x > node.location.x) {
                node.location.transform(-1, 0, 0)
            } else node.location.transform(1, 0, 0)
        }
        return if (player.location.y > node.location.y) {
            node.location.transform(0, -1, 0)
        } else node.location.transform(0, 1, 0)
    }

    fun handleStairs(node: Scenery, player: Player){
        when(node.id){
            5094 -> ClimbActionHandler.climb(player, null, Location.create(2643, 9594, 2))
            5096 -> ClimbActionHandler.climb(player, null, Location.create(2649, 9591, 0))
            5097 -> {
                // Climb the stairs within Brimhaven Dungeon
                player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 1, 16)
                ClimbActionHandler.climb(player, null, Location.create(2636, 9510, 2))
            }
            5098 -> ClimbActionHandler.climb(player, null, Location.create(2636, 9517, 0))
        }
    }

    fun handleSteppingStones(player: Player, node: Scenery){
        if (player.skills.getLevel(Skills.AGILITY) < 12) {
            player.packetDispatch.sendMessage("You need an agility level of 12 to cross this.")
            return
        }
        player.lock(12)
        val dir = AgilityHandler.forceWalk(player, -1, player.location, node.location, Animation.create(769), 10, 0.0, null).direction
        val loc = player.location

        registerLogoutListener(player, "steppingstone"){p ->
            teleport(p, loc)
        }

        GameWorld.Pulser.submit(object : Pulse(3, player) {
            var stage = if (dir == Direction.NORTH) -1 else 0
            var direction = dir
            override fun pulse(): Boolean {
                val l = player.location
                when (stage++) {
                    1 -> direction = Direction.get(direction.toInteger() + 1 and 3)
                    3 -> direction = Direction.get(direction.toInteger() - 1 and 3)
                    5 -> if (direction == Direction.NORTH) {
                        return true
                    }
                }
                if (stage == 6) {
                    player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 1, 15)
                }
                AgilityHandler.forceWalk(player, -1, l, l.transform(direction), Animation.create(769), 10, 0.0, null)
                return stage == 6
            }

            override fun stop() {
                clearLogoutListener(player, "steppingstone")
                super.stop()
            }
        })
    }

    fun handleVines(player: Player, node: Scenery){
        val level: Int = 10 + (node.id - 5103) * 6
        if (player.skills.getLevel(Skills.WOODCUTTING) < level) {
            player.packetDispatch.sendMessage("You need a woodcutting level of $level to chop down this vine.")
            return
        }
        val tool = SkillingTool.getHatchet(player)
        if (tool == null) {
            player.packetDispatch.sendMessage("You don't have an axe to cut these vines.")
            return
        }
        player.animate(tool.animation)
        player.pulseManager.run(object : Pulse(3, player) {
            override fun pulse(): Boolean {
                if (SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(0), 2)) {
                    val destination = getVineDestination(player,node.asScenery())
                    player.lock(3)
                    player.walkingQueue.reset()
                    // Chop the vines to gain deeper access to Brimhaven Dungeon
                    player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 1, 14)
                    player.walkingQueue.addPath(destination.x, destination.y, true)
                }
                return true
            }
        })
    }
}
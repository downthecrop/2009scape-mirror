package core.game.interaction.`object`

import api.*
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.impl.ForceMovement.direction
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

/**
 * Handles the grand exchange shortcut.
 * @author Emperor
 * @author dginovker
 * @version 2.0
 */
@Initializable
class GrandExchangeShortcut : InteractionListener {
    companion object {
        /**
         * The shortcut configs
         */
        val SHORTCUTS = mapOf(
            9311 to listOf(
                Location.create(3138, 3516, 0), // Run to loc
                Location.create(3143, 3514, 0), // Crawl through tele loc
                Location.create(3144, 3514, 0), // End loc
            ),
            9312 to listOf(
                Location.create(3144, 3514, 0), // Run to loc
                Location.create(3139, 3516, 0), // Crawl through tele loc
                Location.create(3138, 3516, 0), // End loc
            )
        )
        /**
         * The climbing down animation.
         */
        private val CLIMB_DOWN = Animation.create(2589)

        /**
         * The crawling through animation.
         */
        private val CRAWL_THROUGH = Animation.create(2590)

        /**
         * The climbing up animation.
         */
        private val CLIMB_UP = Animation.create(2591)
    }

    override fun defineListeners() {
        on(SHORTCUTS.keys.toIntArray(), IntType.SCENERY, "climb-into") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 21)) {
                sendDialogue(player, "You need an Agility level of at least 21 to do this.")
                return@on true
            }
            lock(player, 4)
            val o = node as Scenery
            val path = SHORTCUTS[o.id]!!
            ForceMovement.run(player, path[0], o.location, ForceMovement.WALK_ANIMATION, CLIMB_DOWN, direction(path[0], o.location), ForceMovement.WALKING_SPEED, ForceMovement.WALKING_SPEED, false);
            runCrawlPulse(player, path)
            return@on true
        }
    }

    private fun runCrawlPulse(player: Player, path: List<Location>) {
        submitIndividualPulse(player, object : Pulse(1, player) {
            var count = 0
            var reachedStart = false
            override fun pulse(): Boolean {
                // If the player hasn't reached path[0], don't do anything
                if (!reachedStart && player.location != path[0]) {
                    return false
                }
                reachedStart = true

                when (++count) {
                    2 -> {
                        teleport(player, path[1])
                        visualize(player, CRAWL_THROUGH, -1)
                    }

                    3 -> {
                        ForceMovement.run(
                            player,
                            path[1],
                            path[2],
                            CLIMB_UP
                        )
                        // Use the shortcut under the wall, north-west of the Grand<br><br>Exchange
                        player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 1, 8)
                        unlock(player)
                        return true
                    }
                }
                return false
            }
        })
    }

}

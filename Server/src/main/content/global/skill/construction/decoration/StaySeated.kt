package content.global.skill.construction.decoration

import core.api.animate
import core.api.animationFinished
import core.api.findLocalNPCs
import core.api.forceMove
import core.api.getAttribute
import core.api.getScenery
import core.api.lock
import core.api.queueScript
import core.api.registerTimer
import core.api.removeAttributes
import core.api.removeTimer
import core.api.resetAnimator
import core.api.setAttribute
import core.api.stopExecuting
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.world.map.Direction
import core.tools.ticksToCycles
import org.rs09.consts.Animations
import org.rs09.consts.Scenery as SceneryObj

/**
 * Handles sitting, standing, and of course the continual process of being seated, for anything you can sit on in a POH.
 * Currently just stools though.
 * @author Bishop
 */

class StaySeated : PersistTimer(1 /*ticks*/, "con:stay-seated", isSoft = true)  {
    lateinit var player: Player

    companion object {

        const val STAY_SEATED_IDENTIFIER      = "con:stay-seated"
        const val ATTRIBUTE_SEATED            = "con:stay-seated:seated"
        const val ATTRIBUTE_STANDING_UP       = "con:stay-seated:standing"
        private const val ATTRIBUTE_DIRECTION = "con:stay-seated:direction"
        private const val HUMAN_STANDING_IDLE = 808 // Same as AppearanceCache.STAND_ANIM

        private data class Sittable(val sittingObj: Int, val type: SittableType, val sittingAnim: Int, val seatedAnim: Int, val standingAnim: Int)

        private enum class SittableType { BENCH, CHAIR, STOOL }

        // Register new sittables and animations here
        private val sittables = arrayOf(
            Sittable(SceneryObj.STOOL_13719, SittableType.STOOL, Animations.HUMAN_SIT_POH_STOOL_4103, Animations.HUMAN_SEATED_POH_STOOL_4107, Animations.HUMAN_STAND_POH_STOOL_4105),
            Sittable(SceneryObj.STOOL_13720, SittableType.STOOL, Animations.HUMAN_SIT_POH_STOOL_4103, Animations.HUMAN_SEATED_POH_OAK_STOOL_4108, Animations.HUMAN_STAND_POH_STOOL_4105),
        )

        // Register things here that you interact with in order to sit on something, but you don't actually sit on that thing itself
        private fun getSittingObj(nodeId: Int): Int {
            return when (nodeId) {
                in SceneryObj.CLOCKMAKER_S_BENCH_13709..SceneryObj.CLOCKMAKER_S_BENCH_13712 -> SceneryObj.STOOL_13719
                in SceneryObj.WORKBENCH_13704..SceneryObj.WORKBENCH_13705                   -> SceneryObj.STOOL_13719
                in SceneryObj.WORKBENCH_13706..SceneryObj.WORKBENCH_13708                   -> SceneryObj.STOOL_13720
                else -> nodeId
            }
        }

        private val dirKey = intArrayOf(
                Direction.SOUTH.value, Direction.SOUTH_WEST.value, Direction.WEST.value, Direction.NORTH_WEST.value,
                Direction.NORTH.value, Direction.NORTH_EAST.value, Direction.EAST.value, Direction.SOUTH_EAST.value,
        )

        private fun getOrientation(rotation: Int, type: Int): Int {
            if (type !in 10..11) return 0
            return dirKey[2 * (rotation % 4) + (type - 10)]
        }

        private fun rotate(dirValue: Int, rot45degCW: Int) : Direction {
            return Direction.get(dirKey[(dirKey.indexOf(dirValue) + rot45degCW) % 8])
        }

        fun seat(player: Player, node: Scenery) {
            val sittingObj = getSittingObj(node.id)
            val dirValue = getOrientation(node.rotation, node.type)
            val currentSittable = sittables.firstOrNull { i -> sittingObj == i.sittingObj } ?: return
            setAttribute(player, ATTRIBUTE_DIRECTION, dirValue)
            lock(player, 1)
            when (currentSittable.type) {
                SittableType.BENCH -> return // TODO: Bench functionality
                SittableType.CHAIR -> return // TODO: Chair functionality
                SittableType.STOOL -> {
                    // Right now dirValue is of a direction that points from the table to the player
                    val dirMoving = rotate(dirValue, 2)
                    val dirFacing = rotate(dirValue, 4)
                    forceMove(player, player.location, player.location.transform(dirMoving, 1),
                        0, ticksToCycles(1), dirFacing, currentSittable.sittingAnim) {
                        animate(player, currentSittable.seatedAnim)
                        setAttribute(player, ATTRIBUTE_SEATED, true)
                        registerTimer(player, StaySeated())
                    }
                }
            }
            return
        }

        // Make a player stand up, or if forced just remove all traces of this timer's functionality from the player state
        @JvmStatic
        fun unseat(player: Player, forced: Boolean) {
            val sittingObj = getScenery(player.location)?.id
            val dirValue = getAttribute(player, ATTRIBUTE_DIRECTION, 0)
            val currentSittable = sittables.firstOrNull { i -> sittingObj == i.sittingObj }
            if (forced || currentSittable == null) {
                removeTimer<StaySeated>(player)
                resetAnimator(player)
                return
            }
            setAttribute(player, ATTRIBUTE_STANDING_UP, true)
            lock(player, 1)
            when (currentSittable.type) {
                SittableType.BENCH -> return // TODO: Bench functionality
                SittableType.CHAIR -> return // TODO: Chair functionality
                SittableType.STOOL -> {
                    // Right now dirValue is of a direction that points from the table to the player
                    val dirStand = rotate(dirValue, 6)
                    animate(player, currentSittable.seatedAnim)
                    queueScript(player, 0, QueueStrength.SOFT) {
                        forceMove(player, player.location, player.location.transform(dirStand, 1),
                            0, ticksToCycles(1), dirStand, currentSittable.standingAnim) {
                            removeTimer<StaySeated>(player)
                        }
                        return@queueScript stopExecuting(player)
                    }
                }
            }
            return
        }
    }

    override fun run(entity: Entity): Boolean {
        player = entity as Player
        if (getAttribute(player, ATTRIBUTE_STANDING_UP, false)) return true
        val localObj = getScenery(player.location)?.id
        val currentSittable = sittables.firstOrNull { i -> localObj == i.sittingObj } ?: return false
        // Make a player being attacked by a random event stand up
        // The reason the player is immune in the sitting state is that he or she is inside an object
        val hostileNPCs = findLocalNPCs(player.location, 5).mapNotNull { it.takeIf { it.properties.combatPulse.getVictim() == player } }
        if (hostileNPCs.isNotEmpty()) {
            unseat(player, false)
            return false
        }
        // Don't interrupt a player doing a different animation from the sitting animation unless it's the idle animation
        val currentAnimId = player.animator.animation?.id
        if (currentAnimId != currentSittable.seatedAnim &&
            currentAnimId != HUMAN_STANDING_IDLE &&
            currentAnimId != null &&
            !animationFinished(player)) {
            return true
        }
        animate(player, currentSittable.seatedAnim)
        return true
    }

    override fun onRemoval(entity: Entity) {
        removeAttributes(entity, ATTRIBUTE_DIRECTION, ATTRIBUTE_SEATED, ATTRIBUTE_STANDING_UP)
        super.onRemoval(entity)
    }

    override fun getTimer(vararg args: Any): RSTimer {
        return StaySeated()
    }
}
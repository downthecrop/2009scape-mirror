package content.global.skill.cooking.fermenting

import core.api.*
import core.game.interaction.QueueStrength
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.ticksToCycles
import org.rs09.consts.Items
import org.rs09.consts.Scenery as SceneryIds


class AppleMushListener : InteractionListener {

    private data class BarrelLocations(
        val start: Location, // Where the player begins the mushing animation from
        val jump: Location, // Where the player ends the mushing animation at
        val tap: Location, // Where the apple barrel's tap is located
        val stand: Location // Where the player stands to collect from the tap
    )

    private val barrelLocations = mapOf(
        14747 to BarrelLocations( // Port Phasmatys
            start = Location.create(3676, 9959, 0),
            jump = Location.create(3676, 9957, 0),
            tap = Location.create(3677, 9959, 0),
            stand = Location.create(3678, 9959, 0)
        ),
        11679 to BarrelLocations( // Keldagrim
            start = Location.create(2914, 10193, 1),
            jump = Location.create(2914, 10191, 1),
            tap = Location.create(2915, 10193, 1),
            stand = Location.create(2916, 10193, 1)
        ),
        9780 to BarrelLocations( // Gnome Orchard
            start = Location.create(2484, 3375, 0),
            jump = Location.create(2484, 3373, 0),
            tap = Location.create(2485, 3375, 0),
            stand = Location.create(2486, 3375, 0)
        )
    )

    private val emptyAppleBarrels = intArrayOf(
        SceneryIds.APPLE_BARREL_7403,
        SceneryIds.APPLE_BARREL_8807
    )

    private val fullAppleBarrels = intArrayOf(
        SceneryIds.APPLE_BARREL_7404,
        SceneryIds.APPLE_BARREL_8808
    )

    private val appleTaps = intArrayOf(
        SceneryIds.BARREL_TAP_7406,
        SceneryIds.BARREL_TAP_8737
    )

    private val MUSH_ANIM = Animation(2306)

    override fun defineListeners() {

        onUseWith(SCENERY, Items.COOKING_APPLE_1955, *emptyAppleBarrels) { player, _, with ->
            if (getDynLevel(player, Skills.COOKING) < 14) {
                sendDialogue(player, "You need a Cooking level of at least 14 in order to do this.")
                return@onUseWith true
            }
            if (!inInventory(player, Items.COOKING_APPLE_1955, 4)) {
                sendDialogue(player, "You need 4 cooking apples to make apple mush.")
                return@onUseWith true
            }
            if (!inInventory(player, Items.BUCKET_1925)) {
                sendDialogue(player, "You need a bucket to collect the apple mush.")
                return@onUseWith true
            }
            if (inInventory(player, Items.COOKING_APPLE_1955, 4)) {
                startMushing(player, with.asScenery())
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.BUCKET_1925, *appleTaps) { player, _, _ ->
            if (getAttribute(player, "/save:cooking:brewing-apples", false)) {
                if (removeItem(player, Item(Items.BUCKET_1925, 1))) {
                    addItem(player, Items.APPLE_MUSH_5992, 1)
                    sendMessage(player, "You fill a bucket with apple mush.")
                }
                removeAttribute(player, "/save:cooking:brewing-apples")
                return@onUseWith true
            }
            sendMessage(player, "Nothing interesting happens.")
            return@onUseWith false
        }
    }

    private fun startMushing(player: Player, barrel: Scenery) {
        val initLoc = player.location
        val barrelLoc = barrel.location
        val locations = barrelLocations[player.location.regionId]
        val startLoc = locations?.start
        val jumpLoc = locations?.jump
        val standLoc = locations?.stand
        val tapLoc = locations?.tap
        val fastStart: Boolean = initLoc == startLoc

        // https://www.youtube.com/watch?v=7I_Ec_tQ-tc
        // Has to be soft or else you can log out and clog the barrel
        queueScript(player, 1 ,QueueStrength.SOFT) { counter ->
            when (counter) {
                0 -> {
                    stopWalk(player)
                    forceWalk(player, startLoc!!, "smart")
                    lock(player, MUSH_ANIM.duration + 7)
                    return@queueScript delayScript(player, if (fastStart) 1 else 4)
                }

                1 -> {
                    face(player, barrelLoc)
                    if (!removeItem(player, Item(Items.COOKING_APPLE_1955, 4))) { return@queueScript stopExecuting(player) }
                    setAttribute(player, "/save:cooking:brewing-apples", true)
                    return@queueScript delayScript(player, 0)
                }

                2 -> {
                    forceMove(player, startLoc!!, barrelLoc, 0, ticksToCycles(1), anim = MUSH_ANIM.id)
                    if (barrel.id == SceneryIds.APPLE_BARREL_7403) sendGraphics(414, barrelLoc)
                        else sendGraphics(413, barrelLoc)
                    swapBarrel(barrel)
                    return@queueScript delayScript(player, MUSH_ANIM.duration - 1)
                }

                3 -> {
                    forceMove(player, barrelLoc, jumpLoc!!, 0, ticksToCycles(1))
                    return@queueScript delayScript(player, 2)
                }

                4 -> {
                    stopWalk(player)
                    return@queueScript delayScript(player, 1)
                }

                5 -> {
                    unlock(player)
                    stopWalk(player)
                    forceWalk(player, standLoc!!, "smart")
                    lock(player, 5)
                    return@queueScript delayScript(player, 4)
                }

                6 -> {
                    stopWalk(player)
                    face(player, tapLoc!!)
                    return@queueScript delayScript(player, 1)
                }

                7 -> {
                    if (removeItem(player, Item(Items.BUCKET_1925, 1))) {
                        addItem(player, Items.APPLE_MUSH_5992, 1)
                        sendMessage(player, "You fill a bucket with apple mush.")
                    }
                    removeAttribute(player, "/save:cooking:brewing-apples")
                    val currentBarrel = getScenery(barrelLoc)
                    currentBarrel?.let { swapBarrel(it) }
                    unlock(player)
                    return@queueScript stopExecuting(player)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
        unlock(player)
    }

    private fun swapBarrel(barrel: Scenery) {
        val emptyIndex = emptyAppleBarrels.indexOf(barrel.id)
        val fullIndex = fullAppleBarrels.indexOf(barrel.id)
        val newId = when {
            emptyIndex != -1 -> fullAppleBarrels[emptyIndex]
            fullIndex != -1 -> emptyAppleBarrels[fullIndex]
            else -> return
        }
        replaceScenery(barrel, newId, -1)
    }

}
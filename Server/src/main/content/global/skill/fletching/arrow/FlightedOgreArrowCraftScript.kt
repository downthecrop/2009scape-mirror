package content.global.skill.fletching.arrow

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import kotlin.math.min

/**
 * Defines the queueScript for creating a flighted ogre arrow
 * @author 'Vexia
 * @param player the player.
 * @param idOfFeatherUsed the feather to attach to the shaft.
 * @param sets the amount of sets to complete.
 */
class FlightedOgreArrowCraftScript(
    private val player: Player,
    private val idOfFeatherUsed: Int,
    private val sets: Int
) {

    private val flightedOgreArrow = Items.FLIGHTED_OGRE_ARROW_2865
    private val ogreArrowShaft = Items.OGRE_ARROW_SHAFT_2864
    private val maximumFlightedOgreArrowsCraftableInOneStage = 6

    private val feathersPerArrow = 4

    private val initialDelay = 1
    private val subsequentDelay = 3

    private fun getAmountToCraftForThisStage(shaftsInInventory: Int, feathersInInventory: Int): Int {
        val limitOfCraftableArrowsDueToFeathers = feathersInInventory / feathersPerArrow

        val totalCraftableArrows = min(shaftsInInventory, limitOfCraftableArrowsDueToFeathers)
        return if (totalCraftableArrows > maximumFlightedOgreArrowsCraftableInOneStage) {
            maximumFlightedOgreArrowsCraftableInOneStage
        } else {
            totalCraftableArrows
        }
    }

    fun invoke() {
        queueScript(player, initialDelay) { stage ->
            if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)

            if (getDynLevel(player, Skills.FLETCHING) < ArrowListeners.FLIGHTED_OGRE_ARROW_LEVEL) {
                ArrowListeners.sendLevelCheckFailDialog(player, ArrowListeners.FLIGHTED_OGRE_ARROW_LEVEL)
                return@queueScript stopExecuting(player) // Check each iteration since dynLevel can change (status effects ending, skill assist session end...)
            }

            val featherAmount = amountInInventory(player, idOfFeatherUsed)
            val shaftAmount = amountInInventory(player, ogreArrowShaft)

            val amountToCraft = getAmountToCraftForThisStage(shaftAmount, featherAmount)

            if (
                removeItemsIfPlayerHasEnough(
                    player,
                    Item(ogreArrowShaft, amountToCraft),
                    Item(idOfFeatherUsed, amountToCraft * feathersPerArrow)
                )
            ) {
                when (amountToCraft) {
                    1 -> sendMessage(player, "You attach $feathersPerArrow feathers to a shaft.")
                    else -> {
                        sendMessage(
                            player,
                            "You attach ${amountToCraft * feathersPerArrow} feathers to $amountToCraft arrow shafts."
                        )
                    }
                }

                rewardXP(player, Skills.FLETCHING, amountToCraft.toDouble())
                addItem(player, flightedOgreArrow, amountToCraft)
            } else {
                return@queueScript stopExecuting(player)
            }

            if (stage >= sets - 1) {
                return@queueScript stopExecuting(player)
            }

            return@queueScript delayClock(player, Clocks.SKILLING, subsequentDelay, true)
        }
    }
}
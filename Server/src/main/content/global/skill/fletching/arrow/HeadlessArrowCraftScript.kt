package content.global.skill.fletching.arrow

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import kotlin.math.min

/**
 * Defines the queueScript for creating a headless arrow
 * @author 'Vexia
 * @param player the player.
 * @param idOfFeatherUsed the feather to attach to the shaft.
 * @param sets the amount of sets to complete.
 */
class HeadlessArrowCraftScript(
    private val player: Player,
    private val idOfFeatherUsed: Int,
    private val sets: Int
) {

    private val headlessArrow = Items.HEADLESS_ARROW_53
    private val arrowShaft = Items.ARROW_SHAFT_52
    private val maximumHeadlessArrowsCraftableInOneStage = 15

    private val initialDelay = 1
    private val subsequentDelay = 3

    private fun getAmountToCraftForThisStage(shaftsInInventory: Int, feathersInInventory: Int): Int {
        val smallerItemAmount = min(shaftsInInventory, feathersInInventory)
        return if (smallerItemAmount > maximumHeadlessArrowsCraftableInOneStage) {
            maximumHeadlessArrowsCraftableInOneStage
        } else {
            smallerItemAmount
        }
    }

    fun invoke() {
        queueScript(player, initialDelay) { stage ->
            if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)

            val featherAmount = amountInInventory(player, idOfFeatherUsed)
            val shaftAmount = amountInInventory(player, arrowShaft)

            val amountToCraft = getAmountToCraftForThisStage(shaftAmount, featherAmount)

            if (
                removeItemsIfPlayerHasEnough(
                    player,
                    Item(arrowShaft, amountToCraft),
                    Item(idOfFeatherUsed, amountToCraft)
                )
            ) {
                when (amountToCraft) {
                    1 -> sendMessage(player, "You attach a feather to a shaft.")
                    else -> {
                        sendMessage(player, "You attach feathers to $amountToCraft arrow shafts.")
                    }
                }

                rewardXP(player, Skills.FLETCHING, amountToCraft.toDouble())
                addItem(player, headlessArrow, amountToCraft)
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
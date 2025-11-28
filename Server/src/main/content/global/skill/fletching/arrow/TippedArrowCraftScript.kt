package content.global.skill.fletching.arrow

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import kotlin.math.min

/**
 * Represents the arrow head pulse to complete the headless arrow.
 * @author 'Vexia
 * @param player the player.
 * @param arrowCraftInfo provides information about the arrow we're crafting.
 * @param sets the amount of sets to complete.
 */
class TippedArrowCraftScript(
    private val player: Player,
    private val arrowCraftInfo: ArrowCraftInfo,
    private val sets: Int
) {

    private val headlessArrow = Items.HEADLESS_ARROW_53
    private val maximumArrowsCraftableInOneStage = 15
    private val initialDelay = 1
    private val subsequentDelay = 3

    private fun getAmountToCraftForThisStage(headlessArrowsInInventory: Int, tipsInInventory: Int): Int {
        val smallerItemAmount = min(headlessArrowsInInventory, tipsInInventory)
        return if (smallerItemAmount > maximumArrowsCraftableInOneStage) {
            maximumArrowsCraftableInOneStage
        } else {
            smallerItemAmount
        }
    }

    fun invoke() {
        queueScript(player, initialDelay) { stage ->
            if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)

            if (getDynLevel(player, Skills.FLETCHING) < arrowCraftInfo.level) {
                ArrowListeners.sendLevelCheckFailDialog(player, arrowCraftInfo.level)
                return@queueScript stopExecuting(player) // Check each iteration since dynLevel can change (status effects ending, skill assist session end...)
            }

            val tipsInInventory = amountInInventory(player, arrowCraftInfo.tipItemId)
            val headlessArrowsInInventory = amountInInventory(player, headlessArrow)

            val amountToCraft = getAmountToCraftForThisStage(headlessArrowsInInventory, tipsInInventory)

            if (
                removeItemsIfPlayerHasEnough(
                    player,
                    Item(headlessArrow, amountToCraft),
                    Item(arrowCraftInfo.tipItemId, amountToCraft)
                )
            ) {
                when (amountToCraft) {
                    1 -> sendMessage(player, "You attach an arrow head to an arrow shaft.")
                    else -> {
                        sendMessage(player, "You attach arrow heads to $amountToCraft arrow shafts.")
                    }
                }

                rewardXP(player, Skills.FLETCHING, arrowCraftInfo.experience * amountToCraft)
                addItem(player, arrowCraftInfo.arrowItemId, amountToCraft)
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
package content.global.skill.fletching.darts

import content.global.skill.fletching.Feathers
import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import kotlin.math.min

/**
 * Represents the queueScript to craft a dart.
 * @author ceikry
 * @param player the player.
 * @param dartCraftInfo contains info about the dart we're crafting.
 * @param sets count of sets to make
 */
class DartCraftScript(
    private val player: Player,
    private val dartCraftInfo: DartCraftInfo,
    private var sets: Int
) {

    private val initialDelay = 1
    private val subsequentDelay = 3
    private val maximumDartsCraftableInOneStage = 10

    private fun getAmountToCraftForThisStage(feathersInInventory: Int, dartTipsInInventory: Int): Int {
        val smallerItemAmount = min(feathersInInventory, dartTipsInInventory)
        return if (smallerItemAmount > maximumDartsCraftableInOneStage) {
            maximumDartsCraftableInOneStage
        } else {
            smallerItemAmount
        }
    }

    fun invoke() {
        queueScript(player, initialDelay) { stage ->
            if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)

            if (getDynLevel(player, Skills.FLETCHING) < dartCraftInfo.level) {
                DartListeners.sendLevelCheckFailDialog(player, dartCraftInfo.level)
                return@queueScript stopExecuting(player) // Check each iteration since dynLevel can change (status effects ending, skill assist session end...)
            }

            val dartTipsInInventory = amountInInventory(player, dartCraftInfo.tipItemId)
            val feathersInInventory = amountInInventory(player, Feathers.STANDARD)

            val amountToCraft = getAmountToCraftForThisStage(feathersInInventory, dartTipsInInventory)

            if (
                removeItemsIfPlayerHasEnough(
                    player,
                    Item(Feathers.STANDARD, amountToCraft),
                    Item(dartCraftInfo.tipItemId, amountToCraft)
                )
            ) {
                when (amountToCraft) {
                    1 -> sendMessage(player, "You attach a feather to a dart.")
                    else -> {
                        sendMessage(player, "You attach feathers to $amountToCraft darts.")
                    }
                }

                rewardXP(player, Skills.FLETCHING, dartCraftInfo.experience * amountToCraft)
                addItem(player, dartCraftInfo.dartItemId, amountToCraft)
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
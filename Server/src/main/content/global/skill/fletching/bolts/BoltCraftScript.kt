package content.global.skill.fletching.bolts

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import kotlin.math.min

/**
 * Represents the bolt pulse class to make bolts.
 * @author ceik
 * @param player the player.
 * @param boltCraftInfo contains information about what bolt we're crafting
 * @param feather the feather we're using to craft
 * @param sets the amount of sets to craft
 */
class BoltCraftScript(
    private val player: Player,
    private val boltCraftInfo: BoltCraftInfo,
    private val feather: Item,
    private val sets: Int
) {

    private val maximumBoltsCraftableInOneStage = 10
    private val initialDelay = 1
    private val subsequentDelay = 3

    private fun getAmountToCraftForThisSet(feathersInInventory: Int, unfinishedBoltsInInventory: Int): Int {
        val smallerItemAmount = min(feathersInInventory, unfinishedBoltsInInventory)
        return if (smallerItemAmount > maximumBoltsCraftableInOneStage) {
            maximumBoltsCraftableInOneStage
        } else {
            smallerItemAmount
        }
    }

    fun invoke() {
        queueScript(player, initialDelay) { stage ->
            if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)

            if (getDynLevel(player, Skills.FLETCHING) < boltCraftInfo.level) {
                BoltListeners.sendLevelCheckFailDialog(player, boltCraftInfo.level)
                return@queueScript stopExecuting(player) // Check each iteration since dynLevel can change (status effects ending, skill assist session end...)
            }

            val featherAmount = amountInInventory(player, feather.id)
            val unfinishedBoltAmount = amountInInventory(player, boltCraftInfo.unfinishedBoltItemId)

            val amountToCraft = getAmountToCraftForThisSet(featherAmount, unfinishedBoltAmount)

            if (
                removeItemsIfPlayerHasEnough(
                    player,
                    Item(feather.id, amountToCraft),
                    Item(boltCraftInfo.unfinishedBoltItemId, amountToCraft)
                )
            ) {
                when (amountToCraft) {
                    1 -> sendMessage(player, "You attach a feather to a bolt.")
                    else -> {
                        sendMessage(player, "You fletch $amountToCraft bolts")
                    }
                }

                rewardXP(player, Skills.FLETCHING, boltCraftInfo.experience * amountToCraft)
                addItem(player, boltCraftInfo.finishedItemId, amountToCraft)
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
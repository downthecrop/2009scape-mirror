package content.global.skill.fletching.gem

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import kotlin.math.min

/**
 * Represents the attaching of a gem tip to a premade bolt.
 * @author Ceikry
 * @param player the player.
 * @param gemBoltCraftInfo crafting info for adding the gem to the bolt
 * @param sets the number of sets to craft.
 */
class AttachGemTipToBoltScript(
    private val player: Player,
    private val gemBoltCraftInfo: GemBoltsCraftInfo,
    private val sets: Int
) {

    private val initialDelay = 1
    private val subsequentDelay = 3
    private val maximumGemBoltsCraftableInOneStage = 10

    private fun getAmountToCraftForThisPulse(untippedBoltsInInventory: Int, tipsInInventory: Int): Int {
        val smallerItemAmount = min(untippedBoltsInInventory, tipsInInventory)
        return if (smallerItemAmount > maximumGemBoltsCraftableInOneStage) {
            maximumGemBoltsCraftableInOneStage
        } else {
            smallerItemAmount
        }
    }

    fun invoke() {
        queueScript(player, initialDelay) { stage ->
            if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)

            if (getDynLevel(player, Skills.FLETCHING) < gemBoltCraftInfo.level) {
                GemBoltListeners.sendGemTipAttachLevelCheckFailDialog(player, gemBoltCraftInfo.level)
                return@queueScript stopExecuting(player) // Check each iteration since dynLevel can change (status effects ending, skill assist session end...)
            }

            val untippedBoltsInInventory = amountInInventory(player, gemBoltCraftInfo.untippedBoltItemId)
            val tipsInInventory = amountInInventory(player, gemBoltCraftInfo.tipItemId)

            val amountToCraft = getAmountToCraftForThisPulse(untippedBoltsInInventory, tipsInInventory)

            if (
                removeItemsIfPlayerHasEnough(
                    player,
                    Item(gemBoltCraftInfo.untippedBoltItemId, amountToCraft),
                    Item(gemBoltCraftInfo.tipItemId, amountToCraft)
                )
            ) {
                when (amountToCraft) {
                    1 -> sendMessage(player, "You attach the tip to the bolt.")
                    else -> {
                        sendMessage(player, "You fletch $amountToCraft bolts.")
                    }
                }

                rewardXP(player, Skills.FLETCHING, gemBoltCraftInfo.experience * amountToCraft)
                addItem(player, gemBoltCraftInfo.tippedBoltItemId, amountToCraft)
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
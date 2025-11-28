package content.global.skill.fletching.crossbow

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item

/**
 * The queue script for attaching limbs.
 * @author Ceikry
 * @param player The player
 * @param unfinishedCrossbowCraftInfo info about the unfinished (unstrung) crossbow we're crafting
 * @param amount to create
 */
class LimbingScript(
    private val player: Player,
    private val unfinishedCrossbowCraftInfo: UnfinishedCrossbowCraftInfo,
    private val amount: Int
) {

    private val initialDelay = 1
    private val subsequentDelay = 6

    fun invoke() {
        queueScript(player, initialDelay) { stage ->
            if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)

            if (getDynLevel(player, Skills.FLETCHING) < unfinishedCrossbowCraftInfo.level) {
                LimbingListener.sendLevelCheckFailDialog(player, unfinishedCrossbowCraftInfo.level)
                return@queueScript stopExecuting(player) // Check each iteration since dynLevel can change (status effects ending, skill assist session end...)
            }

            if (
                removeItemsIfPlayerHasEnough(
                    player,
                    Item(unfinishedCrossbowCraftInfo.stockItemId),
                    Item(unfinishedCrossbowCraftInfo.limbItemId)
                )
            ) {
                addItem(player, unfinishedCrossbowCraftInfo.unstrungCrossbowItemId, 1)
                rewardXP(player, Skills.FLETCHING, unfinishedCrossbowCraftInfo.experience)
                sendMessage(player, "You attach the metal limbs to the stock.")
                animate(player, unfinishedCrossbowCraftInfo.animation)
            } else {
                return@queueScript stopExecuting(player)
            }

            if (stage >= amount - 1) {
                return@queueScript stopExecuting(player)
            }

            return@queueScript delayClock(player, Clocks.SKILLING, subsequentDelay, true)
        }
    }
}
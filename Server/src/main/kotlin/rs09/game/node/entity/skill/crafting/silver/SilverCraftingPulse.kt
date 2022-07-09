package rs09.game.node.entity.skill.crafting.silver

import api.*
import api.events.ResourceProducedEvent
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import org.rs09.consts.Animations
import org.rs09.consts.Items

/**
 * Handles tick-based silver crafting logic.
 *
 * @author vddCore
 */
class SilverCraftingPulse(
    private val player: Player,
    private val product: SilverProduct,
    private val furnace: Scenery,
    private var amount: Int
) : Pulse() {
    override fun pulse(): Boolean {
        if (amount < 1) return true

        if (!inInventory(player, product.requiredItemId) || !inInventory(player, Items.SILVER_BAR_2355)) {
            return true
        }

        animate(player, Animations.HUMAN_FURNACE_SMELTING_3243)

        if (removeItem(player, Items.SILVER_BAR_2355, Container.INVENTORY)) {
            addItem(player, product.producedItemId, product.amountProduced)
            rewardXP(player, Skills.CRAFTING, product.xpReward)

            player.dispatch(
                ResourceProducedEvent(
                    product.producedItemId,
                    product.amountProduced,
                    furnace,
                    Items.SILVER_BAR_2355
                )
            )
        } else return true

        amount--
        delay = 5

        return false
    }
}
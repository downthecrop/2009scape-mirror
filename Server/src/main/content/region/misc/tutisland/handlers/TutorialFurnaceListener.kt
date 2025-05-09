package content.region.misc.tutisland.handlers

import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.entity.skill.Skills
import content.global.skill.smithing.smelting.Bar
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import core.game.interaction.IntType
import core.game.interaction.InteractionListener

/**
 * Listener for tutorial island furnace
 * @author Byte
 */
class TutorialFurnaceListener : InteractionListener {

    companion object {
        private val ANIMATION = Animation(833)

        private val ORES = intArrayOf(
            Items.TIN_ORE_438,
            Items.COPPER_ORE_436
        )
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, ORES, Scenery.FURNACE_3044) { player, _, _ ->
            if (!inInventory(player, Items.TIN_ORE_438) || !inInventory(player, Items.COPPER_ORE_436)) {
                return@onUseWith true
            }

            animate(player, ANIMATION)
            submitIndividualPulse(player, object: Pulse(2) {
                override fun pulse(): Boolean {
                    if (removeItem(player, Items.TIN_ORE_438) && removeItem(player, Items.COPPER_ORE_436)) {
                        addItem(player, Items.BRONZE_BAR_2349)
                        rewardXP(player, Skills.SMITHING, Bar.BRONZE.experience)
                        player.dispatch(ResourceProducedEvent(Items.BRONZE_BAR_2349, 1, player, Items.COPPER_ORE_436))
                        return true
                    }
                    return false
                }
            })

            return@onUseWith true
        }
    }
}

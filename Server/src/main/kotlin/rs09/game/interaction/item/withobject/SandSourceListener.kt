package rs09.game.interaction.item.withobject

import api.*
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

/**
 * Listener for collecting buckets of sand from a sand pit
 * @author Byte
 */
@Suppress("unused")
class SandSourceListener : InteractionListener {

    companion object {
        private val ANIMATION = Animation(895)

        private val SAND_PITS = intArrayOf(
            Scenery.SAND_PIT_2645,
            Scenery.SAND_PIT_4373,
            Scenery.SANDPIT_10814
        )
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.BUCKET_1925, *SAND_PITS) { player, used, _ ->
            val numEmptyBuckets = amountInInventory(player, used.id)

            var animationTrigger = 0
            runTask(player, 2, numEmptyBuckets) {
                if (removeItem(player, used)) {
                    if (animationTrigger % 2 == 0) {
                        animate(player, ANIMATION)
                    }

                    sendMessage(player, "You fill the bucket with sand.")
                    addItem(player, Items.BUCKET_OF_SAND_1783)
                }

                animationTrigger++
            }

            return@onUseWith true
        }
    }
}

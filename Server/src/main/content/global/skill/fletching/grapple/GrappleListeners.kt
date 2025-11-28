package content.global.skill.fletching.grapple

import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items

@Suppress("unused") // Reflectively loaded
class GrappleListeners : InteractionListener {
    private val mithrilBolt = Items.MITHRIL_BOLTS_9142
    private val mithrilGrappleTip = Items.MITH_GRAPPLE_TIP_9416
    private val rope = Items.ROPE_954
    private val mithrilGrapple = Items.MITH_GRAPPLE_9418
    private val mithrilGrappleWithRope = Items.MITH_GRAPPLE_9419

    override fun defineListeners() {
        onUseWith(IntType.ITEM, mithrilBolt, mithrilGrappleTip) { player, bolt, tip ->
            if (getDynLevel(player, Skills.FLETCHING) < 59) {
                sendMessage(player, "You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            queueScript(player, 0) { _ ->
                if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)
                if (removeItemsIfPlayerHasEnough(
                        player,
                        Item(mithrilBolt, 1),
                        tip.asItem()
                    )
                ) {
                    addItem(player, mithrilGrapple, 1)
                }

                delayClock(player, Clocks.SKILLING, 3)
                return@queueScript stopExecuting(player)
            }


            return@onUseWith true
        }

        onUseWith(IntType.ITEM, rope, mithrilGrapple) { player, rope, grapple ->
            if (getDynLevel(player, Skills.FLETCHING) < 59) {
                sendMessage(player, "You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            queueScript(player, 0) { _ ->
                if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)
                if (removeItemsIfPlayerHasEnough(
                        player,
                        rope.asItem(),
                        grapple.asItem()
                    )
                ) {
                    addItem(player, mithrilGrappleWithRope, 1)
                }
                delayClock(player, Clocks.SKILLING, 3)
                return@queueScript stopExecuting(player)
            }
            return@onUseWith true
        }

    }

}
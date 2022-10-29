package rs09.game.interaction.item.withobject

import api.*
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

/**
 * Listener for collecting buckets of sap from trees
 * @author Byte
 */
@Suppress("unused")
class SapCollectListener : InteractionListener {

    companion object {
        private val ANIMATION = Animation(Animations.HUMAN_FILL_BUCKET_WITH_SAP_2009)

        private val TREES = intArrayOf(
            Scenery.TREE_1276,
            Scenery.TREE_1277,
            Scenery.TREE_1278,
            Scenery.TREE_1280,
            Scenery.EVERGREEN_1315,
            Scenery.EVERGREEN_1316,
            Scenery.EVERGREEN_1318,
            Scenery.EVERGREEN_1319,
            Scenery.TREE_1330,
            Scenery.TREE_1331,
            Scenery.TREE_1332,
            Scenery.TREE_3033,
            Scenery.TREE_3034,
            Scenery.TREE_3035,
            Scenery.TREE_3036,
            Scenery.TREE_3879,
            Scenery.TREE_3881,
            Scenery.TREE_3882,
            Scenery.TREE_3883,
            Scenery.TREE_10041,
            Scenery.TREE_14308,
            Scenery.TREE_14309,
            Scenery.TREE_30132,
            Scenery.TREE_30133,
            Scenery.TREE_37477,
            Scenery.TREE_37478,
            Scenery.TREE_37652
        )
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.KNIFE_946, *TREES) { player, _, _ ->
            if (!inInventory(player, Items.BUCKET_1925)) {
                sendMessage(player, "You need a bucket to do that.")
            }

            submitIndividualPulse(player, object : Pulse(2) {
                override fun pulse(): Boolean {
                    if (removeItem(player, Items.BUCKET_1925)) {
                        animate(player, ANIMATION)
                        sendMessage(player, "You cut the tree and allow its sap to drip down into your bucket.")
                        addItem(player, Items.BUCKET_OF_SAP_4687)
                        return true
                    }
                    return false
                }
            })

            return@onUseWith true
        }

        on(Items.BUCKET_OF_SAP_4687, IntType.ITEM, "empty") { player, node ->
            val item = node as Item

            if (item.slot < 0) {
                return@on false
            }

            replaceSlot(player, item.slot, Item(Items.BUCKET_1925))
            sendMessage(player, "You empty the contents of the bucket on the floor.")

            return@on true
        }
    }
}

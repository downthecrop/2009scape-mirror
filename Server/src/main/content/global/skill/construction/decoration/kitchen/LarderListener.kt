package content.global.skill.construction.decoration.kitchen

import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items
import org.rs09.consts.Scenery

class LarderListener : InteractionListener {

    companion object {
        private val wooden_larder_items = listOf(
            "Tea Leaves" to Items.TEA_LEAVES_7738,
            "Bucket of Milk" to Items.BUCKET_OF_MILK_1927
        )
        private val oak_larder_items = wooden_larder_items + listOf(
            "Eggs" to Items.EGG_1944,
            "Pot of Flour" to Items.POT_OF_FLOUR_1933
        )
        private val teak_larder_items = oak_larder_items + listOf(
            "Potatoes" to Items.POTATO_1942,
            "Garlic" to Items.GARLIC_1550,
            "Onions" to Items.ONION_1957,
            "Cheese" to Items.CHEESE_1985
        )

        val larders = mapOf(
            Scenery.LARDER_13565 to wooden_larder_items,
            Scenery.LARDER_13566 to oak_larder_items,
            Scenery.LARDER_13567 to teak_larder_items
        )
    }

    override fun defineListeners() {
        on(larders.keys.toIntArray(), IntType.SCENERY, "Search") { player, node ->
            openDialogue(player, LarderDialogue(node.id))
            return@on true
        }
    }

    class LarderDialogue(shelfId: Int) : AbstractContainer(shelfId, larders, "larder")
}
package content.global.skill.cooking.recipe

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs09.consts.Items

class CurryListener : InteractionListener {

    val added = intArrayOf(Items.CURRY_LEAF_5970, Items.SPICE_2007)

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.UNCOOKED_STEW_2001, *added) { player, used, with ->
            val product = Items.UNCOOKED_CURRY_2009
            val amountAdded = when (with.id) {
                Items.CURRY_LEAF_5970 -> 3
                Items.SPICE_2007 -> 1
                else -> 0
            }
            val level = 60
            val experience = 0.0
            val message = "You mix the spice with the stew."
            val failMessage = "You need a Cooking level of at least $level in order to do this."

            return@onUseWith standardMix(
                player,
                used.asItem(),
                Item(with.id, amountAdded),
                product,
                level,
                experience,
                message,
                failMessage
            )
        }
    }
}
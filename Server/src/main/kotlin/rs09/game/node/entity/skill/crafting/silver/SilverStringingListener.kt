package rs09.game.node.entity.skill.crafting.silver

import api.addItem
import api.removeItem
import core.game.node.Node
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * Handles ball of wool <-> stringable silver items interaction.
 *
 * @author vddCore
 */
class SilverStringingListener : InteractionListener {
    companion object {
        private val STRINGABLE_PRODUCTS = intArrayOf(
            Items.UNSTRUNG_SYMBOL_1714,
            Items.UNSTRUNG_EMBLEM_1720
        )
    }

    private fun stringSilverProduct(player: Player, used: Node, with: Node): Boolean {
        SilverProduct.forProductID(with.id)?.let {
            if (removeItem(player, with.id) && removeItem(player, used.id)) {
               addItem(player, it.strungId)
            }
        }

        return true
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.BALL_OF_WOOL_1759, *STRINGABLE_PRODUCTS, handler = ::stringSilverProduct)
    }
}
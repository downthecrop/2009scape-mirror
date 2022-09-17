package rs09.game.node.entity.skill.cooking


import api.*

import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType


/**
 * @author bushtail
 * That's-a spicy kebab!
 */
class SaucyKebabListener : InteractionListener {
    val sauce = Items.RED_HOT_SAUCE_4610
    val kebabArr = intArrayOf(
        Items.KEBAB_1971,
        Items.UGTHANKI_KEBAB_1883,
        Items.UGTHANKI_KEBAB_1885
    )

    override fun defineListeners() {
        onUseWith(IntType.ITEM, kebabArr, sauce) { player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                return@onUseWith addItem(player, Items.SUPER_KEBAB_4608)
            }
            return@onUseWith false
        }
    }
}
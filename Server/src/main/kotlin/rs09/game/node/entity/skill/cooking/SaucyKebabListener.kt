package rs09.game.node.entity.skill.cooking


import api.*

import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener


/**
 * @author bushtail
 * That's-a spicy kebab!
 */
class SaucyKebabListener : InteractionListener {
    val sauce = Items.RED_HOT_SAUCE_4610
    val kebabMap = hashMapOf(
        Items.KEBAB_1971 to Items.KEBAB_1972,
        Items.UGTHANKI_KEBAB_1883 to Items.UGTHANKI_KEBAB_1886
    )

    override fun defineListeners() {
        onUseWith(ITEM, kebabMap.keys.toIntArray(), sauce) { player, used, with ->
            if(removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                return@onUseWith addItem(player, Items.SUPER_KEBAB_4608)
            }
            return@onUseWith false
        }
    }
}
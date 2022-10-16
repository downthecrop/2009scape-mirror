package rs09.game.interaction.item.withobject

import api.addItem
import api.animate
import api.removeItem
import api.sendMessage
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener
import java.util.*

/**
 * Listener for Cauldron of Thunder interaction
 * @author Byte
 */
@Suppress("unused")
class CauldronOfThunderListener : InteractionListener {

    companion object {
        private val ANIMATION = Animation(833)

        private val ITEM_MAP = mapOf(
            Items.RAW_BEEF_2132 to Items.ENCHANTED_BEEF_522,
            Items.RAW_RAT_MEAT_2134 to Items.ENCHANTED_RAT_MEAT_523,
            Items.RAW_BEAR_MEAT_2136 to Items.ENCHANTED_BEAR_MEAT_524,
            Items.RAW_CHICKEN_2138 to Items.ENCHANTED_CHICKEN_525
        )
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, ITEM_MAP.keys.toIntArray(), Scenery.CAULDRON_OF_THUNDER_2142) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }

            animate(player, ANIMATION)
            addItem(player, ITEM_MAP[used.id]!!)
            sendMessage(player, "You dip the " + used.name.lowercase(Locale.getDefault()) + " in the cauldron.")

            return@onUseWith true
        }
    }
}

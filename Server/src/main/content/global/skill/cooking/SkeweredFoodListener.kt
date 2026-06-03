package content.global.skill.cooking

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items

class SkeweredFoodListener : InteractionListener {
    val skewerMap = hashMapOf(
        Items.RAW_CHOMPY_2876 to Items.SKEWERED_CHOMPY_7230,
        Items.RAW_RABBIT_3226 to Items.SKEWERED_RABBIT_7224,
        Items.RAW_BIRD_MEAT_9978 to Items.SKEWERED_BIRD_MEAT_9984,
        Items.RAW_BEAST_MEAT_9986 to Items.SKEWERED_BEAST_9992
    )

    override fun defineListeners() {
        onUseWith(IntType.ITEM, skewerMap.keys.toIntArray(), Items.IRON_SPIT_7225) { player, used, with ->
            val level = when (used.id) {
                Items.RAW_BIRD_MEAT_9978 -> 11
                Items.RAW_RABBIT_3226 -> 16
                Items.RAW_BEAST_MEAT_9986 -> 21
                Items.RAW_CHOMPY_2876 -> 30
                else -> 0
            }

            if (getDynLevel(player, Skills.COOKING) < level) {
                sendMessage(player, "You need a Cooking level of at least $level in order to do this.")
                return@onUseWith true
            }
            if (removeItem(player, used.id) && removeItem(player, with.id)) {
                skewerMap[used.id]?.let { addItem(player, it) }
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
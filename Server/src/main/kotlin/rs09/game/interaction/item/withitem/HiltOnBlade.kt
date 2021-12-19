package rs09.game.interaction.item.withitem

import api.*
import core.tools.StringUtils
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class HiltOnBlade : InteractionListener() {
    override fun defineListeners() {
        val HILTS = intArrayOf(Items.ARMADYL_HILT_11702, Items.ZAMORAK_HILT_11708, Items.BANDOS_HILT_11704, Items.SARADOMIN_HILT_11706)

        onUseWith(ITEM, HILTS, Items.GODSWORD_BLADE_11690) {player, used, with ->
            if (removeItem(player, used.asItem(), Container.INVENTORY) && removeItem(player, with.asItem(), Container.INVENTORY)) {
                val godsword = if(used.id > 11690) used.id - 8 else with.id - 8
                addItem(player, godsword)
                sendMessage(player, "You attach the hilt to the blade and make a ${if(StringUtils.isPlusN(getItemName(godsword))) "an" else "a"} ${getItemName(godsword)}.")
            }

            return@onUseWith true
        }
    }
}
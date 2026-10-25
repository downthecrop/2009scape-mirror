package content.global.skill.construction.decoration.questhall

import content.data.EnchantedJewellery
import content.global.handlers.item.EnchantedJewelleryListener.EnchantedJewelleryDialogueFile
import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.Scenery

class MountedGlory : InteractionListener {
    override fun defineListeners() {
        on(Scenery.AMULET_OF_GLORY_13523, IntType.SCENERY, "Rub") { player, _ ->
            openDialogue(player,EnchantedJewelleryDialogueFile(EnchantedJewellery.AMULET_OF_GLORY, Item(Items.AMULET_OF_GLORY4_1712), false, false))
            return@on true
        }
    }
}

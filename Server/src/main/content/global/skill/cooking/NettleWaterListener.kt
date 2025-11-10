package content.global.skill.cooking

import org.rs09.consts.Items
import core.api.replaceSlot
import core.api.removeItem
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item

class NettleWaterListener : InteractionListener {
	override fun defineListeners() {
		onUseWith(IntType.ITEM, Items.BOWL_OF_WATER_1921, Items.NETTLES_4241) { player, used, with ->
			replaceSlot(player, used.asItem().slot, Item(Items.NETTLE_WATER_4237), used.asItem())
			removeItem(player, with.asItem())
			return@onUseWith true
		}
	}
}
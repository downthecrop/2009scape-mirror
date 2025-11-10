package content.global.skill.cooking

import org.rs09.consts.Items
import core.api.replaceSlot
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item

class NettleTeaListener : InteractionListener {

	override fun defineListeners() {
		onUseWith(IntType.ITEM, Items.EMPTY_CUP_1980, Items.NETTLE_TEA_4239) { player, used, with ->
			replaceSlot(player, with.asItem().slot, Item(Items.BOWL_1923), with.asItem())
			replaceSlot(player, used.asItem().slot, Item(Items.CUP_OF_TEA_4242), used.asItem())
			return@onUseWith true
		}
	}
}
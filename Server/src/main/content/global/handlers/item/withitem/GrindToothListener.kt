package content.global.handlers.item.withitem

import core.api.*
import org.rs09.consts.Items
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor
import core.game.node.item.Item

class GrindToothListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.PESTLE_AND_MORTAR_233, Items.SUQAH_TOOTH_9079) { player, _, with ->
            val item = with as Item
            val res = replaceSlot(player, item.slot, Item(Items.GROUND_TOOTH_9082))
            if (res?.id == Items.SUQAH_TOOTH_9079) {
                sendMessage(player, "You grind the suqah tooth to dust.") //https://www.youtube.com/watch?v=RdIcNH50v7I
            } else {
                PlayerMonitor.log(player, LogType.DUPE_ALERT, "Player ground item ${res?.name} instead of a suqah tooth - potential slot-based manipulation attempt")
            }
            return@onUseWith true
        }
    }
}
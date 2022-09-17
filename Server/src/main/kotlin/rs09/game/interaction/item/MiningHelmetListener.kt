package rs09.game.interaction.item

import api.*
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import org.rs09.consts.Items

class MiningHelmetListener : InteractionListener {
    override fun defineListeners() {
        on(Items.MINING_HELMET_5013, IntType.ITEM, "drop") {player, _ ->
            val removed = removeItem(player, Items.MINING_HELMET_5013)
            if (removed) produceGroundItem(player, Items.MINING_HELMET_5014)
            sendMessage(player, "The helmet goes out as you drop it.")
            return@on true
        }
    }
}
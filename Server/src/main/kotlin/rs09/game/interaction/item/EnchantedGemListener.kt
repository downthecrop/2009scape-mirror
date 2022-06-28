package rs09.game.interaction.item

import api.*
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class EnchantedGemListener : InteractionListener {
    override fun defineListeners() {
        on(Items.ENCHANTED_GEM_4155, ITEM, "check") { player, _ ->
            sendMessage(player,"You're assigned to kill ${getSlayerTaskName(player)}s; only ${getSlayerTaskKillsRemaining(player)} more to go.")
            return@on true
        }
    }
}
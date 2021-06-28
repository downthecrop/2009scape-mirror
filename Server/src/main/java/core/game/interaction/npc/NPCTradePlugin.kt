package core.game.interaction.npc

import api.ContentAPI
import core.cache.def.impl.NPCDefinition
import core.game.component.Component
import core.plugin.Initializable
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Plugin
import core.game.node.entity.skill.crafting.TanningProduct
import rs09.game.interaction.InteractionListener

/**
 * Represents the plugin used for an npc with the trade option.
 * @author Ceikry
 * @version 1.0
 */
class NPCTradePlugin : InteractionListener() {
    override fun defineListeners() {
        on(NPC, "trade", "shop"){player, node ->
            val npc = node as NPC
            if (npc.id == 2824) {
                TanningProduct.open(player, 2824)
                return@on true
            }
            if (npc.id == 7601) {
                ContentAPI.openInterface(player, 732)
                return@on true
            }
            return@on npc.openShop(player)
        }
    }

    override fun defineDestinationOverrides() {
        setDest(NPC,"trade","shop"){node ->
            val npc = node as NPC
            if (npc.getAttribute("facing_booth", false)) {
                val offsetX = npc.direction.stepX shl 1
                val offsetY = npc.direction.stepY shl 1
                return@setDest npc.location.transform(offsetX, offsetY, 0)
            }
            return@setDest node.location
        }
    }
}
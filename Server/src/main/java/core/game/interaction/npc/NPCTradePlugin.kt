package core.game.interaction.npc

import api.*
import core.cache.def.impl.NPCDefinition
import core.game.component.Component
import core.game.content.dialogue.FacialExpression
import core.plugin.Initializable
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Plugin
import core.game.node.entity.skill.crafting.TanningProduct
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

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
                openInterface(player, 732)
                return@on true
            }
            return@on npc.openShop(player)
        }

        on(NPCs.SIEGFRIED_ERKLE_933, NPC, "trade"){player, node ->
            val points = getQP(player)
            if(points < 40){
                sendNPCDialogue(player, NPCs.SIEGFRIED_ERKLE_933, "I'm sorry, adventurer, but you need 40 quest points to buy from me.")
                return@on true
            }
            node.asNpc().openShop(player)
            return@on true
        }

        on(NPCs.FUR_TRADER_1316, NPC,"trade") { player, node ->
            if (!isQuestComplete(player, "Fremennik Trials")) {
                sendNPCDialogue(player, NPCs.FUR_TRADER_1316, "I don't sell to outerlanders.", FacialExpression.ANNOYED).also { END_DIALOGUE }
            } else {
                END_DIALOGUE.also { node.asNpc().openShop(player) }
            }
            return@on true
        }

        on(NPCs.FISH_MONGER_1315, NPC,"trade") { player, node ->
            if (!isQuestComplete(player, "Fremennik Trials")) {
                sendNPCDialogue(player, NPCs.FISH_MONGER_1315, "I don't sell to outerlanders.", FacialExpression.ANNOYED).also { END_DIALOGUE }
            } else {
                END_DIALOGUE.also { node.asNpc().openShop(player) }
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(NPC,"trade","shop"){_,node ->
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
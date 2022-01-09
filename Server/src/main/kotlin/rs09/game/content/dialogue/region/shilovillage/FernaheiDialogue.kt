package rs09.game.content.dialogue.region.shilovillage

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class FernaheiDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY,"Welcome to Fernahei's Fishing Shop Bwana!",
            "Would you like to see my items?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                options("Yes please!", "No, but thanks for the offer.").also { stage++ }
            }

            1 -> when (buttonId) {
                1 -> player(FacialExpression.FRIENDLY, "Yes, please.").also { stage = 10 }
                2 -> player(FacialExpression.FRIENDLY, "No, but thanks for the offer.").also { stage = 20 }
            }

            10 -> {
                end().also { npc.openShop(player) }
            }

            20 -> {
                npc(FacialExpression.FRIENDLY, "That's fine, and thanks for your interest.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FernaheiDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FERNAHEI_517)
    }
}
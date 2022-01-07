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
class ObliDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY,"Welcome to Obli's General Store Bwana!",
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
                2 -> player(FacialExpression.FRIENDLY, "No, but thanks for the offer.").also { stage = 99 }
            }

            10 -> {
                end().also { npc.openShop(player) }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ObliDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OBLI_516)
    }
}
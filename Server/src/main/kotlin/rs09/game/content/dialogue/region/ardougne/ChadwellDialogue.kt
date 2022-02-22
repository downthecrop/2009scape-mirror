package rs09.game.content.dialogue.region.ardougne

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
class ChadwellDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY,"Good day. What can I get you?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Let's see what you've got.", "Nothing thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FacialExpression.FRIENDLY, "Let's see what you've got.").also { stage = 5 }
                2 -> player(FacialExpression.FRIENDLY, "Nothing thanks.").also { stage = 10 }
            }

            5 -> end().also { npc.openShop(player) }

            10 -> npc(FacialExpression.FRIENDLY, "Okay then.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ChadwellDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CHADWELL_971)
    }
}

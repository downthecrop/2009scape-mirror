package rs09.game.content.dialogue.region.witchaven

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
class CarolineDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY,"Hello again.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.FRIENDLY, "Hello traveller, how are you?").also { stage++ }
            1 -> player(FacialExpression.FRIENDLY, "Not bad thanks, yourself?").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "I'm good. Busy as always looking after Kent and Kennith but no complaints.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return CarolineDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAROLINE_696)
    }
}

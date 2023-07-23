package content.region.asgarnia.burthorpe.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Unferth Dialogue
 * @author ovenbread
 */
@Initializable
class UnferthDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
//            when (stage) {
//                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi Unferth. How are you doing?").also { stage++ }
//                1 -> npcl(FacialExpression.FRIENDLY, "It's just not the same without Bob around.").also { stage++ }
//                2 -> playerl(FacialExpression.FRIENDLY, "I'm so sorry Unferth.").also { stage++ }
//                3 -> npcl(FacialExpression.FRIENDLY, "Gertrude asked me if I'd like one of her new kittens. I don't think I'm ready for that yet.").also { stage++ }
//                4 -> playerl(FacialExpression.FRIENDLY, "Give it time. Things will get better, I promise.").also { stage++ }
//                5 -> npcl(FacialExpression.FRIENDLY, "Thanks ${player.name}.").also { stage = END_DIALOGUE }
//            }
        when (stage) {
            START_DIALOGUE -> npcl(FacialExpression.GUILTY, "Hello.").also { stage++ }
            1 -> playerl(FacialExpression.FRIENDLY, "What's wrong?").also { stage++ }
            2 -> npcl(FacialExpression.GUILTY, "It's fine. Nothing for you to worry about.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return UnferthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.UNFERTH_2655)
    }
}

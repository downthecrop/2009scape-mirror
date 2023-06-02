package content.region.kandarin.ardougne.plaguecity.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class NurseSarahDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FacialExpression.FRIENDLY, "Hello my dear, how are you feeling?").also { stage++ }
            1 -> playerl(FacialExpression.FRIENDLY, "Hello my dear, how are you feeling?").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "Well in that case I'd better get back to work. Take care.").also { stage++ }
            3 -> playerl(FacialExpression.FRIENDLY, "You too.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return NurseSarahDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NURSE_SARAH_373)
    }

}
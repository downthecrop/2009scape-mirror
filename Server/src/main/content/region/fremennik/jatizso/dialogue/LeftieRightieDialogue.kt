package content.region.fremennik.jatizso.dialogue

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE

class LeftieRightieDialogue() : DialogueFile() {
    val rightie = NPCs.GUARD_5491
    val leftie = NPC(NPCs.GUARD_5492)
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Are you all right? Leftie?").also { stage++ }
            1 -> npc2("No, I'm on the left.").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Only from your perspective. Someone entering the gate should call you Rightie, right Leftie?").also { stage++ }
            3 -> npc2("Right, Rightie. So you'd be Leftie not Rightie, right?").also { stage++ }
            4 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "That's right Leftie, that's right.").also { stage++ }
            5 -> npc2("Rightie-oh Rightie, or should I call you Leftie?").also { stage++ }
            6 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "No, Rightie's fine Leftie.").also { stage++ }
            7 -> playerl(core.game.dialogue.FacialExpression.ANGRY, "Aaagh! Enough! If either of you mention left or right in my presence I'll have to scream! Can I come through the gate?" ).also { stage++ }
            8 -> npc2("Don't let us stop you.").also { stage++ }
            9 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Yes, head right on in, sir.").also { stage++ }
            10 -> playerl(core.game.dialogue.FacialExpression.ANGRY, "You said it! You said it! ARRRRRRRRGH!").also { stage = END_DIALOGUE }
        }
    }

    fun npc2(messages: String){
        sendNormalDialogue(leftie, core.game.dialogue.FacialExpression.NEUTRAL, *splitLines(messages))
    }
}
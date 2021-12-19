package rs09.game.content.dialogue.region.jatizso

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class LeftieRightieDialogue() : DialogueFile() {
    val rightie = NPCs.GUARD_5491
    val leftie = NPC(NPCs.GUARD_5492)
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> npcl(FacialExpression.NEUTRAL, "Are you all right? Leftie?").also { stage++ }
            1 -> npc2("No, I'm on the left.").also { stage++ }
            2 -> npcl(FacialExpression.NEUTRAL, "Only from your perspective. Someone entering the gate should call you Rightie, right Leftie?").also { stage++ }
            3 -> npc2("Right, Rightie. So you'd be Leftie not Rightie, right?").also { stage++ }
            4 -> npcl(FacialExpression.NEUTRAL, "That's right Leftie, that's right.").also { stage++ }
            5 -> npc2("Rightie-oh Rightie, or should I call you Leftie?").also { stage++ }
            6 -> npcl(FacialExpression.NEUTRAL, "No, Rightie's fine Leftie.").also { stage++ }
            7 -> playerl(FacialExpression.ANGRY, "Aaagh! Enough! If either of you mention left or right in my presence I'll have to scream! Can I come through the gate?" ).also { stage++ }
            8 -> npc2("Don't let us stop you.").also { stage++ }
            9 -> npcl(FacialExpression.NEUTRAL, "Yes, head right on in, sir.").also { stage++ }
            10 -> playerl(FacialExpression.ANGRY, "You said it! You said it! ARRRRRRRRGH!").also { stage = END_DIALOGUE }
        }
    }

    fun npc2(messages: String){
        sendNormalDialogue(leftie, FacialExpression.NEUTRAL, *splitLines(messages))
    }
}
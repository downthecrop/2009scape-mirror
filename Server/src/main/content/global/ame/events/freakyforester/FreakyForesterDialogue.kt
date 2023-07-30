package content.global.ame.events.freakyforester

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class FreakyForesterDialogue : DialogueFile(){

    override fun handle(componentID: Int, buttonID: Int) {
                if (removeItem(player!!, Items.RAW_PHEASANT_6179) && !getAttribute(player!!, FreakUtils.freakComplete, false)) {
                    npcl(FacialExpression.NEUTRAL, "That's not the right one.").also { stage = END_DIALOGUE }
                    setAttribute(player!!, FreakUtils.pheasantKilled, false)
                } else if (removeItem(player!!, Items.RAW_PHEASANT_6178) || getAttribute(player!!, FreakUtils.freakComplete, false)) {
                    npcl(FacialExpression.NEUTRAL, "Thanks, ${player!!.username}, you may leave the area now.").also { stage = END_DIALOGUE }
                    sendChat(findNPC(FreakUtils.freakNpc)!!, "Thanks, ${player!!.username}, you may leave the area now.")
                    setAttribute(player!!, FreakUtils.freakComplete, true)
                } else when (getAttribute(player!!, FreakUtils.freakTask, -1)) {
                    NPCs.PHEASANT_2459 -> sendNPCDialogue(player!!, FreakUtils.freakNpc, "Hey there ${player!!.username}. Can you kill the one tailed pheasant please. Bring me the raw pheasant when you're done.").also { stage = END_DIALOGUE }
                    NPCs.PHEASANT_2460 -> sendNPCDialogue(player!!, FreakUtils.freakNpc, "Hey there ${player!!.username}. Can you kill the two tailed pheasant please. Bring me the raw pheasant when you're done.").also { stage = END_DIALOGUE }
                    NPCs.PHEASANT_2461 -> sendNPCDialogue(player!!, FreakUtils.freakNpc, "Hey there ${player!!.username}. Can you kill the three tailed pheasant please. Bring me the raw pheasant when you're done.").also { stage = END_DIALOGUE }
                    NPCs.PHEASANT_2462 -> sendNPCDialogue(player!!, FreakUtils.freakNpc, "Hey there ${player!!.username}. Can you kill the four tailed pheasant please. Bring me the raw pheasant when you're done.").also { stage = END_DIALOGUE }
                }
    }
}
package content.region.kandarin.ardougne.plaguecity.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class KilronDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.questRepository.getQuest("Plague City").isCompleted(player)){
            npcl(FacialExpression.FRIENDLY, "Looks like you won't be needing the rope ladder any more, adventurer. I heard it was you who started the revolution and freed West Ardougne!").also { stage = END_DIALOGUE }
        } else {
            playerl(FacialExpression.FRIENDLY, "Hello there.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FacialExpression.FRIENDLY, "Hello.").also { stage++ }
            1 -> playerl(FacialExpression.FRIENDLY, "How are you?").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "Busy.").also { stage = END_DIALOGUE }
        }
        return true
    }

        /* After Biohazard and before Plague's End
        0 -> playerl(FacialExpression.FRIENDLY, "Hello Kilron.").also { stage++ }
        1 -> npcl(FacialExpression.FRIENDLY, "Hello traveller. Do you need to go back over?").also { stage++ }
        2 -> options("Not yet Kilron", "Yes I do").also { stage++ }
        3 -> when(buttonID) {
        1 -> playerl(FacialExpression.FRIENDLY, "Not yet Kilron.").also { stage = 4 }
        2 -> playerl(FacialExpression.FRIENDLY, "Yes I do.").also { stage = 5 }
        }
        4 -> npcl(FacialExpression.FRIENDLY, "Okay, just give me the word.").also { stage = END_DIALOGUE }
        5 -> npcl(FacialExpression.FRIENDLY, "Okay, quickly now!").also { stage = END_DIALOGUE }
        */

    override fun newInstance(player: Player?): DialoguePlugin {
        return KilronDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KILRON_349)
    }

}
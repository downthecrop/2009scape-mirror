package content.region.kandarin.ardougne.quest.biohazard.dialogue

import content.data.Quests
import core.api.getQuestStage
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class KilronDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FacialExpression.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (getQuestStage(player, Quests.BIOHAZARD) > 0){
            when(stage){
                0 -> playerl(FacialExpression.FRIENDLY, "Hello Kilron.").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Hello traveller. Do you need to go back over?").also { stage++ }
                2 -> showTopics(
                    Topic("Not yet Kilron.", 4),
                    Topic("Yes I do.", 5)
                )
                4 -> npcl(FacialExpression.FRIENDLY, "Okay, just give me the word.").also { stage = END_DIALOGUE }
                5 -> npcl(FacialExpression.FRIENDLY, "Okay, quickly now!").also { stage = END_DIALOGUE }
            }

        }
        else {
            when (stage) {
                0 -> npcl(FacialExpression.FRIENDLY, "Hello.").also { stage++ }
                1 -> playerl(FacialExpression.FRIENDLY, "How are you?").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "Busy.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KILRON_349)
    }

}
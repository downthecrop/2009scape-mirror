package content.region.kandarin.ardougne.quest.plaguecity.dialogue

import content.data.Quests
import core.api.getQuestStage
import core.api.setQuestStage
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class MilliRehnisonDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.questRepository.getStage(Quests.PLAGUE_CITY) == 9) {
            playerl(FacialExpression.FRIENDLY, "Hello. Your parents say you saw what happened to Elena...").also { stage++ }
        } else {
            npcl(FacialExpression.CHILD_FRIENDLY, "Any luck finding Elena yet?").also { stage++ }
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {

            9 -> when(stage) {
                // Wordwrap doesn't match using npcl
                1 -> npc(FacialExpression.CHILD_SAD, "*sniff* Yes I was near the south east corner when I",
                    "saw Elena walking by. I was about to run to greet her",
                    "when some men jumped out. They shoved a sack over",
                    "her head and dragged her into a building.").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Which building?").also { stage++ }
                3 -> npcl(FacialExpression.CHILD_NORMAL, "It was the boarded up building with no windows in the south east corner of West Ardougne.").also {
                    stage = END_DIALOGUE
                    setQuestStage(player!!, Quests.PLAGUE_CITY, 11)
                }
            }

            in 10..98 -> when (stage) {
                1 -> playerl(FacialExpression.FRIENDLY, "Not yet...").also { stage++ }
                2 -> npcl(FacialExpression.CHILD_FRIENDLY, "I wish you luck, she did a lot for us.").also { stage = END_DIALOGUE }
            }

            in 99..100 -> when (stage) {
                1 -> playerl(FacialExpression.FRIENDLY, "Yes, she is safe at home now.").also { stage++ }
                2 -> npcl(FacialExpression.CHILD_FRIENDLY, "That's good to hear, she helped us a lot.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MILLI_REHNISON_724)
}
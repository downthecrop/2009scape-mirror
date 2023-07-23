package content.region.kandarin.ardougne.plaguecity.quest.elena

import core.api.getQuestStage
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class TedRehnisonDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.questRepository.getStage("Plague City") == 9) {
            playerl(FacialExpression.NEUTRAL, "Hi, I hear a woman called Elena is staying here.").also { stage++ }
        } else if (player.questRepository.getStage("Plague City") > 9) {
            npcl(FacialExpression.FRIENDLY, "Any luck finding Elena yet?").also { stage++ }
        } else {
            npcl(FacialExpression.FRIENDLY, "Go away. We don't want any.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, PlagueCity.PlagueCityQuest)) {

            9 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Yes she was staying here, but slightly over a week ago she was getting ready to go back. However she never managed to leave. My daughter Milli was playing near the west wall when she saw some shadowy figures jump").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "out and grab her. Milli is upstairs if you wish to speak to her.").also { stage = END_DIALOGUE }
            }

            in 10..98 -> when (stage) {
                1 -> playerl(FacialExpression.FRIENDLY, "Not yet...").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "I wish you luck, she did a lot for us.").also { stage = END_DIALOGUE }
            }

            in 99..100 -> when (stage) {
                1 -> playerl(FacialExpression.FRIENDLY, "Yes, she is safe at home now.").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "That's good to hear, she helped us a lot.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.TED_REHNISON_721)

}
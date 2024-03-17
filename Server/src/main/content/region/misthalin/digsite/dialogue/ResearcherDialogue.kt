package content.region.misthalin.digsite.dialogue

import content.region.misthalin.digsite.quest.thedigsite.TheDigSite
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class ResearcherDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (isQuestComplete(player, TheDigSite.questName)){
            when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Hello there. What are you doing here?").also { stage++ }
                1 -> playerl(FacialExpression.FRIENDLY, "Just looking around at the moment.").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "Well, feel free to talk to me should you come across anything you can't figure out.").also {
                    stage = END_DIALOGUE
                }
            }
        } else {
            when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Hello there. What are you doing here?").also { stage++ }
                1 -> playerl(FacialExpression.FRIENDLY, "Just looking around at the moment.").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "Well, feel free to talk to me should you come across anything you can't figure out.").also {
                    stage = END_DIALOGUE
                }
            }
        }
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return ResearcherDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RESEARCHER_4568)
    }
}
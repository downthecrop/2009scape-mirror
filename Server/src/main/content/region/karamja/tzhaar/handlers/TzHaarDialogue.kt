package content.region.karamja.tzhaar.handlers

import core.api.openNpcShop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class TzHaarDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.HALF_GUILTY, "Can I help you JalYt-Ket-${player.username}?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                Topic(FacialExpression.HALF_GUILTY, "What do you have to trade?", 10, true),
                Topic(FacialExpression.HALF_GUILTY, "What did you call me?", 20),
                Topic(FacialExpression.HALF_GUILTY, "No I'm fine thanks.", END_DIALOGUE),
            )
            10 -> end().also { openNpcShop(player, npc.id) }
            20 -> npcl(FacialExpression.HALF_GUILTY, "Are you not JalYt-Ket?").also { stage++ }
            21 -> showTopics(
                Topic(FacialExpression.HALF_GUILTY, "What's a 'JalYt-Ket'?", 22),
                Topic(FacialExpression.HALF_GUILTY, "I guess so...", 25),
                Topic(FacialExpression.HALF_GUILTY, "No I'm not!", END_DIALOGUE)
            )

            22 -> npcl(FacialExpression.HALF_GUILTY, "That what you are... you tough and strong no?").also { stage++ }
            23 -> playerl(FacialExpression.HALF_GUILTY, "Well yes I suppose I am...").also { stage++ }
            24 -> npcl(FacialExpression.HALF_GUILTY, "Then you JalYt-Ket!").also { stage = END_DIALOGUE }

            25 -> npcl(FacialExpression.HALF_GUILTY, "Well then, no problems.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TZHAAR_HUR_TEL_2620, NPCs.TZHAAR_HUR_LEK_2622, NPCs.TZHAAR_MEJ_ROH_2623)
    }

}
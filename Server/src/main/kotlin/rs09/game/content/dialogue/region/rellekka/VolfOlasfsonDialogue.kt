package rs09.game.content.dialogue.region.rellekka

import api.isQuestComplete
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

/**
 * @author qmqz
 */

@Initializable
class VolfOlasfsonDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Fremennik Trials")) {
            npc(FacialExpression.ANNOYED, "Sorry, outlander, but I have things to be doing.").also { stage = END_DIALOGUE }
        } else if (isQuestComplete(player, "Fremennik Trials") && !isQuestComplete(player, "Olaf's Quest")) {
            npc(FacialExpression.FRIENDLY, "Hello there. Enjoying the view?").also { stage = 0 }
        } else if (isQuestComplete(player, "Fremennik Trials") && isQuestComplete(player, "Olaf's Quest")) {
            npcl(FacialExpression.ASKING, "Hello again, friend! Does my father send any word... or treasures like before?").also { stage = 10 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(FacialExpression.FRIENDLY, "Yes I am. You have a lovely yurt.").also { stage++ }
            1 -> npc(FacialExpression.FRIENDLY, "Thanks! I exercise it regularly.").also { stage = END_DIALOGUE }

            10 -> playerl(FacialExpression.HALF_GUILTY, "Not today, but if he does, you will be the first to know.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return VolfOlasfsonDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VOLF_OLAFSON_3695)
    }
}

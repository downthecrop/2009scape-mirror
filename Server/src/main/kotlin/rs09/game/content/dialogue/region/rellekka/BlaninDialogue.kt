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
class BlaninDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Fremennik Trials")) {
            player(FacialExpression.FRIENDLY, "Good day.").also { stage = 0 }
        } else {
            player(FacialExpression.FRIENDLY, "That's one less thing to worry about.").also { stage = 10 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.FRIENDLY, "Good day to you, sir.").also { stage = END_DIALOGUE }
            10 -> npc(FacialExpression.FRIENDLY, "Glad I could help.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BlaninDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLANIN_2940)
    }
}

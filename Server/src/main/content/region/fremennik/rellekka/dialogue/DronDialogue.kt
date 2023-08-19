package content.region.fremennik.rellekka.dialogue

import core.api.isQuestComplete
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

/**
 * @author qmqz
 */

@Initializable
class DronDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Making History")) {
            player(FacialExpression.FRIENDLY, "Excuse me.").also { stage = 0 }
        } else {
            player(FacialExpression.FRIENDLY, "Excuse me.").also { stage = 10 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.ANNOYED, "Leave me or I'll destroy you!").also { stage = END_DIALOGUE }
            10 -> npc(FacialExpression.ANNOYED, "You have your answers, now go away!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DronDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DRON_2939)
    }
}

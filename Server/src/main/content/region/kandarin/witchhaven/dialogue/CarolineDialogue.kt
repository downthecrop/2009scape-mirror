package content.region.kandarin.witchhaven.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class CarolineDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello again.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Hello traveller, how are you?").also { stage++ }
            1 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Not bad thanks, yourself?").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "I'm good. Busy as always looking after Kent and Kennith but no complaints.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return CarolineDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAROLINE_696)
    }
}
